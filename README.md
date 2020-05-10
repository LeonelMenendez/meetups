# Meetups! 🍻
APP to create meetups and enroll to them through invitations sent by his administrator. Additionally, it calculates the number of beer cases needed for the day of the meeting according to the temperature.

---

## Features 📋
* Create meetups.
* Get the temperature of the meetup day.
* Get the recommended number of beer cases for the meetup based on the temperature.
* Send invitations for a meetup.
* Accept an invitation for a meetup and enroll to it.
* Do the check-in for a meetup to confirm your assistance.

---

## Launch 🚀
### Backend
> The ``` application.properties ``` file contains the properties needed to run the application, modify it or create a new profile with your own custom configuration.

> In order to select a profile from where the properties will be recovered, you need to specify the profile name through the environment variables:
> ```
> spring.profiles.active=prod
> ```

> Properties enclosed in curly braces must be provided by environment variables, e.g. ${JWT_DURATION:} can be set as follows:
> ```
> JWT_DURATION=45m
> ```

Generate the jar file:
```
$ mvn install
```
Run:
```
$ java -jar target/meetup-0.0.1-SNAPSHOT.jar
```

### Frontend
Install npm dependencies:
```
$ npm install
```
Run:
 ```
$ ng serve
 ```
---

## Built with 🛠
* [IntelliJ IDEA](https://www.jetbrains.com/idea/) - IDE.
* [Visual Studio Code](https://code.visualstudio.com/) - IDE.
* [Spring Boot](https://spring.io/projects/spring-boot) - Backend framework.
* [Angular](https://angular.io/) - Frontend framework.
* [Angular Material](https://material.angular.io/) - UI component library.
* [Sass](https://sass-lang.com/) - CSS preprocessor.
* [OpenAPI 3.0 (Springdoc)](https://springdoc.org/) - API documentation.
* [Maven](https://maven.apache.org/) - Dependency manager.

---

# API ☁
* Once the backend server is running, you can access the [Swagger UI](http://localhost:8080/api/swagger-ui.html).
* The API uses [JWT](https://jwt.io/) authentication. In order to access its resources, you need to sign in through the endpoint located in the authentication panel. If the credentials are valid, you will receive a response with the JWT, copy it and paste to the popup that is displayed when you click the Authorize button.

---

# To do 📌
* Notifications.
* Testing.

---

# General information 💬
* The number of beer cases needed for a meetup is calculated based on these conditions:
  * If the average temperature of the meetup day:
    * Is less than 20º -> 0.75 beer(s)/person
    * Is between 20º and 24º -> 1 beer(s)/person
    * Is greater than 24º -> 2 beer(s)/person
  * Each beer case has 6 units.
  * The number of needed beer cases is always rounded up.
* The temperature retrieved at the meetups creation moment is obtained from [Weatherbit.io](https://www.weatherbit.io/api) for Buenos Aires city in degree Celsius and it's the average temperature of the given day.
* The weather forecast is limited to 15 days after the current date.

---

## License 📜
This project is licensed under the terms of the **MIT** license.

---
## Author 👦
> [GitHub](https://github.com/lzmz) &nbsp;&middot;&nbsp; [LinkedIn](https://www.linkedin.com/in/leonel-menendez/) &nbsp;&middot;&nbsp;