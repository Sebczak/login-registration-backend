package com.comapny.registration;

import com.comapny.Messages;
import com.comapny.appuser.AppUser;
import com.comapny.appuser.AppUserRole;
import com.comapny.appuser.AppUserService;
import com.comapny.registration.token.ConfirmationToken;
import com.comapny.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.email());
        if (!isValidEmail) {
            throw new IllegalStateException(Messages.EMAIL_NOT_VALID);
        }
        return appUserService.signUpUser(
                new AppUser(
                        request.firstname(),
                        request.lastname(),
                        request.username(),
                        request.email(),
                        request.password(),
                        AppUserRole.USER
                ));
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException(Messages.TOKEN_NOT_FOUND));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException(Messages.EMAIL_ALREADY_CONFIRMED);
        }

        LocalDateTime expiredAt = confirmationToken.getExpiredAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException(Messages.TOKEN_CONFIRMED);
        }

        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(
                confirmationToken.getAppUser().getEmail());
        return "confirmed";
    }

}
