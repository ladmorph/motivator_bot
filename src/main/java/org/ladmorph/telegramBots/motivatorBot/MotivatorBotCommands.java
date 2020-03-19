package org.ladmorph.telegramBots.motivatorBot;

import org.ladmorph.telegramBots.Commands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MotivatorBotCommands implements Commands {

    private static final Logger log = LoggerFactory.getLogger(MotivatorBotCommands.class);

    /**
     * @param update    - this object stores all the necessary information about the user who sent the message
     * @param absSender - this object is responsible for sending a message to the user
     */
    @Override
    public void start(Update update, AbsSender absSender) {
        SendMessage message = new SendMessage()
                .enableMarkdown(true)
                .setChatId(update.getMessage().getChatId())
                .setText("Тебя приветствтует **Мотиватор бот!**" +
                        "\nДля просмотора команд выполните команду /help");

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

        KeyboardRow zakhar = new KeyboardRow();
        zakhar.add(new KeyboardButton().setText("Пнуть Захара!"));

        KeyboardRow grisha = new KeyboardRow();
        grisha.add(new KeyboardButton().setText("Пнуть Гришу!"));

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        keyboardRows.add(zakhar);
        keyboardRows.add(grisha);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);

        SendMessage message = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText("1) Добавить задание /add_task")
                .setReplyMarkup(replyKeyboardMarkup);

        try {
            absSender.execute(message);

            if (update.getCallbackQuery().getMessage().getText().equals("Пнуть Захара!")) {
                System.out.println("Пнем Захара");
            } else {
                System.out.println("Пнуть Гришу");
            }

        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * @param update    - this object stores all the necessary information about the user who sent the message
     * @param absSender - this object is responsible for sending a message to the user
     */
    @Override
    public void addTask(Update update, AbsSender absSender) {
    }
}
