# Scrum
Командная разработка на Java
# интернет-магазин
Приложение - продуктовый маркет.
При запуске приложения появляется главная страница магазина с приветствием.
На главной странцие есть несколько разделов - можно просматривать, откладывать в корзину и заказывать продукты, а также
просматривать свои заказы. Чтобы сделать заказ, нужно авторизоваться.
В режиме админа появляется новый раздел "Администрирование", в котором можно управлять продуктами и учетными записями
пользователей.

Пользователи по умолчанию для тестирования приложения:
1) логин: bob, пароль: 100 (обычный пользователь),
2) логин: john, пароль: 100 (пользователь - админ).

Запуск приложения:
1. git clone https://github.com/DVPeshe/Scrum.git
2. cd Scrum
3. mvn clean install
4. sudo docker-compose up -d
5. Откыть в браузере http://localhost:3000/market-front

Вставка аватаров для личного кабинета:
1. выполнить docker-compose.yml
2. docker cp ./images/bob.jpg scrum-postgres-1:/var/lib/postgresql/data
3. docker cp ./images/john.jpg scrum-postgres-1:/var/lib/postgresql/data
4. docker cp ./images/artur.jpg scrum-postgres-1:/var/lib/postgresql/data
5. перезапустить flyway

Использованные технологии:

* Java
* Spring Boot
* Maven
* Spring Cloud Gateway
* Spring Web Service
* Spring Security
* PostgreSQL
* Flyway Migration
* Redis
* Lombok
* Swagger
* Docker
* Spring Data JPA
* JUnit