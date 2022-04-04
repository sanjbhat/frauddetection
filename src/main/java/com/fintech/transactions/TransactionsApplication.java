package com.fintech.transactions;

import com.fintech.transactions.dao.RedisClient;
import com.fintech.transactions.service.RequestForwarderParallel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry class for the application. Starts the spring boot app and loads necessary data.
 */
@SpringBootApplication
public class TransactionsApplication {

    public static void main(String[] args) {

		SpringApplication.run(TransactionsApplication.class, args);
		RedisClient.init();
        RequestForwarderParallel.init();
    }


}
