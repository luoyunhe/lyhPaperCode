package com.lyh.dapplockerclient;

import android.content.Context;

public class GreenDaoManager {

    private static final String TAG = "GreenDaoManager";

    private static final String DATABASE_NAME = "app.db";

    private boolean isInited;

    private DaoSession daoSession;

    private static final class GreenDaoManagerHolder {
        private static final GreenDaoManager sInstance = new GreenDaoManager();
    }
    public static GreenDaoManager getInstance() {
        return GreenDaoManagerHolder.sInstance;
    }

    private GreenDaoManager() {}

    /**
     * 初始化DaoSession
     *
     * @param context
     */
    public void init(Context context) {
        if (!isInited) {
            DaoMaster.OpenHelper openHelper = new DaoMaster.DevOpenHelper(
                    context.getApplicationContext(), DATABASE_NAME, null);
            DaoMaster daoMaster = new DaoMaster(openHelper.getWritableDatabase());
            daoSession = daoMaster.newSession();
            isInited = true;
        }
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
