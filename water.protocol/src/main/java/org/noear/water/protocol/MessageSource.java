package org.noear.water.protocol;

import org.noear.water.protocol.model.message.DistributionModel;
import org.noear.water.protocol.model.message.MessageModel;
import org.noear.water.protocol.model.message.MessageState;
import org.noear.water.protocol.model.message.SubscriberModel;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * @author noear 2021/2/1 created
 */
public interface MessageSource {
    boolean hasMessage(String msg_key) throws SQLException;
    void cancelMessage(String msg_key) throws SQLException;
    void succeedMessage(String msg_key) throws SQLException;
    void cancelMsgDistribution(String msg_key, String subscriber_key) throws SQLException;
    void succeedMsgDistribution(String msg_key, String subscriber_key) throws SQLException;
    long addMessage(String topic, String content) throws Exception;
    long addMessage(String msg_key, String trace_id, String tags, String topic, String content, Date plan_time) throws Exception;


    List<Long> getMessageList(int rows, long dist_nexttime) throws SQLException;
    MessageModel getMessageOfPending(long msg_id) throws SQLException;
    void setMessageRouteState(MessageModel msg, boolean dist_routed);
    boolean setMessageState(MessageModel msg, MessageState state);
    boolean setMessageState(MessageModel msg, MessageState state, long dist_nexttime);
    boolean setMessageRepet(MessageModel msg, MessageState state);

    void addDistributionNoLock(MessageModel msg, SubscriberModel subs) throws SQLException;
    List<DistributionModel> getDistributionListByMsg(long msg_id) throws SQLException;
    boolean setDistributionState(MessageModel msg, DistributionModel dist, MessageState state);
}
