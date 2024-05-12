package lt.shgg.network;

import java.io.Serial;
import java.io.Serializable;

/**
 * Response - ответ сервера на запрос клиента, содержит результат выполнения команды
 */
public class Response implements Serializable {
    @Serial
    private static final long serialVersionUID = 21L;

    private final String result;

    public Response(String result){
        this.result = result;
    }

    public String getResult() {
        return result;
    }
}
