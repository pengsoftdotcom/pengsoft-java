package com.pengsoft.system.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.pengsoft.support.util.DateUtils;

public class CaptchaWrapperJsonSerializer extends JsonSerializer<CaptchaWrapper> {

    public void serialize(CaptchaWrapper wrapper, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeStartObject();
        gen.writeStringField("expiredAt", DateUtils.formatDateTime(wrapper.getCaptcha().getExpiredAt()));
        gen.writeEndObject();
    }

}
