package ru.spb.tksoft.ads;

import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.spb.tksoft.ads.dto.request.NewPasswordRequestDto;
import ru.spb.tksoft.ads.dto.request.UpdateUserRequestDto;
import ru.spb.tksoft.ads.dto.response.UpdateUserResponseDto;
import ru.spb.tksoft.ads.dto.response.UserResponseDto;

/**
 * E2E for UserController.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
class UserControllerE2ETest extends E2ETestBase {

    @BeforeEach
    void resetDatabase() {

        userRepository.deleteAll();
        userServiceCached.clearCaches();
    }

    @DisplayName("Set valid password")
    @Test
    void setPassword_200_whenValid() {

        UserCredentials credentials = registerAndLoginUser();

        NewPasswordRequestDto request = new NewPasswordRequestDto(
                credentials.password(),
                credentials.password() + "321");

        HttpHeaders headers = createBasicAuthHeaders(credentials);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Void> response = restTemplate.exchange(
                getBaseUrl() + "/users/set_password",
                HttpMethod.POST,
                new HttpEntity<>(request, headers),
                Void.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @DisplayName("Set valid password but from wrong user")
    @Test
    void setPassword_401_whenWrongUser() {

        UserCredentials credentials = registerAndLoginUser();

        NewPasswordRequestDto request = new NewPasswordRequestDto(
                credentials.password(),
                credentials.password() + "321");

        HttpHeaders headers = createBasicAuthHeaders(
                new UserCredentials("wrong@user.uk", "wrongPasswordUK"));
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Void> response = restTemplate.exchange(
                getBaseUrl() + "/users/set_password",
                HttpMethod.POST,
                new HttpEntity<>(request, headers),
                Void.class);

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    static Stream<String> invalidPasswords() {
        return Stream.of(null, "", "short", "too_long_123456781234567812345678");
    }

    @DisplayName("Set invalid password")
    @MethodSource("invalidPasswords")
    @ParameterizedTest
    void setPassword_400_whenInvalid(String newPassword) {

        UserCredentials credentials = registerAndLoginUser();

        NewPasswordRequestDto request = new NewPasswordRequestDto(
                credentials.password(),
                newPassword);

        HttpHeaders headers = createBasicAuthHeaders(credentials);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Void> response = restTemplate.exchange(
                getBaseUrl() + "/users/set_password",
                HttpMethod.POST,
                new HttpEntity<>(request, headers),
                Void.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @DisplayName("Get logged-in user's data")
    @Test
    void getUser_200_whenSuccess() {

        UserCredentials credentials = registerAndLoginUser();
        HttpHeaders headers = createBasicAuthHeaders(credentials);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<UserResponseDto> response = restTemplate.exchange(
                getBaseUrl() + "/users/me",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                UserResponseDto.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(credentials.name(), response.getBody().getEmail());
        Assertions.assertEquals("John", response.getBody().getFirstName());
    }

    @DisplayName("Update logged-in user's data")
    @Test
    void updateUser_200_whenSuccess() {

        UserCredentials credentials = registerAndLoginUser();
        HttpHeaders headers = createBasicAuthHeaders(credentials);
        headers.setContentType(MediaType.APPLICATION_JSON);

        UpdateUserRequestDto updateRequest = new UpdateUserRequestDto(
                "Alice", "Smith", "+79998887766");

        ResponseEntity<UpdateUserResponseDto> response = restTemplate.exchange(
                getBaseUrl() + "/users/me",
                HttpMethod.PATCH,
                new HttpEntity<>(updateRequest, headers),
                UpdateUserResponseDto.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("Alice", response.getBody().getFirstName());
        Assertions.assertEquals("Smith", response.getBody().getLastName());
        Assertions.assertEquals("+79998887766", response.getBody().getPhone());
    }

    @DisplayName("Update user avatar")
    @Test
    void updateUserImage_200_whenValidImage() {

        UserCredentials credentials = registerAndLoginUser();

        Resource imageResource = new ClassPathResource("test-avatar.jpg");
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", imageResource);

        HttpHeaders headers = createBasicAuthHeaders(credentials);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        ResponseEntity<Void> response = restTemplate.exchange(
                getBaseUrl() + "/users/me/image",
                HttpMethod.PATCH,
                new HttpEntity<>(body, headers),
                Void.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @DisplayName("Get default avatar")
    @Test
    void getAvatar_200_whenNoAvatar() {

        UserCredentials credentials = registerAndLoginUser();

        // Get user ID.
        HttpHeaders authHeaders = createBasicAuthHeaders(credentials);
        ResponseEntity<UserResponseDto> userResponse = restTemplate.exchange(
                getBaseUrl() + "/users/me",
                HttpMethod.GET,
                new HttpEntity<>(authHeaders),
                UserResponseDto.class);
        long userId = userResponse.getBody().getId();

        // Try to get user avatar without authorization.
        ResponseEntity<Resource> response = restTemplate.getForEntity(
                getBaseUrl() + "/users/avatar/" + userId,
                Resource.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
