# Система управления банковскими картами

# BankCards REST API

---

## 🚀 Запуск приложения

1. Клонируем репозиторий:

```bash
git clone https://github.com/Dbatr/Bank_REST.git
cd Bank_REST
````

2. Создаём файл `.env` на основе примера и отредактируем его под свои настройки:

```bash
cp .env.example .env
# Отредактируйте .env с вашими настройками
```

3. Запускаем сервисы через Docker Compose:

```bash
docker compose up -d
```

> После запуска система автоматически наполнится примерными тестовыми данными.

### Примерные тестовые пользователи

| Имя   | Пароль         | Роль  |
| ----- | -------------- | ----- |
| admin | SecurePass123! | admin |
| user  | SecurePass123! | user  |

> Вы можете использовать эти учетные данные для входа и тестирования приложения.

---

## 🛑 Остановка приложения

```bash
docker compose down
```

---

## 🧩 Доступные сервисы после запуска

* Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## ⚙️ Настройки

Файл `.env` содержит основные настройки приложения, включая параметры подключения к PostgreSQL, секретные ключи для JWT и криптографический ключ для шифрования.

Пример:

```env
BANK_DB_URL=jdbc:postgresql://postgres:5432
BANK_DB_NAME=bank_cards
BANK_DB_USER=postgres
BANK_DB_PASSWORD=PostgresPassword
APP_CRYPTO_KEY=3F5t9K8s+X4yZk9vQ2m7H8Jr4vT0w1QzYpR6L1Ue0xA=
JWT_SECRET=BF7FD11ACE545745B7BA1AF98B6F156D127BC7BB544BAB6A4FD74E4FC7
JWT_EXPIRATION=3600000
```

---

## 📖 Документация API

Swagger UI позволяет изучить и протестировать все эндпоинты API.

```text
http://localhost:8080/swagger-ui/index.html
```



