package com.itzap.ebeans;

import com.avaje.ebean.SqlQuery;

public abstract class AbstractSelectEbeanHandler<T extends EbeanModelInterface, B extends EbeanModelBuilderInterface<T, B>>
        extends AbstractEbeanHandler<T, B> implements SQLEbeanHandler<T> {
    public AbstractSelectEbeanHandler(Class<T> clazz) {
        super(clazz);
    }

    @Override
    public T execute(SqlQuery sql) {
        return toObject(sql.findUnique());
    }
}
