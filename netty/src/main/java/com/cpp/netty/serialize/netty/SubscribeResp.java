package com.cpp.netty.serialize.netty;

import lombok.Data;

import java.io.Serializable;

/**
 * response
 *
 * @author chenjian
 * @date 2019-04-22 18:47
 */
@Data
public class SubscribeResp implements Serializable {

    private static final long serialVersionUID = 1L;

    private int subReqId;
    private int respCode;
    private String desc;
}
