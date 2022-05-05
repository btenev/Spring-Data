import java.sql.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

public class G_Increase_Minions_Age {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "1234");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        int[] minionsIds = Arrays.stream(scanner.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();

        for (int currentId : minionsIds) {
            increaseAgeOfSelectedMinion(connection, currentId);
        }
        printAllMinions(connection);
    }

    private static void printAllMinions(Connection connection) throws SQLException {
        PreparedStatement printStatement = connection.prepareStatement("SELECT name, age FROM minions");
        ResultSet minionsSet = printStatement.executeQuery();
        while (minionsSet.next()) {
            String name = minionsSet.getString("name");
            int age = minionsSet.getInt("age");
            System.out.println(name + " " + age);
        }
    }

    private static void increaseAgeOfSelectedMinion(Connection connection, int id) throws SQLException {
        PreparedStatement statementAge = connection
                .prepareStatement("UPDATE minions SET age = age + 1, name = lower(name) WHERE id = ?");
        statementAge.setInt(1, id);
        statementAge.executeUpdate();
    }
}
