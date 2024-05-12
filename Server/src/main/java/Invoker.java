import lt.shgg.app.Receiver;
import lt.shgg.commands.Command;
import lt.shgg.network.Response;
import lt.shgg.data.Ticket;
import lt.shgg.network.Request;

/**
 * <h1>Класс инициатора</h1>
 * инициатор - сущность запускающая команды
 */
public class Invoker {
    /**
     * Метод для выполнения команды
     */
    public Response runCommand(Request request, Receiver receiver) {
        Command command = request.getCommand();
        Object args = request.getArgs();
        Ticket ticket = request.getTicket();
        try {
            return command.execute(args, ticket, receiver);
        } catch (Exception e) {
            return new Response("Случилась беда " + e.getMessage());
        }
    }
}