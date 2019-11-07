package com.itzap.ebeans;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlUpdate;
import com.avaje.ebean.Transaction;
import com.itzap.common.exception.IZapException;
import com.itzap.ebeans.execptions.EBeanErrors;

public abstract class BulkEbeanHandler<T extends EbeanModelInterface, B extends EbeanModelBuilderInterface<T, B>>
        extends AbstractBulkEbeanHandler<T, B> implements SQLBulkEbeanHandler {
    protected BulkEbeanHandler(T object, T orgObject) {
        super(object, orgObject);
    }

    @Override
    public int execute(SqlUpdate updateQuery) {
        setObject(updateQuery);
        Transaction transaction = Ebean.beginTransaction();
        try {
            int rows = updateQuery.execute();
            Ebean.commitTransaction();
            return rows;
        } catch (Exception e) {
            transaction.rollback(e);
            throw new IZapException(String.format("Failed bulk query %s", updateQuery.getSql()),
                    EBeanErrors.SQL_SERVER_TRANS_ERROR, e);
        } finally {
            Ebean.endTransaction();
        }
    }
}
