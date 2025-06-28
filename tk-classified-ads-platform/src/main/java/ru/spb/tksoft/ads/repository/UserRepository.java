package ru.spb.tksoft.ads.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.spb.tksoft.ads.entity.UserEntity;

/**
 * Repository of UserEntity.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Check if user with such name exists. Using at registration.
     * 
     * @return true if exists, otherwise false.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM UserEntity u WHERE u.name = :name)")
    boolean existsByName(String name);

    /**
     * UserEntity by name.
     * 
     * @param name User name.
     * @return Optional UserEntity.
     */
    @Query("SELECT u FROM UserEntity u WHERE u.name = :name")
    Optional<UserEntity> findOneByNameLazy(String name);

    /**
     * UserEntity by name with avatar.
     * 
     * @param name User name.
     * @return Optional UserEntity with avatar.
     */
    @Query("SELECT u FROM UserEntity u JOIN FETCH u.avatar WHERE u.name = :name")
    Optional<UserEntity> findOneByNameEager(String name);

    /**
     * UserEntity by ID with avatar.
     * 
     * @param id User ID.
     * @return Optional UserEntity with avatar.
     */
    @Query("SELECT u FROM UserEntity u JOIN FETCH u.avatar WHERE u.id = :id")
    Optional<UserEntity> findOneByIdEager(Long id);
}
