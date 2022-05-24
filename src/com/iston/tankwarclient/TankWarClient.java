package com.iston.tankwarclient;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class TankWarClient extends Frame {

    int x = 50, y = 50;

    // 窗口重画的时候，自动调用paint()方法
    public void paint(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.RED); //设置画笔颜色
        g.fillOval(x, y, 30, 30); //设置坦克的初始位置
        g.setColor(c); // 恢复画笔颜色
        x += 3;
    }

    public void launchFrame() {
        this.setLocation(300, 80);  // 屏幕左上角的左边位置
        this.setSize(800, 600); // 设置窗体的大小
        this.setTitle("坦克大战1.0");
        this.setBackground(Color.GREEN); // 设置窗体背景色
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0); //程序的正常退出
            }
        }); //匿名类，重写内部方法
        this.setResizable(false); // 禁止修改窗体的大小
        setVisible(true);// 显示窗体
        new Thread(new paintThread()).start();
    }

    public static void main(String[] args) {
        System.out.println("test");
        TankWarClient tc = new TankWarClient();
        tc.launchFrame();
    }


    private class paintThread implements Runnable {

        public void run() {
            while (true) {
                repaint(); // 调用外部类的重画方法，因为未重写repaint，即调用父类Frame的repaint()方法
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
