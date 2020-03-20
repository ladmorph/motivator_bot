package org.ladmorph.telegramBots;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

public interface Commands {

    void start(Update update, AbsSender absSender);

    void help(Update update, AbsSender absSender);
}
