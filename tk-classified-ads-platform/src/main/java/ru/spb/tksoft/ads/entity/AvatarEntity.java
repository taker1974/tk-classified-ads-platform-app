package ru.spb.tksoft.ads.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Avatar entity (user's image).
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"avatar\"")
public class AvatarEntity {

    /** Avatar ID. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Owner user. */
    @JsonBackReference("user-avatar")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private UserEntity user;

    /** Name. */
    @Column(nullable = false, unique = true, length = 256)
    @Size(min = 1, max = 256)
    @NotBlank
    private String name;

    /** Size. */
    @Column(nullable = false)
    @Min(0)
    @Max(10_000_000)
    private int size;

    /** Mediatype. */
    @Column(nullable = false, unique = true, length = 128)
    @Size(min = 1, max = 128)
    @NotBlank
    private String mediatype;

    /** Full constructor. */
    public AvatarEntity(long id, String name, int size, String mediatype) {

        this(name, size, mediatype);
        this.id = id;
    }

    /** Common constructor. */
    public AvatarEntity(String name, int size, String mediatype) {

        this.name = name;
        this.size = size;
        this.mediatype = mediatype;
    }
}
