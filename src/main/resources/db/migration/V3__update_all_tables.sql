DROP TABLE IF EXISTS users;

CREATE TABLE users (
                    id bigserial PRIMARY KEY,
                    auth0_id varchar(255) NOT NULL UNIQUE,
                    username varchar(50) NOT NULL UNIQUE,
                    email varchar(255) UNIQUE,
                    profile_picture text,
                    banner_picture text,
                    bio varchar(200),
                    is_public boolean NOT NULL DEFAULT TRUE,
                    created_at TIMESTAMP DEFAULT NOW(),
                    updated_at TIMESTAMP DEFAULT NOW(),
                    enabled boolean NOT NULL
);

DROP TABLE IF EXISTS posts;

CREATE TABLE posts (
                    id bigserial PRIMARY KEY,
                    user_id bigint NOT NULL,
                    content varchar(250) NOT NULL,
                    picture text,
                    is_public boolean NOT NULL DEFAULT TRUE,
                    created_at TIMESTAMP DEFAULT NOW(),
                    updated_at TIMESTAMP DEFAULT NOW(),
                    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS posts;

DROP TABLE IF EXISTS comments;

CREATE TABLE comments (
                    id bigserial PRIMARY KEY,
                    post_id bigint NOT NULL,
                    user_id bigint NOT NULL,
                    content varchar(250) NOT NULL,
                    created_at TIMESTAMP DEFAULT NOW(),
                    FOREIGN KEY  (post_id) REFERENCES posts(id) ON DELETE CASCADE,
                    FOREIGN KEY  (user_id) REFERENCES users(id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS likes;

CREATE TABLE likes (
                    id bigserial PRIMARY KEY,
                    user_id bigint NOT NULL,
                    post_id bigint NOT NULL,
                    created_at TIMESTAMP DEFAULT NOW(),
                    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS friends;

CREATE TABLE friends (
                    user_1_id bigint NOT NULL,
                    user_2_id bigint NOT NULL,
                    status varchar(20) DEFAULT 'pending' CHECK (status IN ('pending', 'accepted', 'blocked')),
                    PRIMARY KEY (user_1_id, user_2_id),
                    FOREIGN KEY (user_1_id) REFERENCES users(id) ON DELETE CASCADE,
                    FOREIGN KEY (user_2_id) REFERENCES users(id) ON DELETE CASCADE
);