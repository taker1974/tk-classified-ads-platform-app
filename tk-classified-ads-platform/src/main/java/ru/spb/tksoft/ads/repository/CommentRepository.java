package ru.spb.tksoft.ads.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.spb.tksoft.ads.entity.CommentEntity;

/**
 * Repository of CommentEntity.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

}
