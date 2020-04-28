package waterapp.models.water_cfg;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.noear.snack.ONode;
import org.noear.water.utils.RedisX;
import org.noear.water.utils.RunUtils;
import org.noear.water.utils.TextUtils;
import org.noear.weed.*;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;
import org.noear.weed.cache.memcached.MemCache;
import waterapp.dso.ConfigType;

import java.io.StringReader;
import java.util.*;

@Getter
public class ConfigModel
{
    public transient int row_id;
    public String tag;
    public String key;
    public int type;
    public String value;
    public transient int is_enabled;

	public String type_str(){
	    return ConfigType.getTitle(type);
    }



    public String getString() {
        return value;
    }

    public String getString(String def) {
        return value == null ? def : value;
    }

    /**
     * 转为Int
     */
    public int getInt(int def) {
        if (TextUtils.isEmpty(value)) {
            return def;
        } else {
            return Integer.parseInt(value);
        }
    }

    /**
     * 转为Long
     */
    public long getLong(long def) {
        if (TextUtils.isEmpty(value)) {
            return def;
        } else {
            return Long.parseLong(value);
        }
    }

    /**
     * 转为Properties
     */
    private Properties _prop;

    public Properties getProp() {
        if (_prop == null) {
            _prop = new Properties();
            RunUtils.runActEx(() -> _prop.load(new StringReader(value)));
        }

        return _prop;
    }

    /**
     * 转为ONode
     */
    private ONode _node;

    public ONode getNode() {
        if (_node == null) {
            _node = ONode.load(value);
        }

        return _node;
    }


    /**
     * 获取 rd:RedisX
     */
    public RedisX getRd(int db) {
        return new RedisX(getProp(), db);
    }

    public RedisX getRd(int db, int maxTotaol) {
        return new RedisX(getProp(), db, maxTotaol);
    }

    /**
     * 获取 cache:ICacheServiceEx
     */
    public ICacheServiceEx getCh(String keyHeader, int defSeconds) {
        if(TextUtils.isEmpty(value)){
            return new LocalCache(keyHeader,defSeconds);
        }else {
            return new MemCache(getProp(), keyHeader, defSeconds);
        }
    }

    /**
     * 获取 db:DbContext
     */
    public DbContext getDb() {
        return getDb(false);
    }

    public DbContext getDb(boolean pool) {
        DbContext db = new DbContext();
        Properties prop = getProp();
        String url = prop.getProperty("url");

        if (pool) {
            HikariDataSource source = new HikariDataSource();

            String schema = prop.getProperty("schema");
            String username = prop.getProperty("username");
            String password = prop.getProperty("password");
            String driverClassName = prop.getProperty("driverClassName");

            if (TextUtils.isEmpty(url) == false) {
                source.setJdbcUrl(url);
            }

            if (TextUtils.isEmpty(username) == false) {
                source.setUsername(username);
            }

            if (TextUtils.isEmpty(password) == false) {
                source.setPassword(password);
            }

            if (TextUtils.isEmpty(schema) == false) {
                source.setSchema(schema);
            }

            if (TextUtils.isEmpty(driverClassName) == false) {
                source.setDriverClassName(driverClassName);
            }

            db.dataSourceSet(source);
            db.schemaSet(schema);
        } else {
            db.propSet(getProp());
        }

        return db;
    }

}