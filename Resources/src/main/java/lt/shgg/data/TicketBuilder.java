package lt.shgg.data;

import java.time.LocalDate;

/**
 * <h1>Класс строитель для создания экземпляров класса {@link data.Ticket}</h1>
 * о шаблоне строитель можно прочитать в книге Джошуа Блоха "Effective Java"
 */
public class TicketBuilder {
    /**
     * Все поля (кроме static nextId) копии полей из класса {@link data.Ticket}, с теми же ограничениями
     */
    private Long id;
    private String name;
    private Coordinates coordinates;
    private LocalDate creationDate;
    private Long price;
    private Ticket.TicketType type;
    private Venue venue;
    /**
     * Вспомогательная константа для автоматической генерации значения id
     */
    private static Long nextId = 1L;

    /*
      Блок инициализации задающий значения полей creationDate и id
     */
    {
        this.creationDate = LocalDate.now();
        this.id = nextId;
        nextId++;
    }

    /**
     * Метод для задания поля id вручную
     * нужен для реализации команды {@link lt.shgg.commands.Update} обновляющей поля с сохранением id
     * не пользоваться в других ситуациях!!!
     * @param id значение для поля id
     */
    public void withId(Long id) {
        if (id < 0) throw new IllegalArgumentException("id - должно быть положительным числом");
        this.id = id;
    }

    /**
     * Метод для задания поля creationDate вручную
     * нужен для реализации команды {@link lt.shgg.commands.Update} обновляющей поля
     * не пользоваться в других ситуациях!!!
     * @param date значение для поля creationDate
     */
    public void withCreationDate(LocalDate date){
        this.creationDate = date;
    }

    /**
     * Мутатор для задания значения поля name
     * @param name значение для поля name
     */
    public void withName(String name) {
        if (name.isBlank()) throw new NullPointerException("это поле не может быть пустым");
        this.name = name;
    }

    /**
     * Мутатор для задания значения поля coordinates
     * @param coordinates значение для поля coordinates
     */
    public void withCoordinates(Coordinates coordinates) {
        if (coordinates == null) throw new NullPointerException("это поле не может быть пустым");
        this.coordinates = coordinates;
    }

    /**
     * Мутатор для задания значения поля price
     * @param price значение для поля price
     */
    public void withPrice(Long price) {
        if (price == null || price <= 0) throw new IllegalArgumentException("цена должна быть положительной");
        this.price = price;
    }
    /**
     * Мутатор для задания значения поля type
     * @param type значение для поля type
     */
    public void withType(Ticket.TicketType type) {
        if (type == null ) throw new NullPointerException("это поле не может быть пустым");
        this.type = type;
    }

    /**
     * Мутатор для задания значения поля venue
     * @param venue значение для поля venue
     */
    public void withVenue(Venue venue) {
        this.venue = venue;
    }

    /**
     * Метод для дополнительной проверки, что все обязательные поля не пусты
     * @return истину, если все обязательные поля заданы, ложь в противном случае
     */
    public boolean isReadyToBuild(){
        return !(this.name == null
                || this.coordinates == null
                || this.price == null
                || this.type == null);
    }

    /**
     * Метод создающий новый билет вызывая конструктор в классе {@link data.Ticket}
     * @return новенький и полностью определенный билет
     */
    public Ticket build(){
        return new Ticket(id, name, coordinates, creationDate, price, type, venue);
    }

}
