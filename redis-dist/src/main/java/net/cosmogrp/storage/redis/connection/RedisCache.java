package net.cosmogrp.storage.redis.connection;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

public class RedisCache {

    private final String name;
    private final JedisPool jedisPool;

    public RedisCache(String name, JedisPool jedisPool) {
        this.name = name;
        this.jedisPool = jedisPool;
    }

    public List<String> getAll(String table) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hvals(makeTable(table));
        }
    }
    
    public void set(String table, String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(makeTable(table), key, value);
        }
    }

    public String get(String table, String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hget(makeTable(table), key);
        }
    }

    public void del(String table, String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hdel(makeTable(table), key);
        }
    }
    
    public String makeTable(String table) {
        return name + ":" + table;
    }
}
