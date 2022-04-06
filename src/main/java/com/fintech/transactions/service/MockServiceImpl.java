package com.fintech.transactions.service;

import com.fintech.transactions.dao.RedisClient;
import java.time.LocalDate;

/**
 * This is a mockservice for an actual service that is decoupled from the transaction and fraud detection system.
 * It just so happens that mockservice in this case, is using the same datastore as the main fraud detection application.
 * This is just for demo purposes.
 */
public class MockServiceImpl implements MockService {

    RedisClient redisClient = new RedisClient();

    @Override
    public Integer getDailyLimit(String userId) {
        return 100000;
    }

    @Override
    public Integer getAmountOnDate(LocalDate date, String userId) throws Exception {
        return redisClient.getAmountOnDate(date, userId);
    }

    @Override
    public String getCountry(String ipAddress) throws Exception {
        if (ipAddress.startsWith("103"))
            return "India";
        return "Foreign";
    }

    @Override
    public String getCity(String ipAddress) throws Exception {
        if (ipAddress.startsWith("103.80"))
            return "Bangalore";
        return "Delhi";
    }

    @Override
    public String getOriginCountry(String userId)
    {
        return "India";
    }
}
