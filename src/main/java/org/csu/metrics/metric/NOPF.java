package org.csu.metrics.metric;

import org.csu.metrics.core.CKNumber;
import org.csu.metrics.core.CKReport;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;

public class NOPF extends ASTVisitor implements Metric { // public变量数
    private int fields;

    @Override
    public boolean visit(FieldDeclaration node) {
        if (Modifier.isPublic(node.getModifiers())) {
            fields++;
        }
        return false;
    }

    @Override
    public void execute(CompilationUnit cu, CKNumber number, CKReport report) {
        cu.accept(this);
    }

    @Override
    public void setResult(CKNumber result) {
        result.setNopf(fields);
    }
}
