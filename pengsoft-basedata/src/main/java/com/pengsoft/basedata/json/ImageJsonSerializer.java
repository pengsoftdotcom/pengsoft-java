package com.pengsoft.basedata.json;

import java.io.IOException;
import java.util.Base64;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ImageJsonSerializer extends JsonSerializer<byte[]> {

    @Override
    public void serialize(final byte[] value, final JsonGenerator gen, final SerializerProvider serializers)
            throws IOException {
        gen.writeString("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(value));
    }

}
