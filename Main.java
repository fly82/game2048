package com.javarush.task.task35.task3513;


public class Main {

    public static void main(String[] args) {

        Model model = new Model();

        model.canMove();
        System.out.println("score = " + model.score);
        System.out.println("max tile = " + model.maxTile);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                //System.out.print("[" + (model.gameTiles[i][j].value) + "]");
            }
            System.out.println();
        }
        for (int m = 0; m < 10; m++) {
            model.left();
            System.out.println("score = " + model.score);
            System.out.println("max tile = " + model.maxTile);
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    //System.out.print("[" + (model.gameTiles[i][j].value) + "]");
                }
                System.out.println();
            }
        }
    }
}
