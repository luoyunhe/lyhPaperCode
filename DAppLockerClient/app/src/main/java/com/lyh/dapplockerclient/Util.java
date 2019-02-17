package com.lyh.dapplockerclient;

import java.io.File;

public class Util {
    public static final String USER_ETH_ADDR_KEY = "user_eth_addr";
    public static final String WALLET_FILE_NAME = "wallet_file_name";
    public static final String LOCK_INFO_KEY = "lock_info";
    public static final String USER_INFO_KEY = "user_info";
    public static final String USER_PRI_KEY_KEY = "user_pri_key";
    public static final String USER_PUB_KEY_KEY = "user_pub_key";


    public static final String KEY_ALGORITHM = "RSA";
    static public File walletDir = null;
    static public String getWalletDirString() {
        return walletDir.getAbsolutePath();
    }
}
