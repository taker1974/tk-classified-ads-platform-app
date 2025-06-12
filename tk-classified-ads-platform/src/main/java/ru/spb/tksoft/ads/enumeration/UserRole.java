package ru.spb.tksoft.ads.enumeration;

/**
 * User's role type.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public enum UserRole {
    
    ADMIN, USER;
    
    public static UserRole fromId(Long id) {

        if (id == null) {
            return null;
        }

        return switch (id.intValue()) {
            case 1 -> ADMIN;
            case 2 -> USER;
            default -> throw new IllegalArgumentException("Unknown role ID: " + id);
        };
    }

    public Long getId() {

        return switch (this) {
            case USER -> 1L;
            case ADMIN -> 2L;
        };
    }
}
