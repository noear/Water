package waterapi.controller.run;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XResult;
import org.noear.solon.extend.validation.annotation.Whitelist;
import waterapi.controller.UapiBase;
import waterapi.dso.db.DbWaterCfgApi;

/**
 * 白名单重新加载
 *
 * @author noear
 * @since 2017.07
 * Update time 2020.09
 */
@Whitelist
@XController
public class CMD_run_whitelist_reload extends UapiBase {
    @XMapping("/run/whitelist/reload/")
    public XResult cmd_exec() {
        try {
            DbWaterCfgApi.loadWhitelist();

            return XResult.succeed();
        } catch (Exception ex) {
            return XResult.failure(ex.getMessage());
        }
    }
}
