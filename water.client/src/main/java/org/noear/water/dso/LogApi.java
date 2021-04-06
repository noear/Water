package org.noear.water.dso;

import org.noear.snack.ONode;
import org.noear.water.WW;
import org.noear.water.WaterAddress;
import org.noear.water.WaterClient;
import org.noear.water.WaterSetting;
import org.noear.water.log.Level;
import org.noear.water.log.LogEvent;
import org.noear.water.log.Logger;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.GzipUtils;
import org.noear.water.utils.TextUtils;
import org.noear.water.utils.ThrowableUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日志服务接口
 * */
public class LogApi {

    protected final ApiCaller apiCaller;
    public LogApi(){
        apiCaller = new ApiCaller(WaterAddress.getLogApiUrl());
    }


    /**
     * 添加日志
     */
    public void append(String logger, Level level, Map<String, Object> map) {
        append(logger, level,
                (String) map.get("tag"),
                (String) map.get("tag1"),
                (String) map.get("tag2"),
                (String) map.get("tag3"),
                (String) map.get("summary"),
                map.get("content"));
    }

    /**
     * 添加日志
     */
    public void append(String logger, Level level, String summary, Object content) {
        append(logger, level, null, null, null, null, summary, content, true);
    }

    /**
     * 添加日志
     */
    public void append(String logger, Level level, String tag, String summary, Object content) {
        append(logger, level, tag, null, null, null, summary, content, true);
    }

    /**
     * 添加日志
     */
    public void append(String logger, Level level, String tag, String tag1, String summary, Object content) {
        append(logger, level, tag, tag1, null, null, summary, content, true);
    }

    /**
     * 添加日志
     */
    public void append(String logger, Level level, String tag, String tag1, String tag2, String summary, Object content) {
        append(logger, level, tag, tag1, tag2, null, summary, content, true);
    }

    /**
     * 添加日志
     */
    public void append(String logger, Level level, String tag, String tag1, String tag2, String tag3, String summary, Object content) {
        append(logger, level, tag, tag1, tag2, tag3, summary, content, true);
    }

    /**
     * 添加日志
     *
     * @param logger  日志接收器
     * @param level   等级
     * @param tag     标签
     * @param tag1    标签1
     * @param tag2    标签2
     * @param tag3    标签3
     * @param summary 简介
     * @param content 内容
     * @param async   是否异步提交
     */
    public void append(String logger, Level level, String tag, String tag1, String tag2, String tag3, String summary, Object content, boolean async) {
        String trace_id = WaterClient.waterTraceId();

//        if (async) {
//            //WaterSetting.pools.submit(() -> {
//                appendDo(logger, trace_id, level, tag, tag1, tag2, tag3, summary, content);
//            //});
//        } else {
            appendDo(logger, trace_id, level, tag, tag1, tag2, tag3, summary, content);
//        }
    }

    private void appendDo(String logger, String trace_id,Level level, String tag, String tag1, String tag2, String tag3, String summary, Object content) {
        if (TextUtils.isEmpty(logger)) {
            return;
        }

        if (logger.indexOf(".") > 0) {
            return;
        }

        Datetime datetime = Datetime.Now();

        LogEvent log = new LogEvent();

        log.logger = logger;
        log.level = level.code;
        log.tag = tag;
        log.tag1 = tag1;
        log.tag2 = tag2;
        log.tag3 = tag3;
        log.summary = summary;
        log.content = content;

        log.trace_id = trace_id;
        log.from = WaterClient.localServiceHost();
        log.thread_name = Thread.currentThread().getName();

        log.log_date = datetime.getDate();
        log.log_fulltime = datetime.getFulltime();

        LogPipeline.singleton().add(log);
    }

    private String content_str(Object content) {
        if (content != null) {
            if (content instanceof String) {
                //处理字符串
                return (String) content;
            } else if (content instanceof Throwable) {
                //处理异常
                return ThrowableUtils.getString((Throwable) content);
            } else {
                //处理其它对象（进行json）
                return ONode.load(content).toJson();
            }
        }

        return null;
    }

    public void appendAll(List<LogEvent> list, boolean async) {
        if (async) {
            WaterSetting.pools.submit(() -> {
                appendAllDo(list);
            });
        } else {
            appendAllDo(list);
        }
    }

    private void appendAllDo(List<LogEvent> list) {
        if (list == null || list.size() == 0) {
            return;
        }

        for (LogEvent log : list) {
            log.content = content_str(log.content);
        }

        String json = ONode.stringify(list);

        try {
            if (WaterSetting.water_logger_gzip()) {
                apiCaller.postBody("/log/add2/", GzipUtils.gZip(json), WW.mime_glog);
            } else {
                Map<String,String> map = new HashMap<>();
                map.put("list",json);

                apiCaller.post("/log/add2/",map);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
