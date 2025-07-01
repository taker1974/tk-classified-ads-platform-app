package ru.spb.tksoft.ads.projection;

import java.math.BigDecimal;

/**
 * Extended ad projection for {@link ru.spb.tksoft.ads.dto.response.AdExtendedResponseDto}.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public interface AdExtendedResponseProjection {

    /** Ad ID. */
    Long getId();

    /** Title. */
    String getTitle();

    /** Price. */
    BigDecimal getPrice();

    /** Description. */
    String getDescription();

    /**
     * Image. Image URL will be produced later from url part and image ID.
     */
    Long getImageId();

    /** Author's first name. */
    String getAuthorFirstName();

    /** Author's last name. */
    String getAuthorLastName();

    /** Author's login name / email. */
    String getEmail();

    /** Author's phone number. */
    String getPhone();
}
