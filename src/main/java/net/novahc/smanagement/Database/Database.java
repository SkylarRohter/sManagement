package net.novahc.smanagement.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Database {
    private ArrayList<Integer> ids;

    public ArrayList<Integer> getIds() {
        return ids;
    }

    private ArrayList<String> users;
    public ArrayList<String> getUsers() {
        return users;
    }

    private ArrayList<Integer> grades;
    public ArrayList<Integer> getGrades(){
        return grades;
    }

    public Database(){
        ids = new ArrayList<>();
        users = new ArrayList<>();
        grades = new ArrayList<>();
        init();
    }

    private void init() {
        String jdbcUrl = "jdbc:mysql://localhost:3306/smanagement";
        String username = "root";
        String password = "Theyellowapple";


        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement statement = connection.createStatement()) {

            String sql = "SELECT * FROM users";
            ResultSet resultSet = statement.executeQuery(sql);
            ResultSetMetaData metaData = resultSet.getMetaData();

            while(resultSet.next()){
                ids.add(resultSet.getInt("id"));
                users.add(resultSet.getString("name"));
                grades.add(resultSet.getInt("grade"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error connecting to database.");
        }
    }
}
