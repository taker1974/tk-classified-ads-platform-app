package ru.spb.tksoft.ads.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
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
     * Check if user with such name exists.
     * 
     * @return true if exists, otherwise false.
     */
    @Query(nativeQuery = true,
            value = "SELECT EXISTS(SELECT 1 FROM \"user\" WHERE name = :name LIMIT 1)")
    boolean existsByName(String name);

    /**
     * Check if user with such name and password exists.
     * 
     * @return true if exists, otherwise false.
     */
    @Query(nativeQuery = true,
            value = "SELECT EXISTS(SELECT 1 FROM \"user\" WHERE name = :name AND password = :password LIMIT 1)")
    boolean existsByNameAndPassword(String name, String password);

    /**
     * UserEntity by user's email as login as name.
     * 
     * @return UserEntity or empty.
     */
    @Query(nativeQuery = true,
            value = "SELECT u.* FROM \"user\" u WHERE u.name = :name LIMIT 1")
    Optional<UserEntity> findOneByNameRaw(String name);

    /**
     * @return Page of UserEntity (for testing/management purposes).
     */
    @EntityGraph(attributePaths = {"avatar"})
    Page<UserEntity> findAll(Pageable pageable);

    /**
     * @return Single UserEntity by name.
     */
    @EntityGraph(attributePaths = {"avatar"})
    Optional<UserEntity> findByName(String name);

    /**
     * @return Single UserEntity by ID.
     */
    @EntityGraph(attributePaths = {"avatar"})
    Optional<UserEntity> findById(Long id);
}
