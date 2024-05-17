import lt.shgg.app.Receiver;
import lt.shgg.database.DatabaseParser;
import lt.shgg.network.Request;
import lt.shgg.network.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.*;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class Server {

    ServerSocketChannel ss;

    private final int port;

    private static final Logger serverLogger = LogManager.getLogger("ServerLogger");

    BufferedInputStream bf = new BufferedInputStream(System.in);
    BufferedReader scanner = new BufferedReader(new InputStreamReader(bf));
    private final Receiver receiver;
    private final Invoker invoker = new Invoker();

    private final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    private final ForkJoinPool forkJoinPool = new ForkJoinPool(3);

    private final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);

    private Response response;
    private  Request request;

    public Server(int port, Receiver receiver) {
        this.port = port;
        this.receiver = receiver;
    }

    public void run() {
        try {
            openServerSocket();
            serverLogger.info("Добро пожаловать на сервер шизофрения");

            while (true) {
                if (scanner.ready()) {
                    String line = scanner.readLine();
                    if (line.equals("save")) {
                        this.receiver.save();
                        serverLogger.info("Объекты успешно сохранены");
                    } else if (line.equals("exit")) {
                        this.receiver.save();
                        serverLogger.info("Объекты успешно сохранены");
                        System.exit(0);
                    } else System.out.println("не реагирую...");
                }

                SocketChannel clientSocket = ss.accept();
                if (clientSocket != null) {
                    processClientRequest(clientSocket);
                }
            }
        } catch (IOException e) {
            serverLogger.error("Произошла ошибка при работе сервера{}", e.getMessage());
        }
    }

    private void openServerSocket() {
        try {
            ss = ServerSocketChannel.open();
            ss.bind(new InetSocketAddress(port));
            ss.configureBlocking(false);
        } catch (IOException exception) {
            serverLogger.warn("Произошла ошибка при попытке использовать порт");
        }
    }

    private void processClientRequest(SocketChannel clientSocket) {
        try (ObjectInputStream clientReader = new ObjectInputStream(clientSocket.socket().getInputStream());
             ObjectOutputStream clientWriter = new ObjectOutputStream(clientSocket.socket().getOutputStream())) {

            cachedThreadPool.submit(() -> readRequest(clientReader)).get();
            serverLogger.info("Получен запрос {}", request);

            forkJoinPool.submit(() -> commandExecution(request)).get();

            fixedThreadPool.submit(() -> sendResponse(response, clientWriter)).get();
            serverLogger.info("Отправлен ответ {}", response.getResult());

        } catch (InvalidClassException | NotSerializableException e) {
            serverLogger.warn("Произошла ошибка при взаимодействии с клиентом!");
        } catch (IOException exception) {
            serverLogger.warn("Ошибка ввода вывода {}", exception.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            serverLogger.warn("Ошибка многопоточности {}", e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                serverLogger.warn("Ошибка при закрытии клиентского сокета");
            }
        }
    }

    private synchronized void readRequest(ObjectInputStream objectInputStream) {
        try {
            request = (Request) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            serverLogger.warn(e.getMessage());
        }


    }


    private Response commandExecution(Request request) {
        response = invoker.runCommand(request, this.receiver);
        return response;
    }

    private synchronized void sendResponse(Response s,  ObjectOutputStream writer) {
        try {
            writer.writeObject(s);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
