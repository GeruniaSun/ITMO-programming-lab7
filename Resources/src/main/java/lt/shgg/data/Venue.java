package lt.shgg.data;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * <h1>Класс описывающий сущность места проведения</h1>
 * место проведения - необязательное поле экземпляра класса {@link lt.shgg.data.Ticket}
 */
public class Venue implements Serializable {
    /**
     * Уникальный положительный номер места проведения в системе, значение этого поля генерируется автоматически
     */
    @JacksonXmlProperty
    private int id;
    /**
     * Номер версии сериализации нужен, чтоб JVM понимала, что это один и тот же класс на клиенте и на сервере
     */
    @Serial
    private static final long serialVersionUID = 39L;
    /**
     * Название места проведения<br>
     * это поле не может быть null или пустой строкой
     */
    @JacksonXmlProperty
    private String name;
    /**
     * Вместимость места проведения<br>
     * значения поля должно быть положительным числом
     */
    @JacksonXmlProperty
    private int capacity;
    /**
     * Адрес места проведения представлен экземпляром специального класса {@link lt.shgg.data.Venue.Address}
     * это поле может быть null
     */
    @JacksonXmlProperty
    private Address address;

    /**
     * Конструктор вызываемый классом строителем {@link lt.shgg.data.VenueBuilder} для создания нового места проведения
     * @param id значение для поля id
     * @param name значение для поля name
     * @param capacity значение для поля capacity
     * @param address значение для поля address
     */
    public Venue(int id, String name, int capacity, Address address) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.address = address;
    }

    /**
     * Стандартный конструктор для корректной работы парсера XML
     */
    private Venue(){}

    /**
     * <h2>Класс описывающий сущность адреса места проведения</h2>
     * адрес - необязательное поля места проведения
     */
    public static class Address implements Serializable {
        /**
         * Полное название улицы, единственное поле класса полностью определяющее экземпляр
         * это поле не может быть null, а длина строки не должна превышать 160 символов
         */
        @JacksonXmlProperty
        private final String street;

        /**
         * Номер версии сериализации нужен, чтоб JVM понимала, что это один и тот же класс на клиенте и на сервере
         */
        @Serial
        private static final long serialVersionUID = 40L;

        /**
         * Конструктор класса
         * @param street значение для поля street
         */
        public Address(String street) {
            if (street.isBlank()) throw new NullPointerException("это поле не может быть пустым");
            if (street.length() > 160) throw new IllegalArgumentException("слишком длинное название. " +
                    "Пожалуйста ограничтесь 160-ью символами");
            this.street = street;
        }

        /**
         * Стандартный конструктор для корректной работы парсера XML
         */
        private Address(){this.street = "костыль";}

        /**
         * Стандартный метод из класса Object для сравнения объектов
         * @param otherObject сравниваемый объект
         * @return истину в случае эквивалентности объектов, ложь в противном случае
         */
        @Override
        public boolean equals(Object otherObject) {
            if (this == otherObject) return true;
            if (otherObject == null || this.getClass() != otherObject.getClass()) return false;
            Address address = (Address) otherObject;
            return Objects.equals(street, address.street);
        }

        /**
         * Стандартный метод из класса Object для вычисления хеш-кода
         * @return хеш-код объекта
         */
        @Override
        public int hashCode() {
            return street.hashCode() * 3;
        }

        /**
         * Стандартный метод для строкового представления объекта
         * @return строку включающую значения всех полей объекта
         */
        @Override
        public String toString() {
            return "Address{" +
                    "street='" + street + '\'' +
                    '}';
        }
    }

    /**
     * Стандартный метод для строкового представления объекта
     * @return строку включающую значения всех полей объекта
     */
    @Override
    public String toString() {
        return "Venue{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", address=" + address +
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
        Venue other = (Venue) otherObject;
        return id == other.id && capacity == other.capacity
                && Objects.equals(name, other.name) && Objects.equals(address, other.address);
    }

    /**
     * Стандартный метод из класса Object для вычисления хеш-кода
     * @return хеш-код объекта
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, capacity, address);
    }
}
