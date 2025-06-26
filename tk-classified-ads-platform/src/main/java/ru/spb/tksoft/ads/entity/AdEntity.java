package ru.spb.tksoft.ads.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Ad entity.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"ad\"")
@NamedEntityGraph(
        name = "Ad.withUserAvatar",
        attributeNodes = {
                @NamedAttributeNode("user"),
                @NamedAttributeNode("images"),
                @NamedAttributeNode(value = "user", subgraph = "user.avatar")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "user.avatar",
                        attributeNodes = @NamedAttributeNode("avatar"))
        })
public class AdEntity {

    /** Ad ID. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** User. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private UserEntity user;

    /** Title. */
    @Column(nullable = false, length = 32)
    @Size(min = 4, max = 32)
    @NotBlank
    private String title;

    /** Price. */
    @Column(nullable = false, precision = 10, scale = 0)
    @DecimalMin(value = "0.00", inclusive = true)
    @DecimalMax(value = "10000000.00", inclusive = true)
    private BigDecimal price;

    /** Description. */
    @Column(nullable = false, length = 64)
    @Size(min = 8, max = 64)
    @NotBlank
    private String description;

    /** Images. */
    @JsonManagedReference("ad-images")
    @OneToMany(mappedBy = "ad",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<ImageEntity> images = new ArrayList<>();

    /** Full constructor. */
    public AdEntity(long id, String title, BigDecimal price, String description) {

        this(title, price, description);
        this.id = id;
    }

    /** Common constructor. */
    public AdEntity(String title, BigDecimal price, String description) {

        this.title = title;
        this.price = price;
        this.description = description;
    }

    /** Add image. */
    public void addImage(ImageEntity image) {

        // Remove old image and it's back link.
        if (!images.isEmpty()) {
            ImageEntity imageOld = images.getFirst();
            images.remove(imageOld);
            imageOld.setAd(null);
        }

        // Add new image and set new back link.
        images.add(image);
        image.setAd(this);
    }
}
