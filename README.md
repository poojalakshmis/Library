📚** Library API**
A Spring Boot REST API for managing library resources, built with Maven. Supports multiple environments (dev, uat, prod) and can optionally be containerized with Docker or deployed to Kubernetes.

✨ **Features**
Spring Boot REST API for library management

PostgreSQL database integration

Environment-specific configurations (dev, uat, prod)

Maven build process

Optional Docker containerization & Kubernetes deployment

📂 **Project Structure**
css
Copy
Edit
library-api/
├── pom.xml
├── mvnw
├── mvnw.cmd
├── .mvn/
├── src/main/java/...
│   └── com.collabera.library
│       ├── LibraryApiApplication.java
│       ├── controller/
│       ├── service/
│       ├── repository/
│       ├── entity/
│       ├── dto/
│       └── exception/
├── src/main/resources/
│   ├── application.properties
│   ├── application-dev.properties
│   ├── application-uat.properties
│   └── application-prod.properties
├── Dockerfile
├── docker-compose.yml
├── k8s-deployment.yaml
└── .github/workflows/deploy.yml
🛠 Overview (Chosen Stack)
Language / Framework: Java 17 + Spring Boot (3.x)

Build / Package Manager: Maven (with mvnw wrapper recommended)

Database: PostgreSQL

Containerization (optional): Docker + docker-compose for local dev; Kubernetes YAML for deployment

CI/CD (optional): GitHub Actions pipeline to build, test, push image, and deploy to K8s

**🌍 Environment Profiles**
Spring Boot uses profiles to load environment-specific configs.

application-dev.properties → local development

application-uat.properties → UAT environment

application-prod.properties → production

Set profile when running:


mvn spring-boot:run -Dspring-boot.run.profiles=dev
or:


java -jar target/library-api.jar --spring.profiles.active=dev
📦 Building with Maven
Clean & Build:


mvn clean package
This creates a JAR file in the target/ directory, for example:


target/library-api-0.0.1-SNAPSHOT.jar
▶️ Running Locally
Using Maven:


mvn spring-boot:run -Dspring-boot.run.profiles=dev
Using the Packaged JAR:

java -jar target/library-api-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
📜 API Documentation
Method	Endpoint	Description
POST	/api/borrowers	Create a new borrower
POST	/api/books	Add a new book
GET	/api/books	Get a list of all books
POST	/api/borrowers/{memberId}/borrow/{bookId}	Borrow a book
POST	/api/borrowers/{memberId}/return/{bookId}	Return a borrowed book

🗄 Database Properties

spring.datasource.url=jdbc:postgresql://localhost:5432/librarydb
spring.datasource.username=postgres
spring.datasource.password=yourpassword
🛠 Database Setup (PostgreSQL)
If you want to create manually:


CREATE TABLE book_details (
  id BIGSERIAL PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  author VARCHAR(255),
  isbn VARCHAR(50),
  is_borrowed BOOLEAN NOT NULL DEFAULT FALSE,
  created_at TIMESTAMP DEFAULT now()
);

CREATE TABLE borrower_details (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  created_at TIMESTAMP DEFAULT now()
);

CREATE TABLE borrowing_record (
  id BIGSERIAL PRIMARY KEY,
  book_id BIGINT NOT NULL REFERENCES book_details(id) ON DELETE CASCADE,
  borrower_id BIGINT NOT NULL REFERENCES borrower_details(id) ON DELETE CASCADE,
  borrow_date TIMESTAMP NOT NULL DEFAULT now(),
  return_date TIMESTAMP NULL,
  UNIQUE (book_id, return_date)
);

📡** Endpoints**
swift
Copy
Edit
POST /api/books                      -> create new book (a copy)
GET /api/books                       -> list all books (with optional filters)
GET /api/books/{id}                  -> get book details
POST /api/borrowers                   -> create borrower
GET /api/borrowers/{id}               -> get borrower
POST /api/borrowers/{memberId}/borrow/{bookId} -> borrow a book
POST /api/borrowers/{memberId}/return/{bookId} -> return a book
GET /api/borrowings                   -> list borrow records (optional filters)
✅ Validation
Use DTO classes annotated with:
@Valid, @NotNull, @Email, @Size
❌ Error Handling
Central @ControllerAdvice class mapping exceptions to HTTP codes:

ResourceNotFoundException → 404

BadRequestException → 400

ConflictException → 409

🐳 Optional: Docker Deployment
Build Image:
docker build -t library-api:latest .
Run Container:
docker run -p 8080:8080 library-api:latest
☸ Optional: Kubernetes Deployment
Apply manifests:
kubectl apply -f k8s-deployment.yaml
⚙ Tech Stack
Java 17
Spring Boot
Maven
PostgreSQL

(Optional) Docker, Kubernetes

📖 Database Choice — PostgreSQL
Reliable, battle-tested relational DB suitable for transactional workloads.
Good JDBC / JPA support in Spring Boot.
Supports row-level locking (pessimistic locking) if we need concurrency control.
Easy to run locally (docker image) and in K8s with PVCs.
🔐** Key Concurrency & Integrity Rules**
Goal: Ensure that no more than one member is borrowing the same book (same book id) at a time.
Approach (recommended): Pessimistic locking + transactional checks at the service layer.
Service flow for borrow(bookId, memberId):
@Transactional
@Lock(LockModeType.PESSIMISTIC_WRITE)
Optional<Book> findById(Long id);

if (book.isBorrowed()) {
    throw new ConflictException("Book already borrowed");
} else {
    create BorrowingRecord with borrow_date=now(), return_date=null;
    set book.isBorrowed=true;
    save both;
}
🏗 How to Build & Run
Build:
mvn clean package
Run (dev):

mvn spring-boot:run -Dspring-boot.run.profiles=dev
Run packaged:

java -jar target/library-api-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
🧪 Example curl commands
Create borrower:

curl -X POST -H "Content-Type: application/json" \
-d '{"name":"Donald trump","email":"trump@usa.com"}' \
http://localhost:8081/api/borrowers
Add book:

curl -X POST -H "Content-Type: application/json" \
-d '{"title":"1984","author":"George Orwell","isbn":"978-0451524935"}' \
http://localhost:8081/api/books
Borrow:

curl -X POST http://localhost:8081/api/borrowers/1/borrow/2
Return:

curl -X POST http://localhost:8081/api/borrowers/1/return/2
List books:

curl http://localhost:8081/api/books**
📌 Assumptions**
Book copies: Each book_details row represents a physical copy; multiple copies may have the same ISBN but different id.

Single current borrower per book id: Enforced via DB constraints & service logic.

Borrow lifecycle: Open when return_date is null; closing sets return_date.

Required fields: Title for book; name + email for borrower.

Email uniqueness: Borrower emails must be unique.

Time zone: Server uses UTC.

Authorization: Not implemented.

Max borrow duration: Not enforced.

12Factor: Tried as much as possible — configs in properties, artifacts via CI, DB persistence.
