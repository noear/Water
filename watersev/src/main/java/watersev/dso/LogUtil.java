package watersev.dso;

import org.noear.solon.extend.schedule.IJob;
import org.noear.solonjt.model.AFileModel;
import org.noear.water.WaterClient;
import org.noear.water.log.Level;
import org.noear.water.log.Logger;
import org.noear.water.log.WaterLogger;
import org.noear.water.utils.TextUtils;
import sun.rmi.runtime.Log;
import watersev.models.water_msg.DistributionModel;
import watersev.models.water_msg.MessageModel;

public class LogUtil {
    private static Logger log_msg =  WaterLogger.get("water_log_msg");
    private static Logger log_sev =  WaterLogger.get("water_log_sev");
    private static Logger log_paas =  WaterLogger.get("water_log_paas");

    public static void writeForMsg(MessageModel msg, DistributionModel dist, String content) {
        if (dist == null) {
            dist = new DistributionModel();
        }

        String summary = msg.msg_id + "#" + msg.dist_count + "#" + msg.topic_name + "=" + msg.content + "@" + dist._duration;


        if (TextUtils.isEmpty(dist.receive_url))
            log_msg.info( msg.topic_name, msg.msg_id + "", summary, content);
        else
            log_msg.info( msg.topic_name, msg.msg_id + "", summary, dist.receive_url + "::\r\n" + content);

//        System.out.print(msg.topic_name + "::\r\n");
//        System.out.print(content);
//        System.out.print("\r\n");
    }

    public static void writeForMsgByError(MessageModel msg, DistributionModel dist, String content) {
        if (dist == null) {
            dist = new DistributionModel();
        }

        String summary = msg.msg_id + "#" + msg.dist_count + "#" + msg.topic_name + "=" + msg.content;

        if (TextUtils.isEmpty(dist.receive_url))
            log_msg.error(msg.topic_name, msg.msg_id + "", summary, content);
        else
            log_msg.error(msg.topic_name, msg.msg_id + "", summary, dist.receive_url + "::\r\n" + content);

//        System.out.print(msg.topic_name + "::\r\n");
//        System.out.print(content);
//        System.out.print("\r\n");
    }

    public static void writeForMsgByError(MessageModel msg, Throwable ex) {
        log_msg.error(msg.topic_name, msg.msg_id + "", "", ex);

//        System.out.print(msg.topic_name + "::\r\n");
//        System.out.print(ThrowableUtils.getString(ex));
//        System.out.print("\r\n");
    }

    //==========================================================
    //
    //

    public static void planInfo(IJob tag, AFileModel plan) {
        String content = plan.path + "(" + plan.plan_count + "/" + plan.plan_max + ")执行成功";

        log_paas.info( "_plan", plan.tag, plan.path, "", "", content);
        //write(tag.getName(), plan.file_id + "", "", content);
    }

    public static void planError(IJob tag, AFileModel plan, Throwable content) {

        //log_sev.error(tag.getName(), tag1, summary, content);

        log_paas.error("_plan", plan.tag, plan.path, "", "", content);

//        System.out.print(tag + "::\r\n");
//        System.out.print(ThrowableUtils.getString(content));
//        System.out.print("\r\n");
    }



    public static void write(IJob tag, String tag1, String summary, String content) {
        write(tag.getName(), tag1, summary, content);
    }

    public static void write(String tag, String summary, String content) {
        write(tag, null, summary, content);
    }

    public static void write(String tag, String tag1, String summary, String content) {
        log_sev.info(tag, tag1, summary, content);

//        System.out.print(tag + "::\r\n");
//        System.out.print(content);
//        System.out.print("\r\n");
    }

    public static void error(IJob tag, String tag1, String summary, Throwable content) {

        log_sev.error(tag.getName(), tag1, summary, content);

//        System.out.print(tag + "::\r\n");
//        System.out.print(ThrowableUtils.getString(content));
//        System.out.print("\r\n");
    }

    public static void error(String tag, String tag1, String summary, Throwable content) {
        log_sev.error(tag, tag1, summary, content);

//        System.out.print(tag + "::\r\n");
//        System.out.print(ThrowableUtils.getString(content));
//        System.out.print("\r\n");
    }

    public static void error(String tag, String tag1, String summary, String content) {
        log_sev.error(tag, tag1, summary, content);

//        System.out.print(tag + "::\r\n");
//        System.out.print(content);
//        System.out.print("\r\n");
    }

    public static void error(String tag, String tag1, String tag2, String summary, String content) {
        log_sev.error(tag, tag1, tag2, summary, content);

//        System.out.print(tag + "::\r\n");
//        System.out.print(content);
//        System.out.print("\r\n");
    }

    public static void debug(IJob tag, String summary, String content) {

        log_sev.debug(tag.getName(), summary, content);

//        System.out.print(tag + "::\r\n");
//        System.out.print(content);
//        System.out.print("\r\n");
    }

    public static void debug(String tag, String summary, String content) {
        log_sev.debug(tag, summary, content);

//        System.out.print(tag + "::\r\n");
//        System.out.print(content);
//        System.out.print("\r\n");
    }
}
