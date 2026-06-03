# Olympic Learning Platform — Phase 1 Requirements Engineering

## 1. Document Information

| Field              | Value                                                                                                                                    |
| ------------------ | ---------------------------------------------------------------------------------------------------------------------------------------- |
| Project name       | Olympic Learning Platform                                                                                                                |
| Phase              | Phase 1 — Requirements Engineering                                                                                                       |
| Status             | Final baseline                                                                                                                           |
| Based on           | Phase 0 — Project Initiation / Discovery                                                                                                 |
| Purpose            | Chuyển mục tiêu, phạm vi và quyết định ở Phase 0 thành hệ thống requirement có ID, priority, acceptance criteria và traceability rõ ràng |
| Standard direction | Inspired by ISO/IEC/IEEE 29148 requirements engineering structure                                                                        |

---

## 2. Phase 1 Goal

Mục tiêu của Phase 1 là trả lời câu hỏi:

> Hệ thống cần làm gì, cho ai, vì sao cần làm, và làm như thế nào để có thể nghiệm thu được?

Phase này tập trung vào việc xác định:

* Stakeholder needs — nhu cầu của các bên liên quan.
* Business requirements — yêu cầu nghiệp vụ/cấp tổ chức.
* User requirements — yêu cầu từ góc nhìn người dùng.
* System requirements — yêu cầu hệ thống ở mức tổng quát.
* Functional requirements — yêu cầu chức năng cụ thể.
* Non-functional requirements — yêu cầu phi chức năng như hiệu năng, bảo mật, độ tin cậy.
* Use case specification — đặc tả các ca sử dụng chính.
* Acceptance criteria — tiêu chí nghiệm thu.
* Requirements traceability matrix — ma trận truy vết requirement.

---

## 3. Requirement ID Convention

Quy ước ID requirement:

| Prefix | Meaning                    |
| ------ | -------------------------- |
| SN     | Stakeholder Need           |
| BR     | Business Requirement       |
| UR     | User Requirement           |
| SR     | System Requirement         |
| FR     | Functional Requirement     |
| NFR    | Non-functional Requirement |
| UC     | Use Case                   |
| AC     | Acceptance Criteria        |

Ví dụ:

```text
BR-001 -> UR-001 -> SR-001 -> FR-001 -> AC-001
```

---

## 4. Baseline Decisions for Phase 1

Do một số chi tiết chưa được confirm riêng, Phase 1 sử dụng các baseline decision sau:

| ID    | Decision                                                                                                                                                  |
| ----- | --------------------------------------------------------------------------------------------------------------------------------------------------------- |
| PD-01 | MVP ưu tiên MVP Core trước, MVP Extended làm sau nếu còn thời gian.                                                                                       |
| PD-02 | MVP Core tập trung vào flow: Teacher/Admin tạo nội dung → Student làm bài → Hệ thống chấm/lưu bài → Teacher/Admin chấm tự luận → Student/BTC xem kết quả. |
| PD-03 | Question type trong MVP Core gồm Single Choice, Multiple Choice và Essay.                                                                                 |
| PD-04 | Mỗi câu hỏi có thể có điểm riêng.                                                                                                                         |
| PD-05 | Single Choice đúng được full point, sai được 0.                                                                                                           |
| PD-06 | Multiple Choice tính theo all-or-nothing: chọn đúng toàn bộ mới được điểm.                                                                                |
| PD-07 | Không trừ điểm khi trả lời sai trong MVP.                                                                                                                 |
| PD-08 | Câu bỏ trống được 0 điểm.                                                                                                                                 |
| PD-09 | Essay được teacher/admin chấm thủ công.                                                                                                                   |
| PD-10 | Teacher/admin có thể nhập điểm và nhận xét cho bài tự luận.                                                                                               |
| PD-11 | Tổng điểm = điểm trắc nghiệm tự động + điểm tự luận thủ công.                                                                                             |
| PD-12 | Screening Mode thuộc MVP Extended, nhưng vẫn có requirement baseline để chuẩn bị mở rộng.                                                                 |
| PD-13 | Excel import thuộc MVP Extended.                                                                                                                          |
| PD-14 | Contribution workflow thuộc MVP Extended.                                                                                                                 |
| PD-15 | PDF/Word/Image trong MVP chỉ là upload/attachment, không tự động parse thành câu hỏi.                                                                     |

---

## 5. Scope of Phase 1

### 5.1. MVP Core Scope

MVP Core bao gồm:

* Authentication & Authorization.
* User, role và permission cơ bản.
* Subject và topic management.
* Question bank.
* Single choice question.
* Multiple choice question.
* Essay question.
* Practice set.
* Student làm bài và nộp bài.
* Auto grading cho trắc nghiệm.
* Lưu câu trả lời tự luận.
* Teacher/admin chấm tự luận thủ công.
* Student xem kết quả.
* Student xem lịch sử làm bài.
* Teacher/admin xem kết quả bài làm.

### 5.2. MVP Extended Scope

MVP Extended bao gồm:

* Screening event.
* Excel import theo template.
* Contribution workflow.
* Ranking nội bộ/tham khảo.
* Progress statistics cơ bản.
* Upload PDF/Word/Image làm tài liệu hoặc attachment.

### 5.3. Out of Scope for MVP

Không làm trong MVP:

* AI tự sinh câu hỏi.
* AI tự chấm tự luận.
* OCR ảnh.
* Parse PDF/Word phức tạp.
* Chống gian lận nâng cao.
* Thi real-time quy mô lớn.
* Mobile app native.
* Thanh toán.
* Chatbot học tập.

---

# 6. Stakeholder Needs

## 6.1. Stakeholder List

| Stakeholder          | Description                                           |
| -------------------- | ----------------------------------------------------- |
| Student              | Sinh viên ôn luyện Olympic                            |
| Contributor          | Sinh viên được cấp thêm quyền đóng góp nội dung       |
| Teacher              | Giảng viên tạo nội dung, giao bài, chấm tự luận       |
| Admin                | Người quản trị hệ thống, user, role, nội dung         |
| BTC / Organizer      | Ban tổ chức sử dụng dữ liệu để tham khảo khi sàng lọc |
| Faculty / Department | Đơn vị cần hệ thống hỗ trợ ôn luyện Olympic tập trung |

