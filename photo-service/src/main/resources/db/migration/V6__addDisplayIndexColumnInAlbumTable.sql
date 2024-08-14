ALTER TABLE `album`
    ADD `display_index` INTEGER NOT NULL DEFAULT 0 COMMENT '표기순' after `owner_member_id`;
CREATE INDEX `idx_album_display_index` ON `album`(`display_index`);

WITH RankedAlbums AS (
    SELECT
        id,
        owner_member_id,
        ROW_NUMBER() OVER (PARTITION BY owner_member_id ORDER BY created_at) - 1 AS new_index
    FROM album
)
UPDATE album
    JOIN RankedAlbums
    ON album.id = RankedAlbums.id
SET album.display_index = RankedAlbums.new_index;
