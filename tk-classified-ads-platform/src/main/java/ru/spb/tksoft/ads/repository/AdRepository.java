package ru.spb.tksoft.ads.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.spb.tksoft.ads.entity.AdEntity;
import ru.spb.tksoft.ads.projection.AdExtendedProjection;
import ru.spb.tksoft.ads.projection.AdProjection;

/**
 * Repository of AdEntity.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Repository
public interface AdRepository extends JpaRepository<AdEntity, Long> {

    /**
     * List of AdProjection.
     * 
     * [INNER] JOIN because if user not found, we don't want to get such result.
     * 
     * @param userName User name.
     * @return List of AdEntity.
     */
    @Query("""
            SELECT a.id AS id, u.id AS userId, 
            a.price AS price, a.title AS title
            FROM AdEntity a
            INNER JOIN UserEntity u ON a.user.id = u.id""")
    List<AdProjection> findMany();

    /**
     * List of AdProjection by user name.
     * 
     * @param userName User name.
     * @return List of AdEntity.
     */
    @Query("""
            SELECT a.id AS id, u.id AS userId,
            a.price AS price, a.title AS title
            FROM AdEntity a
            INNER JOIN UserEntity u ON a.user.id = u.id
            WHERE u.name = :userName""")
    List<AdProjection> findManyByUserName(String userName);

    /**
     * AdExtendedProjection by ID.
     * 
     * @param id Ad ID.
     * @return Optional AdExtendedProjection.
     */
    @Query("""
            SELECT a.id AS id, u.id AS userId,
            a.price AS price, a.title AS title, a.description AS description,
            u.firstName AS authorFirstName, u.lastName AS authorLastName,
            u.name AS email, u.phone AS phone
            FROM AdEntity a
            INNER JOIN UserEntity u ON a.user.id = u.id
            WHERE a.id = :id""")
    Optional<AdExtendedProjection> findOneExtendedById(Long id);
}
