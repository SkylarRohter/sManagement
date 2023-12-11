package net.novahc.smanagement.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Database {
    private ArrayList<Integer> ids;

    // Configuration Fields

    private String username;
    private String password;
    private String url;
    private String tableName;

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

    public ArrayList<Integer> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(ArrayList<Integer> studentIds) {
        this.studentIds = studentIds;
    }

    private ArrayList<Integer> studentIds;

    public void setLastId(int lastId) {
        this.lastId = lastId;
    }

    public int getLastId() {
        return lastId;
    }

    private int lastId;
    public void addUser(String name, int grade, int studentId){
        lastId++;
        getIds().add(lastId);
        getUsers().add(name);
        getGrades().add(grade);
        getStudentIds().add(studentId);
    }
    public void updateName(String newName, int id){
        getUsers().set(id,newName);
    }
    public void updateGrade(int newGrade, int id){
        getGrades().set(id,newGrade);
    }
    public void updateStudentId(int newStudentId, int id){
        getStudentIds().set(id,newStudentId);
    }
    public void removeUser(int key){
        getUsers().remove(key);
        getGrades().remove(key);
        getStudentIds().remove(key);
        System.out.println(ids);
        for(int i = 0; i < ids.size(); i++){
            if(ids.get(i)>key){
                ids.set(i,ids.get(i)-1);
                updateIds(i);
            }
        }
        getIds().remove(key);
        lastId = getIds().size()-1;
        System.out.println(ids);
    }

    public Database(String username, String password, String url, String tableName){
        this.username = username;
        this.password = password;
        this.url = url;
        this.tableName = tableName;

        ids = new ArrayList<>();
        users = new ArrayList<>();
        grades = new ArrayList<>();
        studentIds = new ArrayList<>();
        init();
    }

    private void init() {
        // "jdbc:mysql://localhost:3306/smanagement"
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {

            String sql = "SELECT * FROM "+tableName;
            ResultSet resultSet = statement.executeQuery(sql);
            ResultSetMetaData metaData = resultSet.getMetaData();

            while(resultSet.next()){
                ids.add(resultSet.getInt("id"));
                users.add(resultSet.getString("name"));
                grades.add(resultSet.getInt("grade"));
                studentIds.add(resultSet.getInt("studentid"));
            }

        } catch (Exception e) {
            System.out.println("Error connecting to database.");
        }
        lastId = getIds().size()-1;
        System.out.println(getIds());
        System.out.println(lastId);
    }
    public void insertRecord(String name, int grade, int studentId){
        String sql = "INSERT INTO "+tableName+" (id, name, grade, studentid) VALUES (?, ?, ?, ?);";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, lastId+1);
            preparedStatement.setString(2,name);
            preparedStatement.setInt(3, grade);
            preparedStatement.setInt(4, studentId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Data inserted successfully!");
                addUser(name,grade, studentId);
            } else {
                System.out.println("Failed to insert data.");
            }
        } catch (SQLException e) {
            printSQLException(e);
        }

    }
    public void updateRecord(String columnName, String newValue, boolean isNumeric, int key){
        try (Connection connection  = DriverManager.getConnection(url, username, password)) {
            String sql = "UPDATE " + tableName + " SET " + columnName + " = ? WHERE id = ?";
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                boolean name = false, grade = false, sId = false;
                if(isNumeric){
                    if(newValue.length()<=2){
                        preparedStatement.setInt(1,Integer.parseInt(newValue));
                        grade = true;
                    } else {
                        preparedStatement.setInt(1, Integer.parseInt(newValue));
                        sId = true;
                    }
                } else {
                    preparedStatement.setString(1, newValue);
                    name = true;
                }
                preparedStatement.setInt(2,key);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    if(grade){
                        updateGrade(Integer.parseInt(newValue),key);

                    } else if(sId){
                        updateStudentId(Integer.parseInt(newValue),key);
                    } else if(name){
                        updateName(newValue,key);
                    } else{
                        System.out.println("Error specifying newValue type.");
                    }
                    System.out.println("Data updated successfully!");
                } else {
                    System.out.println("Failed to update data.");
                }
            }
        } catch (SQLException e){
            printSQLException(e);
        }

    }
    public void deleteRecord(int key){
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String deleteQuery = "DELETE FROM " + tableName + " WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setInt(1, key);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    removeUser(key);
                    System.out.println("Row deleted successfully!");
                } else {
                    System.out.println("No rows deleted. Verify the row ID and column names.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * @param lastKey
     */
    private void updateIds(int lastKey){
        try (Connection connection  = DriverManager.getConnection(url, username, password)) {
            String sql = "UPDATE " + tableName + " SET id = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, lastKey - 1);
                preparedStatement.setInt(2, lastKey);
                int rows = preparedStatement.executeUpdate();
                if(rows>0){
                    System.out.println("Yay it worked");
                } else {
                    throw new SQLException();
                }
            }
        } catch (SQLException e){
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
