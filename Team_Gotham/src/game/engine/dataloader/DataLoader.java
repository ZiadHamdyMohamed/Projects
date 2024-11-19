package game.engine.dataloader;

import game.engine.titans.*;
import game.engine.weapons.*;
import java.io.*;
import java.util.*;

public class DataLoader {

    private static final String TITANS_FILE_NAME="titans.csv";
    private static final String WEAPONS_FILE_NAME="weapons.csv";

    public static HashMap<Integer, TitanRegistry> readTitanRegistry() throws IOException {
        HashMap<Integer, TitanRegistry> titanRegistry = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(TITANS_FILE_NAME));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            int code = Integer.parseInt(data[0]);
            if(!titanRegistry.containsKey(code)){
                int baseHealth = Integer.parseInt(data[1]);
                int baseDamage = Integer.parseInt(data[2]);
                int heightInMeters = Integer.parseInt(data[3]);
                int speed = Integer.parseInt(data[4]);
                int resourcesValue = Integer.parseInt(data[5]);
                int dangerLevel = Integer.parseInt(data[6]);
                titanRegistry.put(code, new TitanRegistry(code, baseHealth, baseDamage, heightInMeters, speed, resourcesValue, dangerLevel));
            }
        }
        reader.close();
        return titanRegistry;
    }
    public static HashMap<Integer, WeaponRegistry> readWeaponRegistry() throws IOException{
        HashMap<Integer, WeaponRegistry> weaponRegistry = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(WEAPONS_FILE_NAME));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            int code = Integer.parseInt(data[0]);
            if (!weaponRegistry.containsKey(code)) {
                int price = Integer.parseInt(data[1]);
                int damage = Integer.parseInt(data[2]);
                String name = data[3];
                if (data.length == 6) {
                    int minRange = Integer.parseInt(data[4]);
                    int maxRange = Integer.parseInt(data[5]);
                    weaponRegistry.put(code, new WeaponRegistry(code, price, damage, name, minRange, maxRange));
                } else {
                    weaponRegistry.put(code, new WeaponRegistry(code, price, damage, name));
                }
            }
        }
        reader.close();
        return weaponRegistry;
    }


}
