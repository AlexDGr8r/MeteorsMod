package net.meteor.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Properties;

import net.minecraft.client.Minecraft;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;

public class LangLocalization
{
	private static String loadedLanguage = getCurrentLanguage();
	private static Properties defaultMappings = new Properties();
	private static Properties mappings = new Properties();
	private static LinkedList<modInfo> mods = new LinkedList<modInfo>();

	public static void addLocalization(String path, String defaultLanguage)
	{
		mods.add(new modInfo(path, defaultLanguage));
		load(path, defaultLanguage);
	}

	public static synchronized String get(String key)
	{
		if (getCurrentLanguage() == null) {
			return key;
		}
		if (!getCurrentLanguage().equals(loadedLanguage)) {
			defaultMappings.clear();
			mappings.clear();
			for (modInfo mInfo : mods) {
				load(mInfo.modName, mInfo.defaultLanguage);
			}
			loadedLanguage = getCurrentLanguage();
		}

		return mappings.getProperty(key, defaultMappings.getProperty(key, key));
	}

	private static void loadLanguage(Properties par1Properties, String path, String lang) throws IOException
	{
		InputStream is = LangLocalization.class.getResourceAsStream(path + lang + ".lang");
		if (is != null)
		{
			BufferedReader var3 = new BufferedReader(new InputStreamReader(is, "UTF-8"));

			for (String var4 = var3.readLine(); var4 != null; var4 = var3.readLine())
			{
				var4 = var4.trim();

				if (!var4.startsWith("#"))
				{
					String[] var5 = var4.split("=");

					if ((var5 != null) && (var5.length == 2))
					{
						par1Properties.setProperty(var5[0], var5[1]);
					}
				}
			}
		}
	}

	private static void load(String path, String default_language) {
		InputStream langStream = null;
		Properties modMappings = new Properties();
		try
		{
			loadLanguage(modMappings, path, default_language);
			defaultMappings.putAll(modMappings);
			applyEntityNames(defaultMappings);

			langStream = LangLocalization.class.getResourceAsStream(path + getCurrentLanguage() + ".lang");
			if (langStream != null) {
				modMappings.clear();
				loadLanguage(modMappings, path, getCurrentLanguage());
			}

			if (modMappings.containsKey("language.parent")) {
				langStream = LangLocalization.class.getResourceAsStream(path + modMappings.get("language.parent") + ".lang");

				if (langStream != null) {
					Properties parentModMappings = new Properties();
					loadLanguage(parentModMappings, path, (String)modMappings.get("language.parent"));
					mappings.putAll(parentModMappings);
				}
			}

			mappings.putAll(modMappings);
			applyEntityNames(mappings);
		}
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (langStream != null)
					langStream.close();
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private static void applyEntityNames(Properties props) {
		Enumeration en = props.keys();
		while (en.hasMoreElements()) {
			String s = (String)en.nextElement();
			if (s.startsWith("entity."))
				LanguageRegistry.instance().addStringLocalization(s, getCurrentLanguage(), (String)props.get(s));
		}
	}

	private static String getCurrentLanguage()
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			return "en_US";
		}
		return Minecraft.getMinecraft().func_135016_M().func_135041_c().func_135034_a();
	}

	private static class modInfo
	{
		final String modName;
		final String defaultLanguage;

		public modInfo(String modName, String defaultLanguage)
		{
			this.modName = modName;
			this.defaultLanguage = defaultLanguage;
		}
	}
}