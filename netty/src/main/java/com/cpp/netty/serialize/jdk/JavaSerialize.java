package com.cpp.netty.serialize.jdk;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * java序列化
 *
 * @author chenjian
 * @date 2019-04-22 16:03
 */
public class JavaSerialize {

    public static void main(String[] args) throws IOException {
        UserInfo userInfo = new UserInfo();
        userInfo.buildUserId(100).buildUserName("hello");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(userInfo);
        os.flush();
        os.close();
        byte[] b = bos.toByteArray();

        System.out.println("The jdk serializable length is : " + b.length);
        bos.close();

        System.out.println("----------------------------------------------");

        System.out.println("The byte array serializable length is : " + userInfo.codeC().length);
    }
}
