## Jwt-Spring-Security-JPA ##

![Travis (.org)](https://img.shields.io/travis/isopropylcyanide/Jwt-Spring-Security-JPA)
![GitHub](https://img.shields.io/github/license/isopropylcyanide/Jwt-Spring-Security-JPA?color=blue)

#### A demo project explaining the backend authentication using JWT (Json Web Token) authentication using Spring Security &amp; MySQL JPA.
There's support for the following features:

* Conventional email/username based registration with admin support
* Conventional Login using Spring Security and generation of JWT token
* Multiple device login and logout support
* In memory store for blacklisting logged out tokens
* Support for expiration bases email verification. Mail is sent upon registration. 
* Resend the email confirmation email if old one expires
* Support for password updation once logged in
* Support for forgot-password functionality with password reset token sent to mail.
* Supports admin protected urls leveraging Spring security
* API to refresh JWT tokens once the temporary JWT expires. 
* API to check availability of username/email during registration.

![](https://cdn-images-1.medium.com/max/1334/1*7T41R0dSLEzssIXPHpvimQ.png)

---

## Swagger Docs ##
The project has been configured with a basic Swagger docket that exposes the commonly used API's along with the expected params.
![image](https://user-images.githubusercontent.com/12872673/45046897-24ded880-b095-11e8-8930-7b678e2843bb.png)


---

## JWT ##
JSON Web Tokens are an open, industry standard RFC 7519 method for representing claims securely between two parties.


## Exception Handling ##
The app throws custom exceptions wherever necessary which are captured through a controller advice. It then returns the appropriate error response to the caller
* AppException
* BadRequestException
* ResourceAlreadyInUseException
* ResourceNotFoundException
* UserLoginException
* UserRegistrationException
* MethodArgumentNotValidException
* UserLogoutException
* TokenRefreshException
* UpdatePasswordException
* PasswordResetException
* PasswordResetLinkException

Moreover, entities are validated using JSR-303 Validation constraints. 

---

## Steps to Setup the Spring Boot Back end app

1. **Clone the application**

	```bash
	git clone https://github.com/isopropylcyanide/Jwt-Spring-Security-JPA.git
	cd AuthApp
	```

2. **Create a MySQL database**

	```bash
	create database login_db
	```

3. **Change MySQL username and password as per your MySQL installation**

	+ open `src/main/resources/application.properties` file.

	+ change `spring.datasource.username` and `spring.datasource.password` properties as per your mysql installation
	
	+ open `src/main/resources/mail.properties` file.

	+ change `spring.mail.username` and `spring.mail.password` properties as per your mail installation

4. **Run the app**

	You can run the spring boot app by typing the following command -

	```bash
	mvn spring-boot:run
	```

	The server will start on port 9004. Token default expiration is 600000ms i.e 10 minutes.
	```
5. **Add the default Roles**
	
	The spring boot app uses role based authorization powered by spring security. Please execute the following sql queries in the database to insert the `USER` and `ADMIN` roles.

	```sql
    INSERT INTO ROLE (ROLE_NAME) VALUES ('ROLE_USER');
    INSERT INTO ROLE (ROLE_NAME) VALUES ('ROLE_ADMIN');
	```

	Any new user who signs up to the app is assigned the `ROLE_USER` by default.

---

### Contribution ###
* Please fork the project and adapt it to your use case.
* Submit a pull request.

---
## Demo Screens ##

1. **Registering a user**
---
![image](https://user-images.githubusercontent.com/12872673/44460909-841c0200-a62c-11e8-96b6-996b8de6b2b8.png)


2. **Logging in a valid user**
---
![image](https://user-images.githubusercontent.com/12872673/45047478-c155aa80-b096-11e8-96e8-d7872a92ee03.png)

3. **Logging in an invalid user**
---
![image](https://user-images.githubusercontent.com/12872673/44461046-03a9d100-a62d-11e8-8073-fb6b32cec3de.png)

3. **Using the token in request header & accessing resource**
---
![image](https://user-images.githubusercontent.com/12872673/44461090-2e942500-a62d-11e8-8f05-8ecd1d2828e3.png)

4. **Accessing admin resource with invalid permissions/token**
---
![image](https://user-images.githubusercontent.com/12872673/44461159-68fdc200-a62d-11e8-9a8c-95a9c84d52cd.png)

5. **Logging out the user device**
---
![image](https://user-images.githubusercontent.com/12872673/45047550-f3ffa300-b096-11e8-8520-3eae03b6ef78.png)

6. **Resetting the password**
---
![image](https://user-images.githubusercontent.com/12872673/45047624-3628e480-b097-11e8-944f-c88b1cd0c231.png)

7. **Refreshing the authentication token**
---
![image](https://user-images.githubusercontent.com/12872673/45047676-5bb5ee00-b097-11e8-84d4-2dbbe1489157.png)

8. **Confirming the user email verification token**
---
![image](https://user-images.githubusercontent.com/12872673/45047715-76886280-b097-11e8-9ea6-e0c649eb6cbd.png)

