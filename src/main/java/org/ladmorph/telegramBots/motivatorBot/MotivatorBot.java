package org.ladmorph.telegramBots.motivatorBot;

import org.ladmorph.telegramBots.config.BotConfig;
import org.ladmorph.telegramBots.database.DataBaseManager;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class MotivatorBot extends TelegramLongPollingBot {

    private final MotivatorBotCommands motivatorBotCommands = new MotivatorBotCommands();
    private final String START_COMMAND = "/start";
    private final String HELP_COMMAND = "/help";
    private final String ADD_TASK_COMMAND = "/add_task";
    private final String GET_TASKS_COMMAND = "/get_tasks";
    private final String DELETE_TASKS_COMMAND = "/delete_tasks";
    private DataBaseManager dataBaseManager = DataBaseManager.getInstance();


    public MotivatorBot() {
    }

    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            handleIncomingMessage(update);
        } else if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getData().equals("task")) {
                dataBaseManager.setUserState(update.getCallbackQuery().getFrom().getId(), true);
                motivatorBotCommands.addTask(update, this);
            }
            if (update.getCallbackQuery().getData().equals("delete_task")) {
                motivatorBotCommands.deleteAllTask(update, this);
            }
        }
    }

    public String getBotUsername() {
        return BotConfig.MOTIVATOR_USERNAME;
    }

    public String getBotToken() {
        return BotConfig.MOTIVATOR_TOKEN;
    }

    public void handleIncomingMessage(Update update) {
        String textMessage = update.getMessage().getText();
        if (!dataBaseManager.getUserState(update.getMessage().getFrom().getId())) {

            switch (textMessage) {
                case START_COMMAND:
                    motivatorBotCommands.start(update, this);
                    break;
                case HELP_COMMAND:
                    motivatorBotCommands.help(update, this);
                    break;
                case GET_TASKS_COMMAND:
                    motivatorBotCommands.getTasks(update, this);
                    break;
                case DELETE_TASKS_COMMAND:
                    motivatorBotCommands.deleteAllTask(update, this);
                    break;
            }

            if (textMessage.equals(ADD_TASK_COMMAND)) {
                motivatorBotCommands.addTask(update, this);
                dataBaseManager.setUserState(update.getMessage().getFrom().getId(), true);
            } else {
                motivatorBotCommands.handleAddTask(update, this);
            }

        } else {
            motivatorBotCommands.handleAddTask(update, this);
        }
    }
}

