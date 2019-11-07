package com.itzap.ebeans.test.model.support;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.io.IOException;

public class ISODateTimeDeserializer  extends JsonDeserializer<DateTime> {
    @Override
    public DateTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return ISODateTimeFormat.dateTimeNoMillis().withZoneUTC().parseDateTime(jp.getValueAsString());
    }
}
