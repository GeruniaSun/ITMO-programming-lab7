package lt.shgg.commands;

import lt.shgg.app.Receiver;
import lt.shgg.data.Ticket;
import lt.shgg.network.Response;

import java.io.Serial;
import java.io.Serializable;

/**
 * <h1>Команда info</h1>
 * класс инкапсулирующий объект команды info
 */
public class Info implements Command, Serializable {
    /**
     * Номер версии сериализации нужен, чтоб JVM понимала, что это один и тот же класс на клиенте и на сервере
     */
    @Serial
    private static final long serialVersionUID = 909L;
    /**
     * Переопределенные методы из интерфейса {@link Command}
     * логика описана в самом интерфейсе
     */
    @Override
    public Response execute(Object args, Ticket ticket, Receiver receiver) {
        if (args != null) throw new IllegalArgumentException("Команда info не принимает никаких аргументов");
        return receiver.info();
    }

    @Override
    public String description() {
        return "info - выводит информацию о коллекции";
    }

    @Override
    public String getName(){
        return "info";
    }
}
