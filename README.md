## Jwt-Spring-Security-JPA ##

![Travis (.org)](https://img.shields.io/travis/isopropylcyanide/Jwt-Spring-Security-JPA)
![GitHub](https://img.shields.io/github/license/isopropylcyanide/Jwt-Spring-Security-JPA?color=blue)

#### A demo project explaining the backend authentication using JWT (Json Web Token) authentication using Spring Security &amp; MySQL JPA.

There's support for the following features:

* Conventional email/username based registration with admin support.
* Conventional Login using Spring Security and generation of JWT token.
* Multiple device login and logout support.
* In memory store for blacklisting JWT tokens upon user logout.
* Expiration bases email verification. Mail is sent upon registration.
* Resend the email confirmation email if old one expires.
* Forgot-password functionality with password reset token validations.
* Admin protected urls leveraging Spring security.
* Refresh JWT tokens once the temporary JWT expires.
* Check availability of username/email during registration.

![](https://cdn-images-1.medium.com/max/1334/1*7T41R0dSLEzssIXPHpvimQ.png)

---

## JWT ##

JSON Web Tokens are an open, industry standard RFC 7519 method for representing claims securely between two parties.

---

## Swagger Docs ##

The project has been configured with a Swagger docket that exposes the APIs with the schema

Accessible at `http://localhost:9004/swagger-ui.html` once the app is running.

![image](https://user-images.githubusercontent.com/12872673/139554260-cc570c43-953a-46d1-a4c4-305ff3807ffb.png)

---

## Exception Handling ##

* The app throws custom exceptions wherever necessary which are captured through a controller advice. It then returns
  the appropriate error response to the caller
* Moreover, entities are validated using JSR-303 Validation constraints.

---

## Getting Started

<h4> Clone the application </h4>

```bash
$ git clone https://github.com/isopropylcyanide/Jwt-Spring-Security-JPA.git
$ cd Jwt-Spring-Security-JPA
```

<h4> Create a MySQL database </h4>

```bash
$ create database login_db
```

<h4> Change MySQL username and password as per your MySQL installation </h4>

- Edit `spring.datasource.username` and `spring.datasource.password` properties as per your mysql installation
  in `src/main/resources/application.properties`
- Edit `spring.mail.username` and `spring.mail.password` properties as per your mail
  server `src/main/resources/mail.properties`

<h4> Run the app </h4>

```shell
./mvnw spring-boot:run   # For UNIX/Linux based operating systems
mvnw.cmd spring-boot:run # For Windows based operating systems
```

- The server will start on `server.port:9004` and will create the tables for you.
- Every run of the app will reset your state. To not do that, modify `spring.jpa.hibernate.ddl-auto: update`

---

## API ##

<details>
<summary>Registering a User</summary>

```
curl --location --request POST 'localhost:9004/api/auth/register' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "amangarg1995sep@gmail.com",
    "password": "amangarg",
    "registerAsAdmin": true
}'
```

![image](https://user-images.githubusercontent.com/12872673/139542127-126c70d7-8d94-49a9-9dc6-2c3b127d8844.png)

> ⚠️ If you re-register an email twice, you'll get the "email in use" error

</details>

---

<details>
<summary>Logging in an unverified user</summary>

```
curl --location --request POST 'localhost:9004/api/auth/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "amangarg1995sep@gmail.com",
    "password": "amangarg",
    "deviceInfo": {
        "deviceId": "D1",
        "deviceType": "DEVICE_TYPE_ANDROID",
        "notificationToken": "N1"
    }
}'
```

![image](https://user-images.githubusercontent.com/12872673/139542083-a9df7f31-16d8-4d1c-8187-3e52a8d9d1e6.png)

</details>

---

<details>
<summary>Confirming the user email verification token</summary>

```
curl --location --request GET 'localhost:9004/api/auth/registrationConfirmation?token=bcbf8764-dbf2-4676-9ebd-2c74436293b9' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "a@b.com",
    "password": "HI12",
    "deviceInfo": {
        "deviceId": "D1",
        "deviceType": "DEVICE_TYPE_ANDROID",
        "notificationToken": "N1"
    }
}'
```

![image](https://user-images.githubusercontent.com/12872673/139542456-99cde036-acfe-48db-8bf7-8c86bde18b13.png)

> ⚠️ If you pass the incorrect token you will get a "Token Mismatch error"

> ❔ **Don't know the token?**: Check your email in `mail.properties`

> ❔ **Still didn't get it?**: Look inside the database `email_verification_token#token`

</details>

---

<details>
<summary>Logging in the user with valid credentials</summary>

```
curl --location --request POST 'localhost:9004/api/auth/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "amangarg1995sep@gmail.com",
    "password": "amangarg",
    "deviceInfo": {
        "deviceId": "D1",
        "deviceType": "DEVICE_TYPE_ANDROID",
        "notificationToken": "N1"
    }
}'
```

![image](https://user-images.githubusercontent.com/12872673/139542792-5b3b44d7-9cc3-4cbf-83a3-f4dc677ff2e6.png)

> ⚠️ If you do not enter correct credentials you will get a "Bad credentials error"

> ⚠️ If your email is not verified (refer the above API) you will get an "Unauthorized" error

> ❔ Device information is required to enable a multi device login and logout functionality.

</details>

---

<details>
<summary>Using the JWT token to access a user resource </summary>

```
curl --location --request GET 'localhost:9004/api/user/me' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjM1NjE0NTY4LCJleHAiOjE2MzU2MTU0Njh9.d8CJYduoC44njutphODoezheSt_so3Doc9g1RSiMaDU_qJwY0_3Ym4092hFkHsh-jbyB_9i66LbwSEE-szAgEw'
```

![image](https://user-images.githubusercontent.com/12872673/139542964-87617a5e-8771-44cd-a40f-160a2fb0b8ce.png)

> ⚠️ If you enter an invalid token (obtained post login), you will get an "Incorrect JWT Signature" error.

> ⚠️ If you enter a malformed JWT token, you will get a "Malformed JWT Signature" error.

> ⚠️ If you enter an expired JWT token (default: `app.jwt.expiration`, you will get an "Expired JWT Signature" error and clients should refresh the JWT token.

![image](https://user-images.githubusercontent.com/12872673/139553744-9b3de48f-4974-47b1-9572-6ad767f46fc7.png)


</details>


---

<details>
<summary>Using the JWT token to access an admin resource </summary>

```
curl --location --request GET 'localhost:9004/api/user/admins' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjM1NjE0NTY4LCJleHAiOjE2MzU2MTU0Njh9.d8CJYduoC44njutphODoezheSt_so3Doc9g1RSiMaDU_qJwY0_3Ym4092hFkHsh-jbyB_9i66LbwSEE-szAgEw'
```

![image](https://user-images.githubusercontent.com/12872673/139543215-5235a56b-dccc-4058-a2e7-2943a1edd32d.png)


> ⚠️ If you registered a user with `registerAsAdmin: false`, then you will get a "Forbidden" error.
![image](https://user-images.githubusercontent.com/12872673/139543260-bb34235b-6372-474d-98d9-b8c976dd9c3e.png)

> ⚠️ JWT has to be valid (same constraints as the above user resource API)

</details>

---
<details>
<summary>Logout user</summary>

```
curl --location --request POST 'localhost:9004/api/user/logout' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjM1NjE0NTY4LCJleHAiOjE2MzU2MTU0Njh9.d8CJYduoC44njutphODoezheSt_so3Doc9g1RSiMaDU_qJwY0_3Ym4092hFkHsh-jbyB_9i66LbwSEE-szAgEw' \
--header 'Content-Type: application/json' \
--data-raw '{
    "deviceInfo": {
        "deviceId": "D1",
        "deviceType": "DEVICE_TYPE_ANDROID",
        "notificationToken": "N1"
    }
}'
```

![image](https://user-images.githubusercontent.com/12872673/139543370-9a2b7126-2342-41e5-88ef-4607cd4489a5.png)

> ❔ Logging out also deletes the refresh token associated with the device. In real production, this token should be specifically invalidated.

> ⚠️ If the JWT isn't passed then you will get an "Unauthorized" error.

![image](https://user-images.githubusercontent.com/12872673/139543332-e10e7f09-a8ce-4e9b-826e-4eabb3aa95d2.png)

> ⚠️ If you try to log out same user twice (without an app restart), you will get a "Token Expired" error. This works because on logout we invalidate the JWT

![image](https://user-images.githubusercontent.com/12872673/139543427-255d52fb-7009-40fc-a087-e28371d4d056.png)

> ⚠️ If you try to log out a logged-in user against an invalid device (say D2), you will get an "Invalid Device" error.

![image](https://user-images.githubusercontent.com/12872673/139543479-93638179-ce96-45dd-b9bc-314d73657ccd.png)

</details>

---

<details>
<summary>Request a reset password link for a registered user</summary>

```
curl --location --request POST 'localhost:9004/api/auth/password/resetlink' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "amangarg1995sep@gmail.com"
}'
```

![image](https://user-images.githubusercontent.com/12872673/139543608-1ad23334-84a9-4c43-b849-197a2b4383e2.png)

> ❔ You can request a password reset multiple times. The reset token would be generated multiple times with an `app.token.password.reset.duration`

> ❔ You can request a password reset for a user even when they have not verified their email once. This is okay for our demo case.

> ⚠️ If you try to request a password reset for an unregistered user, you will get a "No matching user" error

</details>

---

<details>
<summary>Reset password for a registered user</summary>

```
curl --location --request POST 'localhost:9004/api/auth/password/reset' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "amangarg1995sep@gmail.com",
    "password": "P1",
    "confirmPassword": "P1",
    "token": "880ab6f1-4b4b-4d04-92bd-8995b4063205"
}'
```

![image](https://user-images.githubusercontent.com/12872673/139558080-cc69c43d-eff3-4316-9834-170f3f496c06.png)


> ⚠️ If your new passwords do not match, there will be an error

> ⚠️ If your password reset token is not valid or is for some other user, you'll get a "Password Reset Token Not Found" error.

![image](https://user-images.githubusercontent.com/12872673/139545592-7076ccef-c23e-4a6b-90ec-de157b4e4d67.png)

> ⚠️ If you try to use a password reset token twice, you will get a "Token Inactive" error
![image](https://user-images.githubusercontent.com/12872673/139558023-f26a60f8-affe-4c39-a998-b264064be4f1.png)



</details>

---


<details>
<summary>Refreshing the JWT token for longer login sessions</summary>

```
curl --location --request POST 'localhost:9004/api/auth/refresh' \
--header 'Content-Type: application/json' \
--data-raw '{
    "refreshToken": "d029e0fa-80f5-4768-837c-7e85a0f94960"
}'
```

![image](https://user-images.githubusercontent.com/12872673/139551414-d45fc7ab-eaf6-4f24-b7ff-b34c7a8d66c4.png)

> ❔ You can refresh a JWT multiple times against the refresh token. That is the purpose of refresh. Refresh token expiry can be controlled with `app.token.refresh.duration`

> ⚠️ If you pass an invalid refresh token (obtained through login), you will get a "No token found" error

![image](https://user-images.githubusercontent.com/12872673/139549108-0c17f424-9a5c-4deb-ad35-ac56b80d28c6.png)

</details>

---

<details>
<summary>Check email in use</summary>

```
curl --location --request GET 'localhost:9004/api/auth/checkEmailInUse?email=amangarg1995sep@gmail.com'
```

![image](https://user-images.githubusercontent.com/12872673/139553820-8d18ad09-9d96-48fa-bca1-0a8ddefd492f.png)



> ❔ The API can be accessed insecurely and hence should be rate limited in production to prevent a DDOS attack.

> ❔ You can request a password reset for a user even when they have not verified their email once. This is okay for our demo case.

> ⚠️ If you try to request a password reset for an unregistered user, you will get a "No matching user" error

</details>

---

<h3> Roles </h3>

- The spring boot app uses role based authorization powered by spring security
- Tables and role data should have been created by default upon the first startup.
- Any new user who signs up to the app is assigned the `ROLE_USER` by default.
- In case the role entries aren't created, please execute the following sql queries in the database to insert the `USER`
  and `ADMIN` roles.

```sql
INSERT INTO `login_db.role` (ROLE_NAME)
VALUES ('ROLE_USER');
INSERT INTO `login_db.role` (ROLE_NAME)
VALUES ('ROLE_ADMIN');
```

### Contribution ###

* Remember, the project is a demo and should not be used into production directly.
* Please fork the project and adapt it to your use case.
* Submit a pull request with proper motivation and test plan.
* Postman collection dump available [here](https://gist.github.com/isopropylcyanide/a4e8556814dbf28dc3320be59785b807))
* Not everything is in scope for this demo project. Feel free to fork the project and extend the functionality.
* Project is equipped with a JUnit but lacks tests in most places. Would really appreciate your contributions here.

---
