package kr.mafoo.photo.exception;

public class AlbumExportAlreadyExistsException extends DomainException {
    public AlbumExportAlreadyExistsException() {
        super(ErrorCode.ALBUM_EXPORT_ALREADY_EXISTS);
    }
}
