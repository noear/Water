package org.noear.water.client.model;

import org.noear.snack.ONode;
import org.noear.water.client.utils.RunUtil;

import java.io.StringReader;
import java.util.Properties;

/**
 * 配置模型
 * */
public class ConfigModel {
    private String tag;
    private String key;
    private String value;

    public ConfigModel(String tag,String key, String value){
        this.tag = tag;
        this.key = key;
        this.value = value;
    }

    //获取标签
    public String getTag(){
        return tag;
    }

    //获取key
    public String getKey(){
        return key;
    }


    //转为String
    public String toString(){
        return  value;
    }

    /** 转为Int */
    public int toInt(){
        return Integer.parseInt(value);
    }

    /** 转为Long */
    public long toLong(){
        return Long.parseLong(value);
    }

    /** 转为Properties */
    public Properties toProp(){
        Properties tmp = new Properties();
        RunUtil.runActEx(()->tmp.load(new StringReader(value)));
        return tmp;
    }

    /** 转为ONode */
    public ONode toNode(){
        return ONode.load(value);
    }
}
