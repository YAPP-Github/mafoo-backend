ALTER TABLE `photo`
    ADD `display_index` INTEGER COMMENT '표시순' AFTER `album_id`;

CREATE INDEX `idx_photo_display_index` ON `photo`(`display_index`);

WITH RankedPhotos AS (
    SELECT
        id,
        owner_member_id,
        album_id,
        CASE
            WHEN album_id IS NOT NULL THEN ROW_NUMBER() OVER (PARTITION BY owner_member_id, album_id ORDER BY created_at) - 1
            ELSE NULL
            END AS new_index
    FROM photo
)
UPDATE photo
    JOIN RankedPhotos
ON photo.id = RankedPhotos.id
    SET photo.display_index = RankedPhotos.new_index;
