package com.plugish.WhitelistReport;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class WRCommand implements CommandExecutor {

	private JavaPlugin plugin;

	private JSONArray jsonCache = null;

	public WRCommand( JavaPlugin plugin ) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand( CommandSender commandSender, Command command, String s, String[] strings ) {
		if ( !command.getName().equalsIgnoreCase( "wr" ) ) {
			return false;
		}

		if ( !commandSender.hasPermission( "wr.admin" ) && !commandSender.isOp() ) {
			return false;
		}

		try {
			checkPlayers( commandSender );
		} catch ( Exception e ) {
			plugin.getLogger().severe( e.getMessage() );
			e.printStackTrace();
		}

		return false;
	}

	static String readFile( String path, Charset encoding ) throws IOException {
		byte[] encoded = Files.readAllBytes( Paths.get( path ) );
		return new String( encoded, encoding );
	}

	private String getUserByUUID( String uniqueID ) throws Exception {

		// Cache the JSON file, no need to read it every command.
		if ( null == jsonCache ) {
			String whitelistPath = new File( "." ).getAbsolutePath() + File.separator + "whitelist.json";
			String whitelistData = readFile( whitelistPath, Charset.defaultCharset() );
			jsonCache = new JSONArray( whitelistData );
		}

		for ( int i = 0; i < jsonCache.length(); i++ ) {
			JSONObject playerObject = jsonCache.getJSONObject( i );
			String uuid = playerObject.getString( "uuid" );
			if ( uuid.equals( uniqueID ) ) {
				return playerObject.getString( "name" );
			}

		}

		return "";
	}

	public boolean checkPlayers( CommandSender commandSender ) throws Exception {
		plugin.getLogger().info( "Starting Server Checks" );

//		List< Map< String, String > > onlineData = new ArrayList<>();

		Set< OfflinePlayer > players = plugin.getServer().getWhitelistedPlayers();
		List<String> output = new ArrayList<>();
		for ( OfflinePlayer serverPlayer : players ) {
			if ( !serverPlayer.hasPlayedBefore() ) {
				output.add( "Not Played - " + getUserByUUID( serverPlayer.getUniqueId().toString() ) );
				continue;
			}

			String playerName = serverPlayer.getName();
			Long lastSeen = serverPlayer.getLastPlayed();

			Date date = new Date( lastSeen );
			DateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd" );
			String dateFormatted = formatter.format( date );

			output.add( dateFormatted + " - " + playerName );
		}

		Collections.sort( output );
		for ( String player : output ) {
			commandSender.sendMessage( player );
		}

		return true;
	}

}
