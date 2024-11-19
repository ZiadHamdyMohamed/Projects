package game.engine.weapons.factory;

import java.io.*;
import java.util.*;

import game.engine.exceptions.*;
import game.engine.weapons.*;
import static game.engine.dataloader.DataLoader.readWeaponRegistry;

public class WeaponFactory {
    private final HashMap<Integer, WeaponRegistry> weaponShop;

    public WeaponFactory() throws IOException {
       this.weaponShop= readWeaponRegistry();
    }



    public HashMap<Integer, WeaponRegistry> getWeaponShop() {
        return weaponShop;
    }
    public FactoryResponse buyWeapon(int resources, int weaponCode) throws InsufficientResourcesException {
        WeaponRegistry weaponRegistry = weaponShop.get(weaponCode);
        if (weaponRegistry == null) {
            throw new IllegalArgumentException("Invalid weapon code: " + weaponCode);
        }

        int weaponPrice = weaponRegistry.getPrice();
        if (resources < weaponPrice) {
            throw new InsufficientResourcesException(resources);
        }

        Weapon weapon;
        switch (weaponCode) {
            case 1:
                weapon = new PiercingCannon(weaponRegistry.getDamage());
                break;
            case 2:
                weapon = new SniperCannon(weaponRegistry.getDamage());
                break;
            case 3:
                weapon = new VolleySpreadCannon(weaponRegistry.getDamage(), weaponRegistry.getMinRange(), weaponRegistry.getMaxRange());
                break;
            case 4:
                weapon = new WallTrap(weaponRegistry.getDamage());
                break;
            default:
                throw new IllegalArgumentException("Unsupported weapon code: " + weaponCode);
        }

        return new FactoryResponse(weapon, resources - weaponPrice);
    }

    public void addWeaponToShop(int code, int price) {
        weaponShop.put(code, new WeaponRegistry(code, price));
    }

    public void addWeaponToShop(int code, int price, int damage, String name) {
        weaponShop.put(code, new WeaponRegistry(code, price, damage, name));
    }

    public void addWeaponToShop(int code, int price, int damage, String name, int minRange, int maxRange) {
        weaponShop.put(code, new WeaponRegistry(code, price, damage, name, minRange, maxRange));
    }


}
