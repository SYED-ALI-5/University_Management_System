package DataBase;

import Classes.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Date;

public class DBConnection {
    private static PreparedStatement preparedStatement;
//
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ums";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "H@mz@t@h1r";

    public DBConnection() throws SQLException {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    static ResultSet resultSet;
    static Connection connection;

    static {
        try {
            connection = getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> String UniqueId(String table, T obj) throws SQLException {
        try {
            Date currentDate = new Date();
            String id = null;

            if (obj instanceof Student) {
                if (currentDate.getMonth() + 1 <= 6) {
                    id = "FA";
                } else {
                    id = "SP";
                }
                id = id + (String.valueOf(currentDate.getYear()).substring(((String.valueOf(currentDate.getYear())).length() - 2)) + "-B");
                String query = "Select Id from Department where Name = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, ((Student) obj).getDepartment());
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String depCode = resultSet.getString("Id");
                    id = id + depCode + "-";
                }
            } else if (obj instanceof Faculty) {
                String query = "Select Id from Department where Name = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, ((Faculty) obj).getDepartment());
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String depCode = resultSet.getString("Id");
                    id = depCode + "-LHR-";
                }
            } else if (obj instanceof Staff) {
                id = ((Staff) obj).getSpeciality();
                id = id + "-CUI-";
            }
            String SELECT_MAX_ID_SQL = "SELECT MAX(CAST(SUBSTRING(Id, LENGTH('" + id + "')+1) AS SIGNED)) " +
                    "FROM " + table +
                    " WHERE Id LIKE '" + id + "%'";
            preparedStatement = connection.prepareStatement(SELECT_MAX_ID_SQL);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int maxIdSuffix = resultSet.getInt(1);
                if (maxIdSuffix < 0) {
                    maxIdSuffix = 0;
                }
                // Increment the maxIdSuffix by 1 and return
                return (id + (String.format("%03d", (maxIdSuffix + 1))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static <T> void AddDataToSQL(T obj) throws FileNotFoundException, SQLException {
        Connection connection = getConnection();
        String insertDataSQL;
        try {
            if (obj instanceof Student) {
                String s = "CURRENTLY_STUDDING";
                String p = "123";
                String id = UniqueId("Student", obj);
                File imageFile = new File(((Student) obj).getImg());
                FileInputStream fis = new FileInputStream(imageFile);
                insertDataSQL = "INSERT INTO Student (Id, Name, Father_Name, CNIC, Address, Age, Gender, DOB, Phone_Number, Department, Semester, Courses, Fee, Image, Status, Password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
                preparedStatement = connection.prepareStatement(insertDataSQL);
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, ((Student) obj).getName());
                preparedStatement.setString(3, ((Student) obj).getFatherName());
                preparedStatement.setString(4, ((Student) obj).getCNIC());
                preparedStatement.setString(5, ((Student) obj).getAddress());
                preparedStatement.setInt(6, ((Student) obj).getAge());
                preparedStatement.setString(7, ((Student) obj).getGender());
                preparedStatement.setString(8, ((Student) obj).getDOB());
                preparedStatement.setString(9, ((Student) obj).getPhnNum());
                preparedStatement.setString(10, ((Student) obj).getDepartment());
                preparedStatement.setInt(11, ((Student) obj).getSemester());
                preparedStatement.setString(12, " ");
                preparedStatement.setInt(13, ((Student) obj).getFee());
                preparedStatement.setBinaryStream(14, fis, (int) imageFile.length());
                preparedStatement.setString(15, s);
                preparedStatement.setString(16, p);
            } else if (obj instanceof Faculty) {
                String status = "ON_DUTY";
                int salary = 0;
                String query = "Select Salary from BPS where Scales = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, ((Faculty) obj).getScale());
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    salary = resultSet.getInt("Salary");
                }
                File imageFile = new File(((Faculty) obj).getImg());
                FileInputStream fis = new FileInputStream(imageFile);
                String id = UniqueId("Faculty", obj);
                insertDataSQL = "INSERT INTO Faculty (Id, Name, Father_Name, CNIC, Address, Age, Gender, DOB, Phone_Number," +
                        " Department, Specialization, Scale, Image, Status, Salary, Type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                // Establish a connection
                preparedStatement = connection.prepareStatement(insertDataSQL);
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, ((Faculty) obj).getName());
                preparedStatement.setString(3, ((Faculty) obj).getFatherName());
                preparedStatement.setString(4, ((Faculty) obj).getCNIC());
                preparedStatement.setString(5, ((Faculty) obj).getAddress());
                preparedStatement.setInt(6, ((Faculty) obj).getAge());
                preparedStatement.setString(7, ((Faculty) obj).getGender());
                preparedStatement.setString(8, ((Faculty) obj).getDOB());
                preparedStatement.setString(9, ((Faculty) obj).getPhnNum());
                preparedStatement.setString(10, ((Faculty) obj).getDepartment());
                preparedStatement.setString(11, ((Faculty) obj).getSpecialization());
                preparedStatement.setString(12, ((Faculty) obj).getScale());
                preparedStatement.setBinaryStream(13, fis, (int) imageFile.length());
                preparedStatement.setString(14, status);
                preparedStatement.setInt(15, salary);
                preparedStatement.setString(16, ((Faculty) obj).getType().name());
            } else if (obj instanceof Staff) {
                int salary = 0;
                String query = "Select Salary from Specialities where Speciality = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, ((Staff) obj).getSpeciality());
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    salary = resultSet.getInt("Salary");
                }
                File imageFile = new File(((Staff) obj).getImg());
                FileInputStream fis = new FileInputStream(imageFile);
                String id = UniqueId("Staff", obj);
                insertDataSQL = "INSERT INTO Staff (Id, Name, Father_Name, CNIC, Address, Age, Gender, DOB, Phone_Number," +
                        " Speciality, Image, Salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                // Establish a connection
                preparedStatement = connection.prepareStatement(insertDataSQL);
                // Set values for the parameters
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, ((Staff) obj).getName());
                preparedStatement.setString(3, ((Staff) obj).getFatherName());
                preparedStatement.setString(4, ((Staff) obj).getCNIC());
                preparedStatement.setInt(5, ((Staff) obj).getAge());
                preparedStatement.setString(6, ((Staff) obj).getGender());
                preparedStatement.setString(7, ((Staff) obj).getAddress());
                preparedStatement.setString(8, ((Staff) obj).getDOB());
                preparedStatement.setString(9, ((Staff) obj).getPhnNum());
                preparedStatement.setString(10, ((Staff) obj).getSpeciality());
                preparedStatement.setBinaryStream(13, fis, (int) imageFile.length());
                preparedStatement.setInt(12, salary);
            }else if (obj instanceof Course) {
                insertDataSQL = "INSERT INTO Courses (Id, Title, Credit_Hours, Department, Level) VALUES (?, ?, ?, ?, ?)";
                // Establish a connection
                preparedStatement = connection.prepareStatement(insertDataSQL);
                // Set values for the parameters
                preparedStatement.setString(2, ((Course) obj).getID());
                preparedStatement.setString(1, ((Course) obj).getTitle());
                preparedStatement.setInt(3, ((Course) obj).getCreditHr());
                preparedStatement.setString(1, ((Course) obj).getDepartment());
                preparedStatement.setString(1, ((Course) obj).getLevel());
            } else if (obj instanceof Department) {
                insertDataSQL = "INSERT INTO Department (Id, Name, HOD, DCO, Fee) VALUES (?, ?, ?, ?, ?)";
                // Establish a connection
                preparedStatement = connection.prepareStatement(insertDataSQL);
                // Set values for the parameters
                preparedStatement.setString(1, ((Department) obj).getID());
                preparedStatement.setString(2, ((Department) obj).getName());
                preparedStatement.setString(3, ((Department) obj).getHOD());
                preparedStatement.setString(4, ((Department) obj).getDCO());
                preparedStatement.setDouble(5, ((Department) obj).getFee());
            }

            preparedStatement.executeUpdate();
        } catch (SQLException var7) {
            System.err.println("Error inserting data into the table!");
            var7.printStackTrace();
        }

    }
}

