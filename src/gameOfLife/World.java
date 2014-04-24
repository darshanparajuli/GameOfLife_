package gameOfLife;

public class World {

    private static int[][] map;
    public static int cellWidth = 10;
    public static int cellHeight = 10;
    public static long numOfGenerations = 0;
    public static String[] patterns = {"Glider", "Glider gun", "Spaceship", "Bee Shuttle", "Pulsar", "Full"};
    public static boolean canAutoResize = false;

    public World() {
        map = new int[DrawPanel.PWIDTH / cellWidth][DrawPanel.PHEIGHT / cellHeight];
        reset(map);
    }

    public static void reset(int[][] arr) {
        for (int x = 0; x < arr.length; x++) {
            for (int y = 0; y < arr[0].length; y++) {
                arr[x][y] = 0;
            }
        }
    }

    public int[][] getMap() {
        return map;
    }

    public void setMap(int x, int y, int i) {
        map[x][y] = i;
    }

    public void update() {
        if (canAutoResize) {
            autoResize(3);
        }
        int temp[][] = new int[map.length][map[0].length];
        reset(temp);
        for (int x = DrawPanel.OutOfBoundsFactor; x < map.length - DrawPanel.OutOfBoundsFactor; x++) {
            for (int y = DrawPanel.OutOfBoundsFactor; y < map[0].length - DrawPanel.OutOfBoundsFactor; y++) {
                boolean error = false;
                int numOfAliveOnes = 0;
                try {
                    numOfAliveOnes = map[x - 1][y - 1] + map[x][y - 1] + map[x + 1][y - 1] + map[x - 1][y] + map[x + 1][y] + map[x - 1][y + 1] + map[x][y + 1] + map[x + 1][y + 1];
                } catch (IndexOutOfBoundsException ex) {
                    error = true;
                }
                if (!error) {
                    if (map[x][y] == 1 && numOfAliveOnes < 2) {
                        temp[x][y] = 0;
                    } else if (map[x][y] == 1 && (numOfAliveOnes == 2 || numOfAliveOnes == 3)) {
                        temp[x][y] = 1;
                    } else if (map[x][y] == 1 && numOfAliveOnes > 3) {
                        temp[x][y] = 0;
                    } else if (map[x][y] == 0 && numOfAliveOnes == 3) {
                        temp[x][y] = 1;
                    }

                }
            }
        }
        map = temp;
        numOfGenerations += 1;
    }

