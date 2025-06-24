package ru.spb.tksoft.ads.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.ads.dto.request.NewPasswordRequestDto;
import ru.spb.tksoft.ads.dto.request.UpdateUserRequestDto;
import ru.spb.tksoft.ads.dto.response.UpdateUserResponseDto;
import ru.spb.tksoft.ads.dto.response.UserResponseDto;
import ru.spb.tksoft.ads.entity.AvatarEntity;
import ru.spb.tksoft.ads.entity.UserEntity;
import ru.spb.tksoft.ads.exception.TkNullArgumentException;
import ru.spb.tksoft.ads.exception.TkUserNotFoundException;
import ru.spb.tksoft.ads.mapper.UserMapper;
import ru.spb.tksoft.ads.repository.UserRepository;
import ru.spb.tksoft.ads.tools.PageTools;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * User service.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final ResourceService resourceService;

    /**
     * Get paginated list of users.
     * 
     * @param pageable Paging info.
     * @return DTO.
     */
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        Page<UserEntity> entity = userRepository.findAll(pageable);
        List<UserResponseDto> dto = entity.stream().map(UserMapper::toDto).toList();

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return PageTools.convertListToPage(dto, pageable);
    }

    /**
     * Create user.
     * 
     * @param newUser New user.
     * @return DTO if created, empty DTO otherwise.
     */
    @Transactional
    public UserResponseDto createUser(UserEntity newUser) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        if (newUser == null) {
            throw new TkNullArgumentException("newUser");
        }

        UserEntity entity = userRepository.save(newUser);
        UserResponseDto dto = UserMapper.toDto(entity);

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return dto;
    }

    /**
     * Check if user exists.
     * 
     * @param userName Name.
     * @return True if user exists.
     */
    public boolean existsByName(String userName) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.SHORT_RUN);

        if (userName == null) {
            throw new TkNullArgumentException("userName");
        }

        return userRepository.existsByName(userName);
    }

    /**
     * Check if user exists.
     * 
     * @param userName Name.
     * @param password Password.
     * @return True if user exists.
     */
    public boolean existsByNameAndPassword(String userName, String password) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.SHORT_RUN);

        if (userName == null) {
            throw new TkNullArgumentException("userName");
        }
        if (password == null) {
            throw new TkNullArgumentException("password");
        }

        return userRepository.existsByNameAndPassword(userName, password);
    }

    /**
     * Find user by name.
     * 
     * @param userName Name.
     * @return DTO if found, empty DTO otherwise.
     */
    public UserResponseDto findUserByName(String userName) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        if (userName == null) {
            throw new TkNullArgumentException("userName");
        }

        UserEntity user = userRepository.findByName(userName)
                .orElseThrow(() -> new TkUserNotFoundException(userName, false));

        UserResponseDto dto = UserMapper.toDto(user);

        if (user.getAvatar() != null) {
            dto.setImage(resourceService.getAvatarImageUrl(user.getId()));
        }

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return dto;
    }

    /**
     * Set new password for user.
     *
     * @param userDetails User details implementation.
     * @param newPasswordRequest DTO.
     */
    @Transactional
    public void setPassword(final UserDetails userDetails,
            final NewPasswordRequestDto newPasswordRequest) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        if (userDetails == null) {
            throw new TkNullArgumentException("userDetails");
        }
        if (newPasswordRequest == null) {
            throw new TkNullArgumentException("newPasswordRequest");
        }

        String userName = userDetails.getUsername();
        if (userName == null) {
            throw new TkNullArgumentException("userDetails.getUsername()");
        }

        UserEntity user = userRepository.findOneByNameRaw(userName)
                .orElseThrow(() -> new TkUserNotFoundException(userName, true));

        user.setPassword(passwordEncoder.encode(newPasswordRequest.getNewPassword()));

        // Save new entity.
        userRepository.save(user);

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPED);
    }

    /**
     * Update user.
     * 
     * @param userName Name.
     * @param updateRequest Request DTO.
     * @return Response DTO.
     */
    @Transactional
    public UpdateUserResponseDto updateUser(String userName,
            final UpdateUserRequestDto updateRequest) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        if (userName == null) {
            throw new TkNullArgumentException("userName");
        }
        if (updateRequest == null) {
            throw new TkNullArgumentException("updateRequest");
        }

        UserEntity user = userRepository.findOneByNameRaw(userName)
                .orElseThrow(() -> new TkUserNotFoundException(userName, true));

        user.setFirstName(updateRequest.getFirstName());
        user.setLastName(updateRequest.getLastName());
        user.setPhone(updateRequest.getPhone());

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return new UpdateUserResponseDto(
                updateRequest.getFirstName(),
                updateRequest.getLastName(),
                updateRequest.getPhone());
    }

    /**
     * Save avatar file.
     * 
     * @param image Uploaded image.
     * @return Filename.
     */
    public String saveAvatarFile(final MultipartFile image) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.SHORT_RUN);
        return resourceService.saveAvatarFile(image);
    }

    /**
     * Update avatar of authenticated user.
     * 
     * @param userName Name.
     * @param fileName Filename.
     * @param fileSize File size.
     * @param contentType File type.
     * @throws TkNullArgumentException If any of arguments is null.
     */
    @Transactional
    public void updateAvatarDb(String userName,
            String fileName, long fileSize, String contentType) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        UserEntity user = userRepository.findByName(userName)
                .orElseThrow(() -> new TkUserNotFoundException(userName, false));

        AvatarEntity avatar = user.getAvatar();

        // Planning to delete old avatar image.
        String oldFileName = avatar != null ? avatar.getName() : "";
        if (!oldFileName.isBlank()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCompletion(int status) {
                            if (status == STATUS_COMMITTED) {
                                resourceService.deleteAvatarImageFile(oldFileName);
                            }
                        }
                    });
        }

        avatar = avatar != null ? avatar : new AvatarEntity();
        avatar.setName(fileName);
        avatar.setSize((int) fileSize);
        avatar.setMediatype(contentType);

        avatar.setUser(user);
        user.setAvatar(avatar);

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPED);
    }

    /**
     * Delete avatar file.
     * 
     * @param fileName Filename.
     */
    public void deleteAvatarFile(final String fileName) {
        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.SHORT_RUN);
        resourceService.deleteAvatarImageFile(fileName);
    }

    /**
     * Get avatar by user id.
     * 
     * @param userId User ID.
     * @return Image resource.
     */
    public ResponseEntity<Resource> getAvatar(final long userId) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new TkUserNotFoundException(String.valueOf(userId), false));

        if (user.getAvatar() == null) {
            return ResponseEntity.notFound().build();
        }

        String filename = user.getAvatar().getName();
        if (filename.isBlank()) {
            return ResponseEntity.notFound().build();
        }

        Path filePath = resourceService.getAvatarImagePath(filename);
        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new PathResource(filePath);
        MediaType mediaType = MediaType.parseMediaType(user.getAvatar().getMediatype());

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(resource);

    }
}
