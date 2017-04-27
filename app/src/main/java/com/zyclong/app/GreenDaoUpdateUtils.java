package com.zyclong.app;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.StandardDatabase;
import org.greenrobot.greendao.internal.DaoConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <pre>
 * Created by zyclong on 2017/4/26.
 * GreenDaoUpdateUtils:解决数据库升级问题
 * 1、修改旧版本的表名,创建新版本的表
 * 3、迁移数据
 *      1、Update接口把旧数据结构转成新版数据数据结构
 *      2、将数据插入到新版数据表中
 * 4、删除旧版本数据表
 * 5、有多个表要升级则循环1~4的步骤。
 * 要求：
 *      新版本的数据表对应的bean要实现Update接口；
 *      Update接口应该完成旧数据到新Bean类的兼容转换；
 *      bean类要有无参构造器。
 * </pre>
 */
public class GreenDaoUpdateUtils {
    /**
     * 调试日志输出控制位
     */
    private static final boolean DEBUG = true;
    /**
     * 调试日志输出的TAG
     */
    private static final String TAG = "GreenDaoUpdateUtils";

    private static final String SQLITE_MASTER = "sqlite_master";
    private static final String SQLITE_TEMP_MASTER = "sqlite_temp_master";

    /**
     * 临时表名称
     */
    private static String tempTableName = null;
    /**
     * 要升级的表名称
     */
    private static String tableName = null;

    /**
     * @see org.greenrobot.greendao.internal.DaoConfig
     */
    private static DaoConfig daoConfig = null;

    /**
     * 保存一行数据的Map集合
     */
    private static Map<String, Object> rawMap = new LinkedHashMap();


    /**
     * 数据库升级入口函数
     *
     * @param db
     * @param daoClasses 在数据库中，要升级bean的DaoClass实例，可变参数
     */
    public static void updateTable(SQLiteDatabase db, Class<? extends org.greenrobot.greendao.AbstractDao<?, ?>>... daoClasses) {
        StandardDatabase database = new StandardDatabase(db);

        printLog("The Old Database Version" + db.getVersion());

//        ALTER TABLE 旧表名 RENAME TO 新表名
        for (Class cla : daoClasses) {
            try {
                database.beginTransaction();
                printLog("Alter temp tablestart");
                alterTempTables(database, cla);
                printLog("Alter temp table complete " + tableName);
                printLog("migrateData data start");
                migrateData(database, cla);
                printLog("migrateData data complete " + tableName);
                deteleTempTables(database, cla);
                database.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                database.endTransaction();
            }
        }

    }

    /**
     * 删除临时表
     * @param db
     * @param cla
     * @throws SQLException
     */
    private static void deteleTempTables(StandardDatabase db, Class cla) throws SQLException {
        String Sql = "DROP TABLE " + tempTableName;
        db.execSQL(Sql);
        printLog(Sql + " has exec");
    }

    /**
     * 迁移数据
     * @param db
     * @param cla
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    private static void migrateData(StandardDatabase db, Class<? extends org.greenrobot.greendao.AbstractDao<?, ?>> cla)
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class table = (Class) getSuperClassParameterizedType(cla)[0];
        printLog(" bean class name: " + table.getName());
        Update up = (Update) table.newInstance();
        String Sql = "select * from " + tempTableName;
        Cursor c = db.rawQuery(Sql, null);
        Constructor constructor = cla.getDeclaredConstructor(DaoConfig.class);
        AbstractDao a = (AbstractDao) constructor.newInstance(daoConfig);
        if (c.moveToFirst()) {
            printLog(c.getCount() + "-->cursor.getCount");
            do {
                for (int i = 0; i < c.getColumnCount(); i++) {
                    rawMap.put(c.getColumnName(i), getObjectFromCursor(c, i));
                }
                up.updateOne(rawMap);
                a.insert(up);
            } while (c.moveToNext());
        }
        c.close();
    }

    /**
     * 从Cursor中获取指定列
     *
     * @param c
     * @param i
     * @return
     */
    private static Object getObjectFromCursor(Cursor c, int i) {
        int type = c.getType(i);
        switch (type) {
            case Cursor.FIELD_TYPE_NULL:
                return null;
            case Cursor.FIELD_TYPE_BLOB:
                return c.getBlob(i);
            case Cursor.FIELD_TYPE_FLOAT:
                return c.getFloat(i);
            case Cursor.FIELD_TYPE_INTEGER:
                return c.getInt(i);
            case Cursor.FIELD_TYPE_STRING:
                return c.getString(i);
        }
        return null;
    }

    /**
     * 修改表名，把旧版本的表改成临时表
     * @param db
     * @param cla
     * @throws Exception
     */
    private static void alterTempTables(StandardDatabase db, Class cla)
            throws Exception {
        daoConfig = new DaoConfig(db, cla);
        tableName = daoConfig.tablename;
        printLog(tableName + "-->tablename");
        if (!isTableExists(db, false, tableName)) {
            printLog("New Table" + tableName);
        } else {
            tempTableName = daoConfig.tablename.concat("_TEMP");
            StringBuilder e = new StringBuilder();
                /*IF EXISTS 去除原有的*/
            e.append("DROP TABLE IF EXISTS ").append(tempTableName).append(";");
            db.execSQL(e.toString());
            e.delete(0, e.length());
                /*修改表名*/
            e.append("ALTER  TABLE  " + tableName + " RENAME TO ").append(tempTableName).append(";");
            db.execSQL(e.toString());
            printLog("Table" + tableName + "\n ---Columns-->" + getColumnsStr(daoConfig));
            printLog("Generate temp table" + tempTableName);
            createTables(db, true, cla);
        }
    }

