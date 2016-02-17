package com.kplus.car.comm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	public static final String DB_NAME = "kplus_car";
	public static final String TABLE_NAME_VEHICLE = "vehicle";
	public static final String TABLE_NAME_AGAINST_RECORDS = "against_records";
    public static final String TABLE_NAME_REMIND_INFO = "remind_info";
    public static final String TABLE_NAME_REMIND_JIAZHAOFEN = "remind_jiazhaofen";
    public static final String TABLE_NAME_REMIND_CHEXIAN = "remind_chexian";
    public static final String TABLE_NAME_REMIND_NIANJIAN = "remind_nianjian";
    public static final String TABLE_NAME_REMIND_BAOYANG = "remind_baoyang";
    public static final String TABLE_NAME_REMIND_CHEDAI = "remind_chedai";
    public static final String TABLE_NAME_REMIND_CUSTOM = "remind_custom";
	public static final String TABLE_NAME_REMIND_RESTRICT = "remind_restrict";
    public static final String TABLE_NAME_BAOYANG_RECORD = "baoyang_record";
	public static final String TABLE_NAME_BAOYANG_ITEM = "baoyang_item";
	public static final String TABLE_NAME_JIAZHAO = "jiazhao";
	public static final String TABLE_NAME_JIAZHAO_AGAINST = "jiazhao_against";
	public static final String TABLE_NAME_CITY = "city";
	public static final String TABLE_NAME_WEATHER = "weather";
