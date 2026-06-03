create table exams (
    id uuid primary key default gen_random_uuid(),
    exam_type varchar(30) not null,
    title varchar(255) not null,
    description text,
    subject_id uuid not null references subjects(id),
    status varchar(30) not null default 'DRAFT',
    start_time timestamptz,
    end_time timestamptz,
    duration_minutes int,
    attempt_limit int,
    show_result boolean not null default true,
    show_explanation boolean not null default false,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    constraint chk_exams_type check (exam_type in ('PRACTICE', 'SCREENING')),
    constraint chk_exams_status check (status in ('DRAFT', 'PUBLISHED', 'CLOSED', 'ARCHIVED')),
    constraint chk_exams_duration check (duration_minutes is null or duration_minutes > 0),
    constraint chk_exams_attempt_limit check (attempt_limit is null or attempt_limit > 0),
    constraint chk_exams_time check (end_time is null or start_time is null or end_time > start_time)
);

create table exam_questions (
    id uuid primary key default gen_random_uuid(),
    exam_id uuid not null references exams(id),
    question_id uuid not null references questions(id),
    order_index int not null,
    point_override numeric(8,2),
    constraint uq_exam_questions_question unique (exam_id, question_id),
    constraint uq_exam_questions_order unique (exam_id, order_index),
    constraint chk_exam_questions_order check (order_index >= 1),
    constraint chk_exam_questions_point_override check (point_override is null or point_override >= 0)
);

create table exam_attempts (
    id uuid primary key default gen_random_uuid(),
    exam_id uuid not null references exams(id),
    student_id uuid not null references users(id),
    status varchar(50) not null default 'IN_PROGRESS',
    started_at timestamptz not null default now(),
    submitted_at timestamptz,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    constraint chk_exam_attempts_status check (status in (
        'IN_PROGRESS',
        'SUBMITTED',
        'AUTO_GRADED',
        'PENDING_MANUAL_GRADING',
        'PARTIALLY_GRADED',
        'GRADED'
    )),
    constraint chk_exam_attempts_time check (submitted_at is null or submitted_at >= started_at)
);

create table attempt_answers (
    id uuid primary key default gen_random_uuid(),
    attempt_id uuid not null references exam_attempts(id),
    question_id uuid not null references questions(id),
    answer_text text,
    auto_score numeric(8,2),
    manual_score numeric(8,2),
    final_score numeric(8,2),
    grading_status varchar(30) not null default 'NOT_REQUIRED',
    constraint uq_attempt_answers_question unique (attempt_id, question_id),
    constraint chk_attempt_answers_auto_score check (auto_score is null or auto_score >= 0),
    constraint chk_attempt_answers_manual_score check (manual_score is null or manual_score >= 0),
    constraint chk_attempt_answers_final_score check (final_score is null or final_score >= 0),
    constraint chk_attempt_answers_grading_status check (grading_status in ('NOT_REQUIRED', 'PENDING', 'GRADED'))
);

create table attempt_answer_options (
    attempt_answer_id uuid not null references attempt_answers(id),
    question_option_id uuid not null references question_options(id),
    primary key (attempt_answer_id, question_option_id)
);

create index idx_exams_subject_id on exams(subject_id);
create index idx_exams_status on exams(status);
create index idx_exam_attempts_exam_id on exam_attempts(exam_id);
create index idx_exam_attempts_student_id on exam_attempts(student_id);
create index idx_attempt_answers_attempt_id on attempt_answers(attempt_id);
