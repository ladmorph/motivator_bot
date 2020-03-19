package org.ladmorph.telegramBots.motivatorBot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class MotivatorBot extends TelegramLongPollingBot {

    private final MotivatorBotCommands motivatorBotCommands = new MotivatorBotCommands();

    public void onUpdateReceived(Update update) {
        String textMessage = update.getMessage().getText();

        if (textMessage.equals("/start")) {
            motivatorBotCommands.start(update, this);
        }

        if (textMessage.equals("/help")) {
            motivatorBotCommands.help(update, this);
        }
    }

    public String getBotUsername() {
        return "super_motivation_bot";
    }

    public String getBotToken() {
        return "1108645653:AAE0kD1pP6dO-0teYSboB5uYmmFVa6-7xpw";
    }

}

