package ru.spb.tksoft.ads.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.spb.tksoft.ads.entity.ImageEntity;

/**
 * Repository of ImageEntity.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

}
