package kr.mafoo.photo.domain;

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
@Table("sumone_event_mapping")
public class SumoneEventMappingEntity implements Persistable<String> {
    @Id
    @Column("sumone_id")
    private String id;

    @Column("invite_code")
    private String inviteCode;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @Transient
    private boolean isNew = false;

    @Override
    public boolean equals(Object obj) {
       if (this == obj) return true;
       if (obj == null || getClass() != obj.getClass()) return false;

       SumoneEventMappingEntity that = (SumoneEventMappingEntity) obj;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public static SumoneEventMappingEntity newEventMember(String id, String inviteCode) {
        SumoneEventMappingEntity sumoneEntity = new SumoneEventMappingEntity();
        sumoneEntity.id = id;
        sumoneEntity.inviteCode = inviteCode;
        sumoneEntity.isNew = true;
        return sumoneEntity;
    }
}
