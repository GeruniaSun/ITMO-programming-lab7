package lt.shgg.app;

import lt.shgg.data.Ticket;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;

/**
 * <h1>Класс хранилища</h1>
 * record для хранения коллекции, которой манипулирует приложение
 */
public final class Storage {
    private final Collection<Ticket> collection;

    public Storage(Collection<Ticket> collection) {
        this.collection = collection;
    }

    public Storage() {
        this.collection = new LinkedHashSet<Ticket>();
    }

    public Collection<Ticket> collection() {
        return collection;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Storage) obj;
        return Objects.equals(this.collection, that.collection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collection);
    }

    @Override
    public String toString() {
        return "Storage[" +
                "collection=" + collection + ']';
    }

}
