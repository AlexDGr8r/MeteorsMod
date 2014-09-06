package net.meteor.common.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class Util {
	
	public static List<String> getFormattedLines(String unlocal, EnumChatFormatting ecf) {
		List<String> lines = new ArrayList<String>();
		String s = StatCollector.translateToLocalFormatted(unlocal, "\n");
		String[] seperated = s.split("\\n");
		for (int i = 0; i < seperated.length; i++) {
			lines.add(ecf + seperated[i]);
		}
		return lines;
	}

}
