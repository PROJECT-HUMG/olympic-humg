# Olympic Learning Platform — Phase 5 API Design

## 1. Document Information

| Field                 | Value                                                                         |
| --------------------- | ----------------------------------------------------------------------------- |
| Project name          | Olympic Learning Platform                                                     |
| Phase                 | Phase 5 — API Design                                                          |
| Status                | Final baseline                                                                |
| Based on              | Phase 0, Phase 1, Phase 2, Phase 3, Phase 4                                   |
| Purpose               | Thiết kế REST API contract để client giao tiếp với backend một cách nhất quán |
| Main question         | Client giao tiếp với hệ thống như thế nào?                                    |
| API style             | REST API                                                                      |
| API contract standard | OpenAPI 3.1                                                                   |
| Error format          | RFC 9457 Problem Details                                                      |

---

## 2. Phase 5 Goal

Phase 5 trả lời câu hỏi:

> Frontend hoặc client sẽ gọi backend qua những API nào, request/response ra sao, lỗi trả về thế nào, phân trang/lọc/sắp xếp theo chuẩn nào, và OpenAPI contract được tổ chức như thế nào?

Các artifact trong Phase 5:

```text id="9u9qbr"
5.1 API Design Principles
5.2 Resource Model
5.3 Endpoint List
5.4 Request/Response Schema
5.5 Error Response Standard
5.6 Pagination/Filtering/Sorting Convention
5.7 OpenAPI Specification
```

---

## 3. API Design Scope

Phase 5 tập trung vào MVP Core và chuẩn bị một phần MVP Extended.

### 3.1. MVP Core API Scope

API Core gồm:

* Authentication API
* User / Current profile API
* Learning API
* Question Bank API
* Exam / Practice API
* Attempt / Submission API
* Auto Grading API
* Manual Essay Grading API
* Result API

### 3.2. MVP Extended API Scope

API Extended gồm:

* Excel Import API
* Contribution Workflow API
* Attachment API
* Audit Log API
* Screening Advanced API

Baseline module/API mapping:

```text id="gj8x1a"
identity -> /auth, /me, /users, /roles, /permissions
learning -> /subjects, /topics
question -> /questions
assessment -> /exams
attempt -> /attempts and /exams/{examId}/attempts
grading -> /grading
result -> /results
file -> /attachments
import -> /imports
contribution -> /contributions
audit -> /audit-logs
```

Screening trong MVP dùng chung `/exams` với `examType = SCREENING`.
Khi screening cần participant list, ranking, export hoặc BTC dashboard riêng, bổ sung Screening Advanced API.

---

# 4. API Design Principles

## 4.1. General Principles

| ID         | Principle                                              | Explanation                                                                                           |
| ---------- | ------------------------------------------------------ | ----------------------------------------------------------------------------------------------------- |
| API-PR-001 | API sinh ra từ use case, không sinh tùy tiện từ entity | Endpoint phải phục vụ flow thật như làm bài, submit, chấm tự luận, xem kết quả.                       |
| API-PR-002 | Resource-oriented URI                                  | URI nên biểu diễn resource chính như `/subjects`, `/questions`, `/exams`.                             |
| API-PR-003 | Use HTTP methods correctly                             | `GET` để đọc, `POST` để tạo/action, `PUT/PATCH` để cập nhật, `DELETE` để xóa/disable/archive nếu cần. |
| API-PR-004 | JSON request/response                                  | Client và backend giao tiếp bằng JSON.                                                                |
| API-PR-005 | Consistent response structure                          | Response thành công và lỗi phải có format thống nhất.                                                 |
| API-PR-006 | Problem Details for error                              | Error response dùng RFC 9457 Problem Details.                                                         |
| API-PR-007 | Secure by default                                      | API cần authentication/authorization nếu không phải public endpoint.                                  |
| API-PR-008 | Pagination for list API                                | API trả danh sách phải hỗ trợ phân trang.                                                             |
| API-PR-009 | Filtering and sorting should be explicit               | Lọc/sắp xếp dùng query params rõ ràng.                                                                |
| API-PR-010 | OpenAPI documented direction                           | API cần được document bằng OpenAPI 3.1 để frontend/backend thống nhất contract.                       |

---

## 4.2. Base URL

```text id="s8uxem"
Local:   http://localhost:8080/api/v1
Staging: https://staging.olympic-platform.example.com/api/v1
Prod:    https://olympic-platform.example.com/api/v1
```

Baseline dùng version prefix:

```text id="25m6db"
/api/v1
```

---

## 4.3. Authentication Header

Client gửi access token qua header:

```http id="gk5nwx"
Authorization: Bearer <access_token>
```

---

# 5. Resource Model

## 5.1. Main API Resources

| Resource      | URI              | Description                               |
| ------------- | ---------------- | ----------------------------------------- |
| Auth          | `/auth`          | Đăng ký, đăng nhập, refresh token, logout |
| Current User  | `/me`            | Thông tin user hiện tại                   |
| Users         | `/users`         | Quản lý user                              |
| Roles         | `/roles`         | Quản lý role                              |
| Permissions   | `/permissions`   | Quản lý permission                        |
| Subjects      | `/subjects`      | Quản lý môn học                           |
| Topics        | `/topics`        | Quản lý chuyên đề                         |
| Questions     | `/questions`     | Quản lý ngân hàng câu hỏi                 |
| Exams         | `/exams`         | Quản lý bài luyện tập/sàng lọc            |
| Attempts      | `/attempts`      | Lượt làm bài                              |
| Results       | `/results`       | Kết quả bài làm                           |
| Grading       | `/grading`       | Chấm tự luận                              |
| Attachments   | `/attachments`   | Upload file/ảnh                           |
| Contributions | `/contributions` | Luồng đóng góp nội dung                   |
| Imports       | `/imports`       | Import Excel                              |
| Audit Logs    | `/audit-logs`    | Nhật ký thao tác quan trọng               |

