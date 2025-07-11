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
     * @return Optional of CommentEntity by ad ID.
     */
    @Query("SELECT c FROM CommentEntity c WHERE c.ad.id = :adId")
    Optional<CommentEntity> findOneByAdId(Long adId);

    /**
     * @return List of CommentEntity by ad ID.
     */
    @Query("SELECT c FROM CommentEntity c JOIN FETCH c.user u WHERE c.ad.id = :adId")
    List<CommentEntity> findManyByAdId(Long adId);

    /**
     * @return Optional CommentEntity by user name and ad ID.
     */
    @Query("""
        SELECT c 
        FROM CommentEntity c 
        JOIN FETCH c.user u 
        WHERE c.ad.id = :adId AND u.name = :userName AND c.id = :id""")
    Optional<CommentEntity> findOneByUserExact(String userName, Long adId, Long id);
}
