package waterapp.dso;

import waterapp.dso.db.DbWaterMsgApi;

public class MsgUtils {
    public static void updateCache(String tags) {
        try {
            DbWaterMsgApi.addMessage(null,null, "water.cache.update", tags, null);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
