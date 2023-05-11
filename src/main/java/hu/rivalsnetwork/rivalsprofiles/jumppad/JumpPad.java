package hu.rivalsnetwork.rivalsprofiles.jumppad;

import hu.rivalsnetwork.rivalsprofiles.RivalsProfilesPlugin;
import io.papermc.paper.entity.TeleportFlag;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class JumpPad {
    public static final List<Player> riding = new ArrayList<>(2048);
    private final int maxX;
    private final int maxY;
    private final int maxZ;
    private final int minX;
    private final int minY;
    private final int minZ;
    private final int[] x;
    private final int[] y;
    private final float speed;
    private final float speed2;
    private final boolean z;
    private final World world;
    
    public JumpPad(World world, int maxX, int maxZ, int maxY, int minX, int minZ, int minY, int[] x, int[] y, float speed, float speed2, boolean z) {
        this.world = world;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.speed2 = speed2;
        this.z = z;
        JumpPads.add(this);
    }
    
    public void check(@NotNull PlayerMoveEvent event) {
        if (!event.getPlayer().getWorld().getName().equals(world.getName())) return;

        if (event.getTo().getBlockX() <= maxX && event.getTo().getBlockX() >= minX && event.getTo().getBlockZ() <= maxZ && event.getTo().getBlockZ() >= minZ && event.getTo().getBlockY() <= maxY && event.getTo().getBlockY() >= minY) {
            event.getPlayer().playSound(event.getPlayer(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0f, 2.0f);
            final float[] position = {0};

            Location loc = event.getPlayer().getLocation().clone();
            ArmorStand ent = (ArmorStand) event.getPlayer().getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
            ent.setInvisible(true);
            ent.setInvulnerable(true);
            ent.setCanTick(false);
            ent.addPassenger(event.getPlayer());
            riding.add(event.getPlayer());
            Location location = loc.clone();
            event.getPlayer().playSound(event.getPlayer(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0f, 2.0f);

            RivalsProfilesPlugin.scheduler().runTimer(task -> {
                if (position[0] >= speed) {
                    task.cancel();
                    riding.remove(event.getPlayer());
                    ent.remove();
                }

                location.setY(cubicBezier(position[0], y));
                if (!z) {
                    location.setX(cubicBezier(position[0], x));
                } else {
                    location.setZ(cubicBezier(position[0], x));
                }

                ent.teleport(location, TeleportFlag.EntityState.RETAIN_PASSENGERS);
                position[0] += speed2;
            }, 0, 0);
        }
    }

    // https://www.geeksforgeeks.org/how-to-create-bezier-curve-animation-using-java-applet/
    public static double cubicBezier(double t, int[] math) {
        return Math.pow(1 - t, 3) * math[0] + 3 * t * Math.pow(1 - t, 2) * math[1] + 3 * t * t * (1 - t) * math[2]
                + Math.pow(t, 3) * math[3];
    }
}
