package lt.shgg.commands;

import lt.shgg.app.Receiver;
import lt.shgg.data.Ticket;
import lt.shgg.network.Response;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <h1>Команда help</h1>
 * класс инкапсулирующий объект команды help
 */
public class Help implements Command, Serializable {
    /**
     * Номер версии сериализации нужен, чтоб JVM понимала, что это один и тот же класс на клиенте и на сервере
     */
    @Serial
    private static final long serialVersionUID = 908L;
    /**
     * Ссылка на коллекцию в которой хранятся все доступные пользователю команды
     */
    private final Collection<Command> commands = new ArrayList<>();

    {
        commands.add(this);
        commands.add(new Info());
        commands.add(new Show());
        commands.add(new Add());
        commands.add(new PrintDescending());
        commands.add(new FilterByType());
        commands.add(new CountGreaterThanType());
        commands.add(new Clear());
        commands.add(new RemoveById());
        commands.add(new RemoveGreater());
        commands.add(new RemoveLower());
        commands.add(new AddIfMax());
        commands.add(new Update());
        commands.add(new Exit());
        commands.add(new ExecuteScript());
    }

    /**
     * Переопределенные методы из интерфейса {@link Command}
     * логика описана в самом интерфейсе
     */
    @Override
    public Response execute(Object args, Ticket ticket, Receiver receiver) {
        if (args != null) throw new IllegalArgumentException("Команда help не принимает никаких аргументов");
        return receiver.help(this.commands);
    }

    @Override
    public String description() {
        return "help - справка по доступным командам";
    }

    @Override
    public String getName(){
        return "help";
    }
}
