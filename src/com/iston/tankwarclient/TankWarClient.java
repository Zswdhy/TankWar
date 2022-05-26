package com.iston.tankwarclient;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class TankWarClient extends Frame {
    public static final int GAME_WIDTH = 800;
    public static final int GAME_HIGH = 600;

    Tank myTank = new Tank(50, 50);
    Image offScreenImage = null; // 背后虚拟的图片

    // 窗口重画的时候，自动调用paint()方法
    public void paint(Graphics g) {
        myTank.draw(g);
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
            myTank.keyPressed(e);
        }
    }

}