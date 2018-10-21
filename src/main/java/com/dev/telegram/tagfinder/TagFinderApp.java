package com.dev.telegram.tagfinder;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TagFinderApp {

	public static void main(String[] args) {

		ApiContextInitializer.init();

		TelegramBotsApi botsApi = new TelegramBotsApi();

		try {
			botsApi.registerBot(new TagFinderBot());
			System.out.println("Bot is started!");
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

}
