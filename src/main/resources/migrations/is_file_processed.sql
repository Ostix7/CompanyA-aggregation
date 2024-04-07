CREATE TABLE processed_files (
                                 id SERIAL PRIMARY KEY,
                                 big_query_id TEXT NOT NULL,
                                 media_type TEXT NOT NULL,
                                 is_processed BOOLEAN DEFAULT FALSE
);
