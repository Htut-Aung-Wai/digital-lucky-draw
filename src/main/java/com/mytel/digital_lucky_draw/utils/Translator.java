package com.mytel.digital_lucky_draw.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.ResourceBundle;


@Component
public class Translator {
    private static Logger log = LoggerFactory.getLogger(Translator.class);

    private static ResourceBundleMessageSource messageSource;


    @Autowired
    public Translator(ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public static String toLocale(String keyword) {
        try {
            Locale locale = LocaleContextHolder.getLocale();
            return messageSource.getMessage(keyword, null, locale);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return keyword;
        }
    }

    public static String toLocale(String key, String language) {
        String locate = "my";
        if (("en".equalsIgnoreCase(language) || "my".equalsIgnoreCase(language))) {
            locate = language;
        }
        ResourceBundle rb = ResourceBundle.getBundle("messages_" + locate.toLowerCase());
        try {
            if (rb.containsKey(key)) {
                return rb.getString(key);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return key;
    }
}