# 🪨 Quarry Management System - Batch Processing Analysis

A Spring Boot web application for quarry management, developed as part of a Master's thesis comparing manual JDBC batch processing against the Spring Batch framework.

---

## 📌 Overview

This project implements a complete quarry management system and serves as the practical foundation for a comparative performance analysis of two batch processing approaches:

- **Manual JDBC implementation** - batch logic implemented directly using `JdbcTemplate` within a standard three-tier architecture
- **Spring Batch implementation** - the same four batch processes re-implemented using the Spring Batch framework (Job/Step/ItemReader/ItemProcessor/ItemWriter)

Both implementations are applied to identical datasets and the same PostgreSQL database, enabling direct and fair performance comparison across execution time, memory usage, and GC behavior.

---

## 🎓 Academic Context

**Thesis title:** Application of the Spring Batch Library in Large-Scale Data Processing  
**Author:** Marija Simović  
**Program:** Master's in Software Engineering and Artificial Intelligence  
**Institution:** University of Belgrade, Faculty of Organizational Sciences  
**Mentor:** Prof. Dr. Siniša Vlajić

---

## 🚀 Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5.13 |
| Batch | Spring Batch |
| Database access | Spring JDBC / JdbcTemplate |
| Database | PostgreSQL 18.1 |
| Frontend | HTML / CSS / JavaScript |
| Monitoring | Spring Boot Actuator, VisualVM 2.2.1 |
| Build tool | Maven |
| IDE | IntelliJ IDEA |

---

## ⚙️ Batch Processes

Four batch processes are implemented both manually and using Spring Batch:

| # | Name | Type | Description |
|---|---|---|---|
| 1 | Invoice Amount Reconciliation | UPDATE | Recalculates and corrects invoice totals based on invoice item prices |
| 2 | Block Classification | UPDATE | Assigns volume-based categories to quarry blocks (category 1/2/3) |
| 3 | Monthly Invoice Report | AGGREGATION | Groups invoices by month/year and calculates revenue metrics |
| 4 | Invoice Archiving | DELETE | Moves invoices older than 2 years to archive tables |

---

## 🗄 Database

The application uses **PostgreSQL**. The following tables are used:

**Core tables:**
- `users` - system users
- `buyer` - individual and company buyers
- `block` - quarry blocks with dimensions and quality class
- `invoice` - invoices
- `invoice_item` - invoice line items
- `position` - job positions

**Batch-created tables:**
- `monthly_report` - created by Batch 3
- `invoice_archive` / `invoice_item_archive` - created by Batch 4
- `manual_batch_execution` / `manual_batch_execution_context` - overhead simulation for manual implementation
- `BATCH_*` - Spring Batch meta-tables (auto-created)

**Setup:**

1. Create a PostgreSQL database named `quarry_db`
2. Update credentials in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/quarry_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. Tables are created automatically on first run - no SQL script needed.

---

## 🧩 Features

**Application:**
- ✔ Full CRUD for invoices, buyers, blocks, users and positions
- ✔ Search and filter functionality
- ✔ Automatic price calculation based on block quality class and category
- ✔ User authentication via localStorage session
- ✔ Responsive dark-themed UI

**Batch processing:**
- ✔ Manual JDBC batch implementation (BatchRepository / ManualBatchService)
- ✔ Spring Batch implementation (Job / Step / Reader / Processor / Writer)
- ✔ Overhead simulation on manual side (manual_batch_execution tables)
- ✔ Test data generator with intentionally corrupted data for batch verification
- ✔ Execution time and records processed displayed after each batch run

**Performance monitoring:**
- ✔ Custom `/memory-peak` endpoint (MemoryPeakTracker component)
- ✔ Spring Boot Actuator endpoints for GC metrics
- ✔ Spring Batch meta-table analysis via DBeaver

---

## 📁 Project Structure

```
quarry-batch/
│
├── src/
│   ├── main/
│   │   ├── java/com/marija/quarry_batch/
│   │   │   ├── batch/
│   │   │   │   ├── config/       # SpringBatchConfig - all Job/Step definitions
│   │   │   │   ├── job1/         # Invoice reconciliation - Reader, Processor, Writer
│   │   │   │   ├── job2/         # Block classification - Reader, Processor, Writer
│   │   │   │   ├── job3/         # Monthly report - Reader, Processor, Writer + DTO
│   │   │   │   └── job4/         # Invoice archiving - Reader, Processor, Writer + DTO
│   │   │   ├── controller/       # REST controllers
│   │   │   ├── model/            # Domain models
│   │   │   ├── repository/       # JdbcTemplate repositories
│   │   │   ├── service/          # Business logic services
│   │   │   └── util/             # DataGenerator, MemoryPeakTracker
│   │   └── resources/
│   │       ├── static/           # Frontend (HTML/CSS/JS)
│   │       │   ├── index.html
│   │       │   ├── invoices.html
│   │       │   ├── buyers.html
│   │       │   ├── blocks.html
│   │       │   ├── positions.html
│   │       │   ├── data-generator.html
│   │       │   └── batch.html
│   │       └── application.properties
└── pom.xml
```

---

## 🔌 Key API Endpoints

| Method | URL | Description |
|---|---|---|
| POST | `/batch/manual/1` | Run Batch 1 manually |
| POST | `/batch/manual/2` | Run Batch 2 manually |
| POST | `/batch/manual/3` | Run Batch 3 manually |
| POST | `/batch/manual/4` | Run Batch 4 manually |
| POST | `/batch/spring/1` | Run Batch 1 with Spring Batch |
| POST | `/batch/spring/2` | Run Batch 2 with Spring Batch |
| POST | `/batch/spring/3` | Run Batch 3 with Spring Batch |
| POST | `/batch/spring/4` | Run Batch 4 with Spring Batch |
| POST | `/generate/buyers?count=N` | Generate N buyers |
| POST | `/generate/blocks?count=N` | Generate N blocks |
| POST | `/generate/invoices?count=N` | Generate N invoices |
| DELETE | `/generate/clear` | Clear all data |
| POST | `/generate/memory-peak/reset` | Reset heap peak tracker |
| GET | `/generate/memory-peak` | Get current heap peak (MB) |

---

## 📊 Test Dataset Sizes

| Size | Buyers | Blocks | Invoices | Invoice items |
|---|---|---|---|---|
| S (small) | 200 | 5,000 | 1,000 | ~4,000 |
| M (medium) | 500 | 20,000 | 10,000 | ~40,000 |
| L (large) | 1,000 | 50,000 | 50,000 | ~200,000 |
| XL (extra large) | 2,000 | 100,000 | 100,000 | ~400,000 |

Test data includes **intentionally corrupted records** to ensure batch processes have data to correct:
- Every 5th invoice has an incorrect `totalAmount` (multiplied by 0.85) - for Batch 1
- Block categories are assigned randomly - for Batch 2
- Invoice dates are randomized within the last 3 years - for Batch 4

---

## 👩‍💻 Author

**Marija Simović**  
Master's student - Software Engineering & Artificial Intelligence  
University of Belgrade, Faculty of Organizational Sciences

*Feel free to explore the project!*