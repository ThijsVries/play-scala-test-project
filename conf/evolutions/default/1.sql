# --- !Ups
DROP TABLE IF EXISTS "author";
DROP TABLE IF EXISTS "book";

CREATE TABLE "author" (
    "author_id" SERIAL PRIMARY KEY,
    "first_name" VARCHAR NOT NULL,
    "last_name" VARCHAR NOT NULL,
    "birth_date" DATE NOT NULL
);

CREATE TABLE "book" (
    "book_id" SERIAL PRIMARY KEY,
    "title" VARCHAR NOT NULL,
    "author_id" BIGINT NOT NULL,
    CONSTRAINT "fk_author"
        FOREIGN KEY("author_id")
        REFERENCES "author"("author_id")
);

# --- !Downs
DROP TABLE IF EXISTS "author";
DROP TABLE IF EXISTS "book";