/*
 * Copyright (C) 2012 All rights reserved
 * VPRO The Netherlands
 */
package nl.vpro.util;

import com.google.common.collect.UnmodifiableIterator;

import java.util.*;

/**
 * User: rico
 * Date: 05/01/2012
 */
public class ExpiringLruCache<K, V> implements Map<K, V> {
    private final long lifeTime;
    private long hits = 0;
    private long misses = 0;
    private int expired = 0;
    private final LruCache<K, CacheEntry> cache;

    public ExpiringLruCache(int maxEntries, long lifeTime) {
        this.lifeTime = lifeTime;
        this.cache = new LruCache<K, CacheEntry>(maxEntries);
    }

    public class CacheEntry {
        final V value;
        final long expireTime;

        public CacheEntry(V value) {
            this.value = value;
            this.expireTime = System.currentTimeMillis() + lifeTime;
        }

        public boolean isExpired() {
            return expireTime < System.currentTimeMillis();
        }
        public V getValue() {
            return value;
        }
    }


    @Override
    public int size() {
        return cache.size();
    }

    @Override
    public boolean isEmpty() {
        return cache.isEmpty();
    }

    @Override
    public synchronized boolean containsKey(Object key) {
        return cache.containsKey(key);
    }

    @Override
    public synchronized boolean containsValue(Object value) {
        if (value != null) {
            for (CacheEntry entry : cache.values()) {
                if (entry.value != null) {
                    if (entry.value.equals(value)) {
                        return true;
                    }
                } else {
                    if (entry.value == null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Gets the entry with given key. If the entry is expired, it will be returned, but also removed.
     * @param key
     * @return
     */
    public synchronized CacheEntry getEntry(K key) {
        CacheEntry entry = cache.get(key);
        if (entry == null) {
            misses++;
            return null;
        } else if (entry.isExpired()) {
            expired++;
            cache.remove(key);
            return entry;
        } else {
            hits++;
            return entry;
        }
    }


    @Override
    public synchronized V get(Object key) {
        CacheEntry entry = getEntry((K) key);
        return entry == null || entry.isExpired() ? null : entry.value;
    }

    @Override
    public synchronized V put(K key, V value) {
        CacheEntry entry = cache.get(key);
        if (entry == null) {
            entry = new CacheEntry(value);
            cache.put(key, entry);
        }
        return value;
    }

    private  void sweep() {
        Iterator<Entry<K, CacheEntry>> iterator = cache.entrySet().iterator();
        while(iterator.hasNext()) {
            Entry<K, CacheEntry> entry = iterator.next();
            CacheEntry e = entry.getValue();
            if (e != null) {
                if (e.isExpired()) {
                    iterator.remove();
                }
            }
        }
    }

    @Override
    public synchronized V remove(Object value) {
        CacheEntry removed = cache.remove(value);
        return removed == null ? null : removed.value;
    }

    @Override
    public synchronized void putAll(Map<? extends K, ? extends V> map) {
        for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public Set<K> keySet() {
        // LinkedHashMap does funky stuff with its keyset ??
        return cache.keySet();
    }

    @Override
    public Collection<V> values() {
        final Collection<CacheEntry> backing = cache.values();
        return new AbstractCollection<V>() {
            @Override
            public Iterator<V> iterator() {
                final Iterator<CacheEntry> iterator = backing.iterator();
                return new UnmodifiableIterator<V> () {
                    @Override
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }
                    @Override
                    public V next() {
                        return iterator.next().value;
                    }
                };
            }
            @Override
            public int size() {
                return backing.size();
            }
        };
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        final Set<Entry<K, CacheEntry>> backing = cache.entrySet();
        return new AbstractSet<Entry<K, V>>() {
            @Override
            public Iterator<Entry<K, V>> iterator() {
                final Iterator<Entry<K, CacheEntry>> iterator = backing.iterator();
                return new UnmodifiableIterator<Entry<K, V>>() {
                    @Override
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }
                    @Override
                    public Entry<K, V> next() {
                        Entry<K, CacheEntry> entry = iterator.next();
                        return new AbstractMap.SimpleImmutableEntry<K, V>(entry.getKey(), entry.getValue().value);
                    }
                };
            }
            @Override
            public int size() {
                return backing.size();
            }
        };
    }


    public long getHits() {
        return hits;
    }

    public long getMisses() {
        return misses;
    }

    public int getExpired() {
        return expired;
    }
}
