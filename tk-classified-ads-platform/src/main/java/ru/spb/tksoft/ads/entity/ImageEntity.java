package ru.spb.tksoft.ads.entity;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
 * Image entity (ad's image).
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"image\"")
public class ImageEntity {

    /** Image ID. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Parent ad. */
    @JsonBackReference("ad-images")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id", nullable = false)
    @NotNull
    private AdEntity ad;

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
    public ImageEntity(long id, String name, int size, String mediatype) {

        this(name, size, mediatype);
        this.id = id;
    }

    /** Common constructor. */
    public ImageEntity(String name, int size, String mediatype) {

        this.name = name;
        this.size = size;
        this.mediatype = mediatype;
    }

    /** Set back link. */
    public void setAd(AdEntity ad) {

        if (this.ad != null) {
            this.ad.getImages().remove(this);
        }

        this.ad = ad;
        if (ad != null && !ad.getImages().contains(this)) {
            ad.getImages().add(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImageEntity that = (ImageEntity) o;
        return Objects.equals(id, that.id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return getClass().hashCode(); // for transients
    }
}
