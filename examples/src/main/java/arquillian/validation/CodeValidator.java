package arquillian.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class CodeValidator implements ConstraintValidator<Code, String> {

    List<String> codes;

    @Override
    public void initialize(Code constraintAnnotation) {
        System.out.println("CodeValidator.initialize");
        codes = new ArrayList<>();
        switch (constraintAnnotation.type()) {
            case A:
                codes.add("95054");
                codes.add("95051");
                codes.add("94043");
                break;
            case B:
                codes.add("110092");
                codes.add("400053");
                codes.add("700073");
                break;
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        System.out.println("Validating: " + value);
        System.out.println("state: " + codes.contains(value));
        return codes.contains(value);
    }
}
