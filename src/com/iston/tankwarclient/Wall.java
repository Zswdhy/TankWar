package com.iston.tankwarclient;

import java.awt.*;

public class Wall {
    int x, y, w, h;
    TankWarClient tc;

    public Wall(int x, int y, int w, int h, TankWarClient tc) {
        // 构造方法，快捷键 alt + ins
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.tc = tc;
    }

    public void draw(Graphics g) {
        g.setColor(Color.orange);
        g.fillRect(x, y, w, h);
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, w, h);
    }
}
