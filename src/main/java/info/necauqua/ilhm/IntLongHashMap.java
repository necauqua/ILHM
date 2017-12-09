package info.necauqua.ilhm;

import java.util.Arrays;

/**
 * Very simple hash map with open addressing, integer keys and long values.
 *
 * Note: {@link Integer#MIN_VALUE} cannot be used as a key in this implementation.
 */
public class IntLongHashMap {

    /**
     * long is a primitive, so it cannot be null.
     * This is a constant which indicates that
     * there were no associated value in this map.
     * Use {@link #contains} when you want to store this exact value.
     */
    public static final long NULL = Long.MIN_VALUE;

    private static final int EMPTY = Integer.MIN_VALUE;

    private int[] keys;
    private long[] values;

    private final float loadFactor;
    private int size = 0;

    /**
     * Creates new map with default start size of 16 and load factor of 0.75
     */
    public IntLongHashMap() {
        keys = new int[16];
        values = new long[16];
        loadFactor = 0.75F;
        Arrays.fill(keys, EMPTY);
    }

    /**
     * Creates new map with custom starting size and load factor.
     * Useful if you already know which number of entries to expect.
     *
     * @param startSize  starting size of the key and value arrays.
     * @param loadFactor determines how much of the array should be filled
     *                   to double it.
     */
    public IntLongHashMap(int startSize, float loadFactor) {
        keys = new int[startSize];
        values = new long[startSize];
        this.loadFactor = loadFactor;
        Arrays.fill(keys, EMPTY);
    }

    private void checkResize() {
        if(size >= Math.max(1, Math.min((int) (keys.length * loadFactor), keys.length - 1))) {
            int[] oldKeys = keys;
            long[] oldValues = values;
            keys = new int[keys.length * 2];
            values = new long[values.length * 2];
            Arrays.fill(keys, EMPTY);
            for(int i = 0; i < oldKeys.length; i++) {
                int key = oldKeys[i];
                if(key != EMPTY) {
                    put(key, oldValues[i]);
                }
            }
        }
    }

    private int index(int key) {
        int hash = (key >> 15) ^ key;
        return hash % keys.length;
    }

    /**
     * Size of this hash map.
     *
     * @return Number of key-value pairs stored in this map.
     */
    public int size() {
        return size;
    }

    /**
     * Assigns given value at given key in this hash map.
     * Remember that {@link Integer#MIN_VALUE} cannot be used as a key.
     *
     * @param key   integer key.
     * @param value long value to be assigned.
     */
    public void put(int key, long value) {
        checkResize();

        for(int i = index(key); ; ++i) {
            if(i == keys.length) {
                i = 0;
            }
            int k = keys[i];
            if(k == EMPTY) {
                keys[i] = key;
                values[i] = value;
                ++size;
                return;
            }
            if(k == key) {
                values[i] = value;
                return;
            }
        }
    }

    /**
     * Looks for a given key in this map and returns assigned value if any.
     * Returns {@link #NULL} if there was no such association.
     *
     * @param key key to look for.
     * @return assigned value or {@link #NULL}.
     */
    public long get(int key) {
        for(int i = index(key); ; ++i) {
            if(i == keys.length) {
                i = 0;
            }
            int k = keys[i];
            if(k == EMPTY) {
                return NULL;
            }
            if(k == key) {
                return values[i];
            }
        }
    }

    /**
     * Looks for a given key in this map and returns true if there is such association.
     * Useful when storing {@link #NULL} keys.
     *
     * @param key key to look for.
     * @return true if there is an associated value with this key.
     */
    public boolean contains(int key) {
        for(int i = index(key); ; ++i) {
            if(i == keys.length) {
                i = 0;
            }
            int k = keys[i];
            if(k == EMPTY) {
                return false;
            }
            if(k == key) {
                return true;
            }
        }
    }
}
