package ua.askerov;

/*
 * Автор: Аскеров А. Ф.
 * Група: 421Б
 *
 * Дата_: 22.02.2024
 * * */

/*
* Створити гру ‘Морський бій’ для двох гравців за класичними правилами, а саме:
кожен з гравців під час свого ходу може виконати постріл за певними координатами (х, у);

* якщо під час пострілу гравець влучив, то він може виконати додатковий постріл, у
іншому випадку хід переходить другому гравцю; перемагає той гравець, котрий потопить
усі ворожі кораблі першим.

* * Ігрове поле має розмір 10х10, кількість кораблів:
* чотири 1-палубних;
* три 2-палубних;
* два 3-палубних;
* один 4-палубний.

* Розміщення кожного окремого корабля має бути або горизонтальним,
або вертикальним. Розміщення кораблів по діагоналі заборонено. Приклад ігрового поля
зображено на рис. 3.6.
Основні елементи, які мають бути в програмі: розстановка кораблів з
підтвердженням вибору, захист від підглядування розстановки, перевірка введення
координат при пострілах, виведення для кожного гравця ігрового поля з попередніми
пострілами, виведення результатів гри з ігровими полями обох гравців, розташованими
поруч (горизонтально на одному рівні) один з одним для найкращого візуального
сприйняття результатів.

* Основні елементи, які мають бути в програмі: розстановка кораблів з
підтвердженням вибору, захист від підглядування розстановки, перевірка введення
координат при пострілах, виведення для кожного гравця ігрового поля з попередніми
пострілами, виведення результатів гри з ігровими полями обох гравців, розташованими
поруч (горизонтально на одному рівні) один з одним для найкращого візуального
сприйняття результатів.
* */

