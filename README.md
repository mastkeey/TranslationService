# Translation-Service

Веб-приложение переводчик, предназначенное для перевода с использованием Yandex Translate API.

### 1. Используемые технологии

- Spring, Spring-Boot
- H2 Database 
- liquibase
- Spring AOP - для аудита
- Gradle
- Yandex Translate API
- Jacoco, Mockito, Testcontainers - для тестов

## Запуск и тестирование

### 1. Клонируйте репозиторий проекта

Для того чтобы склонировать этот репозиторий, выполните следующую команду в вашей терминальной оболочке:

```bash
git clone https://github.com/mastkeey/TranslationService.git
```

После успешного клонирования, перейдите в директорию проекта с помощью команды:

```bash
cd TranslationService
```

### 2. Получите Api-Key Yandex Translate API

#### Документация доступна по ссылке [Yandex Translate](https://yandex.cloud/ru/docs/translate/).

Нужно создать аккаунт в [Yandex Cloud](https://yandex.cloud/ru/), после этого нужно создать сервисный аккаунт 
с ролью ai.translate.user, далее нужно создать платежный аккаунт. После того как мы создали платежный аккаунт, 
следует зайти в наш сервисный аккаунт и создать новый ключ. Это должен быть ОБЯЗАТЕЛЬНО API-ключ (не статический и не авторизированный). 

Вам будет выведен Ваш Api-Key, который надо подставить в файл `application.yml` : 
```yaml
yandex:
  api-key: ваш Api-Key 
```

### 3. Запуск и использование приложения

Сборка проекта:
```bash
./gradlew clean build (Linux, macOS)
gradle  clean build (Командная строка Gradle)
gradlew  clean build (Windows)
```

Для запуска проекта используйте команду из корневой директории:
```bash
./gradlew :bootRun (Linux, macOS)
gradle :bootRun (Командная строка Gradle)
gradlew :bootRun (Windows)
```

#### Доступ к базе данных

- Перейдите в своём браузере на http://localhost:8080/h2-console (если меняли порт, на котором запускается приложение, то соответственно бд будет тоже запущена на нем, а не на 8080).
- Введите из `application.yml` url в JDBC URL:, логин и пароль в соответствующие поля H2 Console.
- Нажмите Connect.

#### Использование приложения

Приложение обрабатывает POST запросы по ендпоинту `/v1/translate` со следующим телом:

```json
{
    "sourceLanguageCode" : "код языка исходного текста",
    "targetLanguageCode" : "код языка, на который мы хотим перевести текст",
    "text" : "текст для перевода"
}
```

И отдает следующее при 200ых запросах:

```json
{
    "translatedText": "переведенный исходный текст"
}
```


## Примеры использования


#### 1. 200
Запрос:
```json
{
    "sourceLanguageCode" : "en",
    "targetLanguageCode" : "ru",
    "text" : "Hello world, this is my first program"
}
```
Ответ:
```json
{
    "translatedText": "Здравствуйте мир, этот является мой первый программа"
}
```
#### 2. 400:
```json
{
  "sourceLanguageCode" : "",
  "targetLanguageCode" : "ru",
  "text" : "Hello world, this is my first program"
}
```
Ответ:
```json
{
  "status": 400,
  "code": "BadArgument",
  "message": "Не найден язык исходного сообщения."
}
```