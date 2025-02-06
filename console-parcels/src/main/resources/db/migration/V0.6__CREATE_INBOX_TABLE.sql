CREATE TABLE inbox (
                       id uuid PRIMARY KEY,
                       created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       owner VARCHAR(255) NOT NULL
);