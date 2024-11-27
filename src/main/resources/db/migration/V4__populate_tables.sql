DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS users_id_seq;

CREATE SEQUENCE IF NOT EXISTS users_id_seq;
CREATE TABLE users (
                    id bigserial PRIMARY KEY,
                    auth0_id varchar(255) NOT NULL UNIQUE,
                    username varchar(50) NOT NULL UNIQUE,
                    email varchar(255) UNIQUE,
                    first_name varchar(50),
                    middle_name varchar(50),
                    last_name varchar(50),
                    birthday date,
                    profile_picture text,
                    banner_picture text,
                    bio varchar(200),
                    location varchar(50),
                    workplace varchar(50),
                    is_public boolean NOT NULL DEFAULT TRUE,
                    created_at TIMESTAMP DEFAULT NOW(),
                    updated_at TIMESTAMP DEFAULT NOW(),
                    enabled boolean NOT NULL
);

DROP TABLE IF EXISTS posts;
DROP SEQUENCE IF EXISTS posts_id_seq;

CREATE SEQUENCE IF NOT EXISTS posts_id_seq;
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

DROP TABLE IF EXISTS comments;
DROP SEQUENCE IF EXISTS comments_id_seq;

CREATE SEQUENCE IF NOT EXISTS comments_id_seq;
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
DROP SEQUENCE IF EXISTS likes_id_seq;

