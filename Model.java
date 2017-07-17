package com.javarush.task.task35.task3513;


import java.util.*;

public class Model {
    private static final int FIELD_WIDTH = 4;
    private Tile[][] gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
    int score;
    int maxTile;
    private Stack<Tile[][]> previousStates = new Stack();
    private Stack<Integer> previousScores = new Stack();
    private boolean isSaveNeeded = true;

    public Model() {
        resetGameTiles();
    }

    private List<Tile> getEmptyTiles() {
        List<Tile> list = new ArrayList<>();
        for (int i=0;i<FIELD_WIDTH;i++)
            for (int j=0;j<FIELD_WIDTH;j++) if (gameTiles[i][j].value==0) list.add(gameTiles[i][j]);
        return list;
    }

    public void resetGameTiles() {
        for (int i=0;i<FIELD_WIDTH;i++)
            for (int j=0;j<FIELD_WIDTH;j++) gameTiles[i][j]=new Tile();
        score=0;
        maxTile=2;
        addTile();
        addTile();
    }

    private void addTile() {
        List<Tile> list = getEmptyTiles();
        if (list.isEmpty()) return;
        Tile t = list.get((int) (list.size()*Math.random()));
        t.value = Math.random() < 0.9 ? 2 : 4;
        if (t.value>maxTile) maxTile=t.value;
    }

    private boolean compressTiles(Tile[] tiles) {
        boolean isChanged = false;
        Tile[] tmp = new Tile[FIELD_WIDTH];
        int shift = 0;
        for (int i=0;i<FIELD_WIDTH;i++) {
            tmp[i] = new Tile();
            if (tiles[i].value > 0) {
                if (shift != i) isChanged = true;
                tmp[shift] = tiles[i];
                shift++;
            }
        }
        System.arraycopy(tmp,0,tiles,0,tmp.length);
        return isChanged;
    }
    private boolean mergeTiles(Tile[] tiles) {
        boolean isChanged = false;
        for (int i=0;i<FIELD_WIDTH-1;i++)
            if (tiles[i].value==tiles[i+1].value && tiles[i].value!=0) {
                tiles[i].value*=2;
                int t = tiles[i].value;
                score+=t;
                if (t>maxTile) maxTile=t;
                tiles[i+1].value=0;
                isChanged = true;
            }
        compressTiles(tiles);
        return isChanged;
    }

    public void left() {
        boolean isChanged=false;
        if (isSaveNeeded) saveState(gameTiles);
        for (Tile[] t:gameTiles)
            if (compressTiles(t) | mergeTiles(t)) isChanged=true;
        if (isChanged) addTile();
        isSaveNeeded=true;
    }

    private void rotate() {
        Tile[][] tmp = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i=0;i<FIELD_WIDTH;i++)
            for (int j=0;j<FIELD_WIDTH;j++) tmp[FIELD_WIDTH-j-1][i]=gameTiles[i][j];
        System.arraycopy(tmp,0,gameTiles,0,tmp.length);
    }

    public void up() {
        saveState(gameTiles);
        rotate();
        left();
        rotate();
        rotate();
        rotate();
    }

    public void down() {
        saveState(gameTiles);
        rotate();
        rotate();
        rotate();
        left();
        rotate();
    }

    public void right() {
        saveState(gameTiles);
        rotate();
        rotate();
        left();
        rotate();
        rotate();
    }

    public Tile[][] getGameTiles() {
        return gameTiles;
    }

    public boolean canMove() {
        if (!getEmptyTiles().isEmpty()) return true;
        for (int i = 0; i < FIELD_WIDTH; i++)
            for (int j = 1; j < FIELD_WIDTH; j++) {
                if(gameTiles[i][j].value == gameTiles[i][j-1].value) return true;
                if(gameTiles[j][i].value == gameTiles[j-1][i].value) return true;
            }
        return false;
    }

    public int getScore() {
        return score;
    }

    private void saveState(Tile[][] tiles) {
        Tile[][] tmp = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i=0;i<FIELD_WIDTH;i++)
            for (int j=0;j<FIELD_WIDTH;j++) tmp[i][j] = new Tile(tiles[i][j].value);
        previousStates.push(tmp);
        previousScores.push(score);
        isSaveNeeded=false;
    }

    public void rollback() {
        if (previousScores.isEmpty() || previousStates.isEmpty()) return;
        score = previousScores.pop();
        gameTiles = previousStates.pop();
    }

    public void randomMove() {
        switch (((int) (Math.random() * 100)) % 4) {
            case 0:left();break;
            case 1:right();break;
            case 2:up();break;
            case 3:down();break;
        }
    }
    private boolean hasBoardChanged() {
        Tile[][] tmp = previousStates.peek();
        int sumNow=0;
        int sumPrev=0;
        for (int i=0;i<FIELD_WIDTH;i++)
            for (int j=0;j<FIELD_WIDTH;j++){
                sumNow+=gameTiles[i][j].value;
                sumPrev+=tmp[i][j].value;
            }
        return sumNow!=sumPrev;
    }

    private MoveEfficiency getMoveEfficiency(Move move) {
        MoveEfficiency m;
        move.move();
        if (hasBoardChanged()) m = new MoveEfficiency(getEmptyTiles().size(),score,move);
        else m = new MoveEfficiency(-1,0,move);
        rollback();
        return m;
    }

    public void autoMove() {
        PriorityQueue<MoveEfficiency> queue = new PriorityQueue<>(4,Collections.reverseOrder());
        queue.add(getMoveEfficiency(this::left));
        queue.add(getMoveEfficiency(this::right));
        queue.add(getMoveEfficiency(this::down));
        queue.add(getMoveEfficiency(this::up));
        Move move = queue.peek().getMove();
        move.move();
    }
}

