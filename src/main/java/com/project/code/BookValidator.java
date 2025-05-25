package com.project.code;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.ValidationUtils;

public class BookValidator implements Validator {
    public final static double MIN_PRICE = 0.19;
    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Book book = (Book) target;

        if (book.getPrice() < MIN_PRICE) {
            errors.rejectValue("price", "price.invalid", "Price must be greater than $" + MIN_PRICE);
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "title.empty", "Title cannot be empty");
    }
}
