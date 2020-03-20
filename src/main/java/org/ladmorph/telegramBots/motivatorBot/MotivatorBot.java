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

            if (textMessage.equals("/start")) {
                motivatorBotCommands.start(update, this);
            }

            if (textMessage.equals("/help")) {
                motivatorBotCommands.help(update, this);
            }

            if (textMessage.equals("/add_task")) {
                motivatorBotCommands.addTask(update, this);
                dataBaseManager.setUserState(update.getMessage().getFrom().getId(), true);
            } else {
                motivatorBotCommands.handleAddTask(update, this);
            }
            if (textMessage.equals("/get_tasks")) {
                motivatorBotCommands.getTasks(update, this);
            }
        }
        if (update.hasCallbackQuery()) {
            dataBaseManager.setUserState(update.getCallbackQuery().getFrom().getId(), true);
            motivatorBotCommands.addTask(update, this);
            }

        }

    public String getBotUsername() {
        return BotConfig.MOTIVATOR_USERNAME;
    }

    public String getBotToken() {
        return BotConfig.MOTIVATOR_TOKEN;
    }

}

