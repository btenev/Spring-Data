import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class H_Increase_Age_Stored_Procedure {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        int minionId = Integer.parseInt(scanner.nextLine());

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "1234");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        CallableStatement callableStatement = connection.prepareCall("{call usp_get_older(?)}");
        callableStatement.setInt(1, minionId);
        callableStatement.executeQuery();

        printMinionNameAndAge(connection, minionId);

    }

    private static void printMinionNameAndAge(Connection connection, int minionId) throws SQLException {
        PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT name, age FROM minions WHERE id = ?");
        preparedStatement.setInt(1, minionId);
        ResultSet minionSet = preparedStatement.executeQuery();

        minionSet.next();
        String minionName = minionSet.getString("name");
        int minionAge = minionSet.getInt("age");

        System.out.println(minionName + " " + minionAge);
    }
}
