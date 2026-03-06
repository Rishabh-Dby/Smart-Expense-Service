# Smart Expense Tracker 💰 #

A production-style backend expense management system built using Java and Spring Boot that helps users track expenses, analyze spending patterns, and receive alerts when their monthly budgets are exceeded.

The project demonstrates real-world backend engineering concepts such as JWT authentication, Redis caching, background jobs, and cloud deployment on AWS.
## 🎥 Demo ##

[Demo link](https://pages.github.com/)

## 🚀 Features ##
### 🔐 Authentication & Security ###

+ JWT-based authentication and authorization

+ Secure signup and login APIs

+ Stateless authentication using Spring Security

### 💸 Expense Management ###

+ Add and manage daily expenses

+ Categorize expenses (Food, Travel, Utilities, etc.)

+ Track spending history

### 📊 Monthly Analytics ###

+ Monthly expense summaries

+ Category-wise spending insights

+ Optimized using Redis caching

### 🧾 Budget Tracking ###

+ Users can set monthly budget limits

+ System detects when spending exceeds the budget

### 📧 Email Alerts ###

+ Automatic email notifications when a budget limit is exceeded

+ Implemented using SMTP (Mailtrap for development)

### ⚡ Redis Integration ###

Redis is used for several backend optimizations:

+ Caching analytics results

+ API rate limiting

+ Cache eviction when new expenses are added

### ⏱ Scheduled Jobs ###

+ Daily cron job checks for users exceeding their budget

+ Automatically sends email alerts

### 📂 CSV Import ###

+ Upload CSV files to bulk import expenses

### 📑 API Documentation ###

+ Interactive API documentation using Swagger UI

## 🏗 Architecture ##
Client
   │
   ▼
Spring Boot REST API
   │
   ▼
Service Layer
   │
   ▼
Repository Layer
   │
   ▼
PostgreSQL Database

Additional infrastructure components:

Spring Boot Application
       │
       ├── PostgreSQL (persistent storage)
       ├── Redis Cloud (caching + rate limiting)
       ├── Mailtrap SMTP (email alerts)
       └── AWS EC2 (application hosting)
## 🛠 Tech Stack ##
+ Backend
+ Java
+ Spring Boot
+ Spring Security
+ Spring Data JPA
+ Database
+ PostgreSQL
+ Caching
+ Redis Cloud
+ Infrastructure
+ AWS EC2
+ Tools
+ Swagger / OpenAPI
+ Gradle
+ RedisInsight
+ Mailtrap

## ⚡ Performance Optimizations ##

+ Redis caching for analytics APIs
  
+ Cache eviction when expenses are updated

+ API rate limiting to prevent abuse

## 📦 Deployment ##

The application is deployed on AWS EC2 as a standalone Spring Boot JAR.

## Deployment stack: ##

AWS EC2
   │
   ▼
Spring Boot Application
   │
   ▼
PostgreSQL Database
   │
   ▼
Redis Cloud

## ⚙️ Running the Project Locally ##
### 1️⃣ Clone the repository ###
+ git clone https://github.com/your-username/smart-expense-tracker.git
+ cd smart-expense-tracker
### 2️⃣ Configure Environment Variables ###
DB_PASSWORD=your_db_password\
MAIL_USERNAME=your_mailtrap_username\
MAIL_PASSWORD=your_mailtrap_password\
REDIS_HOST=your_redis_host\
REDIS_PASSWORD=your_redis_password\
JWT_SECRET=your_secret_key
### 3️⃣ Run the Application ###

1. Using Gradle: ./gradlew bootRun

2. using the JAR: java -jar build/libs/smartExpenseTracker.jar
## 📊 API Documentation ##

Swagger UI is available at: (http://localhost:8080/swagger-ui/index.html)
## 🔮 Future Improvements ##

+ Docker containerization

+ CI/CD pipeline using GitHub Actions

+ Frontend dashboard (React)

+ AI-based expense insights

+ Multi-user organization accounts

## 📚 Learning Outcomes ##

This project demonstrates practical experience with:

+ Building secure REST APIs

+ Backend system design with Spring Boot

+ Redis caching strategies

+ Cloud deployment using AWS

+ Scheduled background jobs

+ API documentation and testing

## 👨‍💻 Author ##

Rishabh Dubey
