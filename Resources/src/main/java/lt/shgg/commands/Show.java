package lt.shgg.commands;

import lt.shgg.app.Receiver;
import lt.shgg.data.Ticket;
import lt.shgg.network.Response;

import java.io.Serial;
import java.io.Serializable;

/**
 * <h1>Команда show</h1>
 * класс инкапсулирующий объект команды show
 */
public class Show implements Command, Serializable {
    /**
     * Номер версии сериализации нужен, чтоб JVM понимала, что это один и тот же класс на клиенте и на сервере
     */
    @Serial
    private static final long serialVersionUID = 914L;
    /**
     * Переопределенные методы из интерфейса {@link Command}
     * логика описана в самом интерфейсе
     */
    @Override
    public Response execute(Object args, Ticket ticket, Receiver receiver) {
        if (args != null) throw new IllegalArgumentException("Команда show не принимает никаких аргументов");
        return receiver.show();
    }

    @Override
    public String description() {
        return "show - выводит все элементы коллекции";
    }

    @Override
    public String getName(){
        return "show";
    }
}
