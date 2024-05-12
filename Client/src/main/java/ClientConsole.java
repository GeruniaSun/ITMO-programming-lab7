import lt.shgg.commands.*;
import lt.shgg.network.Request;

import java.io.InputStreamReader;
import java.util.*;

/**
 * <h1>Класс консоли</h1>
 * объект класса представляет консоль для клиента считывающую команды
 */
public class ClientConsole {
    private final Map<String, Command> commandMap = new HashMap<>();

    {
        commandMap.put("info", new Info());
        commandMap.put("help", new Help());
        commandMap.put("show", new Show());
        commandMap.put("add", new Add());
        commandMap.put("print_descending", new PrintDescending());
        commandMap.put("filter_by_type", new FilterByType());
        commandMap.put("count_greater_than_type", new CountGreaterThanType());
        commandMap.put("clear", new Clear());
        commandMap.put("remove_by_id", new RemoveById());
        commandMap.put("remove_greater", new RemoveGreater());
        commandMap.put("remove_lower", new RemoveLower());
        commandMap.put("add_if_max", new AddIfMax());
        commandMap.put("update", new Update());
        commandMap.put("exit", new Exit());
        commandMap.put("execute_script", new ExecuteScript());
    }
    /**
     * Метод запускающий консоль
     * @param inStream поток ввода (стандартный или файловый)
     * @param sender отправитель запросов на сервер
     * @param fileFlag флаг истинный если выполняется команда execute_script и ложный в противном случае
     */
    public void runApp(InputStreamReader inStream, Sender sender, Boolean fileFlag){
        Scanner in = new Scanner(inStream);
        while (in.hasNextLine()){
            try {
                //Чтение и обработка ввода
                var input = in.nextLine();
                if (input.isBlank()) {throw new NullPointerException("Вы ничего не ввели");}
                List<String> commandWithArgs = List.of(input.split(" "));
                String commandName = commandWithArgs.get(0).toLowerCase();
                List<String> commandArguments = null;
                if (commandWithArgs.size() >= 2){
                    commandArguments = commandWithArgs.subList(1, commandWithArgs.size());
                }
                if (commandMap.get(commandName) == null)
                    throw new IllegalArgumentException("Такой команды нет в меню");

                //Построение запроса на сервер
                var request = new Request();
                request.setCommand(commandMap.get(commandName));
                if (commandArguments != null)request.setArgs(String.join(" ",  commandArguments));
                if (List.of("add", "add_if_max", "remove_greater", "remove_lower", "update").contains(commandName)) {
                    if (fileFlag){
                        request.setTicket(TicketAppender.appendTicket(in.nextLine()));
                    } else request.setTicket(TicketAppender.appendTicket());
                }

                //Получение и вывод ответа сервера
                var response = sender.sendRequest(request);
                try {
                    System.out.println(response.getResult());
                } catch (NullPointerException e) {
                    System.err.println("сервер молчит, видимо ему не до нас...");
                }

                if (commandName.equals("exit")) {
                    System.exit(777);
                }
                if (commandName.equals("execute_script")) {
                    try {
                        var output = List.of(response.getResult().split(" "));
                        var filename = output.get(output.size() - 1);
                        var scriptRunner = new ScriptRunner();
                        scriptRunner.runScript(filename);
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                }
            } catch (NoSuchElementException e) {
                System.err.println("вы какие-то гадости делаете. Закрываю приложение");
                System.exit(999);
            }
            catch (Exception e){
                if (!fileFlag)
                    System.err.println(e.getMessage());
            }
        }
    }
}
