package lt.shgg.data;

/**
 * <h1>Класс строитель для создания экземпляров класса {@link data.Venue}</h1>
 * о шаблоне строитель можно прочитать в книге Джошуа Блоха "Effective Java"
 */
public class VenueBuilder {
    /**
     * Все поля (кроме static nextId) копии полей из класса {@link data.Venue}, с теми же ограничениями
     */
    private final int id;
    private String name;
    private int capacity;
    private Venue.Address address;
    /**
     * Вспомогательная константа для автоматической генерации значения id
     */
    public static int nextId = 1;

    /**
     * Стандартный конструктор без аргументов, задает значения поля id
     */
    public VenueBuilder(){
        this.id = nextId;
        nextId++;
    }

    /**
     * Мутатор для задания значения поля name
     * @param name значение для поля name
     */
    public void withName(String name) {
        if (name.isBlank() | name == "") throw new NullPointerException("это поле не может быть пустым");
        this.name = name;
    }

    /**
     * Мутатор для задания значения поля capacity
     * @param capacity значение для поля capacity
     */
    public void withCapacity(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("вместимость должна быть положительной");
        this.capacity = capacity;
    }

    /**
     * Мутатор для задания значения поля address
     * @param address значение для поля address
     */
    public void withAddress(Venue.Address address) {
        this.address = address;
    }

    /**
     * Метод создающий новый билет вызывая конструктор в классе {@link data.Venue}
     * @return новенькое и полностью определенное место проведения
     */
    public Venue build(){
        return new Venue(id, name, capacity, address);
    }

}
