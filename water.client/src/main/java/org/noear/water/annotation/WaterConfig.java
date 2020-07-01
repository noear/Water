package org.noear.water.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WaterConfig {
    /**
     * config tag
     * */
    String value();
}