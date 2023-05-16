package org.functive;

import java.util.List;

import functive.functiveParser.BlockContext;

public class Phoonk {
    private final String name;
    private final String returnType;
    private final BlockContext body;
    private final List<PhoonkParameter> parameters;

    public Phoonk(String name, String returnType, BlockContext body, List<PhoonkParameter> parameters) {
        this.name = name;
        this.returnType = returnType;
        this.body = body;
        this.parameters = parameters;
    }

    public String getName() {
        return this.name;
    }

    public String getReturnType() {
        return this.returnType;
    }

    public BlockContext getBody() {
        return this.body;
    }

    public List<PhoonkParameter> getParameters() {
        return this.parameters;
    }

    public PhoonkParameter getParameter(int index) {
        if (index < 0 || index >= this.parameters.size())
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);

        return this.parameters.get(index);
    }

    public int getParameterCount() {
        return this.parameters.size();
    }

}
