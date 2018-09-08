package moe.atalanta.accounts;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.yuweiguocn.library.greendao.MigrationHelper;

import org.greenrobot.greendao.database.Database;

import moe.atalanta.accounts.entity.AccountsDao;
import moe.atalanta.accounts.entity.DaoMaster;
import moe.atalanta.accounts.entity.DaoSession;
import moe.atalanta.accounts.entity.UsersDao;

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

	private void init() {
		mDevOpenHelper = new MySQLiteOpenHelper(instance, "atalanta.db", null);
		mDb = mDevOpenHelper.getWritableDatabase();
		mDaoMaster = new DaoMaster(mDb);
		mDaoSession = mDaoMaster.newSession();
	}

	public static MyApplication getInstance() {
		return instance;
	}

	public DaoSession getDaoSession() {
		return mDaoSession;
	}

	public SQLiteDatabase getDb() {
		return mDb;
	}

	class MySQLiteOpenHelper extends DaoMaster.DevOpenHelper {

		public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
			super(context, name, factory);
		}

		@Override
		public void onUpgrade(Database db, int oldVersion, int newVersion) {
			MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
				@Override
				public void onCreateAllTables(Database db, boolean ifNotExists) {
					DaoMaster.createAllTables(db, ifNotExists);
				}
				@Override
				public void onDropAllTables(Database db, boolean ifExists) {
					DaoMaster.dropAllTables(db, ifExists);
				}
			}, AccountsDao.class, UsersDao.class);
		}
	}
}
