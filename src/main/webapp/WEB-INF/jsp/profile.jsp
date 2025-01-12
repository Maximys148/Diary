<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Профиль пользователя</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
        }
        .container {
            max-width: 600px;
            margin: auto;
            background: #F5F5F5;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        h2 {
            color: #333;
        }
        .email-input {
            margin: 20px 0;
        }
        .email-input input {
            width: calc(100% - 20px);
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        .email-input button {
            padding: 10px;
            background-color: blue;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        .email-input button:hover {
            background-color: #4cae4c;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Ваш профиль</h2>
        <h3>Никнейм: ${nickName}</h3>
        <h3>Имя: ${firstName}</h3>
        <h3>Фамилия: ${lastName}</h3>
        <h3>Отчество: ${middleName}</h3>

        <!-- Форма для добавления электронной почты -->
        <div class="email-input">
            <form action="${pageContext.request.contextPath}/main/profile" method="post">
                <label for="email">Добавить Email:</label>
                <input type="email" id="email" name="email" required placeholder="Введите ваш Email">
                <button type="submit">Добавить</button>
            </form>
        </div>
    </div>
</body>
</html>