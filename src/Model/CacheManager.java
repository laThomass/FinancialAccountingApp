package Model;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheManager {
  private static final String CACHE_FILE = "stock_data_cache.ser";

  public static void saveCache(Map<String, List<Stock>> cache) {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CACHE_FILE))) {
      oos.writeObject(cache);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static Map<String, List<Stock>> loadCache() {
    File cacheFile = new File(CACHE_FILE);
    if (!cacheFile.exists()) {
      return new HashMap<>();
    }
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CACHE_FILE))) {
      return (Map<String, List<Stock>>) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      return new HashMap<>();
    }
  }
}
