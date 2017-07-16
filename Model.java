package com.javarush.task.task35.task3513;


import java.util.ArrayList;
import java.util.List;

public class Model {
    private static final int FIELD_WIDTH = 4;
    private Tile[][] gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
    int score;
    int maxTile;

    public Model() {
        resetGameTiles();
    }

    public List<Tile> getEmptyTiles() {
        List<Tile> list = new ArrayList<>();
        for (int i=0;i<FIELD_WIDTH;i++)
            for (int j=0;j<FIELD_WIDTH;j++) if (gameTiles[i][j].value==0) list.add(gameTiles[i][j]);
        return list;
    }

    public void resetGameTiles() {
        for (int i=0;i<FIELD_WIDTH;i++)
            for (int j=0;j<FIELD_WIDTH;j++) gameTiles[i][j]=new Tile();
        addTile();
        addTile();
        score=0;
        maxTile=2;
    }

    private void addTile() {
        List<Tile> list = getEmptyTiles();
        if (list.size()==0) return;
        list.get((int) (list.size()*Math.random())).value = Math.random() < 0.9 ? 2 : 4;
    }
    private boolean compressTiles(Tile[] tiles) {
        boolean isChanged = false;
        Tile[] tmp = new Tile[FIELD_WIDTH];
        for (int i=0;i<FIELD_WIDTH;i++) tmp[i]=new Tile();
        int shift =0;
        for (int i=0;i<FIELD_WIDTH;i++)
            if (tiles[i].value>0) {
                if (shift!=i) isChanged=true;
                tmp[shift]=tiles[i];
                shift++;
            }
        System.arraycopy(tmp,0,tiles,0,tmp.length);
        return isChanged;
    }
    private boolean mergeTiles(Tile[] tiles) {
        boolean isChanged = false;
        for (int i=0;i<FIELD_WIDTH-1;i++)
            if (tiles[i].value==tiles[i+1].value && tiles[i].value!=0) {
                tiles[i].value*=2;
                score+=tiles[i].value;
                if (tiles[i].value>maxTile) maxTile=tiles[i].value;
                tiles[i+1]=new Tile();
                isChanged = true;
                compressTiles(tiles);
            }

        return isChanged;
    }

    public void left() {
        boolean isChanged=false;
        for (Tile[] t:gameTiles) {
            if (compressTiles(t) | mergeTiles(t)) isChanged=true;
        }

        if (isChanged) addTile();

    }
}
