<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Почта</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
            color: #333;
            margin: 20px;
        }

        h1 {
            text-align: center;
            color: #007bff;
        }

        form {
            background: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            margin-bottom: 30px;
        }

        select, textarea, input[type="text"], input[type="submit"] {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        input[type="submit"] {
            background: #007bff;
            color: white;
            border: none;
            cursor: pointer;
            transition: background 0.3s;
        }

        input[type="submit"]:hover {
            background: #0056b3;
        }

        ul {
            list-style: none;
            padding: 0;
        }

        li {
            padding: 10px;
            background: #e9ecef;
            margin-bottom: 5px;
            border-radius: 4px;
        }

        .message-input {
            margin-top: 20px;
        }
        .button {
            display: inline-block;
            padding: 10px 20px;
            font-size: 16px;
            color: white;
            background-color: #007BFF; /* Цвет кнопки */
            border: none;
            border-radius: 5px;
            text-decoration: none; /* Без подчеркивания */
        }
    </style>
</head>
<body>
<div>
    <a href="${pageContext.request.contextPath}/main/main" class="button">Вернуться на главную</a>
</div>
    <h1>Выберите почтовый ящик</h1>

    <form action="${pageContext.request.contextPath}/main/email" method="get">
        <select name="selectedEmail">
            <c:forEach var="email" items="${emails}">
                <option value="${email.address}">
                    ${email.address}
                    <p>Непрочитанные сообщения: ${unreadCounts[email.address]}</p>
                </option>
            </c:forEach>
        </select>
        <input type="submit" value="Показать сообщения">
    </form>

    <c:if test="${not empty messages}">
        <h2>Сообщения для ${userEmail}</h2>
        <ul>
            <c:forEach var="message" items="${messages}">
                <li>${message.content} - От: ${message.sender.address}</li>
            </c:forEach>
        </ul>
    </c:if>

    <c:if test="${not empty userEmail}">
        <h2 class="message-input">Написать сообщение</h2>
        <form action="${pageContext.request.contextPath}/main/email" method="post">
            <input type="text" name="recipientEmails" placeholder="Введите адрес(а) получателя(-ей)" required>
            <input type="hidden" name="senderEmail" value="${userEmail}"> <!-- Указываем email отправителя -->
            <input type="hidden" name="userEmail" value="${userEmail}">
            <textarea name="content" rows="4" cols="50" placeholder="Введите ваше сообщение" required></textarea><br>
            <input type="submit" value="Отправить">
        </form>
    </c:if>
</body>
</html>