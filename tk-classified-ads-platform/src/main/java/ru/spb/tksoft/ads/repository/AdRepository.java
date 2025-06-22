package ru.spb.tksoft.ads.repository;

import java.util.List;
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
     * Returns list of {@link AdEntity} by user name.
     * 
     * @param name User name.
     * @return List of {@link AdEntity}.
     */
    @Query("SELECT ad FROM AdEntity ad JOIN FETCH ad.user u WHERE u.name = :name")
    List<AdEntity> findByUserName(String name);
}
