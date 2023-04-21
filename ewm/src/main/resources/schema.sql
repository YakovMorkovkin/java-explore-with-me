DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS participation_requests CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS compilations_events CASCADE;

CREATE TABLE IF NOT EXISTS users
  (
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name                            VARCHAR(50)        NOT NULL,
    email                           VARCHAR(50)        NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uq_mail UNIQUE (email)
  );

CREATE TABLE IF NOT EXISTS categories
  (
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name                            VARCHAR(50)        NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id),
    CONSTRAINT uq_categories_name UNIQUE (name)
  );
CREATE TABLE IF NOT EXISTS locations
  (
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    lat        REAL                                    NOT NULL,
    lon        REAL                                    NOT NULL,
    CONSTRAINT pk_locations PRIMARY KEY (id),
    CONSTRAINT uq_location UNIQUE (lat, lon)
  );

CREATE TABLE IF NOT EXISTS events
  (
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation                            VARCHAR(2000)         NOT NULL,
    category_id                           BIGINT                NOT NULL,
    initiator_id                          BIGINT                NOT NULL,
    confirmed_requests                                          BIGINT,
    description                           VARCHAR(7000)         NOT NULL,
    created_on                            TIMESTAMP WITHOUT TIME ZONE,
    published_on                          TIMESTAMP WITHOUT TIME ZONE,
    event_date                            TIMESTAMP WITHOUT TIME ZONE,
    location_id                           BIGINT                NOT NULL,
    paid                                  BOOLEAN NOT NULL DEFAULT FALSE,
    participant_limit                     BIGINT               DEFAULT 0,
    views                                                         BIGINT,
    request_moderation                    BOOLEAN NOT NULL DEFAULT FALSE,
    title                                 VARCHAR(120)          NOT NULL,
    state                                 VARCHAR(20)           NOT NULL,
    CONSTRAINT pk_events PRIMARY KEY (id),
    CONSTRAINT fk_initiator_id FOREIGN KEY (initiator_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_category_id FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_location_id FOREIGN KEY (location_id) REFERENCES locations  (id) ON DELETE CASCADE ON UPDATE CASCADE
  );

CREATE TABLE IF NOT EXISTS participation_requests
  (
    id             BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    requester_id   BIGINT                                  NOT NULL,
    event_id       BIGINT                                  NOT NULL,
    created                             TIMESTAMP WITHOUT TIME ZONE,
    status          VARCHAR(30)                             NOT NULL,
    CONSTRAINT pk_requests PRIMARY KEY (id),
    CONSTRAINT fk_requester_id FOREIGN KEY (requester_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_event_id FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE ON UPDATE CASCADE
  );

CREATE TABLE IF NOT EXISTS compilations
  (
    id             BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned         BOOLEAN                   NOT NULL DEFAULT FALSE,
    title          VARCHAR(100)                            NOT NULL,
    CONSTRAINT pk_compilations PRIMARY KEY (id),
    CONSTRAINT uq_compilation UNIQUE (title)
  );

CREATE TABLE IF NOT EXISTS compilations_events
  (
    compilation_id   BIGINT                                  NOT NULL,
    events_id         BIGINT                                          ,
    CONSTRAINT fk_compilation_id FOREIGN KEY (compilation_id) REFERENCES compilations (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_compilation_event_id FOREIGN KEY (events_id) REFERENCES events (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT uq_compilations_events UNIQUE (compilation_id, events_id)
  );