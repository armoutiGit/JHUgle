package hw7.hashing;

import hw7.Map;
import java.util.Iterator;

public class HashMap<K, V> implements Map<K, V> {
  private Map<K, V> map;

  /**
   * Instantiate HashMap.
   */
  public HashMap() {
    // TODO replace JdkHashMap with ChainingHashMap or OpenAddressingHashMap
    //  for experimentation.
    map = new JdkHashMap<>();
  }

  @Override
  public void insert(K k, V v) throws IllegalArgumentException {
    map.insert(k,v);
  }

  @Override
  public V remove(K k) throws IllegalArgumentException {
    return map.remove(k);
  }

  @Override
  public void put(K k, V v) throws IllegalArgumentException {
    map.put(k,v);
  }

  @Override
  public V get(K k) throws IllegalArgumentException {
    return map.get(k);
  }

  @Override
  public boolean has(K k) {
    return map.has(k);
  }

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public Iterator<K> iterator() {
    return map.iterator();
  }
}
