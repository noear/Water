package org.noear.water.protocol.solution;

import org.noear.water.log.Level;
import org.noear.water.protocol.LogStorer;
import org.noear.water.protocol.ProtocolHub;

public class LogStorerImp implements LogStorer {
    public LogStorerImp() {
    }


    @Override
    public void write(String logger, Level level, String tag, String tag1, String tag2, String tag3, String summary, Object content, String from) {
        ProtocolHub.logSourceFactory.getSource(logger)
                .write(buildId(), logger, level, tag, tag1, tag2, tag3, summary, content, from);
    }

    @Override
    public long buildId() {
        return 0;
    }

}
