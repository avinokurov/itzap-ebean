package com.itzap.ebeans;

import com.avaje.ebean.SqlQuery;

import java.util.List;

public interface ListSQLEbeanHandler<T> {
    List<T> execute(SqlQuery sql);
}
