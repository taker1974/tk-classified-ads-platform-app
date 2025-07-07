package ru.spb.tksoft.ads;

import java.io.IOException;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import ru.spb.tksoft.ads.dto.request.CreateOrUpdateAdRequestDto;
import ru.spb.tksoft.ads.dto.response.AdExtendedResponseDto;
import ru.spb.tksoft.ads.dto.response.AdResponseDto;
import ru.spb.tksoft.ads.dto.response.AdsArrayResponseDto;
import org.springframework.core.io.Resource;
import org.springframework.util.MultiValueMap;

/**
 * E2E for AdController.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
class AdControllerE2ETest extends E2ETestBase {

    @BeforeEach
    void setupEach() throws IOException {

        imageRepository.deleteAll();
        adRepository.deleteAll();

        userRepository.deleteAll();

        userServiceCached.clearCaches();
        adServiceCached.clearCaches();

        Resource imageResource = new ClassPathResource(TEST_IMAGE);
        testImageBytes = imageResource.getInputStream().readAllBytes();
    }

    @DisplayName("Get all ads - should return 200 and empty list when no ads")
    @Test
    void getAllAds_shouldReturn200AndEmptyList_whenNoAds() {

        UserCredentials credentials = registerAndLoginUser();
        HttpHeaders headers = createBasicAuthHeaders(credentials);

        ResponseEntity<AdsArrayResponseDto> response = restTemplate.exchange(
                getBaseUrl() + "/ads",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                AdsArrayResponseDto.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(0, response.getBody().getCount());
        Assertions.assertTrue(response.getBody().getResults().isEmpty());
    }

    @DisplayName("Create ad - should return 201 when valid request")
    @Test
    void addAd_shouldReturn201_whenValidRequest() {

        UserCredentials credentials = registerAndLoginUser();
        HttpHeaders headers = createBasicAuthHeaders(credentials);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        CreateOrUpdateAdRequestDto properties = new CreateOrUpdateAdRequestDto(
                "Test Ad",
                1000,
                "Test description");

        ByteArrayResource resource = new ByteArrayResource(testImageBytes) {
            @Override
            public String getFilename() {
                return TEST_IMAGE;
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("properties", properties);
        body.add("image", resource);

        ResponseEntity<AdResponseDto> response = restTemplate.exchange(
                getBaseUrl() + "/ads",
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                AdResponseDto.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("Test Ad", response.getBody().getTitle());
        Assertions.assertEquals(1000, response.getBody().getPrice());
    }

    @DisplayName("Get user ads - should return 200 and user ads")
    @Test
    void getAdsMe_shouldReturnUserAds_whenAuthenticated() {

        UserCredentials credentials = registerAndLoginUser();
        HttpHeaders headers = createBasicAuthHeaders(credentials);

        long adId = createAd(credentials).getId();

        ResponseEntity<AdsArrayResponseDto> response = restTemplate.exchange(
                getBaseUrl() + "/ads/me",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                AdsArrayResponseDto.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(1, response.getBody().getCount());

        Set<AdResponseDto> ads = response.getBody().getResults();
        Assertions.assertEquals(1, ads.size());
        Assertions.assertEquals(adId, ads.iterator().next().getId());
    }

    @DisplayName("Get ad info - should return 200 and ad details")
    @Test
    void getAdInfo_shouldReturnAdDetails_whenValidAdId() {

        UserCredentials credentials = registerAndLoginUser();
        HttpHeaders headers = createBasicAuthHeaders(credentials);

        AdResponseDto createdAd = createAd(credentials);

        ResponseEntity<AdExtendedResponseDto> response = restTemplate.exchange(
                getBaseUrl() + "/ads/" + createdAd.getId(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                AdExtendedResponseDto.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(createdAd.getId(), response.getBody().getId());
        Assertions.assertEquals("Test Ad", response.getBody().getTitle());
        Assertions.assertEquals(1000, response.getBody().getPrice());
        Assertions.assertNotNull(response.getBody().getEmail());
    }

    @DisplayName("Update ad image - should return 200 when valid image")
    @Test
    void updateImage_shouldReturn200_whenValidImage() {

        UserCredentials credentials = registerAndLoginUser();
        HttpHeaders headers = createBasicAuthHeaders(credentials);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        AdResponseDto createdAd = createAd(credentials);

        ByteArrayResource resource = new ByteArrayResource(testImageBytes) {
            @Override
            public String getFilename() {
                return "new-image.jpg";
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", resource);

        ResponseEntity<Void> response = restTemplate.exchange(
                getBaseUrl() + "/ads/" + createdAd.getId() + "/image",
                HttpMethod.PATCH,
                new HttpEntity<>(body, headers),
                Void.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @DisplayName("Update ad - should return 200 and updated ad")
    @Test
    void updateAds_shouldReturnUpdatedAd_whenValidRequest() {

        UserCredentials credentials = registerAndLoginUser();
        HttpHeaders headers = createBasicAuthHeaders(credentials);
        headers.setContentType(MediaType.APPLICATION_JSON);

        AdResponseDto createdAd = createAd(credentials);

        CreateOrUpdateAdRequestDto updateRequest = new CreateOrUpdateAdRequestDto(
                "Updated Title",
                2000,
                "Updated description");

        ResponseEntity<AdResponseDto> response = restTemplate.exchange(
                getBaseUrl() + "/ads/" + createdAd.getId(),
                HttpMethod.PATCH,
                new HttpEntity<>(updateRequest, headers),
                AdResponseDto.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("Updated Title", response.getBody().getTitle());
        Assertions.assertEquals(2000, response.getBody().getPrice());
    }

    @DisplayName("Delete ad - should return 204 when valid")
    @Test
    void removeAd_shouldReturn204_whenValidAdId() {

        UserCredentials credentials = registerAndLoginUser();
        HttpHeaders headers = createBasicAuthHeaders(credentials);

        AdResponseDto createdAd = createAd(credentials);

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                getBaseUrl() + "/ads/" + createdAd.getId(),
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        ResponseEntity<AdExtendedResponseDto> getResponse = restTemplate.exchange(
                getBaseUrl() + "/ads/" + createdAd.getId(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                AdExtendedResponseDto.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @DisplayName("Get ad image - should return 200 when image exists")
    @Test
    void getAdImage_shouldReturnImage_whenExists() {

        UserCredentials credentials = registerAndLoginUser();

        AdResponseDto createdAd = createAd(credentials);

        ResponseEntity<Resource> response = restTemplate.getForEntity(
                getBaseUrl() + "/ads/image/" + createdAd.getId(),
                Resource.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getHeaders()
                .getContentType().toString().startsWith("image/"));
    }
}
