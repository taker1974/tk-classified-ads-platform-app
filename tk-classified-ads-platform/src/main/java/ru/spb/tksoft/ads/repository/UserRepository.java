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
        Optional<UserEntity> findOneByName(String name);

        /**
         * @return Set of all UserEntity (for testing/management purposes).
         */
        @Query("SELECT u FROM UserEntity u JOIN FETCH u.avatar")
        Page<UserEntity> findAll(Pageable pageable);

        /**
         * @return UserEntity with its avatar.
         */
        @Query("SELECT u FROM UserEntity u WHERE u.name = :name")
        Optional<UserEntity> findOneByNameWithAvatar(String name);

        /**
         * @return UserEntity with its avatar.
         */
        @Query("SELECT u FROM UserEntity u WHERE u.id = :id")
        Optional<UserEntity> findOneByIdWithAvatar(long id);
}
