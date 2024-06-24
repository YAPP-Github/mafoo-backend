package kr.mafoo.photo.exception;

public class AlbumNotFoundException extends DomainException {
    public AlbumNotFoundException() {
        super(ErrorCode.ALBUM_NOT_FOUND);
    }
}
