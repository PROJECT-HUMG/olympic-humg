# Phase 0 Confirmation — Olympic Learning Platform

## 1. Product Purpose — Mục đích sản phẩm

App này được xây dựng để:

* [ ] Hỗ trợ sinh viên ôn luyện cá nhân
* [ ] Hỗ trợ tổ chức các đợt luyện tập/sàng lọc Olympic nội bộ
* [x] Cả hai

Diễn giải của mình:

Hỗ trợ sinh viên ôn luyện, học tập, nắm bắt thông báo, biết năng lực của mình so với các bạn (có bảng xếp hạng)
Giúp giảng viên nắm bắt được các mầm mống tốt để triển khai, quản lý nhiều câu hỏi, đề thi, tài liệu, giao bài tập, chấm bài tập
Giúp bên ban tổ chức thay vì chỉ tổ chức 1 vòng cuối (vì không có thời gian) thì có thể tổ chức nhiều cuộc thi, và bài tập, lộ trình học các thứ

Kết quả trên hệ thống có phải chỉ mang tính tham khảo, quyết định cuối cùng thuộc về BTC không?

* [x] Có
* [ ] Không

Đương nhiên rồi, kết quả phải thuộc về BTC, trên hệ thống chỉ mang tính chất tham khảo

## 2. Main Pain Points — Nỗi đau chính

### 2.1. Pain point của sinh viên

Các vấn đề sinh viên đang gặp:

* [x] Tài liệu ôn luyện bị rải rác: Excel, PDF, Word, ảnh, đề cũ, nhóm chat
* [x] Không biết nên ôn từ đâu
* [x] Không biết mình yếu chuyên đề nào
* [x] Làm bài xong không có feedback nhanh
* [x] Không có lịch sử theo dõi tiến bộ
* [x] Thiếu môi trường luyện tập giống thi/sàng lọc thật
* [x] Khác: không có tính cạnh tranh nhiều, không được hỗ trợ kịp thời, ...

Mô tả thêm:

> ...

### 2.2. Pain point của giảng viên/admin/BTC

Các vấn đề phía quản lý nội dung/BTC đang gặp:

* [x] Câu hỏi nằm rải rác ở nhiều file/nguồn
* [x] Khó quản lý ngân hàng câu hỏi tập trung
* [x] Khó chuẩn hóa câu hỏi, đáp án, lời giải, độ khó
* [x] Khó tái sử dụng câu hỏi cho nhiều đợt luyện tập/sàng lọc
* [x] Khó quản lý sinh viên đóng góp câu hỏi
* [x] Khó phê duyệt/chỉnh sửa nội dung được đóng góp
* [x] Khó xem dữ liệu kết quả để hỗ trợ sàng lọc
* [x] Khác: không biết năng lực của các sinh viên, giao bài tập xong hay bị quên vì nhiều sinh viên, phải gửi lại lời giải cho nhiều sinh viên, không tổ chức kiểm tra được

Mô tả thêm:

> ...

---

## 3. Target Users — Nhóm người dùng

Các nhóm người dùng cần có:

* [x] Student — sinh viên ôn luyện/làm bài/xem kết quả
* [x] Contributor — sinh viên được cấp quyền đóng góp nội dung
* [x] Teacher — giảng viên tạo/review nội dung học thuật
* [x] Admin — quản lý hệ thống, user, quyền, dữ liệu
* [x] Organizer/BTC — xem kết quả sàng lọc, hỗ trợ quyết định
* [ ] Khác: ...

Contributor nên được thiết kế là:

* [ ] Role riêng, ví dụ `ROLE_CONTRIBUTOR`
* [x] Student có thêm permission, ví dụ `QUESTION_CONTRIBUTE`
* [ ] Chưa chốt

Ghi chú:

> ...

---

## 4. Usage Context — Bối cảnh sử dụng

App có các mode chính:

* [ ] Practice Mode — sinh viên luyện tập cá nhân
* [ ] Screening Mode — tổ chức đợt sàng lọc/luyện tập Olympic nội bộ
* [x] Cả hai

Mode ưu tiên nhất là:

* [ ] Practice Mode
* [ ] Screening Mode
* [x] Cả hai ngang nhau

Screening Mode có vai trò:

* [ ] Chỉ hỗ trợ BTC tham khảo
* [x] Có ranking/sàng lọc tự động
* [ ] Chưa chốt

Ghi chú:

> ...

---

