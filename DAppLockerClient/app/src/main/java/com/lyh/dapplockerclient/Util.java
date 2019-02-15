package com.lyh.dapplockerclient;

import java.io.File;

public class Util {
    public static final String WALLET_FILE_NAME = "wallet_file_name";
    static public File walletDir = null;
    static public String getWalletDirString() {
        return walletDir.getAbsolutePath();
    }
}
