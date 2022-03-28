package com.pengsoft.basedata.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ImageJsonSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(final String value, final JsonGenerator gen, final SerializerProvider serializers)
            throws IOException {
        gen.writeString("data:image/jpeg;base64," + value);
    }

}
