package com.fintech.transactions;

import com.fintech.transactions.dao.RedisClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TransactionsApplicationTests {

	private static Jedis jedis;

	@BeforeAll
	public static void init()
	{
		jedis = new Jedis();
	}
	@Test
	public void testDatabaseRunning() {
		jedis.set("key","value");
		assertEquals("value",jedis.get("key"),"Please check if redis is running at the localhost default port");
	}

	@AfterAll
	public static void cleanup()
	{
		jedis.close();
	}



}
