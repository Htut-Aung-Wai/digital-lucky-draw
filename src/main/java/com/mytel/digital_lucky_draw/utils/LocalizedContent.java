package com.mytel.digital_lucky_draw.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocalizedContent {
    protected String en;
    protected String my;

    public void setValue(String value) {
        Locale locale = LocaleContextHolder.getLocale();
        setValue(value, locale);
    }

    public void setValue(String value, Locale locale) {
        String languageTag = locale.toLanguageTag();
        if (languageTag.equalsIgnoreCase("my") || languageTag.equalsIgnoreCase("my_mm"))
            my = value;
        else
            en = value;
    }

    public String getValue(Locale locale) {
        String result = en;
        String languageTag = locale.toLanguageTag();
        if (languageTag.equalsIgnoreCase("my") || languageTag.equalsIgnoreCase("my_mm")) {
            if (my != null && my.trim().length() > 0)
                result = my;
        }
        return result;
    }

    @Override
    public String toString() {
        Locale locale = LocaleContextHolder.getLocale();
        return getValue(locale);
    }

    public static LocalizedContent fromString(String value) {
        LocalizedContent localizedContent = new LocalizedContent();
        localizedContent.setValue(value);
        return localizedContent;
    }
}
