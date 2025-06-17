package ru.spb.tksoft.ads.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.ads.dto.NewPasswordRequestDto;
import ru.spb.tksoft.ads.dto.UpdateUserDto;
import ru.spb.tksoft.ads.dto.UserResponseDto;
import ru.spb.tksoft.ads.entity.UserEntity;
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

    @NotNull
    private final UserRepository userRepository;

    @NotNull
    private final PasswordEncoder passwordEncoder;

    /**
     * Get paginated list of users.
     * 
     * @return DTO.
     */
    @NotNull
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
    @NotNull
    public UserResponseDto createUser(@NotNull final UserEntity newUser) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

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
    public boolean existsByName(@NotBlank final String userName) {
        return userRepository.existsByName(userName);
    }

    /**
     * Check if user exists.
     * 
     * @param userName Name.
     * @param password Password.
     * @return True if user exists.
     */
    public boolean existsByNameAndPassword(@NotBlank final String userName,
            @NotBlank final String password) {
        return userRepository.existsByNameAndPassword(userName, password);
    }

    /**
     * Find user by name.
     * 
     * @param userName Name.
     * @return DTO if found, empty DTO otherwise.
     */
    @NotNull
    public UserResponseDto findUserByName(@NotBlank final String userName) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        UserEntity user = userRepository.findOneByName(userName)
                .orElseThrow(() -> new TkUserNotFoundException(userName, false));

        UserResponseDto dto = UserMapper.toDto(user);

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return dto;
    }

    /**
     * Find user by name and password.
     * 
     * @return DTO if found, empty DTO otherwise.
     */
    @NotNull
    public UserResponseDto findUserByNameAndPassword(@NotBlank final String userName,
            @NotBlank final String password) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        UserEntity user = userRepository.findOneByNameAndPassword(userName, password)
                .orElseThrow(() -> new TkUserNotFoundException(userName, true));

        UserResponseDto dto = UserMapper.toDto(user);

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return dto;
    }

    /**
     * Set new password for user.
     *
     * No response DTO. Http response:<br>
     * "200": description: OK <br>
     * "401": description: Unauthorized <br>
     * "403": description: Forbidden <br>
     * 
     * @param newPasswordRequest DTO.
     */
    public void setPassword(@NotNull final UserDetails userDetails,
            @NotNull final NewPasswordRequestDto newPasswordRequest) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        String userName = userDetails.getUsername();

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
     * @param updateUserDto DTO.
     * @return DTO.
     */
    @NotNull
    public UpdateUserDto updateUser(@NotBlank final String userName,
            @NotNull final UpdateUserDto updateRequest) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        UserEntity user = userRepository.findOneByName(userName)
                .orElseThrow(() -> new TkUserNotFoundException(userName, true));

        user.setFirstName(updateRequest.getFirstName());
        user.setLastName(updateRequest.getLastName());
        user.setPhone(updateRequest.getPhone());

        userRepository.save(user);
        
        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return updateRequest;
    }
}