---

## 6.2. Stakeholder Needs

| ID     | Stakeholder          | Need                                                                                   | Priority |
| ------ | -------------------- | -------------------------------------------------------------------------------------- | -------- |
| SN-001 | Faculty / Department | Cần một hệ thống tập trung để hỗ trợ ôn luyện Olympic thay vì dùng nhiều file rời rạc. | Must     |
| SN-002 | Student              | Cần làm bài luyện tập theo môn học và chuyên đề.                                       | Must     |
| SN-003 | Student              | Cần biết kết quả nhanh sau khi làm bài trắc nghiệm.                                    | Must     |
| SN-004 | Student              | Cần xem lịch sử làm bài và tiến độ học tập.                                            | Must     |
| SN-005 | Teacher              | Cần tạo và quản lý ngân hàng câu hỏi tập trung.                                        | Must     |
| SN-006 | Teacher              | Cần giao bài luyện tập cho sinh viên.                                                  | Must     |
| SN-007 | Teacher              | Cần chấm bài tự luận thủ công và nhập nhận xét.                                        | Must     |
| SN-008 | Admin                | Cần quản lý user, role và permission.                                                  | Must     |
| SN-009 | BTC / Organizer      | Cần xem kết quả làm bài để tham khảo khi sàng lọc.                                     | Should   |
| SN-010 | Contributor          | Cần có cơ chế đề xuất câu hỏi hoặc tài liệu vào hệ thống.                              | Could    |
| SN-011 | Teacher/Admin        | Cần import dữ liệu câu hỏi từ Excel template.                                          | Should   |
| SN-012 | Student              | Cần môi trường luyện tập có tính cạnh tranh phù hợp.                                   | Could    |

---

# 7. Business Requirements

| ID     | Business Requirement                                                                         | Source Need    | Priority |
| ------ | -------------------------------------------------------------------------------------------- | -------------- | -------- |
| BR-001 | Khoa cần một nền tảng tập trung để hỗ trợ sinh viên ôn luyện Olympic.                        | SN-001         | Must     |
| BR-002 | Hệ thống phải hỗ trợ quản lý ngân hàng câu hỏi theo môn học và chuyên đề.                    | SN-005         | Must     |
| BR-003 | Hệ thống phải hỗ trợ sinh viên làm bài luyện tập và nhận kết quả.                            | SN-002, SN-003 | Must     |
| BR-004 | Hệ thống phải hỗ trợ cả câu hỏi trắc nghiệm và tự luận.                                      | SN-006, SN-007 | Must     |
| BR-005 | Hệ thống phải lưu lịch sử làm bài và kết quả học tập của sinh viên.                          | SN-004         | Must     |
| BR-006 | Hệ thống phải cho phép teacher/admin chấm bài tự luận thủ công.                              | SN-007         | Must     |
| BR-007 | Hệ thống phải có phân quyền rõ giữa student, teacher, admin, BTC và contributor permission.  | SN-008         | Must     |
| BR-008 | Hệ thống nên hỗ trợ screening event để BTC tham khảo năng lực sinh viên.                     | SN-009         | Should   |
| BR-009 | Hệ thống nên hỗ trợ import câu hỏi từ Excel template để giảm thời gian nhập liệu.            | SN-011         | Should   |
| BR-010 | Hệ thống có thể hỗ trợ contribution workflow cho sinh viên được cấp quyền đóng góp nội dung. | SN-010         | Could    |
| BR-011 | Hệ thống có thể hỗ trợ ranking nội bộ để tăng động lực học tập.                              | SN-012         | Could    |

---

# 8. User Requirements

## 8.1. Student Requirements

| ID     | User Requirement                                                           | Priority |
| ------ | -------------------------------------------------------------------------- | -------- |
| UR-001 | Student cần đăng ký và đăng nhập vào hệ thống.                             | Must     |
| UR-002 | Student cần xem danh sách môn học và chuyên đề.                            | Must     |
| UR-003 | Student cần xem danh sách bài luyện tập có thể làm.                        | Must     |
| UR-004 | Student cần làm bài gồm câu hỏi trắc nghiệm và tự luận.                    | Must     |
| UR-005 | Student cần nộp bài làm.                                                   | Must     |
| UR-006 | Student cần nhận kết quả trắc nghiệm sau khi nộp bài.                      | Must     |
| UR-007 | Student cần xem điểm tự luận sau khi teacher/admin chấm.                   | Must     |
| UR-008 | Student cần xem lịch sử làm bài của mình.                                  | Must     |
| UR-009 | Student cần xem lời giải hoặc feedback nếu bài được cấu hình cho phép xem. | Should   |
| UR-010 | Student cần xem tiến độ học tập cơ bản theo môn/chuyên đề.                 | Should   |

---

## 8.2. Teacher Requirements

| ID     | User Requirement                                              | Priority |
| ------ | ------------------------------------------------------------- | -------- |
| UR-011 | Teacher cần tạo và quản lý môn học/chuyên đề được phân quyền. | Must     |
| UR-012 | Teacher cần tạo câu hỏi trắc nghiệm.                          | Must     |
| UR-013 | Teacher cần tạo câu hỏi tự luận.                              | Must     |
| UR-014 | Teacher cần tạo bài luyện tập từ ngân hàng câu hỏi.           | Must     |
| UR-015 | Teacher cần xem danh sách bài làm của sinh viên.              | Must     |
| UR-016 | Teacher cần chấm bài tự luận thủ công.                        | Must     |
| UR-017 | Teacher cần nhập điểm và nhận xét cho câu trả lời tự luận.    | Must     |
| UR-018 | Teacher cần xem thống kê kết quả bài luyện tập.               | Should   |
| UR-019 | Teacher cần import câu hỏi bằng Excel template.               | Should   |
| UR-020 | Teacher cần review nội dung do contributor gửi lên.           | Could    |

