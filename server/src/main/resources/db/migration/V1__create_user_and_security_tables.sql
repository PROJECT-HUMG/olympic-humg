create extension if not exists pgcrypto;

create table users (
    id uuid primary key default gen_random_uuid(),
    email varchar(255) not null unique,
    password_hash varchar(255) not null,
    full_name varchar(255) not null,
    status varchar(30) not null default 'ACTIVE',
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    constraint chk_users_status check (status in ('ACTIVE', 'DISABLED'))
);

create table roles (
    id uuid primary key default gen_random_uuid(),
    code varchar(100) not null unique,
    name varchar(255) not null
);

create table permissions (
    id uuid primary key default gen_random_uuid(),
    code varchar(100) not null unique,
    name varchar(255) not null
);

create table user_roles (
    user_id uuid not null references users(id),
    role_id uuid not null references roles(id),
    primary key (user_id, role_id)
);

create table role_permissions (
    role_id uuid not null references roles(id),
    permission_id uuid not null references permissions(id),
    primary key (role_id, permission_id)
);
