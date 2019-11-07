package com.itzap.ebeans.test.dao;

import com.avaje.ebean.SqlUpdate;
import com.itzap.ebeans.BaseBulkEbeanCommand;
import com.itzap.ebeans.DeleteEbeanHandler;
import com.itzap.ebeans.QueryParameter;
import com.itzap.ebeans.Servers;
import io.reactivex.Observable;

public class AbstractServiceDao {
    protected final Servers.Server server;

    public AbstractServiceDao(Servers.Server server) {
        this.server = server;
    }

    protected <T> Observable<Integer> deleteItem(QueryParameter<T> id,
                                                 String commandName,
                                                 String deleteSql) {
        return new BaseBulkEbeanCommand<BaseBulkEbeanCommand>(commandName,
                new DeleteEbeanHandler<>(id)) {
            @Override
            protected BaseBulkEbeanCommand getThis() {
                return this;
            }
            @Override
            protected SqlUpdate createSql() {
                return server.getServer().createSqlUpdate(deleteSql);
            }
        }.toObservable();
    }
}
