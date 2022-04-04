package com.fintech.transactions.service;

import com.fintech.transactions.dao.RedisClient;
import com.fintech.transactions.model.Transaction;
import com.fintech.transactions.util.TransactionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to that provides methods for sending notification to the pub/sub
 */
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private static RedisClient redisClient = new RedisClient();

    public static void callUser(Transaction transaction)
    {
        //send a message to the notification engine, which is listening
        logger.info("alerting user for fraud");
        redisClient.sendAlertMessage(TransactionUtil.CHANNEL_NAME, String.valueOf(transaction));
    }
}
