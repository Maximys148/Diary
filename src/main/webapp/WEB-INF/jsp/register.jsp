<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Регистрация</title>
</head>
<body>
    <h2>Регистрация</h2>
    <form action="${pageContext.request.contextPath}/start/register" method="post">
        <input type="text" name="username" placeholder="Имя пользователя" required>
        <input type="password" name="password" placeholder="Пароль" required>
        <input type="email" name="email" placeholder="Email" required>
        <button type="submit">Зарегистрироваться</button>
    </form>
</body>
</html>
