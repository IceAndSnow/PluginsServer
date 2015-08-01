package ice.and.snow.bukkit.spleef;


import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MainPlugin extends JavaPlugin {

	private Server server;
	private SpleefGame game;
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		//Initialisering
		server = getServer();
		game = new SpleefGame(this);
		
		//Konfigurationsfil
		
		saveDefaultConfig(); //Kopierer config.yml fra .jar-filen ind i ./Spleef/ hvis der endnu ikke findes en sådanne fil.
		getConfig().options().copyHeader(true); //Sørger for, at kommentarerne i toppen af config.yml også kopieres med.
		
		//GeneralListener
		server.getPluginManager().registerEvents(new GeneralListener(this), this);

		//Besked til terminalen
		sendMessageToConsole("is enabled!");
	}
	
	public void sendMessageToAllPlayers(String message){
		for(Player player : server.getOnlinePlayers()){
			sendMessageToPlayer(player, message);
		}
	}
	
	public void sendMessageToPlayer(Player player, String message){
		player.sendMessage(ChatColor.GREEN + "[" + ChatColor.RED + "Spleef"
				+ ChatColor.GREEN + "] " + ChatColor.WHITE + " " + message);
	}
	
	public void sendMessageToConsole(String message){
		server.getConsoleSender().sendMessage(ChatColor.GREEN + "[" + ChatColor.RED + "Spleef"
				+ ChatColor.GREEN + "] " + ChatColor.WHITE + " " + message);
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		
		//Besked til terminalen
		sendMessageToConsole("is disabled!");
	}
	
	public SpleefGame getSpleefGame() {
		return game;
	}

}
