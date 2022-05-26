package com.iston.tankwarclient;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Tank {

    public static final int TANK_SPEED = 5;
    int x, y;//位置坐标

    // 同名的构造方法
    public Tank(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.RED); //设置画笔颜色
        g.fillOval(x, y, 30, 30); //设置坦克的初始位置
        g.setColor(c); // 恢复画笔颜色
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT -> x -= TANK_SPEED;
            case KeyEvent.VK_UP -> y -= TANK_SPEED;
            case KeyEvent.VK_RIGHT -> x += TANK_SPEED;
            case KeyEvent.VK_DOWN -> y += TANK_SPEED;
        }
    }
}
