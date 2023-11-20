package servlet;

import dss.model.*;
import java.sql.Date;
import java.sql.SQLException;
import java.io.IOException;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/authors/*")
public class authors extends HttpServlet {
    private AuthorsDAO authorsDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        authorsDAO = new AuthorsDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Authors> authors = authorsDAO.getAllAuthors();
            
            request.setAttribute("authors", authors);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/authors.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("exceptionStackTrace", e.getStackTrace());
            request.setAttribute("exception", e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {        
        String pathInfo = request.getPathInfo();

        switch (pathInfo) {
            case "/add" -> {
                String authorName = request.getParameter("authorName");
                String authorBirth = request.getParameter("authorBirth");
                Date birthDate = java.sql.Date.valueOf(authorBirth);
                Authors newAuthor = new Authors();
                newAuthor.setName(authorName);
                newAuthor.setBirth(birthDate);
                try {
                    authorsDAO.addAuthor(newAuthor);
                } catch(SQLException e) {
                    e.printStackTrace();
                    request.setAttribute("exception", e);
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                }
                response.sendRedirect(request.getContextPath() + "/authors");
                }
            case "/delete" -> {
                String action = request.getParameter("action");
                if ("delete".equals(action)) {
                    int authorId = Integer.parseInt(request.getParameter("authorId"));
                    try {
                        authorsDAO.deleteAuthor(authorId);
                    } catch(SQLException e) {
                        e.printStackTrace();
                        request.setAttribute("exception", e);
                        request.getRequestDispatcher("error.jsp").forward(request, response);
                    }
                    response.sendRedirect(request.getContextPath() + "/authors");
                }
            }
            default -> {
                response.sendRedirect(request.getContextPath() + "/error.jsp");
                return;
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
    
    @Override
    public void destroy() {
        super.destroy();
        try {
            authorsDAO.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void doFilter(HttpServletRequest request,
                     HttpServletResponse response,
                     FilterChain chain) throws ServletException, IOException, IOException {
    request.setCharacterEncoding("UTF-8");
    chain.doFilter(request, response);
}

}
