package org.noear.water.dso;

import org.noear.water.WaterAddress;
import org.noear.water.WaterClient;
import org.noear.water.WW;

import java.util.HashMap;
import java.util.Map;

/**
 * 通知服务接口
 *
 * @author noear
 * @since 2.0
 * */
public class NoticeApi {
    protected final ApiCaller apiCaller;
    public NoticeApi(){
        apiCaller = new ApiCaller(WaterAddress.getNoticeApiUrl());
    }

    /**
     * 嘿嘿通知（经Water服务端处理后再推送）
     *
     * @param target 手机号（多个以,隔开；@alarm 表过报警名单），例：18121212,@alarm
     */
    public String heihei(String target, String msg) {
        Map<String, String> params = new HashMap<>();
        params.put("target", target);
        params.put("msg", msg);

        try {
            return apiCaller.post("/run/push/", params);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 通知缓存更新
     */
    public void updateCache(String... cacheTags) {
        //tags以;隔开
        StringBuilder sb = new StringBuilder();
        for (String tag : cacheTags) {
            sb.append(tag).append(";");
        }

        try {
            WaterClient.Message.sendMessageAndTags(WW.msg_ucache_topic, sb.toString(), WaterClient.localService());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 通知配置更新
     */
    public void updateConfig(String tag, String name) {
        try {
            WaterClient.Message.sendMessageAndTags(WW.msg_uconfig_topic, tag + "::" + name, tag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
