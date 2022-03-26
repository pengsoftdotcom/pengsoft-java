package com.pengsoft.security.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Mobile number serializer.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class MobileJsonSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            gen.writeString(value);
            // if (SecurityUtils.hasAnyRole(Role.ADMIN, Role.ORG_ADMIN)) {
            // gen.writeString(value);
            // } else {
            // gen.writeString(value.substring(0, 3) + "****" + value.substring(7, 11));
            // }
        }
    }

}
