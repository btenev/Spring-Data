import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class B_Get_Minion_Names {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "1234");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        PreparedStatement villainStatement = connection.prepareStatement("SELECT name FROM villains WHERE id = ?");

        int villainId = Integer.parseInt(scanner.nextLine());
        villainStatement.setInt(1, villainId);

        ResultSet villainResult = villainStatement.executeQuery();

        if (!villainResult.next()) {
            System.out.println("No villain with ID " + villainId + " exists in the database.");
            return;
        }

        String dbVillainName = villainResult.getString("name");
        System.out.println("Villain: " + dbVillainName);

        PreparedStatement minionStatement = connection.prepareStatement("SELECT m.name, m.age"
                + " FROM villains AS v"
                + " JOIN minions_villains AS mv ON v.id = mv.villain_id"
                + " JOIN minions AS m ON mv.minion_id = m.id"
                + " WHERE v.id = ?");

        minionStatement.setInt(1, villainId);

        ResultSet minionResult = minionStatement.executeQuery();
        int counter = 1;

        while (minionResult.next()) {
            String dbMinionName = minionResult.getString("name");
            int dbAge = minionResult.getInt("age");
            System.out.println(counter + "." + dbMinionName + " " + dbAge);
            counter++;
        }
    }
}
