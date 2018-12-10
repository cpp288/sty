package com.cpp.event.simples.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.EventListener;

/**
 * spring注解驱动事件
 *
 * @author chenjian
 * @date 2018-12-10 09:42
 */
public class SpringAnnotationDrivenEvent {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        // SpringAnnotationDrivenEvent 注册为 Spring Bean
        context.register(SpringAnnotationDrivenEvent.class);

        // 启动上下文
        context.refresh();
        // 确保上下文启动完毕后，再发送事件
        context.publishEvent(new MyApplicationEvent("Hello,World"));
        // 关闭上下文
        context.close();
    }

    /**
     * 自定义事件
     */
    private static class MyApplicationEvent extends ApplicationEvent {

        public MyApplicationEvent(Object source) {
            super(source);
        }
    }

    /**
     * 监听 MyApplicationEvent 事件，不设置将监听全部事件
     *
     * @param event
     */
    @EventListener(MyApplicationEvent.class)
    public void onMessage(MyApplicationEvent event) {
        System.err.println("监听到 MyApplicationEvent 事件源 : " + event.getSource());
    }
}
