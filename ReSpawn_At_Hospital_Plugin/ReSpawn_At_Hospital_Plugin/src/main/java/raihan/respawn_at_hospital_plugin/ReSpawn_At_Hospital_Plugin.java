package raihan.respawn_at_hospital_plugin;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ReSpawn_At_Hospital_Plugin extends JavaPlugin implements CommandExecutor, Listener {

    private Location hospitalLocation;

    @Override
    public void onEnable() {
        // Registering the command "/sethospital"
        getCommand("sethospital").setExecutor(this);
        // Registering the event listener
        getServer().getPluginManager().registerEvents(this, this);
        // Load hospital location from config
        loadHospitalLocation();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can set the hospital location!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.isOp() && !player.hasPermission("respawn_at_hospital_plugin.admin")) {
            player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD+"You don't have permission to set the hospital location!");
            return true;
        }


        if (args.length != 0) {
            player.sendMessage("Usage: /sethospital");
            return true;
        }

        hospitalLocation = player.getLocation();
        // Save hospital location to config
        saveHospitalLocation();
        player.sendMessage(ChatColor.GREEN +"Hospital location set successfully!");

        return true;
    }

    // Load hospital location from config
    private void loadHospitalLocation() {
        FileConfiguration config = getConfig();
        if (config.contains("hospital.location")) {
            hospitalLocation = (Location) config.get("hospital.location");
        }
    }

    // Save hospital location to config
    private void saveHospitalLocation() {
        FileConfiguration config = getConfig();
        config.set("hospital.location", hospitalLocation);
        saveConfig();
    }

    // This method will be called when a player respawns
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        // Check if hospital location is set
        if (hospitalLocation != null) {
            // Set respawn location to hospital location
            event.setRespawnLocation(hospitalLocation);
            player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD+ "You respawned at the hospital.");

        } else {
            player.sendMessage(ChatColor.RED +""+ ChatColor.BOLD+ "Hospital location is not set. Please ask an admin to set it up.");
        }
    }
}
