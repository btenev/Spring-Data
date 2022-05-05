import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class E_Remove_Villain {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        Properties properties = new Properties();
        properties.setProperty("user", "root" );
        properties.setProperty("password", "1234");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        int villainId = Integer.parseInt(scanner.nextLine());

        connection.setAutoCommit(false);

        ResultSet villainSet = selectVillain(connection, villainId);

        if (!villainSet.next()) {
            System.out.print("No such villain was found");
            return;
        }

        String villainName = villainSet.getString("name");

        int countMinions = countFreedMinions(connection, villainId);

        try {
            String sqlQueryMinionsAndVillains = "DELETE FROM minions_villains WHERE villain_id = ?";
            deleteVillain(connection, sqlQueryMinionsAndVillains, villainId);

            String sqlQueryDeleteVillain = "DELETE FROM villains WHERE id = ?";
            deleteVillain(connection, sqlQueryDeleteVillain, villainId);

            connection.commit();

            System.out.println(villainName + " was deleted");
            System.out.println(countMinions + " minions released");
        } catch (SQLException e) {
            connection.rollback();
        }

    }

    private static int countFreedMinions(Connection connection, int villainId) throws SQLException {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("SELECT DISTINCT count(minion_id) AS minion_count FROM minions_villains WHERE villain_id = ?");
            preparedStatement.setInt(1, villainId);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt("minion_count");
    }

    private static void deleteVillain(Connection connection, String sqlQuery, int villainId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setInt(1, villainId);
        preparedStatement.executeUpdate();
    }

    public static ResultSet selectVillain (Connection connection, int villainId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT name FROM villains WHERE id = ?");
        preparedStatement.setInt(1, villainId);
        return preparedStatement.executeQuery();
    }
}
