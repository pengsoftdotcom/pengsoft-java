package com.pengsoft.basedata.json;

import java.io.IOException;
import java.util.Base64;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.pengsoft.support.util.StringUtils;

public class ImageJsonDeserializer extends JsonDeserializer<byte[]> {

    @Override
    public byte[] deserialize(JsonParser jp, DeserializationContext ctx) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        String text = node.asText();
        if (StringUtils.isBlank(text)) {
            return new byte[] {};
        } else {
            return Base64.getDecoder().decode(text.split(",")[1]);
        }
    }

}
