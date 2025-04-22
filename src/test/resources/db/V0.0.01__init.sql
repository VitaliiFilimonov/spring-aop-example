CREATE SEQUENCE task_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE public.task (
    id int8 NOT NULL,
    title varchar(100) NULL,
    description varchar(100) NULL,
    user_id int8 NULL,
    CONSTRAINT pk_task PRIMARY KEY (id)
);