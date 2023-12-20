package az.ubank.msaccount.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum CurrencyType {
    AZN, USD;

    @JsonValue
    public String toLower() {
        return this.toString().toLowerCase(Locale.ENGLISH);
    }
}
