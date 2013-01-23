/*
 * Copyright (C) 2012 All rights reserved
 * VPRO The Netherlands
 */
package nl.vpro.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User: rico
 * Date: 05/01/2012
 */
class LruCache<A, B> extends LinkedHashMap<A, B> {
    private final int maxEntries;

    public LruCache(final int maxEntries) {
        super(maxEntries + 1, 1.0f, true);
        this.maxEntries = maxEntries;
    }

    @Override
    protected boolean removeEldestEntry(final Map.Entry<A, B> eldest) {
        return super.size() > maxEntries;
    }
}
