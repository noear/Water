package waterapi.dso.interceptor;

import org.noear.solon.annotation.XInterceptor;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XMethod;
import org.noear.water.WW;
import org.noear.water.track.TrackBuffer;
import org.noear.water.utils.Timecount;
import waterapi.Config;
import waterapi.dso.FromUtils;

@XInterceptor
public class TrackInterceptor {
    @XMapping(value = "**", before = true, index = -1)
    public void before(XContext c) {
        //
        //不记录，检测的性能
        //
        if (WW.path_run_check.equals(c.path()) == false) {
            c.attrSet("timecount", new Timecount().start());
        }
    }

    @XMapping(value = "**", after = true, index = 99)
    public void after(XContext c) {
        Timecount timecount = c.attr("timecount", null);

        if (timecount != null) {
            long _times = timecount.stop().milliseconds();
            String _node = Config.getLocalHost();
            String _from = FromUtils.getFrom(c);

            TrackBuffer.singleton().append(Config.water_service_name, "cmd", c.path(), _times, _node, _from);
        }
    }
}
