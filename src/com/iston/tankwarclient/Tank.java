package com.iston.tankwarclient;

import javax.sql.rowset.BaseRowSet;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Tank {

    public static final int TANK_X_SPEED = 5;
    public static final int TANK_Y_SPEED = 5;
    private int x, y;//位置坐标

    public static final int TANK_WIDTH = 30;
    public static final int TANK_HIGH = 30;

    TankWarClient tc;

    private boolean left = false, up = false, right = false, down = false;

    enum Direction {
        L, LU, U, RU, R, RD, D, LD, STOP
    }

    private Direction dir = Direction.STOP;

    // 同名的构造方法
    public Tank(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Tank(int x, int y, TankWarClient tc) {
        this(x, y); // 直接调用另外一个同名的构造函数
        this.tc = tc;
    }

    void move() {
        switch (dir) {
            case L -> x -= TANK_X_SPEED;
            case LU -> {
                x -= TANK_X_SPEED;
                y -= TANK_Y_SPEED;
            }
            case U -> y -= TANK_Y_SPEED;
            case RU -> {
                x += TANK_X_SPEED;
                y -= TANK_Y_SPEED;
            }
            case R -> x += TANK_X_SPEED;
            case RD -> {
                x += TANK_X_SPEED;
                y += TANK_Y_SPEED;
            }
            case D -> y += TANK_Y_SPEED;
            case LD -> {
                x -= TANK_X_SPEED;
                y += TANK_Y_SPEED;
            }
        }
    }

    public void draw(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.RED); //设置画笔颜色
        g.fillOval(x, y, TANK_WIDTH, TANK_HIGH); //设置坦克的初始位置
        g.setColor(c); // 恢复画笔颜色
        move();
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_CONTROL -> {
                System.out.println("按下了ctrl键");
                tc.myMissile = fire();
            }
            case KeyEvent.VK_LEFT -> left = true;
            case KeyEvent.VK_UP -> up = true;
            case KeyEvent.VK_RIGHT -> right = true;
            case KeyEvent.VK_DOWN -> down = true;
        }
        locateDirection();
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT -> left = false;
            case KeyEvent.VK_UP -> up = false;
            case KeyEvent.VK_RIGHT -> right = false;
            case KeyEvent.VK_DOWN -> down = false;
        }
        locateDirection();
    }

    void locateDirection() {
        if (left && !up && !right && !down) {
            dir = Direction.L;
        } else if (left && up && !right && !down) {
            dir = Direction.LU;
        } else if (!left && up && !right && !down) {
            dir = Direction.U;
        } else if (!left && up && right && !down) {
            dir = Direction.RU;
        } else if (!left && !up && right && !down) {
            dir = Direction.R;
        } else if (!left && !up && right && down) {
            dir = Direction.RD;
        } else if (!left && !up && !right && down) {
            dir = Direction.D;
        } else if (left && !up && !right && down) {
            dir = Direction.LD;
        } else if (!left && !up && !right && !down) {
            dir = Direction.STOP;
        }
    }

    public Missile fire() {
        int x = this.x + Tank.TANK_WIDTH / 2 - Missile.MISSILE_WIDTH / 2;
        int y = this.y + Tank.TANK_HIGH / 2 - Missile.MISSILE_HIGH / 2;
        Missile missile = new Missile(x, y, dir);
        return missile;
    }
}
