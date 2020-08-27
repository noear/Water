package org.noear.water.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;

public class MonitorUtils {
    private static MonitorStatus status = new MonitorStatus();


    public static MonitorStatus getStatus() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

        Runtime runtime = Runtime.getRuntime();

        status.memoryFree = (byteToM(runtime.freeMemory()));
        status.memoryTotal = (byteToM(runtime.totalMemory()));
        status.memoryMax = (byteToM(runtime.maxMemory()));

        status.timeStart = new Datetime(runtimeMXBean.getStartTime()).toString();
        status.timeElapsed = (runtimeMXBean.getUptime());

        status.pid = getPid();
        status.os = (System.getProperty("os.name"));
        status.threadCount = (threadMXBean.getThreadCount());


        return status;
    }

    public static long byteToM(long bytes) {
        long kb = (bytes / 1024 / 1024);
        return kb;
    }

    public static long getPid() {
        try {
            String name = ManagementFactory.getRuntimeMXBean().getName();
            String pid = name.split("@")[0];
            return Long.parseLong(pid);
        } catch (Throwable e) {
            return 0;
        }
    }
}
