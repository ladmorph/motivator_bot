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
                .setText(BotConfig.MOTIVATOR_START_MESSAGE)
                .enableHtml(true);

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


            List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
            inlineKeyboardButtons.add(new InlineKeyboardButton().setText("Добавить еще одну задачу!").setCallbackData("task"));

            InlineKeyboardMarkup markupKeyboard = setUpInlineKeyboardMarkup(inlineKeyboardButtons);

            dataBaseManager.setUserState(update.getMessage().getFrom().getId(), false);
            if (update.hasMessage()) {
                String task = update.getMessage().getText();

                if (commands.contains(task)) {
                    sendFailureMessage(update, absSender);
                    return;
                }

                int userId = dataBaseManager.getIdByUserId(update.getMessage().getFrom().getId());
                if (dataBaseManager.addTask(task, userId)) {

                    SendMessage sendMessage = new SendMessage()
                            .setChatId(update.getMessage().getChatId())
                            .setText(BotConfig.MOTIVATOR_HANDLE_TASKS_MESSAGE_SUCCESS)
                            .enableHtml(true)
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

        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
        inlineKeyboardButtons.add(new InlineKeyboardButton().setText("Завершить все задачи!").setCallbackData("delete_task"));

        InlineKeyboardMarkup markupKeyboard = setUpInlineKeyboardMarkup(inlineKeyboardButtons);

        int idByUserId = dataBaseManager.getIdByUserId(update.getMessage().getFrom().getId());

        SendMessage sendMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .enableHtml(true)
                .setText("<b>Ваши задачи на сегодня:</b>\n\n" + "<em>"
                        + String.join("\n", dataBaseManager.fetchAllTaskByUserId(idByUserId)) + "</em>")
                .setReplyMarkup(markupKeyboard);

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
    private void sendFailureMessage(Update update, AbsSender absSender) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText(BotConfig.MOTIVATOR_MESSAGE_FAILURE)
                .enableHtml(true);

        try {
            absSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }

    private InlineKeyboardMarkup setUpInlineKeyboardMarkup(List<InlineKeyboardButton> buttonList) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(buttonList);

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);

        return markupKeyboard;
    }


    /**
     * @param update    - this object stores all the necessary information about the user who sent the message
     * @param absSender - this object is responsible for sending a message to the user
     */
    public void deleteAllTask(Update update, AbsSender absSender) {
        int userId = 0;

        SendMessage sendMessage = new SendMessage();
        try {
            sendMessage.setChatId(update.getMessage().getChatId());
            userId = dataBaseManager.getIdByUserId(update.getMessage().getFrom().getId());
        } catch (NullPointerException e) {
            sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
            userId = dataBaseManager.getIdByUserId(update.getCallbackQuery().getFrom().getId());
        }

        sendMessage.setText(BotConfig.MOTIVATOR_DELETE_TASK_MESSAGE);
        dataBaseManager.deleteAllTaskByUserId(userId);

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
    public void sendDefaultMessage(Update update, AbsSender absSender) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText(BotConfig.DEFAULT_MESSAGE);

        try {
            absSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }
}
