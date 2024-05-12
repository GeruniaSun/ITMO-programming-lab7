package lt.shgg.data;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * <h1>Класс описывающий сущность координат</h1>
 * координаты - обязательное поле экземпляра класса {@link lt.shgg.data.Ticket}
 */
public class Coordinates implements Serializable {
    /**
     * Координата по оси абсцисс
     */
    @JacksonXmlProperty
    private final float x;
    /**
     * Координата по оси ординат
     * значение не должно превышать 202
     */
    @JacksonXmlProperty
    private final int y;
    /**
     * Номер версии сериализации нужен, чтоб JVM понимала, что это один и тот же класс на клиенте и на сервере
     */
    @Serial
    private static final long serialVersionUID = 1488L;

    /**
     * Конструктор класса
     * @param x значение для поля x
     * @param y значение для поля y
     */
    public Coordinates(float x, int y) {
        if (y > 202) throw new IllegalArgumentException("значение ординаты слишком велико");
        this.x = x;
        this.y = y;
    }

    /**
     * Стандартный конструктор для корректной работы парсера XML
     */
    private Coordinates () {
        this.x = 1.5F;
        this.y = 1;
    }

    /**
     * Стандартный метод для строкового представления объекта
     * @return строку включающую значения всех полей объекта
     */
    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    /**
     * Стандартный метод из класса Object для сравнения объектов
     * @param otherObject сравниваемый объект
     * @return истину в случае эквивалентности объектов, ложь в противном случае
     */
    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (otherObject == null || this.getClass() != otherObject.getClass()) return false;
        Coordinates other = (Coordinates) otherObject;
        return Math.abs(Float.compare(x, other.x)) < 0.0001 && y == other.y;
    }

    /**
     * Стандартный метод из класса Object для вычисления хеш-кода
     * @return хеш-код объекта
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
