package com.iston.tankwarclient;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class TankWarClient extends Frame {
    public static final int GAME_WIDTH = 800;
    public static final int GAME_HIGH = 600;

    Tank myTank = new Tank(50, 50, true, this); // my tank
    Image offScreenImage = null; // 背后虚拟的图片
    List<Missile> missiles = new ArrayList<>(); // 定义炮弹容器
    List<Explode> explodes = new ArrayList<>(); // 定义爆炸效果容器
    List<Tank> tanks = new ArrayList<>(); // 定义爆炸效果容器

    // 窗口重画的时候，自动调用paint()方法
    public void paint(Graphics g) {
        g.drawString("MissileCount:" + "【" + missiles.size() + "】", 10, 50);
        g.drawString("ExplodeCount:" + "【" + explodes.size() + "】", 10, 70);
        g.drawString("BadTanksCount:" + "【" + tanks.size() + "】", 10, 90);
        myTank.draw(g); // 自己坦克

        for (Missile missile : missiles) {
            missile.hitTanks(tanks);
            missile.draw(g);
        }

        for (Explode explode : explodes) {
            explode.draw(g);
        }

        for (Tank tank : tanks) {
            tank.draw(g);
        }

    }

    public void update(Graphics g) {
        // repaint 先调用 update() 后调用 paint()
        if (offScreenImage == null) {
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HIGH);
        }
        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor(); // 获取原本的颜色
        gOffScreen.setColor(Color.GREEN);
        gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HIGH);
        gOffScreen.setColor(c); // 恢复之前的颜色
        paint(gOffScreen);
        g.drawImage(offScreenImage, 0, 0, null); // 一次性将内容画在窗体内，解决因为刷新率照成的闪烁现象
    }

    public void launchFrame() {
        // 初始化敌人坦克
        for (int i = 0; i < 10; i++) {
            tanks.add(new Tank(50 + 40 * (i + 1), 50, false, this));
        }

        this.setLocation(300, 80);  // 屏幕左上角的左边位置
        this.setSize(GAME_WIDTH, GAME_HIGH); // 设置窗体的大小
        this.setTitle("坦克大战1.0");
        this.setBackground(Color.GREEN); // 设置窗体背景色
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0); //程序的正常退出
            }
        }); //匿名类，重写内部方法
        this.setResizable(false); // 禁止修改窗体的大小
        this.addKeyListener(new keyMonitor()); // 键盘监听事件
        setVisible(true);// 显示窗体
        new Thread(new paintThread()).start();
    }

    public static void main(String[] args) {
        TankWarClient tc = new TankWarClient();
        tc.launchFrame();
    }

    @SuppressWarnings("InfiniteLoopStatement") // 解决run方法的while(true)警告
    private class paintThread implements Runnable {
        public void run() {
            while (true) {
                repaint(); // 调用外部类的重画方法，因为未重写repaint，即调用父类Frame的repaint()方法
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private class keyMonitor extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            // 键盘按下触发的事件
            myTank.keyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            // 键盘弹起触发的事件，恢复现场
            myTank.keyReleased(e);
        }
    }

}
