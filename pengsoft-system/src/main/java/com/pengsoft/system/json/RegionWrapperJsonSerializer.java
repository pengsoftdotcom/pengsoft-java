package com.pengsoft.system.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class RegionWrapperJsonSerializer extends JsonSerializer<RegionWrapper> {

    public void serialize(RegionWrapper wrapper, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("name", wrapper.getRegion().getName());
        gen.writeStringField("index", wrapper.getRegion().getIndex());
        gen.writeEndObject();
    }

}
