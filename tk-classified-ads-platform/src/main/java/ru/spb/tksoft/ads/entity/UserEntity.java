package ru.spb.tksoft.ads.entity;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.spb.tksoft.ads.enumeration.UserRole;
import ru.spb.tksoft.ads.enumeration.UserRoleConverter;

/**
 * User entity.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"user\"")
public class UserEntity {

    /** User ID. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Name as login as email. */
    @Column(name = "name", nullable = false, length = 32, unique = true)
    @Size(min = 4, max = 32)
    @NotBlank
    private String name;

    /** Password. Password can/will be encoded in bcrypt so it's length can be larger than 16. */
    @Column(name = "password", nullable = false, length = 64)
    @Size(min = 8, max = 64)
    @NotBlank
    private String password;

    /** First name. */
    @Column(name = "first_name", nullable = false, length = 16)
    @Size(min = 2, max = 16)
    @NotBlank
    private String firstName;

    /** Last name. */
    @Column(name = "last_name", nullable = false, length = 16)
    @Size(min = 2, max = 16)
    @NotBlank
    private String lastName;

    /** Phone number. */
    @Column(name = "password", nullable = false, length = 32)
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    @Size(min = 11, max = 32)
    @NotBlank
    private String phone;

    /** User's role, internal technical field. */
    @Column(name = "role_id", nullable = false, insertable = false, updatable = false)
    @JdbcTypeCode(SqlTypes.BIGINT)
    @NotNull
    private Long roleId;

    @Convert(converter = UserRoleConverter.class)
    @Transient
    @NotNull
    private UserRole role;

    /** Full constructor. */
    public UserEntity(long id, String name, String password,
            String firstName, String lastName,
            String phone, UserRole role) {

        this(name, password, firstName, lastName, phone, role);
        this.id = id;
    }

    /** Common constructor. */
    public UserEntity(String name, String password, String firstName, String lastName,
            String phone, UserRole role) {

        this.name = name;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        setRole(role);
    }

    /** Set user role. */
    public void setRole(UserRole role) {
        if (null == role) {
            throw new IllegalArgumentException("Role can not be null.");
        }
        this.role = role;
    }

    /** Get user role. */
    public UserRole getRole() {
        if (null == roleId) {
            throw new IllegalStateException("User role is not initialized.");
        }
        return UserRole.fromId(roleId);
    }
}
