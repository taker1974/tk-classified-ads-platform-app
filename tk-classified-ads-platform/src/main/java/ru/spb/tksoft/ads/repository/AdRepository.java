package ru.spb.tksoft.ads.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.spb.tksoft.ads.entity.AdEntity;

/**
 * Repository of AdEntity.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Repository
public interface AdRepository extends JpaRepository<AdEntity, Long> {

    /**
     * @return List of {@link AdEntity} by name.
     */
    @Query("""
            SELECT DISTINCT a
            FROM AdEntity a
            LEFT JOIN FETCH a.images
            JOIN FETCH a.user u
            WHERE u.name = :userName
            """)
    List<AdEntity> findByUserName(String userName);

    /**
     * @return List of AdEntity.
     */
    @EntityGraph(attributePaths = {"images"})
    List<AdEntity> findAll();

    /**
     * @return Single AdEntity by ID.
     */
    @EntityGraph(
            value = "Ad.withUserAvatar",
            type = EntityGraph.EntityGraphType.FETCH)
    Optional<AdEntity> findById(Long id);
}
