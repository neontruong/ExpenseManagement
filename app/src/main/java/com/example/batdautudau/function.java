package com.example.batdautudau;

import java.text.NumberFormat;
import java.util.Locale;

public class function {


    public static String formatCurrency(int amount) {
        if (amount >= 1_000_000) {
            // Định dạng cho đơn vị triệu
            return (amount / 1_000_000) + "tr" + ((amount % 1_000_000)/1_000) + "k";

        } else if (amount >= 1_000) {
            // Định dạng cho đơn vị nghìn
            return (amount / 1_000) + "k";
        } else {
            // Định dạng cho đơn vị đồng
            return amount + "đ";
        }
    }

    public static String formatCurrencyVND(int amount) {
        // Use the Vietnamese locale
        Locale locale = new Locale("vi", "VN");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
        return currencyFormat.format(amount);
    }
}
