package com.lyh.dapplockerclient;

public class LockInfo {
    private String name;
    private String contractAddr;
    private boolean isImport;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContractAddr() {
        return contractAddr;
    }

    public void setContractAddr(String contractAddr) {
        this.contractAddr = contractAddr;
    }

    public boolean isImport() {
        return isImport;
    }

    public void setImport(boolean anImport) {
        isImport = anImport;
    }
}
