create table essay_gradings (
    id uuid primary key default gen_random_uuid(),
    attempt_answer_id uuid not null unique references attempt_answers(id),
    graded_by uuid not null references users(id),
    score numeric(8,2) not null,
    comment text,
    graded_at timestamptz not null default now(),
    constraint chk_essay_gradings_score check (score >= 0)
);

create table results (
    id uuid primary key default gen_random_uuid(),
    attempt_id uuid not null unique references exam_attempts(id),
    auto_score numeric(8,2) not null default 0,
    manual_score numeric(8,2) not null default 0,
    total_score numeric(8,2) not null default 0,
    grading_status varchar(50) not null,
    calculated_at timestamptz not null default now(),
    constraint chk_results_auto_score check (auto_score >= 0),
    constraint chk_results_manual_score check (manual_score >= 0),
    constraint chk_results_total_score check (total_score >= 0)
);

create index idx_results_attempt_id on results(attempt_id);
