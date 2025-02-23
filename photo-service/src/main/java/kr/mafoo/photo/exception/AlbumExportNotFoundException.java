package kr.mafoo.photo.exception;

public class AlbumExportNotFoundException extends DomainException {
    public AlbumExportNotFoundException() {
        super(ErrorCode.ALBUM_EXPORT_NOT_FOUND);
    }
}