    public void clearCellsOutOfBounds() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < DrawPanel.OutOfBoundsFactor; j++) {
                map[i][j] = -1;
                map[i][map[0].length - 1 - j] = 0;
            }
        }
        for (int i = 0; i < map[0].length; i++) {
            for (int j = 0; j < DrawPanel.OutOfBoundsFactor; j++) {
                map[j][i] = -1;
                map[map.length - 1 - j][i] = 0;
            }
        }
    }

    public static int getCellHeight() {
        return cellHeight;
    }

    public static void setCellHeight(int cellHeight) {
        World.cellHeight = cellHeight;
    }

    public static int getCellWidth() {
        return cellWidth;
    }

    public static void setCellWidth(int cellWidth) {
        World.cellWidth = cellWidth;
    }

    public static void setNumRowCol(int r, int c) {
        map = new int[r][c];
        reset(map);
    }

    public static void setNumOfRow(int r) {
        map = new int[map.length][r];
        reset(map);
    }

    public static void setNumOfCol(int c) {
        map = new int[c][map[0].length];
        reset(map);
    }

    public static void insertPattern(String s, int x, int y) {
        switch (s) {
            case "Glider": {
                int tempx = x;
                int tempy = y;
                for (int i = 0; i < PatternBank.glider.length; i++) {
                    for (int j = 0; j < PatternBank.glider[0].length; j++) {
                        map[tempx + j][tempy + i] = PatternBank.glider[i][j];
                    }
                }
            }
            break;
            case "Glider gun": {
                int tempx = x;
                int tempy = y;
                for (int i = 0; i < PatternBank.gliderGun.length; i++) {
                    for (int j = 0; j < PatternBank.gliderGun[0].length; j++) {
                        map[tempx + j][tempy + i] = PatternBank.gliderGun[i][j];
                    }
                }
            }
            break;
            case "Spaceship": {
                int tempx = x;
                int tempy = y;//map[0].length / 2 - 1;
                for (int i = 0; i < PatternBank.spaceShip.length; i++) {
                    for (int j = 0; j < PatternBank.spaceShip[0].length; j++) {
                        map[tempx + j][tempy + i] = PatternBank.spaceShip[i][j];
                    }
                }
            }
            break;
            case "Bee Shuttle": {
                int tempx = x;//map.length / 2 - 1;
                int tempy = y;//map[0].length / 2 - 1;
                for (int i = 0; i < PatternBank.beeShuttle.length; i++) {
                    for (int j = 0; j < PatternBank.beeShuttle[0].length; j++) {
                        map[tempx + j][tempy + i] = PatternBank.beeShuttle[i][j];
                    }
                }
            }
            break;
            case "Pulsar": {
                int tempx = x;//map.length / 2 - 1;
                int tempy = y;//map[0].length / 2 - 1;
                for (int i = 0; i < PatternBank.pulsar.length; i++) {
                    for (int j = 0; j < PatternBank.pulsar[0].length; j++) {
                        map[tempx + j][tempy + i] = PatternBank.pulsar[i][j];
                    }
                }
            }
            break;
            case "Full": {
                int tempx = DrawPanel.OutOfBoundsFactor + x;
                int tempy = DrawPanel.OutOfBoundsFactor + y;
                for (int i = tempx; i < map.length - DrawPanel.OutOfBoundsFactor; i++) {
                    for (int j = tempy; j < map[0].length - DrawPanel.OutOfBoundsFactor; j++) {
                        map[i][j] = 1;
                    }
                }
            }
            break;
        }
    }

    public static void resetWidthHeight() {
        World.cellWidth = 10;
        World.cellHeight = 10;
    }

    public void autoResize(int r) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 1; j < 4; j++) {
                if (map[i][map[0].length - j] == 1) {
                    int temp[][] = map;
                    map = new int[map.length][map[0].length + r];
                    for (int k = 0; k < temp.length; k++) {
                        for (int l = 0; l < temp[0].length; l++) {
                            map[k][l] = temp[k][l];
                        }
                    }
                    break;
                }
            }
        }
        for (int i = 0; i < map[0].length; i++) {
            for (int j = 1; j < 4; j++) {
                if (map[map.length - j][i] == 1) {
                    int temp[][] = map;
                    map = new int[map.length + r][map[0].length];
                    for (int k = 0; k < temp.length; k++) {
                        for (int l = 0; l < temp[0].length; l++) {
                            map[k][l] = temp[k][l];
                        }
                    }
                    break;
                }
            }
        }
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < 3; j++) {
                if (map[i][j] == 1) {
                    int temp[][] = map;
                    map = new int[map.length][map[0].length + (r)];
                    for (int k = 0; k < temp.length; k++) {
                        for (int l = 0; l < temp[0].length; l++) {
                            map[k][l + r] = temp[k][l];
                        }
                    }
                    DrawPanel.offsetY -= r * cellHeight;
                    break;
                }
            }
        }

        for (int i = 0; i < map[0].length; i++) {
            for (int j = 0; j < 3; j++) {
                if (map[j][i] == 1) {
                    int temp[][] = map;
                    map = new int[map.length + (r)][map[0].length];
                    for (int k = 0; k < temp.length; k++) {
                        for (int l = 0; l < temp[0].length; l++) {
                            map[k + r][l] = temp[k][l];
                        }
                    }
                    DrawPanel.offsetX -= r * cellWidth;
                    break;
                }
            }
        }
    }
}
