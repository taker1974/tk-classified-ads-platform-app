package ru.spb.tksoft.ads.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Ad entity.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"ad\"")
public class AdEntity {

    /** Ad ID. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** User. */
    @Column(nullable = false)
    @NotNull
    private UserEntity user;

    /** Title. */
    @Column(nullable = false, length = 32)
    @Size(min = 4, max = 32)
    @NotBlank
    private String title;

    /** Price. */
    @Column(nullable = false, length = 32)
    @Min(0)
    @Max(10_000_000)
    private int price;

    /** Description. */
    @Column(nullable = false, length = 64)
    @Size(min = 8, max = 64)
    @NotBlank
    private String description;

    /** Full constructor. */
    public AdEntity(long id, UserEntity user, String title, int price, String description) {

        this(user, title, price, description);
        this.id = id;
    }

    /** Common constructor. */
    public AdEntity(UserEntity user, String title, int price, String description) {

        this.user = user;
        this.title = title;
        this.price = price;
        this.description = description;
    }
}
