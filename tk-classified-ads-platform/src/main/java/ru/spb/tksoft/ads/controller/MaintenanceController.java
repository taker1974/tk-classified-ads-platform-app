package ru.spb.tksoft.ads.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.ads.service.AdsServiceCached;
import ru.spb.tksoft.ads.service.UserServiceCached;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Maintenance controller.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@RestController
@RequestMapping(value = "/maintenance")
@Tag(name = "Обслуживание сервиса")
@RequiredArgsConstructor
public class MaintenanceController {

    private final UserServiceCached userServiceCached;
    private final AdsServiceCached adsServiceCached;

    /**
     * Clear caches.
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Сброс всех кэшей")
    @PostMapping("/clear-caches")
    public void clearCaches() {
        
        userServiceCached.clearCaches();
        adsServiceCached.clearCaches();
    }
}
