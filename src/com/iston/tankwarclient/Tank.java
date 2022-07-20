package com.iston.tankwarclient;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;

public class Tank {

    public static final int TANK_X_SPEED = 5;
    public static final int TANK_Y_SPEED = 5;
    private int x, y;//位置坐标
    private int old_x, old_y;//位置坐标


    public int getBLOOD() {
        return BLOOD;
    }

    public void setBLOOD(int BLOOD) {
        this.BLOOD = BLOOD;
    }

    private int BLOOD = 100; // 生命值
    private BloodBar bloodBar = new BloodBar();
    private boolean good;

    public boolean isGood() {
        return good;
    }

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
    private int step = random.nextInt(12) + 3;

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
        this.old_x = x;
        this.old_y = y;
        this.good = good;
    }

    public Tank(int x, int y, boolean good, Direction dir, TankWarClient tc) {
        this(x, y, good); // 直接调用另外一个同名的构造函数
        this.dir = dir;
        this.tc = tc;
    }

    void move() {

        this.old_x = x;
        this.old_y = y;

        switch (dir) {
            case L: {
                x -= TANK_X_SPEED;
                break;
            }
            case LU: {
                x -= TANK_X_SPEED;
                y -= TANK_Y_SPEED;
                break;
            }
            case U: {
                y -= TANK_Y_SPEED;
                break;
            }
            case RU: {
                x += TANK_X_SPEED;
                y -= TANK_Y_SPEED;
                break;
            }
            case R: {
                x += TANK_X_SPEED;
                break;
            }
            case RD: {
                x += TANK_X_SPEED;
                y += TANK_Y_SPEED;
                break;
            }
            case D: {
                y += TANK_Y_SPEED;
                break;
            }
            case LD: {
                x -= TANK_X_SPEED;
                y += TANK_Y_SPEED;
                break;
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

            if (step == 0) {
                step = random.nextInt(12) + 3;
                int random_num = random.nextInt(dirs.length);
                dir = dirs[random_num];
            }
            step--;
            if (random.nextInt(40) > 30) {
                this.fire();
            }
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
            bloodBar.draw(g);
        } else {
            g.setColor(Color.BLUE); //设置 badTank 画笔颜色
        }

        g.fillOval(x, y, TANK_WIDTH, TANK_HIGH); //设置坦克的初始位置
        g.setColor(c); // 恢复画笔颜色
        // 根据坦克的方向画炮筒
        switch (gunDir) {
            case L: {
                g.drawLine(x + TANK_WIDTH / 2, y + TANK_HIGH / 2, x, y + TANK_HIGH / 2);
                break;
            }
            case LU: {
                g.drawLine(x + TANK_WIDTH / 2, y + TANK_HIGH / 2, x, y);
                break;
            }
            case U: {
                g.drawLine(x + TANK_WIDTH / 2, y + TANK_HIGH / 2, x + TANK_WIDTH / 2, y);
                break;
            }
            case RU: {
                g.drawLine(x + TANK_WIDTH / 2, y + TANK_HIGH / 2, x + TANK_WIDTH, y);
                break;
            }
            case R: {
                g.drawLine(x + TANK_WIDTH / 2, y + TANK_HIGH / 2, x + TANK_WIDTH, y + TANK_HIGH / 2);
                break;
            }
            case RD: {
                g.drawLine(x + TANK_WIDTH / 2, y + TANK_HIGH / 2, x + TANK_WIDTH, y + TANK_HIGH);
                break;
            }
            case D: {
                g.drawLine(x + TANK_WIDTH / 2, y + TANK_HIGH / 2, x + TANK_WIDTH / 2, y + TANK_HIGH);
                break;
            }
            case LD: {
                g.drawLine(x + TANK_WIDTH / 2, y + TANK_HIGH / 2, x, y + TANK_HIGH);
                break;
            }
        }

        move();
    }

    private void restart() {
        if (!this.isLive) {
            this.isLive = true;
            this.BLOOD = 100;
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT: {
                left = true;
                break;
            }
            case KeyEvent.VK_UP: {
                up = true;
                break;
            }
            case KeyEvent.VK_RIGHT: {
                right = true;
                break;
            }
            case KeyEvent.VK_DOWN: {
                down = true;
                break;
            }
            case KeyEvent.VK_F2: {
                restart();
                break;
            }

        }
        locateDirection();
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_CONTROL: {
                fire();
                break;
            } // 键盘弹起ctrl键发送炮弹
            case KeyEvent.VK_LEFT: {
                left = false;
                break;
            }
            case KeyEvent.VK_UP: {
                up = false;
                break;
            }
            case KeyEvent.VK_RIGHT: {
                right = false;
                break;
            }
            case KeyEvent.VK_DOWN: {
                down = false;
                break;
            }
            case KeyEvent.VK_A: {
                superFire();
                break;
            }
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

    public Missile fire(Direction dir) {
        if (!isLive()) {
            return null;
        }
        int x = this.x + Tank.TANK_WIDTH / 2 - Missile.MISSILE_WIDTH / 2;
        int y = this.y + Tank.TANK_HIGH / 2 - Missile.MISSILE_HIGH / 2;
        Missile m = new Missile(x, y, good, dir, this.tc);// 根据炮筒的方向fire
        tc.missiles.add(m); // 将每次初始化的炮弹加入ArrayList<>
        return m;
    }


    public void fire() {

        if (!isLive()) {
            return;
        }
        int x = this.x + Tank.TANK_WIDTH / 2 - Missile.MISSILE_WIDTH / 2;
        int y = this.y + Tank.TANK_HIGH / 2 - Missile.MISSILE_HIGH / 2;
        Missile m = new Missile(x, y, good, gunDir, this.tc);// 根据炮筒的方向fire
        tc.missiles.add(m); // 将每次初始化的炮弹加入ArrayList<>
    }

    // 超级炮弹
    public void superFire() {
        Direction[] dirs = Direction.values();

        for (int i = 0; i < 8; i++) {
            fire(dirs[i]);
        }
    }

    private void stay() {
        // 修改为上一次移动位置的pos
        x = old_x;
        y = old_y;
    }

    public boolean collidesWithWall(Wall w) {
        // 子弹和墙的碰撞检测
        if (this.isLive && this.getRectangle().intersects(w.getRect())) {
            this.stay();
            return true;
        }
        return false;
    }

    public boolean collidesWithTanks(List<Tank> tanks) {
        for (Tank t : tanks) {
            if (this != t) {
                // 敌方坦克碰撞检测
                if (this.isLive && t.isLive() && this.getRectangle().intersects(t.getRectangle())) {
                    this.stay();
                    t.stay();
                    return true;
                }
            }
        }
        return false;
    }

    private class BloodBar {
        public void draw(Graphics g) {
            Color c = g.getColor();
            g.setColor(Color.RED);
            g.drawRect(x, y - 10, TANK_WIDTH, 10);
            int w = TANK_WIDTH * BLOOD / 100;
            g.fillRect(x, y - 10, w, 10);
            g.setColor(c);
        }
    }

    public boolean eatBlood(Blood b) {
        if (this.isLive() && b.isLive() && this.getRectangle().intersects(b.getRect())) {
            this.BLOOD = 100;
            b.setLive(false);
            return true;
        }
        return true;
    }
}