package webapp.controller.cmds;

import org.noear.snack.ONode;
import org.noear.solon.core.XContext;
import org.noear.water.tools.TextUtils;
import org.noear.water.tools.Timecount;
import webapp.Config;
import webapp.dso.LogUtil;
import webapp.dso.TrackUtil;
import webapp.dso.db.DbApi;
import webapp.utils.IPUtil;

import java.sql.SQLException;

public abstract class CMDBase {
    protected XContext context;
    protected ONode data;

    protected boolean isTrack(){return true;}
    protected boolean isLogging(){return true;}

    protected String get(String key) {
        return context.param(key);
    }

    protected String get(String key, String def) {
        return context.param(key, def);
    }

    protected int getInt(String key){
        return getInt(key, 0);
    }

    protected int getInt(String key, int def){
        String val = get(key);
        if(TextUtils.isEmpty(val)) {
            return def;
        }
        else {
            return Integer.parseInt(val);
        }
    }

    protected long getlong(String key){
        String val = get(key);
        if(TextUtils.isEmpty(val)) {
            return 0;
        }
        else {
            return Long.parseLong(val);
        }
    }

    public boolean isOutput(){
        return true;
    }

    public void exec(XContext context) {

        this.context = context;
        this.data = new ONode();

        Timecount timecount = new Timecount().start();

        try {
            if (do1_check_ip()) {
                if(isLogging()) {
                    LogUtil.info( context);
                }

                do2_exec();
            }
        } catch (Exception ex) {
            ex.printStackTrace();

            LogUtil.error(context, ex);

            data.set("code", "0");
            data.set("msg", ex.getMessage());
        }

        long timespan = timecount.stop().milliseconds();

        if(isTrack()) {
            TrackUtil.track(Config.water_service_name, "cmd", this.getClass().getSimpleName(), timespan);
        }

        if (isOutput()) {
            do3_ouput();
        }
    }

    protected abstract void cmd_exec() throws Exception;

    private final boolean do1_check_ip() throws SQLException{
        String ip = IPUtil.getIP(context);

        data.set("_ip",ip);

        if (DbApi.isWhitelist(ip) == false) {
            data.set("code", 2);
            data.set("msg", "not whitelist(" + ip + ")");
            return false;
        }else{
            return true;
        }
    }

    private final  void do2_exec() throws Exception{
        cmd_exec();
    }

    private final void do3_ouput() {
        try {
            context.charset("UTF-8");//设置响应的编码类型为UTF-8
            context.contentType("text/json;charset=UTF-8");

            context.output(data.toJson());
        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtil.error(context, ex);
        }
    }

    //==========
    protected final boolean checkParamsIsOk(String... strs) {
        for (String str : strs) {
            if (TextUtils.isEmpty(str)) {

                data.set("code", 3);
                data.set("msg", "not parameter");

                return false;
            }
        }

        return true;
    }
}