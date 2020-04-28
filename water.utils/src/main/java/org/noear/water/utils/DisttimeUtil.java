package org.noear.water.utils;

import java.util.Date;

public class DisttimeUtil {
    public static long currTime(){
        return System.currentTimeMillis();
    }

    public static long nextTime(Date date){
        return date.getTime();
    }

    public static long nextTime(int dist_count) {
        int second = 0;

        switch (dist_count){
            case 0:second  = 0;break;
            case 1:second  = second+30;break; //30秒
            case 2:second  = second+2*60;break;//2分种
            case 3:second  = second+5*60;break;//5分钟
            case 4:second  = second+10*60;break;//10分钟
            case 5:second  = second+30*60;break;//30分钟
            case 6:second  = second+60*60;break;//1小时
            case 7:second  = second+90*60;break;//1.5小时
            default:second = second+120*60;break;//2小时
        }

        return System.currentTimeMillis() + (second * 1000);
    }
}
