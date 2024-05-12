package lt.shgg.commands;

import lt.shgg.app.Receiver;
import lt.shgg.data.Ticket;
import lt.shgg.network.Response;

/**
 * <h1>Интерфейс команды</h1>
 * все команды в этом пакете реализуют данный интерфейс
 * со списком и функциональностью команд можно ознакомиться запустив приложение и введя команду help
 */
public interface Command {
    /**
     * Метод проверяющий аргументы и вызывающий нужный метод приемника {@link Receiver}
     * @param args аргументы переданные в одной строке с командой
     */
    Response execute(Object args, Ticket ticket, Receiver receiver);

    /**
     * Метод для реализации команды help
     * @return строковое описание функции команды
     */
    String description();

    String getName();
}
