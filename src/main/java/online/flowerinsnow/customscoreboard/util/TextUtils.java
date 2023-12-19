package online.flowerinsnow.customscoreboard.util;

public abstract class TextUtils {
    private TextUtils() {
    }

    public static String parseColour(String text) {
        return text.replace("&", "§")
                .replace("§§", "&");
    }
}
