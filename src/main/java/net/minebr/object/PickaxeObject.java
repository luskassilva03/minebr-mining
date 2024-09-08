package net.minebr.object;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.List;
import java.util.Map;

@Getter
public class PickaxeObject {
    private final Material material;
    private final String name;
    private final List<String> lore;
    private final Map<String, Integer> enchants;

    public PickaxeObject(Material material, String name, List<String> lore, Map<String, Integer> enchants) {
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.enchants = enchants;
    }

    // Método para obter o nível de um encantamento pelo nome
    public int getEnchantmentLevel(String enchantmentName) {
        return enchants.get(enchantmentName);
    }

    // Método para obter o Enchantment pelo nome
    public Enchantment getEnchantment(String enchantmentName) {
        for (Enchantment enchantment : Enchantment.values()) {
            if (enchantment.getName().equalsIgnoreCase(enchantmentName)) {
                return enchantment;
            }
        }
        return null; // Retorna null se não encontrar o encantamento
    }
}
