package ice.and.snow.bukkit.spleef;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GeneralListener implements Listener {

	private MainPlugin plugin;

	public GeneralListener(MainPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onUse(PlayerInteractEvent ev) {
		if (!ev.getPlayer().getLocation().getWorld().getName()
				.equalsIgnoreCase(plugin.getConfig().getString("spleef_world")))
			return;
		if (ev.getAction() == Action.RIGHT_CLICK_BLOCK == false)
			return;
		Block clickedBlock = ev.getClickedBlock();
		if (clickedBlock.getType() != Material.WALL_SIGN
				&& clickedBlock.getType() != Material.SIGN_POST)
			return;
		Sign sign = (Sign) clickedBlock.getState();
		boolean approved = false;
		for (String line : sign.getLines()) {
			if (line.contains(plugin.getConfig().getString("sign_line"))) {
				approved = true;
				break;
			}
		}
		if (!approved)
			return;
		Location locationOfBlock = clickedBlock.getLocation().add(0.5f, 0.5f,
				0.5f);
		BlockFace face = ev.getBlockFace();
		Location teleportLocation = locationOfBlock.add(-face.getModX() * 2,
				-face.getModY(), -face.getModZ() * 2);
		Player player = ev.getPlayer();
		player.teleport(teleportLocation);
		giveShovel(player);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onHitBlock(BlockDamageEvent ev) {
		if (!ev.getPlayer().getLocation().getWorld().getName()
				.equalsIgnoreCase("spleef"))
			return;
		ItemStack item = ev.getItemInHand();
		if (item == null)
			return;
		Block block = ev.getBlock();
		if (block.getLocation().getY() != plugin.getConfig().getInt(
				"spleef_height"))
			return;
		if (checkShovel(item))
			block.setType(Material.AIR);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMove(PlayerMoveEvent ev) {
		Player player = ev.getPlayer();
		if(plugin.getSpleefGame().isStarted() && plugin.getSpleefGame().isPlayerInGame(player) && plugin.getSpleefGame().isPlayerOutOfArena(player)){
			plugin.getSpleefGame().removePlayer(player);
			plugin.getSpleefGame().checkEnd();
		}
	}

	private void giveShovel(Player player) {
		ItemStack shovel = new ItemStack(Material.DIAMOND_SPADE);
		ItemMeta shovelMeta = shovel.getItemMeta();
		shovelMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
				plugin.getConfig().getString("shovel_name")));
		shovel.setItemMeta(shovelMeta);
		player.getInventory().addItem(shovel);
		player.updateInventory();
		plugin.sendMessageToPlayer(player, plugin.getConfig().getString("entry_message"));
	}

	private boolean checkShovel(ItemStack item) {
		if (item.getType() != Material.DIAMOND_SPADE)
			return false;
		ItemMeta shovelMeta = item.getItemMeta();
		if (!shovelMeta.getDisplayName().equals(
				ChatColor.translateAlternateColorCodes('&', plugin.getConfig()
						.getString("shovel_name"))))
			return false;
		return true;
	}
}
