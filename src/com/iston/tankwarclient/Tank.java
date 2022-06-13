package com.iston.tankwarclient;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Tank {

    public static final int TANK_X_SPEED = 5;
    public static final int TANK_Y_SPEED = 5;
    private int x, y;//位置坐标

    private boolean good;

    public static final int TANK_WIDTH = 30;
    public static final int TANK_HIGH = 30;

    public void setLive(boolean live) {
        // 因为 isLive 为私有变量，需要设置 setLive 方式
        isLive = live;
    }

    public boolean isLive() {
        return isLive;
    }

    private static Random random = new Random();

    private boolean isLive = true;

    TankWarClient tc;

    private boolean left = false, up = false, right = false, down = false;

    enum Direction {
        L, LU, U, RU, R, RD, D, LD, STOP
    }

    private Direction dir = Direction.STOP; // 坦克方向
    private Direction gunDir = Direction.D; //炮筒方向

    // 同名的构造方法
    public Tank(int x, int y, boolean good) {
        this.x = x;
        this.y = y;
        this.good = good;
    }

    public Tank(int x, int y, boolean good, Direction dir, TankWarClient tc) {
        this(x, y, good); // 直接调用另外一个同名的构造函数
        this.dir = dir;
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
        // 坦克方向和炮筒方向同时更新
        if (this.dir != Direction.STOP) {
            this.gunDir = this.dir;
        }

        if (x < 0) {
            x = 0;
        }
        if (y < TANK_HIGH) {
            y = TANK_HIGH;
        }
        if (x + TANK_WIDTH > TankWarClient.GAME_WIDTH) {
            x = TankWarClient.GAME_WIDTH - TANK_WIDTH;
        }
        if (y + TANK_HIGH > TankWarClient.GAME_HIGH) {
            y = TankWarClient.GAME_HIGH - TANK_HIGH;
        }

        if (!good) {
            Direction[] dirs = Direction.values();
            int random_num = random.nextInt(dirs.length);
            dir = dirs[random_num];
        }
    }

    public void draw(Graphics g) {
        if (!isLive) {
            if (!good) {
                tc.tanks.remove(this);
            }
            return; // 如果 tank 死掉，不重画
        }

        Color c = g.getColor();
        if (good) {
            g.setColor(Color.RED); //设置 my tank 画笔颜色
        } else {
            g.setColor(Color.BLUE); //设置 badTank 画笔颜色
        }

        g.fillOval(x, y, TANK_WIDTH, TANK_HIGH); //设置坦克的初始位置
        g.setColor(c); // 恢复画笔颜色
        // 根据坦克的方向画炮筒
        switch (gunDir) {
            case L -> g.drawLine(x + TANK_WIDTH / 2, y + TANK_HIGH / 2, x, y + TANK_HIGH / 2);
            case LU -> g.drawLine(x + TANK_WIDTH / 2, y + TANK_HIGH / 2, x, y);
            case U -> g.drawLine(x + TANK_WIDTH / 2, y + TANK_HIGH / 2, x + TANK_WIDTH / 2, y);
            case RU -> g.drawLine(x + TANK_WIDTH / 2, y + TANK_HIGH / 2, x + TANK_WIDTH, y);
            case R -> g.drawLine(x + TANK_WIDTH / 2, y + TANK_HIGH / 2, x + TANK_WIDTH, y + TANK_HIGH / 2);
            case RD -> g.drawLine(x + TANK_WIDTH / 2, y + TANK_HIGH / 2, x + TANK_WIDTH, y + TANK_HIGH);
            case D -> g.drawLine(x + TANK_WIDTH / 2, y + TANK_HIGH / 2, x + TANK_WIDTH / 2, y + TANK_HIGH);
            case LD -> g.drawLine(x + TANK_WIDTH / 2, y + TANK_HIGH / 2, x, y + TANK_HIGH);
        }

        move();
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
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
            case KeyEvent.VK_CONTROL -> fire(); // 键盘弹起ctrl键发送炮弹
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
        } else if (!left && !up && right) {
            dir = Direction.RD;
        } else if (!left && !up && down) {
            dir = Direction.D;
        } else if (left && !up && !right) {
            dir = Direction.LD;
        } else if (!left && !up) {
            dir = Direction.STOP;
        }
    }

    public Rectangle getRectangle() {
        return new Rectangle(x, y, TANK_WIDTH, TANK_HIGH);
    }

    public void fire() {
        int x = this.x + Tank.TANK_WIDTH / 2 - Missile.MISSILE_WIDTH / 2;
        int y = this.y + Tank.TANK_HIGH / 2 - Missile.MISSILE_HIGH / 2;
        Missile m = new Missile(x, y, gunDir, this.tc);// 根据炮筒的方向fire
        tc.missiles.add(m); // 将每次初始化的炮弹加入ArrayList<>
    }
}
