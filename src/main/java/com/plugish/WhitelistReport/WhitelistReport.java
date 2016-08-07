package com.plugish.WhitelistReport;

import org.bukkit.plugin.java.JavaPlugin;

public class WhitelistReport extends JavaPlugin {

	@Override
	public void onEnable() {
		getLogger().info( "This is now enabled" );

		getCommand( "whitelistreport" ).setExecutor( new WRCommand( this ) );
	}

}
