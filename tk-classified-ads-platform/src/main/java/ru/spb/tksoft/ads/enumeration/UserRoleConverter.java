package ru.spb.tksoft.ads.enumeration;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * User's role converter.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Converter(autoApply = true)
public class UserRoleConverter implements AttributeConverter<UserRole, Long> {

    /**
     * Converts {@link UserRole} to {@link Long}.
     * 
     * @param attribute User's role.
     * @return User's role id.
     */
    @Override
    public Long convertToDatabaseColumn(UserRole attribute) {
        return (attribute != null) ? attribute.getId() : null;
    }

    /**
     * Converts {@link Long} to {@link UserRole}.
     * 
     * @param dbData User's role id.
     * @return User's role.
     */
    @Override
    public UserRole convertToEntityAttribute(Long dbData) {
        return (dbData != null) ? UserRole.fromId(dbData) : null;
    }
}