---

## 8.3. Admin Requirements

| ID     | User Requirement                                                           | Priority |
| ------ | -------------------------------------------------------------------------- | -------- |
| UR-021 | Admin cần quản lý user.                                                    | Must     |
| UR-022 | Admin cần quản lý role và permission.                                      | Must     |
| UR-023 | Admin cần quản lý subject, topic và question bank ở phạm vi toàn hệ thống. | Must     |
| UR-024 | Admin cần tạo, sửa, ẩn hoặc lưu trữ nội dung học thuật.                    | Must     |
| UR-025 | Admin cần xem dữ liệu bài làm phục vụ vận hành hệ thống.                   | Should   |
| UR-026 | Admin cần cấp hoặc thu hồi quyền contributor cho student.                  | Could    |
| UR-027 | Admin cần xem audit log cho các thao tác quan trọng.                       | Should   |

---

## 8.4. BTC / Organizer Requirements

| ID     | User Requirement                                          | Priority |
| ------ | --------------------------------------------------------- | -------- |
| UR-028 | BTC cần xem kết quả bài screening để tham khảo.           | Should   |
| UR-029 | BTC cần xem ranking hoặc thống kê năng lực theo đợt/môn.  | Could    |
| UR-030 | BTC cần export kết quả screening.                         | Could    |
| UR-031 | BTC cần biết rõ kết quả hệ thống chỉ mang tính tham khảo. | Must     |

---

## 8.5. Contributor Requirements

| ID     | User Requirement                                      | Priority |
| ------ | ----------------------------------------------------- | -------- |
| UR-032 | Contributor cần gửi đề xuất câu hỏi vào hệ thống.     | Could    |
| UR-033 | Contributor cần gửi lời giải hoặc tài liệu học tập.   | Could    |
| UR-034 | Contributor cần xem trạng thái duyệt nội dung đã gửi. | Could    |

---

# 9. System Requirements

| ID     | System Requirement                                                                              | Related UR             | Priority |
| ------ | ----------------------------------------------------------------------------------------------- | ---------------------- | -------- |
| SR-001 | Hệ thống phải cho phép user đăng ký, đăng nhập và đăng xuất.                                    | UR-001                 | Must     |
| SR-002 | Hệ thống phải phân quyền truy cập theo role và permission.                                      | UR-021, UR-022         | Must     |
| SR-003 | Hệ thống phải cho phép quản lý subject và topic.                                                | UR-002, UR-011, UR-023 | Must     |
| SR-004 | Hệ thống phải cho phép tạo và quản lý question bank.                                            | UR-012, UR-013, UR-023 | Must     |
| SR-005 | Hệ thống phải hỗ trợ single choice, multiple choice và essay question.                          | UR-004, UR-012, UR-013 | Must     |
| SR-006 | Hệ thống phải cho phép teacher/admin tạo practice set từ question bank.                         | UR-014                 | Must     |
| SR-007 | Hệ thống phải cho phép student làm và nộp practice attempt.                                     | UR-004, UR-005         | Must     |
| SR-008 | Hệ thống phải tự động chấm phần trắc nghiệm sau khi student submit attempt.                     | UR-006                 | Must     |
| SR-009 | Hệ thống phải lưu câu trả lời tự luận để teacher/admin chấm thủ công.                           | UR-007, UR-016         | Must     |
| SR-010 | Hệ thống phải cho phép teacher/admin nhập điểm và nhận xét cho bài tự luận.                     | UR-016, UR-017         | Must     |
| SR-011 | Hệ thống phải tính tổng điểm bài làm dựa trên điểm trắc nghiệm và điểm tự luận đã chấm.         | UR-006, UR-007         | Must     |
| SR-012 | Hệ thống phải cho phép student xem kết quả và lịch sử làm bài của mình.                         | UR-008                 | Must     |
| SR-013 | Hệ thống phải cho phép teacher/admin xem kết quả bài làm của sinh viên trong phạm vi quyền hạn. | UR-015, UR-018         | Must     |
| SR-014 | Hệ thống nên hỗ trợ screening event với start time, end time, duration và attempt limit.        | UR-028                 | Should   |
| SR-015 | Hệ thống nên hỗ trợ import câu hỏi từ Excel template.                                           | UR-019                 | Should   |
| SR-016 | Hệ thống có thể hỗ trợ contribution workflow với trạng thái duyệt nội dung.                     | UR-032, UR-033, UR-034 | Could    |
| SR-017 | Hệ thống có thể hỗ trợ ranking nội bộ/tham khảo.                                                | UR-029                 | Could    |
| SR-018 | Hệ thống nên ghi audit log cho các thao tác quan trọng.                                         | UR-027                 | Should   |

---

# 10. Functional Requirements

## 10.1. Authentication & Authorization

| ID          | Functional Requirement                                                                        | Priority |
| ----------- | --------------------------------------------------------------------------------------------- | -------- |
| FR-AUTH-001 | Hệ thống phải cho phép user đăng ký tài khoản bằng thông tin hợp lệ.                          | Must     |
| FR-AUTH-002 | Hệ thống phải cho phép user đăng nhập bằng username/email và password.                        | Must     |
| FR-AUTH-003 | Hệ thống phải cho phép user đăng xuất.                                                        | Must     |
| FR-AUTH-004 | Hệ thống phải từ chối truy cập nếu user chưa đăng nhập vào tài nguyên yêu cầu authentication. | Must     |
| FR-AUTH-005 | Hệ thống phải kiểm tra role/permission trước khi cho user thực hiện thao tác quản trị.        | Must     |

---

## 10.2. User, Role & Permission

| ID          | Functional Requirement                                                                  | Priority |
| ----------- | --------------------------------------------------------------------------------------- | -------- |
| FR-USER-001 | Admin phải có thể xem danh sách user.                                                   | Must     |
| FR-USER-002 | Admin phải có thể tạo, cập nhật hoặc vô hiệu hóa user.                                  | Must     |
| FR-USER-003 | Admin phải có thể gán role cho user.                                                    | Must     |
| FR-USER-004 | Admin phải có thể cấp hoặc thu hồi permission cho user nếu cần.                         | Should   |
| FR-USER-005 | Student có thể được cấp thêm permission `QUESTION_CONTRIBUTE` để trở thành contributor. | Could    |

