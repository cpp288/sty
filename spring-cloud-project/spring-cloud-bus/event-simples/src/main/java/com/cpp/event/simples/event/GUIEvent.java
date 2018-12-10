package com.cpp.event.simples.event;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * java gui事件
 *
 * @author chenjian
 * @date 2018-12-10 09:34
 */
public class GUIEvent {

    public static void main(String[] args) throws Exception {

        final JFrame frame = new JFrame("简单 GUI 程序 - Java 事件/监听机制");

        // 增加一个鼠标事件的监听器
        // MouseAdapter 实现了 EventListener，但都是空实现，是因为java8之前，接口没有 default 方法
        frame.addMouseListener(new MouseAdapter() {
            // 重写鼠标点击事件
            @Override
            public void mouseClicked(MouseEvent event) {
                System.out.printf("[%s] 事件 : %s\n", Thread.currentThread().getName(), event);
            }
        });

        frame.setBounds(300, 300, 400, 300);

        frame.setVisible(true);

        // 增加一个窗口事件的监听器
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }

            @Override
            public void windowClosed(WindowEvent event) {
                System.exit(0);
            }
        });

    }
}
