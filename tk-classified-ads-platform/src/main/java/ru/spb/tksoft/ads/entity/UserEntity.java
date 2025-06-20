package ru.spb.tksoft.ads.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.spb.tksoft.ads.enumeration.UserRole;

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
    @Column(nullable = false, length = 32, unique = true)
    @Size(min = 4, max = 32)
    @NotBlank
    private String name;

    /** Password. Password can/will be encoded in bcrypt so it's length can be larger than 16. */
    @Column(nullable = false, length = 64)
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
    @Column(nullable = false, length = 32)
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    @Size(min = 11, max = 32)
    @NotBlank
    private String phone;

    /** User's role. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @NotNull
    private UserRole role;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private AvatarEntity avatar;

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
        this.role = role;
    }
}
