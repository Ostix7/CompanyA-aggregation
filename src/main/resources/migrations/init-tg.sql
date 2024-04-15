CREATE SCHEMA IF NOT EXISTS social_data;

CREATE TABLE social_data.telegram_channels (
                                               channel_id BIGINT PRIMARY KEY,
                                               channel_title TEXT NOT NULL
);

CREATE TABLE social_data.telegram_posts (
                                            id TEXT PRIMARY KEY,
                                            schema_version INT,
                                            channel_id BIGINT NOT NULL,
                                            telegram_post_id BIGINT,
                                            post_date TEXT,
                                            post_ts BIGINT,
                                            updated_at TIMESTAMP WITH TIME ZONE,
                                            lang TEXT,
                                            segment TEXT,
                                            full_text TEXT,
                                            view_count INT,
                                            is_processed BOOLEAN DEFAULT FALSE,
                                            FOREIGN KEY (channel_id) REFERENCES social_data.telegram_channels(channel_id)
);

CREATE TYPE social_data.telegram_media_type AS (
                                                   media_id TEXT,
                                                   media_type TEXT,
                                                   media_duration TEXT
                                               );

CREATE TYPE social_data.telegram_reaction_type AS (
                                                      emoji TEXT,
                                                      count INT
                                                  );

CREATE TABLE social_data.telegram_media (
                                            media_id SERIAL PRIMARY KEY,
                                            post_id TEXT NOT NULL,
                                            media_data social_data.telegram_media_type[],
                                            FOREIGN KEY (post_id) REFERENCES social_data.telegram_posts(id)
);

CREATE TABLE social_data.telegram_comments (
                                               comment_id SERIAL PRIMARY KEY,
                                               post_id TEXT NOT NULL,
                                               telegram_message_id BIGINT,
                                               sender_id BIGINT,
                                               sender_username TEXT,
                                               sender_first_name TEXT,
                                               sender_last_name TEXT,
                                               reply_to BIGINT,
                                               full_text TEXT,
                                               reactions social_data.telegram_reaction_type[],
                                               media social_data.telegram_media_type[],
                                               FOREIGN KEY (post_id) REFERENCES social_data.telegram_posts(id)
);

CREATE TABLE social_data.telegram_reactions (
                                                reaction_id SERIAL PRIMARY KEY,
                                                post_id TEXT NOT NULL,
                                                reaction_data social_data.telegram_reaction_type[],
                                                FOREIGN KEY (post_id) REFERENCES social_data.telegram_posts(id)
);


ALTER TABLE telegram_media ALTER COLUMN media_id TYPE character varying;

ALTER TABLE telegram_comment ALTER COLUMN full_text TYPE TEXT;