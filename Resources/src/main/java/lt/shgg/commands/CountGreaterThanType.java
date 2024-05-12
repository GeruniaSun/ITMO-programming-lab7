package lt.shgg.commands;

import lt.shgg.app.Receiver;
import lt.shgg.data.Ticket;
import lt.shgg.network.Response;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;

/**
 * <h1>Команда count_greater_than_type</h1>
 * класс инкапсулирующий объект команды count_greater_than_type
 */
public class CountGreaterThanType implements Command, Serializable {
    /**
     * Номер версии сериализации нужен, чтоб JVM понимала, что это один и тот же класс на клиенте и на сервере
     */
    @Serial
    private static final long serialVersionUID = 904L;
    /**
     * Переопределенные методы из интерфейса {@link Command}
     * логика описана в самом интерфейсе
     */
    @Override
    public Response execute(Object args, Ticket ticket, Receiver receiver) {
        if (args == null)
            throw new NullPointerException("Команда count_greater_than_type не работает без аргумента type");
        Ticket.TicketType type;
        try {
            type = Ticket.TicketType.valueOf(((String) args).toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("типа " + args + " не существует, " +
                    "пожалуйста введите один из нижеприведенных типов\n" + Arrays.toString(Ticket.TicketType.values()));
        }
        return receiver.countGreaterThanType(type);
    }

    @Override
    public String description() {
        return "count_greater_than_type type - выводит количество элементов коллекции, " +
                "значение поля type которых больше заданного";
    }


    @Override
    public String getName(){
        return "count_greater_than_type";
    }
}