---

## 10.3. Subject & Topic Management

| ID           | Functional Requirement                                                 | Priority |
| ------------ | ---------------------------------------------------------------------- | -------- |
| FR-SUBJ-001  | Teacher/admin phải có thể tạo subject.                                 | Must     |
| FR-SUBJ-002  | Teacher/admin phải có thể cập nhật subject.                            | Must     |
| FR-SUBJ-003  | Teacher/admin phải có thể ẩn hoặc archive subject không còn sử dụng.   | Should   |
| FR-TOPIC-001 | Teacher/admin phải có thể tạo topic thuộc một subject.                 | Must     |
| FR-TOPIC-002 | Teacher/admin phải có thể cập nhật topic.                              | Must     |
| FR-TOPIC-003 | Hệ thống phải cho phép student xem subject và topic đang được publish. | Must     |

---

## 10.4. Question Bank

| ID         | Functional Requirement                                                                                | Priority |
| ---------- | ----------------------------------------------------------------------------------------------------- | -------- |
| FR-QST-001 | Teacher/admin phải có thể tạo câu hỏi mới.                                                            | Must     |
| FR-QST-002 | Teacher/admin phải có thể chọn loại câu hỏi: single choice, multiple choice hoặc essay.               | Must     |
| FR-QST-003 | Teacher/admin phải có thể gán câu hỏi vào subject và topic.                                           | Must     |
| FR-QST-004 | Teacher/admin phải có thể nhập nội dung câu hỏi.                                                      | Must     |
| FR-QST-005 | Teacher/admin phải có thể cấu hình điểm cho từng câu hỏi.                                             | Must     |
| FR-QST-006 | Teacher/admin phải có thể nhập đáp án đúng cho câu hỏi single choice.                                 | Must     |
| FR-QST-007 | Teacher/admin phải có thể nhập nhiều đáp án đúng cho câu hỏi multiple choice.                         | Must     |
| FR-QST-008 | Teacher/admin phải có thể nhập lời giải hoặc explanation cho câu hỏi.                                 | Should   |
| FR-QST-009 | Teacher/admin phải có thể nhập rubric hoặc hướng dẫn chấm cho câu hỏi essay.                          | Should   |
| FR-QST-010 | Hệ thống phải lưu trạng thái câu hỏi: Draft, Published hoặc Archived trong MVP Core.                  | Must     |
| FR-QST-011 | Hệ thống nên hỗ trợ lifecycle đầy đủ: Draft, Pending Review, Approved, Published, Rejected, Archived. | Could    |
| FR-QST-012 | Hệ thống nên hạn chế sửa trực tiếp câu hỏi đã được dùng trong submission.                             | Should   |

---

## 10.5. Practice Set

| ID          | Functional Requirement                                                         | Priority |
| ----------- | ------------------------------------------------------------------------------ | -------- |
| FR-PRAC-001 | Teacher/admin phải có thể tạo practice set.                                    | Must     |
| FR-PRAC-002 | Teacher/admin phải có thể đặt tên, mô tả và subject/topic cho practice set.    | Must     |
| FR-PRAC-003 | Teacher/admin phải có thể thêm câu hỏi từ question bank vào practice set.      | Must     |
| FR-PRAC-004 | Teacher/admin phải có thể cấu hình practice set là draft hoặc published.       | Must     |
| FR-PRAC-005 | Student chỉ được nhìn thấy practice set đã published.                          | Must     |
| FR-PRAC-006 | Teacher/admin phải có thể cập nhật practice set khi chưa có attempt.           | Should   |
| FR-PRAC-007 | Hệ thống nên hạn chế sửa practice set đã có attempt để tránh sai lệch kết quả. | Should   |

---

## 10.6. Attempt & Submission

| ID         | Functional Requirement                                                         | Priority |
| ---------- | ------------------------------------------------------------------------------ | -------- |
| FR-ATT-001 | Student phải có thể bắt đầu một practice attempt từ practice set đã published. | Must     |
| FR-ATT-002 | Hệ thống phải hiển thị danh sách câu hỏi trong attempt.                        | Must     |
| FR-ATT-003 | Student phải có thể chọn đáp án cho câu hỏi single choice.                     | Must     |
| FR-ATT-004 | Student phải có thể chọn nhiều đáp án cho câu hỏi multiple choice.             | Must     |
| FR-ATT-005 | Student phải có thể nhập câu trả lời cho câu hỏi essay.                        | Must     |
| FR-ATT-006 | Student phải có thể submit attempt.                                            | Must     |
| FR-ATT-007 | Hệ thống phải lưu thời điểm bắt đầu và thời điểm nộp bài.                      | Must     |
| FR-ATT-008 | Hệ thống phải lưu toàn bộ câu trả lời của student.                             | Must     |
| FR-ATT-009 | Hệ thống phải ngăn student chỉnh sửa attempt sau khi đã submit.                | Must     |

---

## 10.7. Auto Grading

| ID          | Functional Requirement                                                           | Priority |
| ----------- | -------------------------------------------------------------------------------- | -------- |
| FR-GRAD-001 | Hệ thống phải tự động chấm câu hỏi single choice sau khi student submit.         | Must     |
| FR-GRAD-002 | Hệ thống phải tự động chấm câu hỏi multiple choice sau khi student submit.       | Must     |
| FR-GRAD-003 | Single choice đúng được full point, sai hoặc bỏ trống được 0.                    | Must     |
| FR-GRAD-004 | Multiple choice đúng toàn bộ được full point, sai hoặc thiếu/thừa đáp án được 0. | Must     |
| FR-GRAD-005 | Hệ thống không trừ điểm khi trả lời sai trong MVP.                               | Must     |
| FR-GRAD-006 | Hệ thống phải lưu điểm trắc nghiệm tự động.                                      | Must     |
| FR-GRAD-007 | Hệ thống phải đánh dấu câu essay là cần chấm thủ công.                           | Must     |

