package es.jotero.skybags.utils;

import es.jotero.skybags.Skybags;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.persistence.DataFormats;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;

import java.io.*;
import java.util.Base64;

public class DataHandler {

    public static void writeFile(File file, String content, Skybags skybags) {
        FileWriter fileWriter = null;

        if (file.getParentFile().mkdirs()) {
            skybags.getLogger().error("Creating missing directories...");
        }

        if (file.exists()) {
            try {
                fileWriter = new FileWriter(file.toString(), false);
                fileWriter.write(content);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileWriter != null) {
                    try {
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            try {
                if (file.createNewFile()) {
                    writeFile(file, content, skybags);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String ItemStackToBase64(ItemStack itemStack) {
        return ContainerToBase64(itemStack.toContainer());
    }

    public static String ContainerToBase64(DataContainer container) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            DataFormats.NBT.writeTo(out, container);
            return Base64.getEncoder().encodeToString(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DataContainer Base64ToContainer(String base64) {
        return Base64ToContainer(Base64.getDecoder().decode(base64));
    }

    public static ItemStack Base64ToItemStack(String base64) {
        return ItemStack.builder().fromContainer(Base64ToContainer(base64)).build();
    }

    public static DataContainer Base64ToContainer(byte[] base64) {
        try (ByteArrayInputStream in = new ByteArrayInputStream(base64)) {
            return DataFormats.NBT.readFrom(in);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static SlotPos IndexToSlotPos(Integer index) {
        switch (index) {
            case 0:
                return SlotPos.of(0, 0);
            case 1:
                return SlotPos.of(1, 0);
            case 2:
                return SlotPos.of(2, 0);
            case 3:
                return SlotPos.of(3, 0);
            case 4:
                return SlotPos.of(4, 0);
            case 5:
                return SlotPos.of(5, 0);
            case 6:
                return SlotPos.of(6, 0);
            case 7:
                return SlotPos.of(7, 0);
            case 8:
                return SlotPos.of(8, 0);
            case 9:
                return SlotPos.of(0, 1);
            case 10:
                return SlotPos.of(1, 1);
            case 11:
                return SlotPos.of(2, 1);
            case 12:
                return SlotPos.of(3, 1);
            case 13:
                return SlotPos.of(4, 1);
            case 14:
                return SlotPos.of(5, 1);
            case 15:
                return SlotPos.of(6, 1);
            case 16:
                return SlotPos.of(7, 1);
            case 17:
                return SlotPos.of(8, 1);
            case 18:
                return SlotPos.of(0, 2);
            case 19:
                return SlotPos.of(1, 2);
            case 20:
                return SlotPos.of(2, 2);
            case 21:
                return SlotPos.of(3, 2);
            case 22:
                return SlotPos.of(4, 2);
            case 23:
                return SlotPos.of(5, 2);
            case 24:
                return SlotPos.of(6, 2);
            case 25:
                return SlotPos.of(7, 2);
            case 26:
                return SlotPos.of(8, 2);
            case 27:
                return SlotPos.of(0, 3);
            case 28:
                return SlotPos.of(1, 3);
            case 29:
                return SlotPos.of(2, 3);
            case 30:
                return SlotPos.of(3, 3);
            case 31:
                return SlotPos.of(4, 3);
            case 32:
                return SlotPos.of(5, 3);
            case 33:
                return SlotPos.of(6, 3);
            case 34:
                return SlotPos.of(7, 3);
            case 35:
                return SlotPos.of(8, 3);
            case 36:
                return SlotPos.of(0, 4);
            case 37:
                return SlotPos.of(1, 4);
            case 38:
                return SlotPos.of(2, 4);
            case 39:
                return SlotPos.of(3, 4);
            case 40:
                return SlotPos.of(4, 4);
            case 41:
                return SlotPos.of(5, 4);
            case 42:
                return SlotPos.of(6, 4);
            case 43:
                return SlotPos.of(7, 4);
            case 44:
                return SlotPos.of(8, 4);
            case 45:
                return SlotPos.of(0, 5);
            case 46:
                return SlotPos.of(1, 5);
            case 47:
                return SlotPos.of(2, 5);
            case 48:
                return SlotPos.of(3, 5);
            case 49:
                return SlotPos.of(4, 5);
            case 50:
                return SlotPos.of(5, 5);
            case 51:
                return SlotPos.of(6, 5);
            case 52:
                return SlotPos.of(7, 5);
            case 53:
                return SlotPos.of(8, 5);
        }
        return null;
    }
}
