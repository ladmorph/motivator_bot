package org.ladmorph.telegramBots.motivatorBot;

import org.ladmorph.telegramBots.config.BotConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class MotivatorBot extends TelegramLongPollingBot {

    private final Logger log = LoggerFactory.getLogger(MotivatorBot.class);
    private final MotivatorBotCommands motivatorBotCommands = new MotivatorBotCommands();


    public MotivatorBot() {
    }

    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            String textMessage = update.getMessage().getText();

            if (textMessage.equals("/start")) {
                motivatorBotCommands.start(update, this);
            }

            if (textMessage.equals("/help")) {
                motivatorBotCommands.help(update, this);
            }
        }
    }

    public String getBotUsername() {
        return BotConfig.MOTIVATOR_USERNAME;
    }

    public String getBotToken() {
        return BotConfig.MOTIVATOR_TOKEN;
    }

}