---

## 10.8. Manual Essay Grading

| ID         | Functional Requirement                                                                | Priority |
| ---------- | ------------------------------------------------------------------------------------- | -------- |
| FR-ESS-001 | Teacher/admin phải có thể xem danh sách câu trả lời essay cần chấm.                   | Must     |
| FR-ESS-002 | Teacher/admin phải có thể xem nội dung câu hỏi, bài làm của student và rubric nếu có. | Must     |
| FR-ESS-003 | Teacher/admin phải có thể nhập điểm cho câu trả lời essay.                            | Must     |
| FR-ESS-004 | Teacher/admin phải có thể nhập nhận xét cho câu trả lời essay.                        | Must     |
| FR-ESS-005 | Hệ thống phải lưu điểm và nhận xét tự luận.                                           | Must     |
| FR-ESS-006 | Hệ thống phải cập nhật tổng điểm sau khi tự luận được chấm.                           | Must     |
| FR-ESS-007 | Student phải có thể xem điểm tự luận sau khi bài được chấm và công bố.                | Must     |

---

## 10.9. Result & History

| ID         | Functional Requirement                                                                | Priority |
| ---------- | ------------------------------------------------------------------------------------- | -------- |
| FR-RES-001 | Student phải có thể xem danh sách attempt đã làm.                                     | Must     |
| FR-RES-002 | Student phải có thể xem kết quả từng attempt.                                         | Must     |
| FR-RES-003 | Kết quả phải hiển thị điểm trắc nghiệm, điểm tự luận nếu đã chấm và tổng điểm.        | Must     |
| FR-RES-004 | Hệ thống phải hiển thị trạng thái bài làm: Submitted, Partially Graded, Graded.       | Must     |
| FR-RES-005 | Teacher/admin phải có thể xem kết quả của student trong practice set do mình quản lý. | Must     |
| FR-RES-006 | Hệ thống nên cho phép xem thống kê cơ bản theo practice set.                          | Should   |

---

## 10.10. Screening Event — MVP Extended

| ID         | Functional Requirement                                               | Priority |
| ---------- | -------------------------------------------------------------------- | -------- |
| FR-SCR-001 | Teacher/admin/BTC nên có thể tạo screening event.                    | Should   |
| FR-SCR-002 | Screening event nên có start time, end time và duration.             | Should   |
| FR-SCR-003 | Screening event nên có danh sách student được tham gia.              | Should   |
| FR-SCR-004 | Screening event nên có giới hạn số lần làm.                          | Should   |
| FR-SCR-005 | BTC nên có thể xem kết quả screening event.                          | Should   |
| FR-SCR-006 | Hệ thống phải hiển thị rõ kết quả screening chỉ mang tính tham khảo. | Must     |
| FR-SCR-007 | BTC có thể export kết quả screening.                                 | Could    |

---

## 10.11. Excel Import — MVP Extended

| ID         | Functional Requirement                                                                                                         | Priority |
| ---------- | ------------------------------------------------------------------------------------------------------------------------------ | -------- |
| FR-IMP-001 | Teacher/admin nên có thể upload Excel template để import question bank.                                                        | Should   |
| FR-IMP-002 | Hệ thống nên validate cấu trúc Excel template trước khi import.                                                                | Should   |
| FR-IMP-003 | Hệ thống nên báo lỗi theo từng dòng nếu dữ liệu không hợp lệ.                                                                  | Should   |
| FR-IMP-004 | Hệ thống nên cho preview dữ liệu trước khi import chính thức.                                                                  | Could    |
| FR-IMP-005 | Hệ thống nên import subject, topic, question, options, correct answer, explanation, difficulty và point nếu có trong template. | Should   |

---

## 10.12. Contribution Workflow — MVP Extended

| ID         | Functional Requirement                                        | Priority |
| ---------- | ------------------------------------------------------------- | -------- |
| FR-CON-001 | Student có permission contributor có thể gửi đề xuất câu hỏi. | Could    |
| FR-CON-002 | Nội dung contributor gửi phải có trạng thái Pending Review.   | Could    |
| FR-CON-003 | Teacher/admin có thể approve hoặc reject nội dung đóng góp.   | Could    |
| FR-CON-004 | Nội dung được approve có thể được publish vào question bank.  | Could    |
| FR-CON-005 | Contributor có thể xem trạng thái nội dung đã gửi.            | Could    |

---

## 10.13. Attachment — MVP Extended

| ID          | Functional Requirement                                                  | Priority |
| ----------- | ----------------------------------------------------------------------- | -------- |
| FR-FILE-001 | Teacher/admin có thể upload file PDF/Word/Image làm tài liệu tham khảo. | Could    |
| FR-FILE-002 | Teacher/admin có thể đính kèm ảnh vào câu hỏi hoặc lời giải.            | Could    |
| FR-FILE-003 | Hệ thống phải giới hạn loại file được phép upload.                      | Should   |
| FR-FILE-004 | Hệ thống phải giới hạn dung lượng file upload.                          | Should   |
| FR-FILE-005 | Hệ thống không tự động parse PDF/Word/Image thành câu hỏi trong MVP.    | Must     |

---

# 11. Non-functional Requirements

