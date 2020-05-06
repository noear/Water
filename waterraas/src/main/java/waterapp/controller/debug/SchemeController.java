package waterapp.controller.debug;

import org.noear.rubber.Rubber;
import org.noear.snack.ONode;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XHandler;
import org.noear.water.utils.TextUtils;
import org.noear.water.utils.ThrowableUtils;
import waterapp.controller.SystemCode;
import waterapp.dao.SchemeUtil;

public class SchemeController implements XHandler {
    @Override
    public void handle(XContext context) throws Exception {
        String scheme = context.param("scheme"); //通过参数传入
        String request_id = context.param("request_id");
        String args_str = context.param("args");
        int policy = context.paramAsInt("policy",1001);
        int type = context.paramAsInt("type");
        String rule = context.param("rule");

        if (TextUtils.isEmpty(scheme)  || TextUtils.isEmpty(args_str)) {
            ONode data = new ONode();
            data.set("code", 10).set("msg", SystemCode.code_10);
            context.output(data.toJson());
            return;
        }

        if ("1".equals(context.param("debug"))) {
            Rubber.updateCache("scheme:" + scheme);
        }


        try {
            SchemeUtil.run(context,request_id, scheme,policy, args_str, type, rule, true);
        }catch (Exception ex){
            ONode data = new ONode();
            data.set("code", 0).set("msg", ThrowableUtils.getString(ex));
        }
    }
}