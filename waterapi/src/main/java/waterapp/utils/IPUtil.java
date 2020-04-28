package waterapp.utils;

import org.noear.solon.core.XContext;

public class IPUtil {
    public static String getIP(XContext context){
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
