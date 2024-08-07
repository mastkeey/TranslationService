# Translation-Service

Веб-приложение переводчик, предназначенное для перевода с использованием Yandex Translate API.

### 1. Используемые технологии

- Spring, Spring-Boot
- H2 Database
- PostgreSQL
- liquibase
- Docker
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

### 3. Запуск приложения через Docker контейнер (рекомендуется)

#### 1. Настройте файл окружения

В файл .env в корневой директории проекта нужно подставить полученный Api-ключ:
```yaml
APP_TRANSLATION_API_KEY=Ваш Api-ключ
```
#### 2. Соберите и запустите контейнеры
При запущенном Docker нужно использовать Docker-compose файл для сборки образа и запуска бд с приложением:

```bash
docker-compose up --build
```
Будет создано и запущено два контейнера: для бд и для приложения


### 4. Запуск приложения через Gradle

Сборка проекта:
```bash
./gradlew clean build (Linux, macOS)
gradlew  clean build (Windows)
```

или через Execute gradle task выполнить `gradle clean build`

Для запуска проекта используйте команду из корневой директории:
```bash
./gradlew :bootRun (Linux, macOS)
gradlew :bootRun (Windows)
```

или через Execute gradle task выполнить `gradle :bootRun`


### 5. Доступ к базе данных

Если вы использовали Docker-compose для запуска, то нужно будет подключаться к PostgreSQL:

- JDBC URL: `jdbc:postgresql://localhost:5433/translater`
- User Name: `postgres`
- Password: `password`

В ином случае к H2:

- Перейдите в своём браузере на http://localhost:8080/h2-console (если меняли порт, на котором запускается приложение, то соответственно бд будет тоже запущена на нем, а не на 8080).
- Введите из `application.yml` url в JDBC URL:, логин и пароль в соответствующие поля H2 Console.
- Нажмите Connect.

### 6. Использование приложения

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