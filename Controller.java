package com.javarush.task.task35.task3513;


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static java.awt.event.KeyEvent.*;

public class Controller extends KeyAdapter {
    private final static int WINNING_TILE = 2048;
    private Model model = new Model();
    private View view;

    public Controller(Model model) {
        this.model = model;
        this.view = new View(this);
    }

    public Tile[][] getGameTiles() {
        return model.getGameTiles();
    }

    public int getScore() {
        return model.score;
    }

    public void resetGame() {
        model.score=0;
        view.isGameWon = false;
        view.isGameLost = false;
        model.resetGameTiles();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key==VK_ESCAPE) resetGame();
        if (!model.canMove()) view.isGameLost=true;
        if (!view.isGameLost && !view.isGameWon) {
            switch (key) {
                case VK_LEFT:model.left();break;
                case VK_RIGHT:model.right();break;
                case VK_DOWN:model.down();break;
                case VK_UP:model.up();break;
                case VK_Z:model.rollback();break;
                case VK_R:model.randomMove();break;
                case VK_A:model.autoMove();break;
            }
            if (WINNING_TILE==model.maxTile) view.isGameWon=true;
        }
        view.repaint();
    }

    public View getView() {
        return view;
    }
}
