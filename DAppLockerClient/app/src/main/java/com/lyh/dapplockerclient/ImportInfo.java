package com.lyh.dapplockerclient;

public class ImportInfo {
    private String userAddr;
    private String userPubKey;
    private String userName;


    public String getUserPubKey() {
        return userPubKey;
    }

    public void setUserPubKey(String userPubKey) {
        this.userPubKey = userPubKey;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAddr() {
        return userAddr;
    }

    public void setUserAddr(String userAddr) {
        this.userAddr = userAddr;
    }
}
