package ice.and.snow.bukkit.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class MainPlugin extends JavaPlugin {

	private Server server;

	@Override
	public void onEnable() {
		super.onEnable();

		// Initialisering
		server = getServer();

		// Besked til terminalen
		sendMessageToConsole("is enabled!");

	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (sender instanceof Player) { // Tjekker om det er en spiller, som har
										// skrevet kommandoen
			Player player = (Player) sender;
			if (command.getName().equalsIgnoreCase("chest")) { // Hvis der er
																// skrevet
																// /chest
				if (args.length == 1)
					server.dispatchCommand(player, "gp c " + args[0]); // Udfører
																		// den
																		// reelle
																		// kommando:
																		// /gb c
																		// [kiste-navn]
				else if (args.length == 2)
					server.dispatchCommand(player, "gp b " + args[0] + " "
							+ args[1]); // 'Køber' en ny kiste med
										// størrelsen(large eller normal) som
										// argument 0 og navnet som argument 1
				else
					server.dispatchCommand(player, "gp c"); // Åbner første
															// købte kiste
				return true; // Kommandoen blev genkendt og er valid
			} else if (command.getName().equalsIgnoreCase("gethead")) {
				String owner = player.getName(); // Navnet på den spillers hoved
													// der ønskes
				if (args.length == 1)
					owner = args[0];
				ItemStack head = new ItemStack(Material.SKULL_ITEM, 1,
						(short) 3); // Det sidste argument fortæller, at det er
									// en spillers hoved
				SkullMeta meta = (SkullMeta) head.getItemMeta();
				meta.setOwner(owner);
				head.setItemMeta(meta);
				player.getInventory().addItem(head);
				player.updateInventory();

				return true;
			}
		}
		return false; // Skriv 'usage' fra plugin.yml til spilleren
	}

	public void sendMessageToConsole(String message) {
		server.getConsoleSender().sendMessage(
				ChatColor.GREEN + "[" + ChatColor.RED + "SimplifiedCommands"
						+ ChatColor.GREEN + "] " + ChatColor.WHITE + " "
						+ message);
	}

	@Override
	public void onDisable() {
		super.onDisable();

		// Besked til terminalen
		sendMessageToConsole("is disabled!");
	}

}
