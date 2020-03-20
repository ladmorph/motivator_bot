package org.ladmorph.telegramBots.motivatorBot;

import org.ladmorph.telegramBots.Commands;
import org.ladmorph.telegramBots.config.BotConfig;
import org.ladmorph.telegramBots.database.DataBaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MotivatorBotCommands implements Commands {

    private static final Logger log = LoggerFactory.getLogger(MotivatorBotCommands.class);
    private Set<String> commands = Set.of("/add_task", "/help", "/start", "/get_tasks");
    private DataBaseManager dataBaseManager = DataBaseManager.getInstance();

    /**
     * @param update    - this object stores all the necessary information about the user who sent the message
     * @param absSender - this object is responsible for sending a message to the user
     */
    @Override
    public void start(Update update, AbsSender absSender) {
        SendMessage message = new SendMessage()
                .enableMarkdown(true)
                .setChatId(update.getMessage().getChatId())
                .setText(BotConfig.MOTIVATOR_START_MESSAGE);

        SendAnimation sendAnimation = new SendAnimation()
                .setChatId(update.getMessage().getChatId())
                .setAnimation(new File("src/main/resources/stickers/GreetingsSticker.tgs"));

        try {
            absSender.execute(message);
            absSender.execute(sendAnimation);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * @param update    - this object stores all the necessary information about the user who sent the message
     * @param absSender - this object is responsible for sending a message to the user
     */
    @Override
    public void help(Update update, AbsSender absSender) {


        SendMessage message = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText(BotConfig.MOTIVATOR_HELP_MESSAGE);

        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * @param update    - this object stores all the necessary information about the user who sent the message
     * @param absSender - this object is responsible for sending a message to the user
     */
    public void addTask(Update update, AbsSender absSender) {

        SendMessage sendMessage = new SendMessage();
        try {
            sendMessage.setChatId(update.getMessage().getChatId());
        } catch (NullPointerException e) {
            sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        sendMessage.setText(BotConfig.MOTIVATOR_ADD_TASK_MESSAGE);
        sendMessage.enableMarkdown(true);

        try {
            absSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * @param update    - this object stores all the necessary information about the user who sent the message
     * @param absSender - this object is responsible for sending a message to the user
     */
    public void handleAddTask(Update update, AbsSender absSender) {
        if (dataBaseManager.getUserState(update.getMessage().getFrom().getId())) {

            List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
            List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
            inlineKeyboardButtons.add(new InlineKeyboardButton().setText("Добавить еще одну задачу!").setCallbackData("1"));
            buttons.add(inlineKeyboardButtons);

            InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
            markupKeyboard.setKeyboard(buttons);

            dataBaseManager.setUserState(update.getMessage().getFrom().getId(), false);
            if (update.hasMessage()) {
                String task = update.getMessage().getText();
                if (commands.contains(task)) {
                    SendMessage sendMessage = new SendMessage()
                            .setChatId(update.getMessage().getChatId())
                            .setText(BotConfig.MOTIVATOR_HANDLE_TASKS_MESSAGE_FAILURE)
                            .enableMarkdown(true);

                    try {
                        absSender.execute(sendMessage);
                    } catch (TelegramApiException e) {
                        log.error(e.getMessage(), e);
                    }
                }

                int idByUserId = dataBaseManager.getIdByUserId(update.getMessage().getFrom().getId());
                if (dataBaseManager.addTask(task, idByUserId)) {

                    SendMessage sendMessage = new SendMessage()
                            .setChatId(update.getMessage().getChatId())
                            .setText(BotConfig.MOTIVATOR_HANDLE_TASKS_MESSAGE_SUCCESS)
                            .enableMarkdown(true)
                            .setReplyMarkup(markupKeyboard);

                    try {
                        absSender.execute(sendMessage);
                    } catch (TelegramApiException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }
    }


    /**
     * @param update    - this object stores all the necessary information about the user who sent the message
     * @param absSender - this object is responsible for sending a message to the user
     */
    public void getTasks(Update update, AbsSender absSender) {
        int idByUserId = dataBaseManager.getIdByUserId(update.getMessage().getFrom().getId());
        SendMessage sendMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText("Ваши задачи на сегодня \uD83D\uDE1C:\n" + String.join("\n", dataBaseManager.fetchAllTaskByUserId(idByUserId)));

        try {
            absSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }
}
