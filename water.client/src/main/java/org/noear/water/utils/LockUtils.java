package org.noear.water.utils;

import org.noear.water.WaterSetting;

/**
 * 分布式锁工具
 * */
public class LockUtils {
    private static RedisX _redis_uni = WaterSetting.redis_cfg().getRd(2);

    /**
     * 尝试把 group_key 锁定给 inMaster
     *
     * @param group     分组
     * @param key       关键字
     * @param inSeconds 锁定时间
     * @param inMaster  锁持有人
     */
    public static boolean tryLock(String group, String key, int inSeconds, String inMaster) {
        String key2 = group + ".lk." + key;

        return _redis_uni.open1((ru) -> ru.key(key2).expire(inSeconds).lock(inMaster));
    }

    /**
     * 尝试把 group_key 锁定
     *
     * @param inSeconds 锁定时间
     */
    public static boolean tryLock(String group, String key, int inSeconds) {
        String key2 = group + ".lk." + key;

        return _redis_uni.open1((ru) -> ru.key(key2).expire(inSeconds).lock("_"));
    }

    /**
     * 尝试把 group_key 锁定 （3秒）
     */
    public static boolean tryLock(String group, String key) {
        return tryLock(group, key, 3);
    }

    /**
     * 检查 group_key 是否已被锁定
     */
    public static boolean isLocked(String group, String key) {
        String key2 = group + ".lk." + key;

        return _redis_uni.open1((ru) -> ru.key(key2).exists());
    }


    /**
     * 解锁 group_key
     */
    public static void unLock(String group, String key, String inMaster) {
        String key2 = group + ".lk." + key;

        _redis_uni.open0((ru) -> {
            if (inMaster == null || inMaster.equals(ru.key(key2).val())) {
                ru.key(key2).delete();
            }
        });
    }

    /**
     * 解锁 group_key
     */
    public static void unLock(String group, String key) {
        unLock(group, key, null);
    }
}
