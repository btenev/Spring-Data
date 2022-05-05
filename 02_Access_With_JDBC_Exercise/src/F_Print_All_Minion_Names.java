import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class F_Print_All_Minion_Names {
    public static void main(String[] args) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "1234");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        PreparedStatement minionsStatement = connection.prepareStatement("SELECT name FROM minions");
        ResultSet minionsNameSet = minionsStatement.executeQuery();

        List<String> nameList = new ArrayList<>();

        while (minionsNameSet.next()) {
            String name = minionsNameSet.getString("name");
            nameList.add(name);
        }

        for (int i = 0; i < nameList.size() / 2; i++) {
            String firstName = nameList.get(i);
            String lastName = nameList.get(nameList.size() - 1 - i);
            System.out.println(firstName + System.lineSeparator()  + lastName);
        }

        if (nameList.size() % 2 != 0) {
            System.out.println(nameList.get(nameList.size() / 2));
        }
    }
}
