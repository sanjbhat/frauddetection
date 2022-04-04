package com.fintech.transactions.service;

import com.fintech.transactions.model.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for TransactionService methods.
 *
 * Use cases covered:
 * within daily limit and inside country -> allow
 * outside daily limit -> deny
 * outisde country -> deny
 *
 */
public class TransactionServiceTest {

    private static TransactionService transactionService;
    private static final String userId1 = "67891";
    private static final String userId2 = "38956";

    private static String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.60 Safari/537.36";

    @BeforeAll
    public static void init() {
        transactionService = new TransactionService();
    }

    @Test
    public void testSpentWithinDailyLimit() throws Exception {
        String indianIp = "127.34.56.87";
        Transaction transaction = new Transaction(userId1, indianIp, userAgent, 99999, "credit");
        boolean isAllowed = transactionService.processTransaction(transaction);
        assertEquals(true, isAllowed);
    }

    @Test
    public void testSpendBeyondDailyLimit() throws Exception {
        String indianIp = "127.34.56.87";
        Transaction transaction = new Transaction(userId1, indianIp, userAgent, 100001, "credit");
        boolean isAllowed = transactionService.processTransaction(transaction);
        assertEquals(false, isAllowed);
    }


    @Test
    public void testSpendBeyondDailyLimitCumulative() throws Exception {
        String indianIp = "127.34.56.87";
        Transaction transaction = new Transaction(userId2, indianIp, userAgent, 99999, "credit");
        boolean isAllowed = transactionService.processTransaction(transaction);
        assertEquals(true, isAllowed);

        isAllowed = transactionService.processTransaction(transaction);
        assertEquals(false, isAllowed);
    }

    @Test
    public void testFromCountryOutOfOrigin() throws Exception {
        String polishIp = "10.34.56.87";
        Transaction transaction = new Transaction(userId1, polishIp, userAgent, 100, "credit");
        boolean isAllowed = transactionService.processTransaction(transaction);
        assertEquals(false, isAllowed);
    }

    @AfterAll
    public static void cleanup() {
        //remove transactions related to userIds
        transactionService.cleanupUserTransaction(Arrays.asList(userId1, userId2));

    }

}
