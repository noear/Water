package waterpaas;

import org.noear.solon.XApp;
import org.noear.solon.core.XMethod;
import org.noear.solonjt.dso.*;
import org.noear.water.WaterClient;
import org.noear.water.utils.Timecount;
import solonjt.JtRun;
import waterpaas.controller.AppHandler;
import waterpaas.controller.FrmInterceptor;


public class WaterpaasApp {
    public static void main(String[] args) {
        JtRun.init();

        XApp app = XApp.start(WaterpaasApp.class, args, (x) -> {
            Config.tryInit();

            x.sharedAdd("cache",Config.cache_data);
            x.sharedAdd("XFun", JtFun.g);
            x.sharedAdd("XMsg", JtMsg.g);
            x.sharedAdd("XUtil", JtUtil.g);
            x.sharedAdd("XLock", JtLock.g);
        });

        app.before("**", XMethod.GET, FrmInterceptor.g());
        app.before("**", XMethod.POST, FrmInterceptor.g());

        //文件代理
        app.all("**", AppHandler.g());

        JtRun.xfunInit();


        //添加性能记录
        app.before("**",XMethod.HTTP,-1,(c)->{
            c.attrSet("_timecount", new Timecount().start());
        });
        app.after("**", XMethod.HTTP,(c)->{
            Timecount timecount = c.attr("_timecount", null);

            if (timecount == null || c.status() == 404) {
                return;
            }

            String node = WaterClient.localServiceHost();

            WaterClient.Track.track("water-paas", "paas", c.path(), timecount.stop().milliseconds(), node);
        });
    }
}
