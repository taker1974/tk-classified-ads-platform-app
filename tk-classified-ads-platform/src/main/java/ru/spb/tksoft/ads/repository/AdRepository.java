package ru.spb.tksoft.ads.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.spb.tksoft.ads.entity.AdEntity;
import ru.spb.tksoft.ads.projection.AdExtendedResponseProjection;
import ru.spb.tksoft.ads.projection.AdResponseProjection;

/**
 * Repository of AdEntity.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Repository
public interface AdRepository extends JpaRepository<AdEntity, Long> {

    /**
     * @return List of AdResponseProjection.
     */
    @Query("""
            SELECT
                a.id AS id,
                a.title AS title,
                a.price AS price,
                u.id AS userId,
                i.id AS imageId
            FROM AdEntity a
            JOIN a.user u
            LEFT JOIN a.image i""")
    List<AdResponseProjection> findManyMinimal();

    /**
     * @return List of AdResponseProjection by user name.
     */
    @Query("""
            SELECT
                a.id AS id,
                a.title AS title,
                a.price AS price,
                u.id AS userId,
                i.id AS imageId
            FROM AdEntity a
            JOIN a.user u
            LEFT JOIN a.image i
            WHERE u.name = :userName""")
    List<AdResponseProjection> findManyMinimal(String userName);

    /**
     * @return Optional AdExtendedResponseProjection by ad ID.
     */
    @Query("""
            SELECT
                a.id AS id,
                a.title AS title,
                a.price AS price,
                a.description AS description,
                i.id AS imageId,
                u.firstName AS authorFirstName,
                u.lastName AS authorLastName,
                u.name AS email,
                u.phone AS phone
            FROM AdEntity a
            JOIN a.user u
            JOIN a.image i
            WHERE a.id = :adId""")
    Optional<AdExtendedResponseProjection> findOneExtended(Long adId);

    /**
     * AdEntity by user name and ad ID.
     * 
     * @param userName User name.
     * @param adId Ad ID.
     * @return Optional AdEntity.
     */
    @Query("SELECT a FROM AdEntity a WHERE a.user.name = :userName AND a.id = :adId")
    Optional<AdEntity> findOneByUserNameAndAdId(String userName, Long adId);

    // /**
    // * List of AdEntity.
    // *
    // * @return List of AdEntity.
    // */
    // @Query("""
    // SELECT a FROM AdEntity a
    // INNER JOIN FETCH a.user u
    // INNER JOIN FETCH a.image i""")
    // List<AdEntity> findManyEager();

    // /**
    // * List of AdEntity by user name.
    // *
    // * @param userName User name.
    // * @return List of AdEntity.
    // */
    // @Query("""
    // SELECT a FROM AdEntity a
    // INNER JOIN FETCH a.user u
    // INNER JOIN FETCH a.image i
    // WHERE u.name = :userName""")
    // List<AdEntity> findManyByUserNameEager(String userName);

    // /**
    // * AdEntity by ad ID.
    // *
    // * @param adId Ad ID.
    // * @return Optional AdEntity.
    // */
    // @Query("""
    // SELECT a FROM AdEntity a
    // INNER JOIN FETCH a.user u
    // INNER JOIN FETCH a.image i
    // WHERE a.id = :adId""")
    // Optional<AdEntity> findOneByUserNameEager(Long adId);
}
