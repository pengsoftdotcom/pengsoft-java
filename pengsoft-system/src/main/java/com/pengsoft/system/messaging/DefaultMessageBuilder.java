package com.pengsoft.system.messaging;

import java.util.List;
import java.util.Map;

import javax.inject.Named;

import com.pengsoft.system.domain.Message;

/**
 * Default message builder.
 */
@Named
public class DefaultMessageBuilder implements MessageBuilder {

    @Override
    public Map<String, List<Message>> build(Object[] args, Object result, String[] types) {
        return Map.of();
    }

}
