package com.ngnis.walle.common;

/**
 * @author houyi
 */
public interface Constants {

    Byte IS_DELETED = (byte) 1;
    Byte NOT_DELETED = (byte) 0;

    /**
     * 用来对用户提交的密码进行加密的salt
     */
    String SALT_USE_TO_ENCRYPT_PWD = "_%$$walle$$%_";

}
