package com.arkcraft.mod.common.items.weapons.handlers;

import net.minecraftforge.common.config.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

public class WeaponModConfig
{
	private final Configuration				config;

	private Map<String, EnableSetting>		enableSettings;
	private Map<String, ReloadTimeSetting>	reloadTimeSettings;
	//public static Map<String, Item> enableSettings = new HashMap<String, Item>();
	
	public WeaponModConfig(Configuration configuration)
	{
		config = configuration;
		enableSettings = new LinkedHashMap<String, EnableSetting>();
		reloadTimeSettings = new LinkedHashMap<String, ReloadTimeSetting>();

	}
	
	public void addEnableSetting(String weapon)
	{
		enableSettings.put(weapon, new EnableSetting(weapon));
	}
	
	public void addReloadTimeSetting(String weapon, int defaulttime)
	{
		reloadTimeSettings.put(weapon, new ReloadTimeSetting(weapon, defaulttime));
	}
	
	public boolean isEnabled(String weapon)
	{
		EnableSetting es = enableSettings.get(weapon);
		return es == null || es.enabled;
	}
	
	public int getReloadTime(String weapon)
	{
		ReloadTimeSetting rs = reloadTimeSettings.get(weapon);
		return rs == null ? 0 : rs.reloadTime;
	}
	
	public void loadConfig()
	{
		config.load();
		
		config.addCustomCategoryComment("enable", "Enable or disable certain weapons");
		config.addCustomCategoryComment("reloadtime", "The reload durations of the reloadable weapons");
		config.addCustomCategoryComment("settings", "Miscellaneous mod settings");
			
		for (EnableSetting es : enableSettings.values())
		{
			es.enabled = config.get("enable", es.settingName, es.enabled).getBoolean(es.enabled);
		}
		for (ReloadTimeSetting rs : reloadTimeSettings.values())
		{
			rs.reloadTime = config.get("reloadtime", rs.settingName, rs.reloadTime).getInt(rs.reloadTime);
		}
		
		config.save();
	}
	
	private static abstract class Setting
	{
		final String	settingName;
		
		Setting(String name)
		{
			settingName = name;
		}
	}
	
	private static class ReloadTimeSetting extends Setting
	{
		int	reloadTime;
		
		ReloadTimeSetting(String name, int time)
		{
			super(name + ".reloadtime");
			reloadTime = time;
		}
	}
	
	private static class EnableSetting extends Setting
	{
		boolean	enabled;
		
		EnableSetting(String name)
		{
			super(name + ".enabled");
			enabled = true;
		}
		
	}
}