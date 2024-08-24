package kr.mafoo.photo.repository;

import kr.mafoo.photo.domain.PermissionEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface PermissionRepository extends R2dbcRepository<PermissionEntity, String> {
}