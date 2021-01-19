package org.noear.water.integration.springboot;

import org.noear.solon.extend.springboot.EnableSolon;
import org.noear.water.annotation.Water;
import org.noear.water.integration.solon.WaterBeanInjector;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

/**
 * @author noear 2021/1/6 created
 */
@EnableSolon
@Configuration
public class AutoConfiguration extends InstantiationAwareBeanPostProcessorAdapter {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClz = bean.getClass();

        ReflectionUtils.doWithFields(beanClz, (field -> {
            Water anno = field.getAnnotation(Water.class);

            if (anno != null) {
                Object val = WaterBeanInjector.instance.build(field.getType(), anno);
                field.setAccessible(true);
                field.set(bean, val);
            }
        }));

        return bean;
    }
}
