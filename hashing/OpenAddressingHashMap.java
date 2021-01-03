package hw7.hashing;

import hw7.Map;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class OpenAddressingHashMap<K, V> implements Map<K, V> {

  private Object[] arr;
  private int howMany;
  private int capacity;
  private Object[][] myMap;
  private boolean[] tombstone;
  private int[] primes = {11, 23, 47, 97, 199, 401, 797, 1583, 3061, 6131,
      11617, 23369, 46349, 90053, 198337, 411527, 823117, 1637429, 3283727,
      6585079, 13170161, 26340329, 52680679, 105361363, 210722737, 421398007,
      842878919, 1685769431};

  /**
   * Instantiate open addressing HashMap.
   */
  public OpenAddressingHashMap() {
    this(11);
  }

  /**
   * Instantiate chaining HashMap with a new Capacity.
   * called when rehashing
   * @param newCap new capacity of myMap
   */
  public OpenAddressingHashMap(int newCap) {
    this.arr = new Object[newCap];
    this.howMany = 0;
    this.capacity = newCap;
    this.myMap = new Object[newCap][2];
    this.tombstone = new boolean[newCap];
    for (int i = 0; i < newCap; i++) {
      tombstone[i] = false;
    }
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
    OpenAddressingHashMap<K, V> newMap = new OpenAddressingHashMap<>(newCap);
    arr = new Object[newCap];
    for (int i = 0; i < capacity; i++) {
      if (myMap[i][0] != null) {
        newMap.insert((K) myMap[i][0], (V) myMap[i][1]);
        arr[i] = (K) myMap[i][0];
      }
    }
    myMap = newMap.myMap;
    capacity = newCap;
    tombstone = newMap.tombstone;
  }

  @Override
  public void insert(K k, V v) throws IllegalArgumentException {
    if (load() > 0.5) {
      rehash();
    }
    if (k == null) {
      throw new IllegalArgumentException();
    }
    insertThisK(k, v);
  }

  private void insertThisK(K k, V v) {
    int hash = hash(k);
    int index = hash;
    int i = 0;
    do {
      if (myMap[index][0] == null) {
        myMap[index] = new Object[] {k, v};
        tombstone[index] = false;
        arr[index] = k; //for iterator;
        howMany++;
        return;
      }
      if (k.equals(myMap[index][0])) {
        //already exists!
        throw new IllegalArgumentException();
      }
      i++;
      index = (hash + (i * i)) % capacity;
    } while (index != hash);
  }

  @Override
  public V remove(K k) throws IllegalArgumentException {
    // TODO Implement Me!
    int index = find(k);
    if (index == -1) {
      throw new IllegalArgumentException();
    }
    tombstone[index] = true;
    myMap[index][0] = null;
    arr[index] = null; //for iterator
    howMany--;
    return (V) myMap[index][1];
  }

  @Override
  public void put(K k, V v) throws IllegalArgumentException {
    // TODO Implement Me!
    int index = find(k);
    if (index == -1) {
      throw new IllegalArgumentException();
    }
    myMap[index][1] = v;
  }

  @Override
  public V get(K k) throws IllegalArgumentException {
    // TODO Implement Me!
    int index = find(k);
    if (index == -1) {
      throw new IllegalArgumentException();
    }
    return (V) myMap[index][1];
  }

  @Override
  public boolean has(K k) {
    // TODO Implement Me!
    return find(k) != -1;
  }

  private int safeMod(int a, int b) {
    int m = a % b;
    return (m >= 0) ? m : (m + b);
  }

  private int find(K k) throws IllegalArgumentException {
    if (k == null) {
      return -1;
    }
    int hash = hash(k);
    int j = 0;
    int i = hash;
    while (tombstone[i] || (myMap[i][0] != null)) {
      i = safeMod((hash + (j * j)), capacity);
      if (myMap[i] != null) {
        if (k.equals(myMap[i][0])) {
          return i;
        }
      } else {
        return -1;
      }
      j++;
    }
    return -1;
  }

  @Override
  public int size() {
    // TODO Implement Me!
    return howMany;
  }

  @Override
  public Iterator<K> iterator() {
    // TODO Implement Me!
    return new OpenAddressingIterator();
  }

  private int hash(K k) {
    //this deals with negative hash
    return (k.hashCode() & 0x7fffffff) % capacity;
  }

  private class OpenAddressingIterator implements Iterator<K> {
    Iterator<K> it;
    int idx;
    int numElements;

    OpenAddressingIterator() {
      idx = 0;
      numElements = 0;
    }

    @Override
    public boolean hasNext() {
      return howMany != numElements;
    }

    @Override
    public K next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      while (arr[idx] == null) {
        idx++;
      }
      numElements++;
      return (K) arr[idx++];
    }
  }

}
