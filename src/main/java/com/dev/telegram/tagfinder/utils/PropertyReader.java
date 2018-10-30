package com.dev.telegram.tagfinder.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyReader {

	private PropertyReader() {
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(PropertyReader.class);
	private static Properties properties = new Properties();

	static {
		try (final InputStream stream = PropertyReader.class
				.getResourceAsStream( Constants.BOT_PROPERTIES_FILE)) {
			properties.load(stream);
		} catch (IOException e) {
			LOGGER.error("Not able to access " + Constants.BOT_PROPERTIES_FILE + " file.", e);
		}
	}

	public static String getProperty(String key) {
		return properties.getProperty(key);
	}

	public static void main(String[] args) {
		System.out.println(getProperty("bot.username"));
	}

}
