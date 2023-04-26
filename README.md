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
2. 
docker cp ./images/bob.jpg scrum-postgres-1:/var/lib/postgresql/data

docker cp ./images/john.jpg scrum-postgres-1:/var/lib/postgresql/data

docker cp ./images/artur.jpg scrum-postgres-1:/var/lib/postgresql/data

3. перезапустить flyway

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

### email-service

Реализует возможность массовой рассылки уведомлений о возврате товара в продажу по электронной почте и подписки на них.
Подписываться на рассылку уведомлений может любой зарегестрированный пользователь.

![подписка на рассылку](https://user-images.githubusercontent.com/75074559/234510398-d67f9172-344b-4f5e-8fcd-07014cd7bea5.png)
![подписка на рассылку 2](https://user-images.githubusercontent.com/75074559/234510454-5cc3806e-f107-4579-b452-63a68148fec1.png)


Инициировать рассылку может только менеджер в разделе редактирования продуктов.

![оповещение о поступлении](https://user-images.githubusercontent.com/75074559/234510742-9287bb3c-9f15-4908-bf5e-4dced523729c.png)
![инициирование рассылки](https://user-images.githubusercontent.com/75074559/234510773-fa03ac58-c4c0-40e5-8ca9-2c0709d736c3.png)


Пользователь получает электронное письмо с уведомлением о возврате товара в продажу на указанный им электронный адрес.

![получение электронного письма](https://user-images.githubusercontent.com/75074559/234512163-88ebe795-2f43-4b0b-bccc-c78a9508b7a7.png)



