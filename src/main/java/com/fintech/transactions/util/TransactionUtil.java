package com.fintech.transactions.util;

import com.fintech.transactions.dao.RedisClient;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class TransactionUtil {

    public static final String NAMESPACE_PREFIX = "namespace.";
    public static final String RULE_PREFIX = "spel.";
    public static final String CHANNEL_NAME = "fraud_alert";

    /**
     * @return object with canned response for success
     */
    public static Map<String, String> getSuccessResponse() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("reason", "ok");

        return response;
    }

    /**
     * @return object with canned response for Bad Request/ or transaction that is denied
     */
    public static Map<String, String> getFailureResponse() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "failure");
        response.put("reason", "400");

        return response;
    }

    /**
     * @return object with canned response for internal server error
     */
    public static Map<String, String> getServerErrorResponse() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "failure");
        response.put("reason", "500");

        return response;
    }

    /**
     * helper code to load properties from given file and return prop name and value in a map
     *
     * @param filename
     * @param prefix
     * @return map
     * @throws IOException
     */
    public static Map<String, String> getProperties(String filename, String prefix) throws IOException {
        Map<String, String> props = new HashMap<>();
        try (InputStream input = RedisClient.class.getClassLoader().getResourceAsStream(filename)) {
            Properties properties = new Properties();
            properties.load(input);

            Set<String> keys = properties.stringPropertyNames();

            for (String key : keys) {
                if (key.startsWith(prefix))
                    props.put(key, properties.getProperty(key));
            }

        } catch (IOException ex) {
            throw ex;
        }

        return props;
    }

}
