# 🏫 School Equipment Lending Portal

A full-stack web application designed to simplify and automate the process of borrowing, approving, and returning school equipment.  
The system ensures efficient management of resources and transparency between **Admin, Staff, and Students**.  
Built using **Java Servlets (Backend)** and **React.js (Frontend)** following the **MVC architecture**.

---

## 🚀 Features

### 👤 Role-Based Access Control
- **Admin**
  - Manage equipment, equipment categories and availability types.
  - Add, edit, and delete equipment records.
  - Approve or reject or close borrowing requests from students.
  - Track current and returned items.

- **Staff**
  - View all equipments details.
  - Track request status (Pending, Approved, Rejected).
  - Approve or reject or close borrowing requests from students.
  - View borrowing history.

- **Student**
  - Browse and request available equipment.
  - View all available equipments details.
  - Receive real-time updates on request approvals or rejections.
  - Return borrowed equipment after use.
  - Can cancel pending requests.

---

### 🧾 Equipment Management
- Add new equipment with details such as name, category, and availability type.  
- Update or delete equipment records.
- Manage equipment availability dynamically (available/unavailable).
- Prevent booking of the same equipment if quantity not available to lent out.

---

### 🔁 Lending Process
- Students send borrowing requests for specific equipment.
- Admin and Staff reviews requests and can **approve** or **reject** them.
- Approved requests mark the equipment as **“Issued”**.
- Upon return, Admin updates status to **“Returned”** and marks equipment available again.

---

### 📊 Inventory & Tracking
- Maintain a centralized record of all equipment with statuses:
  - Available
  - Issued
  - Returned
  - Damaged
- Track borrowing history by user or equipment.

---

### 🔐 Authentication & Authorization
- Secure login for Admin, Staff, and Students using sessions.
- Protected routes and role-based access for sensitive APIs.
- Used BCrypt for password hashing and salting.

---

### 🌐 RESTful API Design
- Backend APIs built using Java Servlets and JSON for smooth frontend-backend communication.
- Standard CRUD operations for managing categories, equipment, and lending requests.
- Data exchange via Axios and Fetch API.

---

### 💻 Responsive Frontend
- Built using **React.js** with modern hooks and reusable components.

---

## 🧰 Tech Stack

**Frontend:** React.js, JavaScript, HTML, CSS  
**Backend:** Java Servlets (Jakarta EE), JDBC, Java  
**Database:** PostgreSQL  
**Server:** Apache Tomcat  
**Other Tools:** Postman, DBeaver, Git, VS Code, Eclipse  

---