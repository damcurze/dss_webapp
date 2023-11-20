<%@ page import="java.util.List" %>
<%@ page import="dss.model.Authors" %>
<%@ page import="java.sql.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page errorPage="error.jsp" %>
<html>
<head>
    <title>Управление авторами</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f4f4f4;
        }

        h1 {
            text-align: center;
            color: #333;
        }

        form {
            max-width: 400px;
            margin: 0 auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        label {
            display: block;
            margin-bottom: 8px;
            color: #333;
        }

        input {
            width: 100%;
            padding: 8px;
            margin-bottom: 16px;
            box-sizing: border-box;
        }

        button {
            background-color: #4caf50;
            color: #fff;
            padding: 10px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        table {
            width: 100%;
            margin-top: 20px;
            border-collapse: collapse;
        }

        th, td {
            padding: 10px;
            border: 1px solid #ddd;
            text-align: left;
        }

        th {
            background-color: #4caf50;
            color: #fff;
        }
    </style>
</head>
<body>

    <h1>Управление авторами</h1>

    <h2 align="center">Добавить нового автора</h2>
    <form action="authors/add" method="post" align="center">
        <label for="authorName">Фамилия, имя автора:</label>
        <input type="text" id="authorName" name="authorName" required>
        <br>
        <label for="authorBirth">Дата рождения:</label>
        <input type="date" id="authorBirth" name="authorBirth" required>
        <br>
        <button type="submit">Добавить автора</button>
    </form>
    
    <h2 align="center">Все авторы</h2>
    <table align="center" border="1">
    <thead>
        <tr>
            <th>ID</th>
            <th>Имя</th>
            <th>Дата рождения</th>
            <th>Действия</th> <!-- Новый столбец для кнопки "удалить" -->
        </tr>
    </thead>
    <tbody>
        <c:forEach var="author" items="${authors}">
            <tr>
                <td>${author.id}</td>
                <td>${author.name}</td>
                <td>${author.birth}</td>
                <td>
                    <form action="authors/delete" method="post">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="authorId" value="${author.id}">
                        <button type="submit">Удалить</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </tbody>
    </table>

</body>
</html>
