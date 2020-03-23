package org.ladmorph.telegramBots.config;

public class BotConfig {

    public final static String MOTIVATOR_TOKEN = "1108645653:AAE0kD1pP6dO-0teYSboB5uYmmFVa6-7xpw";
    public final static String MOTIVATOR_USERNAME = "super_motivation_bot";

    public final static String MOTIVATOR_HANDLE_TASKS_MESSAGE_SUCCESS = "<b>Вы успешно добавили новую задачу</b> \uD83D\uDE43";
    public final static String MOTIVATOR_MESSAGE_FAILURE = "<b>Ошибка, операция недоступна!</b>";
    public final static String MOTIVATOR_ADD_TASK_MESSAGE = "Напишите задачу, которую вы хотите добавить \uD83D\uDE0F";
    public final static String MOTIVATOR_HELP_MESSAGE =
            "/add_task - добавление ваших задач на день.\n" +
            "/get_tasks - посмотреть уже добавленные задачи.";
    public final static String MOTIVATOR_START_MESSAGE =
            "<b>Тебя приветствтует Мотиватор бот!</b>" +
            "\nДля просмотора команд выполните команду /help";

    public final static String DEFAULT_MESSAGE = "Я тебя не понимаю.";

    public final static String MOTIVATOR_DELETE_TASK_MESSAGE = "Ваши задания успешно выполнены!";
}
