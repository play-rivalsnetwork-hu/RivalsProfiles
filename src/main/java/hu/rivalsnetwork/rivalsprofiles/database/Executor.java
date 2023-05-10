package hu.rivalsnetwork.rivalsprofiles.database;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import hu.rivalsnetwork.rivalsapi.storage.Storage;
import hu.rivalsnetwork.rivalsprofiles.inventory.InventorySerializer;
import org.bson.Document;
import org.bukkit.entity.Player;

public class Executor {

    public static void serializeInventory(Player player) {
        Storage.mongo(database -> {
            MongoCollection<Document> collection = database.getCollection("inventory-data");
            Document document = new Document();
            document.put("uuid", player.getUniqueId());
            document.put("profile", getCurrentIsland(player));

            Document searchQuery = new Document();
            searchQuery.put("uuid", player.getUniqueId());
            searchQuery.put("profile", getCurrentIsland(player));
            FindIterable<Document> cursor = collection.find(searchQuery);
            try (final MongoCursor<Document> cursorIterator = cursor.cursor()) {
                if (cursorIterator.hasNext()) {
                    Document newDocument = new Document();
                    InventorySerializer.serialize(player.getInventory(), value -> {
                        newDocument.put("contents", value);
                        newDocument.put("xp", player.getExp());
                        newDocument.put("level", player.getLevel());
                        Document updateObject = new Document();
                        updateObject.put("$set", newDocument);

                        collection.updateMany(searchQuery, updateObject);
                    });

                } else {
                    InventorySerializer.serialize(player.getInventory(), value -> {
                        document.put("contents", value);
                        document.put("xp", player.getExp());
                        document.put("level", player.getLevel());
                        collection.insertOne(document);
                    });
                }
            }
        });
    }

    public static String getCurrentIsland(Player player) {
        final String[] profile = {"Banana"};
        Storage.mongo(database -> {
            MongoCollection<Document> collection = database.getCollection("island-data");
            Document searchQuery = new Document();
            searchQuery.put("uuid", player.getUniqueId());
            FindIterable<Document> cursor = collection.find(searchQuery);

            try (final MongoCursor<Document> cursorIterator = cursor.cursor()) {
                if (cursorIterator.hasNext()) {
                    Document doc = cursorIterator.next();
                    profile[0] = doc.getString("current-island");
                }
            }
        });

        return profile[0];
    }

    public static void deserializeInventory(Player player) {
        Storage.mongo(database -> {
            MongoCollection<Document> collection = database.getCollection("inventory-data");
            Document searchQuery = new Document();
            searchQuery.put("uuid", player.getUniqueId());
            FindIterable<Document> cursor = collection.find(searchQuery);

            try (final MongoCursor<Document> cursorIterator = cursor.cursor()) {
                if (cursorIterator.hasNext()) {
                    Document next = cursorIterator.next();
                    InventorySerializer.deserialize(next.getString("contents"), player);
                    player.setExp(((Double) next.get("xp")).floatValue());
                    player.setLevel(next.getInteger("level"));
                }
            }
        });
    }
}
