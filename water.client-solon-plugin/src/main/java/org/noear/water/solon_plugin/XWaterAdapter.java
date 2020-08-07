package org.noear.water.solon_plugin;

import org.noear.snack.ONode;
import org.noear.solon.core.Aop;
import org.noear.solon.core.XContext;
import org.noear.water.WaterClient;
import org.noear.water.WW;
import org.noear.water.WaterConfig;
import org.noear.water.dso.MessageHandler;
import org.noear.water.log.Level;
import org.noear.water.log.WaterLogger;
import org.noear.water.model.MessageM;
import org.noear.water.utils.TextUtils;
import org.noear.weed.WeedConfig;
import org.noear.solon.XApp;
import org.noear.solon.XUtil;
import org.noear.solon.core.XPlugin;
import org.noear.weed.cache.ICacheServiceEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//
// Water for service project adapter
//
public abstract class XWaterAdapter extends XWaterAdapterBase implements XPlugin {
    protected static XWaterAdapter _global;
    public static XWaterAdapter global() {
        return _global;
    }


    private Map<String, MessageHandler> _router;
    public Map<String, MessageHandler> router() {
        return _router;
    }


    public String msg_receiver_url() {
        return null;
    }


    /**
     * 是否为非稳定模式 //用于兼容k8s的ip漂移
     * */
    @Override
    public boolean is_unstable() {
        return XApp.cfg().isDriftMode();
    }

    public XWaterAdapter() {
        super(XApp.cfg().argx(), XApp.global().port());
        _global = this;

        XUtil.loadClass("com.mysql.jdbc.Driver");
        XUtil.loadClass("com.mysql.cj.jdbc.Driver");
    }

    @Override
    public void start(XApp app) {
        //Bean 初始化完成化再启动监听，免得过早被检测
        //
        Aop.beanOnloaded(()->{
            app.all(service_check_path, this::handle);
            app.all(service_stop_path, this::handle);
            app.all(msg_receiver_path, this::handle);
        });
    }

    @Override
    protected void onInit() {
        _router = new HashMap<>();

        //注册服务
        registerService();

        //消息监听（收集本地监听者）
        messageListening(_router);

        //订阅消息
        messageSubscribe();

        //订阅配置更新
        configSubscribe();

        //初始化Weed监听事件
        initWeed();

    }

    /**
     * 订阅配置更新
     * */
    private void configSubscribe() {
        if (TextUtils.isEmpty(service_tag()) == false) {
            WaterClient.Config.subscribe(service_tag(), cfgSet -> {
                //将@@同步到系统配置
                cfgSet.sync();

                //
                //同步water配置
                //
                int gzip = XApp.cfg().getInt(WW.cfg_water_log_gzip, -1);
                if (gzip > -1) {
                    WaterLogger.setGzip(gzip == 1);
                }

                int level = XApp.cfg().getInt(WW.cfg_water_log_level, -1);
                if (level > -1) {
                    WaterLogger.setLevel(Level.of(level));
                }

                int interval = XApp.cfg().getInt(WW.cfg_water_log_interval, -1);
                if (interval > -1) {
                    WaterLogger.setInterval(interval);
                }

                int packetSize = XApp.cfg().getInt(WW.cfg_water_log_packetSize, -1);
                if (packetSize > -1) {
                    WaterLogger.setPacketSize(packetSize);
                }
            });
        }
    }

    //用于作行为记录
    public int user_puid() {
        if (XContext.current() != null) {
            String tmp = XContext.current().attr("user_puid", "0");
            return Integer.parseInt(tmp);
        } else {
            return 0;
        }
    }

    public String user_name() {
        if (XContext.current() != null) {
            return XContext.current().attr("user_name", null);
        } else {
            return null;
        }
    }

    /**
     * 初始化Weed监听事件
     * */
    protected void initWeed() {
        Class<?> clz = XUtil.loadClass(WW.clz_BcfClient);
        final Boolean isDebugMode = XApp.cfg().isDebugMode();
        final Boolean  isWeedStyle2= "text2".equals(XApp.cfg().get("water.weed.log.style"));

        if (clz == null) {
            //api项目
            WeedConfig.onExecuteAft(cmd -> {
                if(isDebugMode){
                    if(isWeedStyle2){
                        System.out.println(cmd.text2());
                    }else {
                        System.out.println(cmd.text + "\n" + ONode.stringify(cmd.paramMap()));
                    }
                }

                WaterClient.Track.track(service_name(), cmd, 1000);
            });
        } else {
            //admin 项目
            WeedConfig.onExecuteAft((cmd) -> {
                if(isDebugMode){
                    if(isWeedStyle2){
                        System.out.println(cmd.text2());
                    }else {
                        System.out.println(cmd.text + "\n" + ONode.stringify(cmd.paramMap()));
                    }
                }

                if (cmd.isLog < 0) {
                    return;
                }

                if (user_name() == null) {
                    return;
                }

                XContext context = XContext.current();

                String sqlUp = cmd.text.toUpperCase();
                String chkUp = "User_Id=? AND Pass_Wd=? AND Is_Disabled=0".toUpperCase();

                if (cmd.timespan() > 2000 || cmd.isLog > 0 || sqlUp.indexOf("INSERT INTO ") >= 0 || sqlUp.indexOf("UPDATE ") >= 0 || sqlUp.indexOf("DELETE ") >= 0 || sqlUp.indexOf(chkUp) >= 0) {
                    WaterClient.Track.track(service_name(), cmd, context.userAgent(), context.path(), user_puid() + "." + user_name(), IPUtils.getIP(context));
                }
            });
        }
    }

    //支持手动加入监听(保持旧的兼容)
    public void messageListening(Map<String, MessageHandler> map) {
    }


    /**
     * 消息订阅处理
     * */
    @Override
    public void messageSubscribeHandler() {
        if (_router.size() == 0) {
            return;
        }

        //远程订阅时，不包括water主题(由本地订阅处理)
        List<String> _list = new ArrayList<>();
        _router.keySet().forEach((t) -> {
            if (t.startsWith("water.") == false) {
                _list.add(t);
            }
        });

        String[] topics = new String[_list.size()];
        _list.toArray(topics);

        messageSubscribeTopic(topics);
    }

    /**
     * 订阅消息主题
     * */
    public void messageSubscribeTopic(String... topics) {
        try {
            messageSubscribeTopic(msg_receiver_url(), 0, topics);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 消息接收处理
     * */
    @Override
    public boolean messageReceiveHandler(MessageM msg) throws Exception {
        MessageHandler handler = _router.get(msg.topic);
        if (handler == null) {
            return true;
        } else {
            return handler.handler(msg);
        }
    }

    /**
     * 缓存更新处理
     * */
    @Override
    public void cacheUpdateHandler(String tag) {
        super.cacheUpdateHandler(tag);

        if (tag.indexOf(".") > 0) {
            String[] ss = tag.split("\\.");
            if(ss.length ==2) {
                ICacheServiceEx cache = WeedConfig.libOfCache.get(ss[0]);

                if (cache != null) {
                    cache.clear(ss[1]);
                }
            }
        } else {
            //删掉cache
            for (ICacheServiceEx cache : WeedConfig.libOfCache.values()) {
                cache.clear(tag);
            }
        }
    }
}
