package lt.shgg.database;

public class QueryDealer {
    final String findUser = """
            SELECT * FROM Users
            WHERE login = ?;
            """;

    final String getPassword = """
            SELECT hash FROM Users
            WHERE name = ? ;
            """;

    final String addUser = """
            INSERT INTO Users(login, password)
            VALUES (?, ?)
            """;

    final String addTicket = """
            INSERT INTO Tickets(name, coordinate_x, coordinate_y, creation_date, price, type,
            venue_id, venue_name, capacity, address, author)
            VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id;
            """;

    final String addSpacialTicket = """
            INSERT INTO Tickets(id, name, coordinate_x, coordinate_y, creation_date, price, type,
            venue_id, venue_name, capacity, address, author)
            VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id;
            """;

    final String clearCollection = """
            DELETE FROM Tickets
            WHERE (user_login = ?)
            RETURNING id;
            """;

    final String deleteObject = """
            DELETE FROM Tickets
            WHERE (user_login = ?) AND (id = ?)
            RETURNING id;
            """;

    final String removeLower = """
            DELETE FROM Tickets
            WHERE (user_login = ?) AND (price < ?)
            RETURNING id;
            """;

    final String removeGreater = """
            DELETE FROM Tickets
            WHERE (user_login = ?) AND (price > ?)
            RETURNING id;
            """;

    final String findMaxPrice = """
            SELECT MAX(price)
            FROM Ticket;
            """;

    final String updateObject = """
            UPDATE Tickets SET (name, coordinate_x, coordinate_y, price, type,
            venue_id, venue_name, capacity, address)
            = (?, ?, ?, ?, ?, ?, ?, ?, ?)
            WHERE (user_login = ?) AND (id = ?)
            RETURNING id;
            """;

    final String selectAllObjects = """
            SELECT *
            FROM Tickets;
            """;

    final String deleteAll = """
            DELETE
            FROM Tickets;
            """;
}
