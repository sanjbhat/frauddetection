package com.fintech.transactions.service;

import com.fintech.transactions.model.Transaction;
import com.fintech.transactions.util.TransactionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

public class ApplyRuleService {

    private static final Logger logger = LoggerFactory.getLogger(ApplyRuleService.class);

    public static boolean executeRules(Transaction transaction) throws Exception {
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext(transaction);
        boolean result = true;

        //TODO Right now, the rules are part of config file. Ideally these rules can be part of database - can provide a user interface to the admin to manage them
        Map<String, String> ruleExpressions = TransactionUtil.getProperties("config.properties", TransactionUtil.RULE_PREFIX);

        for (String ruleExpr : ruleExpressions.values()) {
            Expression exp = parser.parseExpression(ruleExpr);
            result &= (Boolean) exp.getValue(context);
            if (result == false) {
                logger.info("rule " + ruleExpr + " failed");
                return false;
            }
        }

        return result;
    }
}