---

## 5.2. Important Resource Relationship

```text id="6fucfb"
Subject has many Topics
Topic has many Questions
Exam has many ExamQuestions
Exam has many Attempts
Attempt has many AttemptAnswers
Attempt has one Result
Question has many QuestionOptions
Essay AttemptAnswer may have EssayGrading
```

---

# 6. Common API Conventions

## 6.1. Date Time Format

Datetime dùng ISO 8601:

```text id="3b5te7"
2026-06-03T10:15:30+07:00
```

---

## 6.2. ID Format

Tất cả resource ID dùng UUID:

```text id="qav4ji"
550e8400-e29b-41d4-a716-446655440000
```

---

## 6.3. Common Success Response

Với single object:

```json id="kyg3r8"
{
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Mathematics"
  }
}
```

Với list:

```json id="uu5vn0"
{
  "data": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "name": "Mathematics"
    }
  ],
  "page": {
    "number": 0,
    "size": 20,
    "totalElements": 100,
    "totalPages": 5
  }
}
```

---

## 6.4. HTTP Status Code Convention

| Status Code               | Meaning                                    | Usage                             |
| ------------------------- | ------------------------------------------ | --------------------------------- |
| 200 OK                    | Thành công                                 | GET, PATCH, action trả data       |
| 201 Created               | Tạo thành công                             | POST tạo resource                 |
| 204 No Content            | Thành công nhưng không trả body            | DELETE/archive, logout            |
| 400 Bad Request           | Request sai format hoặc validation lỗi     | Thiếu field, sai enum             |
| 401 Unauthorized          | Chưa đăng nhập hoặc token sai              | Missing/invalid JWT               |
| 403 Forbidden             | Không có quyền                             | Student gọi API của teacher/admin |
| 404 Not Found             | Không tìm thấy resource                    | ID không tồn tại                  |
| 409 Conflict              | Xung đột trạng thái/dữ liệu                | Submit attempt đã submit          |
| 422 Unprocessable Entity  | Request hợp lệ về JSON nhưng sai nghiệp vụ | Publish exam không có question    |
| 500 Internal Server Error | Lỗi server                                 | Unexpected error                  |

---

# 7. Error Response Standard

Error response bám RFC 9457 Problem Details.

## 7.1. Base Error Format

```json id="e61m4r"
{
  "type": "https://api.olympic-platform.example.com/problems/validation-error",
  "title": "Validation Error",
  "status": 400,
  "detail": "Request body contains invalid fields.",
  "instance": "/api/v1/questions",
  "code": "VALIDATION_ERROR",
  "errors": [
    {
      "field": "content",
      "message": "Question content must not be blank."
    }
  ],
  "traceId": "01J8ZK7Q3W3A5Y9V1KX2"
}
```

---

## 7.2. Error Fields

| Field    | Required | Description               |
| -------- | -------- | ------------------------- |
| type     | Yes      | URI định danh loại lỗi    |
| title    | Yes      | Tên ngắn của lỗi          |
| status   | Yes      | HTTP status code          |
| detail   | Yes      | Mô tả lỗi dễ hiểu         |
| instance | Yes      | API path gây lỗi          |
| code     | Yes      | Mã lỗi nội bộ             |
| errors   | No       | Danh sách lỗi field-level |
| traceId  | Should   | ID để trace log/debug     |

---

## 7.3. Common Error Codes

| Code                    | HTTP Status | Meaning                                |
| ----------------------- | ----------- | -------------------------------------- |
| VALIDATION_ERROR        | 400         | Request không hợp lệ                   |
| UNAUTHORIZED            | 401         | Chưa đăng nhập hoặc token không hợp lệ |
| FORBIDDEN               | 403         | Không có quyền                         |
| RESOURCE_NOT_FOUND      | 404         | Không tìm thấy resource                |
| RESOURCE_CONFLICT       | 409         | Xung đột dữ liệu/trạng thái            |
| BUSINESS_RULE_VIOLATION | 422         | Vi phạm rule nghiệp vụ                 |
| INTERNAL_ERROR          | 500         | Lỗi hệ thống                           |

---

# 8. Pagination / Filtering / Sorting Convention

## 8.1. Pagination

List API dùng query params:

```text id="z5vfuc"
page=0
size=20
```

Example:

```http id="6ttqav"
GET /api/v1/questions?page=0&size=20
```

Response:

```json id="ot91jw"
{
  "data": [],
  "page": {
    "number": 0,
    "size": 20,
    "totalElements": 0,
    "totalPages": 0
  }
}
```

Default:

```text id="rqqlsx"
page = 0
size = 20
max size = 100
```

---

## 8.2. Sorting

Sorting dùng query param:

```text id="70svq6"
sort=createdAt,desc
sort=name,asc
```

Example:

```http id="ddqqpa"
GET /api/v1/questions?sort=createdAt,desc
```

---

## 8.3. Filtering

Filtering dùng query params rõ nghĩa:

```http id="2htvvx"
GET /api/v1/questions?subjectId={subjectId}&topicId={topicId}&questionType=SINGLE_CHOICE&status=PUBLISHED
```

Common filters:

| Resource  | Filters                                               |
| --------- | ----------------------------------------------------- |
| subjects  | status, keyword                                       |
| topics    | subjectId, status, keyword                            |
| questions | subjectId, topicId, questionType, status, difficulty, keyword |
| exams     | subjectId, examType, status, keyword                  |
| attempts  | examId, studentId, status                             |
| results   | examId, studentId, gradingStatus                      |

