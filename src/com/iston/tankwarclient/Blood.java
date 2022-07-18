package com.iston.tankwarclient;

import java.awt.*;

public class Blood {
    int x, y, w, h;
    int step = 0;
    TankWarClient tc;

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    private boolean live = true;


    private int[][] position = {{350, 300}, {360, 300}, {375, 275}, {400, 200}, {360, 270}, {365, 290}, {340, 280}};

    public Blood() {
        x = position[0][0];
        y = position[0][1];
        w = h = 15;
    }

    public void move() {
        step++;
        if (step == position.length) {
            step = 0;
        }
        x = position[step][0];
        y = position[step][1];
    }

    public void draw(Graphics g) {
        if (!live) {
            return;
        }
        Color c = g.getColor();
        g.setColor(Color.MAGENTA);
        g.fillRect(x, y, w, h);
        g.setColor(c);
        move();
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, w, h);
    }
}
