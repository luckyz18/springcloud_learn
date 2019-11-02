package com.itmuch.contentcenter.rocketmq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MySource {
    String MYOUTPUT = "my-output";

    @Output(MYOUTPUT)
    MessageChannel output();
}
