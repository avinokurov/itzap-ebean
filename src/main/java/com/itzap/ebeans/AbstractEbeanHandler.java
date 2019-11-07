package com.itzap.ebeans;

import com.avaje.ebean.SqlRow;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.itzap.common.exception.IZapException;
import com.itzap.ebeans.execptions.EBeanErrors;

import java.io.IOException;

public abstract class AbstractEbeanHandler<T extends EbeanModelInterface, B extends EbeanModelBuilderInterface<T, B>> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final Class<T> clazz;

    protected AbstractEbeanHandler(Class<T> clazz) {
        this.clazz = clazz;
    }

    protected T toObject(SqlRow row) {
        if (row == null) {
            return null;
        }

        String strObj = row.getString("object");
        Long id = row.getLong("id");

        if (Strings.isNullOrEmpty(strObj)) {
            return from(row, null)
                    .setId(id)
                    .build();
        }
        try {
            T obj = OBJECT_MAPPER.readValue(strObj, clazz);
            return from(row, obj)
                    .setId(id)
                    .build();

        } catch (IOException e) {
            throw new IZapException(String.format("JSON mapping error from row field: %s", row.getString("object")),
                    EBeanErrors.SQL_SERVER_JSON_MAPPING, e);
        }
    }

    protected abstract B from(SqlRow row, T object);
}
