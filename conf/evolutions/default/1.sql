# --- !Ups
DROP TABLE IF EXISTS "task";

CREATE TABLE "task" (
    "task_id" SERIAL PRIMARY KEY,
    "task_name" VARCHAR NOT NULL,
    "task_description" VARCHAR,
    "due_date" DATE,
    "done" BOOLEAN
);

# --- !Downs
DROP TABLE IF EXISTS "task";