package com.pengsoft.system.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.pengsoft.system.domain.EmailMessage;
import com.pengsoft.system.domain.InternalMessage;
import com.pengsoft.system.domain.PushMessage;
import com.pengsoft.system.domain.SmsMessage;

/**
 * Any spring bean method with this annotation will send message after returning
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Messaging {

    String builder();

    String[] types() default { InternalMessage.TYPE, EmailMessage.TYPE, PushMessage.TYPE, SmsMessage.TYPE };

}
