#  LinkVault – Backend URL Shortener API

LinkVault is a secure and feature-packed **backend-only URL shortener service**, built using **Spring Boot**. It allows users to create short links with advanced features like expiration, password protection, public/private access, and detailed click analytics — all accessible through RESTful APIs.

---

##  Features

-  **User Authentication**
    - Register/Login using JWT-based authentication
    - View & manage only your own links

-  **URL Shortening**
    - Convert long URLs into short, shareable links
    - Supports custom aliases (e.g., `/myyt`)

-  **Expiration Support**
    - Set a time limit after which the link expires automatically

-  **Password-Protected URLs**
    - Add password security to links for extra privacy

-  **Public/Private URLs**
    - Public: Anyone can access
    - Private: Only the creator (logged-in user) can use/view

- **Click Analytics**
    - Records:
        - Timestamp
        - IP Address
        - User-Agent (Browser/Device)
        - Referrer (Optional)

-  **Manage URLs**
    - View your created links
    - Delete or update as needed

-  **Backend Only**
    - Designed to be used with Postman, Swagger, or as an API microservice

---

##  Tech Stack

| Layer          | Tech Used               |
|----------------|-------------------------|
| Language       | Java                    |
| Framework      | Spring Boot             |
| Authentication | Spring Security + JWT   |
| Database       | MySQL                   |
| ORM            | Spring Data JPA         |
| Docs & Testing | Swagger / Postman       |
| Deployment     | Docker + Render/Railway |

---

##  DB Diagram

[Click Here For Db Diagram](https://dbdiagram.io/d/url_shortener-687f12b6f413ba3508ff35e1)

---

##  API Endpoints

###  Auth APIs
- `POST /api/auth/register` → Register new user
- `POST /api/auth/login` → Login and receive JWT token

###  URL Shortening
- `POST /api/url/shorten` → Create a short URL
- `GET /{shortCode}` → Redirect to original URL
- `GET /api/url/my-links` → List all URLs created by the user
- `DELETE /api/url/delete/{code}` → Delete a short URL

###  Analytics
- `GET /api/analytics/{shortCode}` → View analytics (if public or owner)

---

##  Sample `POST /shorten` Request
```json
{
  "originalUrl": "https://github.com/biswalsoumya342",
  "isPublic": true,
  "password": "pass123",
  "expireMinutes": 60
}
```
