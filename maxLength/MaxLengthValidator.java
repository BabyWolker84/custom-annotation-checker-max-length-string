package rustbuilds.server.annotation.validators.maxLength;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;

import java.util.Locale;

@RequiredArgsConstructor
@Slf4j
public class MaxLengthValidator implements ConstraintValidator<MaxLength, String> {
    private final Environment env;
    private final MessageSource messageSource;
    private String max;
    @Override
    public void initialize(MaxLength constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        max = constraintAnnotation.max();
    }
    @Override
    public boolean isValid(String valueStr, ConstraintValidatorContext constraintValidatorContext) {
        Locale locale = LocaleContextHolder.getLocale();
        if (max.isEmpty()) {
            String message = messageSource.getMessage(
                    "custom_annotation.max_length.message.parameter.max.is_empty", null, locale);
            log.error(message);
            throw new RuntimeException();
        }
        if (valueStr == null || valueStr.isEmpty()) {
            String message = messageSource.getMessage(
                    "custom_annotation.length.message.value.no_value", null, locale);
            log.warn(message);
            return true;
        }
        String maxValueObj = env.getProperty(max);
        if (maxValueObj == null || maxValueObj.isEmpty()) {
            String message = messageSource.getMessage(
                    "custom_annotation.max_length.message.max.value.not_set", null, locale);
            log.error(message);
            throw new RuntimeException();
        }
        int maxValue = Integer.parseInt(maxValueObj);
        if (valueStr.length() > maxValue) {
            String message = constraintValidatorContext.getDefaultConstraintMessageTemplate();
            message = messageSource.getMessage(message, null, locale);
            message = message.formatted(maxValue);
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
            log.warn(message);
            return false;
        }
        return true;
    }
}
