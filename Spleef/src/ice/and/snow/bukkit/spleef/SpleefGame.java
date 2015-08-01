package ice.and.snow.bukkit.spleef;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpleefGame {

	private MainPlugin plugin;
	private ArrayList<UUID> playersInArena;
	private boolean isStarted;

	public SpleefGame(MainPlugin plugin) {
		this.plugin = plugin;
		playersInArena = new ArrayList<UUID>();
	}

	public boolean addPlayer(Player player) {
		UUID uniqueID = player.getUniqueId();
		if (!playersInArena.contains(uniqueID)
				&& playersInArena.size() <= plugin.getConfig().getInt(
						"max_players") && !isStarted) {
			playersInArena.add(uniqueID);
			return true;
		}
		return false;
	}

	public boolean removePlayer(Player player) {
		UUID uniqueID = player.getUniqueId();
		if (player.isOnline() && playersInArena.contains(uniqueID)) {
			playersInArena.remove(uniqueID);
			ItemStack shovel = new ItemStack(Material.DIAMOND_SPADE, 1);
			ItemMeta shovelMeta = shovel.getItemMeta();
			shovelMeta.setDisplayName(ChatColor.translateAlternateColorCodes(
					'&', plugin.getConfig().getString("shovel_name")));
			shovel.setItemMeta(shovelMeta);
			player.getInventory().removeItem(shovel);

			return true;
		}
		playersInArena.remove(uniqueID);
		return false;
	}

	public boolean isPlayerInGame(Player player) {
		return playersInArena.contains(player.getUniqueId());
	}

	public boolean isStarted() {
		return isStarted;
	}

	/**/
	public void start() {
		if (playersInArena.size() >= 2 && !isStarted)
			isStarted = true;
	}

	public void stop() {
		if (isStarted)
			for (int index = 0; index < playersInArena.size(); index++)
				removePlayer(plugin.getServer().getPlayer(
						playersInArena.get(index)));
	}

	public boolean isPlayerOutOfArena(Player player) {
		return player.getLocation().getY() < plugin.getConfig().getInt(
				"spleef_height");
	}

	public void checkEnd() {
		if (playersInArena.size() == 1) {
			plugin.sendMessageToPlayer(
					plugin.getServer().getPlayer(playersInArena.get(0)), plugin
							.getConfig().getString("winning_message"));
			stop();
		}
	}

}
