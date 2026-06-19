# 📚 ReadLog — Personal Book Tracking Web App

A full stack web application to track books you've read, are currently reading, and want to read. Built with HTML, CSS, JavaScript, Java Spring Boot, and MySQL.

---

## 🔗 Live Demo

- **Frontend:** [ReadLog App](https://rakeshselvakumar.github.io/ReadLog---fullstack-)
- **Backend API:** [ReadLog API](https://readlog-backend-8hq4.onrender.com/api/auth/test)

---

## 👨‍💻 Developer

**Rakesh SJ**
- GitHub: [@rakeshselvakumar](https://github.com/rakeshselvakumar)

---

## ✨ Features

- 📖 Track books across 4 shelves — Reading, Read, Want to Read, Favourites
- ⭐ Rate and review books
- 📊 Reading statistics with charts
- 📅 Yearly reading goal tracker
- 🔍 Book cover fetch via Open Library API
- 📁 Custom cover image upload
- 🔮 Book recommendations based on your genres and favourites
- 🌙 Dark mode / Light mode toggle
- 📤 Export books as CSV or PDF
- 🔐 User authentication with JWT
- 📱 Responsive — works on mobile and desktop
- 🌍 Access your bookshelf from any device

---

## 🛠️ Tech Stack

### Frontend
| Technology | Purpose |
|---|---|
| HTML5 | Structure |
| CSS3 | Styling + Dark mode |
| JavaScript | Logic + API calls |
| GitHub Pages | Hosting |

### Backend
| Technology | Purpose |
|---|---|
| Java 21 | Programming language |
| Spring Boot 3 | REST API framework |
| Spring Security | Authentication |
| JWT | Token based auth |
| Hibernate / JPA | Database ORM |
| Maven | Build tool |
| Docker | Containerization |
| Render.com | Hosting |

### Database
| Technology | Purpose |
|---|---|
| MySQL 8.4 | Relational database |
| Aiven Cloud | Database hosting |

---

## 📁 Project Structure

```
ReadLog---fullstack-/
├── index.html          # Main bookshelf page
├── login.html          # Login / Register page
├── style.css           # Main styles
├── login.css           # Login page styles
├── app.js              # Bookshelf logic + API calls
├── login.js            # Auth logic + API calls
└── backend/
    ├── Dockerfile
    ├── pom.xml
    └── src/main/java/com/readlog/backend/
        ├── BackendApplication.java
        ├── config/
        │   ├── SecurityConfig.java
        │   └── JwtAuthFilter.java
        ├── controller/
        │   ├── AuthController.java
        │   └── BookController.java
        ├── dto/
        │   ├── AuthResponse.java
        │   ├── BookRequest.java
        │   ├── BookResponse.java
        │   └── RegisterRequest.java
        ├── entity/
        │   ├── User.java
        │   └── Book.java
        ├── repository/
        │   ├── UserRepository.java
        │   └── BookRepository.java
        └── service/
            ├── AuthService.java
            ├── BookService.java
            └── JwtService.java
```

---

## 🔌 API Endpoints

### Auth
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Create new account |
| POST | `/api/auth/login` | Login and get JWT token |
| GET | `/api/auth/test` | Health check |

### Books (requires JWT token)
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/books` | Get all my books |
| POST | `/api/books` | Add a new book |
| PUT | `/api/books/{id}` | Edit a book |
| DELETE | `/api/books/{id}` | Delete a book |

---

## 🚀 How It Works

```
Browser (GitHub Pages)
        ↓ API calls with JWT token
Render.com (Spring Boot)
        ↓ reads/writes data
Aiven Cloud (MySQL Database)
```

1. User registers/logs in → Spring Boot verifies → returns JWT token
2. Frontend stores token in localStorage
3. Every book request sends the token in Authorization header
4. Backend verifies token → returns only that user's books
5. Books are stored in MySQL on Aiven Cloud

---

## 💻 Running Locally

### Prerequisites
- Java 21
- MySQL 8.0+
- Git

### Steps

**1. Clone the repo**
```bash
git clone https://github.com/rakeshselvakumar/ReadLog---fullstack-.git
cd ReadLog---fullstack-
```

**2. Set up MySQL**
```sql
CREATE DATABASE readlog;
```

**3. Configure backend**

Create `backend/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/readlog
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
server.port=8081
jwt.secret=your-secret-key-at-least-32-characters
jwt.expiration=86400000
```

**4. Run backend**
```bash
cd backend
./mvnw spring-boot:run
```

**5. Update frontend API URL**

In `app.js` and `login.js`:
```javascript
const API_URL = 'http://localhost:8081';
```

**6. Open frontend**

Open `index.html` in your browser.

---

## 🌐 Deployment

| Service | Purpose | Cost |
|---|---|---|
| GitHub Pages | Frontend hosting | Free forever |
| Render.com | Backend hosting | Free tier |
| Aiven Cloud | MySQL database | Free tier |
| cron-job.org | Keep backend alive | Free forever |

---

## 📸 Screenshots

> Login Page — Register and sign in with JWT authentication

> Home Page — View all books with dark mode support

> Book Cards — Status badges, star ratings, progress bars

> Stats Page — Charts showing reading activity

> Recommendations — AI-powered book suggestions

---

## 🎯 What I Learned Building This

- Building a complete REST API with Java Spring Boot
- JWT authentication and security
- Connecting frontend JavaScript to a backend API
- MySQL database design and JPA/Hibernate
- Docker containerization
- Deploying a full stack app for free
- Git version control and GitHub

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

---

*Built with ❤️ by Rakesh SJ*
