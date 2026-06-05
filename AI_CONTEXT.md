# AI_CONTEXT.md — Olympic Platform

## 1. Project Summary

Olympic Platform is a web system for university Olympiad training and selection.

The system supports subjects such as Mathematics, Physics, Chemistry, Foreign Languages, and other Olympiad-related subjects.

The platform is not only a test system. It should support the full flow:

```text
News / Announcements
Learning Documents
Learning Roadmaps
Question Bank
Practice & Tests
Weekly Evaluation
Mock Tests
Selection Tests
Olympiad Campaigns
Hall of Fame
Activity Leaderboard
Analytics
```

Main users:

```text
STUDENT
LECTURER
ADMIN
CONTENT_CONTRIBUTOR
QUESTION_REVIEWER
```

---

## 2. Product Direction

The platform should support three main modes:

```text
Practice Mode
- Students practice freely by subject, topic, difficulty, or learning objective.
- Main goal: self-study and identify weak areas.

Weekly Test / Mock Test Mode
- Lecturers create weekly tests, assignments, and mock tests.
- Main goal: track progress and prepare for selection.

Official Selection Test Mode
- Used for official Olympiad team selection.
- Should only use verified questions.
- Results can be used to select official team members.
```

Important decision:

```text
Practice, assignments, weekly tests, mock tests, selection tests, and official tests should share one Assessment module.

They are separated by assessment_type:
- PRACTICE
- ASSIGNMENT
- WEEKLY_TEST
- MOCK_TEST
- SELECTION_TEST
- OFFICIAL_TEST
```

---

## 3. Main Product Modules

```text
Olympic Platform
│
├── Information Portal
│   ├── News
│   └── Announcements
│
├── Learning Center
│   ├── Learning resources
│   ├── Subjects
│   ├── Topics
│   ├── Learning objectives
│   └── Learning roadmaps
│
├── Question Bank
│   ├── Questions
│   ├── Options
│   ├── Answer rules
│   ├── Explanations
│   └── Review workflow
│
├── Practice & Assessment
│   ├── Practice
│   ├── Assignments
│   ├── Weekly tests
│   ├── Mock tests
│   └── Selection tests
│
├── Olympiad Campaign
│   ├── Training seasons
│   ├── Candidate enrollment
│   ├── Weekly assessment plan
│   ├── Selection score rules
│   └── Official team members
│
├── Achievement & Leaderboard
│   ├── Hall of Fame
│   ├── Official awards
│   └── Activity leaderboard
│
├── Analytics & Reporting
│   ├── Student progress
│   ├── Topic performance
│   └── Lecturer dashboard
│
├── Notification
└── Admin
```

---

## 4. Backend Architecture

Use:

```text
Spring Boot Modular Monolith
```

Meaning:

```text
- One Spring Boot backend application.
- Code is split by business modules.
- Each module uses familiar Spring Boot layers.
- Do not use microservices in the early stage.
```

Tech direction:

```text
Backend: Spring Boot
Database: PostgreSQL
Auth: JWT
Authorization: RBAC
API: RESTful API
API Docs: OpenAPI 3.1
Mapper: MapStruct
File Storage: local first, cloud later
Redis: optional later for cache/queue
```

---

## 5. Package Structure Convention

Use module-based layered architecture.

Each module should follow this structure:

```text
module/
├── controller
├── request
├── response
├── service
│   └── impl
├── repository
├── entity
├── mapper
└── enums
```

Do not use:

```text
module/
├── api
├── application
├── domain
└── infrastructure
```

Do not use global packages like:

```text
controller/
service/
repository/
entity/
```

Use `entity`, not `domain`.

Keep `enums` as a separate package inside each module.

---

## 6. Root Backend Modules

```text
me.nghlong3004.olympic
│
├── common
├── auth
├── user
├── content
├── learning
├── questionbank
├── assessment
├── campaign
├── achievement
├── leaderboard
├── analytics
├── notification
└── storage
```

