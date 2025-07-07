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

    // /**
    // * List of CommentProjection by ad ID.
    // *
    // * @param adId Ad ID.
    // * @return List of CommentProjection.
    // */
    // @Query("""
    // SELECT c.id AS id, c.createdAt, c.text,
    // u.id AS authorId, u.firstName AS authorFirstName,
    // av.id AS authorAvatarId
    // FROM CommentEntity c
    // INNER JOIN c.user u ON u.id = c.user.id
    // LEFT JOIN u.avatar av ON av.id = u.avatar.id
    // WHERE c.ad.id = :adId""")
    // List<CommentProjection> findManyByAdId(Long adId);

    // /**
    // * CommentProjection by user name, ad ID and comment ID.
    // *
    // * @param adId Ad ID.
    // * @param userId User ID.
    // * @return List of CommentProjection.
    // */
    // @Query("""
    // SELECT c.id AS id, c.createdAt, c.text,
    // u.id AS authorId, u.firstName AS authorFirstName,
    // av.id AS authorAvatarId
    // FROM CommentEntity c
    // INNER JOIN c.user u ON u.id = c.user.id
    // LEFT JOIN u.avatar av ON av.id = u.avatar.id
    // WHERE c.user.name = :userName AND c.ad.id = :adId AND c.id = :commentId""")
    // Optional<CommentProjection> findOneExact(String username, Long adId, Long commentId);

}
