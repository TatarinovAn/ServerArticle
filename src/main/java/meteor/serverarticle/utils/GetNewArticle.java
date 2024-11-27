package meteor.serverarticle.utils;

public class GetNewArticle {
    final static char[] LETTERS = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'F', 'G', 'H', 'J', 'K', 'L', 'M',
            'N', 'P', 'R', 'S', 'T', 'V', 'W', 'X', 'Y', 'Z'};

    public static String getNewArticle(String oldArticle, Long group) {
// если артикул создается в первый раз
        if (oldArticle == "") {
            return "KK" + group + "P";
        }
//сплит строки
        String[] end = oldArticle.split(group + "");
        // letters - окончание артикула
        String letters = end[1];

//последняя буква окончания
        char letNextA = 'A';
        // вторая буква окончания если есть
        char letNextB = 'A';
        // третья буква окончания если есть
        char letNextC = 'A';

        for (int i = 0; i < LETTERS.length; i++) {

// если окончания артикула состоит из одной буквы
            if (letters.length() == 1) {
                // последняя буква не Z
                if (letters.charAt(0) == LETTERS[i] && letters.charAt(0) != 'Z') {
                    letNextA = LETTERS[i + 1];
                    return "KK" + group + letNextA;
                    // если последняя буква Z
                } else if (letters.charAt(0) == LETTERS[i] && letters.charAt(0) == 'Z') {
                    return "KK" + group + "AP";
                }

// если окончания артикула состоит из 2 букв
            } else if (letters.length() == 2) {
                // последняя буква не Z
                if (letters.charAt(1) == LETTERS[i] && letters.charAt(1) != 'Z') {
                    letNextA = LETTERS[i + 1];
                    letters = letters.charAt(0) + "" + letNextA;
                    return "KK" + group + letters;
                    // последняя буква Z, вторая не Z
                } else if (letters.charAt(0) == LETTERS[i] && letters.charAt(1) == 'Z' && letters.charAt(0) != 'Z') {
                    letNextB = LETTERS[i + 1];
                    letters = letNextB + "" + 'P';
                    return "KK" + group + letters;
                    // все 2 буквы Z
                } else if (letters.charAt(1) == 'Z' && letters.charAt(0) == 'Z') {
                    return "KK" + group + "AAA";
                }


                // если окончания артикула состоит из 3 букв
            } else if (letters.length() == 3) {
                    // последняя буква не Z
                if (letters.charAt(2) == LETTERS[i] && letters.charAt(2) != 'Z') {
                    letNextA = LETTERS[i + 1];
                    letters = letters.charAt(0) + "" + letters.charAt(1) + letNextA;
                    return "KK" + group + letters;
                    // последняя буква Z, вторая не Z
                } else if (letters.charAt(1) == LETTERS[i] && letters.charAt(1) != 'Z' && letters.charAt(2) == 'Z') {
                    letNextB = LETTERS[i + 1];
                    letters = letters.charAt(0) + "" + letNextB + "A";
                    return "KK" + group + letters;
                    // последняя буква Z и вторая Z
                } else if (letters.charAt(0) == LETTERS[i] && letters.charAt(1) == 'Z' && letters.charAt(2) == 'Z') {
                    letNextC = LETTERS[i + 1];
                    letters = letNextC + "AA";
                    return "KK" + group + letters;
                }
                // третья буква Z не достигайма в условиях работы этого сервиса
            }
        }

        return "";
    }
}

