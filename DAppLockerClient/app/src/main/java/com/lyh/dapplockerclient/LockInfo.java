package com.lyh.dapplockerclient;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;


@Entity
public class LockInfo implements Parcelable {

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "name")
    private String name;

    @Property(nameInDb = "contract_addr")
    private String contractAddr;

    @Property(nameInDb = "is_import")
    private boolean isImport;

    @Property(nameInDb = "import_task_is")
    private String importTaskId;

    public LockInfo(String name, String contractAddr, boolean isImport) {
        super();
        this.name = name;
        this.contractAddr = contractAddr;
        this.isImport = isImport;
    }

    public LockInfo(){super();}

    @Generated(hash = 154367389)
    public LockInfo(Long id, String name, String contractAddr, boolean isImport,
            String importTaskId) {
        this.id = id;
        this.name = name;
        this.contractAddr = contractAddr;
        this.isImport = isImport;
        this.importTaskId = importTaskId;
    }

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
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(contractAddr);
        dest.writeByte((byte) (isImport ? 1 : 0));
        dest.writeString(importTaskId);
    }
    public static final Parcelable.Creator<LockInfo> CREATOR = new Creator<LockInfo>() {

        @Override
        public LockInfo createFromParcel(Parcel source) {
            LockInfo info = new LockInfo();
            info.setId(source.readLong());
            info.setName(source.readString());
            info.setContractAddr(source.readString());
            info.setImport(source.readByte() != 0);
            info.setImportTaskId(source.readString());
            return info;
        }

        @Override
        public LockInfo[] newArray(int size) {
            return new LockInfo[0];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getIsImport() {
        return this.isImport;
    }

    public void setIsImport(boolean isImport) {
        this.isImport = isImport;
    }

    public String getImportTaskId() {
        return importTaskId;
    }

    public void setImportTaskId(String importTaskId) {
        this.importTaskId = importTaskId;
    }
}
