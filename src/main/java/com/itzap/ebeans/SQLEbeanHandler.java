package com.itzap.ebeans;

import com.avaje.ebean.SqlQuery;

public interface SQLEbeanHandler<T> {
    T execute(SqlQuery sql);
}
