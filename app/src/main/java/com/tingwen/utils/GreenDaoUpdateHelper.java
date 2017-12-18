package com.tingwen.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.tingwen.gen.DaoMaster;
import com.tingwen.gen.DownLoadNewsDao;
import com.tingwen.gen.HistoryNewsDao;
import com.tingwen.gen.LastPlayClassDao;
import com.tingwen.gen.ListenedNewsDao;

import org.greenrobot.greendao.database.Database;

/**
 * 数据库升级工具
 * Created by Administrator on 2017/10/23 0023.
 */
public class GreenDaoUpdateHelper extends DaoMaster.OpenHelper {

    public GreenDaoUpdateHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);

        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {

            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);

            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);

            }
        }, DownLoadNewsDao.class, ListenedNewsDao.class, HistoryNewsDao.class, LastPlayClassDao.class);//在这添加所有的dao
    }
}