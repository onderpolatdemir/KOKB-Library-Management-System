# 📚 Library Management System (LMS) - Android App

A role-based library management mobile application built using modern Android development tools and best practices. This project simulates a real-world library system with Admin, Librarian, and Reader roles, enabling users to perform actions based on their permissions.

## 🚀 Features

### 👤 Admin
- View all registered users
- Assign roles: Librarian or Reader

### 📘 Librarian
- View list of all books
- Add new books
- Update book details
- Delete books
- Check book status (Active / Borrowed)

### 📖 Reader
- Search for books
- Borrow books
- Return borrowed books

## 🛠️ Technologies Used

### Frontend
- **Jetpack Compose** – Declarative UI development
- **Material 3 Design** – Clean and modern UI components
- **Coil** – Image loading and caching
- **Navigation Component** – Type-safe in-app navigation

### Architecture
- **MVVM Pattern** – Separation of UI and business logic
- **Clean Architecture** – Modular and scalable structure
- **Dagger Hilt** – Dependency injection for modularity and testing

### Backend Integration
- **Retrofit2** – REST API communication
- **OkHttp** – Network layer with logging
- **GSON** – JSON serialization/deserialization
- **Kotlin Coroutines** – Asynchronous operations

### Development & Tools
- **Kotlin** – Main programming language
- **Gradle (Kotlin DSL)** – Build configuration
- **JUnit / Espresso** – Unit and UI testing
- **Git** – Version control

> ⚠️ Note: Backend implementation is not included, but the app is fully integrated to communicate with RESTful APIs.


## 📷 Screenshots

| Login Screen | Register |   |
|:------------:|:--------:|:-----------:|
| <img src="https://github.com/user-attachments/assets/67af1f44-e718-4b64-91af-183da5326a21" width="250"/> | <img src="https://github.com/user-attachments/assets/69736a10-ce01-414b-a4c3-44baa46079d3" width="250"/> | <img width="250"/> |

| Admin Home | Admin Search | Admin Role Change |
|:----------:|:------------:|:-----------------:|
| <img src="https://github.com/user-attachments/assets/7886e2bf-f07c-4241-b0f3-173401ea488e" width="250"/> | <img src="https://github.com/user-attachments/assets/f9c50049-3a5a-4bab-8270-5d6e228d5574" width="250"/> | <img src="https://github.com/user-attachments/assets/2be7a405-5fb0-432b-af6c-246603b14323" width="250"/> |

| Admin Role Change | Librarian Home | Librarian Loan Available |
|:-----------------:|:--------------:|:-------------------------:|
| <img src="https://github.com/user-attachments/assets/dab532cd-0dc9-4356-9ad6-0f62c3cf7144" width="250"/> | <img src="https://github.com/user-attachments/assets/3ce38e11-587d-434a-91d4-0a230e51cb24" width="250"/> | <img src="https://github.com/user-attachments/assets/c741bca4-5bcc-49af-932c-013d1ac6d39e" width="250"/> |

| Librarian Loan Reserved | Librarian Cataloging | Librarian Add Book |
|:----------------------:|:--------------------:|:-------------------:|
| <img src="https://github.com/user-attachments/assets/8a12c39d-a46f-475d-867e-009b9dc62205" width="250"/> | <img src="https://github.com/user-attachments/assets/5d51ef08-6d2b-432c-8329-bab942c9c74e" width="250"/> | <img src="https://github.com/user-attachments/assets/ccec5b38-baa0-4fb9-a20e-831925f030d7" width="250"/> |

| Librarian Add Book Success | Librarian Cataloging Search | Librarian Delete Book |
|:--------------------------:|:---------------------------:|:----------------------:|
| <img src="https://github.com/user-attachments/assets/a241065b-76af-4eb9-aeaa-ebb7ce42adee" width="250"/> | <img src="https://github.com/user-attachments/assets/e4be3fb7-6608-4264-b250-8c029a729c00" width="250"/> | <img src="https://github.com/user-attachments/assets/9d0fdfba-3570-48a2-ad46-a0d39277c2e2" width="250"/> |

| Librarian Members | Reader Home | Reader Search |
|:-----------------:|:-----------:|:-------------:|
| <img src="https://github.com/user-attachments/assets/e823d436-8bda-4640-8410-680f049c66f8" width="250"/> | <img src="https://github.com/user-attachments/assets/df6dc0a5-c68a-46f2-8cbd-087f9a881800" width="250"/> | <img src="https://github.com/user-attachments/assets/c10c734b-0bb4-4924-8331-030c7fc465e7" width="250"/> |

| Reader Search Results | Reader My Books Active | Reader My Books History |
|:---------------------:|:----------------------:|:------------------------:|
| <img src="https://github.com/user-attachments/assets/1e4bd734-c66f-4148-9d20-9a8f7a20b85a" width="250"/> | <img src="https://github.com/user-attachments/assets/bda24e25-86ad-4e42-9a12-9d4a7d18ec88" width="250"/> | <img src="https://github.com/user-attachments/assets/de8bc2e5-2ed8-467b-b353-757234a3c2f0" width="250"/> |

| Reader Book Return |  |  |
|:------------------:|:-----------:|:-----------:|
| <img src="https://github.com/user-attachments/assets/0a21d132-56a4-48e1-9e5c-9baf87ca2941" width="250"/> | <img width="250"/> | <img width="250"/> |




## 📂 Project Structure

```plaintext
📁 presentation
   └── UI components, ViewModels, and navigation
📁 domain
   └── Use cases, models, and repositories (interfaces)
📁 data
   └── API services, DTOs, repository implementations
📁 di
   └── Dagger Hilt modules
📁 utils
   └── Helpers and constants
