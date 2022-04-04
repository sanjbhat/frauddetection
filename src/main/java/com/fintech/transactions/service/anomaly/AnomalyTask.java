package com.fintech.transactions.service.anomaly;

import com.fintech.transactions.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.Callable;

/**
 * Task that calls the anomaly service to flag the transaction.
 */
public class AnomalyTask implements Callable<Boolean> {

    private static final Logger logger = LoggerFactory.getLogger(AnomalyTask.class);
    Transaction transaction;
    public AnomalyTask(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public Boolean call() throws Exception {
        try {
            logger.info("Running anomaly service for transaction %s", this.transaction.toString());
            return new AnomalyService().runAnomalies(this.transaction);
        } catch (Exception ex) {
            throw ex;
        }
    }
}
