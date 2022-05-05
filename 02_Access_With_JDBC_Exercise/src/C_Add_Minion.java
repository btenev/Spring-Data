import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class C_Add_Minion {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "1234");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        String[] minionTokens = scanner.nextLine().split(" ");
        String minionName = minionTokens[1];
        int minionAge = Integer.parseInt(minionTokens[2]);
        String minionTown = minionTokens[3];

        String sqlTown = "SELECT id FROM towns WHERE name = ?";
        ResultSet dbTownId = getTownOrVillainOrMinion(connection, sqlTown, minionTown);
        int townId = 0;

        if (!dbTownId.next()) {
            String sqlInsertIntoTowns = "INSERT INTO towns(name) VALUES (?)";
            insertItem(connection, sqlInsertIntoTowns, minionTown, minionName);

            dbTownId = getTownOrVillainOrMinion(connection, sqlTown, minionTown);
            dbTownId.next();
            System.out.println("Town " + minionTown + " was added to the database.");
        }

        townId = dbTownId.getInt("id");

        String villainName = scanner.nextLine().split(" ")[1];
        String sqlVillain = "SELECT id FROM villains WHERE name = ?";
        ResultSet dbVillainId = getTownOrVillainOrMinion(connection, sqlVillain, villainName);
        int villainId = 0;

        if (!dbVillainId.next()) {
            String insertIntoVillains = "INSERT INTO villains(name, evilness_factor) VALUES (?,?)";
            insertItem(connection, insertIntoVillains, villainName, "evil");

            dbVillainId = getTownOrVillainOrMinion(connection, sqlVillain, villainName);
            dbVillainId.next();
            System.out.println("Villain " + villainName + " was added to the database.");
        }

        villainId = dbVillainId.getInt("id");

        String sqlInsertIntoMinions = "INSERT INTO minions(name,age,town_id) VALUES (?,?,?)";
        insertItem(connection,sqlInsertIntoMinions, minionName, String.valueOf(minionAge), String.valueOf(townId));

        String sqlMinion = "SELECT id FROM minions WHERE name = ?";
        ResultSet dbMinionId = getTownOrVillainOrMinion(connection, sqlMinion, minionName);
        dbMinionId.next();
        int minionId = dbMinionId.getInt("id");

        String sqlInsertIntoMinionsVillains = "INSERT INTO minions_villains VALUES (?,?)";
        insertItem(connection,sqlInsertIntoMinionsVillains, String.valueOf(minionId),String.valueOf(villainId));

        System.out.println("Successfully added " + minionName + " to be minion of " + villainName);
    }

    public static void insertItem(Connection connection, String sqlInsert, String... parameters) throws SQLException {
        PreparedStatement insertStatement = connection
                .prepareStatement(sqlInsert);

        if (sqlInsert.contains ("minions_villains")) {
            insertStatement.setInt(1,Integer.parseInt(parameters[0]));
            insertStatement.setInt(2,Integer.parseInt(parameters[1]));
        } else {
            insertStatement.setString(1, parameters[0]);
        }

        if (sqlInsert.contains("evilness_factor")) {
            insertStatement.setString(2, parameters[1]);
        }

        if(sqlInsert.contains("town_id")) {
            insertStatement.setInt(2, Integer.parseInt(parameters[1]));
            insertStatement.setInt(3, Integer.parseInt(parameters[2]));
        }

        insertStatement.executeUpdate();
    }

    public static ResultSet getTownOrVillainOrMinion(Connection connection, String sqlQuery, String getToken) throws SQLException {
        PreparedStatement statement = connection
                .prepareStatement(sqlQuery);
        statement.setString(1, getToken);
        return statement.executeQuery();
    }
}