| ID      | Non-functional Requirement                                                                                              | Priority |
| ------- | ----------------------------------------------------------------------------------------------------------------------- | -------- |
| NFR-001 | Hệ thống phải trả kết quả chấm trắc nghiệm trong vòng 2 giây với bài làm tối đa 100 câu trong điều kiện pilot.          | Must     |
| NFR-002 | Hệ thống phải hỗ trợ tối thiểu 20 sinh viên pilot.                                                                      | Must     |
| NFR-003 | Hệ thống nên hỗ trợ tối thiểu 30 sinh viên làm bài đồng thời trong môi trường pilot.                                    | Should   |
| NFR-004 | Hệ thống phải bảo vệ các endpoint yêu cầu đăng nhập bằng authentication.                                                | Must     |
| NFR-005 | Hệ thống phải kiểm tra authorization trước khi cho user thao tác với dữ liệu không thuộc quyền.                         | Must     |
| NFR-006 | Student chỉ được xem kết quả và lịch sử làm bài của chính mình, trừ khi có quyền cao hơn.                               | Must     |
| NFR-007 | Teacher chỉ được xem/chấm bài trong phạm vi subject/practice set được phân quyền.                                       | Must     |
| NFR-008 | Admin có quyền quản lý toàn hệ thống.                                                                                   | Must     |
| NFR-009 | Hệ thống phải lưu dữ liệu bài làm sau khi student submit thành công.                                                    | Must     |
| NFR-010 | Hệ thống không được cho phép chỉnh sửa attempt sau khi submit.                                                          | Must     |
| NFR-011 | Hệ thống nên ghi audit log cho thao tác tạo/sửa/xóa/publish câu hỏi, tạo bài, chấm tự luận và thay đổi role/permission. | Should   |
| NFR-012 | Hệ thống nên có cơ chế backup dữ liệu định kỳ.                                                                          | Should   |
| NFR-013 | Hệ thống phải validate dữ liệu đầu vào ở cả frontend và backend nếu có frontend.                                        | Must     |
| NFR-014 | API response nên có format lỗi thống nhất.                                                                              | Should   |
| NFR-015 | File upload nên giới hạn tối đa 10MB/file trong MVP.                                                                    | Should   |
| NFR-016 | Hệ thống chỉ cho phép upload các định dạng file được whitelist.                                                         | Should   |
| NFR-017 | Hệ thống nên lưu lịch sử bài làm trong suốt thời gian pilot và không tự động xóa trong MVP.                             | Should   |
| NFR-018 | Codebase nên được thiết kế dễ mở rộng để thêm AI grading, OCR, analytics và import nâng cao ở phase sau.                | Should   |
| NFR-019 | REST API nên được thiết kế nhất quán theo resource naming convention.                                                   | Should   |
| NFR-020 | Hệ thống nên có tài liệu API cơ bản cho frontend/backend integration.                                                   | Should   |

---

# 12. Use Case Specification

## UC-001 — Student làm bài luyện tập

| Field            | Description                                                                        |
| ---------------- | ---------------------------------------------------------------------------------- |
| Use case ID      | UC-001                                                                             |
| Use case name    | Student làm bài luyện tập                                                          |
| Primary actor    | Student                                                                            |
| Supporting actor | System                                                                             |
| Goal             | Student hoàn thành một practice set và nộp bài                                     |
| Precondition     | Student đã đăng nhập; practice set đã published                                    |
| Trigger          | Student chọn một practice set để làm                                               |
| Postcondition    | Attempt được lưu; trắc nghiệm được chấm tự động; tự luận được lưu để chấm thủ công |

### Main Flow

1. Student mở danh sách practice set.
2. Student chọn một practice set đã published.
3. System tạo attempt mới.
4. System hiển thị câu hỏi.
5. Student trả lời single choice, multiple choice hoặc essay.
6. Student submit bài.
7. System lưu câu trả lời.
8. System tự động chấm phần trắc nghiệm.
9. System đánh dấu câu tự luận cần chấm thủ công.
10. System hiển thị kết quả tạm thời cho student.

### Alternative Flow

| Case | Description                                                                                       |
| ---- | ------------------------------------------------------------------------------------------------- |
| A1   | Nếu student chưa đăng nhập, system yêu cầu đăng nhập.                                             |
| A2   | Nếu practice set chưa published, system không cho làm.                                            |
| A3   | Nếu submit thất bại do lỗi hệ thống, system hiển thị thông báo lỗi và không tạo kết quả sai lệch. |

---

## UC-002 — Teacher tạo câu hỏi

| Field            | Description                                  |
| ---------------- | -------------------------------------------- |
| Use case ID      | UC-002                                       |
| Use case name    | Teacher tạo câu hỏi                          |
| Primary actor    | Teacher                                      |
| Supporting actor | System                                       |
| Goal             | Teacher tạo câu hỏi trong question bank      |
| Precondition     | Teacher đã đăng nhập và có quyền tạo câu hỏi |
| Trigger          | Teacher chọn chức năng tạo câu hỏi           |
| Postcondition    | Câu hỏi được lưu vào question bank           |

### Main Flow

1. Teacher mở màn hình question bank.
2. Teacher chọn tạo câu hỏi mới.
3. Teacher chọn subject và topic.
4. Teacher chọn question type.
5. Teacher nhập nội dung câu hỏi.
6. Teacher nhập đáp án/options nếu là trắc nghiệm.
7. Teacher nhập điểm.
8. Teacher nhập lời giải/rubric nếu có.
9. Teacher lưu câu hỏi.
10. System validate dữ liệu.
11. System lưu câu hỏi vào question bank.

---

## UC-003 — Teacher tạo practice set

| Field            | Description                                             |
| ---------------- | ------------------------------------------------------- |
| Use case ID      | UC-003                                                  |
| Use case name    | Teacher tạo practice set                                |
| Primary actor    | Teacher                                                 |
| Supporting actor | System                                                  |
| Goal             | Teacher tạo bài luyện tập từ question bank              |
| Precondition     | Teacher đã đăng nhập và có quyền tạo practice set       |
| Trigger          | Teacher chọn tạo practice set                           |
| Postcondition    | Practice set được tạo ở trạng thái Draft hoặc Published |

### Main Flow

1. Teacher nhập tên và mô tả practice set.
2. Teacher chọn subject/topic.
3. Teacher chọn câu hỏi từ question bank.
4. Teacher kiểm tra danh sách câu hỏi.
5. Teacher lưu practice set.
6. Teacher publish practice set nếu đã sẵn sàng.
7. System hiển thị practice set cho student nếu đã published.

---

## UC-004 — Teacher chấm tự luận

