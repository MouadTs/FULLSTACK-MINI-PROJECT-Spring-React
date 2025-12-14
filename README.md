# Project Task Management System

A full-stack web application designed to manage projects and their associated tasks. This application allows users to create projects, track task progress, filter by status, and manage deadlines using a secure and responsive interface.

---

## 🛠️ Tools & Technologies Used

### Backend
*   **Language:** Java 17
*   **Framework:** Spring Boot 3
*   **Security:** Spring Security (JWT Authentication)
*   **Data Access:** Spring Data JPA / Hibernate
*   **Build Tool:** Maven

### Frontend
*   **Library:** React.js
*   **HTTP Client:** Axios
*   **Routing:** React Router
*   **Styling:** CSS / Bootstrap

### Database & DevOps
*   **Database:** MySQL
*   **Containerization:** Docker & Docker Compose

---

## 🚀 Option 1: Run with Docker (Recommended)

The easiest way to run the application is using Docker. This sets up the MySQL Database, Backend, and Frontend automatically.

**Prerequisites:**
*   [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed and running.

**Steps:**
1.  Open your terminal in the root directory of the project.
2.  Run the following command:
    ```bash
    docker-compose up --build
    ```
3.  Wait for the containers to start. You can access the application here:
    *   **Frontend (UI):** [http://localhost:3000](http://localhost:3000)
    *   **Backend (API):** [http://localhost:8080](http://localhost:8080)

---

## 💻 Option 2: Run Manually (Local Setup)

If you prefer to run the application without Docker, follow these steps in order.

### 1. Database Setup
You must have **MySQL** installed locally (e.g., via MySQL Workbench).

1.  Open MySQL Workbench or your terminal.
2.  Create a new database (Schema) named `PG_Management`:
    ```sql
    CREATE DATABASE PG_Management;
    ```
3.  Update the `application.properties` file in `Project_Tasks_Management/src/main/resources/` if your local MySQL settings differ:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/PG_Management
    spring.datasource.username=root
    spring.datasource.password=your_password
    ```

### 2. How to Run Backend
1.  Navigate to the backend folder:
    ```bash
    cd Project_Tasks_Management
    ```
2.  Install dependencies and run the app:
    ```bash
    mvn spring-boot:run
    ```
3.  The backend server will start at `http://localhost:8080`.

### 3. How to Run Frontend
1.  Open a new terminal and navigate to the frontend folder:
    ```bash
    cd project-management-frontend
    ```
2.  Install the node modules:
    ```bash
    npm install
    ```
3.  Start the React application:
    ```bash
    npm start
    ```
4.  The application will open in your browser at `http://localhost:3000`.

---

## 🔑 Default Access & API

**Authentication:**
The application uses JWT. You must register a user or login via the `/api/auth/login` endpoint to receive a token for protected routes.

**Common Endpoints:**
*   `POST /api/auth/register` - Register a new user
*   `POST /api/auth/login` - Login
*   `GET /api/projects` - List all projects
*   `GET /api/projects/{id}/tasks` - List tasks (Supports pagination & search)
*   `PUT /api/tasks/{id}` - Update a task

---

## 📸 Project Status
*   [x] User Authentication
*   [x] Project Creation
*   [x] Task CRUD Operations
*   [x] Pagination & Search
*   [x] Docker Support
