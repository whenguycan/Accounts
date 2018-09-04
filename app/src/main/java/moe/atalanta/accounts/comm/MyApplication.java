package moe.atalanta.accounts.comm;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import moe.atalanta.accounts.entity.DaoMaster;
import moe.atalanta.accounts.entity.DaoSession;

/**
 * Created by wang on 2018/8/28.
 */

public class MyApplication extends Application {

    private DaoMaster.DevOpenHelper mDevOpenHelper;
    private SQLiteDatabase mDb;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    public static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
    }

    private void init(){
        mDevOpenHelper = new DaoMaster.DevOpenHelper(instance, "atalanta.db", null);
        mDb = mDevOpenHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(mDb);
        mDaoSession = mDaoMaster.newSession();
    }

    public static MyApplication getInstance(){
        return instance;
    }
    public DaoSession getDaoSession(){
        return mDaoSession;
    }
    public SQLiteDatabase getDb(){
        return mDb;
    }
}
