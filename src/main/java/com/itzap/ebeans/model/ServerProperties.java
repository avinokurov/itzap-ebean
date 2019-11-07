package com.itzap.ebeans.model;

import com.itzap.common.Property;

public enum ServerProperties implements Property {
    DEFAULT("default"),
    DRIVER("driver"),
    USER("user"),
    USER_NAME("username"),
    PASSWORD("password"),
    URL("url"),
    IS_DEFAULT("isDefault"),
    PACKAGES("packages"),
    AUTO_DDL("autoDdl")
    ;

    private final String name;

    ServerProperties(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