---

# 9. Endpoint List

## 9.1. Authentication API

| Method | Endpoint         | Actor                         | Description          | Success |
| ------ | ---------------- | ----------------------------- | -------------------- | ------- |
| POST   | `/auth/register` | Public                        | Đăng ký tài khoản    | 201     |
| POST   | `/auth/login`    | Public                        | Đăng nhập            | 200     |
| POST   | `/auth/refresh`  | Authenticated / refresh token | Refresh access token | 200     |
| POST   | `/auth/logout`   | Authenticated                 | Đăng xuất            | 204     |

---

## 9.2. Current User API

| Method | Endpoint       | Actor         | Description                  | Success |
| ------ | -------------- | ------------- | ---------------------------- | ------- |
| GET    | `/me`          | Authenticated | Xem thông tin user hiện tại  | 200     |
| GET    | `/me/attempts` | Student       | Xem lịch sử làm bài của mình | 200     |
| GET    | `/me/results`  | Student       | Xem kết quả của mình         | 200     |

---

## 9.3. User / Role / Permission API

| Method | Endpoint                           | Actor | Description              | Success |
| ------ | ---------------------------------- | ----- | ------------------------ | ------- |
| GET    | `/users`                           | Admin | Xem danh sách user       | 200     |
| GET    | `/users/{userId}`                  | Admin | Xem chi tiết user        | 200     |
| PATCH  | `/users/{userId}`                  | Admin | Cập nhật user            | 200     |
| POST   | `/users/{userId}/roles`            | Admin | Gán role cho user        | 200     |
| DELETE | `/users/{userId}/roles/{roleCode}` | Admin | Gỡ role khỏi user        | 204     |
| GET    | `/roles`                           | Admin | Xem danh sách role       | 200     |
| GET    | `/permissions`                     | Admin | Xem danh sách permission | 200     |

---

## 9.4. Subject API

| Method | Endpoint                        | Actor         | Description           | Success |
| ------ | ------------------------------- | ------------- | --------------------- | ------- |
| GET    | `/subjects`                     | Authenticated | Xem danh sách subject | 200     |
| POST   | `/subjects`                     | Teacher/Admin | Tạo subject           | 201     |
| GET    | `/subjects/{subjectId}`         | Authenticated | Xem chi tiết subject  | 200     |
| PATCH  | `/subjects/{subjectId}`         | Teacher/Admin | Cập nhật subject      | 200     |
| POST   | `/subjects/{subjectId}/archive` | Teacher/Admin | Archive subject       | 204     |

---

## 9.5. Topic API

| Method | Endpoint                    | Actor         | Description         | Success |
| ------ | --------------------------- | ------------- | ------------------- | ------- |
| GET    | `/topics`                   | Authenticated | Xem danh sách topic | 200     |
| POST   | `/topics`                   | Teacher/Admin | Tạo topic           | 201     |
| GET    | `/topics/{topicId}`         | Authenticated | Xem chi tiết topic  | 200     |
| PATCH  | `/topics/{topicId}`         | Teacher/Admin | Cập nhật topic      | 200     |
| POST   | `/topics/{topicId}/archive` | Teacher/Admin | Archive topic       | 204     |

---

## 9.6. Question Bank API

| Method | Endpoint                          | Actor         | Description            | Success |
| ------ | --------------------------------- | ------------- | ---------------------- | ------- |
| GET    | `/questions`                      | Teacher/Admin | Xem danh sách question | 200     |
| POST   | `/questions`                      | Teacher/Admin | Tạo question           | 201     |
| GET    | `/questions/{questionId}`         | Teacher/Admin | Xem chi tiết question  | 200     |
| PATCH  | `/questions/{questionId}`         | Teacher/Admin | Cập nhật question      | 200     |
| POST   | `/questions/{questionId}/publish` | Teacher/Admin | Publish question       | 200     |
| POST   | `/questions/{questionId}/archive` | Teacher/Admin | Archive question       | 204     |

---

## 9.7. Exam / Practice API

| Method | Endpoint                                 | Actor             | Description                               | Success |
| ------ | ---------------------------------------- | ----------------- | ----------------------------------------- | ------- |
| GET    | `/exams`                                 | Authenticated     | Xem danh sách exam/practice được phép xem | 200     |
| POST   | `/exams`                                 | Teacher/Admin     | Tạo exam/practice                         | 201     |
| GET    | `/exams/{examId}`                        | Authenticated     | Xem chi tiết exam                         | 200     |
| PATCH  | `/exams/{examId}`                        | Teacher/Admin     | Cập nhật exam khi còn Draft               | 200     |
| POST   | `/exams/{examId}/questions`              | Teacher/Admin     | Thêm question vào exam                    | 201     |
| DELETE | `/exams/{examId}/questions/{questionId}` | Teacher/Admin     | Gỡ question khỏi exam khi còn Draft       | 204     |
| POST   | `/exams/{examId}/publish`                | Teacher/Admin     | Publish exam                              | 200     |
| POST   | `/exams/{examId}/close`                  | Teacher/Admin/BTC | Close exam                                | 200     |
| POST   | `/exams/{examId}/archive`                | Teacher/Admin     | Archive exam                              | 204     |

---

## 9.8. Attempt / Submission API

