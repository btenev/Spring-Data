package WritingYourOwnDataRetrievalApplication;

import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "1234");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/diablo", properties);

        PreparedStatement statement = connection
                .prepareStatement("SELECT user_name,first_name, last_name, COUNT(users_games.user_id) AS games_played  FROM users"
                                + " JOIN users_games ON users.id = users_games.user_id"
                                + " WHERE user_name = ?"
                                + " GROUP BY users_games.user_id");

        String username = scanner.nextLine();
        statement.setString(1, username);
        ResultSet result = statement.executeQuery();

        if (result.next()) {
            String first_name = result.getString("first_name");
            String last_name = result.getString("last_name");
            Integer countGames = result.getInt("games_played");
            System.out.printf("User: %s%n" +
                    "%s %s has played %d games", username, first_name, last_name, countGames);
        } else {
            System.out.println("No such user exists");

        }
    }
}
