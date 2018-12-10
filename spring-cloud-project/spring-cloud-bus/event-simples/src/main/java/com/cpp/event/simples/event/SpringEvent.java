package com.cpp.event.simples.event;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * spring事件
 *
 * @author chenjian
 * @date 2018-12-10 09:39
 */
public class SpringEvent {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        context.addApplicationListener(e -> {
            System.err.println("监听 : " + e.getClass().getSimpleName());
        });

        context.refresh();
        context.start();
        context.stop();
        context.close();
    }
}
