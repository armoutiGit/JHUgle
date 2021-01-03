package hw7.hashing;

import hw7.Map;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class ChainingHashMap<K, V> implements Map<K, V> {
  private LinkedList<K> myKeys = new LinkedList<>(); //for iterator
  //private Object[] arr;
  private int howMany;
  private int capacity;
  private int[] primes = {11, 23, 47, 97, 199, 401, 797, 1583, 3061, 6131,
      11617, 23369, 46349, 90053, 198337, 411527, 823117, 1637429, 3283727,
      6585079, 13170161, 26340329, 52680679, 105361363, 210722737, 421398007,
      842878919, 1685769431};

  // an array of buckets
  //private Bucket<K, V>[] buckets;
  private LinkedList<Object[]>[] myMap;

  /**
   * Instantiate chaining HashMap.
   */
  public ChainingHashMap() {
    this(11);
  }

  /**
   * Instantiate chaining HashMap with a new Capacity.
   * called when rehashing
   * @param newCap new capacity of myMap
   */
  public ChainingHashMap(int newCap) {
    this.howMany = 0;
    this.capacity = newCap; //default starting size
    myMap = new LinkedList[newCap];
  }

  private float load() {
    return (float) howMany / capacity;
  }

  private void rehash() {
    int newCap = 0;
    for (int i : primes) {
      if (i > capacity) {
        newCap = i;
        break;
      }
    }
    growMap(newCap);
  }

  //helper method to resize hashMap
  private void growMap(int newCap) {
    ChainingHashMap<K, V> newMap = new ChainingHashMap<>(newCap);
    //arr = new Object[newCap];
    for (int i = 0; i < capacity; i++) {
      LinkedList<Object[]> data = myMap[i];
      if (myMap[i] != null) {
        for (Object[] temp : myMap[i]) {
          if (temp[0] != null) {
            newMap.insert((K) temp[0], (V) temp[1]);
            //arr[i] = (K) temp[0];
          }
        }
      }
    }
    capacity = newCap;
    myMap = newMap.myMap;
  }

  @Override
  public void insert(K k, V v) throws IllegalArgumentException {
    // TODO Implement Me!
    if (load() > 0.75) {
      rehash();
    }
    if ((k == null) || has(k)) {
      throw new IllegalArgumentException();
    }
    int hash = hash(k);
    if (myMap[hash] == null) {
      myMap[hash] = new LinkedList<Object[]>();
    }
    Object[] valPair = {k, v};
    myMap[hash].add(valPair);
    howMany++;
    myKeys.add(k);

  }

  @Override
  public V remove(K k) throws IllegalArgumentException {
    // TODO Implement Me!
    if (!has(k)) {
      throw new IllegalArgumentException();
    }

    int hash = hash(k);
    LinkedList<Object[]> myBucket = myMap[hash];
    V val;

    Iterator<Object[]> iter = myBucket.iterator();
    while (iter.hasNext()) {
      Object[] temp = iter.next();
      if (temp[0] == k) {
        val = (V) temp[1];
        iter.remove();
        myKeys.remove(k);
        howMany--;
        return val;
      }
    }
    return null;
  }

  @Override
  public void put(K k, V v) throws IllegalArgumentException {
    // TODO Implement Me!
    Object[] obj = find(k);
    if (obj == null) {
      throw new IllegalArgumentException();
    }
    obj[1] = v;
  }

  @Override
  public V get(K k) throws IllegalArgumentException {
    // TODO Implement Me!
    Object[] obj = find(k);
    if (obj == null) {
      throw new IllegalArgumentException();
    }
    return (V) obj[1];
  }

  @Override
  public boolean has(K k) {
    // TODO Implement Me!
    return find(k) != null;
  }

  private Object[] find(K k) {
    //the bucket we want to look inside
    if (k == null) {
      return null;
    }
    int hash = hash(k);
    if (myMap[hash] == null) {
      //chain does not exist!
      return null;
    }
    for (Object[] temp : myMap[hash]) {
      if (k.equals(temp[0])) {
        return temp;
      }
    }
    return null;
  }

  @Override
  public int size() {
    // TODO Implement Me!
    return howMany;
  }

  private int hash(K k) {
    //this deals with negative hash
    return (k.hashCode() & 0x7fffffff) % capacity;
  }

  @Override
  public Iterator<K> iterator() {
    // TODO Implement Me!
    return new ChainingIterator();
  }

  private class ChainingIterator implements Iterator<K> {
    int idx;

    ChainingIterator() {
      idx = 0;
    }

    @Override
    public boolean hasNext() {
      return idx != howMany;
    }

    @Override
    public K next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      return myKeys.get(idx++);
    }
  }


}
