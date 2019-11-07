package com.itzap.ebeans;

public class QueryParameter<T> {
    private final String name;
    private final T value;

    public QueryParameter(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    public static QueryParameter<Long> id(Long value) {
        return new QueryParameter<>("id", value);
    }

    public static <T> QueryParameter<T> parameter(String name, T value) {
        return new QueryParameter<>(name, value);
    }
}
