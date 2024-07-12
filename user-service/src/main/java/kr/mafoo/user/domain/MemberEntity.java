package kr.mafoo.user.domain;

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

@Getter
@Setter
@NoArgsConstructor
@Table("member")
public class MemberEntity implements Persistable<String> {
    @Id
    @Column("member_id")
    private String id;

    @Column("name")
    private String name;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("profile_img_url")
    private String profileImageUrl;

    @Transient
    private boolean isNew = false;

    @Override
    public boolean equals(Object obj) {
       if (this == obj) return true;
       if (obj == null || getClass() != obj.getClass()) return false;

       MemberEntity that = (MemberEntity) obj;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public static MemberEntity newMember(String id, String name) {
        MemberEntity member = new MemberEntity();
        member.id = id;
        member.name = name;
        member.isNew = true;
        return member;
    }
}
