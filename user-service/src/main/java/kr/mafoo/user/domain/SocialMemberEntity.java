package kr.mafoo.user.domain;

import kr.mafoo.user.enums.IdentityProvider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Table("social_member")
public class SocialMemberEntity implements Persistable<SocialMemberEntityKey> {
    @Column("identity_provider")
    private IdentityProvider identityProvider;

    @Column("identifier")
    private String id;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("member_id")
    private String memberId;

    @Transient
    private boolean isNew = false;

    @Override
    public boolean equals(Object obj) {
       if (this == obj) return true;
       if (obj == null || getClass() != obj.getClass()) return false;

       SocialMemberEntity that = (SocialMemberEntity) obj;

        return id.equals(that.id) && identityProvider.equals(that.identityProvider);
    }

    public SocialMemberEntityKey getId() {
        return new SocialMemberEntityKey(identityProvider, id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identityProvider);
    }

    public static SocialMemberEntity newSocialMember(IdentityProvider identityProvider, String id, String memberId) {
        SocialMemberEntity socialMember = new SocialMemberEntity();
        socialMember.identityProvider = identityProvider;
        socialMember.id = id;
        socialMember.memberId = memberId;
        socialMember.isNew = true;
        return socialMember;
    }
}
