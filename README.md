# Review-Service - Quản Lý Đánh Giá & Báo Cáo

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)](https://github.com/shopping-ecommerce/review-service/actions) [![Coverage](https://img.shields.io/badge/coverage-95%25-brightgreen.svg)](https://codecov.io/gh/shopping-ecommerce/review-service) [![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE) [![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green.svg)](https://spring.io/projects/spring-boot) [![Java](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://openjdk.org/) [![MongoDB](https://img.shields.io/badge/MongoDB-7.x-green.svg)](https://mongodb.com/)

## 📋 Mô Tả
Review-Service là một microservice backend quản lý đánh giá sản phẩm và báo cáo vi phạm cho ứng dụng e-commerce. Xây dựng bằng **Spring Boot 3.x**, sử dụng **MongoDB** làm database chính (review_db), hỗ trợ tạo đánh giá (với rating 1-5, comment, images multipart lên đến 50MB), query đánh giá by productId, xóa đánh giá by product (e.g., khi product bị invalid), và báo cáo sản phẩm (create/update status: PENDING/RESOLVED/REJECTED, admin-only). Tích hợp **Feign** để upload/delete images qua File-Service, và **Kafka** producer cho events (e.g., ProductInvalid).

Dự án tập trung vào bảo mật (JWT cho create/delete, public GET /review/**), validation (rating 1-5), và scalability (indexed fields cho fast query).

### 🏗️ Architecture
Kiến trúc microservices với Review-Service làm core cho user feedback. Các thành phần chính:
- **Communication**: REST API, Feign client (File cho images), Kafka producer (events như ProductInvalid).
- **Database**: MongoDB (reviews/reports collections, indexed productId/userId).
- **Security**: JWT (OAuth2), public GET reviews, PreAuthorize cho admin (reports).
- **Deployment**: Docker + Kubernetes (giả định), port 8088 (context-path: /feedback).
```mermaid
graph TD
    A[Client/App] -->|REST API| B[Review-Service Port 8088]
    B -->|JWT Auth| D[CustomJwtDecoder]
    B -->|Persist Review/Report| E[MongoDB review_db]
    B -->|Upload/Delete Images| F[File-Service Feign Client]
    B -->|Events| G[Kafka ProductInvalid]
    H[Admin] -->|Update Report| B
    
    style B fill:#f9f,stroke:#333,stroke-width:2px
```

## ✨ Tính Năng Chính
- **Review Management**: Tạo review (multipart files, rating/comment), query by productId, delete all by productId (e.g., cleanup).
- **Report Management**: Tạo report (by user on product/reason), admin-only: search pending reports, update status (PENDING → RESOLVED/REJECTED).
- **File Integration**: Upload images (max 50MB) qua Feign to File-Service, delete on product removal.
- **Validation**: Rating 1-5 (@Min/@Max), indexed queries (productId/userId).
- **Events**: Kafka producer cho ProductInvalid (notify invalid product).
- **Security**: Public GET /review/** (no auth), others authenticated; PreAuthorize for admin (reports).
- **Error Handling**: Standardized ApiResponse, GlobalExceptionHandler (AppException, etc.).

## 🛠️ Tech Stack
| Component          | Technology                  | Details                                      |
|--------------------|-----------------------------|----------------------------------------------|
| **Language/Framework** | Java 17+ / Spring Boot 3.x | REST Controllers, MongoDB repos, Validation  |
| **Database**       | MongoDB                     | review_db (Review, Report entities, indexed productId/userId) |
| **Messaging**      | Apache Kafka                | Producer: JsonSerializer; Events: ProductInvalid (productId/reason) |
| **File Handling**  | Multipart/Spring Servlet    | Images upload (max 50MB), Feign to File-Service |
| **Security**       | Spring Security (OAuth2)    | JWT converter (roles/scopes), PreAuthorize (ADMIN for reports) |
| **Client**         | OpenFeign                   | FileClient (upload/delete, timeout 30s)      |
| **Utils**          | Lombok, Jackson             | DTOs (ReviewRequest, ReportRequest), enums (ReportStatus) |

## 🚀 Cài Đặt & Chạy
### Yêu Cầu
- Java 17+ / Maven 3.6+.
- Docker (cho MongoDB, Kafka).
- Environment vars: `SPRING_DATA_MONGODB_URI` (mongodb://root:root@mongodb:27017/review_db), `FEIGN_FILE` (http://file-service:8084/file) (xem application.yml).

### Bước 1: Clone Repo
```bash
git clone https://github.com/shopping-ecommerce/review-service.git
cd review-service
```

### Bước 2: Setup Môi Trường
```bash
# Copy env files (nếu có example)
cp src/main/resources/application.yml.example application.yml

# Build project
mvn clean install

# Setup Docker services (MongoDB, Kafka)
docker-compose up -d  # Sử dụng docker-compose.yml nếu có
```

### Bước 3: Chạy Service
```bash
# Run với Maven
mvn spring-boot:run

# Hoặc JAR
java -jar target/review-service-*.jar
```

- Port mặc định: **8088** (context: /feedback, e.g., http://localhost:8088/feedback/review/{productId}).
- Test endpoints: Sử dụng Postman/Swagger (http://localhost:8088/feedback/swagger-ui.html nếu enable).

Ví dụ test get reviews:
```bash
curl -X GET http://localhost:8088/feedback/review/{productId}
  # Public, no auth needed
```

### Bước 4: Test & Debug
```bash
# Run tests
mvn test

# Check logs
tail -f logs/application.log  # Hoặc console
```

- Public: GET /review/{productId}.
- Auth required: POST /review/create (multipart), DELETE /review/by-product/{productId}, reports.

## 📚 Tài Liệu
- **API Docs**: Sử dụng SpringDoc OpenAPI (Swagger UI tại `/swagger-ui.html`).
- **Endpoints** (base: /feedback):
  | Method | Endpoint                          | Description                  | Auth Required    |
  |--------|-----------------------------------|------------------------------|------------------|
  | POST   | `/review/create`                  | Tạo review (multipart files) | Yes              |
  | GET    | `/review/{productId}`             | Lấy reviews by product       | No               |
  | DELETE | `/review/by-product/{productId}`  | Xóa reviews by product       | Yes              |
  | POST   | `/report/create`                  | Tạo report                   | Yes              |
  | GET    | `/report/searchByStatusPending`   | Tìm reports pending          | Yes (ADMIN)      |
  | POST   | `/report/updateStatus`            | Update report status         | Yes (ADMIN)      |
- **Deployment Guide**: Xem `docs/deploy.md` (Kubernetes manifests cho microservices).
- **Contributing Guide**: Xem `CONTRIBUTING.md`.

## 🤝 Đóng Góp
- Fork repo và tạo PR với branch `feature/[tên-feature]`.
- Tuân thủ code style: Checkstyle, Lombok annotations.
- Test coverage >80% trước merge.
  Pull requests welcome! Báo issue nếu bug hoặc feature request.

## 📄 Giấy Phép
Dự án này được phân phối dưới giấy phép MIT. Xem file [LICENSE](LICENSE) để biết chi tiết.

## 👥 Liên Hệ
- Author: [Hồ Huỳnh Hoài Thịnh] ([@github-hohuynhhoaithinh](https://github.com/hohuynhhoaithinh))
- Email: [hohuynhhoaithinh@gmail.com]

---

*Cảm ơn bạn đã sử dụng Review-Service! 🚀*