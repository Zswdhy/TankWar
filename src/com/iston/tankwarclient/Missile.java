package com.iston.tankwarclient;

import java.awt.*;

public class Missile {
    int x, y;
    Tank.Direction dir;
    public static final int MISSILE_X_SPEED = 10;
    public static final int MISSILE_Y_SPEED = 10;

    public static final int MISSILE_WIDTH = 10;
    public static final int MISSILE_HIGH = 10;

    public Missile(int x, int y, Tank.Direction dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    public void draw(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.BLACK);
        g.fillOval(x, y, MISSILE_WIDTH, MISSILE_HIGH);
        g.setColor(c);
        move();
    }

    public void move() {
        switch (dir) {
            case L -> x -= MISSILE_X_SPEED;
            case LU -> {
                x -= MISSILE_X_SPEED;
                y -= MISSILE_Y_SPEED;
            }
            case U -> y -= MISSILE_Y_SPEED;
            case RU -> {
                x += MISSILE_X_SPEED;
                y -= MISSILE_Y_SPEED;
            }
            case R -> x += MISSILE_X_SPEED;
            case RD -> {
                x += MISSILE_X_SPEED;
                y += MISSILE_Y_SPEED;
            }
            case D -> y += MISSILE_Y_SPEED;
            case LD -> {
                x -= MISSILE_X_SPEED;
                y += MISSILE_Y_SPEED;
            }
        }
    }

}
