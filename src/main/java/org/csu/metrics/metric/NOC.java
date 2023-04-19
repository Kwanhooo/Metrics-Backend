package org.csu.metrics.metric;

import org.csu.metrics.core.CKNumber;
import org.csu.metrics.core.CKReport;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class NOC extends ASTVisitor implements Metric {

    private CKReport report;
    private NOCExtras extras;

    public NOC(NOCExtras extras) {
        this.extras = extras;
    }

    @Override
    public boolean visit(TypeDeclaration node) {
        ITypeBinding binding = node.resolveBinding();
        ITypeBinding father = binding.getSuperclass();
        if (father != null) {
            CKNumber fatherCk = report.getByClassName(father.getBinaryName());
            if (fatherCk != null) fatherCk.incNoc();
            else extras.plusOne(father.getBinaryName());
        }

        return false;
    }

    @Override
    public void execute(CompilationUnit cu, CKNumber number, CKReport report) {
        this.report = report;
        cu.accept(this);
    }

    @Override
    public void setResult(CKNumber result) {
    }
}
