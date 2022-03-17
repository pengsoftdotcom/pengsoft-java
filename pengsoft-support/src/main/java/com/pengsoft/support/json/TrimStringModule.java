package com.pengsoft.support.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.apache.commons.lang3.StringUtils;

/**
 * When doing deserialization, trim the input string.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
class TrimStringModule extends SimpleModule {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8046582030507674164L;

	TrimStringModule() {
        addDeserializer(String.class, new StdScalarDeserializer<>(String.class) {

            /**
			 * 
			 */
			private static final long serialVersionUID = 6946497522196634349L;

			@Override
            public String deserialize(final JsonParser jp, final DeserializationContext ctx) throws IOException {
                return StringUtils.trim(jp.getValueAsString());
            }

        });
    }

}