| Method | Endpoint                                     | Actor                 | Description                                    | Success |
| ------ | -------------------------------------------- | --------------------- | ---------------------------------------------- | ------- |
| POST   | `/exams/{examId}/attempts`                   | Student               | Bắt đầu làm bài                                | 201     |
| GET    | `/attempts/{attemptId}`                      | Student/Teacher/Admin | Xem attempt theo quyền                         | 200     |
| PUT    | `/attempts/{attemptId}/answers/{questionId}` | Student               | Lưu/cập nhật answer khi attempt còn InProgress | 200     |
| POST   | `/attempts/{attemptId}/submit`               | Student               | Submit attempt                                 | 200     |
| GET    | `/attempts/{attemptId}/result`               | Student/Teacher/Admin | Xem result của attempt                         | 200     |

---

## 9.9. Grading API

| Method | Endpoint                                   | Actor         | Description                         | Success |
| ------ | ------------------------------------------ | ------------- | ----------------------------------- | ------- |
| GET    | `/grading/essay-answers`                   | Teacher/Admin | Xem danh sách essay answer cần chấm | 200     |
| POST   | `/grading/essay-answers/{attemptAnswerId}` | Teacher/Admin | Chấm một câu tự luận                | 200     |
| PATCH  | `/grading/essay-answers/{attemptAnswerId}` | Teacher/Admin | Cập nhật điểm/nhận xét tự luận      | 200     |

---

## 9.10. Result API

| Method | Endpoint                  | Actor                     | Description                     | Success |
| ------ | ------------------------- | ------------------------- | ------------------------------- | ------- |
| GET    | `/results`                | Teacher/Admin/BTC         | Xem danh sách result theo quyền | 200     |
| GET    | `/results/{resultId}`     | Student/Teacher/Admin/BTC | Xem chi tiết result theo quyền  | 200     |
| GET    | `/exams/{examId}/results` | Teacher/Admin/BTC         | Xem result theo exam            | 200     |

---

## 9.11. MVP Extended API

| Method | Endpoint                                  | Actor                     | Description                     | Success |
| ------ | ----------------------------------------- | ------------------------- | ------------------------------- | ------- |
| POST   | `/imports/questions/excel`                | Teacher/Admin             | Upload Excel để import question | 202     |
| GET    | `/imports/{importId}`                     | Teacher/Admin             | Xem trạng thái import           | 200     |
| POST   | `/attachments`                            | Teacher/Admin             | Upload file attachment          | 201     |
| POST   | `/contributions`                          | Contributor               | Gửi contribution request        | 201     |
| GET    | `/contributions`                          | Contributor/Teacher/Admin | Xem contribution theo quyền     | 200     |
| POST   | `/contributions/{contributionId}/approve` | Teacher/Admin             | Approve contribution            | 200     |
| POST   | `/contributions/{contributionId}/reject`  | Teacher/Admin             | Reject contribution             | 200     |
| GET    | `/audit-logs`                             | Admin                     | Xem audit log                   | 200     |

---

# 10. Request / Response Schema

## 10.1. Auth Schemas

### Register Request

```json id="1khnxw"
{
  "email": "student@example.com",
  "password": "StrongPassword123!",
  "fullName": "Nguyen Van A"
}
```

### Login Request

```json id="xyl2xj"
{
  "email": "student@example.com",
  "password": "StrongPassword123!"
}
```

### Auth Response

```json id="5vpnjw"
{
  "data": {
    "accessToken": "jwt-access-token",
    "refreshToken": "jwt-refresh-token",
    "tokenType": "Bearer",
    "expiresIn": 3600,
    "user": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "email": "student@example.com",
      "fullName": "Nguyen Van A",
      "roles": ["STUDENT"]
    }
  }
}
```

---

## 10.2. Subject Schemas

### Create Subject Request

```json id="a45m2v"
{
  "code": "MATH",
  "name": "Mathematics",
  "description": "Olympic Mathematics subject"
}
```

### Subject Response

```json id="l19a4d"
{
  "data": {
    "id": "6e6654d7-2a8f-4c49-912f-6fbbda32dc31",
    "code": "MATH",
    "name": "Mathematics",
    "description": "Olympic Mathematics subject",
    "status": "ACTIVE",
    "createdAt": "2026-06-03T10:00:00+07:00",
    "updatedAt": "2026-06-03T10:00:00+07:00"
  }
}
```

---

## 10.3. Topic Schemas

### Create Topic Request

```json id="xv9zvr"
{
  "subjectId": "6e6654d7-2a8f-4c49-912f-6fbbda32dc31",
  "name": "Algebra",
  "description": "Algebra topic"
}
```

### Topic Response

```json id="m3i5qc"
{
  "data": {
    "id": "b8fe17fd-9863-4ff3-9ae1-d37f98a39c91",
    "subjectId": "6e6654d7-2a8f-4c49-912f-6fbbda32dc31",
    "name": "Algebra",
    "description": "Algebra topic",
    "status": "ACTIVE"
  }
}
```

---

## 10.4. Question Schemas

### Create Single Choice Question Request

```json id="dcxrxi"
{
  "topicId": "b8fe17fd-9863-4ff3-9ae1-d37f98a39c91",
  "questionType": "SINGLE_CHOICE",
  "content": "What is 2 + 2?",
  "point": 1,
  "difficulty": "EASY",
  "options": [
    {
      "content": "3",
      "isCorrect": false,
      "orderIndex": 1
    },
    {
      "content": "4",
      "isCorrect": true,
      "orderIndex": 2
    }
  ],
  "explanation": "2 + 2 = 4"
}
```

### Create Multiple Choice Question Request

