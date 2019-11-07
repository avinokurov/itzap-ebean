package com.itzap.ebeans;

import com.avaje.ebean.SqlQuery;
import com.itzap.common.exception.IZapException;
import com.itzap.rxjava.command.AbstractCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class ListBaseEbeanCommand<T, C extends ListBaseEbeanCommand> extends AbstractCommand<List<T>, C> {
    private static final Logger LOG = LoggerFactory.getLogger(ListBaseEbeanCommand.class);

    private final ListSQLEbeanHandler<T> listResponseHandler;
    private final EbeanErrorHandler<SqlQuery> errorHandler;

    public ListBaseEbeanCommand(String name,
                                ListSQLEbeanHandler<T> listResponseHandler) {
        this(name, listResponseHandler, new DefaultEbeanErrorHandler<SqlQuery>());
    }

    public ListBaseEbeanCommand(String name,
                                ListSQLEbeanHandler<T> listResponseHandler,
                                EbeanErrorHandler<SqlQuery> errorHandler) {
        super(name);
        this.listResponseHandler = listResponseHandler;
        this.errorHandler = errorHandler;
    }

    protected abstract SqlQuery createSql();

    @Override
    protected List<T> run() {
        return executeSql(createSql(), this.listResponseHandler);
    }

    protected List<T> executeSql(SqlQuery sql, ListSQLEbeanHandler<T> responseHandler) {
        try {
            LOG.info("command='{}',sql='{}'", this.getName(), sql);

            return responseHandler.execute(sql);
        } catch (IZapException ex) {
            IZapException exception = this.errorHandler.handle(sql, ex);
            if (exception != null) {
                throw exception;
            }
            return null;
        } catch (Exception ex) {
            IZapException exception = this.errorHandler.handle(sql, ex);
            if (exception != null) {
                throw exception;
            }
            return null;
        }
    }
}
