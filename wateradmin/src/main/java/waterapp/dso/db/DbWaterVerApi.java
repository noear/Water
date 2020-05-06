package waterapp.dso.db;

import org.noear.snack.ONode;
import org.noear.solon.core.XContext;
import org.noear.water.solon_plugin.IPUtils;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.EncryptUtils;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DataItem;
import org.noear.weed.DbContext;
import waterapp.Config;
import waterapp.dso.CacheUtil;
import waterapp.dso.Session;
import waterapp.models.water.VersionModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbWaterVerApi {
    private static DbContext db() {
        return Config.water;
    }

    /** 备份表的数据版本 */
    public static void logVersion(String table,String keyName,Object keyValue0){

        if(TextUtils.isEmpty(keyName)  || keyValue0 == null){
            return;
        }

        String keyValue = keyValue0.toString();


        try {
            DataItem data = db().table(table).whereEq(keyName , keyValue).limit(1).select("*").getDataItem();

            if (data == null || data.count() == 0) {
                return;
            }

            Datetime now_time = Datetime.Now();
            String data_json = ONode.stringify(data.getMap());
            String data_md5 =  EncryptUtils.md5(data_json);

            String old_data_md5 = getLastVersionMd5(table,keyName,keyValue);

            //如果md5一样，说明没什么变化
            if(data_md5.equals(old_data_md5)){
                return;
            }

            db().table("water_tool_versions")
                    .set("table", table)
                    .set("key_name", keyName)
                    .set("key_value", keyValue)
                    .set("data", data_json)
                    .set("data_md5",data_md5)
                    .set("log_user", Session.current().getUserName())
                    .set("log_ip", IPUtils.getIP(XContext.current()))
                    .set("log_date", now_time.getDate())
                    .set("log_fulltime",now_time.getFulltime())
                    .insert();

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /** 获取历史版本最近10个 */
    public static List<VersionModel> getVersions(String table, String keyName, String keyValue) throws SQLException {

        if(TextUtils.isEmpty(keyValue)){
            return new ArrayList<>();
        }


        return db().table("water_tool_versions")
                .where("`table` = ?",table)
                .and("`key_name`=?",keyName)
                .and("`key_value`=?",keyValue)
                .orderBy("commit_id DESC")
                .limit(10)
                .select("*")
                .getList(new VersionModel());
    }

    public static VersionModel getVersionByCommit(int commit_id) throws SQLException{

        return db().table("water_tool_versions")
                .where("`commit_id` = ?",commit_id)
                .limit(1)
                .select("*")
                .caching(CacheUtil.data)
                .getItem(new VersionModel());
    }

    /** 最后一个历史版本的MD5 */
    public static String getLastVersionMd5(String table,String keyName,String keyValue) throws SQLException{

        return db().table("water_tool_versions")
                .where("`table` = ?",table)
                .and("`key_name`=?",keyName)
                .and("`key_value`=?",keyValue)
                .orderBy("commit_id DESC")
                .limit(1)
                .select("data_md5")
                .getValue("");
    }
}