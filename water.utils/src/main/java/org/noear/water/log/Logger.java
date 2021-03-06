package org.noear.water.log;


public interface Logger {
    String getName();
    void setName(String name);

    default boolean isTraceEnabled(){return true;}
    void trace(Object content);
    void trace(String summary, Object content);
    void trace(String tag, String summary, Object content);
    void trace(String tag, String tag1, String summary, Object content);
    void trace(String tag, String tag1, String tag2, String summary, Object content);
    void trace(String tag, String tag1, String tag2, String tag3, String summary, Object content);

    default boolean isDebugEnabled(){return true;}
    void debug(Object content);
    void debug(String summary, Object content);
    void debug(String tag, String summary, Object content);
    void debug(String tag, String tag1, String summary, Object content);
    void debug(String tag, String tag1, String tag2, String summary, Object content);
    void debug(String tag, String tag1, String tag2, String tag3, String summary, Object content);

    default boolean isInfoEnabled(){return true;}
    void info(Object content);
    void info(String summary, Object content);
    void info(String tag, String summary, Object content);
    void info(String tag, String tag1, String summary, Object content);
    void info(String tag, String tag1, String tag2, String summary, Object content);
    void info(String tag, String tag1, String tag2, String tag3, String summary, Object content);

    default boolean isWarnEnabled(){return true;}
    void warn(Object content);
    void warn(String summary, Object content);
    void warn(String tag, String summary, Object content);
    void warn(String tag, String tag1, String summary, Object content);
    void warn(String tag, String tag1, String tag2, String summary, Object content);
    void warn(String tag, String tag1, String tag2, String tag3, String summary, Object content);

    default boolean isErrorEnabled(){return true;}
    void error(Object content);
    void error(String summary, Object content);
    void error(String tag, String summary, Object content);
    void error(String tag, String tag1, String summary, Object content);
    void error(String tag, String tag1, String tag2, String summary, Object content);
    void error(String tag, String tag1, String tag2, String tag3, String summary, Object content);
}
