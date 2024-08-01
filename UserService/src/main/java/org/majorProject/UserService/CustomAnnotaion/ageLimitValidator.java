package org.majorProject.UserService.CustomAnnotaion;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ageLimitValidator implements ConstraintValidator<AgeLimit, String> {

    private int minimumAge;

    @Override
    public void initialize(AgeLimit constraintAnnotation) {
        this.minimumAge=constraintAnnotation.minmumAge();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {



        if(StringUtils.isEmpty(s)){
            return false;
        }

        try {
            int  pYear = new SimpleDateFormat("dd/MM/yyyy").parse(s).getYear();

            int tYear = new Date().getYear();
            int diff = tYear - pYear;
            if (diff >= minimumAge) {
                return true;
            }
        } catch(ParseException e){
            return false;
        }

        return false;
    }
}
