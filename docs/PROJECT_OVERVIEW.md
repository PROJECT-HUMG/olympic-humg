# Olympic Learning Platform — Project Overview

## 1. Product Vision

Olympic Learning Platform là nền tảng giúp sinh viên ôn luyện Olympic theo một luồng tập trung:

```text
Teacher/Admin tạo nội dung
-> Student làm bài
-> Hệ thống chấm phần trắc nghiệm
-> Teacher/Admin chấm phần tự luận
-> Student/BTC xem kết quả phù hợp với quyền
```

MVP không cố làm bản nhỏ của mọi chức năng. MVP tập trung làm trọn vẹn core learning loop trước.

---

## 2. Core Learning Loop

```text
identity
  -> learning
  -> question
  -> assessment
  -> attempt
  -> grading
  -> result
```

Ý nghĩa:

| Step       | Main Responsibility                                      |
| ---------- | -------------------------------------------------------- |
| identity   | Auth, user, role, permission, JWT                        |
| learning   | Subject, topic                                           |
| question   | Question bank, options, explanation, rubric              |
| assessment | Practice set / screening exam baseline, exam questions   |
| attempt    | Start attempt, save answer, submit                       |
| grading    | Auto grading objective questions, manual grading essays   |
| result     | Score summary, result visibility, history                |

---

## 3. Module Map

Baseline backend modules:

```text
common
identity
learning
question
assessment
attempt
grading
result
file
import
contribution
audit
```

Module groups:

| Group      | Modules                                           | Priority |
| ---------- | ------------------------------------------------- | -------- |
| Core       | identity, learning, question, assessment, attempt, grading, result | MVP Core |
| Supporting | common, file, import, audit                       | MVP / MVP Extended |
| Extended   | contribution, screening advanced, analytics, notification | Later |

Screening trong MVP được biểu diễn bằng `assessment` với `examType = SCREENING`.
Chỉ tách module `screening` khi có participant list, ranking, export hoặc BTC dashboard riêng.

---

## 4. Data Map

Các bảng chính theo module:

| Module     | Main Tables |
| ---------- | ----------- |
| identity   | users, roles, permissions, user_roles, role_permissions |
| learning   | subjects, topics |
| question   | questions, question_options |
| assessment | exams, exam_questions |
| attempt    | exam_attempts, attempt_answers, attempt_answer_options |
| grading    | essay_gradings |
| result     | results |
| file       | attachments, question_attachments |
| contribution | contribution_requests |
| audit      | audit_logs |

Deferred tables:

```text
teacher_subject_scopes
exam_viewer_scopes
screening_participants
user_permissions
question_versions
attempt_question_snapshots
```

Các bảng deferred chỉ thêm khi requirement thật sự cần. MVP pilot có thể bắt đầu với role/permission ở mức hệ thống.

---

## 5. API Map

| Module       | API Resources |
| ------------ | ------------- |
| identity     | /auth, /me, /users, /roles, /permissions |
| learning     | /subjects, /topics |
| question     | /questions |
| assessment   | /exams |
| attempt      | /exams/{examId}/attempts, /attempts |
| grading      | /grading |
| result       | /results |
| file         | /attachments |
| import       | /imports |
| contribution | /contributions |
| audit        | /audit-logs |

API dùng REST + JSON, error theo RFC 9457 Problem Details, OpenAPI 3.1 generated từ Spring Boot trong MVP.

---

## 6. Implementation Order

Làm theo lát mỏng, mỗi lát có schema, API, service, test cơ bản.

```text
0. Project skeleton
1. identity
2. learning
3. question
4. assessment
5. attempt
6. grading
7. result
8. file/import/contribution/audit nếu còn trong MVP scope
```

Nguyên tắc:

```text
Feature nào không phục vụ core learning loop thì để sau.
Schema do Flyway quản lý.
Backend validate authorization và business rules, không tin frontend.
Question/exam đã có attempt không sửa trực tiếp nếu có thể làm sai lệch result.
```

