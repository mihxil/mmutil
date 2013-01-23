package nl.vpro.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created with IntelliJ IDEA.
 * User: ricojansen
 * Date: 26-04-2012
 * Time: 14:48
 * To change this template use File | Settings | File Templates.
 */
public class ExpiringLruCacheTest {


    @Test
    public void testExpire() throws Exception {
        ExpiringLruCache<String,String> cache=new ExpiringLruCache<String, String>(4,200);
        cache.put("key","value");
        assertEquals("value",cache.get("key"));
        try {
            Thread.sleep(250);
        } catch (InterruptedException ie) {
            throw ie;
        }
        assertNull(cache.get("key"));
    }

    @Test
    public void testSize() {
        ExpiringLruCache<String,String> cache=new ExpiringLruCache<String, String>(4,200);
        cache.put("key1","value1");
        cache.put("key2","value2");
        cache.put("key3","value3");
        cache.put("key4","value4");
        assertEquals(4,cache.size());
        cache.put("key5","value5");
        assertEquals(4,cache.size());
    }

}
