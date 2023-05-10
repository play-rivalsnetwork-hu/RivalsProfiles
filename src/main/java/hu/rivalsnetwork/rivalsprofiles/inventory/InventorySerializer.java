package hu.rivalsnetwork.rivalsprofiles.inventory;

import hu.rivalsnetwork.rivalsprofiles.RivalsProfilesPlugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class InventorySerializer {

    public static void serialize(@NotNull PlayerInventory inventory, @NotNull SerializedData data) {
        RivalsProfilesPlugin.scheduler().runAsync(() -> {
            try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
                try (BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(stream)) {
                    bukkitObjectOutputStream.writeInt(inventory.getContents().length);

                    for (ItemStack item : inventory.getContents()) {
                        bukkitObjectOutputStream.writeObject(item);
                    }
                    data.accept(Base64Coder.encodeLines(stream.toByteArray()));
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public static void deserialize(@NotNull String serialized, @NotNull Player player) {
        final HashMap<Integer, ItemStack> itemMap = new HashMap<>();
        RivalsProfilesPlugin.scheduler().runAsync(() -> {
            try (ByteArrayInputStream stream = new ByteArrayInputStream(Base64Coder.decodeLines(serialized))) {
                try (BukkitObjectInputStream inputStream = new BukkitObjectInputStream(stream)) {
                    ItemStack[] items = new ItemStack[inputStream.readInt()];

                    for (int i = 0; i < items.length; i++) {
                        itemMap.put(i, (ItemStack) inputStream.readObject());
                    }

                    RivalsProfilesPlugin.scheduler().run(() -> itemMap.forEach((slot, item) -> player.getInventory().setItem(slot, item)));
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public interface SerializedData {
        void accept(String string);
    }
}
