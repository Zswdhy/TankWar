package com.iston.tankwarclient;

import java.awt.*;
import java.util.List;

public class Missile {
    private TankWarClient tc;
    int x, y;
    Tank.Direction dir;

    private boolean good;
    public static final int MISSILE_X_SPEED = 10;
    public static final int MISSILE_Y_SPEED = 10;

    public static final int MISSILE_WIDTH = 10;
    public static final int MISSILE_HIGH = 10;

    public boolean isLive() {
        return live;
    }

    private boolean live = true;

    public Missile(int x, int y, Tank.Direction dir) {
        this.x = x;
        this.y = y;

        this.dir = dir;
    }

    public Missile(int x, int y, boolean good, Tank.Direction dir, TankWarClient tc) {
        this(x, y, dir);
        this.good = good;
        this.tc = tc;
    }

    public void draw(Graphics g) {
        // 子弹重画之前，先判断是否活着
        if (!live) {
            tc.missiles.remove(this);
            return;
        }

        Color c = g.getColor();
        g.setColor(Color.BLACK);
        g.fillOval(x, y, MISSILE_WIDTH, MISSILE_HIGH);
        g.setColor(c);
        move();
    }

    public void move() {
        switch (dir) {
            case L: {
                x -= MISSILE_X_SPEED;
                break;
            }
            case LU: {
                x -= MISSILE_X_SPEED;
                y -= MISSILE_Y_SPEED;
                break;
            }
            case U: {
                y -= MISSILE_Y_SPEED;
                break;
            }
            case RU: {
                x += MISSILE_X_SPEED;
                y -= MISSILE_Y_SPEED;
                break;
            }
            case R: {
                x += MISSILE_X_SPEED;
                break;
            }
            case RD: {
                x += MISSILE_X_SPEED;
                y += MISSILE_Y_SPEED;
                break;
            }
            case D: {
                y += MISSILE_Y_SPEED;
                break;
            }
            case LD: {
                x -= MISSILE_X_SPEED;
                y += MISSILE_Y_SPEED;
                break;
            }
        }

        // 炮弹越界，修改炮弹状态
        if (x < 0 || y < 0 || x > TankWarClient.GAME_WIDTH || y > TankWarClient.GAME_HIGH) {
            live = false;
            tc.missiles.remove(this); // 无效炮弹，直接移除ArrayList<>
        }
    }

    public Rectangle getRectangle() {
        return new Rectangle(x, y, MISSILE_WIDTH, MISSILE_HIGH);
    }

    public boolean hitTank(Tank t) {
        // 碰撞检测
        if (this.live && this.getRectangle().intersects(t.getRectangle()) && t.isLive() && this.good != t.isGood()) {
            if (t.isGood()) {
                t.setBLOOD(t.getBLOOD() - 20);
                if (t.getBLOOD() <= 0) {
                    t.setLive(false);
                }
            } else {
                t.setLive(false);
            }

            this.live = false;
            Explode explode = new Explode(x, y, tc);
            tc.explodes.add(explode);
            return true;
        }
        return false;
    }


    public boolean hitTanks(List<Tank> tanks) {
        // 循环判断碰撞检测
        for (int i = 0; i < tanks.size(); i++) {
            if (hitTank(tanks.get(i))) {
                return true;
            }
        }
        return true;
    }

    public boolean hitWall(Wall w) {
        // 子弹和墙的碰撞检测
        if (this.live && this.getRectangle().intersects(w.getRect())) {
            this.live = false;
            return true;
        }
        return false;
    }

}