## 5. MVP Scope — Phạm vi bản đầu

Đánh dấu theo MoSCoW:

* `M` = Must have — bắt buộc MVP có
* `S` = Should have — nên có
* `C` = Could have — có thì tốt
* `W` = Won't have — chưa làm ở MVP

| Feature                              | M/S/C/W | Ghi chú |
| ------------------------------------ | ------- | ------- |
| Đăng ký / đăng nhập                  |    M     |         |
| Quản lý user cơ bản                  |     M    |         |
| Quản lý role/permission cơ bản       |      M   |         |
| Sinh viên xin quyền đóng góp câu hỏi |      S   |         |
| Admin/teacher duyệt quyền đóng góp   |       S  |         |
| Quản lý môn học                      |      S   |         |
| Quản lý chuyên đề                    |      S   |         |
| Quản lý ngân hàng câu hỏi            |      S   |         |
| Hỗ trợ câu hỏi trắc nghiệm           |      C   |         |
| Hỗ trợ câu hỏi tự luận               |      C   |         |
| Tạo bài luyện tập                    |      S   |         |
| Tạo bài/đợt sàng lọc                 |      S   |         |
| Sinh viên làm bài                    |      S   |         |
| Sinh viên nộp bài                    |      S   |         |
| Tự chấm phần trắc nghiệm             |      S   |         |
| Lưu bài tự luận để chấm              |      S   |         |
| Teacher/admin chấm tự luận thủ công  |      S   |         |
| Xem kết quả                          |      S   |         |
| Xem lịch sử làm bài                  |      S   |         |
| Thống kê tiến độ cá nhân             |      S   |         |
| BTC xem kết quả sàng lọc             |      S   |         |
| Import Excel                         |      S   |         |
| Import PDF/Word                      |      S   |         |
| OCR ảnh PNG/JPG                      |      W   |         |
| AI tự chấm tự luận                   |      W   |         |
| AI sinh câu hỏi                      |      W  |         |

Ghi chú thêm về MVP:

> ...

---

## 6. Essay Question Support — Hỗ trợ tự luận

MVP cần hỗ trợ tự luận ở mức:

* [ ] Level 1: Tạo câu hỏi tự luận + sinh viên nhập câu trả lời + lưu lại
* [x] Level 2: Teacher/admin chấm tự luận thủ công, nhập điểm, nhận xét
* [ ] Level 3: AI/rule tự động chấm tự luận
* [ ] Chưa chốt

Ghi chú:

> ...

---

## 7. Data Input — Nguồn dữ liệu đầu vào

Nguồn dữ liệu hệ thống có thể nhận về lâu dài:

* [x] Nhập tay
* [x] Excel
* [x] PDF
* [x] Word
* [x] Ảnh PNG/JPG
* [ ] Tự tạo mới trong hệ thống
* [ ] Khác: ...

Trong MVP bắt buộc hỗ trợ:

* [x] Nhập tay
* [x] Excel
* [x] PDF
* [x] Word
* [x] Ảnh PNG/JPG
* [ ] Chưa chốt

Ghi chú:

> ...

---

## 8. Product Goals — Mục tiêu sản phẩm

Các mục tiêu chính:

* [x] G-01: Giúp sinh viên ôn luyện theo môn/chuyên đề
* [x] G-02: Hỗ trợ luyện tập cá nhân và sàng lọc nội bộ
* [x] G-03: Tập trung hóa ngân hàng câu hỏi từ nhiều nguồn
* [x] G-04: Cho phép giảng viên/admin/sinh viên được cấp quyền đóng góp nội dung
* [x] G-05: Hỗ trợ cả trắc nghiệm và tự luận
* [x] G-06: Lưu lịch sử làm bài và kết quả
* [x] G-07: Cung cấp dữ liệu tham khảo cho BTC khi sàng lọc
* [x] Goal khác: bảng xếp hạng, có lộ trình 

Ghi chú:

> ...

---

## 9. Success Metrics — Tiêu chí đo thành công

Các tiêu chí thành công cho MVP:

* [x] SM-01: Sinh viên có thể hoàn thành một bài luyện tập/sàng lọc trên hệ thống
* [x] SM-02: Hệ thống tự chấm được phần trắc nghiệm
* [x] SM-03: Hệ thống lưu được phần tự luận để teacher/admin chấm
* [x] SM-04: Người có quyền có thể tạo câu hỏi và gửi qua luồng phê duyệt
* [x] SM-05: Admin/teacher có thể tạo bài luyện tập hoặc bài sàng lọc
* [x] SM-06: BTC có thể xem kết quả làm bài để tham khảo
* [x] SM-07: Sinh viên xem được lịch sử làm bài và kết quả của mình
* [x] SM-08: Câu hỏi được quản lý tập trung thay vì nằm rải rác ở nhiều file

