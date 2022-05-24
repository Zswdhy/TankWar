package com.iston.tankwarclient;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class TankWarClient extends Frame {

    public void launchFrame() {
        this.setLocation(300, 80);  // 屏幕左上角的左边位置
        this.setSize(800, 600); // 设置窗体的大小
        this.setTitle("坦克大战1.0");
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0); //程序的正常退出
            }
        }); //匿名类，重写内部方法
        this.setResizable(false); // 禁止修改窗体的大小
        setVisible(true);// 显示窗体
    }

    public static void main(String[] args) {
        System.out.println("test");
        TankWarClient tc = new TankWarClient();
        tc.launchFrame();
    }
}
