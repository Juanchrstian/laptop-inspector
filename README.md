# 💻 Laptop Inspector

A desktop and web-based laptop inspection system developed to help IT administrators monitor laptop specifications, track device conditions, and compare hardware changes before and after employee usage.

This project was developed as my undergraduate thesis at **Universitas Kristen Krida Wacana (UKRIDA)** in collaboration with **Crowe Indonesia**.

---

## 📌 Overview

Laptop Inspector is designed to improve IT asset management by automating laptop inspection processes.

Instead of manually recording laptop specifications, the application automatically detects hardware and system information using the **OSHI (Operating System and Hardware Information)** library and stores the inspection results in a centralized database.

The system also compares laptop conditions between borrowing and returning, allowing IT staff to identify hardware changes and support preventive maintenance.

---

## ✨ Features

### Desktop Application (JavaFX)

- Automatic hardware detection
- Detect CPU information
- Detect GPU information
- Detect RAM capacity
- Detect Storage usage
- Detect Battery Health
- Detect Operating System
- Detect Serial Number
- Detect Device Model
- Loan inspection (Before)
- Return inspection (After)
- Generate handover documents
- Send inspection data to backend API

---

### Web Dashboard (Spring Boot)

- Device inventory management
- Inspection history
- Before vs After comparison
- Condition change visualization
- Search and filtering
- Export inspection reports
- Dashboard statistics

---

## 🏗️ System Architecture

```
+-------------------+
|   JavaFX Desktop  |
|-------------------|
| OSHI Detection    |
| Inspection Form   |
+---------+---------+
          |
      REST API
          |
          ▼
+-------------------+
| Spring Boot API   |
|-------------------|
| Business Logic    |
| Report Service    |
| Comparison Engine |
+---------+---------+
          |
          ▼
      MySQL Database
          |
          ▼
    Web Dashboard
```

---

## 🛠️ Technologies

### Frontend (Desktop)

- Java 17
- JavaFX
- Maven
- OSHI

### Backend

- Spring Boot
- Spring MVC
- Spring Data JPA
- Hibernate
- REST API

### Database

- MySQL

### Libraries

- Jackson
- SLF4J
- Apache POI
- PDF Generator

---

## 📂 Project Structure

```
laptop-inspector
│
├── backend/
│   ├── src/
│   ├── pom.xml
│   └── ...
│
├── laptopinspector/
│   ├── src/
│   ├── pom.xml
│   └── ...
│
└── README.md
```

---

## ⚙️ Installation

### Clone Repository

```bash
git clone https://github.com/Juanchrstian/laptop-inspector.git
```

---

### Backend

```bash
cd backend

mvn spring-boot:run
```

Backend will start at

```
http://localhost:8080
```

---

### Desktop Application

```bash
cd laptopinspector

mvn javafx:run
```

---

## 📸 Screenshots

You can add screenshots here later.

Example:

```
docs/

dashboard.png

inspection-form.png

comparison-report.png
```

---

## 🔄 Inspection Workflow

```
Borrow Laptop
      │
      ▼
Initial Inspection
      │
      ▼
Hardware Detection
      │
      ▼
Save to Database
      │
      ▼
Employee Uses Laptop
      │
      ▼
Return Inspection
      │
      ▼
Compare Results
      │
      ▼
Generate Report
```

---

## 🎯 Purpose

The application aims to:

- Improve IT asset management
- Reduce manual inspection
- Track hardware changes
- Maintain inspection history
- Support preventive maintenance
- Extend laptop lifecycle

---

## 📖 Research

This application was developed as part of my undergraduate thesis:

**Design and Development of a Laptop Specification Detection and Comparison Application at Crowe Indonesia**

Methodology:

- Extreme Programming (XP)

---

## 🚀 Future Improvements

- QR Code support
- Email notification
- Multi-company support
- Docker deployment
- CI/CD Pipeline
- Authentication & Authorization
- Asset maintenance scheduling

---

## 👨‍💻 Author

**Juan Christian**

Information Systems Student  
Universitas Kristen Krida Wacana

GitHub:
https://github.com/Juanchrstian

LinkedIn:
https://linkedin.com/in/juan-christian-jc

---

## 📄 License

This project is intended for educational and research purposes.