Module meanings:

```text
common
- Shared config, exception, response, security, audit, util.

auth
- Login, logout, refresh token, JWT, password reset.

user
- Users, roles, permissions, profile, RBAC.

content
- News, announcements, learning resources.

learning
- Subjects, topics, learning objectives, roadmaps.

questionbank
- Questions, options, answer rules, explanations, review workflow.

assessment
- Practice, assignments, weekly tests, mock tests, selection tests, attempts, answers, grading.

campaign
- Olympiad campaigns, candidate enrollment, selection score rules, official team members.

achievement
- Hall of Fame, official awards, historical achievements.

leaderboard
- Activity score and rankings.

analytics
- Student progress, topic performance, reports.

notification
- Assignment reminders, test reminders, result notifications.

storage
- File upload, download, file metadata.
```

---

## 7. Auth vs User

Split `auth` and `user`.

```text
auth = authentication
Question: Who are you?

user = user management + authorization
Question: What are you allowed to do?
```

`auth` contains:

```text
AuthController
AuthService
JwtService
RefreshTokenService
RefreshToken
RefreshTokenRepository
LoginRequest
LoginResponse
TokenResponse
```

`user` contains:

```text
User
Role
Permission
UserRepository
RoleRepository
PermissionRepository
UserService
RoleService
PermissionService
UserMapper
RoleMapper
PermissionMapper
```

---

## 8. Important Domain Decisions

### Question Bank

Question types:

```text
MCQ_SINGLE
MCQ_MULTIPLE
SHORT_ANSWER
NUMERIC
FORMULA
ESSAY
CLOZE
MATCHING
LISTENING
SPEAKING
```

Question status:

```text
DRAFT
PENDING_REVIEW
VERIFIED
REJECTED
ARCHIVED
```

Grading strategies:

```text
MCQ
EXACT_MATCH
NUMERIC_TOLERANCE
UNIT_AWARE
FORMULA_EQUIVALENT
RUBRIC_BASED
AI_ASSISTED
MANUAL_REVIEW
```

For MVP, support only:

```text
MCQ
EXACT_MATCH
NUMERIC_TOLERANCE
```

Official selection tests should only use `VERIFIED` questions.

---

### Assessment

Assessment types:

```text
PRACTICE
ASSIGNMENT
WEEKLY_TEST
MOCK_TEST
SELECTION_TEST
OFFICIAL_TEST
```

Attempt flow:

```text
Student starts assessment
↓
Create assessment_attempt
↓
Student answers questions
↓
Student submits assessment
↓
GradingService calculates score
↓
Store result
↓
Analytics and leaderboard can use the result later
```

Important: assessment should keep question version/snapshot to preserve historical correctness if questions are edited later.

---

### Campaign

A campaign represents one Olympiad training or selection season.

Example:

```text
Olympic Mathematics 2026
- Week 1: Algebra checkpoint
- Week 2: Inequality checkpoint
- Week 3: Geometry checkpoint
- Week 4: Mock test 1
- Week 5: Mock test 2
- Week 6: Selection test
```

Example selection score:

```text
Final score =
40% weekly test average
+ 30% mock test average
+ 30% selection test score
```

The formula should be configurable.

---

## 9. Module Dependency Direction

Recommended dependency direction:

```text
auth -> user
content -> storage
learning -> content
questionbank -> learning
assessment -> questionbank
campaign -> assessment
analytics -> assessment
leaderboard -> assessment
achievement -> campaign / user
notification -> user
```

Avoid circular dependencies.

---

## 10. MVP Build Order

```text
Phase 1 — Foundation
- common
- auth
- user
- storage

Phase 2 — Learning + Content
- learning
- content

Phase 3 — Question Bank
- questionbank

Phase 4 — Practice & Assessment
- assessment

Phase 5 — Olympiad Campaign
- campaign

Phase 6 — Extended Features
- achievement
- leaderboard
- analytics
- notification
```

---