```json id="06rjnp"
{
  "topicId": "b8fe17fd-9863-4ff3-9ae1-d37f98a39c91",
  "questionType": "MULTIPLE_CHOICE",
  "content": "Which numbers are even?",
  "point": 2,
  "difficulty": "EASY",
  "options": [
    {
      "content": "2",
      "isCorrect": true,
      "orderIndex": 1
    },
    {
      "content": "3",
      "isCorrect": false,
      "orderIndex": 2
    },
    {
      "content": "4",
      "isCorrect": true,
      "orderIndex": 3
    }
  ],
  "explanation": "2 and 4 are even numbers."
}
```

### Create Essay Question Request

```json id="d3dfvx"
{
  "topicId": "b8fe17fd-9863-4ff3-9ae1-d37f98a39c91",
  "questionType": "ESSAY",
  "content": "Explain the idea of mathematical induction.",
  "point": 5,
  "difficulty": "MEDIUM",
  "rubric": "Student should explain base case, induction hypothesis and induction step."
}
```

### Question Response

```json id="u5h3ab"
{
  "data": {
    "id": "9bba86dd-4b99-4e66-aac1-d04c8ad6c252",
    "topicId": "b8fe17fd-9863-4ff3-9ae1-d37f98a39c91",
    "questionType": "SINGLE_CHOICE",
    "content": "What is 2 + 2?",
    "point": 1,
    "difficulty": "EASY",
    "status": "DRAFT",
    "options": [
      {
        "id": "6575cc31-2796-476c-a7f5-73877e51b2dc",
        "content": "3",
        "isCorrect": false,
        "orderIndex": 1
      },
      {
        "id": "21391cf7-5393-4028-87d2-c3885d1dc466",
        "content": "4",
        "isCorrect": true,
        "orderIndex": 2
      }
    ],
    "explanation": "2 + 2 = 4"
  }
}
```

---

## 10.5. Exam Schemas

### Create Exam Request

```json id="v6b2j2"
{
  "examType": "PRACTICE",
  "title": "Algebra Practice 01",
  "description": "Basic algebra practice set",
  "subjectId": "6e6654d7-2a8f-4c49-912f-6fbbda32dc31",
  "durationMinutes": 60,
  "attemptLimit": null,
  "showResult": true,
  "showExplanation": true
}
```

### Add Question To Exam Request

```json id="63fhv9"
{
  "questionId": "9bba86dd-4b99-4e66-aac1-d04c8ad6c252",
  "orderIndex": 1,
  "pointOverride": null
}
```

### Exam Response

```json id="otg8uq"
{
  "data": {
    "id": "81fbe77c-1c8d-4997-b58e-686e84ee8cbd",
    "examType": "PRACTICE",
    "title": "Algebra Practice 01",
    "description": "Basic algebra practice set",
    "subjectId": "6e6654d7-2a8f-4c49-912f-6fbbda32dc31",
    "status": "DRAFT",
    "durationMinutes": 60,
    "attemptLimit": null,
    "showResult": true,
    "showExplanation": true,
    "questionCount": 1
  }
}
```

---

## 10.6. Attempt Schemas

### Start Attempt Response

```json id="5xn1hx"
{
  "data": {
    "attemptId": "687bd081-b337-4e76-9697-4c829d8e24dc",
    "examId": "81fbe77c-1c8d-4997-b58e-686e84ee8cbd",
    "status": "IN_PROGRESS",
    "startedAt": "2026-06-03T10:30:00+07:00",
    "questions": [
      {
        "id": "9bba86dd-4b99-4e66-aac1-d04c8ad6c252",
        "questionType": "SINGLE_CHOICE",
        "content": "What is 2 + 2?",
        "point": 1,
        "options": [
          {
            "id": "6575cc31-2796-476c-a7f5-73877e51b2dc",
            "content": "3",
            "orderIndex": 1
          },
          {
            "id": "21391cf7-5393-4028-87d2-c3885d1dc466",
            "content": "4",
            "orderIndex": 2
          }
        ]
      }
    ]
  }
}
```

Important:

```text id="p79e7o"
Student attempt response không được lộ isCorrect.
```

---

### Save Answer Request — Single Choice

```json id="dq5iw0"
{
  "selectedOptionIds": [
    "21391cf7-5393-4028-87d2-c3885d1dc466"
  ]
}
```

### Save Answer Request — Multiple Choice

```json id="a12i8u"
{
  "selectedOptionIds": [
    "21391cf7-5393-4028-87d2-c3885d1dc466",
    "557cf37d-7d8c-4b5f-8630-f4b530f2a67e"
  ]
}
```

### Save Answer Request — Essay

```json id="o8plap"
{
  "answerText": "Mathematical induction proves a statement by proving a base case and an induction step."
}
```

### Submit Attempt Response

```json id="9vfz71"
{
  "data": {
    "attemptId": "687bd081-b337-4e76-9697-4c829d8e24dc",
    "status": "PENDING_MANUAL_GRADING",
    "submittedAt": "2026-06-03T11:10:00+07:00",
    "autoScore": 8,
    "manualScore": 0,
    "totalScore": 8,
    "gradingStatus": "PENDING_MANUAL_GRADING"
  }
}
```

---

## 10.7. Essay Grading Schemas

### Grade Essay Request

```json id="3vbz7u"
{
  "score": 4.5,
  "comment": "Good explanation, but induction step needs more detail."
}
```

### Grade Essay Response

```json id="q3dcrb"
{
  "data": {
    "attemptAnswerId": "ee312d26-f1f5-48cd-ad12-3876200d220d",
    "score": 4.5,
    "comment": "Good explanation, but induction step needs more detail.",
    "gradedBy": {
      "id": "0d0a8f59-9347-49b2-bb50-b058b3c04e42",
      "fullName": "Teacher A"
    },
    "gradedAt": "2026-06-03T12:00:00+07:00"
  }
}
```

