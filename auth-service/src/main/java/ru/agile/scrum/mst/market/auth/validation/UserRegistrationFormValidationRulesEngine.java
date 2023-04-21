package ru.agile.scrum.mst.market.auth.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.agile.scrum.mst.market.api.RegistrationUserDto;
import ru.agile.scrum.mst.market.api.ValidationError;
import ru.agile.scrum.mst.market.api.ValueWrapper;
import ru.agile.scrum.mst.market.auth.exceptions.UserFormValidationErrorException;
import ru.agile.scrum.mst.market.auth.services.UserService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class UserRegistrationFormValidationRulesEngine {

    private final UserService userService;
    Validator<RegistrationUserDto> validator;

    private final Predicate<RegistrationUserDto> isUsernameNull = form -> form.getUsername() == null;
    private final Predicate<RegistrationUserDto> isUsernameBlank = form -> form.getUsername().isBlank();
    private final Predicate<RegistrationUserDto> isUsernameNotValid = form -> !form.getUsername().matches("^[a-zA-Z0-9]+$");
    private final Predicate<RegistrationUserDto> isPasswordNull = form -> form.getPassword() == null;
    private final Predicate<RegistrationUserDto> isPasswordBlank = form -> form.getPassword().isBlank();
    private final Predicate<RegistrationUserDto> isConfirmPasswordNull = form -> form.getConfirmPassword() == null;
    private final Predicate<RegistrationUserDto> isConfirmPasswordBlank = form -> form.getConfirmPassword().isBlank();
    private final Predicate<RegistrationUserDto> isEmailNull = form -> form.getEmail() == null;
    private final Predicate<RegistrationUserDto> isEmailBlank = form -> form.getEmail().isBlank();
    private final Predicate<RegistrationUserDto> isFullNameNull = form -> form.getFullName() == null;
    private final Predicate<RegistrationUserDto> isFullNameBlank = form -> form.getFullName().isBlank();
    private final Predicate<RegistrationUserDto> isPasswordUnequalConfirmPassword =
            form -> !Objects.equals(form.getPassword(), form.getConfirmPassword());
    private final Predicate<RegistrationUserDto> isUsernameUnique = this::doesUsernameExist;
    private final Predicate<RegistrationUserDto> isEmailUnique = this::doesEmailExist;
    private final Predicate<RegistrationUserDto> isUsernameNotNullAndBlank = isUsernameNull.negate().and(isUsernameBlank);
    private final Predicate<RegistrationUserDto> isPasswordNotNullAndBlank = isPasswordNull.negate().and(isPasswordBlank);
    private final Predicate<RegistrationUserDto> isConfirmPasswordNotNullAndBlank =
            isConfirmPasswordNull.negate().and(isConfirmPasswordBlank);
    private final Predicate<RegistrationUserDto> isEmailNotNullAndBlank = isEmailNull.negate().and(isEmailBlank);
    private final Predicate<RegistrationUserDto> isFullNameNotNullAndBlank = isFullNameNull.negate().and(isFullNameBlank);
    private final Predicate<RegistrationUserDto> isUsernameNotNullAndNotBlankAndNotValid =
            isUsernameNull.negate().and(isUsernameBlank.negate()).and(isUsernameNotValid);

    @PostConstruct
    private void initialize() {
        validator = new Validator<>();
        validator.addRule(isUsernameNull,
                ValidationError.builder()
                        .fieldName("username")
                        .description("не заполнено")
                        .build());
        validator.addRule(isUsernameNotNullAndBlank,
                ValidationError.builder()
                        .fieldName("username")
                        .description("не должно быть пустым или состоять только из пробельных символов")
                        .build());
        validator.addRule(isPasswordNull,
                ValidationError.builder()
                        .fieldName("password")
                        .description("не заполнено")
                        .build());
        validator.addRule(isPasswordNotNullAndBlank,
                ValidationError.builder()
                        .fieldName("password")
                        .description("не должно быть пустым или состоять только из пробельных символов")
                        .build());
        validator.addRule(isConfirmPasswordNull,
                ValidationError.builder()
                        .fieldName("confirmPassword")
                        .description("не заполнено")
                        .build());
        validator.addRule(isConfirmPasswordNotNullAndBlank,
                ValidationError.builder()
                        .fieldName("confirmPassword")
                        .description("не должно быть пустым или состоять только из пробельных символов")
                        .build());
        validator.addRule(isEmailNull,
                ValidationError.builder()
                        .fieldName("email")
                        .description("не заполнено")
                        .build());
        validator.addRule(isEmailNotNullAndBlank,
                ValidationError.builder()
                        .fieldName("email")
                        .description("не должно быть пустым или состоять только из пробельных символов")
                        .build());
        validator.addRule(isFullNameNull,
                ValidationError.builder()
                        .fieldName("fullName")
                        .description("не заполнено")
                        .build());
        validator.addRule(isFullNameNotNullAndBlank,
                ValidationError.builder()
                        .fieldName("fullName")
                        .description("не должно быть пустым или состоять только из пробельных символов")
                        .build());
        validator.addRule(isPasswordUnequalConfirmPassword,
                ValidationError.builder()
                        .fieldName("confirmPassword")
                        .description("не совпадает с паролем")
                        .build());
        validator.addRule(isUsernameUnique,
                ValidationError.builder()
                        .fieldName("username")
                        .description("такой пользователь уже существует")
                        .build());
        validator.addRule(isEmailUnique,
                ValidationError.builder()
                        .fieldName("email")
                        .description("такой email уже используется")
                        .build());
        validator.addRule(isUsernameNotNullAndNotBlankAndNotValid,
                ValidationError.builder()
                        .fieldName("username")
                        .description("содержит недопустимый символ, должно состоять только из латиницы и цифр")
                        .build());
    }

    public final void check(RegistrationUserDto form) {
        List<ValidationError> errors = validator.validate(form);
        if (!errors.isEmpty()) {
            throw new UserFormValidationErrorException(new ValueWrapper<>(errors));
        }
    }

    private boolean doesUsernameExist(RegistrationUserDto form) {
        return userService.existByUsername(form.getUsername());
    }

    private boolean doesEmailExist(RegistrationUserDto form) {
        return userService.existByEmail(form.getEmail());
    }
}
