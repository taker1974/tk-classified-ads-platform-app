package ru.spb.tksoft.ads.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     * UserEntity by user's email and password.
     * 
     * @return UserEntity or empty.
     */
    @Query(nativeQuery = true,
            value = "SELECT u.* FROM \"user\" u WHERE u.name = :name AND u.password = :password LIMIT 1")
    Optional<UserEntity> findOneByNameAndPassword(String name, String password);

    /**
     * @return Set of all UserEntity (for testing/management purposes).
     */
    @Query(nativeQuery = true,
            value = "SELECT * FROM \"user\" u")
    Page<UserEntity> findAll(Pageable pageable);
}
