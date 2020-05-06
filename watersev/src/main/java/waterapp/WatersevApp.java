package waterapp;

import org.noear.solon.XApp;
import org.noear.solon.core.XMap;
import org.noear.solon.extend.schedule.JobRunner;
import org.noear.solonjt.dso.*;
import org.noear.water.protocol.DefaultHeihei;
import org.noear.water.protocol.ProtocolHub;
import solonjt.JtRun;
import waterapp.dso.JobRunnerEx;
import waterapp.wrap.MessageQueueRedis;

/**
 * 可以按三个服务进行部署：
 *
 * -sss=tool
 * -sss=pln
 * -sss=msg (-pool=n)?
 *
 * */
public class WatersevApp {
    public static void main(String[] args) {
        XMap xMap = XMap.from(args);

        //是否有端口
        boolean has_server_port = xMap.containsKey("server.port");


        JobRunner.global = new JobRunnerEx(xMap.get("sss"));
        JtRun.init();

        XApp.start(WatersevApp.class, xMap, (x) -> {
            //有端口才开启http能力
            x.enableHttp = has_server_port;

            Config.tryInit();

            ProtocolHub.messageQueue = MessageQueueRedis.singleton();
            ProtocolHub.heihei = DefaultHeihei.singleton();

            x.sharedAdd("cache", Config.cache_data);
            x.sharedAdd("XFun", JtFun.g);
            x.sharedAdd("XMsg", JtMsg.g);
            x.sharedAdd("XUtil", JtUtil.g);
            x.sharedAdd("XLock", JtLock.g);
        });

        JtRun.xfunInit();
    }
}