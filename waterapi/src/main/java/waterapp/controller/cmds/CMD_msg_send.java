package waterapp.controller.cmds;

import waterapp.dso.DisttimeUtil;
import waterapp.dso.db.DbWaterMsgApi;

import java.util.Date;

/**
 * Created by noear on 2017/7/19.
 */
public class CMD_msg_send extends CMDBase {
    @Override
    protected void cmd_exec() throws Exception {
        String key = get("key"); //消息key //派发时会回传
        String topic = get("topic"); //订阅主题
        String message = get("message"); //消息内容
        String time = get("plan_time"); //分发时间 //yyyy-MM-dd HH:mm:ss

        if (checkParamsIsOk(key, topic, message) == false) {
            return;
        }

        //如果不需要修改，检查是否已存在
        //
        if (DbWaterMsgApi.hasMessage(key)) {
            data.set("code", 1);
            data.set("msg", "success");
            return;
        }

        Date time2 = DisttimeUtil.getDate(time, "yyyy-MM-dd HH:mm:ss");

        if (DbWaterMsgApi.addMessage(key, topic, message, time2) > 0) {
            data.set("code", 1);
            data.set("msg", "success");
            return;
        }

        data.set("code", 0);
        data.set("msg", "unknown");
    }
}
