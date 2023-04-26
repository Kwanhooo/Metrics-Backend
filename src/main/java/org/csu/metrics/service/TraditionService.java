package org.csu.metrics.service;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.csu.metrics.common.Constant;
import org.csu.metrics.domain.TrBean;
import org.csu.metrics.syntax.antlr4.CyclomaticComplexityVisitor;
import org.csu.metrics.syntax.grammar.JavaLexer;
import org.csu.metrics.syntax.grammar.JavaParser;
import org.csu.metrics.vm.TraditionVO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class TraditionService {
    public TrBean start(File targetFile) throws IOException {
        if (targetFile != null) {
            long fileLength = targetFile.length();
            LineNumberReader lnr = new LineNumberReader(new FileReader(targetFile));
            lnr.skip(fileLength);
            int lines = lnr.getLineNumber();
            lnr.close();

            // 处理平均值圈复杂度，方法的复杂度会打印在控制台
            JavaLexer lexer = new JavaLexer(new ANTLRFileStream(targetFile.getAbsolutePath()));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            JavaParser parser = new JavaParser(tokens);
            ParserRuleContext tree = parser.compilationUnit();

            CyclomaticComplexityVisitor mv = new CyclomaticComplexityVisitor();
            mv.visit(tree);

            // 注释百分比
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(targetFile), StandardCharsets.UTF_8));
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("//")) {
                    count++;
                } else if (line.startsWith("/**")) {
                    count++;
                    while (!br.readLine().startsWith("*/")) {
                        count++;
                    }
                    count++;
                }
            }

            return new TrBean(String.valueOf(lines), String.valueOf(mv.getAvgCC()), String.valueOf(((float) count / (float) lines) * 100) + "%");
        } else {
            System.out.println("请选择文件！");
            return null;
        }
    }

    public List<TraditionVO> handleRequest(MultipartFile[] files) {
        List<TrBean> results = new ArrayList<>();
        for (MultipartFile file : files) {
            File targetFile = new File(Constant.UPLOAD_PATH,
                    UUID.randomUUID() + Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".")));
            try {
                file.transferTo(targetFile);

                TrBean result = start(targetFile);
                result.setFile(file.getOriginalFilename());
                results.add(result);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return convertToVO(results);
    }

    private List<TraditionVO> convertToVO(List<TrBean> results) {
        List<TraditionVO> traditionVOS = new ArrayList<>();
        for (TrBean result : results) {
            result.setCp(String.valueOf((int) Math.ceil(Double.parseDouble(result.getCp()))));
            // cc 去掉最后的百分号
            result.setCc(result.getCc().substring(0, result.getCc().length() - 1));
            // cc / 100
            result.setCc(String.valueOf(Double.parseDouble(result.getCc()) / 100));
            traditionVOS.add(new TraditionVO(result.getFile(), result.getLoc(), result.getCp(), result.getCc()));
        }
        return traditionVOS;
    }
}
