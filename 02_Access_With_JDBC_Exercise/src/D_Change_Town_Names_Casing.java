import org.w3c.dom.ls.LSOutput;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class D_Change_Town_Names_Casing {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "1234");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        String country = scanner.nextLine();

        PreparedStatement countryStatement = connection.prepareStatement("SELECT name FROM towns WHERE country = ?");
        countryStatement.setString(1, country);

        ResultSet countrySet = countryStatement.executeQuery();

        List<String> towns = new ArrayList<>();

        while (countrySet.next()) {
            String townName = countrySet.getString("name");
            towns.add(townName.toUpperCase());
        }

        String output = towns.isEmpty()
                ? "No town names were affected."
                : String.format("%d town names were affected.%n", towns.size())
                + "[" + String.join(", ", towns) + "]";

        System.out.println(output);
    }
}
