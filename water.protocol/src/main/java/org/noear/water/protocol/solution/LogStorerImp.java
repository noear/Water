package org.noear.water.protocol.solution;

import org.noear.water.log.Level;
import org.noear.water.log.LogEvent;
import org.noear.water.protocol.IdBuilder;
import org.noear.water.protocol.LogStorer;
import org.noear.water.protocol.ProtocolHub;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LogStorerImp implements LogStorer {
    IdBuilder _idBuilder;

    public LogStorerImp(IdBuilder idBuilder) {
        _idBuilder = idBuilder;
    }

    @Override
    public void write(String logger, Level level, String tag, String tag1, String tag2, String tag3, String summary, Object content, String from, Date log_fulltime) {
        ProtocolHub.logSourceFactory.getSource(logger)
                .write(_idBuilder.getId(), logger, level, tag, tag1, tag2, tag3, summary, content, from, log_fulltime);
    }

    @Override
    public void writeAll(List<LogEvent> list) {
        for (LogEvent log : list) {
            if (log.log_id == 0) {
                log.log_id = _idBuilder.getId();
            }
        }

        Map<String, List<LogEvent>> map = list.stream().collect(Collectors.groupingBy(m -> m.logger));

        map.forEach((logger2, list2) -> {
            ProtocolHub.logSourceFactory.getSource(logger2)
                    .writeAll(logger2, list2);
        });
    }
}
