package net.meteor.common;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ModConfig {
	
	private Configuration config;
	private boolean isLoaded = false;
	
	public static final ModConfig instance = new ModConfig();
	
	private ModConfig() {}
	
	public void load(File suggestedFile) {
		this.config = new Configuration(suggestedFile);
		load();
	}
	
	private void load() {
		if (!this.isLoaded || config.hasChanged()) {
			this.config.load();
			this.isLoaded = true;
		}
	}
	
	/**
	 * General Configuration Value
	 * @param key Key
	 * @param defVal Default Value
	 * @return Value in the configuration or the default value specified if not found.
	 */
	public int get(String key, int defVal) {
		load();
		int i = config.get(Configuration.CATEGORY_GENERAL, key, defVal).getInt();
		this.config.save();
		return i;
	}
	
	/**
	 * General Configuration Value
	 * @param key Key
	 * @param defVal Default Value
	 * @return Value in the configuration or the default value specified if not found.
	 */
	public boolean get(String key, boolean defVal) {
		load();
		boolean b = config.get(Configuration.CATEGORY_GENERAL, key, defVal).getBoolean(defVal);
		this.config.save();
		return b;
	}
	
	/**
	 * General Configuration Value
	 * @param key Key
	 * @param defVal Default Value
	 * @return Value in the configuration or the default value specified if not found.
	 */
	public double get(String key, double defVal) {
		load();
		double d = config.get(Configuration.CATEGORY_GENERAL, key, defVal).getDouble(defVal);
		this.config.save();
		return d;
	}

}
