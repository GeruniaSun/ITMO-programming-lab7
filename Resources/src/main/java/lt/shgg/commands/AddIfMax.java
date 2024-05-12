package lt.shgg.commands;

import lt.shgg.app.Receiver;
import lt.shgg.data.Ticket;
import lt.shgg.network.Response;

import java.io.Serial;
import java.io.Serializable;

/**
 * <h1>Команда add_if_max</h1>
 * класс инкапсулирующий объект команды add_if_max
 */
public class AddIfMax implements Command, Serializable {
    /**
     * Номер версии сериализации нужен, чтоб JVM понимала, что это один и тот же класс на клиенте и на сервере
     */
    @Serial
    private static final long serialVersionUID = 902L;
    /**
     * Переопределенные методы из интерфейса {@link Command}
     * логика описана в самом интерфейсе
     */
    @Override
    public Response execute(Object args, Ticket ticket, Receiver receiver) {
        if (args != null) throw new IllegalArgumentException("Команда add_if_max не принимает никаких аргументов, " +
                "значение element нужно вводить с новой строки");
        if (ticket == null) throw new IllegalArgumentException("Команда add_if_max не работает без билета");
        return receiver.addIfMax(ticket);
    }

    @Override
    public String description() {
        return "add_if_max {element} - добавляет элемент в коллекцию, если он превышает максимальный";
    }


    @Override
    public String getName(){
        return "add_if_max";
    }
}