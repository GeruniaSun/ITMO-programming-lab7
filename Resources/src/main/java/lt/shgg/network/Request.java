package lt.shgg.network;

import lt.shgg.commands.Command;
import lt.shgg.data.Ticket;

import java.io.Serializable;

/**
 * Request - это запрос, который клиент отправляет на сервер, содержит объект команды и ее аргументы
 */
public class Request implements Serializable {
    private static final Long SerialVersionUID = 456L;
    private Command command;
    private Ticket ticket;
    private Object args;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public void setArgs(Object args) {
        this.args = args;
    }

    public Object getArgs() {
        return args;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public Command getCommand() {
        return command;
    }

    @Override
    public String toString() {
        return "Request: " +
                "command= " + command +
                ", ticket= " + ticket +
                ", args= " + args;
    }
}
