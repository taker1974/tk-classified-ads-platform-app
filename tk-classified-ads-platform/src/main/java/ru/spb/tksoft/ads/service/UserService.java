package ru.spb.tksoft.ads.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
import ru.spb.tksoft.ads.exception.TkDeletingMediaException;
import ru.spb.tksoft.ads.exception.TkNullArgumentException;
import ru.spb.tksoft.ads.exception.TkSavingMediaException;
import ru.spb.tksoft.ads.exception.TkUserNotFoundException;
import ru.spb.tksoft.ads.mapper.UserMapper;
import ru.spb.tksoft.ads.repository.UserRepository;
import ru.spb.tksoft.ads.tools.ImageValidationProperties;
import ru.spb.tksoft.ads.tools.ImageValidator;
import ru.spb.tksoft.ads.tools.PageTools;
import ru.spb.tksoft.utils.log.LogEx;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

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

    private final ImageValidationProperties properties;

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

    @Value("${media.avatar-base-path}")
    private String avatarBasePath;

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

        UserEntity user = userRepository.findOneByNameWithAvatar(userName)
                .orElseThrow(() -> new TkUserNotFoundException(userName, false));

        UserResponseDto dto = UserMapper.toDto(user);
        
        if (user.getAvatar() != null) {
            final String path = Paths.get(avatarBasePath)
                .resolve(user.getId().toString()).toString();
            dto.setImage(path);
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

        UserEntity user = userRepository.findOneByName(userName)
                .orElseThrow(() -> new TkUserNotFoundException(userName, true));

        user.setPassword(passwordEncoder.encode(newPasswordRequest.getNewPassword()));
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

        UserEntity user = userRepository.findOneByName(userName)
                .orElseThrow(() -> new TkUserNotFoundException(userName, true));

        user.setFirstName(updateRequest.getFirstName());
        user.setLastName(updateRequest.getLastName());
        user.setPhone(updateRequest.getPhone());

        userRepository.save(user);

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return new UpdateUserResponseDto(
                updateRequest.getFirstName(),
                updateRequest.getLastName(),
                updateRequest.getPhone());
    }

    @Value("${media.avatar-path}")
    private String avatarPath;

    @Value("${media.io-buffer-size}")
    private int avatarIoBufferSize;

    /**
     * Save avatar file.
     * 
     * @param userDetails User details implementation.
     * @param image Uploaded image.
     * @return Filename.
     */
    public String saveAvatarFile(final MultipartFile image) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        ImageValidator.validateImage(image, properties);

        final String fileName = ImageValidator.getImageUniqueFileName(image);
        final Path filePath = Path.of(avatarPath, fileName);
        try {
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);
            try (
                    InputStream is = image.getInputStream();
                    OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                    BufferedInputStream bis = new BufferedInputStream(is, avatarIoBufferSize);
                    BufferedOutputStream bos = new BufferedOutputStream(os, avatarIoBufferSize)) {
                bis.transferTo(bos);
            }
        } catch (Exception e) {
            throw new TkSavingMediaException(filePath.toString());
        }

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return fileName;
    }

    /**
     * Update avatar of authenticated user.
     * 
     * @param userName Name.
     * @param fileName Filename.
     * @param fileSize File size.
     * @param fileType File type.
     * @throws TkNullArgumentException If any of arguments is null.
     */
    @Transactional
    public String updateAvatarDb(String userName,
            String fileName, long fileSize, String contentType) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        UserEntity user = userRepository.findOneByNameWithAvatar(userName)
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
                                deleteAvatar(oldFileName);
                            }
                        }
                    });
        }

        if (avatar == null) {
            avatar = new AvatarEntity();
            avatar.setUser(user);
            user.setAvatar(avatar);
        }

        avatar.setName(fileName);
        avatar.setSize((int) fileSize);
        avatar.setMediatype(contentType);

        userRepository.save(user);
        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return oldFileName;
    }

    /**
     * Delete avatar file.
     * 
     * @param fileName Filename.
     */
    public void deleteAvatar(final String fileName) {
        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);
        if (fileName != null && !fileName.isBlank()) {
            try {
                Files.deleteIfExists(Path.of(avatarPath, fileName));
            } catch (Exception ex) {
                throw new TkDeletingMediaException(fileName);
            }
        }
        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPED);
    }

    /**
     * Get avatar by user name.
     * 
     * @param userName User name.
     * @return Image resource.
     */
    public ResponseEntity<Resource> getAvatar(final long userId) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        UserEntity user = userRepository.findOneByIdWithAvatar(userId)
                .orElseThrow(() -> new TkUserNotFoundException(String.valueOf(userId), false));

        if (user.getAvatar() == null) {
            return ResponseEntity.notFound().build();
        }

        String filename = user.getAvatar().getName();
        if (filename.isBlank()) {
            return ResponseEntity.notFound().build();
        }

        Path filePath = Paths.get(avatarPath).resolve(filename);
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
