package org.noear.water.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WaterMessage {
    /**
     * 消息主题
     * */
    String value();
}
