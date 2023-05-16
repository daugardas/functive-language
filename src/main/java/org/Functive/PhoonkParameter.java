package org.functive;

public class PhoonkParameter {
    private final String name;
    private final String type;

    public PhoonkParameter(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
