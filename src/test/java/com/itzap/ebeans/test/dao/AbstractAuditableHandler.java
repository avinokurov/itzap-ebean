package com.itzap.ebeans.test.dao;

import com.itzap.ebeans.BulkEbeanHandler;
import com.itzap.ebeans.test.model.Auditable;
import org.joda.time.DateTime;

public abstract class AbstractAuditableHandler <T extends Auditable, B extends Auditable.Builder<T, B>>
        extends BulkEbeanHandler<T, B> {
    protected final Long id;

    protected AbstractAuditableHandler(T object) {
        this(null, object, null);
    }

    protected AbstractAuditableHandler(Long id, T object, T orgObject) {
        super(object, orgObject);
        this.id = id;
    }

    protected B audibalBuilder(T object, B builder) {
        DateTime createdTime = DateTime.now();

        builder
                .setId(this.id)
                .setModifiedDateTime(createdTime);

        if (this.id == null) {
            builder.setCreateDateTime(createdTime);
        } else {
            builder.setCreateDateTime(orgObject.getCreateDateTime());
        }

        return builder;
    }
}
