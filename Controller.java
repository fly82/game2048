package com.javarush.task.task35.task3513;


import java.awt.event.KeyAdapter;

public class Controller extends KeyAdapter {
    private Model model = new Model();
    private View view = new View();

    public Tile[][] getGameTiles() {
        return model.getGameTiles();
    }

    public int getScore() {
        return model.score;
    }
}
