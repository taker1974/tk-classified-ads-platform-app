package ru.spb.tksoft.ads.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.spb.tksoft.ads.entity.CommentEntity;

/**
 * Repository of CommentEntity.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    /**
     * List of CommentEntity by ad ID.
     * 
     * @param adId Ad ID.
     * @return List of CommentEntity.
     */
    @Query("""
            SELECT c FROM CommentEntity c
            JOIN FETCH c.ad a
            JOIN FETCH c.user u
            WHERE a.id = :adId""")
    List<CommentEntity> findManyByAdIdEager(Long adId);

    /**
     * CommentEntity by comment ID and ad ID.
     * 
     * @param adId Ad ID.
     * @param commentId Comment ID.
     * @return Optional CommentEntity.
     */
    @Query("""
            SELECT c FROM CommentEntity c
            JOIN FETCH c.ad a
            JOIN FETCH c.user u
            WHERE a.id = :adId AND c.id = :commentId""")
    Optional<CommentEntity> findOneByAdIdAndCommentIdEager(Long adId, Long commentId);
}
