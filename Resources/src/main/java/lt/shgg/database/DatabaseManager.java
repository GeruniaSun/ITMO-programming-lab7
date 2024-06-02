package lt.shgg.database;

import lt.shgg.data.Ticket;
import lt.shgg.data.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.*;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

public class DatabaseManager {
    private final QueryDealer queryDealer = new QueryDealer();
    //private static final Logger logManager.getLogger("DatabaseLogger");
    private final static String propPath = "Resources/src/main/resources/db_local.properties";
    public static Connection connect(){
        try{
            Class.forName("org.postgresql.Driver");
            Properties properties = new Properties();
            properties.load(new FileInputStream(propPath));
            return DriverManager.getConnection(
                    properties.getProperty("address"),
                    properties.getProperty("username"),
                    properties.getProperty("password"));
        } catch (ClassNotFoundException | SQLException e){
            System.out.println("Ошибка при подключении к базе данных");
            System.out.println(e.getMessage());
            //e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Не найден файл конфигурации, программист петух");
            throw new RuntimeException(e);
        }
        return null;
    }

    public String findUser(String login){
        try (Connection connection = connect()){
            PreparedStatement findUser = connection.prepareStatement(queryDealer.findUser);
            findUser.setString(1, login);
            ResultSet resultSet = findUser.executeQuery();
            if (resultSet.next()){
                return resultSet.getString("password");
            } else {
                return "";
            }
        } catch (SQLException | NullPointerException e){
            return "Ошибка подключения к базе данных. Попробуй еще раз ";
        }
    }

    public String addUser(User user){
        try (Connection connection = connect()){
            PreparedStatement addUser = connection.prepareStatement(queryDealer.addUser);
            addUser.setString(1, user.getLogin());
            addUser.setString(2, PasswordHasher.passwordHash(user.getPassword()));
            addUser.execute();
            return "Поздравляем с регистрацией!";
        } catch (SQLException | NullPointerException e){
            return "Ошибка подключения к базе данных. Попробуй еще раз ";
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
            System.out.println("Ошибка при подключении/выполнении запроса");
        }
        return false;
    }
    public boolean removeById(long id, String userLogin){
        try (Connection connection = connect()){
            PreparedStatement remove = connection.prepareStatement(queryDealer.deleteObject);
            remove.setString(1, userLogin);
            remove.setLong(2, id);
            ResultSet resultSet = remove.executeQuery();
            return resultSet.next();
        } catch (SQLException | NullPointerException e) {
            System.out.println("Ошибка при подключении/выполнении запроса");
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
            if (ticket.getVenue() != null) {
                add.setLong(7, ticket.getVenue().getId());
                add.setString(8, ticket.getVenue().getName());
                add.setInt(9, ticket.getVenue().getCapacity());
                if (ticket.getVenue().getAddress() != null){
                    add.setString(10, ticket.getVenue().getAddress().getStreet());
                } else {
                    add.setObject(10, null);
                }
            } else {
                add.setObject(7, null);
                add.setObject(8, null);
                add.setObject(9, null);
                add.setObject(10, null);
            }
            add.setString(11, ticket.getAuthor());
            ResultSet resultSet = add.executeQuery();
            resultSet.next();
            return resultSet.getLong("id");
        } catch (SQLException | NullPointerException e){
            System.out.println("Ошибка при подключении/выполнении запроса");
            System.out.println(e.getMessage());
            System.out.println(e.getClass());
            e.printStackTrace();
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
            System.out.println("Ошибка при подключении/выполнении запроса");
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
            System.out.println("Ошибка при подключении/выполнении запроса");
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
            System.out.println("Ошибка при подключении/выполнении запроса");
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
            System.out.println("Ошибка при подключении/выполнении запроса");
        }
        return ids;
    }
}

