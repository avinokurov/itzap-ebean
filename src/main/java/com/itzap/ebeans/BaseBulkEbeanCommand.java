package com.itzap.ebeans;

import com.avaje.ebean.SqlUpdate;
import com.itzap.common.exception.IZapException;
import com.itzap.rxjava.command.AbstractCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseBulkEbeanCommand<C extends BaseBulkEbeanCommand> extends AbstractCommand<Integer, C> {
    private static final Logger LOG = LoggerFactory.getLogger(BaseBulkEbeanCommand.class);

    private final SQLBulkEbeanHandler responseHandler;
    private final EbeanErrorHandler<SqlUpdate> errorHandler;

    public BaseBulkEbeanCommand(String name,
                                SQLBulkEbeanHandler responseHandler) {
        this(name, responseHandler, new DefaultEbeanErrorHandler<>());
    }


    public BaseBulkEbeanCommand(String name,
                                SQLBulkEbeanHandler responseHandler,
                                EbeanErrorHandler<SqlUpdate> errorHandler) {
        super(name);
        this.responseHandler = responseHandler;
        this.errorHandler = errorHandler;
    }

    protected abstract SqlUpdate createSql();

    @Override
    protected Integer run() {
        return executeSql(createSql(), this.responseHandler);
    }

    protected Integer executeSql(SqlUpdate sql, SQLBulkEbeanHandler responseHandler) {
        try {
            LOG.info("command='{}',sql='{}'", this.getName(), sql);
            return responseHandler.execute(sql);
        } catch (IZapException ex) {
            IZapException exception = this.errorHandler.handle(sql, ex);
            if (exception != null) {
                throw exception;
            }
            return 0;
        } catch (Exception ex) {
            IZapException exception = this.errorHandler.handle(sql, ex);
            if (exception != null) {
                throw exception;
            }
            return 0;
        }
    }
}
