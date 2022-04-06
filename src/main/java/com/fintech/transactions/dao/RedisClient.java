package com.fintech.transactions.dao;

import com.fintech.transactions.util.TransactionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Client to connect to locally running redis datastore (in default port) via Jedis library
 */
public class RedisClient {

    private static final Logger logger = LoggerFactory.getLogger(RedisClient.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    //this connects to the default port running in the local instance
    private static JedisPool jedisPool = new JedisPool(getPoolConfig());

    /**
     * called during loading of spring boot application.
     * 1. Loads namespace - serice mapping into the db
     * 2. creates a listener that listens to the channel - where events for calling customer are sent
     */
    public static void init() {
        loadRedisNamespaceService();
        createListener(TransactionUtil.CHANNEL_NAME);
    }

    /**
     * Reads from namespace mapping property file config.properties and loads into the datastore
     */
    private static void loadRedisNamespaceService() {
        logger.info("loading namespace service mapping into key value store");
        try (Jedis jedis = jedisPool.getResource()) {
            Map<String, String> props = TransactionUtil.getProperties("config.properties", TransactionUtil.NAMESPACE_PREFIX);
            props.forEach((k, v) -> {
                jedis.set(k, v);
            });
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

    /**
     * Creates a listener or a subscriber that listens to the channel name.
     * subscribe is a blocking method, hence, this client is different from the client used for other transctions.
     *
     * @param channelName
     */
    private static void createListener(String channelName) {
        Jedis jSubscriber = new Jedis();
        jSubscriber.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channelName, String message) {
                logger.info("Sending call to user about fraud " + message);
            }
        }, channelName);
    }

    /**
     * Defines a poolconfig
     * @return
     */
    private static JedisPoolConfig getPoolConfig() {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(128);
        poolConfig.setMinIdle(16);

        return poolConfig;
    }

    /**
     * Returns the services related to given namespace from the datastore
     *
     * @param namespace key
     * @return value associated with key
     */
    public List<String> getServicesList(String namespace) {

        try (Jedis jedis = jedisPool.getResource()) {
            String serviceList = jedis.get(TransactionUtil.NAMESPACE_PREFIX + namespace);
            if (serviceList == null || serviceList.isEmpty()) return null;

            return Arrays.asList(serviceList.split(","));
        }
    }

    /**
     * This happens after a transaction is allowed. We register the successful transaction in user's name.
     * Ideally fraud service is decoupled from user store. But I am storing it in local key value store to demonstrate use-case of daily transaction limit
     *
     * @param date   normally today's date because user has a daily limit
     * @param userId
     * @param amount
     * @throws Exception
     */
    public void registerTransaction(LocalDate date, String userId, Integer amount) throws Exception {
        try (Jedis jedis = jedisPool.getResource()) {
            String key = "amount." + userId + "." + date.format(formatter);
            Integer todaySpent = this.getAmountOnDate(date, userId);

            Integer tally = todaySpent + amount;
            jedis.set(key, String.valueOf(tally));
        }
    }

    /**
     * Return the total amount spent by user on given date from data store.
     *
     * @param date   for the given date
     * @param userId user's unique id
     * @return amount user's cumulative amount spent on day provided by date param
     * @throws Exception
     */
    public Integer getAmountOnDate(LocalDate date, String userId) throws Exception {


        String key = "amount." + userId + "." + date.format(formatter);
        Integer amt;
        try (Jedis jedis = jedisPool.getResource()) {
            String amount = jedis.get(key);
            amt = Integer.parseInt(amount);
        } catch (NumberFormatException ex) {
            amt = 0;
        }

        return amt;
    }

    /**
     * This is used by the unit test. To clean up dummy user's transactions afetr testing
     *
     * @param date
     * @param userId
     */
    public void cleanupUserTransactions(LocalDate date, String userId) {
        try (Jedis jedis = jedisPool.getResource()) {
            String key = "amount." + userId + "." + date.format(formatter);
            jedis.set(key, "0");
        }
    }

    /**
     * Publishes message to the given channel
     *
     * @param channelName
     * @param message
     */
    public void sendAlertMessage(String channelName, String message) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.publish(channelName, message);
        }
    }

}

