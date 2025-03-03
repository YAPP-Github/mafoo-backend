package kr.mafoo.user.repository;

import kr.mafoo.user.domain.MemberEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MemberRepository extends R2dbcRepository<MemberEntity, String> {

    Mono<MemberEntity> findByIdAndDeletedAtIsNull(String memberId);

    @Query("SELECT * FROM member WHERE name LIKE :keyword AND member_id != :memberId AND deleted_at IS NULL")
    Flux<MemberEntity> findAllByNameContainingAndMemberIdNot(String keyword, String memberId);

    @Modifying
    @Query("UPDATE member SET deleted_at = NOW() WHERE member_id = :memberId AND deleted_at IS NULL")
    Mono<Void> softDeleteById(String memberId);
}
