package kr.mafoo.photo.exception;

public class AlbumIndexIsSameException extends DomainException {
    public AlbumIndexIsSameException() {
        super(ErrorCode.ALBUM_DISPLAY_INDEX_IS_SAME);
    }
}
