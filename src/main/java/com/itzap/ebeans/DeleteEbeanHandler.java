package com.itzap.ebeans;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlUpdate;
import com.avaje.ebean.Transaction;
import com.itzap.common.exception.IZapException;
import com.itzap.ebeans.execptions.EBeanErrors;

public class DeleteEbeanHandler<T> implements SQLBulkEbeanHandler {
    private final QueryParameter<T> id;

    public DeleteEbeanHandler(QueryParameter<T> id) {
        this.id = id;
    }

    @Override
    public int execute(SqlUpdate updateQuery) {
        Transaction transaction = Ebean.beginTransaction();
        try {
            updateQuery.setParameter(this.id.getName(), this.id.getValue());
            int rows = updateQuery.execute();
            Ebean.commitTransaction();
            return rows;
        } catch (Exception e) {
            transaction.rollback(e);
            throw new IZapException(String.format("Failed delete query %s", updateQuery.getSql()),
                    EBeanErrors.SQL_SERVER_TRANS_ERROR, e);
        } finally {
            Ebean.endTransaction();
        }
    }
}
