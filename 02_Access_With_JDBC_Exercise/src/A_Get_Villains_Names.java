import java.sql.*;
import java.util.Properties;

public class A_Get_Villains_Names {
    public static void main(String[] args) throws SQLException {

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "1234");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT v.name, count(DISTINCT mv.minion_id) AS minions_count  FROM villains AS v"
                        + " JOIN minions_villains AS mv ON v.id = mv.villain_id"
                        + " GROUP BY v.id"
                        + " HAVING minions_count > 15"
                        + " ORDER BY minions_count DESC");

        ResultSet result = preparedStatement.executeQuery();

        while (result.next()) {
            String dbVillainName = result.getString("name");
            int dbMinionsCount = result.getInt("minions_count");
            System.out.println(dbVillainName + " " + dbMinionsCount);
        }

        connection.close();
    }
}
