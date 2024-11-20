package kr.mafoo.photo.exception;

public class AlbumOwnerMismatchException extends DomainException {
    public AlbumOwnerMismatchException() {
        super(ErrorCode.ALBUM_OWNER_MISMATCH);
    }
}
