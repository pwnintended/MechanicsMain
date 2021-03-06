package me.deecaad.weaponmechanics.weapon.info;

import me.deecaad.compatibility.CompatibilityAPI;
import me.deecaad.core.file.Serializer;
import me.deecaad.weaponmechanics.WeaponMechanics;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WeaponConverter implements Serializer<WeaponConverter> {

    private boolean type;
    private boolean name;
    private boolean lore;
    private boolean enchantments;

    /**
     * Empty constructor to be used as serializer
     */
    public WeaponConverter() { }

    public WeaponConverter(boolean type, boolean name, boolean lore, boolean enchantments) {
        this.type = type;
        this.name = name;
        this.lore = lore;
        this.enchantments = enchantments;
    }

    /**
     * Checks whether or not weapon stack is matching with the other item stack.
     * If this returns true, then other item should he converted to weapon stack
     *
     * @param weaponStack the weapon item
     * @param other the other item
     * @return true only if weapon stack matches with other item enough (depending on this converter variable values)
     */
    public boolean isMatch(ItemStack weaponStack, ItemStack other) {
        double version = CompatibilityAPI.getVersion();
        if (this.type) {
            if (weaponStack.getType() != other.getType()) {
                return false;
            }
            if (version < 1.13 && weaponStack.getData().getData() != other.getData().getData()) {
                return false;
            }
        }
        ItemMeta weaponMeta = weaponStack.getItemMeta();
        ItemMeta otherMeta = other.getItemMeta();
        if (this.name) {
            if (weaponMeta.hasDisplayName() != otherMeta.hasDisplayName()
                    || (weaponMeta.hasDisplayName() && !weaponMeta.getDisplayName().equals(otherMeta.getDisplayName()))) {
                return false;
            }
        }
        if (this.lore) {
            if (weaponMeta.hasLore() != otherMeta.hasLore()
                    || (weaponMeta.hasLore() && !weaponMeta.getLore().equals(otherMeta.getLore()))) {
                return false;
            }
        }
        if (this.enchantments) {
            if (weaponMeta.hasEnchants() != otherMeta.hasEnchants()
                    || (weaponMeta.hasEnchants() && !equals(weaponMeta.getEnchants(), otherMeta.getEnchants()))) {
                // If weapon would have enchantments, but other doesn't
                // OR
                // If weapon and other enchantments doesn't match
                return false;
            }
        }
        return true;
    }

    private static boolean equals(Map<Enchantment, Integer> ench1, Map<Enchantment, Integer> ench2) {
        if (ench1 == ench2)
            return true;
        else if (ench1.size() != ench2.size())
            return false;
        else {
            List<Map.Entry<Enchantment, Integer>> list1 = new ArrayList<>(ench1.entrySet());
            List<Map.Entry<Enchantment, Integer>> list2 = new ArrayList<>(ench2.entrySet());

            for (int i = 0; i < list1.size(); i++) {
                Map.Entry<Enchantment, Integer> entry1 = list1.get(i);
                Map.Entry<Enchantment, Integer> entry2 = list2.get(i);

                if (!entry1.getKey().equals(entry2.getKey()))
                    return false;
                else if (!entry1.getValue().equals(entry2.getValue()))
                    return false;
            }

            return true;
        }
    }

    @Override
    public String getKeyword() {
        return "Weapon_Converter_Check";
    }

    @Override
    public WeaponConverter serialize(File file, ConfigurationSection configurationSection, String path) {
        boolean type = configurationSection.getBoolean(path + ".Type");
        boolean name = configurationSection.getBoolean(path + ".Name");
        boolean lore = configurationSection.getBoolean(path + ".Lore");
        boolean enchantments = configurationSection.getBoolean(path + ".Enchantments");
        if (!type && !name && !lore && !enchantments) {
            return null;
        }

        WeaponMechanics.getWeaponHandler().getInfoHandler().addWeaponWithConvert(path.split("\\.")[0]);

        return new WeaponConverter(type, name, lore, enchantments);
    }
}