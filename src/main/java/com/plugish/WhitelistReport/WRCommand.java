package com.plugish.WhitelistReport;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class WRCommand implements CommandExecutor {

	private JavaPlugin plugin;

	public WRCommand( JavaPlugin plugin ) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand( CommandSender commandSender, Command command, String s, String[] strings ) {
		if ( !command.getName().equalsIgnoreCase( "wr" ) ) {
			return false;
		}

		if ( ! commandSender.hasPermission( "wr.admin" ) && ! commandSender.isOp() ) {
			return false;
		}

		try {
			checkPlayers( commandSender );
		} catch ( Exception e ) {
			plugin.getLogger().severe( e.getMessage() );
		}

		return false;
	}

	public boolean checkPlayers( CommandSender commandSender ) throws Exception {
		plugin.getLogger().info( "Starting Server Checks" );

//		List< Map< String, String > > onlineData = new ArrayList<>();

		Set< OfflinePlayer > players = plugin.getServer().getWhitelistedPlayers();
		Integer key = 0;
		for ( OfflinePlayer serverPlayer : players ) {
			if ( !serverPlayer.hasPlayedBefore() ) {
				plugin.getLogger().info( "Not Played - " + serverPlayer.getUniqueId() );
				continue;
			}

			String playerName = serverPlayer.getName();
			Long lastSeen = serverPlayer.getLastPlayed();

			Date date = new Date( lastSeen );
			DateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd" );
			String dateFormatted = formatter.format( date );

			plugin.getLogger().info( playerName + " - " + dateFormatted );
			commandSender.sendMessage( playerName + " - " + dateFormatted );

			key = key + 1;
		}
		return true;
	}

}
