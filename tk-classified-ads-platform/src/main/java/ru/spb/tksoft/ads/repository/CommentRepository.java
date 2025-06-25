package ru.spb.tksoft.ads.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
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
     * @return List of CommentEntity.
     */
    @EntityGraph(attributePaths = {"ad", "user"})
    List<CommentEntity> findAllBy();

    /**
     * @return List of CommentEntity by ad ID.
     */
    @EntityGraph(attributePaths = {"ad", "user"})
    List<CommentEntity> findAllByAd_Id(Long adId);

    /**
     * @return Optional of CommentEntity by ad ID and comment ID.
     */
    @Query("""
            SELECT c FROM CommentEntity c
            JOIN FETCH c.ad a
            JOIN FETCH a.user u
            WHERE c.ad.id = :adId AND c.id = :commentId
                    """)
    Optional<CommentEntity> findByAdAndCommentWithEagerFetch(long adId, long commentId);
}
