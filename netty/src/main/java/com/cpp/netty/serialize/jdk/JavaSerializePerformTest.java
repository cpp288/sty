package com.cpp.netty.serialize.jdk;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * java序列化性能测试
 *
 * @author chenjian
 * @date 2019-04-22 16:07
 */
public class JavaSerializePerformTest {

    public static void main(String[] args) throws IOException {
        UserInfo userInfo = new UserInfo();
        userInfo.buildUserId(100).buildUserName("hello");

        int loop = 1000000;
        ByteArrayOutputStream bos;
        ObjectOutputStream os;

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            bos = new ByteArrayOutputStream();
            os = new ObjectOutputStream(bos);
            os.writeObject(userInfo);
            os.flush();
            os.close();
            byte[] b = bos.toByteArray();
            bos.close();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("The jdk serializable cost time is : " + (endTime - startTime) + " ms");

        System.out.println("----------------------------------------------");

        startTime = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            userInfo.codeC();
        }
        endTime = System.currentTimeMillis();
        System.out.println("The byte array serializable cost time is : " + (endTime - startTime) + " ms");
    }
}
