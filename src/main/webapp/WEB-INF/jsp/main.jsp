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
        .notification-bell {
            position: relative;
            cursor: pointer;
        }
        .notification-bell .badge {
            position: absolute;
            top: 0;
            right: 0;
            transform: translate(50%, -50%);
            background-color: red;
            color: white;
            border-radius: 50%;
        }
    </style>
</head>
<body>

<div class="container layout">
    <div class="sidebar bg-light p-3 border-right">
        <h4>Функции сайта</h4>
        <ul class="list-unstyled">
            <li><a href="/main/email">Почта</a></li>
            <li><a href="/main/profile">Профиль</a></li>
            <li><a href="/main/event">Мои события</a></li>
        </ul>
    </div>

    <div class="content p-3">
            <h2>Календарь</h2>
            <div id="calendar"></div>

            <!-- Иконка колокольчика -->
            <div class="notification-bell" data-toggle="modal" data-target="#notificationModal">
                <i class="fas fa-bell" style="font-size: 24px;"></i>
                <span class="badge" id="notificationCount">0</span>
            </div>

            <!-- Модальное окно для уведомлений -->
            <div class="modal fade" id="notificationModal" tabindex="-1" aria-labelledby="notificationModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="notificationModalLabel">Уведомления</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body" id="notificationList">
                            <!-- Проверяем, есть ли уведомления -->
                            <c:forEach var="notification" items="${notifications}">
                               <c:if test="${!notification.read}">
                                   <li class="list-group-item font-weight-bold">
                                       осталось ${notification.alertTime} до события ${notification.eventName}
                                   </li>
                               </c:if>
                            </c:forEach>
                            <c:if test="${empty notifications}">
                                <p>Нет новых уведомлений.</p>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Кнопка для открытия модального окна -->
            <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#notificationModal">
                Показать уведомления
            </button>
        </div>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.0.7/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script src='https://unpkg.com/fullcalendar@5.10.2/main.js'></script>
<script src="https://kit.fontawesome.com/a076d05399.js"></script>


<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Преобразуйте события в формат JSON
        var events = ${events}; // Здесь events уже в формате JSON

        var calendarEl = document.getElementById('calendar');
        var calendar = new FullCalendar.Calendar(calendarEl, {
            initialView: 'dayGridMonth',
            events: function(fetchInfo, successCallback) {
                var allEvents = [];

                // Добавьте занятости из объекта events
                events.forEach(event => {

                    allEvents.push({ title: event.name, date: event.dateTime.split('T')[0], className: 'busy-day' });
                });

                var startDate = fetchInfo.start;
                var endDate = fetchInfo.end;
                var date = startDate;

                while (date < endDate) {
                    var dateString = date.toISOString().split('T')[0];
                    if (!allEvents.some(event => event.date === dateString)) {
                        allEvents.push({ title: 'Свободно', date: dateString, className: 'free-day' });
                    }
                    date.setDate(date.getDate() + 1);
                }
                successCallback(allEvents);
            }
        });
        calendar.render();
    });
</script>

</body>
</html>
