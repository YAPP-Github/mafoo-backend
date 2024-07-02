package kr.mafoo.photo.repository;

import kr.mafoo.photo.domain.PhotoEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface PhotoRepository extends R2dbcRepository<PhotoEntity, String> {
}