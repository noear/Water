package waterpaas.controller._msg;

import org.noear.solon.core.handle.Context;
import org.noear.luffy.executor.ExecutorFactory;
import org.noear.luffy.model.AFileModel;
import org.noear.water.annotation.WaterMessage;
import org.noear.water.dso.MessageHandler;
import org.noear.water.model.MessageM;
import org.noear.water.utils.StringUtils;
import org.noear.water.utils.TextUtils;
import waterpaas.dso.AFileUtil;
import waterpaas.dso.DbPaaSApi;
import waterpaas.dso.RouteHelper;

@WaterMessage("water.cache.update")
public class msg_updatecache implements MessageHandler {
    static final String label_hook_start = "hook.start";

    @Override
    public boolean handler(MessageM msg) throws Exception {
        for (String tag : msg.message.split(";")) {
            if (TextUtils.isEmpty(tag) == false) {
                handlerDo(tag);
            }
        }

        return true;
    }

    private void handlerDo(String tag) throws Exception{
        if (tag.indexOf(":") > 0) {
            String[] ss = tag.split(":");
            if ("paas".equals(ss[0])) {
                String file_id = ss[1];

                if (StringUtils.isNumeric(file_id)) {
                    AFileModel file = DbPaaSApi.fileGet(Integer.parseInt(file_id));

                    if (TextUtils.isEmpty(file.path) == false) {
                        String name = file.path.replace("/", "__");
                        AFileUtil.remove(file.path);
                        ExecutorFactory.del(name);

                        RouteHelper.reset();

                        //处理hook.start
                        //
                        if (label_hook_start.equals(file.label)) {
                            ExecutorFactory.execOnly(file, Context.current());
                        }
                    }
                }
            }
        }
    }
}