## 11. Early Non-Goals

Do not implement these too early:

```text
AI-generated questions
Advanced adaptive learning
Microservices
Complex anti-cheating system
Real-time proctoring
Formula equivalence checker
Speaking/listening grading
Full recommendation engine
```

---

## 12. Key Principles

```text
1. Use modular monolith first.
2. Split by business module first.
3. Use controller/request/response/service/repository/entity/mapper/enums inside each module.
4. Split auth and user.
5. Use entity, not domain.
6. Use MapStruct for mapping.
7. Keep Question Bank separate from Assessment.
8. Use one Assessment module for all practice/test types.
9. Separate Achievement from Leaderboard.
10. Official tests use only verified questions.
11. Store question snapshots or versions for historical correctness.
12. Build MVP before adding AI or advanced analytics.
```

## One-Sentence Summary

Olympic Platform is a Spring Boot modular monolith for university Olympiad training and selection, using modules such as auth, user, content, learning, questionbank, assessment, campaign, achievement, leaderboard, analytics, notification, and storage. Each module follows familiar Spring Boot layers: controller, request, response, service, service/impl, repository, entity, mapper, and enums.

## 13. Quality, Testing & DevSecOps Direction

The backend should be developed with testing and basic DevSecOps practices from the beginning.

### Testing Strategy

The project should include these test levels:

```text
Unit Test
- Test service logic and business rules.
- Example: grading logic, score calculation, permission checking.

Repository Test
- Test JPA repositories and database queries.
- Use PostgreSQL/Testcontainers later if possible.

Controller Test
- Test REST APIs, request validation, status codes, and response format.
- Use MockMvc or WebTestClient.

Integration Test
- Test full flow across controller, service, repository, and database.
- Example: create assessment -> start attempt -> submit answer -> calculate result.

Security Test
- Test authentication and authorization.
- Example: unauthenticated user cannot access protected APIs.
- Example: student cannot create official tests.
- Example: lecturer can only manage allowed resources.
```

For MVP, prioritize:

```text
1. Unit tests for important business logic
2. Controller tests for important APIs
3. Security tests for protected endpoints
4. Integration tests for core flows
```

Core flows that must have tests:

```text
Auth:
- Login
- Refresh token
- Access protected API
- Reject invalid token

User:
- Create user
- Assign role
- Check permission

Question Bank:
- Create question
- Add options
- Add answer rule
- Review question
- Mark question as VERIFIED

Assessment:
- Create assessment
- Add questions
- Start attempt
- Submit answer
- Auto-grade MCQ / exact answer / numeric tolerance
- View result

Campaign:
- Create campaign
- Enroll participant
- Attach assessment
- Calculate candidate score
```

### DevSecOps Direction

The project should have a basic CI pipeline.

Recommended pipeline stages:

```text
Build
- Compile source code.
- Run Maven build.

Test
- Run unit tests.
- Run integration tests if available.

Static Analysis
- Run Checkstyle / Spotless / PMD / SpotBugs if configured.

Security Scan
- Scan dependencies for vulnerabilities.
- Scan secrets to avoid leaking tokens or passwords.
- Scan Docker image later if Docker is used.

Package
- Build application artifact or Docker image.

Deploy
- Deploy only from selected branches such as main or deploy.
```

Early MVP does not need a complex DevSecOps system, but it should at least include:

```text
- Automated test execution in CI
- Dependency vulnerability checking
- Secret scanning
- Environment-based configuration
- No hardcoded credentials
- Standard error response
- API validation
- Role/permission checks
```

### Quality Principles

```text
1. Every important business rule should have a unit test.
2. Every protected API should have an authorization test.
3. Every core user flow should have at least one integration test.
4. Grading logic must be tested carefully because it affects student results.
5. Selection score calculation must be tested carefully because it affects team selection.
6. CI should fail if tests fail.
7. Secrets must never be committed to Git.
8. Official selection features require stricter validation and permission checks.
```
