package com.Sk.BookMyShow.Utils;

import java.util.Currency;
import java.util.Locale;

import com.Sk.BookMyShow.Exceptions.InvalidCurrencyException;

public final class CurrencyUtils {

    private CurrencyUtils() {
    }

    public static Currency getCurrency(String currencyCode) {

        if (currencyCode == null || currencyCode.isBlank()) {
            throw new InvalidCurrencyException("Currency is required");
        }

        try {
            return Currency.getInstance(
                currencyCode.trim().toUpperCase(Locale.ROOT)
            );
        } catch (IllegalArgumentException e) {
            throw new InvalidCurrencyException("Invalid currency code");
        }
    }

    public static String getCurrencyCode(String currencyCode) {
        return getCurrency(currencyCode).getCurrencyCode();
    }

    public static String getCurrencySymbol(String currencyCode) {
        return getCurrency(currencyCode).getSymbol();
    }
}
