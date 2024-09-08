package net.minebr.inventory;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.simple.SimpleInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import net.minebr.MiningMain;
import net.minebr.api.builder.ItemBuilder;
import net.minebr.api.builder.SkullBuilder;
import net.minebr.object.Booster;
import net.minebr.object.PlayerObject;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class InventoryMain extends SimpleInventory {
    private final MiningMain main;

    public InventoryMain(MiningMain main) {
        super("minebr.mining", "&8Mineração", 9 * 5);
        this.main = main;
        configuration(configuration -> configuration.secondUpdate(1));
    }

    @Override
    protected void update(Viewer viewer, InventoryEditor editor) {
        Player viewerPlayer = viewer.getPlayer();
        setGlassPanels(editor);

        // Item de picareta
        ItemBuilder itemBuilder = new ItemBuilder();
        SkullBuilder skullBuilder = new SkullBuilder();

        itemBuilder.setType(Material.DIAMOND_PICKAXE);
        itemBuilder.setName("§bSua picareta.");
        itemBuilder.setLore(new String[]{
                "§7Não há como evoluir os encantamentos, porém,",
                "§7existem maneiras de melhorar trocando",
                "§7sua picareta por picareta melhores.",
                "",
                "§7Faça as suas trocas em §f/warp troca",
                "",
                "§a↓ Ou adquira picaretas exclusivas no site! ↓",
                "                 §fwww.minebr.net"
        });
        ItemStack picareta = itemBuilder.build();
        InventoryItem picaretaItem = InventoryItem.of(picareta);
        editor.setItem(20, picaretaItem.defaultCallback(event -> {
            viewerPlayer.closeInventory();
            viewerPlayer.sendMessage("");
            viewerPlayer.sendMessage("§aTem ótimos itens no site esperando por você, use o cupom §lMINEBR");
            viewerPlayer.sendMessage("                         §fwww.minebr.net");
            viewerPlayer.sendMessage("");
        }));

        // Item de boosters
        itemBuilder.setType(Material.WATCH);
        itemBuilder.setName("§aBooster(s)");

        // Obtendo os boosters ativos do jogador
        List<Booster> activeBoosters = getActiveBoosters(viewerPlayer);

        // Montando a lore
        String[] lore = {
                "§7Com os boosters, você ganha uma porcentagem",
                "§7a mais na venda de todos os drops da mineração.",
                "",
                " §aSeu(s) booster(s):"
        };

        // Verificando se há boosters ativos
        String[] boostersArray;
        if (activeBoosters.isEmpty()) {
            boostersArray = new String[]{"   §cVocê não possui boosters ativos no momento."};
        } else {
            boostersArray = activeBoosters.stream()
                    .map(booster -> "   §a↳ §f" + booster.getType() + "§7: §a" + booster.getPercentage() + "%, §7Tempo restante: §e" + booster.getRemainingTime() + "s")
                    .toArray(String[]::new);
        }

        // Concatenando as duas arrays de strings
        String[] finalLore = new String[lore.length + boostersArray.length + 3];
        System.arraycopy(lore, 0, finalLore, 0, lore.length);
        System.arraycopy(boostersArray, 0, finalLore, lore.length, boostersArray.length);
        finalLore[finalLore.length - 3] = "";
        finalLore[finalLore.length - 2] = "§a↓ Adquira mais boosters em: ↓";
        finalLore[finalLore.length - 1] = "         §fwww.minebr.net";

        itemBuilder.setLore(finalLore);
        ItemStack booster = itemBuilder.build();
        InventoryItem boosterItem = InventoryItem.of(booster);
        editor.setItem(22, boosterItem.defaultCallback(event -> {
            viewerPlayer.closeInventory();
            viewerPlayer.sendMessage("");
            viewerPlayer.sendMessage("§aTem ótimos itens no site esperando por você, use o cupom §lMINEBR");
            viewerPlayer.sendMessage("                         §fwww.minebr.net");
            viewerPlayer.sendMessage("");
        }));


        itemBuilder.setType(Material.BOOK);
        itemBuilder.setName("§cÁreas de Mineração.");
        itemBuilder.setLore(new String[]{
                "§7Tenha chances de conseguir chave(s)",
                "§7ou ativar encantamentos especiais",
                "§7para ter maores lucros!",
                "",
                "§cClique aqui para visualizar."
        });
        ItemStack mines = itemBuilder.build();
        InventoryItem minesItem = InventoryItem.of(mines);
        editor.setItem(24, minesItem.defaultCallback(event -> {
            viewerPlayer.closeInventory();
            InventoryMinesList invMines = new InventoryMinesList(MiningMain.getPlugin());
            invMines.openInventory(viewerPlayer);
        }));

    }

    private List<Booster> getActiveBoosters(Player player) {
        PlayerObject playerObject = main.getMainDataManager().USERS.getCached(player.getName());
        return playerObject.getActiveAllBoosters();
    }

    private void setGlassPanels(InventoryEditor editor) {
        int[] borderSlots = {
                0, 1, 2, 3, 4, 5, 6, 7, 8,
                9, 18, 27, 36,
                17, 26, 35, 44,
                37, 38, 39, 40, 41, 42, 43
        };

        ItemStack glassPanel = createGlassPanel("§ewww.minebr.net");

        for (int slot : borderSlots) {
            if (slot < getSize() && (editor.getInventory().getItem(slot) == null || editor.getInventory().getItem(slot).getType() == Material.AIR)) {
                editor.setItem(slot, InventoryItem.of(glassPanel));
            }
        }
    }

    private ItemStack createGlassPanel(String name) {
        return new ItemBuilder()
                .setType(Material.STAINED_GLASS_PANE)
                .setData((byte) 7)
                .setName(name)
                .build();
    }
}
