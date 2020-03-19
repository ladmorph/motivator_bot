package org.ladmorph.telegramBots;

import org.ladmorph.telegramBots.motivatorBot.MotivatorBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        ApiContextInitializer.init();

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new MotivatorBot());
            log.info("Motivator bot is starting!");
        } catch (TelegramApiRequestException e) {
            log.error(e.getMessage(), e);
        }
    }
}
