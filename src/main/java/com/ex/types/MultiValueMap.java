package com.ex.types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MultiValueMap <K, V> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Map<K, Set<V>> mappings = new HashMap<K, Set<V>>();

    public Set<V> getValues(K key) {
        return mappings.get(key);
    }

    public Boolean putValue(K key, V value) {
        System.out.println("KEY IS: " + key.toString());
        System.out.println("VALUE IS: " + value.toString());
        Set<V> target = mappings.get(key);
        if(target == null) {
            logger.info("MultiValueMap::putValue() => No matching key found.  Creating new key: {}", key.toString());
            target = new HashSet<V>();
            mappings.put(key, target);
        }
        return target.add(value);
    }

    public Boolean removeValue(K key, V value) {
        Set<V> target = mappings.get(key);
        if(target == null) {
            logger.info("MultiValueMap::removeValue() === ERROR ===No matching key found. ");
            return false;
        }
        else return target.remove(value);
    }
}
