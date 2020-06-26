package org.noear.water.protocol;

import org.noear.water.protocol.model.LogModel;

import java.util.List;

public interface ILogQuerier {
    List<LogModel> query(String logger, Integer level, int size, String tag, String tag1, String tag2, String tag3, Integer log_date, Long log_id) throws Exception;
}
