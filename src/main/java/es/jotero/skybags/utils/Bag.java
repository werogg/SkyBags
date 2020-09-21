package es.jotero.skybags.utils;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import es.jotero.skybags.Skybags;
import org.apache.commons.io.FileUtils;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryDimension;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.api.item.inventory.query.QueryOperation;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.text.Text;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bag {

    public List<SlotPos> itemsPositions;
    private Path bagDataFilePath;
    private Inventory inventory;
    private final Skybags skybags;

    public Bag(Player argsPlayer, Player sourcePlayer, int size, Boolean save, Skybags skybags) {
        this.bagDataFilePath = Paths.get(skybags.getBagsDirPath() + File.separator + argsPlayer.getUniqueId().toString() + ".bag");
        this.skybags = skybags;
        this.itemsPositions = new ArrayList<>(54);

        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j <= 8; j++) {
                itemsPositions.add(new SlotPos(j, i));
            }
        }

        Text inventoryTitle;
        if (!sourcePlayer.getUniqueId().equals(argsPlayer.getUniqueId())) {
            inventoryTitle = Text.of("Mochila de " + argsPlayer.getName());
        } else {
            inventoryTitle = Text.of("Mochila");
        }

        this.inventory = Inventory.builder()
                .of(InventoryArchetypes.DOUBLE_CHEST)
                .property(InventoryTitle.PROPERTY_NAME, InventoryTitle.of(Text.of(inventoryTitle)))
                .property("inventorydimension", InventoryDimension.of(9, size))
                .listener(ClickInventoryEvent.class, (ClickInventoryEvent event) -> {
                    if (save) {
                        try {
                            Map<String, String> items = loadStacks(argsPlayer);
                            saveBagData(items, skybags);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        event.setCancelled(true);
                    }
                })
                .build(skybags);

        loadBag(argsPlayer, skybags);
    }

    private void saveBagData(Map<String, String> items, Skybags skybags) {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        if (items == null || items.isEmpty()) {
            DataHandler.writeFile(bagDataFilePath.toFile(), "{}", skybags);
        } else {
            DataHandler.writeFile(bagDataFilePath.toFile(), gson.toJson(items), skybags);
        }
    }

    private Map<String, String> loadStacks(Player player) throws Exception {
        Map<String, String> items = loadSlots(skybags);
        for (Inventory slot : this.inventory.slots()) {
            if (slot.getProperty(SlotIndex.class, "slotindex").isPresent()) {
                Integer index = slot.getProperty(SlotIndex.class, "slotindex").get().getValue();

                if (index == null) throw new Exception("An intern error has ocurred while loading an slot index.");
                SlotPos slotPos = DataHandler.IndexToSlotPos(index);

                if (slotPos == null) throw new Exception("The slot position failed to be loaded.");
                items.put(slotPos.getX() + "," + slotPos.getY(), "EMPTY");
                if (slot.size() > 0 && slot.peek().isPresent() && slot.getProperty(SlotIndex.class, "slotindex").isPresent() && !slot.peek().get().getType().equals(ItemTypes.NONE))
                {
                    try {
                        items.put(slotPos.getX() + "," + slotPos.getY(), DataHandler.ItemStackToBase64(slot.peek().get()));
                    } catch (Exception e) {
                        Skybags.getInstance().getLogger().error("Failed to load a stack data from inventory for this user: " + player.getName() + " SlotPos: " + slotPos.getX() + "X," + slotPos.getY() + "Y");
                        e.printStackTrace();
                        throw new Exception("Failed to load a stack data from inventory for this user: " + player.getName() + " SlotPos: " + slotPos.getX() + "X," + slotPos.getY() + "Y");
                    }
                }
            }
        }
        return items;
    }

    private Map<String, String> loadSlots(Skybags skybags) throws Exception {
        File file = bagDataFilePath.toFile();

        if (!file.exists())
            DataHandler.writeFile(file, "{}", skybags);

        Gson gson = new Gson();
        Type type = (new TypeToken<Map<String, String>>() {

        }).getType();
        Map<String, String> models = null;
        try {
            models = gson.fromJson(FileUtils.readFileToString(file, Charsets.UTF_8), type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (models != null)
            return models;
        throw new Exception("There was a error loading backpack file. (" + file.getPath() + ")");
    }

    private void loadBag(Player player, Skybags skybags) {
        Map<String, String> items = new HashMap<>();
        try {
            items = loadSlots(skybags);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Map.Entry<String, String> entry : items.entrySet()) {
            if (entry != null &&
                    entry.getValue() != null) {
                SlotPos sp = SlotPos.of(Integer.parseInt(entry.getKey().split(",")[0]), Integer.parseInt(entry.getKey().split(",")[1]));
                try {
                    if (!entry.getValue().equals("EMPTY")) {
                        ItemStack itemStack = DataHandler.Base64ToItemStack(entry.getValue());
                        this.inventory.query(new QueryOperation[] { QueryOperationTypes.INVENTORY_PROPERTY.of(sp) }).set(itemStack);
                        continue;
                    }
                    this.inventory.query(new QueryOperation[] { QueryOperationTypes.INVENTORY_PROPERTY.of(sp) }).set(ItemStack.empty());
                } catch (Exception ex) {
                    Skybags.getInstance().getLogger().error("Failed to load a stack data from file for this user: " + player.getName() + " SlotPos: " + sp.getX() + "X," + sp.getY() + "Y");
                    ex.printStackTrace();
                }
            }
        }
    }

    public Inventory getBag() {
        return inventory;
    }

}
