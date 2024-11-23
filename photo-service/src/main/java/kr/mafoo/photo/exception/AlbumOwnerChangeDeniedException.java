package kr.mafoo.photo.exception;

public class AlbumOwnerChangeDeniedException extends DomainException {
    public AlbumOwnerChangeDeniedException() {
        super(ErrorCode.ALBUM_OWNER_CHANGE_DENIED);
    }
}