    /**
     * 创建新版数据库的表
     * @param db
     * @param b
     * @param cla
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private static void createTables(StandardDatabase db, boolean b, Class cla) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        reflectMethod(db, "createTable", b, cla);
        printLog("Create all table " + tableName);
    }

    /**
     * 反射执行指定方法，创建数据库表时使用
     * @param db
     * @param methodName
     * @param isExists
     * @param daoClasses
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private static void reflectMethod(Database db, String methodName, boolean isExists, @NonNull Class... daoClasses)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (daoClasses.length >= 1) {
            Class[] e = daoClasses;
            int var5 = daoClasses.length;
            for (int var6 = 0; var6 < var5; ++var6) {
                Class cls = e[var6];
                Method method = cls.getDeclaredMethod(methodName, new Class[]{Database.class, Boolean.TYPE});
                method.invoke((Object) null, new Object[]{db, Boolean.valueOf(isExists)});
            }

        }
    }

    /**
     * 获取新版表的全部字段
     * @param daoConfig
     * @return
     */
    private static String getColumnsStr(DaoConfig daoConfig) {
        if (daoConfig == null) {
            return "no columns";
        } else {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < daoConfig.allColumns.length; ++i) {
                builder.append(daoConfig.allColumns[i]);
                builder.append(",");
            }
            if (builder.length() > 0) {
                builder.deleteCharAt(builder.length() - 1);
            }
            return builder.toString();
        }
    }

    /**
     * 判断指定表是否在数据库中
     * @param db
     * @param isTemp
     * @param tableName
     * @return
     */
    private static boolean isTableExists(Database db, boolean isTemp, String tableName) throws Exception{
        if (db != null && !TextUtils.isEmpty(tableName)) {
            String dbName = isTemp ? SQLITE_TEMP_MASTER : SQLITE_MASTER;
            String sql = "SELECT COUNT(*) FROM " + dbName + " WHERE type = ? AND name = ?";
            Cursor cursor = null;
            int count = 0;

            try {
                cursor = db.rawQuery(sql, new String[]{"table", tableName});
                if (cursor == null || !cursor.moveToFirst()) {
                    boolean e = false;
                    return e;
                }
                count = cursor.getInt(0);
            } catch (Exception var11) {
               throw var11;
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            return count > 0;
        } else {
            return false;
        }
    }

    /**
     * 调试日志输出
     *
     * @param info
     */
    private static void printLog(String info) {
        if (DEBUG) {
            Log.d(TAG, info);
        }

    }

    /**
     * 获得父类泛型的实际类型
     * @param cla
     * @return
     */
    public static java.lang.reflect.Type[] getSuperClassParameterizedType(Class cla) {
        /**
         * getGenericSuperclass() 获取带有泛型的父类
         * Type 是 java 编译语言中所有类型的公共高级接口，它们包括 原始类型，参数化类型，数组类型，类型变量和基本类型
         */
        java.lang.reflect.Type type = cla.getGenericSuperclass();

        /**
         * ParameterizedType参数化类型，即泛型
         */
        ParameterizedType p = (ParameterizedType) type;
        /**
         * getActualTypeArguments获取参数化类型的数组，泛型可能有多个
         */
        java.lang.reflect.Type[] c = p.getActualTypeArguments();
        return c;

    }

    /**
     * <pre>
     * GreenDao 升级处理接口完成旧数据到新Bean类的兼容转换
     * 例如：
     *
     * @Entity
     * public class AppWifiInfo implements GreenDaoUpdateUtils.Update{
     *      ...........
     *
     *      @Override
     *      public void updateOne(Map<String, Object> oneRow ) {
     *          this.setUp("up"); //新版新增字段，设置默认值
     *          this.setId(null); //id 的设置
     *          this.setKEYMGMT((String) oneRow.get(AppWifiInfoDao.Properties.KEYMGMT.name));//旧版数据赋值新版对应字段
     *          this.setSSID((String) oneRow.get(AppWifiInfoDao.Properties.SSID.name));//旧版数据赋值新版对应字段
     *          this.setPASSWORD((String) oneRow.get(AppWifiInfoDao.Properties.PASSWORD.name));//旧版数据赋值新版对应字段
     *          this.setLASTTIME((String) oneRow.get(AppWifiInfoDao.Properties.LASTTIME.name));//旧版数据赋值新版对应字段
     *      }
     * }
     * 经过updateOne()后，该实例会被插入到新版数据库中，务必保证updateOne()后，该实例兼容新版数据库
     *
     * </pre>
     */
    public interface Update {
        /**
         * <pre>
         * 参数为数据库中每一行所有列的集合
         * 每一列的数据类型请参阅sqlite的数据类型
         *
         * @param oneRow 数据库中每一行的数据（旧版数据）Map<String, Object>保存列名（key）与对应列的值（values）
         * </pre>
         */
        void updateOne(Map<String, Object> oneRow);
    }
}
