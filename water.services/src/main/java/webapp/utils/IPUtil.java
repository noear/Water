package webapp.utils;

import org.noear.solon.core.XContext;
import org.noear.water.tools.TextUtils;

/**
 * Created by Mazexal on 2017/4/25.
 */
public class IPUtil {


    public static String getIP(XContext context){
        if(context == null){
            return "";
        }

        String ip =  context.header("RemoteIp");

        if (TextUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = context.header("X-Forwarded-For");
        }

        if (TextUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = context.header("Proxy-Client-IP");
        }

        if (TextUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = context.header("WL-Proxy-Client-IP");
        }

        if (TextUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = context.ip();
        }

        return ip;
    }
}