package com.marxist.leftwing_community.util;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 缓存处理工具类
 */
public class CacheUtils {

    // 键值对集合
    private final static Map<String, Entity> map = new ConcurrentHashMap<>();
    // 定时器线程池, 用于清除过期缓存
    private final static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    /**
     * 添加缓存
     */
    public synchronized static void put(String key, Object data) {
        CacheUtils.put(key, data, 0);
    }

    /**
     * 添加缓存
     * 过期时间: 单位毫秒, 0表示无限长
     */
    public synchronized static void put(String key, Object data, long expire) {
        // 清除原键值对
        CacheUtils.remove(key);
        // 设置过期时间
        if (expire > 0) {
            Future future = executor.schedule(() -> {
                // 过期后清除该键值对
                synchronized (CacheUtils.class) {
                    map.remove(key);
                }
            }, expire, TimeUnit.MILLISECONDS);
            map.put(key, new Entity(data, future));
        } else {
            // 不设置过期时间
            map.put(key, new Entity(data, null));
        }
    }

    /**
     * 读取缓存
     */
    public synchronized static Object get(String key) {
        Entity entity = map.get(key);
        return entity == null ? null : entity.getValue();
    }

    /**
     * 读取缓存
     * clazz 值类型
     */
    public synchronized static <T> T get(String key, Class<T> clazz) {
        return clazz.cast(CacheUtils.get(key));
    }

    /**
     * 清除指定缓存
     * 返回值为指定key的value
     */
    public synchronized static Object remove(String key) {
        // 清除指定缓存数据
        Entity entity = map.remove(key);
        if (entity == null)
            return null;
        // 清除指定键值对定时器
        Future future = entity.getFuture();
        if (future != null)
            future.cancel(true);
        return entity.getValue();
    }

    /**
     * 清除所有缓存
     */
    public synchronized static void removeAll() {
        map.clear();
    }

    /**
     * 查询当前缓存的键值对数量
     */
    public synchronized static int size() {
        return map.size();
    }

    /**
     * 缓存实体类
     */
    private static class Entity {
        // 键值对的value
        private Object value;
        // 定时器的future
        private Future future;

        /**
         * 创建实体类
         */
        public Entity(Object value, Future future) {
            this.value = value;
            this.future = future;
        }

        /**
         * 获取value值
         */
        public Object getValue() {
            return value;
        }

        /**
         * 获取future对象
         */
        public Future getFuture() {
            return future;
        }
    }

}
