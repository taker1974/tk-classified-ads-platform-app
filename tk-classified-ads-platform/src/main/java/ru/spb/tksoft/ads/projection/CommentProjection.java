package ru.spb.tksoft.ads.projection;

import java.time.Instant;

/**
 * Comment projection.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public interface CommentProjection {

    /** Comment ID. */
    Long getId();

    /** Author ID. */
    Long getAuthorId();

    /** Author's first name. */
    String getAuthorFirstName();

    /** Author's image ID. */
    String getAuthorAvatarId();

    /** Created at timestamp. */
    Instant getCreatedAt();

    /** Comment text. */
    String getText();
}
