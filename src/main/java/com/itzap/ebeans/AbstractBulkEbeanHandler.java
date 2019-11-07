package com.itzap.ebeans;

import com.avaje.ebean.SqlUpdate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itzap.common.exception.IZapException;
import com.itzap.ebeans.execptions.EBeanErrors;

import java.io.IOException;
import java.util.Objects;

public abstract class AbstractBulkEbeanHandler<T extends EbeanModelInterface, B extends EbeanModelBuilderInterface<T, B>> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    protected final T object;
    protected final T orgObject;

    AbstractBulkEbeanHandler(T object,
                             T orgObject) {
        this.object = object;
        this.orgObject = orgObject;
    }

    protected void setObject(SqlUpdate updateQuery) {
        T thisObject = this.object;
        try {
            B builder = rowProperties(updateQuery, thisObject);

            if (builder != null) {
                thisObject = builder
                        .merge(this.orgObject)
                        .build();
                String obj = OBJECT_MAPPER.writeValueAsString(thisObject);
                updateQuery.setParameter("object", obj);
            }
            if (thisObject.getId() != null) {
                updateQuery.setParameter("id", thisObject.getId());
            }
        } catch (IOException e) {
            throw new IZapException(
                    String.format("JSON mapping error from object: %s",
                            Objects.toString(thisObject)), EBeanErrors.SQL_SERVER_JSON_MAPPING, e);
        }
    }

    protected abstract B rowProperties(SqlUpdate updateQuery, T obj);
}
