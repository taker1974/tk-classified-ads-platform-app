package ru.spb.tksoft.ads;

import java.io.IOException;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import ru.spb.tksoft.ads.dto.request.CreateOrUpdateCommentRequestDto;
import ru.spb.tksoft.ads.dto.response.AdResponseDto;
import ru.spb.tksoft.ads.dto.response.CommentResponseDto;
import ru.spb.tksoft.ads.dto.response.CommentsArrayResponseDto;
import org.springframework.core.io.Resource;

import static ru.spb.tksoft.utils.string.StringEx.r;

/**
 * E2E for CommentController.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
class CommentControllerE2ETest extends E2ETestBase {

    private AdResponseDto testAd;
    private UserCredentials credentials;

    @BeforeEach
    void setupEach() throws IOException {

        commentRepository.deleteAll();

        imageRepository.deleteAll();
        adRepository.deleteAll();

        userRepository.deleteAll();

        userServiceCached.clearCaches();
        adServiceCached.clearCaches();

        Resource imageResource = new ClassPathResource(TEST_IMAGE);
        testImageBytes = imageResource.getInputStream().readAllBytes();

        credentials = registerAndLoginUser();
        testAd = createAd(credentials);
    }

    @AfterEach
    void tearDownAll() {
        clearMedia();
    }

    static final String COMMENT_TEXT = "Test comment";

    @DisplayName("Add comment - should return 200 and created comment")
    @Test
    void addComment_shouldReturn200AndCreatedComment_whenValidRequest() {

        HttpHeaders headers = createBasicAuthHeaders(credentials);
        headers.setContentType(MediaType.APPLICATION_JSON);

        CreateOrUpdateCommentRequestDto request =
                new CreateOrUpdateCommentRequestDto(COMMENT_TEXT);

        ResponseEntity<CommentResponseDto> response = restTemplate.exchange(
                r("{api}/ads/{id}/comments", api(), testAd.getId()),
                HttpMethod.POST, new HttpEntity<>(request, headers),
                CommentResponseDto.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(COMMENT_TEXT, response.getBody().getText());
        Assertions.assertNotEquals(0, response.getBody().getId());
    }

    @DisplayName("Get comments - should return 200 and empty list when no comments")
    @Test
    void getComments_shouldReturn200AndEmptyList_whenNoComments() {

        HttpHeaders headers = createBasicAuthHeaders(credentials);

        ResponseEntity<CommentsArrayResponseDto> response = restTemplate.exchange(
                r("{api}/ads/{id}/comments", api(), testAd.getId()),
                HttpMethod.GET, new HttpEntity<>(headers),
                CommentsArrayResponseDto.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(0, response.getBody().getCount());
        Assertions.assertTrue(response.getBody().getResults().isEmpty());
    }

    private CommentResponseDto addComment(long adId,
            CreateOrUpdateCommentRequestDto request, HttpHeaders headers) {

        ResponseEntity<CommentResponseDto> response = restTemplate.exchange(
                r("{api}/ads/{id}/comments", api(), adId),
                HttpMethod.POST, new HttpEntity<>(request, headers),
                CommentResponseDto.class);

        return response.getBody();
    }

    @DisplayName("Get comments - should return 200 and comments list")
    @Test
    void getComments_shouldReturnCommentsList_whenCommentsExist() {

        HttpHeaders headers = createBasicAuthHeaders(credentials);
        headers.setContentType(MediaType.APPLICATION_JSON);

        var request = new CreateOrUpdateCommentRequestDto("First comment");
        addComment(testAd.getId(), request, headers);
        addComment(testAd.getId(), new CreateOrUpdateCommentRequestDto("Second comment"),
                headers);

        ResponseEntity<CommentsArrayResponseDto> response = restTemplate.exchange(
                r("{api}/ads/{id}/comments", api(), testAd.getId()),
                HttpMethod.GET, new HttpEntity<>(headers),
                CommentsArrayResponseDto.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(2, response.getBody().getCount());

        Set<CommentResponseDto> comments = response.getBody().getResults();
        Assertions.assertEquals(2, comments.size());
        Assertions.assertTrue(comments.stream()
                .anyMatch(c -> "First comment".equals(c.getText())));
        Assertions.assertTrue(comments.stream()
                .anyMatch(c -> "Second comment".equals(c.getText())));
    }

    @DisplayName("Update comment - should return 200 and updated comment")
    @Test
    void updateComment_shouldReturnUpdatedComment_whenValidRequest() {

        HttpHeaders headers = createBasicAuthHeaders(credentials);
        headers.setContentType(MediaType.APPLICATION_JSON);

        CommentResponseDto comment = addComment(
                testAd.getId(),
                new CreateOrUpdateCommentRequestDto("Original comment"),
                headers);

        var updateRequest = new CreateOrUpdateCommentRequestDto("Updated comment");

        ResponseEntity<CommentResponseDto> response = restTemplate.exchange(
                r("{api}/ads/{id}/comments/{commentId}", api(), testAd.getId(),
                        comment.getId()),
                HttpMethod.PATCH, new HttpEntity<>(updateRequest, headers),
                CommentResponseDto.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("Updated comment", response.getBody().getText());
        Assertions.assertEquals(comment.getId(), response.getBody().getId());
    }

    @DisplayName("Delete comment - should return 204 when valid")
    @Test
    void deleteComment_shouldReturn204_whenValidCommentId() {

        HttpHeaders headers = createBasicAuthHeaders(credentials);
        headers.setContentType(MediaType.APPLICATION_JSON);

        CommentResponseDto comment = addComment(
                testAd.getId(),
                new CreateOrUpdateCommentRequestDto("To be deleted"),
                headers);

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                r("{api}/ads/{id}/comments/{commentId}", api(), testAd.getId(),
                        comment.getId()),
                HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        ResponseEntity<CommentsArrayResponseDto> getResponse = restTemplate.exchange(
                r("{api}/ads/{id}/comments", api(), testAd.getId()),
                HttpMethod.GET, new HttpEntity<>(headers),
                CommentsArrayResponseDto.class);

        Assertions.assertEquals(0, getResponse.getBody().getCount());
    }

    @DisplayName("Add comment - should return 404 when ad not found")
    @Test
    void addComment_shouldReturn404_whenAdNotFound() {

        HttpHeaders headers = createBasicAuthHeaders(credentials);
        headers.setContentType(MediaType.APPLICATION_JSON);

        CreateOrUpdateCommentRequestDto request =
                new CreateOrUpdateCommentRequestDto(COMMENT_TEXT);
        long invalidAdId = 9999L; // Несуществующий ID

        ResponseEntity<CommentResponseDto> response = restTemplate.exchange(
                r("{api}/ads/{id}/comments", api(), invalidAdId),
                HttpMethod.POST, new HttpEntity<>(request, headers),
                CommentResponseDto.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @DisplayName("Update comment - should return 404 when comment not found")
    @Test
    void updateComment_shouldReturn404_whenCommentNotFound() {

        HttpHeaders headers = createBasicAuthHeaders(credentials);
        headers.setContentType(MediaType.APPLICATION_JSON);

        CreateOrUpdateCommentRequestDto updateRequest =
                new CreateOrUpdateCommentRequestDto("Updated comment");
        long invalidCommentId = 9999L;

        ResponseEntity<CommentResponseDto> response = restTemplate.exchange(
                r("{api}/ads/{id}/comments/{commentId}", api(), testAd.getId(),
                        invalidCommentId),
                HttpMethod.PATCH, new HttpEntity<>(updateRequest, headers),
                CommentResponseDto.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
