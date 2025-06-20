package ru.spb.tksoft.ads.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.ads.dto.request.NewPasswordRequestDto;
import ru.spb.tksoft.ads.dto.request.UpdateUserRequestDto;
import ru.spb.tksoft.ads.dto.response.UpdateUserResponseDto;
import ru.spb.tksoft.ads.dto.response.UserResponseDto;
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

        UserEntity user = userRepository.findOneByName(userName)
                .orElseThrow(() -> new TkUserNotFoundException(userName, false));

        UserResponseDto dto = UserMapper.toDto(user);

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return dto;
    }

    /**
     * Find user by name and password.
     * 
     * @param userName Name.
     * @param password Password.
     * @return DTO if found, empty DTO otherwise.
     */
    public UserResponseDto findUserByNameAndPassword(String userName, String password) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        if (userName == null) {
            throw new TkNullArgumentException("userName");
        }
        if (password == null) {
            throw new TkNullArgumentException("password");
        }

        UserEntity user = userRepository.findOneByNameAndPassword(userName, password)
                .orElseThrow(() -> new TkUserNotFoundException(userName, true));

        UserResponseDto dto = UserMapper.toDto(user);

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return dto;
    }

    /**
     * Set new password for user.
     *
     * @param userDetails User details implementation.
     * @param newPasswordRequest DTO.
     */
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
}
