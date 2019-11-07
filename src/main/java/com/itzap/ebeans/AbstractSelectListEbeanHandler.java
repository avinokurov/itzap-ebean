package com.itzap.ebeans;

import com.avaje.ebean.SqlQuery;

import java.util.List;

public abstract class AbstractSelectListEbeanHandler<T extends EbeanModelInterface, B extends EbeanModelBuilderInterface<T, B>>
        extends AbstractListSQLEbeanHandler<T, B> implements ListSQLEbeanHandler<T> {
    public AbstractSelectListEbeanHandler(Class<T> clazz) {
        super(clazz);
    }

    @Override
    public List<T> execute(SqlQuery sql) {
        return toObject(sql.findList());
    }
}