---

## 10.8. Result Schema

### Result Response

```json id="iwy3vd"
{
  "data": {
    "resultId": "b4b2f397-5b0e-40a3-801d-7fd4b2a62164",
    "attemptId": "687bd081-b337-4e76-9697-4c829d8e24dc",
    "examId": "81fbe77c-1c8d-4997-b58e-686e84ee8cbd",
    "examTitle": "Algebra Practice 01",
    "student": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "fullName": "Nguyen Van A"
    },
    "autoScore": 8,
    "manualScore": 4.5,
    "totalScore": 12.5,
    "gradingStatus": "GRADED",
    "calculatedAt": "2026-06-03T12:00:00+07:00"
  }
}
```

---

# 11. Business Error Examples

## 11.1. Submit Attempt Twice

Status:

```text id="wn3d2z"
409 Conflict
```

Response:

```json id="v12vc5"
{
  "type": "https://api.olympic-platform.example.com/problems/attempt-already-submitted",
  "title": "Attempt Already Submitted",
  "status": 409,
  "detail": "This attempt has already been submitted and cannot be submitted again.",
  "instance": "/api/v1/attempts/687bd081-b337-4e76-9697-4c829d8e24dc/submit",
  "code": "ATTEMPT_ALREADY_SUBMITTED",
  "traceId": "01J8ZK7Q3W3A5Y9V1KX2"
}
```

---

## 11.2. Publish Exam Without Questions

Status:

```text id="6cytgj"
422 Unprocessable Entity
```

Response:

```json id="v39iql"
{
  "type": "https://api.olympic-platform.example.com/problems/exam-has-no-questions",
  "title": "Exam Has No Questions",
  "status": 422,
  "detail": "Exam must have at least one question before publishing.",
  "instance": "/api/v1/exams/81fbe77c-1c8d-4997-b58e-686e84ee8cbd/publish",
  "code": "EXAM_HAS_NO_QUESTIONS",
  "traceId": "01J8ZK7Q3W3A5Y9V1KX2"
}
```

---

## 11.3. Forbidden Result Access

Status:

```text id="hpblni"
403 Forbidden
```

Response:

```json id="o7q2qq"
{
  "type": "https://api.olympic-platform.example.com/problems/forbidden",
  "title": "Forbidden",
  "status": 403,
  "detail": "You do not have permission to access this result.",
  "instance": "/api/v1/results/b4b2f397-5b0e-40a3-801d-7fd4b2a62164",
  "code": "FORBIDDEN",
  "traceId": "01J8ZK7Q3W3A5Y9V1KX2"
}
```

---

# 12. API Security Rules

| ID          | Rule                                                                         |
| ----------- | ---------------------------------------------------------------------------- |
| API-SEC-001 | Public endpoint chỉ gồm register, login và refresh nếu refresh token hợp lệ. |
| API-SEC-002 | Các endpoint còn lại yêu cầu JWT access token.                               |
| API-SEC-003 | Student chỉ được xem attempt/result của chính mình.                          |
| API-SEC-004 | Student không được nhận response có `isCorrect` khi đang làm bài.            |
| API-SEC-005 | Teacher/admin mới được tạo/sửa/publish question và exam.                     |
| API-SEC-006 | Teacher/admin mới được chấm tự luận.                                         |
| API-SEC-007 | BTC chỉ được xem screening result trong phạm vi được cấp quyền.              |
| API-SEC-008 | Admin mới được quản lý user, role và permission.                             |
| API-SEC-009 | Backend phải validate authorization ở server, không tin frontend.            |
| API-SEC-010 | File upload phải validate file type và file size.                            |

---

# 13. OpenAPI 3.1 Baseline Contract

## 13.1. OpenAPI File Name

Recommended file:

```text id="zn1ge0"
docs/api/openapi.yaml
```

---

## 13.2. OpenAPI Skeleton

