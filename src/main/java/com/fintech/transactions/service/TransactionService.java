package com.fintech.transactions.service;

import com.fintech.transactions.dao.RedisClient;
import com.fintech.transactions.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.util.List;

public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);
    private final RedisClient redisClient = new RedisClient();
    private final MockService mockService = new MockServiceImpl();

    public boolean processTransaction(Transaction transaction) throws Exception {

        //1. enrich the request
        transaction = enrichRequest(transaction);

        //2. run SpEL logic for daily limit and country of origin
        try {
            boolean isAllowed = ApplyRuleService.executeRules(transaction);
            if (!isAllowed) {
                //notify user
                NotificationService.callUser(transaction);
                return false;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

        //TODO forward request to AnomalyService
        addUserTransactionAmount(transaction);

        RequestForwarderParallel.checkForAnamolies(transaction);
        return true;
    }

    private Transaction enrichRequest(Transaction transaction) {

        //1. read the namespace and call respective methods
        String namespace = transaction.getType();
        List<String> services = this.getServicesList(namespace);

        try {
            for (String service : services) {
                switch (service) {
                    case "getDailyLimit":
                        transaction.setDailyLimit(mockService.getDailyLimit(transaction.getUserId()));
                        break;
                    case "getAmountToday":
                        transaction.setSpentBeforeThis(mockService.getAmountOnDate(LocalDate.now(), transaction.getUserId()));
                        break;
                    case "getCountry":
                        transaction.setCountry(mockService.getCountry(transaction.getIpAddress()));
                        break;
                    case "getCity":
                        transaction.setCity(mockService.getCity(transaction.getIpAddress()));
                        break;
                    case "getOriginCountry":
                        transaction.setOriginCountry(mockService.getOriginCountry(transaction.getUserId()));
                    default:
                        break;

                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

        return transaction;
    }

    private List<String> getServicesList(String namespace) {
        return redisClient.getServicesList(namespace);
    }

    private void addUserTransactionAmount(Transaction transaction) throws Exception {
        redisClient.registerTransaction(LocalDate.now(), transaction.getUserId(), transaction.getAmount());
    }

    public void cleanupUserTransaction(List<String> userIdList)
    {
        if(userIdList == null || userIdList.isEmpty())
            return;

        userIdList.forEach((userId -> {redisClient.cleanupUserTransactions(LocalDate.now(), userId);}));
    }


}