Metric định lượng nếu có:

* Số môn học tối thiểu: 4
* Số câu hỏi tối thiểu: 40
* Số sinh viên thử nghiệm tối thiểu: 20
* Thời gian trả kết quả mong muốn: 4 ngày
* Số định dạng import cần hỗ trợ trong MVP: Excel, PDF, Word, text
* Khác: ...

---

## 10. Out of Scope — Chưa làm ở MVP

Các phần chưa làm trong MVP:

* [x] AI tự sinh câu hỏi
* [x] AI tự chấm tự luận hoàn toàn
* [x] OCR ảnh PNG/JPG
* [x] Parse PDF/Word phức tạp
* [x] Chống gian lận nâng cao
* [x] Thi real-time quy mô lớn
* [x] Leaderboard công khai
* [x] Mobile app native
* [x] Thanh toán
* [x] Chatbot học tập
* [ ] Khác: ...

Ghi chú:

> ...

---

## 11. Assumptions — Giả định ban đầu

Các giả định hiện tại:

* [x] Khoa có nhu cầu dùng thật, không chỉ demo
* [x] Luyện tập là mục tiêu ưu tiên nhất
* [x] Sàng lọc là mục tiêu phụ/hỗ trợ
* [x] Kết quả hệ thống không thay thế quyết định của BTC
* [x] Dữ liệu ban đầu có thể chưa chuẩn
* [x] Sinh viên có thể đóng góp nội dung nếu được cấp quyền
* [ ] MVP cần hỗ trợ tự luận
* [x] Backend dùng Spring Boot
* [x] Hệ thống expose REST API cho frontend

Giả định khác:

> ...

---

## 12. Constraints — Ràng buộc

Các ràng buộc chính:

* [x] Dùng thật trong khoa nên cần thiết kế cẩn thận
* [ ] MVP có tự luận nên grading phức tạp
* [ ] Dữ liệu đầu vào đa dạng
* [x] Cần approval workflow cho nội dung đóng góp
* [x] Cần phân biệt luyện tập và sàng lọc
* [x] Cần phân quyền rõ
* [x] Cần thiết kế mở rộng để thêm AI/import/OCR sau này
* [ ] Khác: ...

Ghi chú:

> ...

---

## 13. Key Risks — Rủi ro chính

Các rủi ro cần lưu ý:

* [ ] R-01: MVP quá rộng
* [ ] R-02: Tự luận khó chấm tự động
* [x] R-03: PDF/Word/ảnh khó parse chuẩn
* [x] R-04: Sinh viên đóng góp nội dung có thể làm dữ liệu loạn nếu thiếu phê duyệt
* [x] R-05: Dùng thật trong khoa nên cần bảo mật/phân quyền/backup tốt
* [ ] R-06: Kết quả sàng lọc có thể bị hiểu nhầm là quyết định cuối cùng
* [ ] R-07: Auth/role/permission phức tạp ngay từ đầu
* [ ] Khác: ...

Cách giảm rủi ro dự kiến:

> ...

---

## 14. Decision Log — Quyết định đã chốt

Các quyết định hiện tại:

* D-01: App phục vụ cả ôn luyện cá nhân và sàng lọc Olympic nội bộ.
* D-02: Mục tiêu ưu tiên là luyện tập.
* D-03: Kết quả sàng lọc chỉ hỗ trợ BTC; quyết định cuối cùng thuộc BTC.
* D-04: Người tạo nội dung gồm giảng viên, admin và sinh viên được cấp quyền.
* D-05: Cần có cơ chế xin quyền và phê duyệt quyền đóng góp.
* D-06: Dữ liệu đầu vào có thể đến từ Excel, PDF, Word, ảnh, nhập tay hoặc tự tạo mới.
* D-07: MVP không bắt buộc hỗ trợ tự luận.
* D-08: Project hướng tới dùng thật trong khoa, không chỉ demo kỹ thuật.

Cần sửa/thêm decision:

> ...

---

## 15. Final Notes — Ghi chú thêm

Những điểm khác cần lưu ý:

> ...
