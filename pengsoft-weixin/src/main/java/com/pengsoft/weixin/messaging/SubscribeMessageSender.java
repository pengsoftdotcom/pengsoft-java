package com.pengsoft.weixin.messaging;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import com.pengsoft.support.exception.InvalidConfigurationException;
import com.pengsoft.support.util.StringUtils;
import com.pengsoft.system.domain.Message;
import com.pengsoft.system.domain.SubscribeMessage;
import com.pengsoft.system.domain.SubscribeMessageTemplate;
import com.pengsoft.system.messaging.MessageSender;
import com.pengsoft.weixin.service.WeixinMaServices;

import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage.MsgData;
import lombok.SneakyThrows;

/**
 * Weixin miniapp subscribe message sender.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Named
public class SubscribeMessageSender implements MessageSender {

    @Inject
    private WeixinMaServices services;

    @SneakyThrows
    @Override
    public void send(@NotNull Message message) {
        final var subscribeMessage = (SubscribeMessage) message;
        final var template = (SubscribeMessageTemplate) subscribeMessage.getTemplate();
        final var service = services.get(template.getAppid());
        if (service == null) {
            throw new InvalidConfigurationException("Weixin miniapp config(" + template.getAppid() + ") not found");
        }
        final var subscribeMsg = WxMaSubscribeMessage.builder()
                .toUser(subscribeMessage.getReceiver().getMpOpenid())
                .templateId(template.getTemplateId())
                .data(subscribeMessage.getParams().entrySet().stream()
                        .map(param -> new MsgData(param.getKey(), param.getValue())).toList())
                .build();
        if (StringUtils.isNotBlank(template.getPage())) {
            subscribeMsg.setPage(template.getPage());
        }
        service.getSubscribeService().sendSubscribeMsg(subscribeMsg);

    }

}