CREATE SEQUENCE IF NOT EXISTS likes_id_seq;
CREATE TABLE likes (
                       id bigserial PRIMARY KEY,
                       user_id bigint NOT NULL,
                       post_id bigint NOT NULL,
                       created_at TIMESTAMP DEFAULT NOW(),
                       FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                       FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS friends;
DROP SEQUENCE IF EXISTS friends_id_seq;

CREATE SEQUENCE IF NOT EXISTS friends_id_seq;
CREATE TABLE friends (
                         user_1_id bigint NOT NULL,
                         user_2_id bigint NOT NULL,
                         status varchar(20) DEFAULT 'pending' CHECK (status IN ('pending', 'accepted', 'blocked')),
                         PRIMARY KEY (user_1_id, user_2_id),
                         FOREIGN KEY (user_1_id) REFERENCES users(id) ON DELETE CASCADE,
                         FOREIGN KEY (user_2_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Seed users:
INSERT INTO users (auth0_id, username, email, first_name, middle_name, last_name, birthday, profile_picture, banner_picture, bio, location, workplace, is_public, created_at, updated_at, enabled)
VALUES
    ('auth0|A1B2C3D4E5F6', 'john_doe', 'john.doe@example.com', 'John', 'Michael', 'Doe', '1990-02-15', 'https://example.com/profile1.jpg', 'https://example.com/banner1.jpg', 'Loves technology and software development.', 'New York, USA', 'TechCorp', TRUE, NOW(), NOW(), TRUE),
    ('auth0|G7H8I9J0K1L2', 'jane_smith', 'jane.smith@example.com', 'Jane', 'Marie', 'Smith', '1985-06-25', 'https://example.com/profile2.jpg', 'https://example.com/banner2.jpg', 'Passionate about graphic design and art.', 'Los Angeles, USA', 'DesignWorks', TRUE, NOW(), NOW(), TRUE),
    ('auth0|M3N4O5P6Q7R8', 'alex_lee', 'alex.lee@example.com', 'Alex', 'Jaden', 'Lee', '1992-03-10', 'https://example.com/profile3.jpg', 'https://example.com/banner3.jpg', 'Coffee enthusiast and software engineer.', 'San Francisco, USA', 'CodeX Solutions', TRUE, NOW(), NOW(), TRUE),
    ('auth0|S9T0U1V2W3X4', 'chris_jones', 'chris.jones@example.com', 'Chris', 'David', 'Jones', '1988-11-05', 'https://example.com/profile4.jpg', 'https://example.com/banner4.jpg', 'Travel lover, writer, and photographer.', 'Chicago, USA', 'Wanderlust Media', TRUE, NOW(), NOW(), TRUE),
    ('auth0|Z5A6B7C8D9E0', 'samantha_brown', 'samantha.brown@example.com', 'Samantha', 'Grace', 'Brown', '1995-09-12', 'https://example.com/profile5.jpg', 'https://example.com/banner5.jpg', 'Marketing professional and yoga instructor.', 'Austin, USA', 'MarketUp', TRUE, NOW(), NOW(), TRUE),
    ('auth0|K1L2M3N4O5P6', 'michael_johnson', 'michael.johnson@example.com', 'Michael', 'William', 'Johnson', '1987-12-30', 'https://example.com/profile6.jpg', 'https://example.com/banner6.jpg', 'Outdoor enthusiast and web developer.', 'Seattle, USA', 'WebWorks', TRUE, NOW(), NOW(), TRUE),
    ('auth0|Q7R8S9T0U1V2', 'linda_davis', 'linda.davis@example.com', 'Linda', 'Ann', 'Davis', '1991-05-22', 'https://example.com/profile7.jpg', 'https://example.com/banner7.jpg', 'Health and wellness advocate.', 'Miami, USA', 'FitLife', TRUE, NOW(), NOW(), TRUE),
    ('auth0|Y1Z2A3B4C5D6', 'robert_miller', 'robert.miller@example.com', 'Robert', 'Andrew', 'Miller', '1983-07-17', 'https://example.com/profile8.jpg', 'https://example.com/banner8.jpg', 'Guitarist and software architect.', 'Boston, USA', 'TechDev', TRUE, NOW(), NOW(), TRUE),
    ('auth0|J4K5L6M7N8O9', 'emily_wilson', 'emily.wilson@example.com', 'Emily', 'Rose', 'Wilson', '1994-01-03', 'https://example.com/profile9.jpg', 'https://example.com/banner9.jpg', 'Digital marketer and photography enthusiast.', 'Denver, USA', 'AdVantage', TRUE, NOW(), NOW(), TRUE),
    ('auth0|B2C3D4E5F6G7', 'david_moore', 'david.moore@example.com', 'David', 'James', 'Moore', '1989-08-19', 'https://example.com/profile10.jpg', 'https://example.com/banner10.jpg', 'Tech lover, gamer, and entrepreneur.', 'Dallas, USA', 'GameTech', TRUE, NOW(), NOW(), TRUE),
    ('auth0|F2G3H4I5J6K7', 'nina_patel', 'nina.patel@example.com', 'Nina', 'Priya', 'Patel', '1992-04-17', 'https://example.com/profile11.jpg', 'https://example.com/banner11.jpg', 'Food lover and social entrepreneur.', 'Mumbai, India', 'Savor Co.', TRUE, NOW(), NOW(), TRUE),
    ('auth0|M2N3O4P5Q6R7', 'omar_khan', 'omar.khan@example.com', 'Omar', 'Ali', 'Khan', '1987-09-23', 'https://example.com/profile12.jpg', 'https://example.com/banner12.jpg', 'Engineer and advocate for renewable energy.', 'Karachi, Pakistan', 'GreenFuture', TRUE, NOW(), NOW(), TRUE),
    ('auth0|W3X4Y5Z6A7B8', 'ana_santos', 'ana.santos@example.com', 'Ana', 'Luiza', 'Santos', '1990-11-30', 'https://example.com/profile13.jpg', 'https://example.com/banner13.jpg', 'Passionate about digital marketing and nature.', 'São Paulo, Brazil', 'EcoPlus', TRUE, NOW(), NOW(), TRUE),
    ('auth0|E8F9G1H2I3J4', 'yuki_ando', 'yuki.ando@example.com', 'Yuki', 'Satoshi', 'Ando', '1989-12-10', 'https://example.com/profile14.jpg', 'https://example.com/banner14.jpg', 'Tech enthusiast and game developer.', 'Tokyo, Japan', 'NexTech', TRUE, NOW(), NOW(), TRUE),
    ('auth0|L1M2N3O4P5Q6', 'mohamed_ali', 'mohamed.ali@example.com', 'Mohamed', 'Abdullah', 'Ali', '1994-06-02', 'https://example.com/profile15.jpg', 'https://example.com/banner15.jpg', 'Student of AI and software development.', 'Cairo, Egypt', 'TechVision', TRUE, NOW(), NOW(), TRUE);

-- Seed posts:
INSERT INTO posts (user_id, content, picture, is_public, created_at, updated_at)
VALUES
    (1, 'Just completed a new project! Excited to share it soon.', 'https://example.com/post1.jpg', TRUE, '2024-10-01 09:15:00', '2024-10-01 09:15:00'),
    (2, 'Had an amazing day exploring the city! #adventuretime', 'https://example.com/post2.jpg', TRUE, '2024-09-12 14:45:00', '2024-09-12 14:45:00'),
    (3, 'Working on a new tech startup. Stay tuned!', 'https://example.com/post3.jpg', TRUE, '2024-08-23 18:30:00', '2024-08-23 18:30:00'),
    (4, 'Feeling grateful for the opportunity to travel the world!', 'https://example.com/post4.jpg', TRUE, '2024-07-25 11:00:00', '2024-07-25 11:00:00'),
    (4, 'Can’t believe how much I’ve learned on this trip. #lifechanger', 'https://example.com/post5.jpg', TRUE, '2024-07-26 13:00:00', '2024-07-26 13:00:00'),
    (4, 'Visited the most beautiful beaches today. #paradise', 'https://example.com/post6.jpg', TRUE, '2024-07-27 15:30:00', '2024-07-27 15:30:00'),
    (4, 'Excited to share my travel blog! Check it out.', 'https://example.com/post7.jpg', TRUE, '2024-07-28 17:45:00', '2024-07-28 17:45:00'),
    (4, 'Had a great night out with new friends I met along the way!', 'https://example.com/post8.jpg', TRUE, '2024-07-29 20:00:00', '2024-07-29 20:00:00'),
    (4, 'Reflecting on this incredible journey. Feeling so lucky.', 'https://example.com/post9.jpg', TRUE, '2024-07-30 10:00:00', '2024-07-30 10:00:00'),
    (5, 'Started a new yoga practice today. Feeling so good!', 'https://example.com/post10.jpg', TRUE, '2024-08-15 08:15:00', '2024-08-15 08:15:00'),
    (6, 'Loving this hiking trail in the Pacific Northwest! #nature', 'https://example.com/post11.jpg', TRUE, '2024-09-01 12:00:00', '2024-09-01 12:00:00'),
    (7, 'Got into the best shape of my life in just 3 months! #fitnessgoals', 'https://example.com/post12.jpg', TRUE, '2024-10-05 14:30:00', '2024-10-05 14:30:00'),
    (8, 'Learning to play guitar! Excited for this new journey.', 'https://example.com/post13.jpg', TRUE, '2024-09-18 16:00:00', '2024-09-18 16:00:00'),
    (9, 'Excited about my latest photography project! #photography', 'https://example.com/post14.jpg', TRUE, '2024-10-12 11:00:00', '2024-10-12 11:00:00'),
    (10, 'Just finished an incredible book on AI. So much to learn!', 'https://example.com/post15.jpg', TRUE, '2024-08-05 09:30:00', '2024-08-05 09:30:00'),
    (11, 'Exploring some of the best street food in Mumbai!', 'https://example.com/post16.jpg', TRUE, '2024-06-25 13:00:00', '2024-06-25 13:00:00'),
    (12, 'Solar power is the future. Proud to be part of a renewable energy startup!', 'https://example.com/post17.jpg', TRUE, '2024-07-10 10:00:00', '2024-07-10 10:00:00'),
    (13, 'So grateful for the support from my followers. Thank you for everything!', 'https://example.com/post18.jpg', TRUE, '2024-05-01 18:00:00', '2024-05-01 18:00:00'),
    (14, 'Tokyo’s technology scene is so inspiring. Loving my time here!', 'https://example.com/post19.jpg', TRUE, '2024-04-18 14:00:00', '2024-04-18 14:00:00'),
    (15, 'Started a new project to raise awareness for environmental sustainability.', 'https://example.com/post20.jpg', TRUE, '2024-03-30 09:45:00', '2024-03-30 09:45:00'),
    (1, 'First day at my new job! Excited to learn and grow with this amazing team.', 'https://example.com/post21.jpg', TRUE, '2024-05-14 17:30:00', '2024-05-14 17:30:00'),
    (3, 'I can’t believe how much my photography skills have improved in the last year!', 'https://example.com/post22.jpg', TRUE, '2024-06-01 13:45:00', '2024-06-01 13:45:00'),
    (4, 'Feeling inspired after a great trip to the Amazon rainforest. The beauty is unreal.', 'https://example.com/post23.jpg', TRUE, '2024-07-02 12:30:00', '2024-07-02 12:30:00'),
    (5, 'Just launched a new campaign for my startup. Let’s make a change!', 'https://example.com/post24.jpg', TRUE, '2024-08-19 10:30:00', '2024-08-19 10:30:00'),
    (8, 'Enjoying a peaceful day at the beach with my loved ones.', 'https://example.com/post25.jpg', TRUE, '2024-09-10 14:00:00', '2024-09-10 14:00:00'),
    (12, 'Great workout today. Gonna feel it tomorrow!', 'https://example.com/post26.jpg', TRUE, '2024-09-25 15:00:00', '2024-09-25 15:00:00'),
    (12, 'Just finished a new painting! Can’t wait to share it with you all.', 'https://example.com/post27.jpg', TRUE, '2024-08-03 17:30:00', '2024-08-03 17:30:00'),
    (13, 'Finally checked off a bucket list item: scuba diving in the Great Barrier Reef!', 'https://example.com/post28.jpg', TRUE, '2024-07-15 12:00:00', '2024-07-15 12:00:00'),
    (15, 'I’m in love with this new series on coding. Highly recommend it!', 'https://example.com/post29.jpg', TRUE, '2024-06-20 08:15:00', '2024-06-20 08:15:00'),
    (4, 'Nothing like a cozy weekend with good friends and great food.', 'https://example.com/post30.jpg', TRUE, '2024-07-08 16:30:00', '2024-07-08 16:30:00'),
    (4, 'Excited to start my journey with this amazing new team.', 'https://example.com/post31.jpg', TRUE, '2024-09-30 09:00:00', '2024-09-30 09:00:00'),
    (4, 'I can’t believe it’s already been a year since I started my business.', 'https://example.com/post32.jpg', TRUE, '2024-10-01 13:15:00', '2024-10-01 13:15:00'),
    (5, 'Feeling blessed to have made such great friends in my travels.', 'https://example.com/post33.jpg', TRUE, '2024-07-21 18:45:00', '2024-07-21 18:45:00'),
    (2, 'I can’t wait to share my new music album with you all!', 'https://example.com/post34.jpg', TRUE, '2024-06-11 19:00:00', '2024-06-11 19:00:00'),
    (4, 'Had an incredible dinner at this hidden gem of a restaurant. So worth it.', 'https://example.com/post35.jpg', TRUE, '2024-07-18 22:00:00', '2024-07-18 22:00:00'),
    (9, 'Just made it to the top of the mountain! The view is amazing!', 'https://example.com/post36.jpg', TRUE, '2024-08-20 14:15:00', '2024-08-20 14:15:00'),
    (9, 'Can’t wait to dive into the new project with the team.', 'https://example.com/post37.jpg', TRUE, '2024-09-13 12:00:00', '2024-09-13 12:00:00'),
    (4, 'Had an incredible yoga retreat this weekend. Feeling so at peace.', 'https://example.com/post38.jpg', TRUE, '2024-07-23 08:00:00', '2024-07-23 08:00:00'),
    (4, 'Exploring more sustainable ways to live. Let’s save the planet together!', 'https://example.com/post39.jpg', TRUE, '2024-09-27 17:30:00', '2024-09-27 17:30:00'),
    (1, 'Pushing myself to learn new coding languages every day. Progress is being made!', 'https://example.com/post40.jpg', TRUE, '2024-09-08 16:00:00', '2024-09-08 16:00:00');

-- Seed comments:
INSERT INTO comments (post_id, user_id, content, created_at)
VALUES
    (1, 2, 'This project looks awesome! Can''t wait to see more.', '2024-10-01 09:45:00'),
    (2, 3, 'I love your adventures! #inspiration', '2024-09-12 15:00:00'),
    (3, 5, 'Your startup sounds like a game-changer! Best of luck!', '2024-08-24 12:00:00'),
    (11, 6, 'Wow, that beach looks amazing. I need to visit soon.', '2024-07-26 16:15:00'),
    (13, 4, 'Great blog post! I love the tips you shared.', '2024-07-27 17:30:00'),
    (12, 8, 'I want to learn guitar too! Let me know how it goes.', '2024-09-18 16:30:00'),
    (19, 10, 'AI is the future! Can''t wait to see where this takes us.', '2024-08-05 09:45:00'),
    (28, 12, 'Street food in Mumbai is always a win. So good!', '2024-06-25 13:30:00'),
    (40, 14, 'I can''t wait to see your work in Tokyo. The tech scene there is crazy!', '2024-04-18 14:15:00'),
    (10, 7, 'So much to learn from books like that. Definitely on my list!', '2024-08-07 10:00:00'),
    (11, 5, 'This yoga journey is life-changing. Let''s keep pushing forward!', '2024-08-15 09:00:00'),
    (12, 6, 'This looks like the adventure of a lifetime. What was your favorite part?', '2024-09-02 14:45:00'),
    (13, 13, 'I love that you appreciate your followers so much. So inspiring!', '2024-05-01 19:15:00'),
    (14, 9, 'Tokyo is such an exciting place! The tech culture is unlike anywhere else.', '2024-04-19 12:30:00'),
    (15, 16, 'Sustainability is so important. Let’s make it a priority for future generations!', '2024-03-30 09:00:00'),
    (16, 17, 'That first day feeling is always surreal. Congrats!', '2024-05-14 18:00:00'),
    (17, 11, 'Those hiking trails are amazing. Keep the adventure going!', '2024-06-01 14:15:00'),
    (18, 2, 'The Amazon rainforest is one of the most beautiful places on Earth.', '2024-07-02 13:00:00'),
    (4, 15, 'Love the campaign! It’s so important to raise awareness for sustainability.', '2024-08-19 11:00:00'),
    (20, 4, 'Nothing beats a beach day with good company. I need one of those soon.', '2024-09-10 14:45:00'),
    (21, 5, 'Your fitness progress is incredible! Keep pushing!', '2024-09-25 16:30:00'),
    (22, 6, 'I love the creativity in your new art project. Can’t wait to see the final product!', '2024-08-03 18:30:00'),
    (23, 3, 'Diving in the Great Barrier Reef is on my bucket list now. Thanks for sharing!', '2024-07-15 12:45:00'),
    (24, 10, 'This series on coding is so motivating. It'' s a must-watch!', '2024-06-20 08:30:00'),
    (25, 4, 'Cozy weekends like this are the best! #weekendvibes', '2024-07-08 16:15:00'),
    (26, 4, 'The new team is lucky to have you! Can’t wait to hear more.', '2024-09-30 09:30:00'),
    (4, 13, 'A year in business is a huge milestone! Congratulations!', '2024-10-01 14:00:00'),
    (4, 7, 'This sounds like the perfect spot to find peace. Where was this?', '2024-07-21 18:30:00'),
    (29, 8, 'Your music album will be amazing! Looking forward to it.', '2024-06-11 19:15:00'),
    (30, 4, 'Best meal I’ve had in a while. I need the recipe!', '2024-07-18 22:30:00'),
    (31, 11, 'The mountain top views are breathtaking. #bucketlist', '2024-08-20 15:30:00'),
    (32, 14, 'Great teamwork! Keep up the awesome work!', '2024-09-13 12:30:00'),
    (33, 6, 'Yoga retreats like that change your life. Such an amazing experience!', '2024-07-23 08:45:00'),
    (34, 2, 'We need more content like this! Let’s save the planet together.', '2024-09-27 18:00:00'),
    (12, 4, 'Learning new coding languages is the best way to grow. Keep it up!', '2024-09-08 17:00:00'),
    (36, 1, 'This view is everything! So jealous.', '2024-08-12 14:30:00'),
    (37, 9, 'I’m really excited to see what your next project will be!', '2024-07-05 13:45:00'),
    (6, 4, 'Your passion for sustainability really shines through in everything you do.', '2024-09-18 14:45:00'),
    (4, 5, 'This project is going to make a real difference. Proud of you!', '2024-09-15 16:00:00'),
    (3, 4, 'Incredible photo! You’ve captured the moment perfectly.', '2024-07-10 14:00:00');

-- Seed likes:
INSERT INTO likes (user_id, post_id, created_at)
VALUES
    (1, 2, '2024-10-01 09:45:00'),
    (1, 5, '2024-09-25 10:30:00'),
    (2, 7, '2024-08-15 14:00:00'),
    (2, 10, '2024-07-18 22:30:00'),
    (3, 8, '2024-06-25 13:30:00'),
    (3, 15, '2024-09-30 09:30:00'),
    (4, 1, '2024-07-02 13:00:00'),
    (4, 9, '2024-08-18 17:00:00'),
    (5, 3, '2024-08-04 12:15:00'),
    (5, 16, '2024-07-30 18:45:00'),
    (6, 5, '2024-08-03 09:00:00'),
    (6, 13, '2024-09-12 12:00:00'),
    (7, 2, '2024-07-19 15:00:00'),
    (7, 12, '2024-08-22 16:30:00'),
    (8, 4, '2024-08-07 10:00:00'),
    (8, 16, '2024-09-10 14:30:00'),
    (9, 3, '2024-07-08 16:30:00'),
    (9, 14, '2024-07-19 13:00:00'),
    (10, 6, '2024-09-17 18:15:00'),
    (10, 17, '2024-09-21 09:45:00'),
    (11, 1, '2024-08-19 11:15:00'),
    (11, 9, '2024-09-05 08:30:00'),
    (12, 18, '2024-07-12 14:45:00'),
    (12, 7, '2024-09-09 10:00:00'),
    (13, 16, '2024-06-29 13:30:00'),
    (13, 5, '2024-08-03 17:00:00'),
    (14, 4, '2024-07-28 16:15:00'),
    (14, 11, '2024-09-19 14:00:00'),
    (15, 15, '2024-08-20 12:30:00'),
    (15, 40, '2024-09-25 14:15:00'),
    (6, 18, '2024-09-14 10:00:00'),
    (8, 20, '2024-07-25 11:30:00'),
    (1, 22, '2024-08-06 17:00:00'),
    (2, 30, '2024-09-12 14:30:00'),
    (3, 35, '2024-08-09 13:15:00'),
    (4, 27, '2024-09-22 12:45:00'),
    (5, 13, '2024-07-10 16:00:00'),
    (6, 23, '2024-08-02 09:45:00'),
    (7, 19, '2024-09-29 14:00:00'),
    (8, 12, '2024-06-18 15:15:00');
