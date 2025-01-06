<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Авторизация</title>
</head>
<body>
    <h2>Авторизация</h2>
    <form action="${pageContext.request.contextPath}/auth/login" method="post">
        <input type="text" name="username" placeholder="Имя пользователя" required>
        <input type="password" name="password" placeholder="Пароль" required>
        <button type="submit">Войти</button>
    </form>
    <c:if test="${not empty param.error}">
        <p>Неверные данные для входа!</p>
    </c:if>
</body>
</html>