//	public static final String TABLE_NAME_FRIEND = "friend";
	public static final String SYS_TEMP = "sys_temp";
	public static final String FLASH_IMAGES = "flash_images";
	public static final String HOME_PAGE_IMAGES = "home_page_images";
	public static final String HOME_ACTIVITY_IMAGES = "home_activity_images";
	public static final String TAB_INFO = "tab_info";
	public static final String TABEL_USER_INFO = "user_info";
	public static final String BODY_IMAGES = "body_images";
	public static final String VEHICLE_HEAD_IMAGES = "vehicle_head_images";
	public static final String UPGRADE_COMPONENT_INFOS = "upgrade_component_infos";
	public static final String VEHICLE_AUTHENTICATION_INFOS = "vehicle_authentication_infos";
	public static final String THIRD_PART_ACCOUNTS = "third_part_accounts";
	public static final String DAZE_USER_ACCOUNT = "daze_user_account";
	public static final String DAZE_VEHICLE_MODE_SELF_DEFINE = "daze_vehicle_mode_self_define";
	public static final String DAZE_NOTICES = "daze_notices";
	public static final String DAZE_ACTIVITIES = "daze_activities";
	public static final String DAZE_VEHICLE_TYPES = "daze_vehicle_types";
	public static final String DAZE_HISTORY_FRAME_AND_MOTOR = "daze_history_frame_and_motor";
    public static final String TABEL_NAME_DAZE_PROVIDER_INFO = "daze_provider_info";
	public static final String TABEL_NAME_DAZE_ORDER_PAYING = "daze_order_paying";
	public static final String TABLE_NAME_SERVICE_GROUPS = "car_service_groups";
	public static final String TABLE_NAME_SERVICES = "car_services";
	public static final String TABLE_NAME_REQUEST_INFO = "request_info";
	public static final int V = 31;

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, V);
	}

	public void onCreate(SQLiteDatabase db) {

//		String sql = "CREATE TABLE "
//				+ TABLE_NAME_VEHICLE
//				+ "(id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT, vehicleNum TEXT NOT NULL, cityId TEXT NOT NULL, cityName TEXT NOT NULL, motorNum TEXT, frameNum TEXT, vehicleModelId Long, modelName TEXT, picUrl TEXT, descr TEXT, score INTEGER, money INTEGER, newNum INTEGER, oldNum INTEGER)";
		String sql = "CREATE TABLE "
				+ TABLE_NAME_VEHICLE
				+ "(id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT, vehicleNum TEXT NOT NULL,"
				+ " cityId TEXT NOT NULL, cityName TEXT NOT NULL, motorNum TEXT, frameNum TEXT, vehicleModelId Long,"
				+ " modelName TEXT, picUrl TEXT, descr TEXT, score INTEGER, money INTEGER, newNum INTEGER,"
				+ " oldNum INTEGER, hide TEXT, canDeal INTEGER, url TEXT, maxFrameNum TEXT, maxMotorNum TEXT,"
				+ " updateTime TEXT, hasRuleError TEXT, hasParamError TEXT, cityUnValid TEXT, requestTime LONG,"
				+ " returnTime LONG, account TEXT, password TEXT, vehicleType INTEGER, issueDate TEXT, regDate TEXT,"
				+ " vehicleOwner TEXT, vehicleRegCerNo TEXT, hasSearchFail INTEGER, newRecordNumber INTEGER,"
				+ " nianjianDate TEXT, jiazhaofenDate TEXT, company TEXT, phone TEXT, restrictNum TEXT, restrictRegion TEXT, restrictNums TEXT,"
				+ " ownerId TEXT, driverName TEXT, driverId TEXT)";
		db.execSQL(sql);
		sql = "CREATE TABLE "
				+ TABLE_NAME_AGAINST_RECORDS
				+ "(id INTEGER, vehicleNum TEXT, cityId Long, cityName TEXT, address TEXT, behavior TEXT, time TEXT,"
				+ " score INTEGER, money INTEGER, status INTEGER, lat DOUBLE, lng DOUBLE, orderStatus INTEGER,"
				+ " canSubmit INTEGER, pId Long, orderId Long, selfProcess INTEGER, resulttype INTEGER,"
				+ " paymentstatus INTEGER, dataSourceTitle TEXT,orderCode TEXT, ordertime TEXT, recordType INTEGER,"
				+ " img_0 TEXT, img_1 TEXT, img_2 TEXT, img_3 TEXT, img_4 TEXT, selfScore INTEGER, selfMoney INTEGER)";
		db.execSQL(sql);
		sql = "CREATE TABLE "
				+ TABLE_NAME_CITY
				+ "(id INTEGER, province TEXT, name TEXT, prefix TEXT, motorNumLen INTEGER, frameNumLen INTEGER,"
				+ " PY TEXT, valid INTEGER, hot INTEGER, owner INTEGER, accountLen INTEGER, passwordLen INTEGER,"
				+ " motorvehiclenumLen INTEGER, ownerIdNoLen INTEGER, drivingLicenseName INTEGER, drivingLicenseNoLen INTEGER, fieldComment TEXT)";
		db.execSQL(sql);
//		sql = "CREATE TABLE " + TABLE_NAME_FRIEND
//				+ "(id INTEGER, name TEXT, phone TEXT)";
//		db.execSQL(sql);
		sql = "CREATE TABLE "
				+ SYS_TEMP
				+ "(id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT, key LONG, value TEXT)";
		db.execSQL(sql);
		sql = "CREATE TABLE "
				+ FLASH_IMAGES
				+ "(imgUrl TEXT, actionType TEXT, actionValue TEXT, orderId INTEGER, imagePath TEXT, valid TEXT)";
		db.execSQL(sql);
		sql = "CREATE TABLE "
				+ HOME_PAGE_IMAGES
				+ "(imgUrl TEXT, actionType TEXT, actionValue TEXT, orderId INTEGER, imagePath TEXT, valid TEXT)";
		db.execSQL(sql);
		sql = "CREATE TABLE " + TAB_INFO + "(identity TEXT, name TEXT, description TEXT, orderId INTEGER, isValid INTEGER)";
		db.execSQL(sql);
		sql = "CREATE TABLE " + TABEL_USER_INFO + "(uid Long, iconUrl TEXT, name TEXT, sex INTEGER, address TEXT, info TEXT, cashBalance FLOAT, coupon INTEGER, orderCount INTEGER)";
		db.execSQL(sql);
		sql = "CREATE TABLE "
				+ BODY_IMAGES
				+ "(imgUrl TEXT, actionType TEXT, actionValue TEXT, orderId INTEGER, imagePath TEXT, valid TEXT)";
		db.execSQL(sql);
		sql = "CREATE TABLE "
				+ VEHICLE_HEAD_IMAGES
				+ "(imgUrl TEXT, actionType TEXT, actionValue TEXT, orderId INTEGER, imagePath TEXT, valid TEXT)";
		db.execSQL(sql);
		sql = "CREATE TABLE "
				+ UPGRADE_COMPONENT_INFOS
				+ "(comId TEXT, hasNew TEXT, lazy TEXT, downloadUrl TEXT, time TEXT)";
		db.execSQL(sql);
		sql = "CREATE TABLE "
				+ VEHICLE_AUTHENTICATION_INFOS
				+ "(authId LONG, vehicleNum TEXT, status INTEGER, belong TEXT, drivingLicenceUrl TEXT, authDatetime TEXT, owner TEXT, brandModel TEXT, motorNum TEXT, frameNum TEXT, issueDate TEXT, residueDegree INTEGER, descr TEXT, adjustDate TEXT)";
		db.execSQL(sql);
		sql = "CREATE TABLE "
				+ THIRD_PART_ACCOUNTS
				+ "(userName TEXT, nickname TEXT, type INTEGER, pid LONG)";
		db.execSQL(sql);
		sql = "CREATE TABLE "
				+ DAZE_USER_ACCOUNT
				+ "(uid LONG, nickName TEXT, phoneNumber TEXT, phoneLoginName TEXT, qqLoginName TEXT, wechatLoginName TEXT, weiboLoginName TEXT, creatDatetime TEXT, qqCreatDatetime TEXT, wechatCreatDatetime TEXT, weiboCreatDatetime TEXT, status INTEGER)";
		db.execSQL(sql);
		sql = "CREATE TABLE "
				+ DAZE_VEHICLE_MODE_SELF_DEFINE
				+ "(modeId LONG, name TEXT, image TEXT, classfy TEXT, brandId LONG)";
		db.execSQL(sql);
		sql = "CREATE TABLE "
				+ DAZE_NOTICES
				+ "(noticeId LONG, content TEXT, url TEXT, noticeTime TEXT, readflag INTEGER, title TEXT, titleImg TEXT, type INTEGER, motionType TEXT, motionValue TEXT)";
		db.execSQL(sql);
		sql = "CREATE TABLE "
				+ DAZE_ACTIVITIES
				+ "(valid INTEGER, link TEXT, img TEXT, content TEXT, title TEXT, summary TEXT, startTime TEXT, endTime TEXT)";
		db.execSQL(sql);
		sql = "CREATE TABLE "
				+ DAZE_VEHICLE_TYPES
				+ "(typeId LONG, type TEXT, value TEXT)";
		db.execSQL(sql);
		sql = "CREATE TABLE "
				+ DAZE_HISTORY_FRAME_AND_MOTOR
				+ "(vehicleNum TEXT, historyFrame TEXT, historyMotor TEXT)";
		db.execSQL(sql);
        sql = "CREATE TABLE "
                + TABLE_NAME_REMIND_INFO
                + "(id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT, vehicleNum TEXT, title TEXT, desc TEXT, icon INTEGER, type INTEGER, isHidden TEXT, position INTEGER)";
        db.execSQL(sql);
        sql = "CREATE TABLE "
                + TABLE_NAME_REMIND_JIAZHAOFEN
                + "(id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT, vehicleNum TEXT, score INTEGER, date TEXT, remindDate1 TEXT, remindDate2 TEXT, repeatType INTEGER, orignalDate TEXT, name TEXT, zhengjianhao TEXT)";
        db.execSQL(sql);
        sql = "CREATE TABLE "
                + TABLE_NAME_REMIND_CHEXIAN
                + "(id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT, vehicleNum TEXT, type INTEGER, company TEXT, phone TEXT, money INTEGER, baodanhao TEXT, photo TEXT, date TEXT, remindDate1 TEXT, remindDate2 TEXT, repeatType INTEGER, orignalDate TEXT, same INTEGER, fromType INTEGER)";
        db.execSQL(sql);
        sql = "CREATE TABLE "
                + TABLE_NAME_REMIND_NIANJIAN
                + "(id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT, vehicleNum TEXT, date TEXT, remindDate1 TEXT, remindDate2 TEXT, repeatType INTEGER, orignalDate TEXT, fromType INTEGER)";
        db.execSQL(sql);
        sql = "CREATE TABLE "
                + TABLE_NAME_REMIND_BAOYANG
                + "(id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT, vehicleNum TEXT, date TEXT, remindDate1 TEXT, remindDate2 TEXT, repeatType INTEGER, orignalDate TEXT, licheng INTEGER, jiange INTEGER)";
        db.execSQL(sql);
        sql = "CREATE TABLE "
                + TABLE_NAME_REMIND_CHEDAI
                + "(id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT, vehicleNum TEXT, date TEXT, remindDate1 TEXT, remindDate2 TEXT, repeatType INTEGER, orignalDate TEXT, money INTEGER, total INTEGER, fenshu INTEGER, remark TEXT, dayOfMonth INTEGER)";
        db.execSQL(sql);
        sql = "CREATE TABLE "
                + TABLE_NAME_REMIND_CUSTOM
                + "(id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT, vehicleNum TEXT, date TEXT, remindDate1 TEXT, remindDate2 TEXT, repeatType INTEGER, orignalDate TEXT, name TEXT, location TEXT, remark TEXT)";
        db.execSQL(sql);
        sql = "CREATE TABLE "
                + TABLE_NAME_BAOYANG_RECORD
                + "(id INTEGER, vehicleNum TEXT, date TEXT, licheng INTEGER, money INTEGER, company TEXT, phone TEXT, remark TEXT, baoyangItemId TEXT, baoyangItem TEXT, lat TEXT, lon TEXT, address TEXT)";
        db.execSQL(sql);
		sql = "CREATE TABLE "
				+ TABLE_NAME_JIAZHAO
				+ "(dbid INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT, id TEXT, jszh TEXT, xm TEXT, startTime TEXT, isHidden TEXT, space INTEGER, dabh TEXT, date TEXT, remindDate TEXT, score INTEGER, zjcx TEXT)";
		db.execSQL(sql);
        sql = "CREATE TABLE "
                + TABEL_NAME_DAZE_PROVIDER_INFO
                + "(openUserId TEXT, name TEXT, image TEXT)";
        db.execSQL(sql);
		sql = "CREATE TABLE "
				+ TABLE_NAME_JIAZHAO_AGAINST
				+ "(vehicleNum TEXT, total INTEGER)";
		db.execSQL(sql);
		// baoyang_item
		sql = "CREATE TABLE "
				+ TABLE_NAME_BAOYANG_ITEM
				+ "(id INTEGER, item TEXT, type INTEGER, createTime TEXT, updateTime TEXT, isDelete INTEGER, uid INTEGER)";
		db.execSQL(sql);
		sql = "CREATE TABLE "
				+ TABLE_NAME_REMIND_RESTRICT
				+ "(dbid INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT, id TEXT, isHidden TEXT, vehicleNum TEXT, remindDateType TEXT, remindDate TEXT, phone TEXT, messageRemind TEXT, vehicleCityId TEXT)";
		db.execSQL(sql);
		sql = "CREATE TABLE "
				+ TABLE_NAME_WEATHER
				+ "(phenomenon TEXT, temperatureDay TEXT, wash INTEGER, temperatureNight TEXT, type INTEGER, washDescr TEXT, dateStr TEXT)";
		db.execSQL(sql);
		sql = "CREATE TABLE "
				+ TABEL_NAME_DAZE_ORDER_PAYING
				+ "(orderId LONG)";
		db.execSQL(sql);
		// 汽车服务表
		createCarServicesTable(db);
		sql = "CREATE TABLE "
				+ TABLE_NAME_REQUEST_INFO
				+ "(id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT, code TEXT, status TEXT, uid LONG, version INTEGER, joinInNum INTEGER, createTime LONG, modifyTime LONG, providerId TEXT, serviceName TEXT, serviceType INTEGER, openUserId TEXT, telPhone TEXT, price FLOAT, notifyNum INTEGER, expectTime LONG, extendsion TEXT, nickName TEXT)";
		db.execSQL(sql);
	}

	private void createCarServicesTable(SQLiteDatabase db) {
		// 汽车服务
		String sql = "CREATE TABLE "
				+ TABLE_NAME_SERVICE_GROUPS
				+ "(id INTEGER NOT NULL, name TEXT, system BOOLEAN, showName BOOLEAN, indexServiceCount INTEGER, serviceCount INTEGER, sort INTEGER, cityId LONG)";
		db.execSQL(sql);
		sql = "CREATE TABLE "
				+ TABLE_NAME_SERVICES
				+ "(id INTEGER NOT NULL, name TEXT, title TEXT, info TEXT, flag INTEGER, favorableTag TEXT, listIcon TEXT, linkIcon TEXT, tabIcon TEXT, motionType TEXT, motionValue TEXT, transitionUrl TEXT, motionValueRelation TEXT, sort INTEGER, groupId INTEGER, cityId LONG)";
		db.execSQL(sql);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql;
		switch (oldVersion) {
		case 1:
			sql = "ALTER TABLE against_records RENAME TO against_records_temp";
			db.execSQL(sql);
			sql = "CREATE TABLE against_records (id INTEGER, vehicleNum TEXT, cityId Long, cityName TEXT, address TEXT, behavior TEXT, time TEXT, score INTEGER, money INTEGER, status INTEGER, lat DOUBLE, lng DOUBLE, orderStatus INTEGER, canSubmit INTEGER, pId Long, orderId Long)";
			db.execSQL(sql);
			sql = "INSERT INTO against_records(id,vehicleNum,cityId,cityName,address,behavior,time,score,money,status,lat,lng,orderStatus,canSubmit,pId,orderId) SELECT id,vehicleNum,cityId,cityName,address,behavior,time,score,money,status,lat,lng,0,0,0,0 FROM against_records_temp";
			db.execSQL(sql);
			sql = "update against_records set orderStatus = 0, canSubmit = 0";
			db.execSQL(sql);
		case 2:
			sql = "CREATE TABLE "
					+ SYS_TEMP
					+ "(id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT, key Long, value TEXT)";
			db.execSQL(sql);
//			sql = "CREATE TABLE " + TABLE_NAME_FRIEND + "(id INTEGER, name TEXT, phone TEXT)";
//			db.execSQL(sql);
		case 3:
			sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD hide TEXT NULL";
			db.execSQL(sql);
		case 4:
			sql = "CREATE TABLE "
					+ FLASH_IMAGES
					+ "(imgUrl TEXT, actionType TEXT, actionValue TEXT, orderId INTEGER, imagePath TEXT)";
			db.execSQL(sql);
			sql = "CREATE TABLE "
					+ HOME_PAGE_IMAGES
					+ "(imgUrl TEXT, actionType TEXT, actionValue TEXT, orderId INTEGER, imagePath TEXT)";
			db.execSQL(sql);
		case 5:
			sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD canDeal INTEGER";
			db.execSQL(sql);
			sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD url TEXT NULL";
			db.execSQL(sql);
			sql = "CREATE TABLE " + TAB_INFO + "(identity TEXT, name TEXT, description TEXT, orderId INTEGER, isValid INTEGER)";
			db.execSQL(sql);
		case 6:
			sql = "CREATE TABLE " + TABEL_USER_INFO + "(uid Long, iconUrl TEXT, name TEXT, sex INTEGER, address TEXT, info TEXT, cashBalance FLOAT, coupon INTEGER, orderCount INTEGER)";
			db.execSQL(sql);
		case 7:
			sql = "ALTER TABLE " + FLASH_IMAGES + " ADD valid TEXT";
			db.execSQL(sql);
			sql = "ALTER TABLE " + HOME_PAGE_IMAGES + " ADD valid TEXT";
			db.execSQL(sql);
			sql = "CREATE TABLE "
					+ BODY_IMAGES
					+ "(imgUrl TEXT, actionType TEXT, actionValue TEXT, orderId INTEGER, imagePath TEXT, valid TEXT)";
			db.execSQL(sql);
			sql = "CREATE TABLE "
					+ VEHICLE_HEAD_IMAGES
					+ "(imgUrl TEXT, actionType TEXT, actionValue TEXT, orderId INTEGER, imagePath TEXT, valid TEXT)";
			db.execSQL(sql);
		case 8:
		case 9:
			sql = "CREATE TABLE "
					+ UPGRADE_COMPONENT_INFOS
					+ "(comId TEXT, hasNew TEXT, lazy TEXT, downloadUrl TEXT, time TEXT)";
			db.execSQL(sql);
		case 10:
			sql = "CREATE TABLE "
					+ VEHICLE_AUTHENTICATION_INFOS
					+ "(authId LONG, vehicleNum TEXT, status INTEGER, belong TEXT, drivingLicenceUrl TEXT, authDatetime TEXT, owner TEXT, brandModel TEXT, motorNum TEXT, frameNum TEXT, issueDate TEXT, residueDegree INTEGER, descr TEXT, adjustDate TEXT)";
			db.execSQL(sql);
			sql = "CREATE TABLE "
					+ THIRD_PART_ACCOUNTS
					+ "(userName TEXT, nickname TEXT, type INTEGER, pid LONG)";
			db.execSQL(sql);
		case 11:
			sql = "CREATE TABLE "
					+ DAZE_USER_ACCOUNT
					+ "(uid LONG, nickName TEXT, phoneNumber TEXT, phoneLoginName TEXT, qqLoginName TEXT, wechatLoginName TEXT, weiboLoginName TEXT, creatDatetime TEXT, qqCreatDatetime TEXT, wechatCreatDatetime TEXT, weiboCreatDatetime TEXT, status INTEGER)";
			db.execSQL(sql);
		case 12:
			sql = "CREATE TABLE "
					+ DAZE_VEHICLE_MODE_SELF_DEFINE
					+ "(modeId LONG, name TEXT, image TEXT, classfy TEXT, brandId LONG)";
			db.execSQL(sql);
			sql = "ALTER TABLE " + TABLE_NAME_AGAINST_RECORDS + " ADD selfProcess INTEGER";
			db.execSQL(sql);
			sql = "CREATE TABLE "
					+ DAZE_NOTICES
					+ "(noticeId LONG, content TEXT, url TEXT, noticeTime TEXT, readflag INTEGER)";
			db.execSQL(sql);
		case 13:
			sql = "CREATE TABLE "
					+ DAZE_ACTIVITIES
					+ "(valid INTEGER, link TEXT, img TEXT, content TEXT, title TEXT, summary TEXT, startTime TEXT, endTime TEXT)";
			db.execSQL(sql);
		case 14:
			try{
				sql = "ALTER TABLE " + TABLE_NAME_CITY + " ADD valid INTEGER";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_CITY + " ADD hot INTEGER";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD updateTime TEXT";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD maxFrameNum TEXT";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD maxMotorNum TEXT";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD hasRuleError TEXT";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD hasParamError TEXT";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD cityUnValid TEXT";
				db.execSQL(sql);
			}catch(Exception e){
				e.printStackTrace();
			}
		case 15:
			try{
				sql = "ALTER TABLE " + TABLE_NAME_CITY + " ADD valid INTEGER";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_CITY + " ADD hot INTEGER";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD updateTime TEXT";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD maxFrameNum TEXT";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD maxMotorNum TEXT";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD hasRuleError TEXT";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD hasParamError TEXT";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD cityUnValid TEXT";
				db.execSQL(sql);
				}catch(Exception e){
					e.printStackTrace();
				}
		case 16:
			try{
				sql = "ALTER TABLE " + TABLE_NAME_AGAINST_RECORDS + " ADD resulttype INTEGER";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_AGAINST_RECORDS + " ADD paymentstatus INTEGER";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_AGAINST_RECORDS + " ADD dataSourceTitle TEXT";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_AGAINST_RECORDS + " ADD orderCode TEXT";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_AGAINST_RECORDS + " ADD ordertime TEXT";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_AGAINST_RECORDS + " ADD recordType INTEGER";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_AGAINST_RECORDS + " ADD  img_0 TEXT";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_AGAINST_RECORDS + " ADD  img_1 TEXT";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_AGAINST_RECORDS + " ADD  img_2 TEXT";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_AGAINST_RECORDS + " ADD  img_3 TEXT";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_AGAINST_RECORDS + " ADD  img_4 TEXT";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_AGAINST_RECORDS + " ADD selfScore INTEGER";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_AGAINST_RECORDS + " ADD selfMoney INTEGER";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD requestTime LONG";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD returnTime LONG";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD account TEXT";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD password TEXT";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD vehicleType INTEGER";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD issueDate TEXT";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD regDate TEXT";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD vehicleOwner TEXT";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD vehicleRegCerNo TEXT";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD hasSearchFail INTEGER";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD newRecordNumber INTEGER";
				db.execSQL(sql);
				sql = "CREATE TABLE "
						+ DAZE_VEHICLE_TYPES
						+ "(typeId LONG, type TEXT, value TEXT)";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_CITY + " ADD owner INTEGER";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_CITY + " ADD accountLen INTEGER";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_CITY + " ADD passwordLen INTEGER";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_CITY + " ADD motorvehiclenumLen INTEGER";
				db.execSQL(sql);
				sql = "CREATE TABLE "
						+ DAZE_HISTORY_FRAME_AND_MOTOR
						+ "(vehicleNum TEXT, historyFrame TEXT, historyMotor TEXT)";
				db.execSQL(sql);
			}catch(Exception e){
				e.printStackTrace();
			}
        case 17:
            try {
                sql = "CREATE TABLE "
                        + TABLE_NAME_REMIND_INFO
                        + "(id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT, vehicleNum TEXT, title TEXT, desc TEXT, icon INTEGER, type INTEGER, isHidden TEXT)";
                db.execSQL(sql);
                sql = "CREATE TABLE "
                        + TABLE_NAME_REMIND_JIAZHAOFEN
                        + "(id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT, vehicleNum TEXT, score INTEGER, date TEXT, remindDate1 TEXT, remindDate2 TEXT, repeatType INTEGER, orignalDate TEXT, name TEXT, zhengjianhao TEXT)";
                db.execSQL(sql);
                sql = "CREATE TABLE "
                        + TABLE_NAME_REMIND_CHEXIAN
                        + "(id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT, vehicleNum TEXT, type INTEGER, company TEXT, phone TEXT, money INTEGER, baodanhao TEXT, photo TEXT, date TEXT, remindDate1 TEXT, remindDate2 TEXT, repeatType INTEGER, orignalDate TEXT, same INTEGER)";
                db.execSQL(sql);
                sql = "CREATE TABLE "
                        + TABLE_NAME_REMIND_NIANJIAN
                        + "(id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT, vehicleNum TEXT, date TEXT, remindDate1 TEXT, remindDate2 TEXT, repeatType INTEGER, orignalDate TEXT)";
                db.execSQL(sql);
                sql = "CREATE TABLE "
                        + TABLE_NAME_REMIND_BAOYANG
                        + "(id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT, vehicleNum TEXT, date TEXT, remindDate1 TEXT, remindDate2 TEXT, repeatType INTEGER, orignalDate TEXT, licheng INTEGER, jiange INTEGER)";
                db.execSQL(sql);
                sql = "CREATE TABLE "
                        + TABLE_NAME_REMIND_CHEDAI
                        + "(id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT, vehicleNum TEXT, date TEXT, remindDate1 TEXT, remindDate2 TEXT, repeatType INTEGER, orignalDate TEXT, money INTEGER, total INTEGER, fenshu INTEGER, remark TEXT, dayOfMonth INTEGER)";
                db.execSQL(sql);
                sql = "CREATE TABLE "
                        + TABLE_NAME_REMIND_CUSTOM
                        + "(id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT, vehicleNum TEXT, date TEXT, remindDate1 TEXT, remindDate2 TEXT, repeatType INTEGER, orignalDate TEXT, name TEXT, location TEXT, remark TEXT)";
                db.execSQL(sql);
                sql = "CREATE TABLE "
                        + TABLE_NAME_BAOYANG_RECORD
                        + "(id INTEGER, vehicleNum TEXT, date TEXT, licheng INTEGER, money INTEGER, company TEXT, phone TEXT, remark TEXT)";
                db.execSQL(sql);
                sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD nianjianDate TEXT ";
                db.execSQL(sql);
                sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD jiazhaofenDate TEXT ";
                db.execSQL(sql);
            }catch(Exception e){
                e.printStackTrace();
            }
        case 18:
            try {
                sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD company TEXT ";
                db.execSQL(sql);
                sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD phone TEXT ";
                db.execSQL(sql);
            }catch(Exception e){
                e.printStackTrace();
            }
		case 19:
			try {
				sql = "CREATE TABLE "
						+ TABLE_NAME_JIAZHAO
						+ "(id TEXT, jszh TEXT, xm TEXT, startTime TEXT, isHidden TEXT, space INTEGER, dabh TEXT, date TEXT, remindDate TEXT)";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_REMIND_NIANJIAN + " ADD fromType INTEGER ";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_REMIND_CHEXIAN + " ADD fromType INTEGER ";
				db.execSQL(sql);
			}catch(Exception e){
				e.printStackTrace();
			}
		case 20:
			try {
                sql = "CREATE TABLE "
                        + TABEL_NAME_DAZE_PROVIDER_INFO
                        + "(openUserId TEXT, name TEXT, image TEXT)";
                db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_JIAZHAO + " ADD score INTEGER ";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_JIAZHAO + " ADD dbid INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT ";
				db.execSQL(sql);
				sql = "CREATE TABLE "
						+ TABLE_NAME_JIAZHAO_AGAINST
						+ "(vehicleNum TEXT, total INTEGER)";
				db.execSQL(sql);
			}catch(Exception e){
				e.printStackTrace();
			}
		case 21:
			try {
				sql = "CREATE TABLE "
						+ TABLE_NAME_JIAZHAO_AGAINST
						+ "(vehicleNum TEXT, total INTEGER)";
				db.execSQL(sql);
				sql = "DROP TABLE IF EXISTS " + TABLE_NAME_JIAZHAO;
				db.execSQL(sql);
				sql = "CREATE TABLE "
						+ TABLE_NAME_JIAZHAO
						+ "(dbid INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT, id TEXT, jszh TEXT, xm TEXT, startTime TEXT, isHidden TEXT, space INTEGER, dabh TEXT, date TEXT, remindDate TEXT, score INTEGER)";
				db.execSQL(sql);
			}catch(Exception e){
				e.printStackTrace();
			}
		case 22:
			try {
				sql = "ALTER TABLE " + TABLE_NAME_REMIND_INFO + " ADD position INTEGER ";
				db.execSQL(sql);
			}catch(Exception e){
				e.printStackTrace();
			}
		case 23:
			try {
				sql = "ALTER TABLE " + DAZE_NOTICES + " ADD title TEXT ";
				db.execSQL(sql);
				sql = "ALTER TABLE " + DAZE_NOTICES + " ADD titleImg TEXT ";
				db.execSQL(sql);
				sql = "ALTER TABLE " + DAZE_NOTICES + " ADD type INTEGER ";
				db.execSQL(sql);
			}catch(Exception e){
				e.printStackTrace();
			}
		case 24:
			try {
				sql = "ALTER TABLE " + TABLE_NAME_JIAZHAO + " ADD zjcx TEXT ";
				db.execSQL(sql);
			}catch(Exception e){
				e.printStackTrace();
			}
		case 25:
			try {
				sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD restrictNum TEXT ";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD restrictRegion TEXT ";
				db.execSQL(sql);
				sql = " ALTER TABLE " + TABLE_NAME_BAOYANG_RECORD + " ADD baoyangItemId TEXT ";
				db.execSQL(sql);
				sql = " ALTER TABLE " + TABLE_NAME_BAOYANG_RECORD + " ADD baoyangItem TEXT ";
				db.execSQL(sql);
				// baoyang_item
				sql = "CREATE TABLE "
						+ TABLE_NAME_BAOYANG_ITEM
						+ "(id INTEGER, item TEXT, type INTEGER, createTime TEXT, updateTime TEXT, isDelete INTEGER, uid INTEGER)";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD restrictNums TEXT ";
				db.execSQL(sql);
				sql = "CREATE TABLE "
						+ TABLE_NAME_REMIND_RESTRICT
						+ "(dbid INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT, id TEXT, isHidden TEXT, vehicleNum TEXT, remindDateType TEXT, remindDate TEXT, phone TEXT, messageRemind TEXT, vehicleCityId TEXT)";
				db.execSQL(sql);
			}catch(Exception e){
				e.printStackTrace();
			}
		case 26:
			try {
				sql = "CREATE TABLE "
						+ TABLE_NAME_WEATHER
						+ "(phenomenon TEXT, temperatureDay TEXT, wash INTEGER, temperatureNight TEXT, type INTEGER, washDescr TEXT, dateStr TEXT)";
				db.execSQL(sql);
			}catch(Exception e){
				e.printStackTrace();
			}
		case 27:
			try {
				sql = "ALTER TABLE " + TABLE_NAME_BAOYANG_RECORD + " ADD lat TEXT ";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_BAOYANG_RECORD + " ADD lon TEXT ";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_BAOYANG_RECORD + " ADD address TEXT ";
				db.execSQL(sql);
			}catch(Exception e){
				e.printStackTrace();
			}
		case 28:
			sql = "CREATE TABLE "
					+ TABEL_NAME_DAZE_ORDER_PAYING
					+ "(orderId LONG)";
			db.execSQL(sql);
			// 汽车服务
			createCarServicesTable(db);
			sql = "CREATE TABLE "
					+ TABLE_NAME_REQUEST_INFO
					+ "(id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT, code TEXT, status TEXT, uid LONG, version INTEGER, joinInNum INTEGER, createTime LONG, modifyTime LONG, providerId TEXT, serviceName TEXT, serviceType INTEGER, openUserId TEXT, telPhone TEXT, price FLOAT, notifyNum INTEGER, expectTime LONG, extendsion TEXT, nickName TEXT)";
			db.execSQL(sql);
		case 29:
			try {
				sql = "ALTER TABLE " + TABLE_NAME_CITY + " ADD ownerIdNoLen INTEGER ";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_CITY + " ADD drivingLicenseName INTEGER ";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_CITY + " ADD drivingLicenseNoLen INTEGER ";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_CITY + " ADD fieldComment TEXT ";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD ownerId TEXT ";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD driverName TEXT ";
				db.execSQL(sql);
				sql = "ALTER TABLE " + TABLE_NAME_VEHICLE + " ADD driverId TEXT ";
				db.execSQL(sql);
			}catch(Exception e){
				e.printStackTrace();
			}
		case 30:
			try {
				sql = "ALTER TABLE " + DAZE_NOTICES + " ADD motionType TEXT ";
				db.execSQL(sql);
				sql = "ALTER TABLE " + DAZE_NOTICES + " ADD motionValue TEXT ";
				db.execSQL(sql);
			}catch(Exception e){
				e.printStackTrace();
			}
		default:
			break;
		}
	}
}
