package arquillian.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

@Documented
@Target({ ANNOTATION_TYPE, METHOD, FIELD, CONSTRUCTOR, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CodeValidator.class)
@Size(min = 5, message = "{example.code.min_size}")
@Pattern(regexp = "[0-9]*")
@NotNull(message = "{example.code.cannot_be_null}")
public @interface Code {

    String message() default "{example.code.invalid_code}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Type type() default Type.A;

    public enum Type { A, B }
}
