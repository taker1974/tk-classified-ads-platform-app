package ru.spb.tksoft.ads.entity;

import java.time.Instant;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Comment entity.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"comment\"")
@EntityListeners(AuditingEntityListener.class)
public class CommentEntity {

    /** Comment ID. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Source ad. */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id", nullable = false)
    @NotNull
    private AdEntity ad;

    /** Commentator. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private UserEntity user;

    /** 
     * Created timestamp. 
     * 
     * @see org.springframework.data.jpa.repository.config.EnableJpaAuditing
     * @see jakarta.persistence.EntityListeners
     * @see org.springframework.data.jpa.domain.support.AuditingEntityListener
     * @see ru.spb.tksoft.ads.TKAdsApplication
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull
    private Instant createdAt;

    /** Text. */
    @Column(nullable = false, unique = true, length = 64)
    @Size(min = 8, max = 64)
    @NotBlank
    private String text;

    /** Full constructor. */
    public CommentEntity(long id, String text) {

        this(text);
        this.id = id;
    }

    /** Common constructor. */
    public CommentEntity(String text) {

        this.text = text;
    }
}
