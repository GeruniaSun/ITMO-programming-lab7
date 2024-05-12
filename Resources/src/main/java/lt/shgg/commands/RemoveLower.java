package lt.shgg.commands;

import lt.shgg.app.Receiver;
import lt.shgg.data.Ticket;
import lt.shgg.network.Response;

import java.io.Serial;
import java.io.Serializable;

/**
 * <h1>Команда remove_lower</h1>
 * класс инкапсулирующий объект команды remove_lower
 */
public class RemoveLower implements Command, Serializable {
    /**
     * Номер версии сериализации нужен, чтоб JVM понимала, что это один и тот же класс на клиенте и на сервере
     */
    @Serial
    private static final long serialVersionUID = 913L;
    /**
     * Переопределенные методы из интерфейса {@link Command}
     * логика описана в самом интерфейсе
     */
    @Override
    public Response execute(Object args, Ticket ticket, Receiver receiver) {
        if (args != null)
            throw new IllegalArgumentException("Команда remove_lower не принимает никаких аргументов, " +
                "значение element нужно вводить с новой строки");
        return receiver.removeLower(ticket);
    }

    @Override
    public String description() {
        return "remove_lower {element} - удаляет из коллекции все элементы, большие, чем заданный";
    }

    @Override
    public String getName(){
        return "remove_lower";
    }
}
