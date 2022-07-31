package info.necauqua.ilhm;

/**
 * Very simple hash map with open addressing, integer keys and long values.
 *
 * Note: {@link Integer#MIN_VALUE} cannot be used as a key in this
 * implementation.
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

    private int[] data;

    private final float loadFactor;
    private int filled = 0, maxFilled = 0, size = 0;

    /**
     * Creates new map with default start size of 16 and load factor of 0.75
     */
    public IntLongHashMap() {
        this(4, 0.75F);
    }

    /**
     * Creates new map with custom starting size and load factor.
     * Useful if you already know which number of entries to expect.
     *
     * @param startPow   starting size of data array is determined as (2 ^ startPow)
     *                   * 3.
     * @param loadFactor determines how much of the array should be filled
     *                   to double it.
     */
    public IntLongHashMap(int startPow, float loadFactor) {
        size = 1 << startPow;
        maxFilled = (int) (size * loadFactor);
        data = new int[size * 3];
        this.loadFactor = loadFactor;
        for (int i = 0, len = data.length; i < len; i += 3) {
            data[i] = EMPTY;
        }
    }

    private void checkResize() {
        if (filled >= maxFilled) {
            size <<= 1;
            maxFilled = (int) (size * loadFactor);
            int[] old = data;
            data = new int[size * 3];
            for (int i = 0, len = data.length; i < len; i += 3) {
                data[i] = EMPTY;
            }
            filled = 0;
            for (int i = 0, len = old.length; i < len; i += 3) {
                int key = old[i];
                if (key != EMPTY) {
                    put(key, ((long) old[i + 1]) << 32 | old[i + 2] & 0xFFFFFFFFL);
                }
            }
        }
    }

    private int index(int key) {
        return key & (size - 1); // this also strips sign bit
    }

    /**
     * Size of this hash map.
     *
     * @return Number of key-value pairs stored in this map.
     */
    public int size() {
        return filled;
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

        for (int i = index(key) * 3, len = data.length;; i += 3) {
            if (i == len) {
                i = 0;
            }
            int k = data[i];
            if (k == EMPTY) {
                data[i] = key;
                data[i + 1] = (int) (value >> 32);
                data[i + 2] = (int) value;
                ++filled;
                return;
            }
            if (k == key) {
                data[i + 1] = (int) (value >> 32);
                data[i + 2] = (int) value;
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
        for (int i = index(key) * 3, len = data.length;; i += 3) {
            if (i == len) {
                i = 0;
            }
            int k = data[i];
            if (k == EMPTY) {
                return NULL;
            }
            if (k == key) {
                return ((long) data[i + 1]) << 32 | data[i + 2] & 0xFFFFFFFFL;
            }
        }
    }

    /**
     * Looks for a given key in this map and returns true if there is such
     * association.
     * Useful when storing {@link #NULL} keys.
     *
     * @param key key to look for.
     * @return true if there is an associated value with this key.
     */
    public boolean contains(int key) {
        for (int i = index(key) * 3, len = data.length;; i += 3) {
            if (i == len) {
                i = 0;
            }
            int k = data[i];
            if (k == EMPTY) {
                return false;
            }
            if (k == key) {
                return true;
            }
        }
    }
}
