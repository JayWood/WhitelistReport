package com.plugish.WhitelistReport;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

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

		plugin.getLogger().info( "Starting Server Checks" );

		Set< OfflinePlayer > players = plugin.getServer().getWhitelistedPlayers();
		for ( OfflinePlayer serverPlayer : players ) {
			if ( !serverPlayer.hasPlayedBefore() ) {
				plugin.getLogger().info( "Not Played - " + serverPlayer.getUniqueId() );
				continue;
			}

			String playerName = serverPlayer.getName();
			Long lastSeen = serverPlayer.getLastPlayed();

			Date date = new Date( lastSeen );
			DateFormat formatter = new SimpleDateFormat( "EEE, MMM d, ''yy" );
			String dateFormatted = formatter.format( date );

			plugin.getLogger().info( playerName + " - " + dateFormatted );
			commandSender.sendMessage( playerName + " - " + dateFormatted );
		}

		return false;
	}

}
