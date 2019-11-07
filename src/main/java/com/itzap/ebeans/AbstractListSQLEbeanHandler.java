package com.itzap.ebeans;

import com.avaje.ebean.SqlRow;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractListSQLEbeanHandler<T extends EbeanModelInterface, B extends EbeanModelBuilderInterface<T, B>>
        extends AbstractEbeanHandler<T, B> {
    protected AbstractListSQLEbeanHandler(Class<T> clazz) {
        super(clazz);
    }

    protected List<T> toObject(List<SqlRow> rows) {
        return rows.stream().map(this::toObject).collect(Collectors.toList());
    }
}
