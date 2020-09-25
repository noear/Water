package waterapi.controller.run;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XContext;
import org.noear.solon.extend.validation.annotation.NotEmpty;
import org.noear.solon.extend.validation.annotation.Whitelist;
import waterapi.controller.UapiBase;
import waterapi.dso.db.DbWaterCfgApi;

@Whitelist
@XController
public class CMD_run_whitelist_check extends UapiBase {
    @NotEmpty({"type", "value"})
    @XMapping("/run/whitelist/check/")
    public String cmd_exec(XContext ctx, String type, String value) throws Exception {
        String tags = ctx.param("tags", "");

        if (tags.contains("client")) {
            if (DbWaterCfgApi.whitelistIgnoreClient()) {
                return "OK";
            }
        }

        boolean isOk = DbWaterCfgApi.isWhitelist(tags, type, value);

        if (isOk) {
            return ("OK");
        } else {
            return (value + ",not is whitelist!");
        }
    }
}