import java.io.*;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Main {

    static final int BATTLEFIELD_SIZE = 10;
    static char[][] battlefield = new char[BATTLEFIELD_SIZE][BATTLEFIELD_SIZE];
    static char water = '-',
            hit = 'x',
            miss = 'o',
            ship = 's';
    static Pattern pattern = Pattern.compile("[A-J]\\d");
    static StringBuilder stringBuilder;
    static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {
        prepareBattlefield("Player_1");
    }

    /* Ігрове поле має розмір 10х10, кількість кораблів:
     * чотири 1-палубних;
     * три 2-палубних;
     * два 3-палубних;
     * один 4-палубний.
     * */
    public static void prepareBattlefield(String playerName) {
        // заповнити поле водою
        for (char[] row :
                battlefield) {
            Arrays.fill(row, water);
        }
/*
        // заповнити поле кораблями
        // один 4-палубний
        showBattlefield(battlefield, playerName);
        placeBattleship(battlefield, BattleshipTypes.a4x1);

        // два 3-палубних
        for (int i = 0; i < 2; i++) {
            showBattlefield(battlefield, playerName);
            placeBattleship(battlefield, BattleshipTypes.a3x1);
        }

        // три 2-палубних
        for (int i = 0; i < 3; i++) {
            showBattlefield(battlefield, playerName);
            placeBattleship(battlefield, BattleshipTypes.a2x1);
        }*/

        // чотири 1-палубних
        for (int i = 0; i < 4; i++) {
            showBattlefield(battlefield, playerName);
//            placeBattleship(battlefield, BattleshipTypes.a1x1);
        }
    }

    private static void showBattlefield(char[][] battlefield, String playerName) {
        stringBuilder = new StringBuilder();
        stringBuilder.append("Battlefield of \"").append(playerName).append("\":\n\n").append("\t");

        int asciiCharacters = 65;
        for (int i = 0; i < battlefield.length; i++) {
            stringBuilder.append("| ").append(String.format("%c ", asciiCharacters + i));
        }
        stringBuilder.append("|");

        stringBuilder.append("\n");
        for (int i = 0; i < battlefield.length; i++) {
            stringBuilder.append("     ---+---+---+---+---+---+---+---+---+---+---+").append("\n");
            stringBuilder.append(String.format("     %2d ", i));
            for (int j = 0; j < battlefield[i].length; j++) {
                stringBuilder.append("| ").append(battlefield[i][j]).append(" ");
            }
            stringBuilder.append("|\n");
        }
        stringBuilder.append("     ---+---+---+---+---+---+---+---+---+---+---+").append("\n");

        // очистити консоль (якщо у CMD)
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // вивести поле на консоль
        System.out.println(stringBuilder.toString());
    }

    private static boolean isPlacementLegal(int[][] battleshipLocation, BattleshipTypes battleshipType) {
        int x1 = battleshipLocation[0][0];
        int y1 = battleshipLocation[0][1];
        int x2 = battleshipLocation[1][0];
        int y2 = battleshipLocation[1][1];

        if (x1 != x2 && y1 != y2) {
            System.out.println("\tShip can't be placed diagonally\n");
            return false;
        }

        int length = (int) Math.sqrt(Math.pow(Math.max(x1, x2) - Math.min(x1, x2), 2) + Math.pow(Math.max(y1, y2) - Math.min(y1, y2), 2)) + 1;
        if (length > battleshipType.length) {
            System.out.printf("\tShip \"" + battleshipType + "\" is %d tiles shorter\n", length - battleshipType.length);
            return false;
        } else if (length < battleshipType.length) {
            System.out.printf("\tShip \"" + battleshipType + "\" is %d tiles longer\n", battleshipType.length - length);
            return false;
        }
        return true;
    }

//    private static void placeBattleship(char[][] battlefield, BattleshipTypes battleshipType) {
//        if (battleshipType == BattleshipTypes.a1x1) {
//            int[] battleshipLocation;
//            do {
//                battleshipLocation = getLocation(battleshipType);
//            } while (!isPlacementLegal(battleshipLocation, battleshipType));
//        } else {
//            int[][] battleshipLocation;
//            do {
//                battleshipLocation = getLocation(battleshipType);
//            } while (!isPlacementLegal(battleshipLocation, battleshipType));
//            battlefield[battleshipLocation[0][0]][battleshipLocation[0][1]] = ship;
//            battlefield[battleshipLocation[1][0]][battleshipLocation[1][1]] = ship;
//        }
//    }

    private static int[][] getLocation(BattleshipTypes battleshipType) {
        int[][] location = new int[2][2];
        int[] coordinateA;
        int[] coordinateB;

        try {
            coordinateA = getCoordinates("F9", 1);
            // TODO: interactive updating of a map (place an '*' cursor to highlight the area)
            coordinateB = getCoordinates("C9", 2);
            location[0][0] = coordinateA[0];
            location[0][1] = coordinateA[1];
            location[1][0] = coordinateB[0];
            location[1][1] = coordinateB[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return location;
    }

    private static int[] getCoordinates(String example, int stageNumber) throws IOException {
        int[] coordinates = new int[2];
        String input;
        char[] inputParse;
        do {
            // просимо користувача розмістити корабель
            System.out.print("Specify the coordinates [" + example + "] (" + stageNumber + "/2): ");
            input = bufferedReader.readLine();
            while (!pattern.matcher(input).matches()) {
                System.out.print("Invalid input. Try again: ");
                input = bufferedReader.readLine();
            }

            // парсимо значення
            inputParse = input.toCharArray();
            coordinates[0] = inputParse[0] - 65;
            coordinates[1] = inputParse[1] - 48;
        } while (!areCoordinatesLegal(battlefield, coordinates));
        return coordinates;
    }

    private static boolean areCoordinatesLegal(char[][] battlefield, int[] coordinates) {
        if (battlefield[coordinates[0]][coordinates[1]] == ship) {
            System.out.println("\tChoose a free space");
            return false;
        }
        return true;
    }

    enum BattleshipTypes {
        a4x1(4),
        a3x1(3),
        a2x1(2),
        a1x1(1);

        final int length;

        BattleshipTypes(int length) {
            this.length = length;
        }
    }
}