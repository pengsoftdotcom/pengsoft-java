package com.pengsoft.system.messaging;

import java.util.List;
import java.util.Map;

import com.pengsoft.system.domain.Message;

public interface MessageBuilder {

    Map<String, List<Message>> build(Object[] paramArrayOfObject, Object paramObject, String[] paramArrayOfString);

}
