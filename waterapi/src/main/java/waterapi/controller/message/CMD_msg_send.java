package waterapi.controller.message;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Result;
import org.noear.solon.extend.validation.annotation.NotEmpty;
import org.noear.solon.extend.validation.annotation.Whitelist;
import org.noear.water.WW;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.track.TrackBuffer;
import org.noear.water.utils.DisttimeUtils;
import waterapi.controller.UapiBase;
import waterapi.dso.db.DbWaterMsgApi;
import waterapi.dso.interceptor.Logging;
import waterapi.models.TopicModel;

import java.util.Date;

/**
 * 消息发送
 *
 * @author noear
 * @since 2017.07
 * Update time 2020.09
 */
@Logging
@Whitelist
@Controller
public class CMD_msg_send extends UapiBase {
    /**
     * @param key       消息key（派发时会回传）
     * @param topic     消息主题
     * @param message   消息内容
     * @param plan_time 分发时间(yyyy-MM-dd HH:mm:ss)
     * @param tags      查询标签
     */
    @NotEmpty({"topic", "message"})
    @Mapping("/msg/send/")
    public Result cmd_exec(Context ctx, String key, String topic, String message, String plan_time, String tags) throws Exception {

        Date plan_time2 = DisttimeUtils.parse(plan_time);
        String trace_id = ctx.header(WW.http_header_trace);

        if (Utils.isEmpty(trace_id)) {
            trace_id = Utils.guid();
        }

        TopicModel topicModel = DbWaterMsgApi.getTopicById(topic);

        TrackBuffer.singleton().append("watermsg", "topic", topic, 1);

        long msg_id = ProtocolHub.messageSource().addMessage(key, trace_id, tags, topicModel.topic_id, topic, message, plan_time2, false);

        if (msg_id > 0) {
            //非定时消息，直接转队列
//            if (plan_time2 == null) {
//                ProtocolHub.messageQueue.push(String.valueOf(msg_id));
//            }

            return Result.succeed();
        } else {
            return Result.failure();
        }
    }
}
