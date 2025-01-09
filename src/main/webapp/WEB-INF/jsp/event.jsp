<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>События</title>
</head>
<body>
    <h2>Твои события</h2>
    <form action="${pageContext.request.contextPath}/main/event" method="post">
        <input type="text" name="nickName" placeholder="Никнейм" required>
        <input type="text" name="firstName" placeholder="Имя" required>
        <input type="text" name="lastName" placeholder="Фамилия" required>
        <input type="text" name="middleName" placeholder="Отчество" required>
        <input type="password" name="password" placeholder="Пароль" required>
        <button type="submit">Зарегистрироваться</button>
    </form>
</body>
</html>
