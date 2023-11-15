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
    private int lastId;
    public void addUser(String name, int grade){
        lastId++;
        getIds().add(lastId);
        getUsers().add(name);
        getGrades().add(grade);
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
        lastId = getIds().get(getIds().size()-1)+1;
    }
    public void insertRecord(String name, int grade){
        String jdbcUrl = "jdbc:mysql://localhost:3306/smanagement";
        String username = "root";
        String password = "Theyellowapple";

        String sql = "INSERT INTO users (id, name, grade) VALUES (?, ?, ?);";
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, lastId);
            preparedStatement.setString(2,name);
            preparedStatement.setInt(3, grade);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Data inserted successfully!");
                addUser(name,grade);
            } else {
                System.out.println("Failed to insert data.");
            }
        } catch (SQLException e) {
            printSQLException(e);
        }

    }
    public void printSQLException(SQLException ex){
        for(Throwable e: ex){
            e.printStackTrace(System.err);
            System.err.println("SQLState: " + ((SQLException) e).getSQLState());
            System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
            System.err.println("Message: " + e.getMessage());
            Throwable t = e.getCause();
            while(t != null){
                System.out.println("Cause: " + t);
                t = t.getCause();
            }
        }
    }
}
