package com.cpp.netty.serialize.netty;

import lombok.Data;

import java.io.Serializable;

/**
 * request
 *
 * @author chenjian
 * @date 2019-04-22 18:46
 */
@Data
public class SubscribeReq implements Serializable {

    private static final long serialVersionUID = 1L;

    private int subReqId;
    private String userName;
    private String productName;
    private String phoneNumber;
    private String address;
}
