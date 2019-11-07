package webapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

/**
 * <ul>
 * <li>
 * class comment
 * </li>
 * </ul>
 *
 * @author Lu.Bing
 * on 17-7-18
 * at 下午5:56
 * in webadmin.utils, admin-rock
 */
public class CodeUtil {
    /**
     * <ul>
     * <li>
     * 生成16为大写+小写+数字的appkey
     * </li>
     * </ul>
     *
     * @return appKey
     */
    public static String genAppKey() {
        char[] chars = new char[16];
        int i = 0;
        while (i < 16) {
            int f = (int) (Math.random() * 3);
            if (f == 0)
                chars[i] = (char) ('A' + Math.random() * 26);
            else if (f == 1)
                chars[i] = (char) ('a' + Math.random() * 26);
            else
                chars[i] = (char) ('0' + Math.random() * 10);
            i++;
        }
        return new String(chars);
    }

    //生成uuid
    public static String genUUID() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}