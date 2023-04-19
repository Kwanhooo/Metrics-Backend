package org.csu.metrics.core;

import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.csu.metrics.metric.*;
import org.csu.metrics.util.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class CK {

    private static final int MAX_AT_ONCE;

    static {
        String jdtMax = System.getProperty("jdt.max");
        if (jdtMax != null) {
            MAX_AT_ONCE = Integer.parseInt(jdtMax);
        } else {
            long maxMemory = Runtime.getRuntime().maxMemory() / (1 << 20); // in MiB

            if (maxMemory >= 2000) MAX_AT_ONCE = 400;
            else if (maxMemory >= 1500) MAX_AT_ONCE = 300;
            else if (maxMemory >= 1000) MAX_AT_ONCE = 200;
            else if (maxMemory >= 500) MAX_AT_ONCE = 100;
            else MAX_AT_ONCE = 25;
        }
    }

    private final NOCExtras extras;

    public List<Callable<Metric>> pluggedMetrics;
    private static Logger log = Logger.getLogger(CK.class);

    public CK() {
        this.pluggedMetrics = new ArrayList<>();
        this.extras = new NOCExtras();
    }

    public CK plug(Callable<Metric> metric) {
        this.pluggedMetrics.add(metric);
        return this;
    }

    public CKReport calculate(String path) {
        String[] srcDirs = FileUtils.getAllDirs(path);
        String[] javaFiles = FileUtils.getAllJavaFiles(path);
//		log.info("Found " + javaFiles.length + " java files");

        MetricsExecutor storage = new MetricsExecutor(() -> metrics());

        List<List<String>> partitions = Lists.partition(Arrays.asList(javaFiles), MAX_AT_ONCE);
//		log.info("Max partition size: " + MAX_AT_ONCE + ", total partitions=" + partitions.size());

        for (List<String> partition : partitions) {
//			log.info("Next partition");
            ASTParser parser = ASTParser.newParser(AST.JLS8);

            parser.setResolveBindings(true);
            parser.setBindingsRecovery(true);

            Map<?, ?> options = JavaCore.getOptions();
            JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
            parser.setCompilerOptions(options);
            parser.setEnvironment(null, srcDirs, null, true);
            parser.createASTs(partition.toArray(new String[partition.size()]), null, new String[0], storage, null);
        }

//		log.info("Finished parsing");
        CKReport report = storage.getReport();
        extras.update(report);
        return report;
    }

    private List<Metric> metrics() {
        List<Metric> all = defaultMetrics();
        all.addAll(userMetrics());

        return all;
    }

    private List<Metric> defaultMetrics() {
        return new ArrayList<>(Arrays.asList(new DIT(), new NOC(extras), new WMC(), new CBO(), new LCOM(), new RFC(), new NOM(),
                new NOF(), new NOPF(), new NOSF(),
                new NOPM(), new NOSM(), new NOSI()));
    }

    private List<Metric> userMetrics() {
        try {
            List<Metric> userMetrics = new ArrayList<Metric>();

            for (Callable<Metric> metricToBeCreated : pluggedMetrics) {
                userMetrics.add(metricToBeCreated.call());
            }

            return userMetrics;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
