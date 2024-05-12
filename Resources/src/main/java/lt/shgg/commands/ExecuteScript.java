package lt.shgg.commands;

import lt.shgg.app.Receiver;
import lt.shgg.data.Ticket;
import lt.shgg.network.Response;

import java.io.Serial;
import java.io.Serializable;

/**
 * <h1>Команда execute_script</h1>
 * класс инкапсулирующий объект команды execute_script
 */
public class ExecuteScript implements Command, Serializable {
    /**
     * Номер версии сериализации нужен, чтоб JVM понимала, что это один и тот же класс на клиенте и на сервере
     */
    @Serial
    private static final long serialVersionUID = 905L;
    /**
     * Переопределенные методы из интерфейса {@link Command}
     * логика описана в самом интерфейсе
     */
    @Override
    public Response execute(Object args, Ticket ticket, Receiver receiver) {
        if (args == null)
            throw new NullPointerException("Команда execute_script не работает без аргумента filename");
        String filename = args.toString();
        return new Response("начинаю читать " + filename);
    }

    @Override
    public String description() {
        return "execute_script filename - выполняет команды из файла filename";
    }

    @Override
    public String getName(){
        return "execute_script";
    }
}