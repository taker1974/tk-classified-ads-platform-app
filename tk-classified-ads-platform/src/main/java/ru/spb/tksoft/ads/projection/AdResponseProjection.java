package ru.spb.tksoft.ads.projection;

import java.math.BigDecimal;

/**
 * Ad projection for {@link ru.spb.tksoft.ads.dto.response.AdResponseDto}.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public interface AdResponseProjection {

    /** Ad ID. */
    Long getId();

    /** Title. */
    String getTitle();
    
    /** Price. */
    BigDecimal getPrice();
    
    /** User/creator/author ID. */
    Long getUserId();

    /** Image. */
    Long getImageId();
}
