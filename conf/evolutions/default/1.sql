# --- !Ups
DROP TABLE IF EXISTS "author";
DROP TABLE IF EXISTS "book";

CREATE TABLE "author" (
    "author_id" BIGINT GENERATED ALWAYS AS IDENTITY,
    "first_name" VARCHAR NOT NULL,
    "last_name" VARCHAR NOT NULL,
    "birth_date" DATE NOT NULL,
    PRIMARY KEY("author_id")
);

CREATE TABLE "book" (
    "book_id" BIGINT GENERATED ALWAYS AS IDENTITY,
    "title" VARCHAR NOT NULL,
    "author_id" BIGINT NOT NULL,
    PRIMARY KEY("book_id"),
    CONSTRAINT "fk_author"
        FOREIGN KEY("author_id")
        REFERENCES "author"("author_id")
);

# --- !Downs
DROP TABLE IF EXISTS "author";
DROP TABLE IF EXISTS "book";