| Field            | Description                                                |
| ---------------- | ---------------------------------------------------------- |
| Use case ID      | UC-004                                                     |
| Use case name    | Teacher chấm tự luận                                       |
| Primary actor    | Teacher                                                    |
| Supporting actor | System                                                     |
| Goal             | Teacher chấm câu trả lời tự luận của student               |
| Precondition     | Student đã submit attempt có câu hỏi essay                 |
| Trigger          | Teacher mở danh sách bài cần chấm                          |
| Postcondition    | Điểm và nhận xét tự luận được lưu; tổng điểm được cập nhật |

### Main Flow

1. Teacher mở danh sách bài cần chấm.
2. Teacher chọn một submission.
3. System hiển thị câu hỏi, câu trả lời của student và rubric nếu có.
4. Teacher nhập điểm.
5. Teacher nhập nhận xét.
6. Teacher lưu kết quả chấm.
7. System cập nhật điểm tự luận.
8. System cập nhật tổng điểm.
9. Student có thể xem kết quả sau khi được công bố.

---

## UC-005 — Student xem lịch sử làm bài

| Field            | Description                                 |
| ---------------- | ------------------------------------------- |
| Use case ID      | UC-005                                      |
| Use case name    | Student xem lịch sử làm bài                 |
| Primary actor    | Student                                     |
| Supporting actor | System                                      |
| Goal             | Student xem lại các bài đã làm và kết quả   |
| Precondition     | Student đã đăng nhập                        |
| Trigger          | Student mở trang lịch sử làm bài            |
| Postcondition    | Student xem được danh sách attempt của mình |

### Main Flow

1. Student mở trang lịch sử làm bài.
2. System lấy danh sách attempt của student.
3. System hiển thị tên bài, thời gian nộp, trạng thái và điểm.
4. Student chọn một attempt.
5. System hiển thị chi tiết kết quả attempt.

---

## UC-006 — Admin quản lý user và quyền

| Field            | Description                            |
| ---------------- | -------------------------------------- |
| Use case ID      | UC-006                                 |
| Use case name    | Admin quản lý user và quyền            |
| Primary actor    | Admin                                  |
| Supporting actor | System                                 |
| Goal             | Admin quản lý user, role và permission |
| Precondition     | Admin đã đăng nhập                     |
| Trigger          | Admin mở trang quản lý user            |
| Postcondition    | User/role/permission được cập nhật     |

### Main Flow

1. Admin mở danh sách user.
2. Admin chọn một user.
3. Admin xem thông tin user.
4. Admin cập nhật role hoặc permission.
5. System validate quyền admin.
6. System lưu thay đổi.
7. System ghi audit log nếu có hỗ trợ.

---

## UC-007 — BTC xem kết quả screening

| Field            | Description                                |
| ---------------- | ------------------------------------------ |
| Use case ID      | UC-007                                     |
| Use case name    | BTC xem kết quả screening                  |
| Primary actor    | BTC / Organizer                            |
| Supporting actor | System                                     |
| Goal             | BTC xem kết quả screening để tham khảo     |
| Precondition     | Screening event đã có submission           |
| Trigger          | BTC mở trang kết quả screening             |
| Postcondition    | BTC xem được kết quả và thống kê tham khảo |

### Main Flow

1. BTC mở danh sách screening event.
2. BTC chọn một screening event.
3. System hiển thị danh sách kết quả.
4. System hiển thị điểm, ranking hoặc thống kê nếu có.
5. System hiển thị thông báo kết quả chỉ mang tính tham khảo.

---

# 13. Acceptance Criteria

## 13.1. Authentication

### AC-AUTH-001 — Đăng nhập thành công

```gherkin
Given user has a valid account
When user submits correct login credentials
Then system authenticates the user
And redirects user to the appropriate home page
```

### AC-AUTH-002 — Đăng nhập thất bại

```gherkin
Given user enters invalid login credentials
When user submits the login form
Then system rejects the login request
And shows an error message
```

---

## 13.2. Question Bank

### AC-QST-001 — Tạo single choice question

```gherkin
Given teacher is logged in
And teacher has permission to create question
When teacher creates a single choice question with valid content, options, correct answer and point
Then system saves the question
And the question appears in the question bank
```

### AC-QST-002 — Tạo multiple choice question

```gherkin
Given teacher is logged in
And teacher has permission to create question
When teacher creates a multiple choice question with valid content, options, correct answers and point
Then system saves the question
And the question appears in the question bank
```

### AC-QST-003 — Tạo essay question

```gherkin
Given teacher is logged in
And teacher has permission to create question
When teacher creates an essay question with valid content and point
Then system saves the question
And marks the question as essay type
```

---

## 13.3. Practice Set

### AC-PRAC-001 — Tạo practice set

```gherkin
Given teacher is logged in
And there are published questions in the question bank
When teacher creates a practice set with valid information and selected questions
Then system saves the practice set
And allows teacher to publish it
```

### AC-PRAC-002 — Student xem practice set published

```gherkin
Given student is logged in
And a practice set is published
When student opens the practice list
Then system shows the published practice set
```

---

## 13.4. Attempt & Submit

### AC-ATT-001 — Student bắt đầu làm bài

```gherkin
Given student is logged in
And practice set is published
When student starts the practice set
Then system creates a new attempt
And shows the questions to the student
```

### AC-ATT-002 — Student submit bài

```gherkin
Given student is doing an attempt
When student submits the attempt
Then system saves all answers
And locks the attempt from further editing
```

---

## 13.5. Auto Grading

### AC-GRAD-001 — Chấm single choice

```gherkin
Given student submitted a single choice answer
When system grades the answer
Then system gives full point if the selected option is correct
And gives zero point if the selected option is incorrect or blank
```

### AC-GRAD-002 — Chấm multiple choice

```gherkin
Given student submitted a multiple choice answer
When system grades the answer
Then system gives full point if the selected options exactly match all correct options
And gives zero point if the answer is missing, extra, incorrect or blank
```

### AC-GRAD-003 — Không trừ điểm

```gherkin
Given student submitted wrong answers
When system calculates the score
Then system does not subtract points beyond zero for each question
```

---

## 13.6. Essay Grading

### AC-ESS-001 — Teacher chấm tự luận

