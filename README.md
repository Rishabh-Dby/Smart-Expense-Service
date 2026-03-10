# Smart Expense Tracker 💰 #

A production-style backend expense management system built using Java and Spring Boot that helps users track expenses, analyze spending patterns, and receive alerts when their monthly budgets are exceeded.

The project demonstrates real-world backend engineering concepts such as JWT authentication, Redis caching, background jobs, and cloud deployment on AWS.
## 🎥 Demo ##

[Demo](https://pages.github.com/)

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
<img width="1536" height="1024" alt="ChatGPT Image Mar 11, 2026, 02_31_30 AM" src="https://github.com/user-attachments/assets/024a6cd9-ecfd-4704-ab14-e59706d51c08" />


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

## ⚙️ Running the Project Locally ##
### 1️⃣ Clone the repository ###
+ git clone https://github.com/your-username/smart-expense-tracker.git
+ cd smart-expense-tracker
### 2️⃣ Configure Environment Variables ###
&emsp;DB_PASSWORD=your_db_password\
&emsp;MAIL_USERNAME=your_mailtrap_username\
&emsp;MAIL_PASSWORD=your_mailtrap_password\
&emsp;REDIS_HOST=your_redis_host\
&emsp;REDIS_PASSWORD=your_redis_password\
&emsp;JWT_SECRET=your_secret_key
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
