import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

/**
 * Created by NamaK on 29.12.2016.
 */
public class Field extends JPanel {
    private int linesCount;
    private int cellSize;
    private static final int FIELD_SIZE = 500;
    private static int[][] map;
    private final static int PLAYER1_SYMBOL = 1;
    private final static int PLAYER2_SYMBOL = 2;
    private final static int EMPTY = 0;
    private static Random rnd = new Random();
    private int symToWin;
    private boolean gameOver;
    private String gameOverMsg;
    private boolean aiOn;
    private boolean player1;
    private int playerX = -1;
    private int playerY = -1;

    public Field(int linesCount) {
        startGame(linesCount, linesCount, true);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (aiOn) {
                    playerTurn(PLAYER1_SYMBOL, e);
                    aiTurn();
                } else {
                    if (player1) {
                        playerTurn(PLAYER1_SYMBOL, e);
                        player1 = !player1;
                    } else {
                        playerTurn(PLAYER2_SYMBOL, e);
                        player1 = !player1;
                    }
                }
            }
        });
    }

    public void playerTurn(int playerSymbol, MouseEvent e) {
        playerX = e.getX() / cellSize;
        playerY = e.getY() / cellSize;
        if (!gameOver) {
            if (setSymbol(playerX, playerY, playerSymbol)) {
                repaint();
                isMapFull();
                checkWin(playerSymbol);
            }
        }
    }

    public void startGame(int lines, int symToWin, boolean aiOn) {
        linesCount = lines;
        player1 = true;
        cellSize = FIELD_SIZE / linesCount;
        this.symToWin = symToWin;
        this.aiOn = aiOn;
        map = new int[linesCount][linesCount];
        gameOver = false;
        gameOverMsg = "";
        repaint();
    }

    public void startGame() {
        player1 = true;
        cellSize = FIELD_SIZE / linesCount;
        map = new int[linesCount][linesCount];
        gameOver = false;
        gameOverMsg = "";
        repaint();
    }

    public boolean isMapFull() {
        for (int i = 0; i < linesCount; i++) {
            for (int j = 0; j < linesCount; j++) {
                if (map[i][j] == EMPTY) return false;
            }

        }
        gameOver = true;
        gameOverMsg = "Ничья";
        return true;
    }

    public void aiTurn() {
        if (gameOver) return;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (setSymbol(i, j, PLAYER2_SYMBOL)) {
                    if (checkWin(PLAYER2_SYMBOL)) {
                        repaint();
                        return;
                    }
                    map[i][j] = EMPTY;
                }
            }
        }
        boolean b = false;
        int vectorPlus = 0;
        int vectorMinus = 0;
        int x = playerX;
        int y = playerY;
        switch (think(playerX, playerY)) {
            case 0: {
                do {
                    if (map[playerX + vectorPlus][playerY] == EMPTY) break;
                    vectorPlus++;
                } while ((playerX + vectorPlus) != map.length);
                do {
                    if (map[playerX - vectorMinus][playerY] == EMPTY) break;
                    vectorMinus++;
                } while ((playerX - vectorMinus) != -1);
                if (vectorPlus > vectorMinus && playerX - vectorMinus >= 0) {
                    b = setSymbol(playerX - vectorMinus, playerY, PLAYER2_SYMBOL);
                } else {
                    if (vectorPlus <= vectorMinus && playerX + vectorPlus < map.length) {
                        b = setSymbol(playerX + vectorPlus, playerY, PLAYER2_SYMBOL);
                    } else {
                        for (int i = 0; i < map.length; i++) {
                            if (setSymbol(i, playerY, PLAYER2_SYMBOL)) {
                                b = true;
                                break;
                            }
                        }
                    }
                }
                break;
            }
            case 1: {
                do {
                    if (map[playerX][playerY + vectorPlus] == EMPTY) break;
                    vectorPlus++;
                } while ((playerY + vectorPlus) != map.length);
                do {
                    if (map[playerX][playerY - vectorMinus] == EMPTY) break;
                    vectorMinus++;
                } while ((playerY - vectorMinus) != -1);
                if (vectorPlus > vectorMinus && playerY - vectorMinus >= 0) {
                    b = setSymbol(playerX, playerY - vectorMinus, PLAYER2_SYMBOL);
                } else {
                    if (vectorPlus <= vectorMinus && playerY + vectorPlus < map.length) {
                        b = setSymbol(playerX, playerY + vectorPlus, PLAYER2_SYMBOL);
                    } else {
                        for (int i = 0; i < map.length; i++) {
                            if (setSymbol(playerX, i, PLAYER2_SYMBOL)) {
                                b = true;
                                break;
                            }
                        }
                    }
                }
                break;
            }
            case 2: {
                do {
                    if (map[playerX + vectorPlus][playerY + vectorPlus] == EMPTY) break;
                    vectorPlus++;
                } while ((playerX + vectorPlus) != map.length && (playerY + vectorPlus) != map.length);
                do {
                    if (map[playerX - vectorMinus][playerY - vectorMinus] == EMPTY) break;
                    vectorMinus++;
                } while ((playerX - vectorMinus) != -1 && (playerY - vectorMinus) != -1);
                if (vectorPlus > vectorMinus && playerX - vectorMinus >= 0 && playerY - vectorMinus >= 0) {
                    b = setSymbol(playerX - vectorMinus, playerY - vectorMinus, PLAYER2_SYMBOL);
                } else {
                    if (vectorPlus <= vectorMinus && playerX + vectorPlus < map.length && playerY + vectorPlus < map.length) {
                        b = setSymbol(playerX + vectorPlus, playerY + vectorPlus, PLAYER2_SYMBOL);
                    } else {
                        for (int i = 0; i < map.length; i++) {
                            x = playerX - i;
                            y = playerY - i;
                            if (x < 0 || y < 0) break;
                            if (map[x][y]==EMPTY){
                                x--;
                                y--;
                                break;
                            }
                        }
                        do {
                            x++;
                            y++;
                            if (x == map.length || y == map.length) {
                                b = false;
                                break;
                            }
                            if (setSymbol(x, y, PLAYER2_SYMBOL)) {
                                b = true;
                                break;
                            }
                        } while (true);
                    }
                }
                break;
            }
            case 3: {
                do {
                    if (map[playerX + vectorPlus][playerY - vectorPlus] == EMPTY) break;
                    vectorPlus++;
                } while ((playerX + vectorPlus) != map.length && (playerY - vectorPlus) != -1);
                do {
                    if (map[playerX - vectorMinus][playerY + vectorMinus] == EMPTY) break;
                    vectorMinus++;
                } while ((playerX - vectorMinus) != -1 && (playerY + vectorMinus) != map.length);
                if (vectorPlus > vectorMinus && playerX - vectorMinus >= 0 && playerY + vectorMinus < map.length) {
                    b = setSymbol(playerX - vectorMinus, playerY + vectorMinus, PLAYER2_SYMBOL);
                } else {
                    if (vectorPlus <= vectorMinus && playerX + vectorPlus < map.length && playerY - vectorPlus >= 0) {
                        b = setSymbol(playerX + vectorPlus, playerY - vectorPlus, PLAYER2_SYMBOL);
                    } else {
                        for (int i = 0; i < map.length; i++) {
                            x = playerX - i;
                            y = playerY + i;
                            if (x < 0 || y == map.length) break;
                            if (map[x][y]==EMPTY){
                                x--;
                                y++;
                                break;
                            }
                        }
                        do {
                            x++;
                            y--;
                            if (x == map.length || y == -1) {
                                b = false;
                                break;
                            }
                            if (setSymbol(x, y, PLAYER2_SYMBOL)) {
                                b = true;
                                break;
                            }
                        } while (true);
                    }
                }
                break;
            }
            default: {
                do {
                } while (!setSymbol(rnd.nextInt(linesCount), rnd.nextInt(linesCount), PLAYER2_SYMBOL));
                break;
            }
        }
        if (!b) {
            do {
            } while (!setSymbol(rnd.nextInt(linesCount), rnd.nextInt(linesCount), PLAYER2_SYMBOL));
        }
        repaint();
        isMapFull();
        checkWin(PLAYER2_SYMBOL);
    }

    public int think(int x, int y) {
        int[] lines = new int[4];
        int[] linesLenght = new int[4];
        int turnLine = 0;
        int max = 0;
        for (int i = 0; i < map.length; i++) {
            if (map[x][i] == PLAYER1_SYMBOL) {
                lines[1]++;
            }
            linesLenght[1] = map.length;
            if (map[i][y] == PLAYER1_SYMBOL) {
                lines[0]++;
            }
            linesLenght[0] = map.length;
            if ((x + i) < map.length && (y + i) < map.length) {
                if (map[x + i][y + i] == PLAYER1_SYMBOL) {
                    lines[2]++;
                }
                linesLenght[2]++;
            }
            if ((x - i) >= 0 && (y - i) >= 0) {
                if (map[x - i][y - i] == PLAYER1_SYMBOL) {
                    if (i != 0) {
                        lines[2]++;
                    }
                }
                linesLenght[2]++;
            }
            if ((x + i) < map.length && (y - i) >= 0) {
                if (map[x + i][y - i] == PLAYER1_SYMBOL) {
                    lines[3]++;
                }
                linesLenght[3]++;
            }
            if ((x - i) >= 0 && (y + i) < map.length) {
                if (map[x - i][y + i] == PLAYER1_SYMBOL) {
                    if (i != 0) {
                        lines[3]++;
                    }
                }
                linesLenght[3]++;
            }
        }
        linesLenght[2]--;
        linesLenght[3]--;
        for (int i = 0; i < 4; i++) {
            if (lines[i] >= max && lines[i] != linesLenght[i]) {
                max = lines[i];
                turnLine = i;
            }
        }
        return turnLine;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 0; i <= linesCount; i++) {
            g.drawLine(0, cellSize * i, FIELD_SIZE, cellSize * i);
            g.drawLine(cellSize * i, 0, cellSize * i, FIELD_SIZE);
        }
        BasicStroke pen = new BasicStroke(5);
        g2.setStroke(pen);
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j] != EMPTY) {
                    if (map[i][j] == PLAYER1_SYMBOL) {
                        g2.drawLine(i * cellSize + 5, j * cellSize + 5, i * cellSize + cellSize - 5, j * cellSize + cellSize - 5);
                        g2.drawLine(i * cellSize + 5, j * cellSize + cellSize - 5, i * cellSize + cellSize - 5, j * cellSize + 5);
                    }
                    if (map[i][j] == PLAYER2_SYMBOL) {
                        g2.drawOval(i * cellSize + 5, j * cellSize + 5, cellSize - 10, cellSize - 10);
                    }
                }
            }

        }
        if (gameOver) {
            g.setColor(Color.lightGray);
            g.fillRect(0, 225, FIELD_SIZE, 40);
            Font f = new Font("Arial", Font.BOLD, 30);
            g.setFont(f);
            FontMetrics metr = getFontMetrics(f);
            g.setColor(Color.BLACK);
            g.drawString(gameOverMsg, (FIELD_SIZE - metr.stringWidth(gameOverMsg)) / 2 + 3, 258);
            g.setColor(Color.YELLOW);
            g.drawString(gameOverMsg, (FIELD_SIZE - metr.stringWidth(gameOverMsg)) / 2, 255);
        }
    }

    public static boolean setSymbol(int x, int y, int symbol) {
        if (map[x][y] == EMPTY) {
            map[x][y] = symbol;
            return true;
        }
        return false;
    }

    public boolean checkLine(int xo, int yo, int vx, int vy, int l, int ox) {
        if (xo + l * vx > linesCount || yo + l * vy > linesCount || xo + l * vx < -1 || yo + l * vy < -1) {
            return false;
        }
        for (int i = 0; i < l; i++) {
            if (map[xo + i * vx][yo + i * vy] != ox) {
                return false;
            }
        }
        return true;
    }

    public boolean checkWin(int ox) {
        for (int i = 0; i < linesCount; i++) {
            for (int j = 0; j < linesCount; j++) {
                if (checkLine(i, j, 1, 0, symToWin, ox) || checkLine(i, j, 0, 1, symToWin, ox) ||
                        checkLine(i, j, 1, 1, symToWin, ox) || checkLine(i, j, 1, -1, symToWin, ox)) {
                    gameOver = true;
                    if (ox == PLAYER1_SYMBOL) gameOverMsg = "Игрок 1 выиграл";
                    if (ox == PLAYER2_SYMBOL) gameOverMsg = "Игрок 2 выиграл";
                    return true;
                }
            }
        }
        return false;
    }
}
