package lt.shgg.database;

import lt.shgg.data.Ticket;
import lt.shgg.data.User;
import lt.shgg.network.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.LinkedHashSet;
import java.util.Set;

public class DatabaseManager {
    private final QueryDealer queryDealer = new QueryDealer();
    private static final Logger DBLogger = LogManager.getLogger("DatabaseLogger");

    public static Connection connect(){
        try{
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
                    "postgres", "rhbc1995");
        } catch (ClassNotFoundException | SQLException e){
            DBLogger.warn("Ошибка при подключении к базе данных");
        }
        return null;
    }
    public Response registration(User user){
        try (Connection connection = connect()){
//            Connection connection = connect();
            PreparedStatement findUser = connection.prepareStatement(queryDealer.findUser);
            findUser.setString(1, user.getLogin());
            ResultSet resultSet = findUser.executeQuery();
            if (resultSet.next()){
                return new Response("Пользователь с таким логином уже существует. Попробуй еще раз ");
            } else {
                PreparedStatement addUser = connection.prepareStatement(queryDealer.addUser);
                addUser.setString(1, user.getLogin());
                addUser.setString(2, PasswordHasher.passwordHash(user.getPassword()));
                addUser.execute();
                DBLogger.info("Регистрация нового пользователя: {}", user.getLogin());
                return new Response("Поздравляю, вы смешарик!");
            }
        } catch (SQLException | NullPointerException e){
            return new Response("Ошибка подключения к базе данных. Попробуй еще раз ");
        }
    }

    public Response authorisation(User user){
        try (Connection connection = connect()){
            PreparedStatement findUser = connection.prepareStatement(queryDealer.findUser);
            findUser.setString(1, user.getLogin());
            ResultSet resultSet = findUser.executeQuery();
            if (resultSet.next()){
                if (PasswordHasher.passwordHash(resultSet.getString("password"))
                        .equals(PasswordHasher.passwordHash(user.getPassword())))
                    return new Response("Вход выполнен успешно!");
                return new Response("Неправильно, попробуй еще раз: ");
            } else {
                return new Response("Пользователь с таким логином не найден. Попробуй еще раз ");
            }
        } catch (SQLException | NullPointerException e){
            return new Response("Ошибка подключения к базе данных. Попробуй еще раз ");
        }
    }

    public boolean updateObject(Ticket ticket, String user){
        try{
            Connection connection = connect();
            PreparedStatement update = connection.prepareStatement(queryDealer.updateObject);
            update.setString(1, ticket.getName());
            update.setFloat(2, ticket.getCoordinates().getX());
            update.setInt(3, ticket.getCoordinates().getY());
            update.setLong(4, ticket.getPrice());
            update.setString(5, ticket.getType().toString());
            update.setLong(6, ticket.getVenue().getId());
            update.setString(7, ticket.getVenue().getName());
            update.setInt(8, ticket.getVenue().getCapacity());
            update.setString(9, ticket.getVenue().getAddress().getStreet());
            update.setString(10, user);
            update.setLong(11, ticket.getId());
            ResultSet resultSet = update.executeQuery();
            return (resultSet.next());
        } catch (SQLException | NullPointerException e){
            DBLogger.warn("Ошибка при подключении/выполнении запроса");
        }
        return false;
    }
    public boolean removeObject(int id, String userLogin){
        try (Connection connection = connect()){
            PreparedStatement remove = connection.prepareStatement(queryDealer.deleteObject);
            remove.setString(1, userLogin);
            remove.setInt(2, id);
            ResultSet resultSet = remove.executeQuery();
            return resultSet.next();
        } catch (SQLException | NullPointerException e) {
            DBLogger.warn("Ошибка при подключении/выполнении запроса");
        }
        return false;
    }
    public long addObject(Ticket ticket){
        try{
            Connection connection = connect();
            PreparedStatement add = connection.prepareStatement(queryDealer.addTicket);
            add.setString(1, ticket.getName());
            add.setFloat(2, ticket.getCoordinates().getX());
            add.setInt(3, ticket.getCoordinates().getY());
            add.setString(4, ticket.getCreationDate().toString());
            add.setLong(5, ticket.getPrice());
            add.setString(6, ticket.getType().toString());
            add.setLong(7, ticket.getVenue().getId());
            add.setString(8, ticket.getVenue().getName());
            add.setInt(9, ticket.getVenue().getCapacity());
            add.setString(10, ticket.getVenue().getAddress().getStreet());
            add.setString(11, ticket.getAuthor());
            ResultSet resultSet = add.executeQuery();
            resultSet.next();
            return resultSet.getLong("id");
        } catch (SQLException | NullPointerException e){
            DBLogger.warn("Ошибка при подключении/выполнении запроса");
        }
        return -1;
    }
    public long addIfMax(Ticket ticket){
        try{
            Connection connection = connect();
            PreparedStatement findMaxPrice = connection.prepareStatement(queryDealer.findMaxPrice);
            ResultSet resultSet = findMaxPrice.executeQuery();
            var maxPrice = 0L;
            if (resultSet.next()){
                maxPrice = resultSet.getLong("price");
            }
            return (ticket.getPrice() > maxPrice) ? addObject(ticket) : -2;
        } catch (SQLException | NullPointerException e){
            DBLogger.warn("Ошибка при подключении/выполнении запроса");
        }
        return -1;
    }

    public Set<Long> clear(String userLogin){
        Set<Long> ids = new LinkedHashSet<>();
        try{
            Connection connection = connect();
            PreparedStatement clear = connection.prepareStatement(queryDealer.clearCollection);
            clear.setString(1, userLogin);
            ResultSet resultSet = clear.executeQuery();
            while (resultSet.next()){
                ids.add(resultSet.getLong("id"));
            }
        } catch (SQLException | NullPointerException e){
            DBLogger.warn("Ошибка при подключении/выполнении запроса");
        }
        return ids;
    }

    public Set<Long> removeLower(String userLogin, long price){
        Set<Long> ids = new LinkedHashSet<>();
        try{
            Connection connection = connect();
            PreparedStatement removeLower = connection.prepareStatement(queryDealer.removeLower);
            removeLower.setString(1, userLogin);
            removeLower.setLong(2, price);
            ResultSet resultSet = removeLower.executeQuery();
            while (resultSet.next()){
                ids.add(resultSet.getLong("id"));
            }
        } catch (SQLException | NullPointerException e){
            DBLogger.warn("Ошибка при подключении/выполнении запроса");
        }
        return ids;
    }

    public Set<Long> removeGreater(String userLogin, long price){
        Set<Long> ids = new LinkedHashSet<>();
        try{
            Connection connection = connect();
            PreparedStatement removeGreater = connection.prepareStatement(queryDealer.removeGreater);
            removeGreater.setString(1, userLogin);
            removeGreater.setLong(2, price);
            ResultSet resultSet = removeGreater.executeQuery();
            while (resultSet.next()){
                ids.add(resultSet.getLong("id"));
            }
        } catch (SQLException | NullPointerException e){
            DBLogger.warn("Ошибка при подключении/выполнении запроса");
        }
        return ids;
    }
}

