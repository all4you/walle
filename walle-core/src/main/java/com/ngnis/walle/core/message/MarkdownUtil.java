package com.ngnis.walle.core.message;

import java.util.List;

/**
 * @author houyi.wh
 * @since 2018-11-13
 */
public class MarkdownUtil {

    public static String getBoldText(String text) {
        return "**" + text + "**";
    }

    public static String getItalicText(String text) {
        return "*" + text + "*";
    }

    public static String getLinkText(String text, String href) {
        return "[" + text + "](" + href + ")";
    }

    public static String getImageText(String imageUrl) {
        return "![image](" + imageUrl + ")";
    }

    public static String getHeaderText(int headerType, String text) {
        int minHeader = 1, maxHeader = 6;
        if (headerType < minHeader || headerType > maxHeader) {
            throw new IllegalArgumentException("headerType should be in ["+minHeader+", "+maxHeader+"]");
        }

        StringBuilder numbers = new StringBuilder();
        for (int i = 0; i < headerType; i++) {
            numbers.append("#");
        }
        return numbers + " " + text;
    }

    public static String getReferenceText(String text) {
        return "> " + text;
    }

    public static String getOrderListText(List<String> orderItems) {
        if (orderItems.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= orderItems.size() - 1; i++) {
            sb.append(String.valueOf(i)).append(". ").append(orderItems.get(i - 1)).append("\n");
        }
        sb.append(String.valueOf(orderItems.size())).append(". ").append(orderItems.get(orderItems.size() - 1));
        return sb.toString();
    }

    public static String getUnOrderListText(List<String> unOrderItems) {
        if (unOrderItems.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < unOrderItems.size() - 1; i++) {
            sb.append("- ").append(unOrderItems.get(i)).append("\n");
        }
        sb.append("- ").append(unOrderItems.get(unOrderItems.size() - 1));
        return sb.toString();
    }

}
