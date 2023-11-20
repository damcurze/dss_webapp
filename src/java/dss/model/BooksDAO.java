package dss.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.tomcat.jdbc.pool.DataSource;


public class BooksDAO {

    private DataSource dataSource;

    public BooksDAO(Connection connection) {
        try {
            Context initialContext = new InitialContext();
            dataSource = (DataSource) initialContext.lookup("java:comp/env/jdbc/book_inf");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public void saveBook(Books book) throws SQLException {
        String query = "INSERT INTO books (author_id, book_label, book_genre) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, book.getAuthorId());
            statement.setString(2, book.getBookLabel());
            statement.setString(3, book.getBookGenre());
            statement.executeUpdate();
        }
    }

    public void updateBook(Books book) throws SQLException {
        String query = "UPDATE books SET author_id=?, book_label=?, book_genre=? WHERE book_id=?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, book.getAuthorId());
            statement.setString(2, book.getBookLabel());
            statement.setString(3, book.getBookGenre());
            statement.setInt(4, book.getBookId());
            statement.executeUpdate();
        }
    }

    public void deleteBook(int bookId) throws SQLException {
        String query = "DELETE FROM books WHERE book_id=?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookId);
            statement.executeUpdate();
        }
    }

    public Books getBookById(int bookId) throws SQLException {
        String query = "SELECT * FROM books WHERE book_id=?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Books book = new Books();
                    book.setBookId(resultSet.getInt("book_id"));
                    book.setAuthorId(resultSet.getInt("author_id"));
                    book.setBookLabel(resultSet.getString("book_label"));
                    book.setBookGenre(resultSet.getString("book_genre"));
                    return book;
                }
            }
        }
        return null;
    }
    
    public List<Books> getAllBooks() throws SQLException {
        List<Books> books = new ArrayList<>();
        String query = "SELECT * FROM books";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Books book = new Books();
                book.setBookId(resultSet.getInt("book_id"));
                book.setAuthorId(resultSet.getInt("author_id"));
                book.setBookLabel(resultSet.getString("book_label"));
                book.setBookGenre(resultSet.getString("book_genre"));
                books.add(book);
            }
        }
        return books;
    }

    public List<Books> searchBooksByAuthor(int authorId) throws SQLException {
        List<Books> books = new ArrayList<>();
        String query = "SELECT * FROM books WHERE author_id=?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, authorId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Books book = new Books();
                    book.setBookId(resultSet.getInt("book_id"));
                    book.setAuthorId(resultSet.getInt("author_id"));
                    book.setBookLabel(resultSet.getString("book_label"));
                    book.setBookGenre(resultSet.getString("book_genre"));
                    books.add(book);
                }
            }
        }
        return books;
    }
}