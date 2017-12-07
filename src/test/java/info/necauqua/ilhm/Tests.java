package info.necauqua.ilhm;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class Tests {

    @Test
    public void common() {
        IntLongHashMap map = new IntLongHashMap();

        map.put(123, 11111L);
        map.put(321, 2222222222L);
        map.put(231, 333333333333333L);

        assertEquals(IntLongHashMap.NULL, map.get(42));

        assertEquals(11111L, map.get(123));
        assertEquals(2222222222L, map.get(321));
        assertEquals(333333333333333L, map.get(231));
    }

    @Test
    public void size() {
        IntLongHashMap map = new IntLongHashMap();

        assertEquals(0, map.size());

        map.put(123, 11111L);

        assertEquals(1, map.size());

        map.put(321, 2222222222L);
        map.put(231, 333333333333333L);

        assertEquals(3, map.size());

        map.put(321, 333333333333333L);
        map.put(231, 2222222222L);

        assertEquals(3, map.size());
    }

    @Test
    public void replace() {
        IntLongHashMap map = new IntLongHashMap();

        map.put(123, 11111L);
        map.put(321, 2222222222L);

        assertEquals(11111L, map.get(123));
        assertEquals(2222222222L, map.get(321));

        map.put(123, 123456789L);
        map.put(321, 987654321L);

        assertEquals(123456789L, map.get(123));
        assertEquals(987654321L, map.get(321));
    }

    @Test
    public void highLoad() {
        IntLongHashMap map = new IntLongHashMap();

        final int howMuch = 100000;

        for(int i = 0; i < howMuch; i++) {
            int key = ("super-random-key-" + i).hashCode();
            long value = ("value-is-even-more-random-" + i).hashCode();
            map.put(key, value);
        }

        for(int i = 0; i < howMuch; i++) {
            int key = ("super-random-key-" + i).hashCode();
            long value = ("value-is-even-more-random-" + i).hashCode();
            assertEquals("same on " + (i + 1) + " pass", value, map.get(key));
        }
    }

    @Test
    public void random() {
        Map<Integer, Long> ref = new HashMap<>();
        IntLongHashMap map = new IntLongHashMap();

        Random rng = new Random();

        final int howMuch = 100000;
        for(int i = 0; i < howMuch; i++) {
            int key = rng.nextInt(howMuch);
            map.put(key, i);
            ref.put(key, (long) i);
            assertEquals("same on " + (i + 1) + " pass", ref.get(key).longValue(), map.get(key));
        }
    }
}
