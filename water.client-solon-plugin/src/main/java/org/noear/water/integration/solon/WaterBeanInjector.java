package org.noear.water.integration.solon;

import org.noear.solon.core.BeanInjector;
import org.noear.solon.core.VarHolder;
import org.noear.solon.core.util.ConvertUtil;
import org.noear.water.WaterClient;
import org.noear.water.WaterProps;
import org.noear.water.WaterSetting;
import org.noear.water.annotation.Water;
import org.noear.water.log.WaterLogger;
import org.noear.water.model.ConfigM;
import org.noear.water.utils.RedisX;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;
import org.noear.weed.cache.SecondCache;

import java.util.Properties;

/**
 * 提供water注入支持
 * */
public class WaterBeanInjector implements BeanInjector<Water> {
    public static final WaterBeanInjector instance = new WaterBeanInjector();

    @Override
    public void doInject(VarHolder varH, Water anno) {
        //RPC client注入
        if(TextUtils.isEmpty(anno.value())) {
            if (varH.getType().isInterface()) {
                varH.setValue(WaterUpstream.xclient(varH.getType()));
            }
            return;
        }

        //日志注入
        if(varH.getType() == WaterLogger.class) {
            varH.setValue(WaterLogger.get(anno.value()));
            return;
        }

        //配置注入
        String[] tmp = anno.value().split("::");

        String tagKey = tmp[0];
        String arg = null;
        if(tmp.length > 1){
            arg = tmp[1];
        }


        if(TextUtils.isEmpty(tagKey)){
            return;
        }

        ConfigM cfg = WaterClient.Config.getByTagKey(tagKey);

        //DbContext
        if(DbContext.class.isAssignableFrom(varH.getType())){
            DbContext db = WaterSetting.libOfDb.get(cfg.value);
            if(db == null){
                db = cfg.getDb(true);
                WaterSetting.libOfDb.put(cfg.value,db);
            }
            varH.setValue(db);
            return;
        }

        //RedisX
        if(RedisX.class.isAssignableFrom(varH.getType())){
            String key = cfg.value +"_"+arg;

            RedisX rdx = WaterSetting.libOfRd.get(key);
            if(rdx == null){
                if(TextUtils.isEmpty(arg)){
                    rdx = cfg.getRd();
                }else{
                    rdx = cfg.getRd(Integer.parseInt(arg));
                }

                WaterSetting.libOfRd.put(key,rdx);
            }

            varH.setValue(rdx);
            return;
        }

        //ICacheServiceEx
        if(ICacheServiceEx.class.isAssignableFrom(varH.getType())) {
            ICacheServiceEx cache = WaterSetting.libOfCache.get(cfg.value);

            if (cache == null) {
                String keyHeader = WaterProps.service_name();
                int defSeconds = 300;

                ICacheServiceEx cache1 = new LocalCache(defSeconds);
                ICacheServiceEx cache2 = cfg.getCh(keyHeader, defSeconds);
                cache = new SecondCache(cache1, cache2);

                WaterSetting.libOfCache.put(cfg.value, cache);
            }

            varH.setValue(cache);
            return;
        }

        //Properties
        if(Properties.class.isAssignableFrom(varH.getType())){
            varH.setValue(cfg.getProp());
            return;
        }

        Object val2 = ConvertUtil.to(varH.getType(), cfg.value);
        varH.setValue(val2);
    }
}
