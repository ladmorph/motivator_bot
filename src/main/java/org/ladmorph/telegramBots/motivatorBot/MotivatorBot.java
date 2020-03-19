package org.ladmorph.telegramBots.motivatorBot;

import org.ladmorph.telegramBots.config.BotConfig;
import org.ladmorph.telegramBots.database.DataBaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class MotivatorBot extends TelegramLongPollingBot {

    private final Logger log = LoggerFactory.getLogger(MotivatorBot.class);
    private final MotivatorBotCommands motivatorBotCommands = new MotivatorBotCommands();
    DataBaseManager dataBaseManager = DataBaseManager.getInstance();


    public MotivatorBot() {
    }

    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            String textMessage = update.getMessage().getText();

            if (dataBaseManager.getUserState(update.getMessage().getFrom().getId())) {
                dataBaseManager.setUserState(update.getMessage().getFrom().getId(), false);
                if (update.hasMessage()) {
                    System.out.println(update.getMessage().getText());
                }
            }
            if (textMessage.equals("/start")) {
                motivatorBotCommands.start(update, this);
            }

            if (textMessage.equals("/help")) {
                motivatorBotCommands.help(update, this);
                dataBaseManager.setUserState(update.getMessage().getFrom().getId(), true);
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

