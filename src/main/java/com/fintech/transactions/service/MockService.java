package com.fintech.transactions.service;

import java.time.LocalDate;

/**
 * Interface for data enricher services.
 * ideally, these services should be in their own interface like CreditService, UserActivityService or better, be microservices.
 **/
public interface MockService {

    public Integer getDailyLimit(String userId) throws Exception;

    public Integer getAmountOnDate(LocalDate date, String userId) throws Exception;

    public String getCountry(String ipAddress) throws Exception;

    public String getCity(String ipAddress) throws Exception;

    public String getOriginCountry(String userId) throws Exception;
}
