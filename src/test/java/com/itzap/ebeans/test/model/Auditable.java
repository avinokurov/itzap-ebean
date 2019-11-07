package com.itzap.ebeans.test.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.itzap.ebeans.EbeanModelBuilderInterface;
import com.itzap.ebeans.EbeanModelInterface;
import com.itzap.ebeans.test.model.support.ISODateTimeDeserializer;
import com.itzap.ebeans.test.model.support.ISODateTimeSerializer;
import org.apache.commons.lang3.ObjectUtils;
import org.joda.time.DateTime;

public class Auditable implements EbeanModelInterface {
    private final Long id;

    @JsonSerialize(using = ISODateTimeSerializer.class)
    @JsonDeserialize(using = ISODateTimeDeserializer.class)
    private final DateTime createDateTime;

    @JsonSerialize(using = ISODateTimeSerializer.class)
    @JsonDeserialize(using = ISODateTimeDeserializer.class)
    private final DateTime modifiedDateTime;

    Auditable(Builder builder) {
        this.id = builder.id;
        this.createDateTime = ObjectUtils.defaultIfNull(builder.createDateTime,
                new DateTime());
        this.modifiedDateTime = ObjectUtils.defaultIfNull(builder.modifiedDateTime,
                new DateTime());
    }

    @Override
    public Long getId() {
        return id;
    }

    public DateTime getCreateDateTime() {
        return createDateTime;
    }

    public DateTime getModifiedDateTime() {
        return modifiedDateTime;
    }

    public static abstract class Builder<T extends Auditable,
            B extends Auditable.Builder> implements EbeanModelBuilderInterface<T, B> {
        private Long id;

        @JsonDeserialize(using = ISODateTimeDeserializer.class)
        private DateTime createDateTime;

        @JsonDeserialize(using = ISODateTimeDeserializer.class)
        private DateTime modifiedDateTime;

        protected abstract B getThis();

        public Builder() {}

        public B setCreateDateTime(DateTime createDateTime) {
            this.createDateTime = createDateTime;
            return getThis();
        }

        public B setModifiedDateTime(DateTime modifiedDateTime) {
            this.modifiedDateTime = modifiedDateTime;
            return getThis();
        }

        @Override
        public B setId(Long id) {
            this.id = id;
            return getThis();
        }

        @Override
        public B merge(T org) {
            return setId(ObjectUtils.defaultIfNull(this.id, org.getId()));
        }
    }
}
