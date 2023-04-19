package org.csu.metrics.metric;

import org.csu.metrics.core.CKNumber;
import org.csu.metrics.core.CKReport;
import org.eclipse.jdt.core.dom.*;

import java.util.HashSet;
import java.util.List;

public class RFC extends ASTVisitor implements Metric {

    private HashSet<String> methodInvocations = new HashSet<String>();

    public boolean visit(MethodInvocation node) {
        IMethodBinding binding = node.resolveMethodBinding();
        count(node.getName() + "/" + arguments(node.arguments()), binding);

        return super.visit(node);
    }

    private String arguments(List<?> arguments) {
        if (arguments == null || arguments.isEmpty()) return "0";
        return "" + arguments.size();
    }

    private void count(String methodName, IMethodBinding binding) {
        if (binding != null) {
            String method = getMethodName(binding);
            methodInvocations.add(method);
        } else {
            methodInvocations.add(methodName);
        }
    }

    public boolean visit(SuperMethodInvocation node) {
        IMethodBinding binding = node.resolveMethodBinding();
        count(node.getName() + "/" + arguments(node.arguments()), binding);

        return super.visit(node);
    }

    private String getMethodName(IMethodBinding binding) {

        String argumentList = "";
        ITypeBinding[] args = binding.getParameterTypes();
        for (ITypeBinding arg : args) {
            argumentList += arg.getName();
        }
        String method = binding.getDeclaringClass().getQualifiedName() + "." + binding.getName() + "/" + binding.getTypeArguments().length + "[" + argumentList + "]";
        return method;
    }

    @Override
    public void execute(CompilationUnit cu, CKNumber number, CKReport report) {
        cu.accept(this);
    }

    @Override
    public void setResult(CKNumber result) {
        result.setRfc(methodInvocations.size());

    }

}
