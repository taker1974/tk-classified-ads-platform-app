package ru.spb.tksoft.ads.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import ru.spb.tksoft.ads.entity.AvatarEntity;
import ru.spb.tksoft.ads.entity.UserEntity;
import ru.spb.tksoft.ads.exception.TkNullArgumentException;
import ru.spb.tksoft.ads.mapper.UserMapper;
import ru.spb.tksoft.ads.repository.UserRepository;
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

    private final UserServiceCached userServiceCached;

    private final ResourceService resourceService;

    /**
     * Create user.
     * 
     * @param newUser New user.
     * @return DTO if created, empty DTO otherwise.
     */
    @Transactional
    public void createUser(final UserEntity newUser) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);
        if (newUser == null) {
            throw new TkNullArgumentException("newUser");
        }
        userRepository.save(newUser);
        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPED);
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

        userServiceCached.getUserEntityLazy(userDetails.getUsername())
                .setPassword(passwordEncoder.encode(newPasswordRequest.getNewPassword()));

        userServiceCached.clearCaches();
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
    public UpdateUserResponseDto updateUser(final String userName,
            final UpdateUserRequestDto updateRequest) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        if (userName == null) {
            throw new TkNullArgumentException("userName");
        }
        if (updateRequest == null) {
            throw new TkNullArgumentException("updateRequest");
        }

        UserEntity user = userServiceCached.getUserEntityLazy(userName);
        user.setFirstName(updateRequest.getFirstName());
        user.setLastName(updateRequest.getLastName());
        user.setPhone(updateRequest.getPhone());

        userServiceCached.clearCaches();

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return UserMapper.toDto(updateRequest);
    }

    /**
     * Save avatar file.
     * 
     * @param image Uploaded image.
     * @return Filename.
     */
    public String saveAvatarFile(final MultipartFile image) {
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
    public void updateAvatarDb(final String userName,
            final String fileName, final long fileSize, final String contentType) {

        UserEntity user = userServiceCached.getUserEntityEager(userName);
        AvatarEntity avatar = user.getAvatar();

        // Planning to delete old avatar image.
        final String oldFileName = avatar != null ? avatar.getName() : "";
        if (oldFileName != null && !oldFileName.isBlank()) {
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

        user.setAvatar(avatar);
        userServiceCached.clearCaches();
    }

    /**
     * Delete avatar file.
     * 
     * @param fileName Filename.
     */
    public void deleteAvatarFile(final String fileName) {
        resourceService.deleteAvatarImageFile(fileName);
    }
}
