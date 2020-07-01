package org.noear.water.dso;

import org.noear.water.WaterClient;
import org.noear.water.log.LogEvent;
import org.noear.water.utils.TaskUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 写入时，先写到队列
 * 提交时，每次提交100条；消费完后暂停1秒
 *
 * */
public class LogPipeline implements TaskUtils.ITask {
    private static LogPipeline singleton = new LogPipeline();

    public static LogPipeline singleton() {
        return singleton;
    }

    //
    //
    //

    private LogPipeline() {
        TaskUtils.run(this);
    }

    Queue<LogEvent> queueLocal = new LinkedBlockingQueue<>(1024);

    public void add(LogEvent event) {
        queueLocal.add(event);
    }


    private int packetSize = 100;
    private long interval = 1000;
    @Override
    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        if (interval >= 1000) {
            this.interval = interval;
        }
    }

    public void setPacketSize(int packetSize) {
        if (packetSize >= 100) {
            this.packetSize = packetSize;
        }
    }

    @Override
    public void exec() throws Exception {

        while (true) {
            List<LogEvent> list = new ArrayList<>(packetSize);

            collectDo(list);

            if (list.size() > 0) {
                WaterClient.Log.appendAll(list, true);
            } else {
                break;
            }
        }
    }

    private void collectDo(List<LogEvent> list) {
        //收集最多100条日志
        //
        int count = 0;
        while (true) {
            LogEvent log = queueLocal.poll();

            if (log == null) {
                break;
            } else {
                list.add(log);
                count++;

                if (count == packetSize) {
                    break;
                }
            }
        }
    }
}