```yaml id="barm0u"
openapi: 3.1.0
info:
  title: Olympic Learning Platform API
  version: 1.0.0
  description: REST API for Olympic Learning Platform MVP.
servers:
  - url: http://localhost:8080/api/v1
    description: Local
  - url: https://staging.olympic-platform.example.com/api/v1
    description: Staging

security:
  - bearerAuth: []

tags:
  - name: Auth
  - name: Me
  - name: Users
  - name: Subjects
  - name: Topics
  - name: Questions
  - name: Exams
  - name: Attempts
  - name: Grading
  - name: Results

paths:
  /auth/login:
    post:
      tags: [Auth]
      summary: Login
      security: []
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/LoginRequest'
            examples:
              default:
                value:
                  email: student@example.com
                  password: StrongPassword123!
      responses:
        '200':
          description: Login success
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/AuthResponse'
        '400':
          $ref: '#/components/responses/BadRequest'
        '401':
          $ref: '#/components/responses/Unauthorized'

  /subjects:
    get:
      tags: [Subjects]
      summary: List subjects
      parameters:
        - $ref: '#/components/parameters/Page'
        - $ref: '#/components/parameters/Size'
        - $ref: '#/components/parameters/Sort'
      responses:
        '200':
          description: Subject list
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/SubjectPageResponse'
    post:
      tags: [Subjects]
      summary: Create subject
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/CreateSubjectRequest'
      responses:
        '201':
          description: Subject created
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/SubjectResponse'
        '400':
          $ref: '#/components/responses/BadRequest'
        '403':
          $ref: '#/components/responses/Forbidden'

  /questions:
    get:
      tags: [Questions]
      summary: List questions
      parameters:
        - name: topicId
          in: query
          schema:
            type: string
            format: uuid
        - name: questionType
          in: query
          schema:
            type: string
            enum: [SINGLE_CHOICE, MULTIPLE_CHOICE, ESSAY]
        - name: status
          in: query
          schema:
            type: string
            enum: [DRAFT, PUBLISHED, ARCHIVED]
        - $ref: '#/components/parameters/Page'
        - $ref: '#/components/parameters/Size'
      responses:
        '200':
          description: Question list
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/QuestionPageResponse'
    post:
      tags: [Questions]
      summary: Create question
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/CreateQuestionRequest'
      responses:
        '201':
          description: Question created
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/QuestionResponse'

  /exams/{examId}/attempts:
    post:
      tags: [Attempts]
      summary: Start attempt
      parameters:
        - name: examId
          in: path
          required: true
          schema:
            type: string
            format: uuid
      responses:
        '201':
          description: Attempt started
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/AttemptResponse'
        '404':
          $ref: '#/components/responses/NotFound'
        '422':
          $ref: '#/components/responses/BusinessRuleViolation'

  /attempts/{attemptId}/submit:
    post:
      tags: [Attempts]
      summary: Submit attempt
      parameters:
        - name: attemptId
          in: path
          required: true
          schema:
            type: string
            format: uuid
      responses:
        '200':
          description: Attempt submitted
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/SubmitAttemptResponse'
        '409':
          $ref: '#/components/responses/Conflict'

components:
  securitySchemes:
    bearerAuth:
      type: http
      scheme: bearer
      bearerFormat: JWT

  parameters:
    Page:
      name: page
      in: query
      schema:
        type: integer
        minimum: 0
        default: 0
    Size:
      name: size
      in: query
      schema:
        type: integer
        minimum: 1
        maximum: 100
        default: 20
    Sort:
      name: sort
      in: query
      schema:
        type: string
        example: createdAt,desc

  responses:
    BadRequest:
      description: Bad request
      content:
        application/problem+json:
          schema:
            $ref: '#/components/schemas/ProblemDetail'
    Unauthorized:
      description: Unauthorized
      content:
        application/problem+json:
          schema:
            $ref: '#/components/schemas/ProblemDetail'
    Forbidden:
      description: Forbidden
      content:
        application/problem+json:
          schema:
            $ref: '#/components/schemas/ProblemDetail'
    NotFound:
      description: Resource not found
      content:
        application/problem+json:
          schema:
            $ref: '#/components/schemas/ProblemDetail'
    Conflict:
      description: Resource conflict
      content:
        application/problem+json:
          schema:
            $ref: '#/components/schemas/ProblemDetail'
    BusinessRuleViolation:
      description: Business rule violation
      content:
        application/problem+json:
          schema:
            $ref: '#/components/schemas/ProblemDetail'

  schemas:
    ProblemDetail:
      type: object
      required: [type, title, status, detail, instance, code]
      properties:
        type:
          type: string
          format: uri
        title:
          type: string
        status:
          type: integer
        detail:
          type: string
        instance:
          type: string
        code:
          type: string
        traceId:
          type: string
        errors:
          type: array
          items:
            type: object
            properties:
              field:
                type: string
              message:
                type: string

    LoginRequest:
      type: object
      required: [email, password]
      properties:
        email:
          type: string
          format: email
        password:
          type: string

    AuthResponse:
      type: object
      properties:
        data:
          type: object
          properties:
            accessToken:
              type: string
            refreshToken:
              type: string
            tokenType:
              type: string
              example: Bearer
            expiresIn:
              type: integer

    CreateSubjectRequest:
      type: object
      required: [code, name]
      properties:
        code:
          type: string
        name:
          type: string
        description:
          type: string

    SubjectResponse:
      type: object
      properties:
        data:
          $ref: '#/components/schemas/Subject'

    Subject:
      type: object
      properties:
        id:
          type: string
          format: uuid
        code:
          type: string
        name:
          type: string
        description:
          type: string
        status:
          type: string
          enum: [ACTIVE, ARCHIVED]

    SubjectPageResponse:
      type: object
      properties:
        data:
          type: array
          items:
            $ref: '#/components/schemas/Subject'
        page:
          $ref: '#/components/schemas/PageMeta'

    PageMeta:
      type: object
      properties:
        number:
          type: integer
        size:
          type: integer
        totalElements:
          type: integer
        totalPages:
          type: integer

    CreateQuestionRequest:
      type: object
      required: [topicId, questionType, content, point]
      properties:
        topicId:
          type: string
          format: uuid
        questionType:
          type: string
          enum: [SINGLE_CHOICE, MULTIPLE_CHOICE, ESSAY]
        content:
          type: string
        point:
          type: number
        difficulty:
          type: string
          enum: [EASY, MEDIUM, HARD]
        options:
          type: array
          items:
            $ref: '#/components/schemas/CreateQuestionOptionRequest'
        explanation:
          type: string
        rubric:
          type: string

    CreateQuestionOptionRequest:
      type: object
      required: [content, isCorrect, orderIndex]
      properties:
        content:
          type: string
        isCorrect:
          type: boolean
        orderIndex:
          type: integer

    QuestionResponse:
      type: object
      properties:
        data:
          $ref: '#/components/schemas/Question'

    QuestionPageResponse:
      type: object
      properties:
        data:
          type: array
          items:
            $ref: '#/components/schemas/Question'
        page:
          $ref: '#/components/schemas/PageMeta'

    Question:
      type: object
      properties:
        id:
          type: string
          format: uuid
        topicId:
          type: string
          format: uuid
        questionType:
          type: string
        content:
          type: string
        point:
          type: number
        difficulty:
          type: string
        status:
          type: string

    AttemptResponse:
      type: object
      properties:
        data:
          type: object
          properties:
            attemptId:
              type: string
              format: uuid
            examId:
              type: string
              format: uuid
            status:
              type: string

    SubmitAttemptResponse:
      type: object
      properties:
        data:
          type: object
          properties:
            attemptId:
              type: string
              format: uuid
            status:
              type: string
            autoScore:
              type: number
            manualScore:
              type: number
            totalScore:
              type: number
```

