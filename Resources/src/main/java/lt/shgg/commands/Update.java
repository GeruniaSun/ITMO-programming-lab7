package lt.shgg.commands;

import lt.shgg.app.Receiver;
import lt.shgg.data.Ticket;
import lt.shgg.network.Response;

import java.io.Serial;
import java.io.Serializable;

/**
 * <h1>Команда update</h1>
 * класс инкапсулирующий объект команды update
 */
public class Update implements Command, Serializable {
    /**
     * Номер версии сериализации нужен, чтоб JVM понимала, что это один и тот же класс на клиенте и на сервере
     */
    @Serial
    private static final long serialVersionUID = 915L;
    /**
     * Переопределенные методы из интерфейса {@link Command}
     * логика описана в самом интерфейсе
     */
    @Override
    public Response execute(Object args, Ticket ticket, Receiver receiver) {
        if (args == null)
            throw new NullPointerException("Команда update не работает без аргумента id");
        long id;
        try {
            id = Long.parseLong((String) args);
        } catch (Exception e) {
            throw new IllegalArgumentException("аргумент id должен быть числом");
        }
        return receiver.update(id, ticket);
    }

    @Override
    public String description() {
        return "update id {element} - обновляет поля элемента по его id";
    }

    @Override
    public String getName(){
        return "update";
    }
}