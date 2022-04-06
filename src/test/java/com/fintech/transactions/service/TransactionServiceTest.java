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
    private static final String USER_ID_1 = "67891";
    private static final String USER_ID_2 = "38956";
    private static final String INDIAN_IP = "103.34.56.87";

    private static String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.60 Safari/537.36";

    @BeforeAll
    public static void init() {
        transactionService = new TransactionService();
    }

    @Test
    public void testSpentWithinDailyLimit() throws Exception {
        Transaction transaction = new Transaction(USER_ID_1, INDIAN_IP, userAgent, 99999, "credit");
        boolean isAllowed = transactionService.processTransaction(transaction);
        assertEquals(true, isAllowed);
    }

    @Test
    public void testSpendBeyondDailyLimit() throws Exception {
        Transaction transaction = new Transaction(USER_ID_1, INDIAN_IP, userAgent, 100001, "credit");
        boolean isAllowed = transactionService.processTransaction(transaction);
        assertEquals(false, isAllowed);
    }


    @Test
    public void testSpendBeyondDailyLimitCumulative() throws Exception {
        Transaction transaction = new Transaction(USER_ID_2, INDIAN_IP, userAgent, 99999, "credit");
        boolean isAllowed = transactionService.processTransaction(transaction);
        assertEquals(true, isAllowed);

        isAllowed = transactionService.processTransaction(transaction);
        assertEquals(false, isAllowed);
    }

    @Test
    public void testFromCountryOutOfOrigin() throws Exception {
        String polishIp = "10.34.56.87";
        Transaction transaction = new Transaction(USER_ID_1, polishIp, userAgent, 100, "credit");
        boolean isAllowed = transactionService.processTransaction(transaction);
        assertEquals(false, isAllowed);
    }

    @AfterAll
    public static void cleanup() {
        //remove transactions related to userIds
        transactionService.cleanupUserTransaction(Arrays.asList(USER_ID_1, USER_ID_2));
    }

}
