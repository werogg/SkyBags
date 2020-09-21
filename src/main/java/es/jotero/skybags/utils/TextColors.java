package es.jotero.skybags.utils;

public class TextColors {
    public static final int NORMAL = 0;
    public static final int BRIGHT = 1;

    public static String get(Colors fc, String text) {
        return "\u001b[0;" + fc.value + "m" + text + "\u001b[m";
    }

    public static String get(Colors fc, Integer colormod, String text) {
        return "\u001b[" + colormod + ";" + fc.value + "m" + text + "\u001b[m";
    }

    public enum Colors {
        BLACK(30),
        RED(31),
        GREEN(32),
        YELLOW(33),
        BLUE(34),
        MAGENTA(35),
        CYAN(36),
        WHITE(37),;

        private int value;

        Colors(int i) {
            this.value = i;
        }
    }

    public enum BGColors {
        BLACK(40),
        RED(41),
        GREEN(42),
        YELLOW(43),
        BLUE(44),
        MAGENTA(45),
        CYAN(46),
        WHITE(47),;

        private int value;

        BGColors(int i) {
            this.value = i;
        }
    }
}
