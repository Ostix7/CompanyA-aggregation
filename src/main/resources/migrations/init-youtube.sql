CREATE TABLE social_data.youtube_channels (
                                              id TEXT PRIMARY KEY,
                                              youtube_channel_id TEXT,
                                              title TEXT,
                                              subscribers_count BIGINT,
                                              video_ids TEXT[],
                                              insertion_time BIGINT
);

CREATE TABLE social_data.youtube_videos (
                                            id TEXT PRIMARY KEY,
                                            youtube_video_id TEXT UNIQUE,
                                            title TEXT,
                                            description TEXT,
                                            likes BIGINT,
                                            tags TEXT[],
                                            published_at BIGINT,
                                            insertion_time BIGINT,
                                            sentiment_value DOUBLE PRECISION,
                                            channel_id TEXT,
                                            is_processed BOOLEAN DEFAULT FALSE,
                                            CONSTRAINT fk_youtube_channel
                                                FOREIGN KEY(channel_id)
                                                    REFERENCES social_data.youtube_channels(id)
                                                    ON DELETE SET NULL
);

CREATE TABLE social_data.youtube_video_topics (
    youtube_video_id TEXT NOT NULL,
    topic_modeling CHARACTER VARYING(255),
    FOREIGN KEY (youtube_video_id) REFERENCES social_data.youtube_videos(id)
);

CREATE TABLE social_data.youtube_comments (
                                              id TEXT PRIMARY KEY,
                                              youtube_comment_id TEXT,
                                              youtube_video_id TEXT,
                                              text TEXT,
                                              likes BIGINT,
                                              author_name TEXT,
                                              author_profile_image_url TEXT,
                                              published_at BIGINT,
                                              insertion_time BIGINT,
                                              CONSTRAINT fk_youtube_video_comment
                                                  FOREIGN KEY(youtube_video_id)
                                                      REFERENCES social_data.youtube_videos(youtube_video_id)
                                                      ON DELETE CASCADE
);

CREATE TABLE social_data.youtube_captions (
                                              id TEXT PRIMARY KEY,
                                              youtube_video_id TEXT,
                                              language TEXT,
                                              content TEXT,
                                              insertion_time BIGINT,
                                              sentiment_value DOUBLE PRECISION,
                                              CONSTRAINT fk_youtube_video_caption
                                                  FOREIGN KEY(youtube_video_id)
                                                      REFERENCES social_data.youtube_videos(youtube_video_id)
                                                      ON DELETE CASCADE
);

CREATE TABLE social_data.youtube_caption_topics (
    youtube_caption_id TEXT NOT NULL,
    topic_modeling CHARACTER VARYING(255),
    FOREIGN KEY (youtube_caption_id) REFERENCES social_data.youtube_captions(id)
);

ALTER TABLE youtube_comments
    ALTER COLUMN text TYPE TEXT;

ALTER TABLE youtube_captions
    ALTER COLUMN content TYPE TEXT;

ALTER TABLE youtube_videos
    ALTER COLUMN description TYPE TEXT;