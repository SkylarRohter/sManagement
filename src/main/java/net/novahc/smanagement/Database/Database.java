package net.novahc.smanagement.Database;

public class Database {
    public Database(){
        init();
    }

    private void init() {
        String jdbcUrl = "jdbc:mysql://localhost:3306/your_database_name";
        String username = "your_username";
        String password = "your_password";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement statement = connection.createStatement()) {

            String sql = "SELECT * FROM your_table_name";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                // Process the results
                String columnName = resultSet.getString("column_name");
                // Do something with the retrieved data
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
