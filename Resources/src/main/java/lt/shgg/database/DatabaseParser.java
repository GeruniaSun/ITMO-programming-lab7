package lt.shgg.database;

import lt.shgg.data.Coordinates;
import lt.shgg.data.Ticket;
import lt.shgg.data.TicketBuilder;
import lt.shgg.data.Venue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class DatabaseParser {
    private final QueryDealer queryDealer = new QueryDealer();
    private final Connection connection = DatabaseManager.connect();
    private static final Logger ParseLogger = LogManager.getLogger("ParseLogger");


    public void save(Collection<Ticket> data) {
        try{
            PreparedStatement deleteAll = connection.prepareStatement(queryDealer.deleteAll);
            deleteAll.execute();
            data.forEach(ticket -> {
                PreparedStatement add = null;
                try {
                add = connection.prepareStatement(queryDealer.addSpacialTicket);
                add.setLong(1, ticket.getId());
                add.setString(2, ticket.getName());
                add.setFloat(3, ticket.getCoordinates().getX());
                add.setInt(4, ticket.getCoordinates().getY());
                add.setString(5, ticket.getCreationDate().toString());
                add.setLong(6, ticket.getPrice());
                add.setString(7, ticket.getType().toString());
                add.setLong(8, ticket.getVenue().getId());
                add.setString(9, ticket.getVenue().getName());
                add.setInt(10, ticket.getVenue().getCapacity());
                add.setString(11, ticket.getVenue().getAddress().getStreet());
                add.setString(12, ticket.getAuthor());
                add.executeQuery();
                } catch (SQLException e) {
                    throw new NullPointerException();
                }
            });
            ParseLogger.info("Коллекция сохранена в базу данных.");
        } catch (SQLException | NullPointerException e){
            ParseLogger.warn("Ошибка при подключении к базе данных. Колекция не сохранена.");
        }
    }

    public Set<Ticket> load() {
        Set<Ticket> tickets = new LinkedHashSet<>();
        try{
            PreparedStatement selectAll = connection.prepareStatement(queryDealer.selectAllObjects);
            ResultSet result = selectAll.executeQuery();
            var builder = new TicketBuilder();
            while (result.next()){
                long id = result.getLong("id");
                String name = result.getString("name");
                float x = result.getFloat("coordinate_x");
                int y = result.getInt("coordinate_y");
                LocalDate creationDate = LocalDate.parse(result.getString("creation_date"));
                long price = result.getLong("price");
                Ticket.TicketType type = Ticket.TicketType.valueOf(result.getString("type"));
                int venueId = result.getInt("venue_id");
                String venueName = result.getString("venue_name");
                int capacity = result.getInt("capacity");
                String address = result.getString("address");
                String author = result.getString("author");
                Venue venue = null;
                venue = new Venue(venueId, venueName, capacity,
                            address == null ? null : new Venue.Address(address));
                Ticket ticket = new Ticket(id, name, new Coordinates(x, y), creationDate, price, type,
                        venue, author);
                tickets.add(ticket);
            }
        } catch (SQLException | NullPointerException e){
            ParseLogger.warn("Ошибка при подключении или чтении данных из базы данных. Создана пустая коллекция");
        }
        return tickets;
    }
}
