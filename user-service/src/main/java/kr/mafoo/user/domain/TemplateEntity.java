package kr.mafoo.user.domain;

import java.time.LocalDateTime;
import kr.mafoo.user.enums.NotificationType;
import kr.mafoo.user.enums.RouteType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@NoArgsConstructor
@Table("notification_template")
public class TemplateEntity implements Persistable<String> {
    @Id
    @Column("notification_template_id")
    private String templateId;

    @Column("notification_type")
    private NotificationType notificationType;

    @Column("thumbnail_image_url")
    private String thumbnailImageUrl;

    @Column("title")
    private String title;

    @Column("body")
    private String body;

    @Column("routeType")
    private RouteType routeType;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;

    @Column("deleted_at")
    private LocalDateTime deletedAt;

    @Transient
    private boolean isNew = false;

    @Override
    public boolean equals(Object obj) {
       if (this == obj) return true;
       if (obj == null || getClass() != obj.getClass()) return false;

        TemplateEntity that = (TemplateEntity) obj;
        return templateId.equals(that.templateId);
    }

    @Override
    public int hashCode() {
        return templateId.hashCode();
    }

    @Override
    public String getId() {
        return templateId;
    }

    public TemplateEntity updateTemplate(NotificationType notificationType, String thumbnailImageUrl, String title, String body) {
        this.notificationType = notificationType;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.title = title;
        this.body = body;
        return this;
    }

    public static TemplateEntity newTemplate(String templateId, NotificationType notificationType, String thumbnailImageUrl, String title, String body) {
        TemplateEntity template = new TemplateEntity();
        template.templateId = templateId;
        template.notificationType = notificationType;
        template.thumbnailImageUrl = thumbnailImageUrl;
        template.title = title;
        template.body = body;
        template.isNew = true;
        return template;
    }
}
