package com.fintech.transactions.controller;

import com.fintech.transactions.model.Transaction;
import com.fintech.transactions.service.TransactionService;
import com.fintech.transactions.util.TransactionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * Controller for exposing transaction REST API
 */
@RestController
@RequestMapping("/transaction")
public class TransactionController {

    TransactionService transactionService = new TransactionService();
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    /**
     * Controller method to accept transaction request and forward to service that will allow or deny a transaction.
     *
     * @param transaction
     * @return the json response comprising two fields - status and reason.
     */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, String>> processTransaction(@RequestBody Transaction transaction) {

        //TODO data validation - for required fields

        try {
            if (transactionService.processTransaction(transaction)) {
                logger.info("Received transaction request=" + transaction.toString());
                return ResponseEntity.ok().body(TransactionUtil.getSuccessResponse());
            }
        }catch(Exception ex)
        {
            logger.error("Exception while processing request",ex);
            return ResponseEntity.internalServerError().body(TransactionUtil.getServerErrorResponse());
        }

        return ResponseEntity.badRequest().body(TransactionUtil.getFailureResponse());
    }
}
