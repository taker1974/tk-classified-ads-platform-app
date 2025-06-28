package ru.spb.tksoft.ads.projection;

/**
 * Ad projection extended for {@link ru.spb.tksoft.ads.dto.response.AdExtendedResponseDto}.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public interface AdExtendedProjection {

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

    /** Description. */
    String getDescription();

    /** Author's first name. */
    String getAuthorFirstName();

    /** Author's last name. */
    String getAuthorLastName();

    /** Author's login name / email. */
    String getEmail();

    /** Author's phone number. */
    String getPhone();
}
