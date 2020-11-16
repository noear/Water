package waterapi.dso.validation;

import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Result;
import org.noear.solon.extend.validation.annotation.Whitelist;
import org.noear.solon.extend.validation.annotation.WhitelistValidator;
import waterapi.dso.IPUtils;
import waterapi.dso.db.DbWaterCfgApi;

import java.sql.SQLException;

public class WhitelistValidatorImp extends WhitelistValidator {
    @Override
    public Result validate(Context ctx, Whitelist anno, String name, StringBuilder tmp) {
        String ip = IPUtils.getIP(ctx);

        try {
            if (DbWaterCfgApi.isWhitelist(ip)) {
                return Result.succeed();
            } else {
                return Result.failure(ip);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
