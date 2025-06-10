package ru.spb.tksoft.ads.enumeration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * User's role type.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public enum UserRole {

    USER("USER"), ADMIN("ADMIN");

    @NotBlank
    private final String roleName;

    /**
     * User's role type.
     * @param roleName User's role type name.
     */
    UserRole(@NotBlank @Size(min = 4, max = 5) String roleName) {
        this.roleName = roleName.toUpperCase();
    }
}
