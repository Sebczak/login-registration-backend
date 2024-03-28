package com.comapny.registration;

import com.comapny.Messages;
import com.comapny.appuser.AppUser;
import com.comapny.appuser.AppUserRole;
import com.comapny.appuser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private EmailValidator emailValidator;

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
}
