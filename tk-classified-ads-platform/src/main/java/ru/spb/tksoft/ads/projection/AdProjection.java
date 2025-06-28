package ru.spb.tksoft.ads.projection;

/**
 * Ad projection for {@link ru.spb.tksoft.ads.dto.response.AdResponseDto}.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public interface AdProjection {

    /** Ad ID. */
    Long getId();

    /** User/creator/author ID. */
    Long getUserId();

    /** Title. */
    String getTitle();

    /** Price. */
    int getPrice();

    /** Image. */
    String getImage();
}