```gherkin
Given student submitted an essay answer
And teacher has permission to grade the submission
When teacher enters a score and comment
Then system saves the essay score and comment
And updates the submission total score
```

### AC-ESS-002 — Student xem điểm tự luận

```gherkin
Given teacher has graded the essay answer
When student opens the submission result
Then system shows the essay score and teacher comment if visible
```

---

## 13.7. Result & History

### AC-RES-001 — Student xem lịch sử

```gherkin
Given student is logged in
When student opens attempt history
Then system shows only attempts created by that student
```

### AC-RES-002 — Teacher xem kết quả

```gherkin
Given teacher is logged in
And teacher has permission to view a practice set
When teacher opens practice set results
Then system shows submissions related to that practice set
```

---

## 13.8. Screening

### AC-SCR-001 — Hiển thị kết quả chỉ tham khảo

```gherkin
Given BTC opens screening result
When system displays the result list
Then system shows a notice that the result is for reference only
And does not present the result as the final selection decision
```

---

## 13.9. Excel Import

### AC-IMP-001 — Validate Excel template

```gherkin
Given teacher/admin uploads an Excel file
When system validates the file
Then system checks required columns
And reports invalid rows if the template is not valid
```

---

# 14. Requirements Traceability Matrix

| Business Requirement | User Requirement               | System Requirement     | Functional Requirement                                                           | Acceptance Criteria                                                        |
| -------------------- | ------------------------------ | ---------------------- | -------------------------------------------------------------------------------- | -------------------------------------------------------------------------- |
| BR-001               | UR-001                         | SR-001                 | FR-AUTH-001, FR-AUTH-002                                                         | AC-AUTH-001, AC-AUTH-002                                                   |
| BR-002               | UR-011, UR-012, UR-013         | SR-003, SR-004, SR-005 | FR-SUBJ-001, FR-TOPIC-001, FR-QST-001 to FR-QST-012                              | AC-QST-001, AC-QST-002, AC-QST-003                                         |
| BR-003               | UR-003, UR-004, UR-005, UR-006 | SR-006, SR-007, SR-008 | FR-PRAC-001 to FR-PRAC-005, FR-ATT-001 to FR-ATT-009, FR-GRAD-001 to FR-GRAD-007 | AC-PRAC-001, AC-PRAC-002, AC-ATT-001, AC-ATT-002, AC-GRAD-001, AC-GRAD-002 |
| BR-004               | UR-012, UR-013, UR-016, UR-017 | SR-005, SR-009, SR-010 | FR-QST-002, FR-QST-006 to FR-QST-009, FR-ESS-001 to FR-ESS-007                   | AC-QST-001, AC-QST-002, AC-QST-003, AC-ESS-001                             |
| BR-005               | UR-008                         | SR-012                 | FR-RES-001 to FR-RES-004                                                         | AC-RES-001                                                                 |
| BR-006               | UR-016, UR-017                 | SR-009, SR-010, SR-011 | FR-ESS-001 to FR-ESS-007                                                         | AC-ESS-001, AC-ESS-002                                                     |
| BR-007               | UR-021, UR-022                 | SR-002                 | FR-AUTH-004, FR-AUTH-005, FR-USER-001 to FR-USER-005                             | AC-AUTH-001, AC-RES-001, AC-RES-002                                        |
| BR-008               | UR-028, UR-029, UR-030, UR-031 | SR-014                 | FR-SCR-001 to FR-SCR-007                                                         | AC-SCR-001                                                                 |
| BR-009               | UR-019                         | SR-015                 | FR-IMP-001 to FR-IMP-005                                                         | AC-IMP-001                                                                 |
| BR-010               | UR-032, UR-033, UR-034         | SR-016                 | FR-CON-001 to FR-CON-005                                                         | To be defined in MVP Extended                                              |
| BR-011               | UR-010, UR-029                 | SR-017                 | FR-SCR-005, FR-SCR-007                                                           | AC-SCR-001                                                                 |

---

# 15. Requirement Quality Checklist

Mỗi requirement trong Phase 1 cần đạt các tiêu chí sau:

| Criterion   | Description                           | Status |
| ----------- | ------------------------------------- | ------ |
| Clear       | Requirement dễ hiểu, không mơ hồ      | Passed |
| Atomic      | Một requirement chỉ mô tả một ý chính | Passed |
| Testable    | Có thể kiểm thử hoặc nghiệm thu       | Passed |
| Traceable   | Có thể truy vết từ business/user need | Passed |
| Prioritized | Có priority Must/Should/Could/Won't   | Passed |
| Feasible    | Phù hợp với phạm vi MVP               | Passed |
| Consistent  | Không mâu thuẫn với Phase 0           | Passed |

---

# 16. Phase 1 Exit Criteria

Phase 1 được xem là hoàn thành khi:

* Có danh sách stakeholder needs có ID.
* Có danh sách business requirements có ID.
* Có danh sách user requirements có ID.
* Có danh sách system requirements có ID.
* Có danh sách functional requirements có ID.
* Có danh sách non-functional requirements có ID.
* Có use case specification cho các flow chính.
* Có acceptance criteria theo format Given/When/Then.
* Có priority theo MoSCoW: Must, Should, Could, Won't.
* Có requirements traceability matrix.
* Requirement không mơ hồ và có thể kiểm thử.
* MVP Core và MVP Extended được phân biệt rõ.
* Các phần out of scope vẫn giữ đúng theo Phase 0.

Với các nội dung trên, Phase 1 có thể được chốt và chuyển sang Phase 2 — System Analysis & Design.

---

# 17. Next Phase

Phase tiếp theo:

# Phase 2 — System Analysis & Design

Các artifact nên tạo ở Phase 2:

* Domain Model.
* Use Case Diagram.
* Activity Diagram.
* Sequence Diagram.
* ERD.
* API Specification.
* Database Design.
* Permission Matrix.
* System Architecture.
* C4 Model.
* UI Wireframe nếu cần.

Phase 2 sẽ chuyển requirement thành thiết kế hệ thống cụ thể để chuẩn bị implementation.

---
