package com.dev.telegram.tagfinder;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.dev.aws.ml.utils.AmazonRekognitionUtil;
import com.dev.telegram.tagfinder.utils.Constants;
import com.dev.telegram.tagfinder.utils.PropertyReader;

public class TagFinderBot extends TelegramLongPollingBot {

	private static final Logger LOGGER = LoggerFactory.getLogger(TagFinderBot.class);

	@Override
	public String getBotUsername() {
		return PropertyReader.getProperty(Constants.BOT_USERNAME);
	}

	@Override
	public void onUpdateReceived(Update update) {

		String msg = null;
		if (!update.getMessage().hasPhoto()) {
			String name = update.getMessage().getFrom().getFirstName();
			msg = "Hi " + name + "! Please send image.";
		} else {
			PhotoSize photoSize = update.getMessage().getPhoto().get(2);

			ByteBuffer buffer = getPhotoData(photoSize);
			if (buffer == null) {
				msg = "Oops! I am not able to process this image 😒";
			} else {
				List<String> tags = AmazonRekognitionUtil.getLabels(buffer);

				if (tags.isEmpty()) {
					msg = "Sorry I am not able to find any tags in this image 😔";

				} else {
					StringBuilder builder = new StringBuilder();
					builder.append("🤖 I find following tags -\n");
					for (String tag : tags) {
						builder.append(tag);
						builder.append(", ");
					}
					builder.delete(builder.length() - 2, builder.length() - 1);
					msg = builder.toString();
				}
			}
		}
		postMsg(update.getMessage().getChatId(), msg);

	}

	@Override
	public String getBotToken() {
		return PropertyReader.getProperty(Constants.BOT_TOKEN);
	}

	private ByteBuffer getPhotoData(PhotoSize photoSize) {
		GetFile getFileMethod = new GetFile();
		getFileMethod.setFileId(photoSize.getFileId());
		org.telegram.telegrambots.meta.api.objects.File file = null;
		File localFile = null;
		try {
			file = this.execute(getFileMethod);
			localFile = this.downloadFile(file.getFilePath());
		} catch (TelegramApiException ex) {
			LOGGER.error("Not able to download file from telegram.", ex);
			return null;
		}
		byte[] bytes = null;
		try {
			bytes = Files.readAllBytes(Paths.get(localFile.getPath()));
		} catch (IOException e) {
			LOGGER.error("Not able to get bytes from the file.", e);
			return null;
		}
		return ByteBuffer.wrap(bytes);
	}

	private void postMsg(long chatId, String reply) {
		SendMessage message = new SendMessage().setChatId(chatId).setText(reply);
		try {
			execute(message);
			LOGGER.info("sending msg - {}", reply);
		} catch (TelegramApiException e) {
			LOGGER.error("Not able to send messge.", e);
		}
	}

}
