package ru.agile.scrum.mst.market.auth.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.agile.scrum.mst.market.api.UserPersonalAccountRequest;
import ru.agile.scrum.mst.market.api.ValidationError;
import ru.agile.scrum.mst.market.api.ValueWrapper;
import ru.agile.scrum.mst.market.auth.entities.User;
import ru.agile.scrum.mst.market.auth.exceptions.UserFormValidationErrorException;
import ru.agile.scrum.mst.market.auth.services.UserService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class UserUpdateFormValidationRulesEngine {

    private final UserService userService;
    Validator<UserPersonalAccountRequest> validator;

    private final Predicate<UserPersonalAccountRequest> isPasswordNull = form -> form.getPassword() == null;
    private final Predicate<UserPersonalAccountRequest> isPasswordBlank = form -> form.getPassword().isBlank();
    private final Predicate<UserPersonalAccountRequest> isConfirmPasswordNull = form -> form.getConfirmPassword() == null;
    private final Predicate<UserPersonalAccountRequest> isConfirmPasswordBlank = form -> form.getConfirmPassword().isBlank();
    private final Predicate<UserPersonalAccountRequest> isEmailNull = form -> form.getEmail() == null;
    private final Predicate<UserPersonalAccountRequest> isEmailBlank = form -> form.getEmail().isBlank();
    private final Predicate<UserPersonalAccountRequest> isFullNameNull = form -> form.getFullName() == null;
    private final Predicate<UserPersonalAccountRequest> isFullNameBlank = form -> form.getFullName().isBlank();
    private final Predicate<UserPersonalAccountRequest> isEmailChangedAndUnique = this::isTheEmailOccupiedByAnotherUser;
    private final Predicate<UserPersonalAccountRequest> isPasswordNotNullAndBlank = isPasswordNull.negate().and(isPasswordBlank);
    private final Predicate<UserPersonalAccountRequest> isConfirmPasswordNotNullAndBlank =
            isConfirmPasswordNull.negate().and(isConfirmPasswordBlank);
    private final Predicate<UserPersonalAccountRequest> isEmailNotNullAndBlank = isEmailNull.negate().and(isEmailBlank);
    private final Predicate<UserPersonalAccountRequest> isFullNameNotNullAndBlank = isFullNameNull.negate().and(isFullNameBlank);

    @PostConstruct
    private void initialize() {
        validator = new Validator<>();
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
        validator.addRule(isEmailChangedAndUnique,
                ValidationError.builder()
                        .fieldName("email")
                        .description("такой email уже используется другим пользователем")
                        .build());
    }

    public final void check(UserPersonalAccountRequest form) {
        List<ValidationError> errors = validator.validate(form);
        if (!errors.isEmpty()) {
            throw new UserFormValidationErrorException(new ValueWrapper<>(errors));
        }
    }

    private boolean isTheEmailOccupiedByAnotherUser(UserPersonalAccountRequest form) {
        User user = userService.getUserByName(getTheLoggedInUserUsername());
        if (Objects.equals(form.getEmail(), user.getEmail())) {
            return false;
        }
        return userService.existByEmail(form.getEmail());
    }

    private String getTheLoggedInUserUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
