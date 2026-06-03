insert into roles (code, name)
values
    ('STUDENT', 'Student'),
    ('TEACHER', 'Teacher'),
    ('ADMIN', 'Admin'),
    ('BTC', 'Organizer');

insert into permissions (code, name)
values
    ('USER_MANAGE', 'Manage users'),
    ('ROLE_MANAGE', 'Manage roles'),
    ('SUBJECT_MANAGE', 'Manage subjects'),
    ('TOPIC_MANAGE', 'Manage topics'),
    ('QUESTION_CREATE', 'Create questions'),
    ('QUESTION_UPDATE', 'Update questions'),
    ('QUESTION_PUBLISH', 'Publish questions'),
    ('QUESTION_ARCHIVE', 'Archive questions'),
    ('QUESTION_CONTRIBUTE', 'Contribute questions'),
    ('ASSESSMENT_CREATE', 'Create assessments'),
    ('ASSESSMENT_PUBLISH', 'Publish assessments'),
    ('ATTEMPT_SUBMIT', 'Submit attempts'),
    ('ESSAY_GRADE', 'Grade essays'),
    ('RESULT_VIEW_OWN', 'View own results'),
    ('RESULT_VIEW_ALL', 'View all results'),
    ('SCREENING_VIEW', 'View screening results');

insert into subjects (code, name, description)
values
    ('MATH', 'Mathematics', 'Olympic Mathematics subject'),
    ('PHYSICS', 'Physics', 'Olympic Physics subject'),
    ('CHEMISTRY', 'Chemistry', 'Olympic Chemistry subject'),
    ('INFORMATICS', 'Informatics', 'Olympic Informatics subject');

insert into role_permissions (role_id, permission_id)
select r.id, p.id
from roles r
join permissions p on p.code in ('ATTEMPT_SUBMIT', 'RESULT_VIEW_OWN')
where r.code = 'STUDENT';

insert into role_permissions (role_id, permission_id)
select r.id, p.id
from roles r
join permissions p on p.code in (
    'SUBJECT_MANAGE',
    'TOPIC_MANAGE',
    'QUESTION_CREATE',
    'QUESTION_UPDATE',
    'QUESTION_PUBLISH',
    'QUESTION_ARCHIVE',
    'ASSESSMENT_CREATE',
    'ASSESSMENT_PUBLISH',
    'ESSAY_GRADE',
    'RESULT_VIEW_ALL',
    'SCREENING_VIEW'
)
where r.code = 'TEACHER';

insert into role_permissions (role_id, permission_id)
select r.id, p.id
from roles r
cross join permissions p
where r.code = 'ADMIN';

insert into role_permissions (role_id, permission_id)
select r.id, p.id
from roles r
join permissions p on p.code in ('RESULT_VIEW_ALL', 'SCREENING_VIEW')
where r.code = 'BTC';
