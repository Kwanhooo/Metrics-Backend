package org.csu.metrics.metric;

import org.csu.metrics.core.CKNumber;
import org.csu.metrics.core.CKReport;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class DIT extends ASTVisitor implements Metric {
    // Object is the father of everyone!
    int dit = 1;

    @Override
    public boolean visit(TypeDeclaration node) {
        ITypeBinding binding = node.resolveBinding();
        if (binding != null) {
            calculate(binding);
        }

        return super.visit(node);
    }

    private void calculate(ITypeBinding binding) {
        ITypeBinding father = binding.getSuperclass();
        if (father != null) {
            String fatherName = father.getQualifiedName();
            if (fatherName.endsWith("Object")) return;
            dit++;

            calculate(father);
        }

    }

    @Override
    public void execute(CompilationUnit cu, CKNumber number, CKReport report) {
        cu.accept(this);
    }

    @Override
    public void setResult(CKNumber result) {
        result.setDit(dit);
    }
}
