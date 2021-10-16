package de.laurox.mc.util;

/**
 * Immutable Pair storing a corresponding Key and Value
 * @param <K> Key
 * @param <V> Value
 */
public class Pair<K, V> {

    private final K k;
    private final V v;

    public Pair(K k, V v) {
        this.k = k;
        this.v = v;
    }

    public K getK() {
        return k;
    }

    public V getV() {
        return v;
    }
}
