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
	 * @param category category
	 * @param key Key
	 * @param defVal Default Value
	 * @param comment Comment to be added to property
	 * @return Value in the configuration or the default value specified if not found.
	 */
	public int get(String category, String key, int defVal, String comment) {
		load();
		int i = config.get(category, key, defVal, comment).getInt();
		this.config.save();
		return i;
	}
	
	/**
	 * General Configuration Value
	 * @param key Key
	 * @param defVal Default Value
	 * @param comment Comment to be added to property
	 * @return Value in the configuration or the default value specified if not found.
	 */
	public int get(String key, int defVal, String comment) {
		return get(Configuration.CATEGORY_GENERAL, key, defVal, comment);
	}
	
	/**
	 * General Configuration Value
	 * @param category category
	 * @param key Key
	 * @param defVal Default Value
	 * @param comment Comment to be added to property
	 * @return Value in the configuration or the default value specified if not found.
	 */
	public int[] get(String category, String key, int[] defVal, String comment) {
		load();
		int[] i = config.get(category, key, defVal, comment).getIntList();
		this.config.save();
		return i;
	}
	
	/**
	 * General Configuration Value
	 * @param key Key
	 * @param defVal Default Value
	 * @param comment Comment to be added to property
	 * @return Value in the configuration or the default value specified if not found.
	 */
	public int[] get(String key, int[] defVal, String comment) {
		return get(Configuration.CATEGORY_GENERAL, key, defVal, comment);
	}
	
	/**
	 * General Configuration Value
	 * @param category category
	 * @param key Key
	 * @param defVal Default Value
	 * @param comment Comment to be added to property
	 * @return Value in the configuration or the default value specified if not found.
	 */
	public boolean get(String category, String key, boolean defVal, String comment) {
		load();
		boolean b = config.get(category, key, defVal, comment).getBoolean(defVal);
		this.config.save();
		return b;
	}
	
	/**
	 * General Configuration Value
	 * @param key Key
	 * @param defVal Default Value
	 * @param comment Comment to be added to property
	 * @return Value in the configuration or the default value specified if not found.
	 */
	public boolean get(String key, boolean defVal, String comment) {
		return get(Configuration.CATEGORY_GENERAL, key, defVal, comment);
	}
	
	/**
	 * General Configuration Value
	 * @param category category
	 * @param key Key
	 * @param defVal Default Value
	 * @param comment Comment to be added to property
	 * @return Value in the configuration or the default value specified if not found.
	 */
	public double get(String category, String key, double defVal, String comment) {
		load();
		double d = config.get(category, key, defVal, comment).getDouble(defVal);
		this.config.save();
		return d;
	}
	
	/**
	 * General Configuration Value
	 * @param key Key
	 * @param defVal Default Value
	 * @param comment Comment to be added to property
	 * @return Value in the configuration or the default value specified if not found.
	 */
	public double get(String key, double defVal, String comment) {
		return get(Configuration.CATEGORY_GENERAL, key, defVal, comment);
	}

}
