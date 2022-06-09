package com.iston.tankwarclient;

import java.awt.*;

public class Explode {
    int x, y; // 爆炸位置
    private boolean live = true;

    private TankWarClient tc;

    public Explode(int x, int y, TankWarClient tc) {
        this.x = x;
        this.y = y;
        this.tc = tc;
    }

    int[] diameter = {4, 7, 12, 18, 26, 32, 49, 30, 14, 6};
    int step = 0;


    public void draw(Graphics g) {
        if (!live) {
            tc.explodes.remove(this);
            return;
        }

        if (step == diameter.length) {
            step = 0;
            live = false;
            return;
        }

        Color c = g.getColor();

        g.setColor(Color.ORANGE);
        g.fillOval(x, y, diameter[step], diameter[step]);
        g.setColor(c);
        step++;
    }
}
