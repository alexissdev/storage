package com.pixeldv.storage.redis.channel;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pixeldv.storage.redis.messenger.Messenger;
import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

public class RedisChannel<T> implements Channel<T> {

    private final String parentChannel;
    private final String serverId;
    private final String name;
    private final TypeToken<T> type;
    private final Messenger messenger;
    private final JedisPool jedisPool;
    private final Set<ChannelListener<T>> listeners;

    private final Gson gson;

    public RedisChannel(
            String parentChannel, String serverId,
            String name, TypeToken<T> type,
            Messenger messenger,
            JedisPool jedisPool,
            Gson gson
    ) {
        this.parentChannel = parentChannel;
        this.serverId = serverId;
        this.name = name;
        this.type = type;
        this.messenger = messenger;
        this.jedisPool = jedisPool;
        this.gson = gson;
        this.listeners = new HashSet<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Messenger getMessenger() {
        return messenger;
    }

    @Override
    public TypeToken<T> getType() {
        return type;
    }

    @Override
    public Channel<T> sendMessage(T message, @Nullable String targetServer) {
        JsonElement jsonElement = gson.toJsonTree(message, type.getType());
        JsonObject objectToSend = new JsonObject();

        objectToSend.addProperty("channel", name);
        objectToSend.addProperty("server", serverId);

        if (targetServer != null) {
            objectToSend.addProperty("targetServer", targetServer);
        }

        objectToSend.add("object", jsonElement);
        String json = objectToSend.toString();

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.publish(parentChannel, json);
        }

        return this;
    }

    @Override
    public Channel<T> addListener(ChannelListener<T> channelListener) {
        listeners.add(channelListener);
        return this;
    }

    @Override
    public void listen(String server, T object) {
        for (ChannelListener<T> listener : listeners) {
            listener.listen(this, server, object);
        }
    }

    @Override
    public Set<ChannelListener<T>> getListeners() {
        return listeners;
    }
}
