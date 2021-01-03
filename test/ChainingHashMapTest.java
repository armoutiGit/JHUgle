import hashing.ChainingHashMap;

class ChainingHashMapTest extends MapTest {
  @Override
  protected Map<String, String> createMap() {
    return new ChainingHashMap<>();
  }

}
