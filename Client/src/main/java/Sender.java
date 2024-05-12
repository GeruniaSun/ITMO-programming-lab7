import lt.shgg.network.Request;
import lt.shgg.network.Response;

import java.io.*;
import java.net.*;
import java.util.Objects;

/**
 * <h1>Класс отправителя</h1>
 * Отправитель содержит всю логику соединения с сервером, он отправляет запрос и он же получает на него ответ
 */
public class Sender {

    private final String host;
    private final int port;
    private final int reconnectionTimeout;
    private final int maxReconnectionAttempts;

    private Socket socket;
    private ObjectOutputStream serverWriter;
    private ObjectInputStream serverReader;


    public Sender(String host, int port, int reconnectionTimeout, int maxReconnectionAttempts) {
        this.host = host;
        this.port = port;
        this.reconnectionTimeout = reconnectionTimeout;
        this.maxReconnectionAttempts = maxReconnectionAttempts;
    }

    public Response sendRequest(Request request) throws InterruptedException {
        this.connect();
        int reconnectionAttempts = 0;
        while(true) {
            try {
                if (Objects.isNull(serverWriter) || Objects.isNull(serverReader)) throw new IOException();
                if (request.getCommand() == null) System.err.println("Запрос пустой!");
                serverWriter.writeObject(request);
                serverWriter.flush();
                Response response = (Response) serverReader.readObject();
                this.disconnect();
                return response;
            } catch (IOException ignored) {
                reconnectionAttempts++;
                if(reconnectionAttempts >= maxReconnectionAttempts){
                    ignored.printStackTrace();
                    break;
                }
                System.err.println("Повторная попытка через " + reconnectionTimeout / 1000 + " секунд");
                Thread.sleep(reconnectionTimeout);
                this.connect();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }


    public void connect(){
        try {
            socket = new Socket(host, port);
            serverWriter = new ObjectOutputStream(socket.getOutputStream());
            serverReader = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println("Произошла ошибка при соединении с сервером");
        }
    }

    public void disconnect() {
        try {
            socket.close();
            serverReader.close();
            serverWriter.close();
        } catch (IOException e) {
            System.err.println("Не подключен к серверу");;
        }
    }

}
