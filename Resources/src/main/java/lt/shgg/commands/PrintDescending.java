package lt.shgg.commands;

import lt.shgg.app.Receiver;
import lt.shgg.data.Ticket;
import lt.shgg.network.Response;

import java.io.Serial;
import java.io.Serializable;

/**
 * <h1>Команда print_descending</h1>
 * класс инкапсулирующий объект команды print_descending
 */
public class PrintDescending implements Command, Serializable {
    /**
     * Номер версии сериализации нужен, чтоб JVM понимала, что это один и тот же класс на клиенте и на сервере
     */
    @Serial
    private static final long serialVersionUID = 910L;
    /**
     * Переопределенные методы из интерфейса {@link Command}
     * логика описана в самом интерфейсе
     */
    @Override
    public Response execute(Object args, Ticket ticket, Receiver receiver) {
        if (args != null) throw new IllegalArgumentException("Команда print_descending не принимает никаких аргументов");
        return receiver.printDescending();
    }

    @Override
    public String description() {
        return "print_descending - выводит все элементы коллекции в порядке убывания";
    }

    @Override
    public String getName(){
        return "print_descending";
    }
}
