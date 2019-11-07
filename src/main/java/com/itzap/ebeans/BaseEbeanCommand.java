package com.itzap.ebeans;

import com.avaje.ebean.SqlQuery;
import com.itzap.common.exception.IZapException;
import com.itzap.rxjava.command.AbstractCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseEbeanCommand<T, C extends BaseEbeanCommand> extends AbstractCommand<T, C> {
    private static final Logger LOG = LoggerFactory.getLogger(BaseEbeanCommand.class);

    private final SQLEbeanHandler<T> responseHandler;
    private final EbeanErrorHandler<SqlQuery> errorHandler;

    public BaseEbeanCommand(String name,
                            SQLEbeanHandler<T> responseHandler) {
        this(name, responseHandler, new DefaultEbeanErrorHandler<SqlQuery>());
    }


    public BaseEbeanCommand(String name,
                            SQLEbeanHandler<T> responseHandler,
                            EbeanErrorHandler<SqlQuery> errorHandler) {
        super(name);
        this.responseHandler = responseHandler;
        this.errorHandler = errorHandler;
    }

    protected abstract SqlQuery createSql();

    @Override
    protected T run() {
        return executeSql(createSql(), this.responseHandler);
    }

    protected T executeSql(SqlQuery sql, SQLEbeanHandler<T> responseHandler) {
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
