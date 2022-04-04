package com.fintech.transactions.service.anomaly;

import com.fintech.transactions.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;

/**
 * Service class that is supposed to run pre-configured anomaly rules on the transactions.
 * Right now, it is a bunch of print statements.
 */
public class AnomalyService {

    private static final Logger logger = LoggerFactory.getLogger(AnomalyService.class);

    public boolean runAnomalies(Transaction transaction) {
        if (transaction == null)
            throw new IllegalArgumentException("transaction object cannot be null");
        boolean foundAnomaly = false;
        try {
            //1. enrich the transaction based on user id. We need user's usual device/browser/lastLoggedin, etc. details
            logger.info("Getting user past activity details");

            //2. Run the anomaly rules on the transaction
            logger.info("Running the rule engine");

            //3.Alert the customer support if anything flagged.

            //some random condition to flag some requests since this is a mock
            if (new SecureRandom().nextInt(20) == 1)
                foundAnomaly = true;

            if (foundAnomaly)
                logger.info("Anomaly found in transaction.Sending notification %s", transaction);

        } catch (Exception ex) {
            throw ex;
        }

        return foundAnomaly;

    }
}