---

# 14. OpenAPI Implementation Direction

Trong Spring Boot, API docs có thể dùng:

```text id="vtl17w"
springdoc-openapi
```

Recommended generated docs path:

```text id="dp6k8q"
/swagger-ui.html
/v3/api-docs
```

Recommended source file:

```text id="y1bavk"
docs/api/openapi.yaml
```

Khi implementation, có hai hướng:

| Approach       | Description                                            |
| -------------- | ------------------------------------------------------ |
| Code-first     | Viết controller trước, dùng annotation để sinh OpenAPI |
| Contract-first | Viết OpenAPI YAML trước, backend bám theo contract     |

Baseline decision:

```text id="dbsgri"
MVP dùng code-first kết hợp review OpenAPI generated output.
```

Lý do:

* Dễ làm với Spring Boot.
* Phù hợp MVP.
* Vẫn có generated OpenAPI contract để frontend kiểm tra.
* Sau này có thể chuyển sang contract-first nếu team lớn hơn.

---

# 15. API Validation Rules

| ID         | Validation Rule                                                        |
| ---------- | ---------------------------------------------------------------------- |
| API-DV-001 | Request body phải đúng JSON schema.                                    |
| API-DV-002 | UUID path variable phải đúng format.                                   |
| API-DV-003 | Enum value phải nằm trong danh sách cho phép.                          |
| API-DV-004 | Required field không được null hoặc blank.                             |
| API-DV-005 | Pagination size không được vượt quá 100.                               |
| API-DV-006 | Student attempt response không được trả `isCorrect`.                   |
| API-DV-007 | Create single choice question phải có đúng 1 correct option.           |
| API-DV-008 | Create multiple choice question phải có ít nhất 1 correct option.      |
| API-DV-009 | Publish exam phải có ít nhất 1 question.                               |
| API-DV-010 | Submit attempt chỉ hợp lệ nếu attempt đang `IN_PROGRESS`.              |
| API-DV-011 | Grade essay chỉ hợp lệ nếu attempt answer thuộc question type `ESSAY`. |
| API-DV-012 | Essay score không được vượt quá max point của câu hỏi.                 |

---

# 16. API Design Decisions

| ID          | Decision                                                   | Reason                                            |
| ----------- | ---------------------------------------------------------- | ------------------------------------------------- |
| API-DEC-001 | Dùng REST API với JSON                                     | Dễ tích hợp frontend và phù hợp Spring Boot       |
| API-DEC-002 | Dùng `/api/v1` prefix                                      | Hỗ trợ versioning                                 |
| API-DEC-003 | Dùng JWT Bearer token                                      | Phù hợp stateless API                             |
| API-DEC-004 | Dùng RFC 9457 Problem Details cho lỗi                      | Chuẩn hóa error response                          |
| API-DEC-005 | Dùng page/size/sort cho list API                           | Dễ dùng và phổ biến với Spring                    |
| API-DEC-006 | Dùng action endpoint cho nghiệp vụ đặc biệt                | Ví dụ `/submit`, `/publish`, `/archive`, `/grade` |
| API-DEC-007 | Không expose đáp án đúng cho student khi đang làm bài      | Bảo vệ tính đúng đắn của attempt                  |
| API-DEC-008 | Endpoint được sinh từ use case, không chỉ CRUD theo entity | Tránh API rời rạc, không phục vụ workflow thật    |
| API-DEC-009 | OpenAPI 3.1 là contract chuẩn                              | Frontend/backend thống nhất API                   |
| API-DEC-010 | MVP dùng code-first OpenAPI generation                     | Dễ triển khai nhanh với Spring Boot               |

---

# 17. Phase 5 Exit Criteria

Phase 5 được xem là hoàn thành khi:

* Có API design principles.
* Có resource model.
* Có endpoint list cho MVP Core.
* Có endpoint list cho MVP Extended ở mức baseline.
* Endpoint sinh ra từ use case, không sinh tùy tiện từ entity.
* Có request/response schema mẫu cho các flow chính.
* Có status code convention.
* Có error response format thống nhất theo RFC 9457 Problem Details.
* Có pagination/filtering/sorting convention.
* Có OpenAPI 3.1 baseline contract.
* Có API security rules.
* Có API validation rules.
* Thiết kế API không mâu thuẫn với Phase 0, Phase 1, Phase 2, Phase 3 và Phase 4.

Với các nội dung trên, Phase 5 có thể được chốt và chuyển sang Phase 6 — Application Design / Backend Design.

---

# 18. Next Phase

Phase tiếp theo:

# Phase 6 — Application Design / Backend Design

Các artifact nên tạo ở Phase 6:

```text id="l5qjfj"
6.1 Package Structure
6.2 Layer Responsibilities
6.3 DTO Design
6.4 Service Design
6.5 Repository Design
6.6 Security Design
6.7 Exception Handling Design
6.8 Validation Design
6.9 Transaction Boundary
6.10 Testing Strategy
```

Phase 6 sẽ chuyển API contract và data design thành thiết kế backend cụ thể cho Spring Boot implementation.

---
