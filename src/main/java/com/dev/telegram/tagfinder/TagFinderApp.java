package com.dev.telegram.tagfinder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TagFinderApp {

	public static final Logger LOGGER = LoggerFactory.getLogger(TagFinderApp.class);

	public static void main(String[] args) {

		ApiContextInitializer.init();

		TelegramBotsApi botsApi = new TelegramBotsApi();

		try {
			botsApi.registerBot(new TagFinderBot());
			LOGGER.info("TagFinderBot has started!");
		} catch (TelegramApiException e) {
			LOGGER.error("Not able to start TagFinderBot.", e);
		}
	}

}
