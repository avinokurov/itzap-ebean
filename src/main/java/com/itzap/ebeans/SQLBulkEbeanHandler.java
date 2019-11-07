package com.itzap.ebeans;

import com.avaje.ebean.SqlUpdate;

public interface SQLBulkEbeanHandler {
    int execute(SqlUpdate sql);
}
