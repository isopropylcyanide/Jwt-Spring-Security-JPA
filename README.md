## Jwt-Spring-Security-JPA ##
<a href="https://sourcerer.io/isopropylcyanide"><img src="https://img.shields.io/badge/Java-50%20commits-orange.svg" alt=""></a>

A demo project explaining the backend authentication using JWT (Json Web Token) authentication using Spring Security &amp; MySQL JPA



![](https://cdn-images-1.medium.com/max/1334/1*7T41R0dSLEzssIXPHpvimQ.png)



---

## JWT ##
JSON Web Tokens are an open, industry standard RFC 7519 method for representing claims securely between two parties.
![image](https://user-images.githubusercontent.com/12872673/44461592-20470880-a62f-11e8-8597-0a0f79b5ef92.png)


## Exception Handling ##
The app throws custom exceptions wherever necessary which are captured through a controller advice. It then returns the appropriate error response to the caller
* AppException
* BadRequestException
* ResourceAlreadyInUseException
* ResourceNotFoundException
* UserLoginException
* UserRegistrationException
* MethodArgumentNotValidException

Moreover, entities are validated using JSR-303 Validation constraints. 

---

## Steps to Setup the Spring Boot Back end app (polling-app-server)

1. **Clone the application**

	```bash
	git clone https://github.com/isopropylcyanide/Jwt-Spring-Security-JPA.git
	cd AuthApp
	```

2. **Create MySQL database**

	```bash
	create database login_db
	```

3. **Change MySQL username and password as per your MySQL installation**

	+ open `src/main/resources/application.properties` file.

	+ change `spring.datasource.username` and `spring.datasource.password` properties as per your mysql installation

4. **Run the app**

	You can run the spring boot app by typing the following command -

	```bash
	mvn spring-boot:run
	```

	The server will start on port 9004. Token default expiration is 600000ms i.e 10ms.
	```
5. **Add the default Roles**
	
	The spring boot app uses role based authorization powered by spring security. Please execute the following sql queries in the database to insert the `USER` and `ADMIN` roles.

	```sql
    INSERT INTO ROLE (ROLE_NAME) VALUES ('ROLE_USER');
    INSERT INTO ROLE (ROLE_NAME) VALUES ('ROLE_ADMIN');
	```

	Any new user who signs up to the app is assigned the `ROLE_USER` by default.

---
## Demo Screens ##

1. **Registering a user**
---
![image](https://user-images.githubusercontent.com/12872673/44460909-841c0200-a62c-11e8-96b6-996b8de6b2b8.png)


2. **Logging in a valid user**
---
![image](https://user-images.githubusercontent.com/12872673/44460953-b6c5fa80-a62c-11e8-929e-affe4df964c4.png)

3. **Logging in an invalid user**
---
![image](https://user-images.githubusercontent.com/12872673/44461046-03a9d100-a62d-11e8-8073-fb6b32cec3de.png)

3. **Using the token in request header & accessing resource**
---
![image](https://user-images.githubusercontent.com/12872673/44461090-2e942500-a62d-11e8-8f05-8ecd1d2828e3.png)

4. **Accessing admin resource with invalid permissions/token**
---
![image](https://user-images.githubusercontent.com/12872673/44461159-68fdc200-a62d-11e8-9a8c-95a9c84d52cd.png)

