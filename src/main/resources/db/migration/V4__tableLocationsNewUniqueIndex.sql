ALTER TABLE locations
    ADD CONSTRAINT unique_user_latitude_longitude UNIQUE (UserId, Latitude, Longitude);