<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>События</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        .sidebar {
            width: 250px;
        }
        .content {
            flex: 1;
        }
        .layout {
            display: flex;
        }
        .event-title {
            font-weight: bold;
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
        .button:hover {
            background-color: #0056b3; /* Цвет кнопки при наведении */
        }
</style>
</head>
<body>
<div>
    <a href="${pageContext.request.contextPath}/main/main" class="button">Вернуться на главную</a>
</div>

<div class="container layout">
    <div class="content p-3">
        <h2>Мои события</h2>

        <button class="btn btn-primary mb-3" data-toggle="modal" data-target="#createEventModal">
            Создать событие
        </button>

        <div class="event-list">
            <c:choose>
                <c:when test="${not empty events}">
                    <c:forEach var="event" items="${events}">
                        <div class="border rounded p-2 mb-2">
                            <span class="event-title">${event.name}</span><br>
                            <span>Описание: ${event.data}</span><br>
                            <span>Дата: ${event.dateTime}</span><br>
                            <span>Частота напоминания: ${event.reminderFrequency}</span><br>
                            <span>Напомнить за:
                                <c:set var="leadTime" value="${event.leadTime}" />
                                <c:set var="unitTime" value="${event.unitTime}" />

                                <c:choose>
                                    <c:when test="${unitTime == 'DAY'}">
                                        ${leadTime}
                                        <c:choose>
                                            <c:when test="${leadTime % 10 == 1 && leadTime % 100 != 11}">день</c:when>
                                            <c:when test="${leadTime % 10 >= 2 && leadTime % 10 <= 4 && (leadTime % 100 < 10 || leadTime % 100 >= 20)}">дня</c:when>
                                            <c:otherwise>дней</c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:when test="${unitTime == 'HOUR'}">
                                        ${leadTime}
                                        <c:choose>
                                            <c:when test="${leadTime % 10 == 1 && leadTime % 100 != 11}">час</c:when>
                                            <c:when test="${leadTime % 10 >= 2 && leadTime % 10 <= 4 && (leadTime % 100 < 10 || leadTime % 100 >= 20)}">часа</c:when>
                                            <c:otherwise>часов</c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:when test="${unitTime == 'MINUTE'}">
                                        ${leadTime}
                                        <c:choose>
                                            <c:when test="${leadTime % 10 == 1 && leadTime % 100 != 11}">минута</c:when>
                                            <c:when test="${leadTime % 10 >= 2 && leadTime % 10 <= 4 && (leadTime % 100 < 10 || leadTime % 100 >= 20)}">минуты</c:when>
                                            <c:otherwise>минут</c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:when test="${unitTime == 'WEEK'}">
                                        ${leadTime}
                                        недель
                                    </c:when>
                                    <c:when test="${unitTime == 'MONTH'}">
                                        ${leadTime}
                                        месяцев
                                    </c:when>
                                    <c:otherwise>
                                        ${leadTime} ${unitTime}
                                    </c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p>У вас пока нет событий. Создайте первое событие!</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<!-- Модальное окно для создания события -->
<div class="modal fade" id="createEventModal" tabindex="-1" role="dialog" aria-labelledby="createEventModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="createEventModalLabel">Создать событие</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form action="/main/event" method="post">
                    <div class="form-group">
                        <label for="name">Название события</label>
                        <input type="text" class="form-control" id="name" name="name" required>
                    </div>
                    <div class="form-group">
                        <label for="data">Описание события</label>
                        <textarea class="form-control" id="data" name="data" rows="5" required></textarea>
                    </div>
                    <div class="form-group">
                        <label for="dateTime">Дата и время события</label>
                        <input type="datetime-local" class="form-control" id="dateTime" name="dateTime" required>
                    </div>
                    <div class="form-group">
                        <label for="reminderFrequency">Сколько раз напомнить</label>
                        <input type="text" class="form-control" id="reminderFrequency" name="reminderFrequency">
                    </div>
                    <div class="form-group">
                        <label for="leadTime">За какое время до наступления события напомнить</label>
                        <input type="text" class="form-control" id="leadTime" name="leadTime" required>
                    </div>
                    <label for="unitTime">Единица измерения:</label>
                    <select id="unitTime" name="unitTime" class="form-control">
                        <option value="MINUTE">Минуты</option>
                        <option value="HOUR">Часы</option>
                        <option value="DAY">Дни</option>
                        <option value="WEEK">Недели</option>
                        <option value="MONTH">Месяцы</option>
                    </select>
                    <button type="submit" class="btn btn-primary">Создать событие</button>
                </form>
            </div>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.0.7/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

</body>
</html>