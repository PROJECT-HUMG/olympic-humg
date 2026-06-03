create table questions (
    id uuid primary key default gen_random_uuid(),
    topic_id uuid not null references topics(id),
    question_type varchar(30) not null,
    content text not null,
    point numeric(8,2) not null default 1,
    difficulty varchar(30),
    status varchar(30) not null default 'DRAFT',
    explanation text,
    rubric text,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    constraint chk_questions_type check (question_type in ('SINGLE_CHOICE', 'MULTIPLE_CHOICE', 'ESSAY')),
    constraint chk_questions_point check (point >= 0),
    constraint chk_questions_difficulty check (difficulty is null or difficulty in ('EASY', 'MEDIUM', 'HARD')),
    constraint chk_questions_status check (status in ('DRAFT', 'PUBLISHED', 'ARCHIVED'))
);

create table question_options (
    id uuid primary key default gen_random_uuid(),
    question_id uuid not null references questions(id),
    content text not null,
    is_correct boolean not null default false,
    order_index int not null,
    constraint uq_question_options_order unique (question_id, order_index),
    constraint chk_question_options_order check (order_index >= 1)
);

create index idx_questions_topic_id on questions(topic_id);
create index idx_questions_status on questions(status);
