import hashing.ChainingHashMap;
import hashing.HashMap;
import hashing.OpenAddressingHashMap;
import search.Jhugle;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Driver {

  // Update this to any other data file for benchmarking experiments.
  private static String getDataFile() {
    return "random164.txt";
  }

  private static Jhugle createJhugle() {
    return new Jhugle(new ChainingHashMap<>());
  }

  /**
   * Execution starts here.
   *
   * @param args command-line arguments not used here.
   */
  public static void main(String[] args) {
    Jhugle jhUgle = createJhugle();

    URL url = Thread.currentThread().getContextClassLoader().getResource("");
    String path = url.getPath().replace("%20", " ")
        .replace("classes", "resources");
    Path dataFile = Paths.get(path, getDataFile());
    // On Windows, use this:
    // Path dataFile = Paths.get(path.substring(1), getDataFile());

    BasicProfiler.reset();
    BasicProfiler.start();
    jhUgle.buildSearchEngine(dataFile.toFile());
    String description = String.format("Processed %s", getDataFile());
    BasicProfiler.stop();
    System.out.println(BasicProfiler.getStatistics(description));

    jhUgle.runSearchEngine();
  }
}
