ALTER TABLE album
    RENAME COLUMN photoCount TO photo_count;

UPDATE album a
SET photo_count = (
    SELECT COUNT(p.id)
    FROM photo p
    WHERE p.album_id = a.id
);