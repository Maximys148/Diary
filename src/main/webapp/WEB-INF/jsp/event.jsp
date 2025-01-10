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
    </style>
</head>
<body>

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
                            <span>Напомнить за: ${event.leadTime}</span>
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
                        <label for="dateTime">Дата события</label>
                        <input type="date" class="form-control" id="dateTime" name="dateTime" required>
                    </div>
                    <div class="form-group">
                        <label for="reminderFrequency">Частота напоминания</label>
                        <input type="text" class="form-control" id="reminderFrequency" name="reminderFrequency">
                    </div>
                    <div class="form-group">
                        <label for="leadTime">Напомнить за (время)</label>
                        <input type="text" class="form-control" id="leadTime" name="leadTime">
                    </div>
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
