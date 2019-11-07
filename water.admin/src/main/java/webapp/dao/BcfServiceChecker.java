package webapp.dao;

import org.noear.bcf.BcfClient;
import org.noear.bcf.models.BcfResourceModel;
import org.noear.water.admin.tools.dso.Session;
import org.noear.water.tools.TextUtils;
import org.noear.weed.ext.Fun1;
import webapp.Config;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//不能用静态函数
public class BcfServiceChecker {
    private Map<String,String> tmpCache = null;

    private void tryLoadAgroupByUser() throws SQLException {
        if(tmpCache == null){
            tmpCache = new HashMap<>();

            List<BcfResourceModel> list = BcfClient.getUserResourcesByPack(Session.current().getPUID(), "service");

            list.forEach((r) -> {
                tmpCache.put(r.cn_name, r.cn_name);
            });
        }

    }

    public boolean find(String tag) throws SQLException {
        if (tag == null) {
            return false;
        }

        if (Config.is_use_tag_checker() == false) {
            return true;
        }

        tryLoadAgroupByUser();

        return tmpCache.containsKey(tag);
    }

    public static <T> void filter(List<T> list, Fun1<String,T> getter) throws SQLException{
        if(Session.current().getIsAdmin()==1){
            return;
        }

        if (Config.is_use_tag_checker() == false) {
            return;
        }

        BcfServiceChecker checker = new BcfServiceChecker();

        for(int i=0,len=list.size(); i<len; i++){
            String tag = getter.run(list.get(i));

            if(TextUtils.isEmpty(tag)){
                list.remove(i);
                i--;
                len--;
            }else {
                if (checker.find(tag) == false) {
                    list.remove(i);
                    i--;
                    len--;
                }
            }
        }
    }

}