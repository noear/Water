package waterapi.dso.db;

import org.noear.water.model.ConfigM;
import org.noear.water.protocol.model.LoggerModel;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import waterapi.Config;
import waterapi.dso.CacheUtils;
import waterapi.models.ConfigModel;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 配置服务接口
 * */
public class DbWaterCfgApi {
    private static DbContext db() {
        return Config.water;
    }


    //获取配置项目
    public static DbContext getDbContext(String sourceKey, DbContext defDb) throws SQLException {
        if (sourceKey == null || sourceKey.indexOf(".") < 0) {
            return defDb;
        } else {
            String[] ss = sourceKey.split("\\.");
            ConfigModel cfg = getConfig(ss[0], ss[1]);

            if (TextUtils.isEmpty(cfg.value)) {
                return defDb;
            } else {
                return cfg.getDb();
            }
        }

    }

    public static List<ConfigModel> getConfigByTag(String tag) throws SQLException {
        return db().table("water_cfg_properties")
                .whereEq("tag", tag)
                .select("*")
//                .caching(CacheUtil.data)
//                .usingCache(5)
                .getList(new ConfigModel());
    }

    public static ConfigM getConfigM(String tag, String key) {
        return getConfig(tag,key).toConfigM();
    }

    public static ConfigModel getConfig(String tag, String key) {
        try {
            return db().table("water_cfg_properties")
                    .where("tag=? AND `key`=?", tag, key)
                    .select("*")
                    .caching(CacheUtils.data)
                    .getItem(new ConfigModel());
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static void setConfig(String tag, String key, String value) throws SQLException {
        db().table("water_cfg_properties")
                .set("tag", tag)
                .set("key", key)
                .set("value", value)
                .set("update_fulltime",new Date())
                .where("tag=? AND `key`=?", tag, key)
                .update();
    }

    public static void addConfig(String tag, String key, String value) throws SQLException {
        db().table("water_cfg_properties")
                .set("tag", tag)
                .set("key", key)
                .set("value", value)
                .set("is_editable",true)
                .set("update_fulltime",new Date())
                .insert();
    }

    public static ConfigModel getConfigNoCache(String tag, String key) throws SQLException {
        return db().table("water_cfg_properties")
                .where("tag=? AND `key`=?", tag, key)
                .select("*")
                .getItem(new ConfigModel());
    }


    //获取账号的手机号（用于报警）
    public static List<String> getAlarmMobiles() throws SQLException {
        return Config.water.table("water_cfg_whitelist")
                .whereEq("type","mobile")
                .andEq("tag","_alarm")
                .andNeq("value","")
                .select("value ")
                .caching(CacheUtils.data)
                .getArray(0);
    }

    //获取IP白名单
    private static List<String> _whitelist = null;

    private static synchronized List<String> getWhitelist() throws SQLException {
        if (_whitelist == null) {
            loadWhitelist();
        }

        return _whitelist;
    }

    //加载IP白名单到静态缓存里
    public static void loadWhitelist() throws SQLException {
        _whitelist = db().table("water_cfg_whitelist")
                .whereEq("type","ip")
                .andEq("tag","server")
                .select("value")
                .caching(CacheUtils.data).usingCache(60)
                .getArray("value");
    }

    //检查是否为IP白名单
    public static boolean isWhitelist(String ip) throws SQLException {
        return getWhitelist().contains(ip);
    }

    public static boolean isWhitelist(String tags, String type, String value) throws SQLException {
        return db().table("water_cfg_whitelist")
                .whereEq("type", type)
                .andEq("value", value)
                .build((tb)->{
                    if(tags.length()  > 0){
                        if(tags.indexOf(",") < 0){
                            tb.andEq("tag", tags);
                        }else{
                            tb.andIn("tag", Arrays.asList(tags.split(",")));
                        }

                    }
                })
                .caching(CacheUtils.data).usingCache(60)
                .exists();

    }


    public static LoggerModel getLogger(String logger) {
        try {
            return db().table("water_cfg_logger").where("logger=?", logger).limit(1)
                    .select("*")
                    .caching(CacheUtils.data).usingCache(60)
                    .getItem(LoggerModel.class);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static boolean hasGateway(String name) {
        try {
            return db().table("water_cfg_properties")
                    .whereEq("tag", "_gateway")
                    .andEq("key", name)
                    .exists();
        }catch (Exception ex){
            return false;
        }
    }
}
