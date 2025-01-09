<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Главная страница</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link href='https://unpkg.com/fullcalendar@5.10.2/main.css' rel='stylesheet' />
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
        .free-day {
            background-color: green;
            color: white;
        }
        .busy-day {
            background-color: red;
            color: white;
        }
    </style>
</head>
<body>

<div class="container layout">
    <div class="sidebar bg-light p-3 border-right">
        <h4>Функции сайта</h4>
        <ul class="list-unstyled">
            <li><a href="/main/email">Проверка почты</a></li>
            <li><a href="/main/profile">Профиль</a></li>
            <li><a href="/main/event">Свои события</a></li>
        </ul>
    </div>

    <div class="content p-3">
        <h2>Календарь</h2>
        <div id="calendar"></div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.0.7/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script src='https://unpkg.com/fullcalendar@5.10.2/main.js'></script>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        var calendarEl = document.getElementById('calendar');
        var calendar = new FullCalendar.Calendar(calendarEl, {
            initialView: 'dayGridMonth',
            events: [
                // Пример: события, полученные от бизнес логики и базы данных
                // Здесь вы можете динамически генерировать массив событий на основе данных пользователя
                { title: 'Занято', date: '2023-10-01', className: 'busy-day' },
                { title: 'Свободно', date: '2023-10-02', className: 'free-day' },
                // Добавьте более событий
            ]
        });
        calendar.render();
    });
</script>

</body>
</html>
