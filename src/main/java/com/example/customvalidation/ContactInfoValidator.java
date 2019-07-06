package com.example.customvalidation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

@Configuration
public class ContactInfoValidator implements ConstraintValidator<ContactInfo, String> {

    private static final Logger LOG = LogManager.getLogger(ContactInfoValidator.class);

    @Value("${contactInfoType}")
    private String expressionType;

    private String pattern;

    @Autowired
    private ContactInfoExpressionRepository contactInfoExpressionRepository;

    @Override
    public void initialize(ContactInfo contactInfo) {
        if (StringUtils.isEmptyOrWhitespace(expressionType)) {
            LOG.error("Contact info type missing!");
        } else {
            pattern = contactInfoExpressionRepository.findById(expressionType)
                    .map(ContactInfoExpression::getPattern).get();
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!StringUtils.isEmptyOrWhitespace(pattern)) {
            return Pattern.matches(pattern, value);
        }
        LOG.error("Contact info pattern missing!");
        return false;
    }
}