package ru.spb.tksoft.ads.tools;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

/**
 * Page-related tools.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public final class PageTools {

    /**
     * Private constructor.
     */
    private PageTools() {}

    /**
     * Converts a list to a page.
     * 
     * @param list the list to be converted.
     * @param pageable the pageable data.
     * @param <T> the type of the list.
     * @return the converted page.
     * @throws IllegalArgumentException if the pageable data is invalid.
     */
    public static <T> Page<T> convertListToPage(List<T> list, Pageable pageable) {

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<T> pageList;

        if (list.size() < startItem) {
            pageList = List.of();
        } else {
            int toIndex = Math.min(startItem + pageSize, list.size());
            pageList = list.subList(startItem, toIndex);
        }

        return new PageImpl<>(pageList, pageable, list.size());
    }
}
