create table subjects (
    id uuid primary key default gen_random_uuid(),
    code varchar(100) not null unique,
    name varchar(255) not null,
    description text,
    status varchar(30) not null default 'ACTIVE',
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    constraint chk_subjects_status check (status in ('ACTIVE', 'ARCHIVED'))
);

create table topics (
    id uuid primary key default gen_random_uuid(),
    subject_id uuid not null references subjects(id),
    name varchar(255) not null,
    description text,
    status varchar(30) not null default 'ACTIVE',
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    constraint uq_topics_subject_name unique (subject_id, name),
    constraint chk_topics_status check (status in ('ACTIVE', 'ARCHIVED'))
);

create index idx_topics_subject_id on topics(subject_id);
