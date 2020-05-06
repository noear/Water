package waterapp.controller.cmds;

import org.noear.water.log.Level;
import org.noear.water.protocol.ProtocolHub;

/**
 * Created by noear on 2017/7/19.
 */
public class CMD_log_add extends CMDBase {

    @Override
    protected boolean isLogging() {
        return false;
    }

    @Override
    protected void cmd_exec() throws Exception {
        String logger = get("logger");

        if (checkParamsIsOk(logger) == false) {
            return;
        }

        String tag = get("tag", "");
        String tag1 = get("tag1", "");
        String tag2 = get("tag2", "");
        String tag3 = get("tag3", "");


        int level = getInt("level", 0);

        String summary = get("summary");
        String content = get("content");

        String from = get("from");

        ProtocolHub.logStorer.append(logger, Level.of(level), tag, tag1, tag2, tag3, summary, content, from);
        data.set("code", 1);
        data.set("msg", "success");
    }
}