# 📔 Diary - Система управления записями

![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.3-6DB33F?logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-4169E1?logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-24.0-2496ED?logo=docker)

Система для создания и управления персональными записями с возможностью тегирования и поиска.

## 🌟 Особенности
- **CRUD операции** для записей и тегов
- **JWT аутентификация** (регистрация/логин)
- **Поиск по содержимому** с пагинацией
- **Логирование** ключевых операций
- **Docker-контейнеризация**

## 🚀 Запуск проекта

### Требования
- JDK 17+
- Docker 24.0+
- PostgreSQL 16

### 1. Сборка и запуск
```bash
# Клонирование репозитория
git clone https://github.com/Maximys148/Diary.git
cd Diary

# Сборка (Maven)
mvn clean package

# Запуск через Docker
docker-compose up -d
