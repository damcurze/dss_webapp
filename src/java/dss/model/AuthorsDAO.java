package dss.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

public class AuthorsDAO {

    private BasicDataSource dataSource;

    public AuthorsDAO() {
        try {
            Context initialContext = new InitialContext();
            dataSource = (BasicDataSource) initialContext.lookup("java:comp/env/jdbc/books_inf");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
    
    public void addAuthor(Authors author) throws SQLException {
        String sql = "INSERT INTO authors (author_name, author_birth) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, author.getName());
            preparedStatement.setDate(2, author.getBirth());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    author.setId(generatedKeys.getInt(1));
                }
            }
        }
    }
    
    public void updateAuthor(Authors author) throws SQLException {
        String sql = "UPDATE authors SET author_name=?, author_birth=? WHERE id=?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, author.getName());
            preparedStatement.setDate(2, author.getBirth());
            preparedStatement.setInt(3, author.getId());
            preparedStatement.executeUpdate();
        }
    }

    public void deleteAuthor(int authorId) throws SQLException {
        String sql = "DELETE FROM authors WHERE id=?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, authorId);
            preparedStatement.executeUpdate();
        }
    }
    
    public Authors getAuthorById(int authorId) throws SQLException {
        String sql = "SELECT * FROM authors WHERE id=?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, authorId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToAuthor(resultSet);
                }
            }
        }
        return null;
    }     

    public List<Authors> getAllAuthors() throws SQLException {
        List<Authors> authors = new ArrayList<>();
        String sql = "SELECT * FROM authors";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                authors.add(mapResultSetToAuthor(resultSet));
            }
        }
        return authors;
    }

    public List<Authors> findAuthorsByCriteria(String criteria) throws SQLException {
        List<Authors> authors = new ArrayList<>();
        String sql = "SELECT * FROM authors WHERE author_name LIKE ?";
    
        try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, "%" + criteria + "%");
        
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                authors.add(mapResultSetToAuthor(resultSet));
            }
        }
        }
        return authors;
    }
        
        

    private Authors mapResultSetToAuthor(ResultSet resultSet) throws SQLException {
        Authors author = new Authors();
        author.setId(resultSet.getInt("id"));
        author.setName(resultSet.getString("author_name"));
        author.setBirth(resultSet.getDate("author_birth"));
        return author;
    }

    public void close() throws SQLException {
        Connection connection = dataSource.getConnection();
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
