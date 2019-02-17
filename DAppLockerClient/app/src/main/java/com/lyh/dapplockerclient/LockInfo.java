package com.lyh.dapplockerclient;

import android.os.Parcel;
import android.os.Parcelable;

public class LockInfo implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(contractAddr);
        dest.writeByte((byte) (isImport ? 1 : 0));
    }
    public static final Parcelable.Creator<LockInfo> CREATOR = new Creator<LockInfo>() {

        @Override
        public LockInfo createFromParcel(Parcel source) {
            LockInfo info = new LockInfo();
            info.setName(source.readString());
            info.setContractAddr(source.readString());
            info.setImport(source.readByte() != 0);
            return info;
        }

        @Override
        public LockInfo[] newArray(int size) {
            return new LockInfo[0];
        }
    };
}
