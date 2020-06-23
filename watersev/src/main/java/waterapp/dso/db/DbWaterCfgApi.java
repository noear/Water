package waterapp.dso.db;

import org.noear.snack.ONode;
import org.noear.weed.DbContext;
import waterapp.Config;

import java.sql.SQLException;
import java.util.List;

public class DbWaterCfgApi {
    public static DbContext db() {
        return Config.water;
    }

    //获取账号的手机号（用于报警）
    public static List<String> getAlarmMobiles() throws SQLException {
        return db().table("water_cfg_whitelist")
                .whereEq("tag", "_alarm")
                .andEq("type", "mobile")
                .andNeq("value", "")
                .select("value ")
                .caching(Config.cache_data)
                .getArray(0);
    }

    public static boolean hasGateway(String name) {
        try {
            return db().table("water_cfg_properties")
                    .set("tag", "_gateway")
                    .set("key", name)
                    .exists();
        }catch (Exception ex){
            return false;
        }
    }
}
