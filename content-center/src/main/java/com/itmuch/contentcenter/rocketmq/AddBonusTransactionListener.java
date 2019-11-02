package com.itmuch.contentcenter.rocketmq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itmuch.contentcenter.dao.content.RocketmqTransactionLogMapper;
import com.itmuch.contentcenter.domain.dto.content.ShareAuditDto;
import com.itmuch.contentcenter.domain.entity.content.RocketmqTransactionLog;
import com.itmuch.contentcenter.service.content.ShareService;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

/**
 * 首先调用TestProducerService.testSendMsg向MQ Server发送半消息，从代码也可以看到该方法里不会执行本地事务方法。
 * 当 MQ Server接收半消息成功后，会告诉生产者接收成功，接着就会执行本地事务监听器里的 executeLocalTransaction 方法，
 * 该方法里会调用 TestProducerService 里带有事务注解的方法 auditByIdWithTransactionLog，并在事务方法执行完毕后返回本地事务状态给MQ Server。
 * 若 executeLocalTransaction方法返回的事务状态是 UNKNOWN 或者该方法出于某种原因没有被执行完毕，那么 MQ Server就接收不到二次确认消息，
 * 默认会在一分钟后向生产者发送回查消息，生产者接收到回查消息的话就会执行本地事务监听器里的 checkLocalTransaction方法，
 * 通过事务日志记录表的数据来确认该事务状态并返回。
 */

//这里 txProducerGroup 一定要和 发送消息的 group写的一致

/**
 * 本地事务监听器
 */
@RocketMQTransactionListener(txProducerGroup = "tx-add-bonus-group")
public class AddBonusTransactionListener implements RocketMQLocalTransactionListener {
    @Autowired
    private ShareService shareService;

    @Autowired
    private RocketmqTransactionLogMapper rocketmqTransactionLogMapper;

    /**
     * 执行本地事务
     * @param message
     * @param args
     * @return
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object args) {

        try {
            MessageHeaders headers = message.getHeaders();
            Integer shareId = Integer.valueOf(headers.get("share_id").toString());
            String tranctionId = headers.get(RocketMQHeaders.TRANSACTION_ID).toString();

            //stream rmq 来获取 auditDto  args ==null
            //小坑： 这里获取的是 String类型的，没办法转换成对象 所以传参的时候 用jsonString 在这里在解析成对象
            String auditDtoString = (String)headers.get("dto");
            ShareAuditDto auditDto = JSON.parseObject(auditDtoString,ShareAuditDto.class);
            shareService.auditByIdWithTransactionLog(shareId, auditDto, tranctionId);

            // 执行带有事务注解的方法
            //shareService.auditByIdWithTransactionLog(shareId, (ShareAuditDto) args, tranctionId);
            // 正常执行，向MQ Server发送commit消息
            return RocketMQLocalTransactionState.COMMIT;
        } catch (NumberFormatException e) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    /**
     * 用记日志的方式实现消息的回查
     *  用于回查本地事务的执行结果
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        MessageHeaders headers = message.getHeaders();
        String tranctionId = headers.get(RocketMQHeaders.TRANSACTION_ID).toString();
        RocketmqTransactionLog rocketmqTransactionLog = rocketmqTransactionLogMapper.selectOne(RocketmqTransactionLog.builder()
                .transactionId(tranctionId)
                .build()
        );
        if (rocketmqTransactionLog != null) {
            return RocketMQLocalTransactionState.COMMIT;
        }
        return RocketMQLocalTransactionState.ROLLBACK;
    }
}
