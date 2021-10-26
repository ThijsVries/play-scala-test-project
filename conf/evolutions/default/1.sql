# --- !Ups
DROP TABLE IF EXISTS "author";
DROP TABLE IF EXISTS "book";

CREATE TABLE "author" (
    "author_id" BIGINT GENERATED ALWAYS AS IDENTITY,
    "firstName" VARCHAR NOT NULL,
    "lastName" VARCHAR NOT NULL,
    "birthDate" DATE NOT NULL,
    PRIMARY KEY("author_id")
);

CREATE TABLE "book" (
    "book_id" BIGINT GENERATED ALWAYS AS IDENTITY,
    "title" VARCHAR NOT NULL,
    "author_id" BIGINT,
    CONSTRAINT fk_author_id
    FOREIGN KEY(author_id)
    REFERENCES author(author_id)
);

# --- !Downs

drop table "author" if exists;
drop table "book" if exists;