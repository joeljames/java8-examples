package examples;

import java.util.HashMap;
import java.util.Map;

public class ByReference {

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");

        ConfigHolder configHolder = new ConfigHolder(map);
        System.out.println("The config is : " + configHolder.getConfig());
        map.put("key2", "value2");
        System.out.println("The config after modifying the initial map : " + configHolder.getConfig());
    }

    static class ConfigHolder {
        private final Map<String, String> conf;


        ConfigHolder(Map<String, String> conf) {
            this.conf = conf;
        }

        public Map<String, String> getConfig() {
            return conf;
        }
    }

}
