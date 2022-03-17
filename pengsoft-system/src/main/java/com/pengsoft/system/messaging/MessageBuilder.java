package com.pengsoft.system.messaging;

import java.util.List;
import java.util.Map;

import com.pengsoft.system.domain.Message;

/**
 * The message builder.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface MessageBuilder {

    /**
     * Build messages.
     * 
     * @param args   The join point args
     * @param result The join point return value.
     * @param types  The message type.
     * @return The key is the message type, value is the message collection.
     */
    Map<String, List<Message>> build(Object[] args, Object result, String[] types);

}
