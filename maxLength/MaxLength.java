package rustbuilds.server.annotation.validators.maxLength;

import com.nimbusds.jose.Payload;
import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = MaxLengthValidator.class)
public @interface MaxLength {
    String message() default "";
    String max() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
