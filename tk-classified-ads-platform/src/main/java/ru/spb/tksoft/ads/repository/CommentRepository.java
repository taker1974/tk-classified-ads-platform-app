package ru.spb.tksoft.ads.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.spb.tksoft.ads.entity.CommentEntity;
import ru.spb.tksoft.ads.projection.CommentProjection;

/**
 * Repository of CommentEntity.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    // TODO: What if u.avatar is null?

//     /**
//      * List of CommentProjection by ad ID.
//      * 
//      * @param adId Ad ID.
//      * @return List of CommentProjection.
//      */
//     @Query("""
//             SELECT c.id AS id, c.createdAt, c.text,
//             u.id AS authorId, u.firstName AS authorFirstName,
//             av.id AS authorAvatarId
//             FROM CommentEntity c
//             INNER JOIN c.user u ON u.id = c.user.id
//             LEFT JOIN u.avatar av ON av.id = u.avatar.id
//             WHERE c.ad.id = :adId""")
//     List<CommentProjection> findManyByAdId(Long adId);

//     /**
//      * CommentProjection by user name, ad ID and comment ID.
//      * 
//      * @param adId Ad ID.
//      * @param userId User ID.
//      * @return List of CommentProjection.
//      */
//     @Query("""
//             SELECT c.id AS id, c.createdAt, c.text,
//             u.id AS authorId, u.firstName AS authorFirstName,
//             av.id AS authorAvatarId
//             FROM CommentEntity c
//             INNER JOIN c.user u ON u.id = c.user.id
//             LEFT JOIN u.avatar av ON av.id = u.avatar.id
//             WHERE c.user.name = :userName AND c.ad.id = :adId AND c.id = :commentId""")
//     Optional<CommentProjection> findOneExact(String username, Long adId, Long commentId);

//     /**
//      * CommentEntity by comment ID.
//      * 
//      * @param id Comment ID.
//      * @return List of CommentProjection.
//      */
//     @Query("SELECT c FROM CommentEntity c WHERE c.id = :id")
//     Optional<CommentEntity> findOneById(Long id);
}
