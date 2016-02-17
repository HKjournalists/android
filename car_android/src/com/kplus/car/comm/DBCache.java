package com.kplus.car.comm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.model.Account;
import com.kplus.car.model.ActivityInfo;
import com.kplus.car.model.AgainstRecord;
import com.kplus.car.model.BaoyangItem;
import com.kplus.car.model.BaoyangRecord;
import com.kplus.car.model.CarService;
import com.kplus.car.model.CarServiceGroup;
import com.kplus.car.model.CityVehicle;
import com.kplus.car.model.CommonDictionary;
import com.kplus.car.model.FWRequestInfo;
import com.kplus.car.model.HistoryFrameAndMotor;
import com.kplus.car.model.ImageInfo;
import com.kplus.car.model.ImageNames;
import com.kplus.car.model.Jiazhao;
import com.kplus.car.model.JiazhaoAgainst;
import com.kplus.car.model.NoticeContent;
import com.kplus.car.model.ProviderInfo;
import com.kplus.car.model.RemindBaoyang;
import com.kplus.car.model.RemindChedai;
import com.kplus.car.model.RemindChexian;
import com.kplus.car.model.RemindCustom;
import com.kplus.car.model.RemindInfo;
import com.kplus.car.model.RemindNianjian;
import com.kplus.car.model.RemindRestrict;
import com.kplus.car.model.TabInfo;
import com.kplus.car.model.UpgradeComponent;
import com.kplus.car.model.UserInfo;
import com.kplus.car.model.UserVehicle;
import com.kplus.car.model.VehicleAuth;
import com.kplus.car.model.VehicleModel;
import com.kplus.car.model.Weather;
import com.kplus.car.util.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DBCache {
    private DatabaseHelper helper;
    private int nResult;

    public DBCache(Context context) {
        helper = new DatabaseHelper(context);
        if (Build.VERSION.SDK_INT >= 11)
            helper.getWritableDatabase().enableWriteAheadLogging();
    }

    public synchronized String getValue(final String key) {
        final StringBuilder value = new StringBuilder();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.SYS_TEMP, new String[]{"value"}, " key = ? ", new String[]{key}, null, null, null);
                    if (cur.moveToNext()) {
                        value.append(cur.getString(cur.getColumnIndex("value")));
                    }
                    cur.close();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (!StringUtils.isEmpty(value.toString()))
            return value.toString();
        return null;
    }

    public void putValue(final String key, final String value) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.SYS_TEMP, new String[]{"value"}, " key = ? ", new String[]{key}, null, null, null);
                    if (cur.getCount() > 0) {
                        database.delete(DatabaseHelper.SYS_TEMP, " key = ? ", new String[]{key});
                    }
                    ContentValues root = new ContentValues();
                    root.put("key", key);
                    root.put("value", value);
                    database.insert(DatabaseHelper.SYS_TEMP, "id", root);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
    }

    public void saveUserVehicles(final List<UserVehicle> uservehicles) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    database.beginTransaction();
                    for (UserVehicle uv : uservehicles) {
                        String vehicleNum = uv.getVehicleNum();
                        cur = database.query(DatabaseHelper.TABLE_NAME_VEHICLE, new String[]{"id"}, " vehicleNum = ? ",
                                new String[]{vehicleNum}, null, null, null);
                        if (cur.getCount() > 0) {
                            database.delete(DatabaseHelper.TABLE_NAME_VEHICLE, " vehicleNum = ? ", new String[]{vehicleNum});
                        }
                        ContentValues root = new ContentValues();
                        root.put("vehicleNum", uv.getVehicleNum());
                        root.put("cityId", uv.getCityId());
                        root.put("cityName", uv.getCityName());
                        root.put("motorNum", uv.getMotorNum());
                        root.put("frameNum", uv.getFrameNum());
                        root.put("vehicleModelId", uv.getVehicleModelId());
                        root.put("modelName", uv.getModelName());
                        root.put("picUrl", uv.getPicUrl());
                        root.put("descr", uv.getDescr());
                        root.put("score", uv.getScore());
                        root.put("money", uv.getMoney());
                        root.put("newNum", uv.getNewNum());
                        root.put("oldNum", uv.getOldNum());
                        root.put("hide", uv.isHiden() ? "2" : "1");
                        root.put("canDeal", uv.getCanDeal());
                        root.put("url", uv.getUrl());
                        root.put("maxFrameNum", uv.getMaxFrameNum());
                        root.put("maxMotorNum", uv.getMaxMotorNum());
                        root.put("updateTime", uv.getUpdateTime() == null ? "" : uv.getUpdateTime());
                        root.put("hasRuleError", uv.isHasRuleError() ? "1" : "0");
                        root.put("hasParamError", uv.isHasParamError() ? "1" : "0");
                        root.put("cityUnValid", uv.getCityUnValid());
                        root.put("requestTime", uv.getRequestTime());
                        root.put("returnTime", uv.getReturnTime());
                        root.put("account", uv.getAccount());
                        root.put("password", uv.getPassword());
                        root.put("vehicleType", uv.getVehicleType());
                        root.put("issueDate", uv.getIssueDate());
                        root.put("regDate", uv.getRegDate());
                        root.put("vehicleOwner", uv.getVehicleOwner());
                        root.put("vehicleRegCerNo", uv.getVehicleRegCerNo());
                        root.put("hasSearchFail", uv.isHasSearchFail() ? 1 : 0);
                        root.put("newRecordNumber", uv.getNewRecordNumber());
                        root.put("jiazhaofenDate", uv.getJiazhaofenDate());
                        root.put("nianjianDate", uv.getNianjianDate());
                        root.put("company", uv.getCompany());
                        root.put("phone", uv.getPhone());
                        root.put("restrictNum", uv.getRestrictNum());
                        root.put("restrictRegion", uv.getRestrictRegion());
                        root.put("restrictNums", uv.getRestrictNums());
                        root.put("ownerId", uv.getOwnerId());
                        root.put("driverName", uv.getDriverName());
                        root.put("driverId", uv.getDriverId());
                        database.insert(DatabaseHelper.TABLE_NAME_VEHICLE, "id", root);
                    }
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (database != null)
                        database.endTransaction();
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
    }

    public void saveVehicle(final UserVehicle vehicle) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    String vehicleNum = vehicle.getVehicleNum();
                    cur = database.query(DatabaseHelper.TABLE_NAME_VEHICLE, new String[]{"id"}, " vehicleNum = ? ",
                            new String[]{vehicleNum}, null, null, null);
                    if (cur.getCount() > 0) {
                        database.delete(DatabaseHelper.TABLE_NAME_VEHICLE, " vehicleNum = ? ", new String[]{vehicleNum});
                    }
                    ContentValues root = new ContentValues();
                    root.put("vehicleNum", vehicle.getVehicleNum());
                    root.put("cityId", vehicle.getCityId());
                    root.put("cityName", vehicle.getCityName());
                    root.put("motorNum", vehicle.getMotorNum());
                    root.put("frameNum", vehicle.getFrameNum());
                    root.put("vehicleModelId", vehicle.getVehicleModelId());
                    root.put("modelName", vehicle.getModelName());
                    root.put("picUrl", vehicle.getPicUrl());
                    root.put("descr", vehicle.getDescr());
                    root.put("score", vehicle.getScore());
                    root.put("money", vehicle.getMoney());
                    root.put("newNum", vehicle.getNewNum());
                    root.put("oldNum", vehicle.getOldNum());
                    root.put("hide", vehicle.isHiden() ? "2" : "1");
                    root.put("canDeal", vehicle.getCanDeal());
                    root.put("url", vehicle.getUrl());
                    root.put("maxFrameNum", vehicle.getMaxFrameNum());
                    root.put("maxMotorNum", vehicle.getMaxMotorNum());
                    root.put("updateTime", vehicle.getUpdateTime() == null ? "" : vehicle.getUpdateTime());
                    root.put("hasRuleError", vehicle.isHasRuleError() ? "1" : "0");
                    root.put("hasParamError", vehicle.isHasParamError() ? "1" : "0");
                    root.put("cityUnValid", vehicle.getCityUnValid());
                    root.put("requestTime", vehicle.getRequestTime());
                    root.put("returnTime", vehicle.getReturnTime());
                    root.put("account", vehicle.getAccount());
                    root.put("password", vehicle.getPassword());
                    root.put("vehicleType", vehicle.getVehicleType());
                    root.put("issueDate", vehicle.getIssueDate());
                    root.put("regDate", vehicle.getRegDate());
                    root.put("vehicleOwner", vehicle.getVehicleOwner());
                    root.put("vehicleRegCerNo", vehicle.getVehicleRegCerNo());
                    root.put("hasSearchFail", vehicle.isHasSearchFail() ? 1 : 0);
                    root.put("newRecordNumber", vehicle.getNewRecordNumber());
                    root.put("jiazhaofenDate", vehicle.getJiazhaofenDate());
                    root.put("nianjianDate", vehicle.getNianjianDate());
                    root.put("company", vehicle.getCompany());
                    root.put("phone", vehicle.getPhone());
                    root.put("restrictNum", vehicle.getRestrictNum());
                    root.put("restrictRegion", vehicle.getRestrictRegion());
                    root.put("restrictNums", vehicle.getRestrictNums());
                    root.put("ownerId", vehicle.getOwnerId());
                    root.put("driverName", vehicle.getDriverName());
                    root.put("driverId", vehicle.getDriverId());
                    database.insert(DatabaseHelper.TABLE_NAME_VEHICLE, "id", root);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
    }

    public void updateVehicle(final UserVehicle vehicle) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    String[] args = {vehicle.getVehicleNum()};
                    ContentValues root = new ContentValues();
                    root.put("cityId", vehicle.getCityId());
                    root.put("cityName", vehicle.getCityName());
                    root.put("motorNum", vehicle.getMotorNum());
                    root.put("frameNum", vehicle.getFrameNum());
                    root.put("vehicleModelId", vehicle.getVehicleModelId());
                    root.put("modelName", vehicle.getModelName());
                    root.put("picUrl", vehicle.getPicUrl());
                    root.put("descr", vehicle.getDescr());
                    root.put("score", vehicle.getScore());
                    root.put("money", vehicle.getMoney());
                    root.put("newNum", vehicle.getNewNum());
                    root.put("oldNum", vehicle.getOldNum());
                    root.put("hide", vehicle.isHiden() ? "2" : "1");
                    root.put("canDeal", vehicle.getCanDeal());
                    root.put("url", vehicle.getUrl());
                    root.put("maxFrameNum", vehicle.getMaxFrameNum());
                    root.put("maxMotorNum", vehicle.getMaxMotorNum());
                    root.put("updateTime", vehicle.getUpdateTime() == null ? "" : vehicle.getUpdateTime());
                    root.put("hasRuleError", vehicle.isHasRuleError() ? "1" : "0");
                    root.put("hasParamError", vehicle.isHasParamError() ? "1" : "0");
                    root.put("cityUnValid", vehicle.getCityUnValid());
                    root.put("requestTime", vehicle.getRequestTime());
                    root.put("returnTime", vehicle.getReturnTime());
                    root.put("account", vehicle.getAccount());
                    root.put("password", vehicle.getPassword());
                    root.put("vehicleType", vehicle.getVehicleType());
                    root.put("issueDate", vehicle.getIssueDate());
                    root.put("regDate", vehicle.getRegDate());
                    root.put("vehicleOwner", vehicle.getVehicleOwner());
                    root.put("vehicleRegCerNo", vehicle.getVehicleRegCerNo());
                    root.put("hasSearchFail", vehicle.isHasSearchFail() ? 1 : 0);
                    root.put("newRecordNumber", vehicle.getNewRecordNumber());
                    root.put("jiazhaofenDate", vehicle.getJiazhaofenDate());
                    root.put("nianjianDate", vehicle.getNianjianDate());
                    root.put("company", vehicle.getCompany());
                    root.put("phone", vehicle.getPhone());
                    root.put("restrictNum", vehicle.getRestrictNum());
                    root.put("restrictRegion", vehicle.getRestrictRegion());
                    root.put("restrictNums", vehicle.getRestrictNums());
                    root.put("ownerId", vehicle.getOwnerId());
                    root.put("driverName", vehicle.getDriverName());
                    root.put("driverId", vehicle.getDriverId());
                    database.update(DatabaseHelper.TABLE_NAME_VEHICLE, root, " vehicleNum = ? ", args);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public UserVehicle getVehicle(final String vehicleNum) {
        final UserVehicle userVehicle = new UserVehicle();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_VEHICLE,
                            null, " vehicleNum = ? ",
                            new String[]{vehicleNum}, null, null, null);
                    if (cur.moveToNext()) {
                        userVehicle.setVehicleNum(cur.getString(cur.getColumnIndex("vehicleNum")));
                        userVehicle.setCityId(cur.getString(cur.getColumnIndex("cityId")));
                        userVehicle.setCityName(cur.getString(cur.getColumnIndex("cityName")));
                        userVehicle.setMotorNum(cur.getString(cur.getColumnIndex("motorNum")));
                        userVehicle.setFrameNum(cur.getString(cur.getColumnIndex("frameNum")));
                        userVehicle.setVehicleModelId(cur.getLong(cur.getColumnIndex("vehicleModelId")));
                        userVehicle.setModelName(cur.getString(cur.getColumnIndex("modelName")));
                        userVehicle.setPicUrl(cur.getString(cur.getColumnIndex("picUrl")));
                        userVehicle.setDescr(cur.getString(cur.getColumnIndex("descr")));
                        String hide = cur.getString(cur.getColumnIndex("hide"));
                        if (hide != null && hide.trim().equals("2"))
                            userVehicle.setHiden(true);
                        userVehicle.setCanDeal(cur.getInt(cur.getColumnIndex("canDeal")));
                        userVehicle.setUrl(cur.getString(cur.getColumnIndex("url")));
                        userVehicle.setMaxFrameNum(cur.getString(cur.getColumnIndex("maxFrameNum")));
                        userVehicle.setMaxMotorNum(cur.getString(cur.getColumnIndex("maxMotorNum")));
                        userVehicle.setUpdateTime(cur.getString(cur.getColumnIndex("updateTime")));
                        String hasRuleError = cur.getString(cur.getColumnIndex("hasRuleError"));
                        userVehicle.setHasRuleError((hasRuleError != null && hasRuleError.equals("1")) ? true : false);
                        String hasParamError = cur.getString(cur.getColumnIndex("hasParamError"));
                        userVehicle.setHasParamError((hasParamError != null && hasParamError.equals("1")) ? true : false);
                        String cityUnValid = cur.getString(cur.getColumnIndex("cityUnValid"));
                        userVehicle.setCityUnValid(cityUnValid);
                        userVehicle.setRequestTime(cur.getLong(cur.getColumnIndex("requestTime")));
                        userVehicle.setReturnTime(cur.getLong(cur.getColumnIndex("returnTime")));
                        userVehicle.setAccount(cur.getString(cur.getColumnIndex("account")));
                        userVehicle.setPassword(cur.getString(cur.getColumnIndex("password")));
                        userVehicle.setVehicleType(cur.getInt(cur.getColumnIndex("vehicleType")));
                        userVehicle.setIssueDate(cur.getString(cur.getColumnIndex("issueDate")));
                        userVehicle.setRegDate(cur.getString(cur.getColumnIndex("regDate")));
                        userVehicle.setVehicleOwner(cur.getString(cur.getColumnIndex("vehicleOwner")));
                        userVehicle.setVehicleRegCerNo(cur.getString(cur.getColumnIndex("vehicleRegCerNo")));
                        userVehicle.setHasSearchFail(cur.getInt(cur.getColumnIndex("hasSearchFail")) == 1 ? true : false);
                        userVehicle.setNewRecordNumber(cur.getInt(cur.getColumnIndex("newRecordNumber")));
                        userVehicle.setJiazhaofenDate(cur.getString(cur.getColumnIndex("jiazhaofenDate")));
                        userVehicle.setNianjianDate(cur.getString(cur.getColumnIndex("nianjianDate")));
                        userVehicle.setCompany(cur.getString(cur.getColumnIndex("company")));
                        userVehicle.setPhone(cur.getString(cur.getColumnIndex("phone")));
                        userVehicle.setRestrictNum(cur.getString(cur.getColumnIndex("restrictNum")));
                        userVehicle.setRestrictRegion(cur.getString(cur.getColumnIndex("restrictRegion")));
                        userVehicle.setRestrictNums(cur.getString(cur.getColumnIndex("restrictNums")));
                        userVehicle.setOwnerId(cur.getString(cur.getColumnIndex("ownerId")));
                        userVehicle.setDriverName(cur.getString(cur.getColumnIndex("driverName")));
                        userVehicle.setDriverId(cur.getString(cur.getColumnIndex("driverId")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (!StringUtils.isEmpty(userVehicle.getVehicleNum()))
            return userVehicle;
        return null;
    }

    public List<UserVehicle> getVehicles() {
        final List<UserVehicle> vehicleList = new ArrayList<UserVehicle>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_VEHICLE,
                            null, null, null, null, null, "id desc", null);
                    while (cur.moveToNext()) {
                        UserVehicle vehicle = new UserVehicle();
                        vehicle.setVehicleNum(cur.getString(cur.getColumnIndex("vehicleNum")));
                        vehicle.setCityId(cur.getString(cur.getColumnIndex("cityId")));
                        vehicle.setCityName(cur.getString(cur.getColumnIndex("cityName")));
                        vehicle.setMotorNum(cur.getString(cur.getColumnIndex("motorNum")));
                        vehicle.setFrameNum(cur.getString(cur.getColumnIndex("frameNum")));
                        vehicle.setVehicleModelId(cur.getLong(cur.getColumnIndex("vehicleModelId")));
                        vehicle.setModelName(cur.getString(cur.getColumnIndex("modelName")));
                        vehicle.setPicUrl(cur.getString(cur.getColumnIndex("picUrl")));
                        vehicle.setDescr(cur.getString(cur.getColumnIndex("descr")));
                        vehicle.setScore(cur.getInt(cur.getColumnIndex("score")));
                        vehicle.setMoney(cur.getInt(cur.getColumnIndex("money")));
                        vehicle.setNewNum(cur.getInt(cur.getColumnIndex("newNum")));
                        vehicle.setOldNum(cur.getInt(cur.getColumnIndex("oldNum")));
                        String hide = cur.getString(cur.getColumnIndex("hide"));
                        if (hide != null && hide.trim().equals("2"))
                            vehicle.setHiden(true);
                        vehicle.setCanDeal(cur.getInt(cur.getColumnIndex("canDeal")));
                        vehicle.setUrl(cur.getString(cur.getColumnIndex("url")));
                        vehicle.setMaxFrameNum(cur.getString(cur.getColumnIndex("maxFrameNum")));
                        vehicle.setMaxMotorNum(cur.getString(cur.getColumnIndex("maxMotorNum")));
                        vehicle.setUpdateTime(cur.getString(cur.getColumnIndex("updateTime")));
                        String hasRuleError = cur.getString(cur.getColumnIndex("hasRuleError"));
                        vehicle.setHasRuleError((hasRuleError != null && hasRuleError.equals("1")) ? true : false);
                        String hasParamError = cur.getString(cur.getColumnIndex("hasParamError"));
                        vehicle.setHasParamError((hasParamError != null && hasParamError.equals("1")) ? true : false);
                        String cityUnValid = cur.getString(cur.getColumnIndex("cityUnValid"));
                        vehicle.setCityUnValid(cityUnValid);
                        vehicle.setRequestTime(cur.getLong(cur.getColumnIndex("requestTime")));
                        vehicle.setReturnTime(cur.getLong(cur.getColumnIndex("returnTime")));
                        vehicle.setAccount(cur.getString(cur.getColumnIndex("account")));
                        vehicle.setPassword(cur.getString(cur.getColumnIndex("password")));
                        vehicle.setVehicleType(cur.getInt(cur.getColumnIndex("vehicleType")));
                        vehicle.setIssueDate(cur.getString(cur.getColumnIndex("issueDate")));
                        vehicle.setRegDate(cur.getString(cur.getColumnIndex("regDate")));
                        vehicle.setVehicleOwner(cur.getString(cur.getColumnIndex("vehicleOwner")));
                        vehicle.setVehicleRegCerNo(cur.getString(cur.getColumnIndex("vehicleRegCerNo")));
                        vehicle.setHasSearchFail(cur.getInt(cur.getColumnIndex("hasSearchFail")) == 1 ? true : false);
                        vehicle.setNewRecordNumber(cur.getInt(cur.getColumnIndex("newRecordNumber")));
                        vehicle.setJiazhaofenDate(cur.getString(cur.getColumnIndex("jiazhaofenDate")));
                        vehicle.setNianjianDate(cur.getString(cur.getColumnIndex("nianjianDate")));
                        vehicle.setCompany(cur.getString(cur.getColumnIndex("company")));
                        vehicle.setPhone(cur.getString(cur.getColumnIndex("phone")));
                        vehicle.setRestrictNum(cur.getString(cur.getColumnIndex("restrictNum")));
                        vehicle.setRestrictRegion(cur.getString(cur.getColumnIndex("restrictRegion")));
                        vehicle.setRestrictNums(cur.getString(cur.getColumnIndex("restrictNums")));
                        vehicle.setOwnerId(cur.getString(cur.getColumnIndex("ownerId")));
                        vehicle.setDriverName(cur.getString(cur.getColumnIndex("driverName")));
                        vehicle.setDriverId(cur.getString(cur.getColumnIndex("driverId")));
                        vehicleList.add(vehicle);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });

        return vehicleList;
    }

    public int deleteVehicle(final String vehicleNum) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    nResult = database.delete(DatabaseHelper.TABLE_NAME_VEHICLE, " vehicleNum = ? ", new String[]{vehicleNum});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return nResult;
    }

    public int clearVehicles() {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    nResult = database.delete(DatabaseHelper.TABLE_NAME_VEHICLE, null, new String[]{});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return nResult;
    }

    public void saveAgainstRecords(final List<AgainstRecord> againstRecordList) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    database.beginTransaction();
                    for (AgainstRecord record : againstRecordList) {
                        long id = record.getId();
                        cur = database.query(DatabaseHelper.TABLE_NAME_AGAINST_RECORDS, new String[]{"id"}, " id = ? ", new String[]{"" + id}, null, null, null);
                        if (cur.getCount() > 0) {
                            database.delete(DatabaseHelper.TABLE_NAME_AGAINST_RECORDS, " id = ? ", new String[]{"" + id});
                        }
                        ContentValues root = new ContentValues();
                        root.put("id", record.getId());
                        root.put("vehicleNum", record.getVehicleNum());
                        root.put("cityId", record.getCityId());
                        root.put("cityName", record.getCityName());
                        root.put("address", record.getAddress());
                        root.put("behavior", record.getBehavior());
                        root.put("time", record.getTime());
                        root.put("score", record.getScore());
                        root.put("money", record.getMoney());
                        root.put("status", record.getStatus());
                        root.put("lat", record.getLat());
                        root.put("lng", record.getLng());
                        root.put("orderStatus", record.getOrderStatus());
                        root.put("canSubmit", record.getCanSubmit());
                        root.put("pId", record.getPId());
                        root.put("orderId", record.getOrderId());
                        root.put("selfProcess", record.getSelfProcess() != null ? record.getSelfProcess() : 0);
                        root.put("resulttype", record.getResultType() != null && record.getResultType() == 1 ? 1 : 0);
                        root.put("paymentstatus", record.getPaymentStatus() != null && record.getPaymentStatus() == 1 ? 1 : 0);
                        root.put("dataSourceTitle", record.getDataSourceTitle());
                        root.put("orderCode", record.getOrderCode());
                        root.put("ordertime", record.getOrdertime());
                        root.put("recordType", record.getRecordType());
                        ImageNames imageNames = record.getImageName();
                        if (imageNames != null) {
                            root.put("img_0", imageNames.getImg_0());
                            root.put("img_1", imageNames.getImg_1());
                            root.put("img_2", imageNames.getImg_2());
                            root.put("img_3", imageNames.getImg_3());
                            root.put("img_4", imageNames.getImg_4());
                        }
                        root.put("selfScore", record.getSelfScore() == null ? -1 : record.getSelfScore());
                        root.put("selfMoney", record.getSelfMoney() == null ? -1 : record.getSelfMoney());
                        database.insert(DatabaseHelper.TABLE_NAME_AGAINST_RECORDS, "id", root);
                    }
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (database != null)
                        database.endTransaction();
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
    }

    public void saveAgainstRecord(final AgainstRecord record) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    long id = record.getId();
                    cur = database.query(DatabaseHelper.TABLE_NAME_AGAINST_RECORDS, new String[]{"id"}, " id = ? ", new String[]{"" + id}, null, null, null);
                    if (cur.getCount() > 0) {
                        database.delete(DatabaseHelper.TABLE_NAME_AGAINST_RECORDS, " id = ? ", new String[]{"" + id});
                    }
                    ContentValues root = new ContentValues();
                    root.put("id", record.getId());
                    root.put("vehicleNum", record.getVehicleNum());
                    root.put("cityId", record.getCityId());
                    root.put("cityName", record.getCityName());
                    root.put("address", record.getAddress());
                    root.put("behavior", record.getBehavior());
                    root.put("time", record.getTime());
                    root.put("score", record.getScore());
                    root.put("money", record.getMoney());
                    root.put("status", record.getStatus());
                    root.put("lat", record.getLat());
                    root.put("lng", record.getLng());
                    root.put("orderStatus", record.getOrderStatus());
                    root.put("canSubmit", record.getCanSubmit());
                    root.put("pId", record.getPId());
                    root.put("orderId", record.getOrderId());
                    root.put("selfProcess", record.getSelfProcess() != null ? record.getSelfProcess() : 0);
                    root.put("resulttype", record.getResultType() != null && record.getResultType() == 1 ? 1 : 0);
                    root.put("paymentstatus", record.getPaymentStatus() != null && record.getPaymentStatus() == 1 ? 1 : 0);
                    root.put("dataSourceTitle", record.getDataSourceTitle());
                    root.put("orderCode", record.getOrderCode());
                    root.put("ordertime", record.getOrdertime());
                    root.put("recordType", record.getRecordType());
                    ImageNames imageNames = record.getImageName();
                    if (imageNames != null) {
                        root.put("img_0", imageNames.getImg_0());
                        root.put("img_1", imageNames.getImg_1());
                        root.put("img_2", imageNames.getImg_2());
                        root.put("img_3", imageNames.getImg_3());
                        root.put("img_4", imageNames.getImg_4());
                    }
                    root.put("selfScore", record.getSelfScore() == null ? -1 : record.getSelfScore());
                    root.put("selfMoney", record.getSelfMoney() == null ? -1 : record.getSelfMoney());
                    database.insert(DatabaseHelper.TABLE_NAME_AGAINST_RECORDS, "id", root);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
    }

    public AgainstRecord getAgainstRecordById(final long id) {
        final AgainstRecord againstRecord = new AgainstRecord();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_AGAINST_RECORDS, null, " id = ? ", new String[]{"" + id}, null, null, null);
                    if (cur.getCount() > 0) {
                        if (cur.moveToNext()) {
                            againstRecord.setId(cur.getLong(cur.getColumnIndex("id")));
                            againstRecord.setVehicleNum(cur.getString(cur.getColumnIndex("vehicleNum")));
                            againstRecord.setCityId(cur.getLong(cur.getColumnIndex("cityId")));
                            againstRecord.setCityName(cur.getString(cur.getColumnIndex("cityName")));
                            againstRecord.setAddress(cur.getString(cur.getColumnIndex("address")));
                            againstRecord.setBehavior(cur.getString(cur.getColumnIndex("behavior")));
                            againstRecord.setTime(cur.getString(cur.getColumnIndex("time")));
                            againstRecord.setScore(cur.getInt(cur.getColumnIndex("score")));
                            againstRecord.setMoney(cur.getInt(cur.getColumnIndex("money")));
                            againstRecord.setStatus(cur.getInt(cur.getColumnIndex("status")));
                            againstRecord.setLat(cur.getDouble(cur.getColumnIndex("lat")));
                            againstRecord.setLng(cur.getDouble(cur.getColumnIndex("lng")));
                            againstRecord.setOrderStatus(cur.getInt(cur.getColumnIndex("orderStatus")));
                            againstRecord.setCanSubmit(cur.getInt(cur.getColumnIndex("canSubmit")));
                            againstRecord.setPId(cur.getLong(cur.getColumnIndex("pId")));
                            againstRecord.setOrderId(cur.getLong(cur.getColumnIndex("orderId")));
                            againstRecord.setSelfProcess(cur.getInt(cur.getColumnIndex("selfProcess")));
                            againstRecord.setDataSourceTitle(cur.getString(cur.getColumnIndex("dataSourceTitle")));
                            againstRecord.setPaymentStatus(cur.getInt(cur.getColumnIndex("paymentstatus")));
                            againstRecord.setResultType(cur.getInt(cur.getColumnIndex("resulttype")));
                            againstRecord.setOrderCode(cur.getString(cur.getColumnIndex("orderCode")));
                            againstRecord.setOrdertime(cur.getString(cur.getColumnIndex("ordertime")));
                            againstRecord.setRecordType(cur.getInt(cur.getColumnIndex("recordType")));
                            if (!StringUtils.isEmpty(cur.getString(cur.getColumnIndex("img_0")))) {
                                ImageNames imageNames = new ImageNames();
                                imageNames.setImg_0(cur.getString(cur.getColumnIndex("img_0")));
                                imageNames.setImg_1(cur.getString(cur.getColumnIndex("img_1")));
                                imageNames.setImg_2(cur.getString(cur.getColumnIndex("img_2")));
                                imageNames.setImg_3(cur.getString(cur.getColumnIndex("img_3")));
                                imageNames.setImg_4(cur.getString(cur.getColumnIndex("img_4")));
                                againstRecord.setImageName(imageNames);
                            }
                            againstRecord.setSelfScore(cur.getInt(cur.getColumnIndex("selfScore")));
                            againstRecord.setSelfMoney(cur.getInt(cur.getColumnIndex("selfMoney")));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (!StringUtils.isEmpty(againstRecord.getVehicleNum()))
            return againstRecord;
        return null;
    }

    public List<AgainstRecord> getAgainstRecordsByNum(final String vehicleNum) {
        final List<AgainstRecord> againstRecordList = new ArrayList<AgainstRecord>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_AGAINST_RECORDS,
                            null, " vehicleNum = ? ",
                            new String[]{vehicleNum}, null, null, "time desc", null);
                    while (cur.moveToNext()) {
                        AgainstRecord record = new AgainstRecord();
                        record.setId(cur.getLong(cur.getColumnIndex("id")));
                        record.setVehicleNum(cur.getString(cur.getColumnIndex("vehicleNum")));
                        record.setCityId(cur.getLong(cur.getColumnIndex("cityId")));
                        record.setCityName(cur.getString(cur.getColumnIndex("cityName")));
                        record.setAddress(cur.getString(cur.getColumnIndex("address")));
                        record.setBehavior(cur.getString(cur.getColumnIndex("behavior")));
                        record.setTime(cur.getString(cur.getColumnIndex("time")));
                        record.setScore(cur.getInt(cur.getColumnIndex("score")));
                        record.setMoney(cur.getInt(cur.getColumnIndex("money")));
                        record.setStatus(cur.getInt(cur.getColumnIndex("status")));
                        record.setLat(cur.getDouble(cur.getColumnIndex("lat")));
                        record.setLng(cur.getDouble(cur.getColumnIndex("lng")));
                        record.setOrderStatus(cur.getInt(cur.getColumnIndex("orderStatus")));
                        record.setCanSubmit(cur.getInt(cur.getColumnIndex("canSubmit")));
                        record.setPId(cur.getLong(cur.getColumnIndex("pId")));
                        record.setOrderId(cur.getLong(cur.getColumnIndex("orderId")));
                        record.setSelfProcess(cur.getInt(cur.getColumnIndex("selfProcess")));
                        record.setDataSourceTitle(cur.getString(cur.getColumnIndex("dataSourceTitle")));
                        record.setPaymentStatus(cur.getInt(cur.getColumnIndex("paymentstatus")));
                        record.setResultType(cur.getInt(cur.getColumnIndex("resulttype")));
                        record.setOrderCode(cur.getString(cur.getColumnIndex("orderCode")));
                        record.setOrdertime(cur.getString(cur.getColumnIndex("ordertime")));
                        record.setRecordType(cur.getInt(cur.getColumnIndex("recordType")));
                        if (!StringUtils.isEmpty(cur.getString(cur.getColumnIndex("img_0")))) {
                            ImageNames imageNames = new ImageNames();
                            imageNames.setImg_0(cur.getString(cur.getColumnIndex("img_0")));
                            imageNames.setImg_1(cur.getString(cur.getColumnIndex("img_1")));
                            imageNames.setImg_2(cur.getString(cur.getColumnIndex("img_2")));
                            imageNames.setImg_3(cur.getString(cur.getColumnIndex("img_3")));
                            imageNames.setImg_4(cur.getString(cur.getColumnIndex("img_4")));
                            record.setImageName(imageNames);
                        }
                        record.setSelfScore(cur.getInt(cur.getColumnIndex("selfScore")));
                        record.setSelfMoney(cur.getInt(cur.getColumnIndex("selfMoney")));
                        record.setnType(KplusConstants.TYPE_NOT_SUPPORT);
                        againstRecordList.add(record);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });

        return againstRecordList;
    }

    public List<AgainstRecord> getCanSubmitAgainstRecords(final String vehicleNum, final int status, final int canSubmit) {
        final List<AgainstRecord> againstRecordList = new ArrayList<AgainstRecord>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_AGAINST_RECORDS,
                            new String[]{"id", "address", "behavior", "time", "score", "money"},
                            " vehicleNum = ? and status = ? and (orderStatus = ? or orderStatus = ? or orderStatus= ? or orderStatus = ?) and canSubmit = ? and money > ? and score >= ? ",
                            new String[]{vehicleNum, "" + status, "0", "2", "14", "20", "" + canSubmit, "0", "0"}, null,
                            null, "time desc", null);
                    while (cur.moveToNext()) {
                        AgainstRecord record = new AgainstRecord();
                        record.setId(cur.getLong(cur.getColumnIndex("id")));
                        record.setAddress(cur.getString(cur.getColumnIndex("address")));
                        record.setBehavior(cur.getString(cur.getColumnIndex("behavior")));
                        record.setTime(cur.getString(cur.getColumnIndex("time")));
                        record.setScore(cur.getInt(cur.getColumnIndex("score")));
                        record.setMoney(cur.getInt(cur.getColumnIndex("money")));
                        if (record.getMoney() > 0 && record.getScore() == 0)
                            againstRecordList.add(record);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });

        return againstRecordList;
    }

    public List<AgainstRecord> getPayOnlineAgainstRecords(final String vehicleNum) {
        final List<AgainstRecord> againstRecordList = new ArrayList<AgainstRecord>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_AGAINST_RECORDS,
                            null, " vehicleNum = ? and status = ? and (orderStatus = ? or orderStatus = ? or orderStatus= ? or orderStatus = ?) and canSubmit = ? and score >= ? and money > ? ",
                            new String[]{vehicleNum, "0", "0", "2", "14", "20", "0", "0", "0"}, null,
                            null, "time desc", null);
                    while (cur.moveToNext()) {
                        AgainstRecord record = new AgainstRecord();
                        record.setId(cur.getLong(cur.getColumnIndex("id")));
                        record.setVehicleNum(cur.getString(cur.getColumnIndex("vehicleNum")));
                        record.setCityId(cur.getLong(cur.getColumnIndex("cityId")));
                        record.setCityName(cur.getString(cur.getColumnIndex("cityName")));
                        record.setAddress(cur.getString(cur.getColumnIndex("address")));
                        record.setBehavior(cur.getString(cur.getColumnIndex("behavior")));
                        record.setTime(cur.getString(cur.getColumnIndex("time")));
                        record.setScore(cur.getInt(cur.getColumnIndex("score")));
                        record.setMoney(cur.getInt(cur.getColumnIndex("money")));
                        record.setStatus(cur.getInt(cur.getColumnIndex("status")));
                        record.setLat(cur.getDouble(cur.getColumnIndex("lat")));
                        record.setLng(cur.getDouble(cur.getColumnIndex("lng")));
                        record.setOrderStatus(cur.getInt(cur.getColumnIndex("orderStatus")));
                        record.setCanSubmit(cur.getInt(cur.getColumnIndex("canSubmit")));
                        record.setPId(cur.getLong(cur.getColumnIndex("pId")));
                        record.setOrderId(cur.getLong(cur.getColumnIndex("orderId")));
                        record.setSelfProcess(cur.getInt(cur.getColumnIndex("selfProcess")));
                        record.setDataSourceTitle(cur.getString(cur.getColumnIndex("dataSourceTitle")));
                        record.setPaymentStatus(cur.getInt(cur.getColumnIndex("paymentstatus")));
                        record.setResultType(cur.getInt(cur.getColumnIndex("resulttype")));
                        record.setOrderCode(cur.getString(cur.getColumnIndex("orderCode")));
                        record.setOrdertime(cur.getString(cur.getColumnIndex("ordertime")));
                        record.setRecordType(cur.getInt(cur.getColumnIndex("recordType")));
                        if (!StringUtils.isEmpty(cur.getString(cur.getColumnIndex("img_0")))) {
                            ImageNames imageNames = new ImageNames();
                            imageNames.setImg_0(cur.getString(cur.getColumnIndex("img_0")));
                            imageNames.setImg_1(cur.getString(cur.getColumnIndex("img_1")));
                            imageNames.setImg_2(cur.getString(cur.getColumnIndex("img_2")));
                            imageNames.setImg_3(cur.getString(cur.getColumnIndex("img_3")));
                            imageNames.setImg_4(cur.getString(cur.getColumnIndex("img_4")));
                            record.setImageName(imageNames);
                        }
                        record.setSelfScore(cur.getInt(cur.getColumnIndex("selfScore")));
                        record.setSelfMoney(cur.getInt(cur.getColumnIndex("selfMoney")));
                        record.setnType(KplusConstants.TYPE_CAN_PAY_ONLINE);
                        againstRecordList.add(record);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        return againstRecordList;
    }

    public List<AgainstRecord> getDisposingAgainstRecords(final String vehicleNum) {
        final List<AgainstRecord> againstRecordList = new ArrayList<AgainstRecord>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_AGAINST_RECORDS,
                            null, " vehicleNum = ? and status = ? and (orderStatus > ? and orderStatus < ? or orderStatus = ?)",
                            new String[]{vehicleNum, "0", "2", "12", "13"}, null,
                            null, "time desc", null);
                    while (cur.moveToNext()) {
                        AgainstRecord record = new AgainstRecord();
                        record.setId(cur.getLong(cur.getColumnIndex("id")));
                        record.setVehicleNum(cur.getString(cur.getColumnIndex("vehicleNum")));
                        record.setCityId(cur.getLong(cur.getColumnIndex("cityId")));
                        record.setCityName(cur.getString(cur.getColumnIndex("cityName")));
                        record.setAddress(cur.getString(cur.getColumnIndex("address")));
                        record.setBehavior(cur.getString(cur.getColumnIndex("behavior")));
                        record.setTime(cur.getString(cur.getColumnIndex("time")));
                        record.setScore(cur.getInt(cur.getColumnIndex("score")));
                        record.setMoney(cur.getInt(cur.getColumnIndex("money")));
                        record.setStatus(cur.getInt(cur.getColumnIndex("status")));
                        record.setLat(cur.getDouble(cur.getColumnIndex("lat")));
                        record.setLng(cur.getDouble(cur.getColumnIndex("lng")));
                        record.setOrderStatus(cur.getInt(cur.getColumnIndex("orderStatus")));
                        record.setCanSubmit(cur.getInt(cur.getColumnIndex("canSubmit")));
                        record.setPId(cur.getLong(cur.getColumnIndex("pId")));
                        record.setOrderId(cur.getLong(cur.getColumnIndex("orderId")));
                        record.setSelfProcess(cur.getInt(cur.getColumnIndex("selfProcess")));
                        record.setDataSourceTitle(cur.getString(cur.getColumnIndex("dataSourceTitle")));
                        record.setPaymentStatus(cur.getInt(cur.getColumnIndex("paymentstatus")));
                        record.setResultType(cur.getInt(cur.getColumnIndex("resulttype")));
                        record.setOrderCode(cur.getString(cur.getColumnIndex("orderCode")));
                        record.setOrdertime(cur.getString(cur.getColumnIndex("ordertime")));
                        record.setRecordType(cur.getInt(cur.getColumnIndex("recordType")));
                        if (!StringUtils.isEmpty(cur.getString(cur.getColumnIndex("img_0")))) {
                            ImageNames imageNames = new ImageNames();
                            imageNames.setImg_0(cur.getString(cur.getColumnIndex("img_0")));
                            imageNames.setImg_1(cur.getString(cur.getColumnIndex("img_1")));
                            imageNames.setImg_2(cur.getString(cur.getColumnIndex("img_2")));
                            imageNames.setImg_3(cur.getString(cur.getColumnIndex("img_3")));
                            imageNames.setImg_4(cur.getString(cur.getColumnIndex("img_4")));
                            record.setImageName(imageNames);
                        }
                        record.setSelfScore(cur.getInt(cur.getColumnIndex("selfScore")));
                        record.setSelfMoney(cur.getInt(cur.getColumnIndex("selfMoney")));
                        record.setnType(KplusConstants.TYPE_DISPOSING);
                        againstRecordList.add(record);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        return againstRecordList;
    }

    public List<AgainstRecord> getTicketPaymentAgainstRecords(final String vehicleNum) {
        final List<AgainstRecord> againstRecordList = new ArrayList<AgainstRecord>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_AGAINST_RECORDS,
                            null, " vehicleNum = ? and status = ? and paymentstatus = ? and (selfProcess = ? or selfProcess is null)",
                            new String[]{vehicleNum, "1", "0", "0"}, null,
                            null, "time desc", null);
                    while (cur.moveToNext()) {
                        AgainstRecord record = new AgainstRecord();
                        record.setId(cur.getLong(cur.getColumnIndex("id")));
                        record.setVehicleNum(cur.getString(cur.getColumnIndex("vehicleNum")));
                        record.setCityId(cur.getLong(cur.getColumnIndex("cityId")));
                        record.setCityName(cur.getString(cur.getColumnIndex("cityName")));
                        record.setAddress(cur.getString(cur.getColumnIndex("address")));
                        record.setBehavior(cur.getString(cur.getColumnIndex("behavior")));
                        record.setTime(cur.getString(cur.getColumnIndex("time")));
                        record.setScore(cur.getInt(cur.getColumnIndex("score")));
                        record.setMoney(cur.getInt(cur.getColumnIndex("money")));
                        record.setStatus(cur.getInt(cur.getColumnIndex("status")));
                        record.setLat(cur.getDouble(cur.getColumnIndex("lat")));
                        record.setLng(cur.getDouble(cur.getColumnIndex("lng")));
                        record.setOrderStatus(cur.getInt(cur.getColumnIndex("orderStatus")));
                        record.setCanSubmit(cur.getInt(cur.getColumnIndex("canSubmit")));
                        record.setPId(cur.getLong(cur.getColumnIndex("pId")));
                        record.setOrderId(cur.getLong(cur.getColumnIndex("orderId")));
                        record.setSelfProcess(cur.getInt(cur.getColumnIndex("selfProcess")));
                        record.setDataSourceTitle(cur.getString(cur.getColumnIndex("dataSourceTitle")));
                        record.setPaymentStatus(cur.getInt(cur.getColumnIndex("paymentstatus")));
                        record.setResultType(cur.getInt(cur.getColumnIndex("resulttype")));
                        record.setOrderCode(cur.getString(cur.getColumnIndex("orderCode")));
                        record.setOrdertime(cur.getString(cur.getColumnIndex("ordertime")));
                        record.setRecordType(cur.getInt(cur.getColumnIndex("recordType")));
                        if (!StringUtils.isEmpty(cur.getString(cur.getColumnIndex("img_0")))) {
                            ImageNames imageNames = new ImageNames();
                            imageNames.setImg_0(cur.getString(cur.getColumnIndex("img_0")));
                            imageNames.setImg_1(cur.getString(cur.getColumnIndex("img_1")));
                            imageNames.setImg_2(cur.getString(cur.getColumnIndex("img_2")));
                            imageNames.setImg_3(cur.getString(cur.getColumnIndex("img_3")));
                            imageNames.setImg_4(cur.getString(cur.getColumnIndex("img_4")));
                            record.setImageName(imageNames);
                        }
                        record.setSelfScore(cur.getInt(cur.getColumnIndex("selfScore")));
                        record.setSelfMoney(cur.getInt(cur.getColumnIndex("selfMoney")));
                        record.setnType(KplusConstants.TYPE_CAN_TICKET_PAY);
                        againstRecordList.add(record);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        return againstRecordList;
    }

    public List<AgainstRecord> getCompletedRecords(final String vehicleNum) {
        final List<AgainstRecord> againstRecordList = new ArrayList<AgainstRecord>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_AGAINST_RECORDS,
                            null, " vehicleNum = ? and status = ? and (paymentstatus = ? or money = ? or selfProcess = ? )",
                            new String[]{vehicleNum, "1", "1", "0", "1"}, null,
                            null, "time desc", null);
                    while (cur.moveToNext()) {
                        AgainstRecord record = new AgainstRecord();
                        record.setId(cur.getLong(cur.getColumnIndex("id")));
                        record.setVehicleNum(cur.getString(cur.getColumnIndex("vehicleNum")));
                        record.setCityId(cur.getLong(cur.getColumnIndex("cityId")));
                        record.setCityName(cur.getString(cur.getColumnIndex("cityName")));
                        record.setAddress(cur.getString(cur.getColumnIndex("address")));
                        record.setBehavior(cur.getString(cur.getColumnIndex("behavior")));
                        record.setTime(cur.getString(cur.getColumnIndex("time")));
                        record.setScore(cur.getInt(cur.getColumnIndex("score")));
                        record.setMoney(cur.getInt(cur.getColumnIndex("money")));
                        record.setStatus(cur.getInt(cur.getColumnIndex("status")));
                        record.setLat(cur.getDouble(cur.getColumnIndex("lat")));
                        record.setLng(cur.getDouble(cur.getColumnIndex("lng")));
                        record.setOrderStatus(cur.getInt(cur.getColumnIndex("orderStatus")));
                        record.setCanSubmit(cur.getInt(cur.getColumnIndex("canSubmit")));
                        record.setPId(cur.getLong(cur.getColumnIndex("pId")));
                        record.setOrderId(cur.getLong(cur.getColumnIndex("orderId")));
                        record.setSelfProcess(cur.getInt(cur.getColumnIndex("selfProcess")));
                        record.setDataSourceTitle(cur.getString(cur.getColumnIndex("dataSourceTitle")));
                        record.setPaymentStatus(cur.getInt(cur.getColumnIndex("paymentstatus")));
                        record.setResultType(cur.getInt(cur.getColumnIndex("resulttype")));
                        record.setOrderCode(cur.getString(cur.getColumnIndex("orderCode")));
                        record.setOrdertime(cur.getString(cur.getColumnIndex("ordertime")));
                        record.setRecordType(cur.getInt(cur.getColumnIndex("recordType")));
                        if (!StringUtils.isEmpty(cur.getString(cur.getColumnIndex("img_0")))) {
                            ImageNames imageNames = new ImageNames();
                            imageNames.setImg_0(cur.getString(cur.getColumnIndex("img_0")));
                            imageNames.setImg_1(cur.getString(cur.getColumnIndex("img_1")));
                            imageNames.setImg_2(cur.getString(cur.getColumnIndex("img_2")));
                            imageNames.setImg_3(cur.getString(cur.getColumnIndex("img_3")));
                            imageNames.setImg_4(cur.getString(cur.getColumnIndex("img_4")));
                            record.setImageName(imageNames);
                        }
                        record.setSelfScore(cur.getInt(cur.getColumnIndex("selfScore")));
                        record.setSelfMoney(cur.getInt(cur.getColumnIndex("selfMoney")));
                        record.setnType(KplusConstants.TYPE_DISPOSED);
                        againstRecordList.add(record);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        return againstRecordList;
    }


    public int deleteAgainstRecord(final String vehicleNum) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    nResult = database.delete(DatabaseHelper.TABLE_NAME_AGAINST_RECORDS, " vehicleNum = ? ", new String[]{vehicleNum});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return nResult;
    }

    public int deleteImageAgainstRecord(final String vehicleNum) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    nResult = database.delete(DatabaseHelper.TABLE_NAME_AGAINST_RECORDS, " vehicleNum = ? and resulttype = ? ", new String[]{vehicleNum, "1"});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return nResult;
    }

    public int deleteAgainstRecordByCityIds(final String cityIds) {
        if (StringUtils.isEmpty(cityIds))
            return -1;
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    String[] ids = cityIds.split(",");
                    String selection = " cityId = ? ";
                    if (ids != null && ids.length > 1) {
                        for (int i = 1; i < ids.length; i++) {
                            selection += "or cityId = ? ";
                        }
                    }
                    nResult = database.delete(DatabaseHelper.TABLE_NAME_AGAINST_RECORDS, selection, ids);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return nResult;
    }


    public int deleteAgainstRecord(final long id) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    nResult = database.delete(DatabaseHelper.TABLE_NAME_AGAINST_RECORDS, " id = ? ", new String[]{"" + id});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return nResult;
    }

    public int clearAgainstRecords() {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    nResult = database.delete(DatabaseHelper.TABLE_NAME_AGAINST_RECORDS, null, new String[]{});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return nResult;
    }

    public void saveCityVehicles(final List<CityVehicle> cityVehicles) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    database.beginTransaction();
                    for (CityVehicle cityVehicle : cityVehicles) {
                        long id = cityVehicle.getId();
                        cur = database.query(
                                DatabaseHelper.TABLE_NAME_CITY, new String[]{"id"}, " id = ? ", new String[]{"" + id}, null, null, null);
                        if (cur.getCount() > 0) {
                            database.delete(DatabaseHelper.TABLE_NAME_CITY, " id = ? ", new String[]{"" + id});
                        }
                        ContentValues root = new ContentValues();
                        root.put("id", cityVehicle.getId());
                        root.put("province", cityVehicle.getProvince());
                        root.put("name", cityVehicle.getName());
                        root.put("prefix", cityVehicle.getPrefix());
                        root.put("motorNumLen", cityVehicle.getMotorNumLen());
                        root.put("frameNumLen", cityVehicle.getFrameNumLen());
                        root.put("PY", cityVehicle.getPY());
                        root.put("valid", (cityVehicle.getValid() != null && cityVehicle.getValid() == true) ? 1 : 0);
                        root.put("hot", (cityVehicle.getHot() != null && cityVehicle.getHot() == true) ? 1 : 0);
                        root.put("owner", (cityVehicle.getOwner() != null && cityVehicle.getOwner() == true) ? 1 : 0);
                        root.put("accountLen", cityVehicle.getAccountLen());
                        root.put("passwordLen", cityVehicle.getPasswordLen());
                        root.put("motorvehiclenumLen", cityVehicle.getMotorvehiclenumLen());
                        root.put("ownerIdNoLen", cityVehicle.getOwnerIdNoLen());
                        root.put("drivingLicenseName", true == cityVehicle.isDrivingLicenseName() ? 1 : 0);
                        root.put("drivingLicenseNoLen", cityVehicle.getDrivingLicenseNoLen());
                        root.put("fieldComment", cityVehicle.getFieldComment());
                        database.insert(DatabaseHelper.TABLE_NAME_CITY, "id", root);
                    }
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (database != null)
                        database.endTransaction();
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
    }

    public void saveCityVehicle(final CityVehicle cityVehicle) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    long id = cityVehicle.getId();
                    cur = database.query(
                            DatabaseHelper.TABLE_NAME_CITY, new String[]{"id"}, " id = ? ", new String[]{"" + id}, null, null, null);
                    if (cur.getCount() > 0) {
                        database.delete(DatabaseHelper.TABLE_NAME_CITY, " id = ? ", new String[]{"" + id});
                    }
                    ContentValues root = new ContentValues();
                    root.put("id", cityVehicle.getId());
                    root.put("province", cityVehicle.getProvince());
                    root.put("name", cityVehicle.getName());
                    root.put("prefix", cityVehicle.getPrefix());
                    root.put("motorNumLen", cityVehicle.getMotorNumLen());
                    root.put("frameNumLen", cityVehicle.getFrameNumLen());
                    root.put("PY", cityVehicle.getPY());
                    root.put("valid", (cityVehicle.getValid() != null && cityVehicle.getValid() == true) ? 1 : 0);
                    root.put("hot", (cityVehicle.getHot() != null && cityVehicle.getHot() == true) ? 1 : 0);
                    root.put("owner", (cityVehicle.getOwner() != null && cityVehicle.getOwner() == true) ? 1 : 0);
                    root.put("accountLen", cityVehicle.getAccountLen());
                    root.put("passwordLen", cityVehicle.getPasswordLen());
                    root.put("motorvehiclenumLen", cityVehicle.getMotorvehiclenumLen());
                    root.put("ownerIdNoLen", cityVehicle.getOwnerIdNoLen());
                    root.put("drivingLicenseName", true == cityVehicle.isDrivingLicenseName() ? 1 : 0);
                    root.put("drivingLicenseNoLen", cityVehicle.getDrivingLicenseNoLen());
                    root.put("fieldComment", cityVehicle.getFieldComment());
                    database.insert(DatabaseHelper.TABLE_NAME_CITY, "id", root);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
    }

    public List<CityVehicle> getCityVehicles() {
        final List<CityVehicle> cityVehicleList = new ArrayList<CityVehicle>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_CITY,
                            null, null, null, null, null, null, null);
                    while (cur.moveToNext()) {
                        CityVehicle cityVehicle = new CityVehicle();
                        cityVehicle.setId(cur.getLong(cur.getColumnIndex("id")));
                        cityVehicle.setProvince(cur.getString(cur.getColumnIndex("province")));
                        cityVehicle.setName(cur.getString(cur.getColumnIndex("name")));
                        cityVehicle.setPrefix(cur.getString(cur.getColumnIndex("prefix")));
                        cityVehicle.setMotorNumLen(cur.getInt(cur.getColumnIndex("motorNumLen")));
                        cityVehicle.setFrameNumLen(cur.getInt(cur.getColumnIndex("frameNumLen")));
                        cityVehicle.setPY(cur.getString(cur.getColumnIndex("PY")));
                        Integer nValid = cur.getInt(cur.getColumnIndex("valid"));
                        if (nValid != null && nValid == 1)
                            cityVehicle.setValid(true);
                        else
                            cityVehicle.setValid(false);
                        Integer nHot = cur.getInt(cur.getColumnIndex("hot"));
                        if (nHot != null && nHot == 1)
                            cityVehicle.setHot(true);
                        else
                            cityVehicle.setHot(false);
                        Integer owner = cur.getInt(cur.getColumnIndex("owner"));
                        if (owner != null && owner == 1)
                            cityVehicle.setOwner(true);
                        else
                            cityVehicle.setOwner(false);
                        cityVehicle.setAccountLen(cur.getInt(cur.getColumnIndex("accountLen")));
                        cityVehicle.setPasswordLen(cur.getInt(cur.getColumnIndex("passwordLen")));
                        cityVehicle.setMotorvehiclenumLen(cur.getInt(cur.getColumnIndex("motorvehiclenumLen")));
                        cityVehicle.setOwnerIdNoLen(cur.getInt(cur.getColumnIndex("ownerIdNoLen")));
                        Integer nName = cur.getInt(cur.getColumnIndex("drivingLicenseName"));
                        if (nName != null && nName == 1)
                            cityVehicle.setDrivingLicenseName(true);
                        else
                            cityVehicle.setDrivingLicenseName(false);
                        cityVehicle.setDrivingLicenseNoLen(cur.getInt(cur.getColumnIndex("drivingLicenseNoLen")));
                        cityVehicle.setFieldComment(cur.getString(cur.getColumnIndex("fieldComment")));
                        cityVehicleList.add(cityVehicle);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });

        return cityVehicleList;
    }

    public List<CityVehicle> getCityVehicles(final String id) {
        final List<CityVehicle> cityVehicleList = new ArrayList<CityVehicle>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    String[] ids = id.split(",");
                    String selection = " id = ? ";
                    if (ids != null && ids.length > 1) {
                        for (int i = 1; i < ids.length; i++) {
                            selection += "or id = ? ";
                        }
                    }
                    cur = database.query(DatabaseHelper.TABLE_NAME_CITY,
                            null, selection, ids, null, null, null, null);
                    while (cur.moveToNext()) {
                        CityVehicle cityVehicle = new CityVehicle();
                        cityVehicle.setId(cur.getLong(cur.getColumnIndex("id")));
                        cityVehicle.setProvince(cur.getString(cur.getColumnIndex("province")));
                        cityVehicle.setName(cur.getString(cur.getColumnIndex("name")));
                        cityVehicle.setPrefix(cur.getString(cur.getColumnIndex("prefix")));
                        cityVehicle.setMotorNumLen(cur.getInt(cur.getColumnIndex("motorNumLen")));
                        cityVehicle.setFrameNumLen(cur.getInt(cur.getColumnIndex("frameNumLen")));
                        cityVehicle.setPY(cur.getString(cur.getColumnIndex("PY")));
                        Integer nValid = cur.getInt(cur.getColumnIndex("valid"));
                        if (nValid != null && nValid == 1)
                            cityVehicle.setValid(true);
                        else
                            cityVehicle.setValid(false);
                        Integer nHot = cur.getInt(cur.getColumnIndex("hot"));
                        if (nHot != null && nHot == 1)
                            cityVehicle.setHot(true);
                        else
                            cityVehicle.setHot(false);
                        Integer owner = cur.getInt(cur.getColumnIndex("owner"));
                        if (owner != null && owner == 1)
                            cityVehicle.setOwner(true);
                        else
                            cityVehicle.setOwner(false);
                        cityVehicle.setAccountLen(cur.getInt(cur.getColumnIndex("accountLen")));
                        cityVehicle.setPasswordLen(cur.getInt(cur.getColumnIndex("passwordLen")));
                        cityVehicle.setMotorvehiclenumLen(cur.getInt(cur.getColumnIndex("motorvehiclenumLen")));
                        cityVehicle.setOwnerIdNoLen(cur.getInt(cur.getColumnIndex("ownerIdNoLen")));
                        Integer nName = cur.getInt(cur.getColumnIndex("drivingLicenseName"));
                        if (nName != null && nName == 1)
                            cityVehicle.setDrivingLicenseName(true);
                        else
                            cityVehicle.setDrivingLicenseName(false);
                        cityVehicle.setDrivingLicenseNoLen(cur.getInt(cur.getColumnIndex("drivingLicenseNoLen")));
                        cityVehicle.setFieldComment(cur.getString(cur.getColumnIndex("fieldComment")));
                        cityVehicleList.add(cityVehicle);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });

        return cityVehicleList;
    }

    public CityVehicle getCityVehicle(final String name) {
        final CityVehicle cityVehicle = new CityVehicle();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_CITY,
                            null, " name = ? ", new String[]{name}, null, null, null, null);
                    if (cur.moveToNext()) {
                        CityVehicle cityVehicle = new CityVehicle();
                        cityVehicle.setId(cur.getLong(cur.getColumnIndex("id")));
                        cityVehicle.setProvince(cur.getString(cur.getColumnIndex("province")));
                        cityVehicle.setName(cur.getString(cur.getColumnIndex("name")));
                        cityVehicle.setPrefix(cur.getString(cur.getColumnIndex("prefix")));
                        cityVehicle.setMotorNumLen(cur.getInt(cur.getColumnIndex("motorNumLen")));
                        cityVehicle.setFrameNumLen(cur.getInt(cur.getColumnIndex("frameNumLen")));
                        cityVehicle.setPY(cur.getString(cur.getColumnIndex("PY")));
                        Integer nValid = cur.getInt(cur.getColumnIndex("valid"));
                        if (nValid != null && nValid == 1)
                            cityVehicle.setValid(true);
                        else
                            cityVehicle.setValid(false);
                        Integer nVHot = cur.getInt(cur.getColumnIndex("hot"));
                        if (nVHot != null && nVHot == 1)
                            cityVehicle.setHot(true);
                        else
                            cityVehicle.setHot(false);
                        Integer owner = cur.getInt(cur.getColumnIndex("owner"));
                        if (owner != null && owner == 1)
                            cityVehicle.setOwner(true);
                        else
                            cityVehicle.setOwner(false);
                        cityVehicle.setAccountLen(cur.getInt(cur.getColumnIndex("accountLen")));
                        cityVehicle.setPasswordLen(cur.getInt(cur.getColumnIndex("passwordLen")));
                        cityVehicle.setMotorvehiclenumLen(cur.getInt(cur.getColumnIndex("motorvehiclenumLen")));
                        cityVehicle.setOwnerIdNoLen(cur.getInt(cur.getColumnIndex("ownerIdNoLen")));
                        Integer nName = cur.getInt(cur.getColumnIndex("drivingLicenseName"));
                        if (nName != null && nName == 1)
                            cityVehicle.setDrivingLicenseName(true);
                        else
                            cityVehicle.setDrivingLicenseName(false);
                        cityVehicle.setDrivingLicenseNoLen(cur.getInt(cur.getColumnIndex("drivingLicenseNoLen")));
                        cityVehicle.setFieldComment(cur.getString(cur.getColumnIndex("fieldComment")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });

        return cityVehicle;
    }

    public int deleteCityVehicle(final long id) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    nResult = database.delete(DatabaseHelper.TABLE_NAME_CITY, " id = ? ",
                            new String[]{"" + id});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return nResult;
    }

    public int clearCityVehicles() {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    nResult = database.delete(DatabaseHelper.TABLE_NAME_CITY, null, new String[]{});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return nResult;
    }

    public void saveFlashImage(final ImageInfo imageInfo) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    database.delete(DatabaseHelper.FLASH_IMAGES, null, new String[]{});
                    ContentValues root = new ContentValues();
                    root.put("imgUrl", imageInfo.getImgUrl());
                    root.put("actionType", imageInfo.getActionType());
                    root.put("actionValue", imageInfo.getActionValue());
                    root.put("orderId", imageInfo.getOrderId());
                    root.put("imagePath", imageInfo.getImagePath());
                    root.put("valid", imageInfo.getValid() ? "1" : "0");
                    database.insert(DatabaseHelper.FLASH_IMAGES, "imgUrl", root);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void saveBodyImage(final ImageInfo imageInfo) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    database.delete(DatabaseHelper.BODY_IMAGES, null, new String[]{});
                    ContentValues root = new ContentValues();
                    root.put("imgUrl", imageInfo.getImgUrl());
                    root.put("actionType", imageInfo.getActionType());
                    root.put("actionValue", imageInfo.getActionValue());
                    root.put("orderId", imageInfo.getOrderId());
                    root.put("imagePath", imageInfo.getImagePath());
                    root.put("valid", imageInfo.getValid() ? "1" : "0");
                    database.insert(DatabaseHelper.BODY_IMAGES, "imgUrl", root);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void saveVehicleHeadImage(final ImageInfo imageInfo) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    database.delete(DatabaseHelper.VEHICLE_HEAD_IMAGES, null, new String[]{});
                    ContentValues root = new ContentValues();
                    root.put("imgUrl", imageInfo.getImgUrl());
                    root.put("actionType", imageInfo.getActionType());
                    root.put("actionValue", imageInfo.getActionValue());
                    root.put("orderId", imageInfo.getOrderId());
                    root.put("imagePath", imageInfo.getImagePath());
                    root.put("valid", imageInfo.getValid() ? "1" : "0");
                    database.insert(DatabaseHelper.VEHICLE_HEAD_IMAGES, "imgUrl", root);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void saveHeadImage(final ImageInfo imageInfo) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    String imgUrl = imageInfo.getImgUrl();
                    cur = database.query(
                            DatabaseHelper.HOME_PAGE_IMAGES, new String[]{"imgUrl"}, " imgUrl = ? ", new String[]{imgUrl}, null, null, null);
                    if (cur.getCount() > 0) {
                        database.delete(DatabaseHelper.HOME_PAGE_IMAGES, " imgUrl = ? ", new String[]{imgUrl});
                    }
                    ContentValues root = new ContentValues();
                    root.put("imgUrl", imageInfo.getImgUrl());
                    root.put("actionType", imageInfo.getActionType());
                    root.put("actionValue", imageInfo.getActionValue());
                    root.put("orderId", imageInfo.getOrderId());
                    root.put("imagePath", imageInfo.getImagePath());
                    root.put("valid", imageInfo.getValid() ? "1" : "0");
                    database.insert(DatabaseHelper.HOME_PAGE_IMAGES, "imgUrl", root);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
    }


    public ImageInfo getFlashImageInfo() {
        final ImageInfo flashImageInfo = new ImageInfo();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.FLASH_IMAGES,
                            new String[]{"imgUrl", "actionType", "actionValue", "orderId", "imagePath", "valid"}, null, null, null,
                            null, null, null);
                    if (cur.moveToNext()) {
                        flashImageInfo.setImgUrl(cur.getString(cur.getColumnIndex("imgUrl")));
                        flashImageInfo.setActionType(cur.getString(cur.getColumnIndex("actionType")));
                        flashImageInfo.setActionValue(cur.getString(cur.getColumnIndex("actionValue")));
                        flashImageInfo.setOrderId(cur.getInt(cur.getColumnIndex("orderId")));
                        flashImageInfo.setImagePath(cur.getString(cur.getColumnIndex("imagePath")));
                        String valid = cur.getString(cur.getColumnIndex("valid"));
                        if (!StringUtils.isEmpty(valid))
                            flashImageInfo.setValid(valid.equals("0") ? false : true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (!StringUtils.isEmpty(flashImageInfo.getImgUrl()))
            return flashImageInfo;
        return null;
    }

    public ImageInfo getBodyImageInfo() {
        final ImageInfo bodyImageInfo = new ImageInfo();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.BODY_IMAGES,
                            new String[]{"imgUrl", "actionType", "actionValue", "orderId", "imagePath", "valid"}, null, null, null,
                            null, null, null);
                    if (cur.moveToNext()) {
                        bodyImageInfo.setImgUrl(cur.getString(cur.getColumnIndex("imgUrl")));
                        bodyImageInfo.setActionType(cur.getString(cur.getColumnIndex("actionType")));
                        bodyImageInfo.setActionValue(cur.getString(cur.getColumnIndex("actionValue")));
                        bodyImageInfo.setOrderId(cur.getInt(cur.getColumnIndex("orderId")));
                        bodyImageInfo.setImagePath(cur.getString(cur.getColumnIndex("imagePath")));
                        String valid = cur.getString(cur.getColumnIndex("valid"));
                        if (!StringUtils.isEmpty(valid))
                            bodyImageInfo.setValid(valid.equals("0") ? false : true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (!StringUtils.isEmpty(bodyImageInfo.getImgUrl()))
            return bodyImageInfo;
        return null;
    }

    public ImageInfo getVehicleHeadImageInfo() {
        final ImageInfo vehicleHeadImageInfo = new ImageInfo();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.VEHICLE_HEAD_IMAGES,
                            new String[]{"imgUrl", "actionType", "actionValue", "orderId", "imagePath", "valid"}, null, null, null,
                            null, null, null);
                    if (cur.moveToNext()) {
                        vehicleHeadImageInfo.setImgUrl(cur.getString(cur.getColumnIndex("imgUrl")));
                        vehicleHeadImageInfo.setActionType(cur.getString(cur.getColumnIndex("actionType")));
                        vehicleHeadImageInfo.setActionValue(cur.getString(cur.getColumnIndex("actionValue")));
                        vehicleHeadImageInfo.setOrderId(cur.getInt(cur.getColumnIndex("orderId")));
                        vehicleHeadImageInfo.setImagePath(cur.getString(cur.getColumnIndex("imagePath")));
                        String valid = cur.getString(cur.getColumnIndex("valid"));
                        if (!StringUtils.isEmpty(valid))
                            vehicleHeadImageInfo.setValid(valid.equals("0") ? false : true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (!StringUtils.isEmpty(vehicleHeadImageInfo.getImgUrl()))
            return vehicleHeadImageInfo;
        return null;
    }

    public List<ImageInfo> getHeadImageInfos() {
        final List<ImageInfo> imageInfoList = new ArrayList<ImageInfo>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.HOME_PAGE_IMAGES,
                            new String[]{"imgUrl", "actionType", "actionValue", "orderId", "imagePath", "valid"}, null, null, null,
                            null, null, null);
                    while (cur.moveToNext()) {
                        ImageInfo imageInfo = new ImageInfo();
                        imageInfo.setImgUrl(cur.getString(cur.getColumnIndex("imgUrl")));
                        imageInfo.setActionType(cur.getString(cur.getColumnIndex("actionType")));
                        imageInfo.setActionValue(cur.getString(cur.getColumnIndex("actionValue")));
                        imageInfo.setOrderId(cur.getInt(cur.getColumnIndex("orderId")));
                        imageInfo.setImagePath(cur.getString(cur.getColumnIndex("imagePath")));
                        String valid = cur.getString(cur.getColumnIndex("valid"));
                        if (!StringUtils.isEmpty(valid))
                            imageInfo.setValid(valid.equals("0") ? false : true);
                        imageInfoList.add(imageInfo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });

        return imageInfoList;
    }

    public int clearFlashImages() {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    nResult = database.delete(DatabaseHelper.FLASH_IMAGES, null, new String[]{});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return nResult;
    }

    public int clearBodyImages() {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    nResult = database.delete(DatabaseHelper.BODY_IMAGES, null, new String[]{});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return nResult;
    }

    public int clearVehicleHeadImages() {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    nResult = database.delete(DatabaseHelper.VEHICLE_HEAD_IMAGES, null, new String[]{});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return nResult;
    }

    public int clearHeadImages() {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    nResult = database.delete(DatabaseHelper.HOME_PAGE_IMAGES, null, new String[]{});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return nResult;
    }

    public List<TabInfo> getTabInfos() {
        final List<TabInfo> tabInfoList = new ArrayList<TabInfo>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TAB_INFO,
                            new String[]{"identity", "name", "description", "orderId", "isValid"}, null, null, null, null, null, null);
                    while (cur.moveToNext()) {
                        TabInfo info = new TabInfo();
                        info.setDescription(cur.getString(cur.getColumnIndex("description")));
                        info.setName(cur.getString(cur.getColumnIndex("name")));
                        info.setIdentity(cur.getString(cur.getColumnIndex("identity")));
                        info.setOrderId(cur.getInt(cur.getColumnIndex("orderId")));
                        info.setIsValid(cur.getInt(cur.getColumnIndex("isValid")));
                        tabInfoList.add(info);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });

        return tabInfoList;
    }

    public int clearTabInfos() {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    nResult = database.delete(DatabaseHelper.TAB_INFO, null, new String[]{});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return nResult;
    }

    public void saveTabInfo(final TabInfo info) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    ContentValues root = new ContentValues();
                    root.put("identity", info.getIdentity());
                    root.put("name", info.getName());
                    root.put("description", info.getDescription());
                    root.put("orderId", info.getOrderId());
                    root.put("isValid", info.getIsValid());
                    database.insert(DatabaseHelper.TAB_INFO, "name", root);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public UserInfo getUserInfo() {
        final UserInfo userInfo = new UserInfo();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABEL_USER_INFO, new String[]{"uid", "iconUrl", "name", "sex", "address", "info", "cashBalance", "coupon", "orderCount"}, null, null, null, null, null);
                    if (cur.moveToNext()) {
                        userInfo.setUid(cur.getLong(cur.getColumnIndex("uid")));
                        userInfo.setIconUrl(cur.getString(cur.getColumnIndex("iconUrl")));
                        userInfo.setName(cur.getString(cur.getColumnIndex("name")));
                        userInfo.setSex(cur.getInt(cur.getColumnIndex("sex")));
                        userInfo.setAddress(cur.getString(cur.getColumnIndex("address")));
                        userInfo.setInfo(cur.getString(cur.getColumnIndex("info")));
                        userInfo.setCashBalance(cur.getFloat(cur.getColumnIndex("cashBalance")));
                        userInfo.setCoupon(cur.getInt(cur.getColumnIndex("coupon")));
                        userInfo.setOrderCount(cur.getInt(cur.getColumnIndex("orderCount")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (userInfo.getUid() == null || userInfo.getUid() == 0)
            return null;
        return userInfo;
    }

    public int clearUserInfo() {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    nResult = database.delete(DatabaseHelper.TABEL_USER_INFO, null, new String[]{});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return nResult;
    }

    public void saveUserInfo(final UserInfo info) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    database.delete(DatabaseHelper.TABEL_USER_INFO, null, new String[]{});
                    ContentValues root = new ContentValues();
                    root.put("uid", info.getUid());
                    root.put("iconUrl", info.getIconUrl() == null ? "" : info.getIconUrl());
                    root.put("name", info.getName() == null ? "" : info.getName());
                    root.put("sex", info.getSex() == null ? -1 : info.getSex());
                    root.put("address", info.getAddress() == null ? "" : info.getAddress());
                    root.put("info", info.getInfo() == null ? "" : info.getInfo());
                    root.put("cashBalance", info.getCashBalance() == null ? 0 : info.getCashBalance());
                    root.put("coupon", info.getCoupon() == null ? 0 : info.getCoupon());
                    root.put("orderCount", info.getOrderCount() == null ? 0 : info.getOrderCount());
                    database.insert(DatabaseHelper.TABEL_USER_INFO, "uid", root);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void saveUpgradeCompanentInfos(final List<UpgradeComponent> infos) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    database.delete(DatabaseHelper.UPGRADE_COMPONENT_INFOS, null, new String[]{});
                    database.beginTransaction();
                    for (UpgradeComponent uc : infos) {
                        ContentValues root = new ContentValues();
                        root.put("comId", uc.getComId());
                        Boolean hasNew = uc.getHasNew();
                        root.put("hasNew", hasNew == null ? "false" : String.valueOf(hasNew.booleanValue()));
                        Boolean lazy = uc.getLazy();
                        root.put("lazy", lazy == null ? "true" : String.valueOf(lazy.booleanValue()));
                        root.put("downloadUrl", uc.getDownloadUrl());
                        root.put("time", "");
                        database.insert(DatabaseHelper.UPGRADE_COMPONENT_INFOS, null, root);
                    }
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (database != null)
                        database.endTransaction();
                }
            }
        });
    }

    public void saveUpgradeCompanentInfo(final UpgradeComponent info) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    String comId = info.getComId();
                    cur = database.query(
                            DatabaseHelper.UPGRADE_COMPONENT_INFOS, new String[]{"comId"}, " comId = ? ", new String[]{comId}, null, null, null);
                    if (cur.getCount() > 0) {
                        database.delete(DatabaseHelper.UPGRADE_COMPONENT_INFOS, " comId = ? ", new String[]{comId});
                    }
                    ContentValues root = new ContentValues();
                    root.put("comId", info.getComId());
                    Boolean hasNew = info.getHasNew();
                    root.put("hasNew", hasNew == null ? "false" : String.valueOf(hasNew.booleanValue()));
                    Boolean lazy = info.getLazy();
                    root.put("lazy", lazy == null ? "true" : String.valueOf(lazy.booleanValue()));
                    root.put("downloadUrl", info.getDownloadUrl());
                    Calendar calendar = Calendar.getInstance();
                    root.put("time", "" + calendar.get(Calendar.DAY_OF_YEAR));
                    database.insert(DatabaseHelper.UPGRADE_COMPONENT_INFOS, null, root);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
    }

    public int deleteUpgradeComponentInfo(final String comId) {
        DatabaseManager.initializeInstance(helper);
                    DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                        @Override
                        public void run(SQLiteDatabase database) {
                            Cursor cur = null;
                            try {
                                cur = database.query(
                            DatabaseHelper.UPGRADE_COMPONENT_INFOS, new String[]{"comId"}, " comId = ? ", new String[]{comId}, null, null, null);
                    if (cur.getCount() > 0) {
                        nResult = database.delete(DatabaseHelper.UPGRADE_COMPONENT_INFOS, " comId = ? ", new String[]{comId});
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        return nResult;
    }

    public int deleteUpgradeComponentInfos() {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    nResult = database.delete(DatabaseHelper.UPGRADE_COMPONENT_INFOS, null, new String[]{});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return nResult;
    }


    public UpgradeComponent getUpgradeComponent(final String comId) {
        final UpgradeComponent upgradeComponent = new UpgradeComponent();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.UPGRADE_COMPONENT_INFOS, null, " comId = ? ", new String[]{comId}, null, null, null);
                    if (cur.moveToNext()) {
                        upgradeComponent.setComId(comId);
                        upgradeComponent.setHasNew(Boolean.parseBoolean(cur.getString(cur.getColumnIndex("hasNew"))));
                        upgradeComponent.setLazy(Boolean.parseBoolean(cur.getString(cur.getColumnIndex("lazy"))));
                        upgradeComponent.setDownloadUrl(cur.getString(cur.getColumnIndex("downloadUrl")));
                        upgradeComponent.setTime(cur.getString(cur.getColumnIndex("time")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (!StringUtils.isEmpty(upgradeComponent.getComId()))
            return upgradeComponent;
        return null;
    }

    public List<VehicleAuth> getVehicleAuths() {
        final List<VehicleAuth> listUA = new ArrayList<VehicleAuth>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.VEHICLE_AUTHENTICATION_INFOS, null, null, null, null, null, null);
                    while (cur.moveToNext()) {
                        VehicleAuth ua = new VehicleAuth();
                        ua.setId(cur.getLong(cur.getColumnIndex("authId")));
                        ua.setVehicleNum(cur.getString(cur.getColumnIndex("vehicleNum")));
                        ua.setStatus(cur.getInt(cur.getColumnIndex("status")));
                        String belong = cur.getString(cur.getColumnIndex("belong"));
                        if (StringUtils.isEmpty(belong))
                            ua.setBelong(false);
                        else
                            ua.setBelong(belong.equals("0") ? false : true);
                        ua.setDrivingLicenceUrl(cur.getString(cur.getColumnIndex("drivingLicenceUrl")));
                        ua.setAuthDatetime(cur.getString(cur.getColumnIndex("authDatetime")));
                        ua.setOwner(cur.getString(cur.getColumnIndex("owner")));
                        ua.setBrandModel(cur.getString(cur.getColumnIndex("brandModel")));
                        ua.setMotorNum(cur.getString(cur.getColumnIndex("motorNum")));
                        ua.setFrameNum(cur.getString(cur.getColumnIndex("frameNum")));
                        ua.setIssueDate(cur.getString(cur.getColumnIndex("issueDate")));
                        ua.setResidueDegree(cur.getInt(cur.getColumnIndex("residueDegree")));
                        ua.setDescr(cur.getString(cur.getColumnIndex("descr")));
                        ua.setAdjustDate(cur.getString(cur.getColumnIndex("adjustDate")));
                        listUA.add(ua);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        return listUA;
    }

    public VehicleAuth getVehicleAuthByVehicleNumber(final String vehicleNumber) {
        final VehicleAuth vehicleAuth = new VehicleAuth();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.VEHICLE_AUTHENTICATION_INFOS, null, "vehicleNum = ? ", new String[]{vehicleNumber}, null, null, null);
                    if (cur.moveToNext()) {
                        vehicleAuth.setId(cur.getLong(cur.getColumnIndex("authId")));
                        vehicleAuth.setVehicleNum(cur.getString(cur.getColumnIndex("vehicleNum")));
                        vehicleAuth.setStatus(cur.getInt(cur.getColumnIndex("status")));
                        String belong = cur.getString(cur.getColumnIndex("belong"));
                        if (StringUtils.isEmpty(belong))
                            vehicleAuth.setBelong(false);
                        else
                            vehicleAuth.setBelong(belong.equals("0") ? false : true);
                        vehicleAuth.setDrivingLicenceUrl(cur.getString(cur.getColumnIndex("drivingLicenceUrl")));
                        vehicleAuth.setAuthDatetime(cur.getString(cur.getColumnIndex("authDatetime")));
                        vehicleAuth.setOwner(cur.getString(cur.getColumnIndex("owner")));
                        vehicleAuth.setBrandModel(cur.getString(cur.getColumnIndex("brandModel")));
                        vehicleAuth.setMotorNum(cur.getString(cur.getColumnIndex("motorNum")));
                        vehicleAuth.setFrameNum(cur.getString(cur.getColumnIndex("frameNum")));
                        vehicleAuth.setIssueDate(cur.getString(cur.getColumnIndex("issueDate")));
                        vehicleAuth.setResidueDegree(cur.getInt(cur.getColumnIndex("residueDegree")));
                        vehicleAuth.setDescr(cur.getString(cur.getColumnIndex("descr")));
                        vehicleAuth.setAdjustDate(cur.getString(cur.getColumnIndex("adjustDate")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (!StringUtils.isEmpty(vehicleAuth.getVehicleNum()))
            return vehicleAuth;
        return null;
    }

    public void saveVehicleAuths(final List<VehicleAuth> vehicleAuths) {
        if (vehicleAuths != null && !vehicleAuths.isEmpty()) {
            DatabaseManager.initializeInstance(helper);
            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    Cursor cur = null;
                    try {
                        database.beginTransaction();
                        for (VehicleAuth va : vehicleAuths) {
                            String vehicleNumber = va.getVehicleNum();
                            if (!StringUtils.isEmpty(vehicleNumber)) {
                                cur = database.query(
                                        DatabaseHelper.VEHICLE_AUTHENTICATION_INFOS, new String[]{"vehicleNum"}, " vehicleNum = ? ", new String[]{vehicleNumber}, null, null, null);
                                if (cur.getCount() > 0) {
                                    database.delete(DatabaseHelper.VEHICLE_AUTHENTICATION_INFOS, " vehicleNum = ? ", new String[]{vehicleNumber});
                                }
                                setAdjustDate(va);
                                ContentValues cv = new ContentValues();
                                cv.put("vehicleNum", va.getVehicleNum());
                                cv.put("authId", va.getId() == null ? 0 : va.getId());
                                cv.put("status", va.getStatus() == null ? 0 : va.getStatus());
                                if (va.getBelong() == null)
                                    cv.put("belong", "0");
                                else
                                    cv.put("belong", va.getBelong() ? "1" : "0");
                                cv.put("drivingLicenceUrl", StringUtils.isEmpty(va.getDrivingLicenceUrl()) ? "" : va.getDrivingLicenceUrl());
                                cv.put("authDatetime", StringUtils.isEmpty(va.getAuthDatetime()) ? "" : va.getAuthDatetime());
                                cv.put("owner", StringUtils.isEmpty(va.getOwner()) ? "" : va.getOwner());
                                cv.put("brandModel", StringUtils.isEmpty(va.getBrandModel()) ? "" : va.getBrandModel());
                                cv.put("motorNum", StringUtils.isEmpty(va.getMotorNum()) ? "" : va.getMotorNum());
                                cv.put("frameNum", StringUtils.isEmpty(va.getFrameNum()) ? "" : va.getFrameNum());
                                cv.put("issueDate", StringUtils.isEmpty(va.getIssueDate()) ? "" : va.getIssueDate());
                                cv.put("residueDegree", va.getResidueDegree() == null ? 0 : va.getResidueDegree());
                                cv.put("descr", StringUtils.isEmpty(va.getDescr()) ? "" : va.getDescr());
                                cv.put("adjustDate", StringUtils.isEmpty(va.getAdjustDate()) ? "" : va.getAdjustDate());
                                database.insert(DatabaseHelper.VEHICLE_AUTHENTICATION_INFOS, null, cv);
                            }
                        }
                        database.setTransactionSuccessful();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (database != null)
                            database.endTransaction();
                        if (cur != null && !cur.isClosed()) {
                            cur.close();
                            cur = null;
                        }
                    }
                }
            });
        }
    }

    public void saveVehicleAuth(final VehicleAuth vehicleAuth) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                String vehicleNumber = vehicleAuth.getVehicleNum();
                if (!StringUtils.isEmpty(vehicleNumber)) {
                    Cursor cur = null;
                    try {
                        cur = database.query(
                                DatabaseHelper.VEHICLE_AUTHENTICATION_INFOS, new String[]{"vehicleNum"}, " vehicleNum = ? ", new String[]{vehicleNumber}, null, null, null);
                        if (cur.getCount() > 0) {
                            database.delete(DatabaseHelper.VEHICLE_AUTHENTICATION_INFOS, " vehicleNum = ? ", new String[]{vehicleNumber});
                        }
                        setAdjustDate(vehicleAuth);
                        ContentValues cv = new ContentValues();
                        cv.put("vehicleNum", vehicleAuth.getVehicleNum());
                        cv.put("authId", vehicleAuth.getId() == null ? 0 : vehicleAuth.getId());
                        cv.put("status", vehicleAuth.getStatus() == null ? 0 : vehicleAuth.getStatus());
                        if (vehicleAuth.getBelong() == null)
                            cv.put("belong", "0");
                        else
                            cv.put("belong", vehicleAuth.getBelong() ? "1" : "0");
                        cv.put("drivingLicenceUrl", StringUtils.isEmpty(vehicleAuth.getDrivingLicenceUrl()) ? "" : vehicleAuth.getDrivingLicenceUrl());
                        cv.put("authDatetime", StringUtils.isEmpty(vehicleAuth.getAuthDatetime()) ? "" : vehicleAuth.getAuthDatetime());
                        cv.put("owner", StringUtils.isEmpty(vehicleAuth.getOwner()) ? "" : vehicleAuth.getOwner());
                        cv.put("brandModel", StringUtils.isEmpty(vehicleAuth.getBrandModel()) ? "" : vehicleAuth.getBrandModel());
                        cv.put("motorNum", StringUtils.isEmpty(vehicleAuth.getMotorNum()) ? "" : vehicleAuth.getMotorNum());
                        cv.put("frameNum", StringUtils.isEmpty(vehicleAuth.getFrameNum()) ? "" : vehicleAuth.getFrameNum());
                        cv.put("issueDate", StringUtils.isEmpty(vehicleAuth.getIssueDate()) ? "" : vehicleAuth.getIssueDate());
                        cv.put("residueDegree", vehicleAuth.getResidueDegree() == null ? 0 : vehicleAuth.getResidueDegree());
                        cv.put("descr", StringUtils.isEmpty(vehicleAuth.getDescr()) ? "" : vehicleAuth.getDescr());
                        cv.put("adjustDate", StringUtils.isEmpty(vehicleAuth.getAdjustDate()) ? "" : vehicleAuth.getAdjustDate());
                        database.insert(DatabaseHelper.VEHICLE_AUTHENTICATION_INFOS, null, cv);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (cur != null && !cur.isClosed()) {
                            cur.close();
                            cur = null;
                        }
                    }
                }
            }
        });
    }

    private void setAdjustDate(VehicleAuth va) {
        try {
            String issueDateTemp = va.getIssueDate();
            if (!StringUtils.isEmpty(issueDateTemp) && StringUtils.isEmpty(va.getAdjustDate())) {
                String[] params = issueDateTemp.split("-");
                int year = Integer.parseInt(params[0]);
                year++;
                params[0] = String.valueOf(year);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < params.length; i++) {
                    sb.append(i == 0 ? params[i] : "-" + params[i]);
                }
                va.setAdjustDate(sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int deleteVehicleAuthByVehicleNumber(final String vehicleNumber) {
        if (!StringUtils.isEmpty(vehicleNumber)) {
            DatabaseManager.initializeInstance(helper);
            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    Cursor cur = null;
                    try {
                        cur = database.query(DatabaseHelper.VEHICLE_AUTHENTICATION_INFOS, new String[]{"vehicleNum"}, " vehicleNum = ? ", new String[]{vehicleNumber}, null, null, null);
                        if (cur.getCount() > 0) {
                            nResult = database.delete(DatabaseHelper.VEHICLE_AUTHENTICATION_INFOS, " vehicleNum = ? ", new String[]{vehicleNumber});
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (cur != null && !cur.isClosed()) {
                            cur.close();
                            cur = null;
                        }
                    }
                }
            });
        }
        return nResult;
    }

    public Account getAccountByType(final int type) {
        final Account mAccount = new Account();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.THIRD_PART_ACCOUNTS, null, " type = ? ", new String[]{"" + type}, null, null, null);
                    if (cur.moveToNext()) {
                        mAccount.setUserName(cur.getString(cur.getColumnIndex("userName")));
                        mAccount.setType(cur.getInt(cur.getColumnIndex("type")));
                        mAccount.setPid(cur.getLong(cur.getColumnIndex("pid")));
                        mAccount.setNickname(cur.getString(cur.getColumnIndex("nickname")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (!StringUtils.isEmpty(mAccount.getUserName()))
            return mAccount;
        return null;
    }

    public List<Account> getAccounts() {
        final List<Account> accounts = new ArrayList<Account>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.THIRD_PART_ACCOUNTS, null, null, null, null, null, null);
                    while (cur.moveToNext()) {
                        Account account = new Account();
                        account.setUserName(cur.getString(cur.getColumnIndex("userName")));
                        account.setType(cur.getInt(cur.getColumnIndex("type")));
                        account.setPid(cur.getLong(cur.getColumnIndex("pid")));
                        account.setNickname(cur.getString(cur.getColumnIndex("nickname")));
                        accounts.add(account);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        return accounts;
    }

    public void saveAccounts(final List<Account> accounts) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    database.beginTransaction();
                    for (Account account : accounts) {
                        cur = database.query(DatabaseHelper.THIRD_PART_ACCOUNTS, new String[]{"type"}, " type = ? ", new String[]{"" + account.getType().intValue()}, null, null, null);
                        if (cur.getCount() > 0) {
                            database.delete(DatabaseHelper.THIRD_PART_ACCOUNTS, " type = ? ", new String[]{"" + account.getType().intValue()});
                        }
                        cur.close();
                        ContentValues cv = new ContentValues();
                        cv.put("userName", account.getUserName());
                        cv.put("type", account.getType());
                        if (account.getPid() != null)
                            cv.put("pid", account.getPid());
                        else
                            cv.put("pid", 0);
                        cv.put("nickname", account.getNickname() == null ? "" : account.getNickname());
                        database.insert(DatabaseHelper.THIRD_PART_ACCOUNTS, null, cv);
                    }
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (database != null)
                        database.endTransaction();
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
    }

    public int deleteAccountByType(final int type) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.THIRD_PART_ACCOUNTS, new String[]{"type"}, " type = ? ", new String[]{"" + type}, null, null, null);
                    if (cur.getCount() > 0) {
                        nResult = database.delete(DatabaseHelper.THIRD_PART_ACCOUNTS, " type = ? ", new String[]{"" + type});
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        return nResult;
    }

    public void saveAccount(final Account account) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.THIRD_PART_ACCOUNTS, new String[]{"type"}, " type = ? ", new String[]{"" + account.getType().intValue()}, null, null, null);
                    if (cur.getCount() > 0) {
                        database.delete(DatabaseHelper.THIRD_PART_ACCOUNTS, " type = ? ", new String[]{"" + account.getType().intValue()});
                    }
                    ContentValues cv = new ContentValues();
                    cv.put("userName", account.getUserName());
                    cv.put("type", account.getType());
                    if (account.getPid() != null)
                        cv.put("pid", account.getPid());
                    else
                        cv.put("pid", 0);
                    cv.put("nickname", account.getNickname() == null ? "" : account.getNickname());
                    database.insert(DatabaseHelper.THIRD_PART_ACCOUNTS, null, cv);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
    }

    public int deleteAccounts() {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    nResult = database.delete(DatabaseHelper.THIRD_PART_ACCOUNTS, null, new String[]{});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return nResult;
    }

    public void saveDazeUserAccount(final DazeUserAccount dua) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.DAZE_USER_ACCOUNT, new String[]{"uid"}, " uid = ? ", new String[]{"" + dua.getUid()}, null, null, null);
                    if (cur.getCount() > 0)
                        database.delete(DatabaseHelper.DAZE_USER_ACCOUNT, " uid = ? ", new String[]{"" + dua.getUid()});
                    ContentValues cv = new ContentValues();
                    cv.put("uid", dua.getUid());
                    cv.put("nickName", dua.getNickName());
                    cv.put("phoneNumber", dua.getPhoneNumber());
                    cv.put("phoneLoginName", dua.getPhoneLoginName());
                    cv.put("qqLoginName", dua.getQqLoginName());
                    cv.put("wechatLoginName", dua.getWechatLoginName());
                    cv.put("weiboLoginName", dua.getWeiboLoginName());
                    cv.put("creatDatetime", dua.getCreatDatetime());
                    cv.put("qqCreatDatetime", dua.getQqCreatDatetime());
                    cv.put("wechatCreatDatetime", dua.getWechatCreatDatetime());
                    cv.put("weiboCreatDatetime", dua.getWeiboCreatDatetime());
                    cv.put("status", dua.getStatus());
                    database.insert(DatabaseHelper.DAZE_USER_ACCOUNT, null, cv);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
    }

    public void saveDazeUserAccounts(final List<DazeUserAccount> duas) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                if (duas != null && !duas.isEmpty()) {
                    Cursor cur = null;
                    try {
                        for (DazeUserAccount dua : duas) {
                            cur = database.query(DatabaseHelper.DAZE_USER_ACCOUNT, new String[]{"uid"}, " uid = ? ", new String[]{"" + dua.getUid()}, null, null, null);
                            if (cur.getCount() > 0)
                                database.delete(DatabaseHelper.DAZE_USER_ACCOUNT, " uid = ? ", new String[]{"" + dua.getUid()});
                            ContentValues cv = new ContentValues();
                            cv.put("uid", dua.getUid());
                            cv.put("nickName", dua.getNickName());
                            cv.put("phoneNumber", dua.getPhoneNumber());
                            cv.put("phoneLoginName", dua.getPhoneLoginName());
                            cv.put("qqLoginName", dua.getQqLoginName());
                            cv.put("wechatLoginName", dua.getWechatLoginName());
                            cv.put("weiboLoginName", dua.getWeiboLoginName());
                            cv.put("creatDatetime", dua.getCreatDatetime());
                            cv.put("qqCreatDatetime", dua.getQqCreatDatetime());
                            cv.put("wechatCreatDatetime", dua.getWechatCreatDatetime());
                            cv.put("weiboCreatDatetime", dua.getWeiboCreatDatetime());
                            cv.put("status", dua.getStatus());
                            database.insert(DatabaseHelper.DAZE_USER_ACCOUNT, null, cv);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (cur != null && !cur.isClosed()) {
                            cur.close();
                            cur = null;
                        }
                    }
                }
            }
        });
    }

    public DazeUserAccount getDazeUserAccount(final long uid) {
        final DazeUserAccount dazeUserAccount = new DazeUserAccount();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.DAZE_USER_ACCOUNT, null, " uid = ? ", new String[]{"" + uid}, null, null, null);
                    if (cur.moveToFirst()) {
                        dazeUserAccount.setUid(cur.getLong(cur.getColumnIndex("uid")));
                        dazeUserAccount.setNickName(cur.getString(cur.getColumnIndex("nickName")));
                        dazeUserAccount.setPhoneNumber(cur.getString(cur.getColumnIndex("phoneNumber")));
                        dazeUserAccount.setPhoneLoginName(cur.getString(cur.getColumnIndex("phoneLoginName")));
                        dazeUserAccount.setQqLoginName(cur.getString(cur.getColumnIndex("qqLoginName")));
                        dazeUserAccount.setWechatLoginName(cur.getString(cur.getColumnIndex("wechatLoginName")));
                        dazeUserAccount.setWeiboLoginName(cur.getString(cur.getColumnIndex("weiboLoginName")));
                        dazeUserAccount.setCreatDatetime(cur.getString(cur.getColumnIndex("creatDatetime")));
                        dazeUserAccount.setQqCreatDatetime(cur.getString(cur.getColumnIndex("qqCreatDatetime")));
                        dazeUserAccount.setWechatCreatDatetime(cur.getString(cur.getColumnIndex("wechatCreatDatetime")));
                        dazeUserAccount.setWeiboCreatDatetime(cur.getString(cur.getColumnIndex("weiboCreatDatetime")));
                        dazeUserAccount.setStatus(cur.getInt(cur.getColumnIndex("status")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (dazeUserAccount.getUid() == null || dazeUserAccount.getUid() == 0)
            return null;
        return dazeUserAccount;
    }

    public List<DazeUserAccount> getDazeUserAccounts() {
        final List<DazeUserAccount> duas = new ArrayList<DazeUserAccount>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.DAZE_USER_ACCOUNT, null, null, null, null, null, null);
                    while (cur.moveToNext()) {
                        DazeUserAccount dua = new DazeUserAccount();
                        dua.setUid(cur.getLong(cur.getColumnIndex("uid")));
                        dua.setNickName(cur.getString(cur.getColumnIndex("nickName")));
                        dua.setPhoneNumber(cur.getString(cur.getColumnIndex("phoneNumber")));
                        dua.setPhoneLoginName(cur.getString(cur.getColumnIndex("phoneLoginName")));
                        dua.setQqLoginName(cur.getString(cur.getColumnIndex("qqLoginName")));
                        dua.setWechatLoginName(cur.getString(cur.getColumnIndex("wechatLoginName")));
                        dua.setWeiboLoginName(cur.getString(cur.getColumnIndex("weiboLoginName")));
                        dua.setCreatDatetime(cur.getString(cur.getColumnIndex("creatDatetime")));
                        dua.setQqCreatDatetime(cur.getString(cur.getColumnIndex("qqCreatDatetime")));
                        dua.setWechatCreatDatetime(cur.getString(cur.getColumnIndex("wechatCreatDatetime")));
                        dua.setWeiboCreatDatetime(cur.getString(cur.getColumnIndex("weiboCreatDatetime")));
                        dua.setStatus(cur.getInt(cur.getColumnIndex("status")));
                        duas.add(dua);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        return duas;
    }

    public void deleteDazeUserAccount(final long uid) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    database.delete(DatabaseHelper.DAZE_USER_ACCOUNT, " uid = ? ", new String[]{"" + uid});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void deleteDazeUserAccounts() {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    database.delete(DatabaseHelper.DAZE_USER_ACCOUNT, null, new String[]{});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void saveSelfDefineVehicleMode(final VehicleModel vm) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    ContentValues cv = new ContentValues();
                    cv.put("modeId", vm.getId() == null ? 0 : vm.getId());
                    cv.put("name", vm.getName() == null ? "" : vm.getName());
                    cv.put("image", vm.getImage() == null ? "" : vm.getImage());
                    cv.put("classfy", vm.getClassfy() == null ? "" : vm.getClassfy());
                    cv.put("brandId", vm.getBrandId());
                    database.insert(DatabaseHelper.DAZE_VEHICLE_MODE_SELF_DEFINE, null, cv);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void deleteDefineVehicleMode(final VehicleModel vm) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    database.delete(DatabaseHelper.DAZE_VEHICLE_MODE_SELF_DEFINE, " brandId = ? and name = ? ", new String[]{"" + vm.getBrandId(), vm.getName()});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public List<VehicleModel> getSelfDefineModesByBrandId(final long brandId) {
        final List<VehicleModel> result = new ArrayList<VehicleModel>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.DAZE_VEHICLE_MODE_SELF_DEFINE, null, " brandId = ? ", new String[]{"" + brandId}, null, null, null);
                    while (cur.moveToNext()) {
                        VehicleModel vm = new VehicleModel();
                        vm.setBrandId(cur.getLong(cur.getColumnIndex("modeId")));
                        vm.setName(cur.getString(cur.getColumnIndex("name")));
                        vm.setImage(cur.getString(cur.getColumnIndex("image")));
                        vm.setClassfy(cur.getString(cur.getColumnIndex("classfy")));
                        vm.setBrandId(cur.getLong(cur.getColumnIndex("brandId")));
                        result.add(vm);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        return result;
    }

    public void saveActivities(final List<ActivityInfo> activities) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    database.beginTransaction();
                    for (ActivityInfo ac : activities) {
                        ContentValues cv = new ContentValues();
                        Boolean valid = ac.getValid();
                        if (valid != null && valid == true)
                            cv.put("valid", 1);
                        else
                            cv.put("valid", 0);
                        cv.put("link", StringUtils.isEmpty(ac.getLink()) ? "" : ac.getLink());
                        cv.put("img", StringUtils.isEmpty(ac.getImg()) ? "" : ac.getImg());
                        cv.put("content", StringUtils.isEmpty(ac.getContent()) ? "" : ac.getContent());
                        cv.put("title", StringUtils.isEmpty(ac.getTitle()) ? "" : ac.getTitle());
                        cv.put("summary", StringUtils.isEmpty(ac.getSummary()) ? "" : ac.getSummary());
                        cv.put("startTime", StringUtils.isEmpty(ac.getStartTime()) ? "" : ac.getStartTime());
                        cv.put("endTime", StringUtils.isEmpty(ac.getEndTime()) ? "" : ac.getEndTime());
                        database.insert(DatabaseHelper.DAZE_ACTIVITIES, null, cv);
                    }
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (database != null)
                        database.endTransaction();
                }
            }
        });
    }

    public List<ActivityInfo> getActivities() {
        final List<ActivityInfo> activities = new ArrayList<ActivityInfo>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.DAZE_ACTIVITIES, null, null, null, null, null, null);
                    while (cur.moveToNext()) {
                        ActivityInfo ai = new ActivityInfo();
                        ai.setValid(cur.getInt(cur.getColumnIndex("valid")) == 1 ? true : false);
                        ai.setLink(cur.getString(cur.getColumnIndex("link")));
                        ai.setImg(cur.getString(cur.getColumnIndex("img")));
                        ai.setContent(cur.getString(cur.getColumnIndex("content")));
                        ai.setTitle(cur.getString(cur.getColumnIndex("title")));
                        ai.setSummary(cur.getString(cur.getColumnIndex("summary")));
                        ai.setStartTime(cur.getString(cur.getColumnIndex("startTime")));
                        ai.setEndTime(cur.getString(cur.getColumnIndex("endTime")));
                        activities.add(ai);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        return activities;
    }

    public void saveVehicleTypes(final List<CommonDictionary> list) {
        if (list != null && !list.isEmpty()) {
            DatabaseManager.initializeInstance(helper);
            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    Cursor cur = null;
                    try {
                        database.beginTransaction();
                        for (CommonDictionary cd : list) {
                            cur = database.query(DatabaseHelper.DAZE_VEHICLE_TYPES, new String[]{"typeId"}, " typeId = ? ", new String[]{"" + cd.getId()}, null, null, null);
                            if (cur.getCount() > 0) {
                                database.delete(DatabaseHelper.DAZE_VEHICLE_TYPES, " typeId = ? ", new String[]{"" + cd.getId()});
                            }
                            if (cd.getId() != null && cd.getType() != null && cd.getValue() != null) {
                                ContentValues cv = new ContentValues();
                                cv.put("typeId", cd.getId());
                                cv.put("type", cd.getType());
                                cv.put("value", cd.getValue());
                                database.insert(DatabaseHelper.DAZE_VEHICLE_TYPES, null, cv);
                            }
                            cur.close();
                        }
                        database.setTransactionSuccessful();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (database != null)
                            database.endTransaction();
                        if (cur != null && !cur.isClosed()) {
                            cur.close();
                            cur = null;
                        }
                    }
                }
            });
        }
    }

    public List<CommonDictionary> getVehicleTypes() {
        final List<CommonDictionary> list = new ArrayList<CommonDictionary>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.DAZE_VEHICLE_TYPES, null, null, null, null, null, null);
                    while (cur.moveToNext()) {
                        CommonDictionary cd = new CommonDictionary();
                        cd.setId(cur.getLong(cur.getColumnIndex("typeId")));
                        cd.setType(cur.getString(cur.getColumnIndex("type")));
                        cd.setValue(cur.getString(cur.getColumnIndex("value")));
                        list.add(cd);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        return list;
    }

    public void saveHistoryFrameAndMotor(final HistoryFrameAndMotor history) {
        if (history == null || StringUtils.isEmpty(history.getVehicleNum()))
            return;
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.DAZE_HISTORY_FRAME_AND_MOTOR, null, " vehicleNum = ? ", new String[]{history.getVehicleNum()}, null, null, null);
                    if (cur.getCount() > 0) {
                        database.delete(DatabaseHelper.DAZE_HISTORY_FRAME_AND_MOTOR, " vehicleNum = ? ", new String[]{history.getVehicleNum()});
                    }
                    cur.close();
                    cur = null;
                    ContentValues cv = new ContentValues();
                    cv.put("vehicleNum", history.getVehicleNum());
                    cv.put("historyFrame", history.getHistoryFrame());
                    cv.put("historyMotor", history.getHistoryMotor());
                    database.insert(DatabaseHelper.DAZE_HISTORY_FRAME_AND_MOTOR, null, cv);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
    }

    public HistoryFrameAndMotor getHistoryFrameAndMotor(final String vehicleNum) {
        if (StringUtils.isEmpty(vehicleNum))
            return null;
        final HistoryFrameAndMotor result = new HistoryFrameAndMotor();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.DAZE_HISTORY_FRAME_AND_MOTOR, null, " vehicleNum = ? ", new String[]{vehicleNum}, null, null, null);
                    if (cur.moveToFirst()) {
                        result.setVehicleNum(cur.getString(cur.getColumnIndex("vehicleNum")));
                        result.setHistoryFrame(cur.getString(cur.getColumnIndex("historyFrame")));
                        result.setHistoryMotor(cur.getString(cur.getColumnIndex("historyMotor")));
                    }
                    cur.close();
                    cur = null;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (result.getVehicleNum() == null)
            return null;
        return result;
    }

    public void saveRemindChexian(final List<RemindChexian> list) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    database.beginTransaction();
                    database.delete(DatabaseHelper.TABLE_NAME_REMIND_CHEXIAN, null, null);
                    if (list != null) {
                        for (RemindChexian chexian : list) {
                            if (chexian == null)
                                continue;
                            ContentValues root = new ContentValues();
                            root.put("vehicleNum", chexian.getVehicleNum());
                            root.put("type", chexian.getType());
                            root.put("money", chexian.getMoney());
                            root.put("baodanhao", chexian.getBaodanhao());
                            root.put("photo", chexian.getPhoto());
                            root.put("date", chexian.getDate());
                            root.put("remindDate1", chexian.getRemindDate1());
                            root.put("remindDate2", chexian.getRemindDate2());
                            root.put("repeatType", chexian.getRepeatType());
                            root.put("orignalDate", chexian.getOrignalDate());
                            root.put("same", chexian.getSame());
                            root.put("fromType", chexian.getFromType());
                            database.insert(DatabaseHelper.TABLE_NAME_REMIND_CHEXIAN, "id", root);
                        }
                    }
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (database != null)
                        database.endTransaction();
                }
            }
        });
    }

    public void saveRemindChexian(final RemindChexian chexian) {
        if (chexian == null)
            return;
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    String vehicleNum = chexian.getVehicleNum();
                    database.delete(DatabaseHelper.TABLE_NAME_REMIND_CHEXIAN, " vehicleNum = ? and type = ? ", new String[]{vehicleNum, String.valueOf(chexian.getType())});
                    ContentValues root = new ContentValues();
                    root.put("vehicleNum", chexian.getVehicleNum());
                    root.put("type", chexian.getType());
                    root.put("money", chexian.getMoney());
                    root.put("baodanhao", chexian.getBaodanhao());
                    root.put("photo", chexian.getPhoto());
                    root.put("date", chexian.getDate());
                    root.put("remindDate1", chexian.getRemindDate1());
                    root.put("remindDate2", chexian.getRemindDate2());
                    root.put("repeatType", chexian.getRepeatType());
                    root.put("orignalDate", chexian.getOrignalDate());
                    root.put("same", chexian.getSame());
                    root.put("fromType", chexian.getFromType());
                    database.insert(DatabaseHelper.TABLE_NAME_REMIND_CHEXIAN, "id", root);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        });
    }

    public void updateRemindChexian(final RemindChexian chexian) {
        if (chexian == null)
            return;
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    String[] args = {chexian.getVehicleNum(), String.valueOf(chexian.getType())};
                    ContentValues root = new ContentValues();
                    root.put("vehicleNum", chexian.getVehicleNum());
                    root.put("type", chexian.getType());
                    root.put("money", chexian.getMoney());
                    root.put("baodanhao", chexian.getBaodanhao());
                    root.put("photo", chexian.getPhoto());
                    root.put("date", chexian.getDate());
                    root.put("remindDate1", chexian.getRemindDate1());
                    root.put("remindDate2", chexian.getRemindDate2());
                    root.put("repeatType", chexian.getRepeatType());
                    root.put("orignalDate", chexian.getOrignalDate());
                    root.put("same", chexian.getSame());
                    root.put("fromType", chexian.getFromType());
                    database.update(DatabaseHelper.TABLE_NAME_REMIND_CHEXIAN, root, " vehicleNum = ? and type = ? ", args);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        });
    }

    public int deleteRemindChexian(final String vehicleNum) {
        if (!StringUtils.isEmpty(vehicleNum)) {
            DatabaseManager.initializeInstance(helper);
            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    try {
                        nResult = database.delete(DatabaseHelper.TABLE_NAME_REMIND_CHEXIAN, " vehicleNum = ? ", new String[]{vehicleNum});
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                    }
                }
            });
        }
        return nResult;
    }

    public RemindChexian getRemindChexian(final String vehicleNum, final int type) {
        final RemindChexian chexian = new RemindChexian();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_REMIND_CHEXIAN,
                            new String[]{"id", "vehicleNum", "type", "money", "baodanhao", "photo", "date", "remindDate1",
                                    "remindDate2", "repeatType", "orignalDate", "same", "fromType"}, " vehicleNum = ? and type = ? ",
                            new String[]{vehicleNum, String.valueOf(type)}, null, null, null);
                    if (cur.moveToNext()) {
                        chexian.setId(cur.getInt(cur.getColumnIndex("id")));
                        chexian.setVehicleNum(cur.getString(cur.getColumnIndex("vehicleNum")));
                        chexian.setType(cur.getInt(cur.getColumnIndex("type")));
                        chexian.setMoney(cur.getInt(cur.getColumnIndex("money")));
                        chexian.setBaodanhao(cur.getString(cur.getColumnIndex("baodanhao")));
                        chexian.setPhoto(cur.getString(cur.getColumnIndex("photo")));
                        chexian.setDate(cur.getString(cur.getColumnIndex("date")));
                        chexian.setRemindDate1(cur.getString(cur.getColumnIndex("remindDate1")));
                        chexian.setRemindDate2(cur.getString(cur.getColumnIndex("remindDate2")));
                        chexian.setRepeatType(cur.getInt(cur.getColumnIndex("repeatType")));
                        chexian.setOrignalDate(cur.getString(cur.getColumnIndex("orignalDate")));
                        chexian.setSame(cur.getInt(cur.getColumnIndex("same")));
                        chexian.setFromType(cur.getInt(cur.getColumnIndex("fromType")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (!StringUtils.isEmpty(chexian.getVehicleNum()))
            return chexian;
        return null;
    }

    public List<RemindChexian> getRemindChexian(final String vehicleNum) {
        final List<RemindChexian> list = new ArrayList<RemindChexian>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_REMIND_CHEXIAN,
                            new String[]{"id", "vehicleNum", "type", "money", "baodanhao", "photo", "date", "remindDate1",
                                    "remindDate2", "repeatType", "orignalDate", "same", "fromType"}, " vehicleNum = ? ",
                            new String[]{vehicleNum}, null, null, null);
                    while (cur.moveToNext()) {
                        RemindChexian chexian = new RemindChexian();
                        chexian.setId(cur.getInt(cur.getColumnIndex("id")));
                        chexian.setVehicleNum(cur.getString(cur.getColumnIndex("vehicleNum")));
                        chexian.setType(cur.getInt(cur.getColumnIndex("type")));
                        chexian.setMoney(cur.getInt(cur.getColumnIndex("money")));
                        chexian.setBaodanhao(cur.getString(cur.getColumnIndex("baodanhao")));
                        chexian.setPhoto(cur.getString(cur.getColumnIndex("photo")));
                        chexian.setDate(cur.getString(cur.getColumnIndex("date")));
                        chexian.setRemindDate1(cur.getString(cur.getColumnIndex("remindDate1")));
                        chexian.setRemindDate2(cur.getString(cur.getColumnIndex("remindDate2")));
                        chexian.setRepeatType(cur.getInt(cur.getColumnIndex("repeatType")));
                        chexian.setOrignalDate(cur.getString(cur.getColumnIndex("orignalDate")));
                        chexian.setSame(cur.getInt(cur.getColumnIndex("same")));
                        chexian.setFromType(cur.getInt(cur.getColumnIndex("fromType")));
                        list.add(chexian);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (list.size() > 0)
            return list;
        return null;
    }

    public List<RemindChexian> getRemindChexian() {
        final List<RemindChexian> list = new ArrayList<RemindChexian>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_REMIND_CHEXIAN,
                            new String[]{"id", "vehicleNum", "type", "money", "baodanhao", "photo", "date", "remindDate1",
                                    "remindDate2", "repeatType", "orignalDate", "same", "fromType"}, null,
                            null, null, null, null);
                    while (cur.moveToNext()) {
                        RemindChexian chexian = new RemindChexian();
                        chexian.setId(cur.getInt(cur.getColumnIndex("id")));
                        chexian.setVehicleNum(cur.getString(cur.getColumnIndex("vehicleNum")));
                        chexian.setType(cur.getInt(cur.getColumnIndex("type")));
                        chexian.setMoney(cur.getInt(cur.getColumnIndex("money")));
                        chexian.setBaodanhao(cur.getString(cur.getColumnIndex("baodanhao")));
                        chexian.setPhoto(cur.getString(cur.getColumnIndex("photo")));
                        chexian.setDate(cur.getString(cur.getColumnIndex("date")));
                        chexian.setRemindDate1(cur.getString(cur.getColumnIndex("remindDate1")));
                        chexian.setRemindDate2(cur.getString(cur.getColumnIndex("remindDate2")));
                        chexian.setRepeatType(cur.getInt(cur.getColumnIndex("repeatType")));
                        chexian.setOrignalDate(cur.getString(cur.getColumnIndex("orignalDate")));
                        chexian.setSame(cur.getInt(cur.getColumnIndex("same")));
                        chexian.setFromType(cur.getInt(cur.getColumnIndex("fromType")));
                        list.add(chexian);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (list.size() > 0)
            return list;
        return null;
    }

    public void saveRemindNianjian(final List<RemindNianjian> list) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    database.beginTransaction();
                    database.delete(DatabaseHelper.TABLE_NAME_REMIND_NIANJIAN, null, null);
                    if (list != null) {
                        for (RemindNianjian nianjian : list) {
                            if (nianjian == null)
                                continue;
                            ContentValues root = new ContentValues();
                            root.put("vehicleNum", nianjian.getVehicleNum());
                            root.put("date", nianjian.getDate());
                            root.put("remindDate1", nianjian.getRemindDate1());
                            root.put("remindDate2", nianjian.getRemindDate2());
                            root.put("repeatType", nianjian.getRepeatType());
                            root.put("orignalDate", nianjian.getOrignalDate());
                            root.put("fromType", nianjian.getFromType());
                            database.insert(DatabaseHelper.TABLE_NAME_REMIND_NIANJIAN, "id", root);
                        }
                    }
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (database != null)
                        database.endTransaction();
                }
            }
        });
    }

    public void saveRemindNianjian(final RemindNianjian nianjian) {
        if (nianjian == null)
            return;
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    String vehicleNum = nianjian.getVehicleNum();
                    database.delete(DatabaseHelper.TABLE_NAME_REMIND_NIANJIAN, " vehicleNum = ? ", new String[]{vehicleNum});
                    ContentValues root = new ContentValues();
                    root.put("vehicleNum", nianjian.getVehicleNum());
                    root.put("date", nianjian.getDate());
                    root.put("remindDate1", nianjian.getRemindDate1());
                    root.put("remindDate2", nianjian.getRemindDate2());
                    root.put("repeatType", nianjian.getRepeatType());
                    root.put("orignalDate", nianjian.getOrignalDate());
                    root.put("fromType", nianjian.getFromType());
                    database.insert(DatabaseHelper.TABLE_NAME_REMIND_NIANJIAN, "id", root);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        });
    }

    public void updateRemindNianjian(final RemindNianjian nianjian) {
        if (nianjian == null)
            return;
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    String[] args = {nianjian.getVehicleNum()};
                    ContentValues root = new ContentValues();
                    root.put("vehicleNum", nianjian.getVehicleNum());
                    root.put("date", nianjian.getDate());
                    root.put("remindDate1", nianjian.getRemindDate1());
                    root.put("remindDate2", nianjian.getRemindDate2());
                    root.put("repeatType", nianjian.getRepeatType());
                    root.put("orignalDate", nianjian.getOrignalDate());
                    root.put("fromType", nianjian.getFromType());
                    database.update(DatabaseHelper.TABLE_NAME_REMIND_NIANJIAN, root, " vehicleNum = ? ", args);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public int deleteRemindNianjian(final String vehicleNum) {
        if (!StringUtils.isEmpty(vehicleNum)) {
            DatabaseManager.initializeInstance(helper);
            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    try {
                        nResult = database.delete(DatabaseHelper.TABLE_NAME_REMIND_NIANJIAN, " vehicleNum = ? ", new String[]{vehicleNum});
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                    }
                }
            });
        }
        return nResult;
    }

    public RemindNianjian getRemindNianjian(final String vehicleNum) {
        final RemindNianjian nianjian = new RemindNianjian();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_REMIND_NIANJIAN,
                            new String[]{"id", "vehicleNum", "date", "remindDate1",
                                    "remindDate2", "repeatType", "orignalDate", "fromType"}, " vehicleNum = ? ",
                            new String[]{vehicleNum}, null, null, null);
                    if (cur.moveToNext()) {
                        nianjian.setId(cur.getInt(cur.getColumnIndex("id")));
                        nianjian.setVehicleNum(cur.getString(cur.getColumnIndex("vehicleNum")));
                        nianjian.setDate(cur.getString(cur.getColumnIndex("date")));
                        nianjian.setRemindDate1(cur.getString(cur.getColumnIndex("remindDate1")));
                        nianjian.setRemindDate2(cur.getString(cur.getColumnIndex("remindDate2")));
                        nianjian.setRepeatType(cur.getInt(cur.getColumnIndex("repeatType")));
                        nianjian.setOrignalDate(cur.getString(cur.getColumnIndex("orignalDate")));
                        nianjian.setFromType(cur.getInt(cur.getColumnIndex("fromType")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (!StringUtils.isEmpty(nianjian.getVehicleNum()))
            return nianjian;
        return null;
    }

    public List<RemindNianjian> getRemindNianjian() {
        final List<RemindNianjian> list = new ArrayList<RemindNianjian>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_REMIND_NIANJIAN,
                            new String[]{"id", "vehicleNum", "date", "remindDate1",
                                    "remindDate2", "repeatType", "orignalDate", "fromType"}, null,
                            null, null, null, null);
                    while (cur.moveToNext()) {
                        RemindNianjian nianjian = new RemindNianjian();
                        nianjian.setId(cur.getInt(cur.getColumnIndex("id")));
                        nianjian.setVehicleNum(cur.getString(cur.getColumnIndex("vehicleNum")));
                        nianjian.setDate(cur.getString(cur.getColumnIndex("date")));
                        nianjian.setRemindDate1(cur.getString(cur.getColumnIndex("remindDate1")));
                        nianjian.setRemindDate2(cur.getString(cur.getColumnIndex("remindDate2")));
                        nianjian.setRepeatType(cur.getInt(cur.getColumnIndex("repeatType")));
                        nianjian.setOrignalDate(cur.getString(cur.getColumnIndex("orignalDate")));
                        nianjian.setFromType(cur.getInt(cur.getColumnIndex("fromType")));
                        list.add(nianjian);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (list.size() > 0)
            return list;
        return null;
    }

    public void updateRemindInfo(final RemindInfo info) {
        if (info == null)
            return;
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    String[] args = new String[]{info.getVehicleNum(), String.valueOf(info.getId())};
                    ContentValues root = new ContentValues();
                    root.put("vehicleNum", info.getVehicleNum());
                    root.put("title", info.getTitle());
                    root.put("desc", info.getDesc());
                    root.put("type", info.getType());
                    root.put("isHidden", info.isHidden() ? "1" : "0");
                    root.put("position", info.getPosition());
                    database.update(DatabaseHelper.TABLE_NAME_REMIND_INFO, root, " vehicleNum = ? and id = ? ", args);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        });
    }

    public void saveRemindInfo(final List<RemindInfo> list) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    database.beginTransaction();
                    database.delete(DatabaseHelper.TABLE_NAME_REMIND_INFO, null, null);
                    if (list != null) {
                        for (int i = 0; i < list.size(); i++) {
                            RemindInfo info = list.get(i);
                            ContentValues root = new ContentValues();
                            root.put("vehicleNum", info.getVehicleNum());
                            root.put("title", info.getTitle());
                            root.put("desc", info.getDesc());
                            root.put("type", info.getType());
                            root.put("isHidden", info.isHidden() ? "1" : "0");
                            root.put("position", info.getPosition());
                            database.insert(DatabaseHelper.TABLE_NAME_REMIND_INFO, "id", root);
                        }
                    }
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (null != database)
                        database.endTransaction();
                }
            }
        });
    }

    public void saveRemindInfo(final List<RemindInfo> list, final String vehicleNum) {
        if (list == null)
            return;
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    database.beginTransaction();
                    database.delete(DatabaseHelper.TABLE_NAME_REMIND_INFO, " vehicleNum = ? ", new String[]{vehicleNum});
                    for (int i = 0; i < list.size(); i++) {
                        RemindInfo info = list.get(i);
                        ContentValues root = new ContentValues();
                        root.put("vehicleNum", info.getVehicleNum());
                        root.put("title", info.getTitle());
                        root.put("desc", info.getDesc());
                        root.put("type", info.getType());
                        root.put("isHidden", info.isHidden() ? "1" : "0");
                        root.put("position", i);
                        database.insert(DatabaseHelper.TABLE_NAME_REMIND_INFO, "id", root);
                    }
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (null != database)
                        database.endTransaction();
                }
            }
        });
    }

    public int deleteRemindInfo(final String vehicleNum) {
        if (!StringUtils.isEmpty(vehicleNum)) {
            DatabaseManager.initializeInstance(helper);
            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    try {
                        nResult = database.delete(DatabaseHelper.TABLE_NAME_REMIND_INFO, " vehicleNum = ? ", new String[]{vehicleNum});
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                    }
                }
            });
        }
        return nResult;
    }

    public RemindInfo getRemindInfo(final String vehicleNum, final String name) {
        final RemindInfo info = new RemindInfo();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_REMIND_INFO,
                            null, " vehicleNum = ? and title = ? ",
                            new String[]{vehicleNum, name}, null, null, null);
                    if (cur.moveToNext()) {
                        info.setId(cur.getInt(cur.getColumnIndex("id")));
                        info.setVehicleNum(cur.getString(cur.getColumnIndex("vehicleNum")));
                        info.setTitle(cur.getString(cur.getColumnIndex("title")));
                        info.setDesc(cur.getString(cur.getColumnIndex("desc")));
                        info.setType(cur.getInt(cur.getColumnIndex("type")));
                        String hide = cur.getString(cur.getColumnIndex("isHidden"));
                        if (hide != null && hide.trim().equals("1"))
                            info.setHidden(true);
                        info.setPosition(cur.getInt(cur.getColumnIndex("position")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (!StringUtils.isEmpty(info.getVehicleNum()))
            return info;
        return null;
    }

    public List<RemindInfo> getRemindInfo(final String vehicleNum) {
        final List<RemindInfo> list = new ArrayList<RemindInfo>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_REMIND_INFO,
                            null, " vehicleNum = ? ",
                            new String[]{vehicleNum}, null, null, " position ASC ");
                    while (cur.moveToNext()) {
                        RemindInfo info = new RemindInfo();
                        info.setId(cur.getInt(cur.getColumnIndex("id")));
                        info.setVehicleNum(cur.getString(cur.getColumnIndex("vehicleNum")));
                        info.setTitle(cur.getString(cur.getColumnIndex("title")));
                        info.setDesc(cur.getString(cur.getColumnIndex("desc")));
                        info.setType(cur.getInt(cur.getColumnIndex("type")));
                        String hide = cur.getString(cur.getColumnIndex("isHidden"));
                        if (hide != null && hide.trim().equals("1"))
                            info.setHidden(true);
                        info.setPosition(cur.getInt(cur.getColumnIndex("position")));
                        list.add(info);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (list.size() > 0)
            return list;
        return null;
    }

    public List<RemindInfo> getRemindInfo() {
        final List<RemindInfo> list = new ArrayList<RemindInfo>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_REMIND_INFO,
                            null, null,
                            null, null, null, null);
                    while (cur.moveToNext()) {
                        RemindInfo info = new RemindInfo();
                        info.setId(cur.getInt(cur.getColumnIndex("id")));
                        info.setVehicleNum(cur.getString(cur.getColumnIndex("vehicleNum")));
                        info.setTitle(cur.getString(cur.getColumnIndex("title")));
                        info.setDesc(cur.getString(cur.getColumnIndex("desc")));
                        info.setType(cur.getInt(cur.getColumnIndex("type")));
                        String hide = cur.getString(cur.getColumnIndex("isHidden"));
                        if (hide != null && hide.trim().equals("1"))
                            info.setHidden(true);
                        info.setPosition(cur.getInt(cur.getColumnIndex("position")));
                        list.add(info);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (list.size() > 0)
            return list;
        return null;
    }

    public void saveRemindBaoyang(final List<RemindBaoyang> list) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    database.beginTransaction();
                    database.delete(DatabaseHelper.TABLE_NAME_REMIND_BAOYANG, null, null);
                    if (list != null) {
                        for (RemindBaoyang baoyang : list) {
                            if (baoyang == null)
                                continue;
                            ContentValues root = new ContentValues();
                            root.put("vehicleNum", baoyang.getVehicleNum());
                            root.put("date", baoyang.getDate());
                            root.put("remindDate1", baoyang.getRemindDate1());
                            root.put("remindDate2", baoyang.getRemindDate2());
                            root.put("repeatType", baoyang.getRepeatType());
                            root.put("orignalDate", baoyang.getOrignalDate());
                            root.put("licheng", baoyang.getLicheng());
                            root.put("jiange", baoyang.getJiange());
                            database.insert(DatabaseHelper.TABLE_NAME_REMIND_BAOYANG, "id", root);
                        }
                    }
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (database != null)
                        database.endTransaction();
                }
            }
        });
    }

    public void saveRemindBaoyang(final RemindBaoyang baoyang) {
        if (baoyang == null)
            return;
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    String vehicleNum = baoyang.getVehicleNum();
                    database.delete(DatabaseHelper.TABLE_NAME_REMIND_BAOYANG, " vehicleNum = ? ", new String[]{vehicleNum});
                    ContentValues root = new ContentValues();
                    root.put("vehicleNum", baoyang.getVehicleNum());
                    root.put("date", baoyang.getDate());
                    root.put("remindDate1", baoyang.getRemindDate1());
                    root.put("remindDate2", baoyang.getRemindDate2());
                    root.put("repeatType", baoyang.getRepeatType());
                    root.put("orignalDate", baoyang.getOrignalDate());
                    root.put("licheng", baoyang.getLicheng());
                    root.put("jiange", baoyang.getJiange());
                    database.insert(DatabaseHelper.TABLE_NAME_REMIND_BAOYANG, "id", root);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        });
    }

    public void updateRemindBaoyang(final RemindBaoyang baoyang) {
        if (baoyang == null)
            return;
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    String[] args = {baoyang.getVehicleNum()};
                    ContentValues root = new ContentValues();
                    root.put("vehicleNum", baoyang.getVehicleNum());
                    root.put("date", baoyang.getDate());
                    root.put("remindDate1", baoyang.getRemindDate1());
                    root.put("remindDate2", baoyang.getRemindDate2());
                    root.put("repeatType", baoyang.getRepeatType());
                    root.put("orignalDate", baoyang.getOrignalDate());
                    root.put("licheng", baoyang.getLicheng());
                    root.put("jiange", baoyang.getJiange());
                    database.update(DatabaseHelper.TABLE_NAME_REMIND_BAOYANG, root, " vehicleNum = ? ", args);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        });
    }

    public int deleteRemindBaoyang(final String vehicleNum) {
        if (!StringUtils.isEmpty(vehicleNum)) {
            DatabaseManager.initializeInstance(helper);
            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    try {
                        nResult = database.delete(DatabaseHelper.TABLE_NAME_REMIND_BAOYANG, " vehicleNum = ? ", new String[]{vehicleNum});
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                    }
                }
            });
        }
        return nResult;
    }

    public RemindBaoyang getRemindBaoyang(final String vehicleNum) {
        final RemindBaoyang baoyang = new RemindBaoyang();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_REMIND_BAOYANG,
                            new String[]{"id", "vehicleNum", "date", "remindDate1",
                                    "remindDate2", "repeatType", "orignalDate", "licheng", "jiange"}, " vehicleNum = ? ",
                            new String[]{vehicleNum}, null, null, null);
                    if (cur.moveToNext()) {
                        baoyang.setId(cur.getInt(cur.getColumnIndex("id")));
                        baoyang.setVehicleNum(cur.getString(cur.getColumnIndex("vehicleNum")));
                        baoyang.setDate(cur.getString(cur.getColumnIndex("date")));
                        baoyang.setRemindDate1(cur.getString(cur.getColumnIndex("remindDate1")));
                        baoyang.setRemindDate2(cur.getString(cur.getColumnIndex("remindDate2")));
                        baoyang.setRepeatType(cur.getInt(cur.getColumnIndex("repeatType")));
                        baoyang.setOrignalDate(cur.getString(cur.getColumnIndex("orignalDate")));
                        baoyang.setLicheng(cur.getInt(cur.getColumnIndex("licheng")));
                        baoyang.setJiange(cur.getInt(cur.getColumnIndex("jiange")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (!StringUtils.isEmpty(baoyang.getVehicleNum()))
            return baoyang;
        return null;
    }

    public List<RemindBaoyang> getRemindBaoyang() {
        final List<RemindBaoyang> list = new ArrayList<RemindBaoyang>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_REMIND_BAOYANG,
                            new String[]{"id", "vehicleNum", "date", "remindDate1",
                                    "remindDate2", "repeatType", "orignalDate", "licheng", "jiange"}, null,
                            null, null, null, null);
                    while (cur.moveToNext()) {
                        RemindBaoyang baoyang = new RemindBaoyang();
                        baoyang.setId(cur.getInt(cur.getColumnIndex("id")));
                        baoyang.setVehicleNum(cur.getString(cur.getColumnIndex("vehicleNum")));
                        baoyang.setDate(cur.getString(cur.getColumnIndex("date")));
                        baoyang.setRemindDate1(cur.getString(cur.getColumnIndex("remindDate1")));
                        baoyang.setRemindDate2(cur.getString(cur.getColumnIndex("remindDate2")));
                        baoyang.setRepeatType(cur.getInt(cur.getColumnIndex("repeatType")));
                        baoyang.setOrignalDate(cur.getString(cur.getColumnIndex("orignalDate")));
                        baoyang.setLicheng(cur.getInt(cur.getColumnIndex("licheng")));
                        baoyang.setJiange(cur.getInt(cur.getColumnIndex("jiange")));
                        list.add(baoyang);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (list.size() > 0)
            return list;
        return null;
    }

    public void saveRemindChedai(final List<RemindChedai> list) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    database.beginTransaction();
                    database.delete(DatabaseHelper.TABLE_NAME_REMIND_CHEDAI, null, null);
                    if (list != null) {
                        for (RemindChedai chedai : list) {
                            if (chedai == null)
                                continue;
                            ContentValues root = new ContentValues();
                            root.put("vehicleNum", chedai.getVehicleNum());
                            root.put("date", chedai.getDate());
                            root.put("remindDate1", chedai.getRemindDate1());
                            root.put("remindDate2", chedai.getRemindDate2());
                            root.put("repeatType", chedai.getRepeatType());
                            root.put("orignalDate", chedai.getOrignalDate());
                            root.put("money", chedai.getMoney());
                            root.put("total", chedai.getTotal());
                            root.put("fenshu", chedai.getFenshu());
                            root.put("remark", chedai.getRemark());
                            root.put("dayOfMonth", chedai.getDayOfMonth());
                            database.insert(DatabaseHelper.TABLE_NAME_REMIND_CHEDAI, "id", root);
                        }
                    }
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (database != null)
                        database.endTransaction();
                }
            }
        });
    }

    public void saveRemindChedai(final RemindChedai chedai) {
        if (chedai == null)
            return;
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    String vehicleNum = chedai.getVehicleNum();
                    database.delete(DatabaseHelper.TABLE_NAME_REMIND_CHEDAI, " vehicleNum = ? ", new String[]{vehicleNum});
                    ContentValues root = new ContentValues();
                    root.put("vehicleNum", chedai.getVehicleNum());
                    root.put("date", chedai.getDate());
                    root.put("remindDate1", chedai.getRemindDate1());
                    root.put("remindDate2", chedai.getRemindDate2());
                    root.put("repeatType", chedai.getRepeatType());
                    root.put("orignalDate", chedai.getOrignalDate());
                    root.put("money", chedai.getMoney());
                    root.put("total", chedai.getTotal());
                    root.put("fenshu", chedai.getFenshu());
                    root.put("remark", chedai.getRemark());
                    root.put("dayOfMonth", chedai.getDayOfMonth());
                    database.insert(DatabaseHelper.TABLE_NAME_REMIND_CHEDAI, "id", root);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        });
    }

    public void updateRemindChedai(final RemindChedai chedai) {
        if (chedai == null)
            return;
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    String[] args = {chedai.getVehicleNum()};
                    ContentValues root = new ContentValues();
                    root.put("vehicleNum", chedai.getVehicleNum());
                    root.put("date", chedai.getDate());
                    root.put("remindDate1", chedai.getRemindDate1());
                    root.put("remindDate2", chedai.getRemindDate2());
                    root.put("repeatType", chedai.getRepeatType());
                    root.put("orignalDate", chedai.getOrignalDate());
                    root.put("money", chedai.getMoney());
                    root.put("total", chedai.getTotal());
                    root.put("fenshu", chedai.getFenshu());
                    root.put("remark", chedai.getRemark());
                    root.put("dayOfMonth", chedai.getDayOfMonth());
                    database.update(DatabaseHelper.TABLE_NAME_REMIND_CHEDAI, root, " vehicleNum = ? ", args);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        });
    }

    public int deleteRemindChedai(final String vehicleNum) {
        if (!StringUtils.isEmpty(vehicleNum)) {
            DatabaseManager.initializeInstance(helper);
            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    try {
                        nResult = database.delete(DatabaseHelper.TABLE_NAME_REMIND_CHEDAI, " vehicleNum = ? ", new String[]{vehicleNum});
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                    }
                }
            });
        }
        return nResult;
    }

    public RemindChedai getRemindChedai(final String vehicleNum) {
        final RemindChedai chedai = new RemindChedai();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_REMIND_CHEDAI,
                            new String[]{"id", "vehicleNum", "date", "remindDate1",
                                    "remindDate2", "repeatType", "orignalDate", "money", "total", "fenshu", "remark", "dayOfMonth"}, " vehicleNum = ? ",
                            new String[]{vehicleNum}, null, null, null);
                    if (cur.moveToNext()) {
                        chedai.setId(cur.getInt(cur.getColumnIndex("id")));
                        chedai.setVehicleNum(cur.getString(cur.getColumnIndex("vehicleNum")));
                        chedai.setDate(cur.getString(cur.getColumnIndex("date")));
                        chedai.setRemindDate1(cur.getString(cur.getColumnIndex("remindDate1")));
                        chedai.setRemindDate2(cur.getString(cur.getColumnIndex("remindDate2")));
                        chedai.setRepeatType(cur.getInt(cur.getColumnIndex("repeatType")));
                        chedai.setOrignalDate(cur.getString(cur.getColumnIndex("orignalDate")));
                        chedai.setMoney(cur.getInt(cur.getColumnIndex("money")));
                        chedai.setTotal(cur.getInt(cur.getColumnIndex("total")));
                        chedai.setFenshu(cur.getInt(cur.getColumnIndex("fenshu")));
                        chedai.setRemark(cur.getString(cur.getColumnIndex("remark")));
                        chedai.setDayOfMonth(cur.getInt(cur.getColumnIndex("dayOfMonth")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (!StringUtils.isEmpty(chedai.getVehicleNum()))
            return chedai;
        return null;
    }

    public List<RemindChedai> getRemindChedai() {
        final List<RemindChedai> list = new ArrayList<RemindChedai>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_REMIND_CHEDAI,
                            new String[]{"id", "vehicleNum", "date", "remindDate1",
                                    "remindDate2", "repeatType", "orignalDate", "money", "total", "fenshu", "remark", "dayOfMonth"}, null,
                            null, null, null, null);
                    while (cur.moveToNext()) {
                        RemindChedai chedai = new RemindChedai();
                        chedai.setId(cur.getInt(cur.getColumnIndex("id")));
                        chedai.setVehicleNum(cur.getString(cur.getColumnIndex("vehicleNum")));
                        chedai.setDate(cur.getString(cur.getColumnIndex("date")));
                        chedai.setRemindDate1(cur.getString(cur.getColumnIndex("remindDate1")));
                        chedai.setRemindDate2(cur.getString(cur.getColumnIndex("remindDate2")));
                        chedai.setRepeatType(cur.getInt(cur.getColumnIndex("repeatType")));
                        chedai.setOrignalDate(cur.getString(cur.getColumnIndex("orignalDate")));
                        chedai.setMoney(cur.getInt(cur.getColumnIndex("money")));
                        chedai.setTotal(cur.getInt(cur.getColumnIndex("total")));
                        chedai.setFenshu(cur.getInt(cur.getColumnIndex("fenshu")));
                        chedai.setRemark(cur.getString(cur.getColumnIndex("remark")));
                        chedai.setDayOfMonth(cur.getInt(cur.getColumnIndex("dayOfMonth")));
                        list.add(chedai);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (list.size() > 0)
            return list;
        return null;
    }

    public void saveRemindCustom(final List<RemindCustom> list) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    database.beginTransaction();
                    database.delete(DatabaseHelper.TABLE_NAME_REMIND_CUSTOM, null, null);
                    if (list != null) {
                        for (RemindCustom custom : list) {
                            if (custom == null)
                                continue;
                            ContentValues root = new ContentValues();
                            root.put("vehicleNum", custom.getVehicleNum());
                            root.put("date", custom.getDate());
                            root.put("remindDate1", custom.getRemindDate1());
                            root.put("remindDate2", custom.getRemindDate2());
                            root.put("repeatType", custom.getRepeatType());
                            root.put("orignalDate", custom.getOrignalDate());
                            root.put("name", custom.getName());
                            root.put("location", custom.getLocation());
                            root.put("remark", custom.getRemark());
                            database.insert(DatabaseHelper.TABLE_NAME_REMIND_CUSTOM, "id", root);
                        }
                    }
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (database != null)
                        database.endTransaction();
                }
            }
        });
    }

    public void saveRemindCustom(final RemindCustom custom) {
        if (custom == null)
            return;
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    database.delete(DatabaseHelper.TABLE_NAME_REMIND_CUSTOM, " vehicleNum = ? and name = ? ", new String[]{custom.getVehicleNum(), custom.getName()});
                    ContentValues root = new ContentValues();
                    root.put("vehicleNum", custom.getVehicleNum());
                    root.put("date", custom.getDate());
                    root.put("remindDate1", custom.getRemindDate1());
                    root.put("remindDate2", custom.getRemindDate2());
                    root.put("repeatType", custom.getRepeatType());
                    root.put("orignalDate", custom.getOrignalDate());
                    root.put("name", custom.getName());
                    root.put("location", custom.getLocation());
                    root.put("remark", custom.getRemark());
                    database.insert(DatabaseHelper.TABLE_NAME_REMIND_CUSTOM, "id", root);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        });
    }

    public void updateRemindCustom(final RemindCustom custom) {
        if (custom == null)
            return;
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    String[] args = {custom.getVehicleNum(), String.valueOf(custom.getId())};
                    ContentValues root = new ContentValues();
                    root.put("vehicleNum", custom.getVehicleNum());
                    root.put("date", custom.getDate());
                    root.put("remindDate1", custom.getRemindDate1());
                    root.put("remindDate2", custom.getRemindDate2());
                    root.put("repeatType", custom.getRepeatType());
                    root.put("orignalDate", custom.getOrignalDate());
                    root.put("name", custom.getName());
                    root.put("location", custom.getLocation());
                    root.put("remark", custom.getRemark());
                    database.update(DatabaseHelper.TABLE_NAME_REMIND_CUSTOM, root, " vehicleNum = ? and id = ? ", args);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        });
    }

    public int deleteRemindCustom(final String vehicleNum, final String name) {
        if (!StringUtils.isEmpty(vehicleNum) && !StringUtils.isEmpty(name)) {
            DatabaseManager.initializeInstance(helper);
            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    try {
                        nResult = database.delete(DatabaseHelper.TABLE_NAME_REMIND_CUSTOM, " vehicleNum = ? and name = ? ", new String[]{vehicleNum, name});
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                    }
                }
            });
        }
        return nResult;
    }

    public int deleteRemindCustom(final String vehicleNum) {
        if (!StringUtils.isEmpty(vehicleNum)) {
            DatabaseManager.initializeInstance(helper);
            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    try {
                        nResult = database.delete(DatabaseHelper.TABLE_NAME_REMIND_CUSTOM, " vehicleNum = ? ", new String[]{vehicleNum});
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                    }
                }
            });
        }
        return nResult;
    }

    public List<RemindCustom> getRemindCustom(final String vehicleNum) {
        final List<RemindCustom> list = new ArrayList<RemindCustom>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_REMIND_CUSTOM,
                            new String[]{"id", "vehicleNum", "date", "remindDate1",
                                    "remindDate2", "repeatType", "orignalDate", "name", "location", "remark"}, " vehicleNum = ? ",
                            new String[]{vehicleNum}, null, null, null);
                    while (cur.moveToNext()) {
                        RemindCustom custom = new RemindCustom();
                        custom.setId(cur.getInt(cur.getColumnIndex("id")));
                        custom.setVehicleNum(cur.getString(cur.getColumnIndex("vehicleNum")));
                        custom.setDate(cur.getString(cur.getColumnIndex("date")));
                        custom.setRemindDate1(cur.getString(cur.getColumnIndex("remindDate1")));
                        custom.setRemindDate2(cur.getString(cur.getColumnIndex("remindDate2")));
                        custom.setRepeatType(cur.getInt(cur.getColumnIndex("repeatType")));
                        custom.setOrignalDate(cur.getString(cur.getColumnIndex("orignalDate")));
                        custom.setName(cur.getString(cur.getColumnIndex("name")));
                        custom.setLocation(cur.getString(cur.getColumnIndex("location")));
                        custom.setRemark(cur.getString(cur.getColumnIndex("remark")));
                        list.add(custom);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (list.size() > 0)
            return list;
        return null;
    }

    public RemindCustom getRemindCustom(final String vehicleNum, final String name) {
        final RemindCustom custom = new RemindCustom();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_REMIND_CUSTOM,
                            new String[]{"id", "vehicleNum", "date", "remindDate1",
                                    "remindDate2", "repeatType", "orignalDate", "name", "location", "remark"}, " vehicleNum = ? and name = ? ",
                            new String[]{vehicleNum, name}, null, null, null);
                    if (cur.moveToNext()) {
                        custom.setId(cur.getInt(cur.getColumnIndex("id")));
                        custom.setVehicleNum(cur.getString(cur.getColumnIndex("vehicleNum")));
                        custom.setDate(cur.getString(cur.getColumnIndex("date")));
                        custom.setRemindDate1(cur.getString(cur.getColumnIndex("remindDate1")));
                        custom.setRemindDate2(cur.getString(cur.getColumnIndex("remindDate2")));
                        custom.setRepeatType(cur.getInt(cur.getColumnIndex("repeatType")));
                        custom.setOrignalDate(cur.getString(cur.getColumnIndex("orignalDate")));
                        custom.setName(cur.getString(cur.getColumnIndex("name")));
                        custom.setLocation(cur.getString(cur.getColumnIndex("location")));
                        custom.setRemark(cur.getString(cur.getColumnIndex("remark")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (!StringUtils.isEmpty(custom.getVehicleNum()))
            return custom;
        return null;
    }

    public List<RemindCustom> getRemindCustom() {
        final List<RemindCustom> list = new ArrayList<RemindCustom>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_REMIND_CUSTOM,
                            new String[]{"id", "vehicleNum", "date", "remindDate1",
                                    "remindDate2", "repeatType", "orignalDate", "name", "location", "remark"}, null,
                            null, null, null, null);
                    while (cur.moveToNext()) {
                        RemindCustom custom = new RemindCustom();
                        custom.setId(cur.getInt(cur.getColumnIndex("id")));
                        custom.setVehicleNum(cur.getString(cur.getColumnIndex("vehicleNum")));
                        custom.setDate(cur.getString(cur.getColumnIndex("date")));
                        custom.setRemindDate1(cur.getString(cur.getColumnIndex("remindDate1")));
                        custom.setRemindDate2(cur.getString(cur.getColumnIndex("remindDate2")));
                        custom.setRepeatType(cur.getInt(cur.getColumnIndex("repeatType")));
                        custom.setOrignalDate(cur.getString(cur.getColumnIndex("orignalDate")));
                        custom.setName(cur.getString(cur.getColumnIndex("name")));
                        custom.setLocation(cur.getString(cur.getColumnIndex("location")));
                        custom.setRemark(cur.getString(cur.getColumnIndex("remark")));
                        list.add(custom);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (list.size() > 0)
            return list;
        return null;
    }

    public void addBaoyangRecord(final BaoyangRecord baoyang) {
        if (baoyang == null)
            return;
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    ContentValues root = new ContentValues();
                    root.put("vehicleNum", baoyang.getVehicleNum());
                    root.put("date", baoyang.getDate());
                    root.put("licheng", baoyang.getLicheng());
                    root.put("money", baoyang.getMoney());
                    root.put("company", baoyang.getCompany());
                    root.put("phone", baoyang.getPhone());
                    root.put("remark", baoyang.getRemark());
                    root.put("baoyangItemId", baoyang.getBaoyangItemId());
                    root.put("baoyangItem", baoyang.getBaoyangItem());
                    root.put("lat", baoyang.getLat());
                    root.put("lon", baoyang.getLon());
                    root.put("address", baoyang.getAddress());
                    database.insert(DatabaseHelper.TABLE_NAME_BAOYANG_RECORD, "id", root);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        });
    }

    public void saveBaoyangRecord(final List<BaoyangRecord> list) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    database.beginTransaction();
                    database.delete(DatabaseHelper.TABLE_NAME_BAOYANG_RECORD, null, null);
                    if (list != null) {
                        for (BaoyangRecord baoyang : list) {
                            if (baoyang == null)
                                continue;
                            ContentValues root = new ContentValues();
                            root.put("vehicleNum", baoyang.getVehicleNum());
                            root.put("date", baoyang.getDate());
                            root.put("licheng", baoyang.getLicheng());
                            root.put("money", baoyang.getMoney());
                            root.put("company", baoyang.getCompany());
                            root.put("phone", baoyang.getPhone());
                            root.put("remark", baoyang.getRemark());
                            root.put("baoyangItemId", baoyang.getBaoyangItemId());
                            root.put("baoyangItem", baoyang.getBaoyangItem());
                            root.put("lat", baoyang.getLat());
                            root.put("lon", baoyang.getLon());
                            root.put("address", baoyang.getAddress());
                            database.insert(DatabaseHelper.TABLE_NAME_BAOYANG_RECORD, "id", root);
                        }
                    }
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (database != null)
                        database.endTransaction();
                }
            }
        });
    }

    public int deleteBaoyangRecord(final String vehicleNum) {
        if (!StringUtils.isEmpty(vehicleNum)) {
            DatabaseManager.initializeInstance(helper);
            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    try {
                        nResult = database.delete(DatabaseHelper.TABLE_NAME_BAOYANG_RECORD, " vehicleNum = ? ", new String[]{vehicleNum});
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                    }
                }
            });
        }
        return nResult;
    }

    public List<BaoyangRecord> getBaoyangRecord() {
        final List<BaoyangRecord> list = new ArrayList<BaoyangRecord>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_BAOYANG_RECORD,
                            new String[]{"vehicleNum", "date", "licheng", "money", "company",
                                    "phone", "remark", "baoyangItemId", "baoyangItem", "lat", "lon", "address"}, null,
                            null, null, null, null);
                    while (cur.moveToNext()) {
                        BaoyangRecord baoyang = new BaoyangRecord();
                        baoyang.setVehicleNum(cur.getString(cur.getColumnIndex("vehicleNum")));
                        baoyang.setDate(cur.getString(cur.getColumnIndex("date")));
                        baoyang.setLicheng(cur.getInt(cur.getColumnIndex("licheng")));
                        baoyang.setMoney(cur.getInt(cur.getColumnIndex("money")));
                        baoyang.setCompany(cur.getString(cur.getColumnIndex("company")));
                        baoyang.setPhone(cur.getString(cur.getColumnIndex("phone")));
                        baoyang.setRemark(cur.getString(cur.getColumnIndex("remark")));
                        baoyang.setBaoyangItemId(cur.getString(cur.getColumnIndex("baoyangItemId")));
                        baoyang.setBaoyangItem(cur.getString(cur.getColumnIndex("baoyangItem")));
                        baoyang.setLat(cur.getString(cur.getColumnIndex("lat")));
                        baoyang.setLon(cur.getString(cur.getColumnIndex("lon")));
                        baoyang.setAddress(cur.getString(cur.getColumnIndex("address")));
                        list.add(baoyang);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (list.size() > 0)
            return list;
        return null;
    }

    public List<BaoyangRecord> getBaoyangRecord(final String vehicleNum) {
        final List<BaoyangRecord> list = new ArrayList<BaoyangRecord>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_BAOYANG_RECORD,
                            new String[]{"vehicleNum", "date", "licheng", "money", "company",
                                    "phone", "remark", "baoyangItemId", "baoyangItem", "lat", "lon", "address"}, " vehicleNum = ? ",
                            new String[]{vehicleNum}, null, null, null);
                    while (cur.moveToNext()) {
                        BaoyangRecord baoyang = new BaoyangRecord();
                        baoyang.setVehicleNum(cur.getString(cur.getColumnIndex("vehicleNum")));
                        baoyang.setDate(cur.getString(cur.getColumnIndex("date")));
                        baoyang.setLicheng(cur.getInt(cur.getColumnIndex("licheng")));
                        baoyang.setMoney(cur.getInt(cur.getColumnIndex("money")));
                        baoyang.setCompany(cur.getString(cur.getColumnIndex("company")));
                        baoyang.setPhone(cur.getString(cur.getColumnIndex("phone")));
                        baoyang.setRemark(cur.getString(cur.getColumnIndex("remark")));
                        baoyang.setBaoyangItemId(cur.getString(cur.getColumnIndex("baoyangItemId")));
                        baoyang.setBaoyangItem(cur.getString(cur.getColumnIndex("baoyangItem")));
                        baoyang.setLat(cur.getString(cur.getColumnIndex("lat")));
                        baoyang.setLon(cur.getString(cur.getColumnIndex("lon")));
                        baoyang.setAddress(cur.getString(cur.getColumnIndex("address")));
                        list.add(baoyang);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (list.size() > 0)
            return list;
        return null;
    }

    public void addBaoyangItem(final BaoyangItem baoyangItem) {
        if (null == baoyangItem)
            return;

        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    long uid = KplusApplication.getInstance().getId();
                    ContentValues root = new ContentValues();
                    root.put("id", baoyangItem.getId());
                    root.put("item", baoyangItem.getItem());
                    root.put("type", baoyangItem.getType());
                    root.put("createTime", baoyangItem.getCreateTime());
                    root.put("updateTime", baoyangItem.getUpdateTime());
                    root.put("isDelete", baoyangItem.getIsDelete());
                    root.put("uid", uid);
                    database.insert(DatabaseHelper.TABLE_NAME_BAOYANG_ITEM, "id", root);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        });
    }

    public void saveBaoyangItem(final List<BaoyangItem> list) {
        if (null == list || list.size() == 0)
            return;

        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    long uid = KplusApplication.getInstance().getId();
                    database.beginTransaction();

                    database.delete(DatabaseHelper.TABLE_NAME_BAOYANG_ITEM, " uid = ? ", new String[]{String.valueOf(uid)});

                    for (BaoyangItem baoyangItem : list) {
                        if (baoyangItem == null)
                            continue;

                        ContentValues root = new ContentValues();
                        root.put("id", baoyangItem.getId());
                        root.put("item", baoyangItem.getItem());
                        root.put("type", baoyangItem.getType());
                        root.put("createTime", baoyangItem.getCreateTime());
                        root.put("updateTime", baoyangItem.getUpdateTime());
                        root.put("isDelete", baoyangItem.getIsDelete());
                        root.put("uid", uid);
                        database.insert(DatabaseHelper.TABLE_NAME_BAOYANG_ITEM, "id", root);
                    }
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (database != null)
                        database.endTransaction();
                }
            }
        });
    }

    public int deleteBaoyangItem(final int id) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    long uid = KplusApplication.getInstance().getId();
                    nResult = database.delete(DatabaseHelper.TABLE_NAME_BAOYANG_ITEM,
                            " id = ? and uid = ? ", new String[]{String.valueOf(id), String.valueOf(uid)});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return nResult;
    }

    public int deleteBaoyangItem() {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    long uid = KplusApplication.getInstance().getId();
                    nResult = database.delete(DatabaseHelper.TABLE_NAME_BAOYANG_ITEM,
                            " uid = ? ", new String[]{String.valueOf(uid)});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return nResult;
    }

    public List<BaoyangItem> getBaoyangItem() {
        final List<BaoyangItem> list = new ArrayList<>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    long uid = KplusApplication.getInstance().getId();

                    cur = database.query(DatabaseHelper.TABLE_NAME_BAOYANG_ITEM,
                            new String[]{"id", "item", "type", "createTime", "updateTime", "isDelete"}, " uid = ? ",
                            new String[]{String.valueOf(uid)}, null, null, null);
                    while (cur.moveToNext()) {
                        BaoyangItem baoyangItem = new BaoyangItem();
                        baoyangItem.setId(cur.getInt(cur.getColumnIndex("id")));
                        baoyangItem.setItem(cur.getString(cur.getColumnIndex("item")));
                        baoyangItem.setType(cur.getInt(cur.getColumnIndex("type")));
                        baoyangItem.setCreateTime(cur.getString(cur.getColumnIndex("createTime")));
                        baoyangItem.setUpdateTime(cur.getString(cur.getColumnIndex("updateTime")));
                        baoyangItem.setIsDelete(cur.getInt(cur.getColumnIndex("isDelete")));
                        list.add(baoyangItem);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                    }
                }
            }
        });
        if (list.size() > 0) {
            return list;
        }
        return null;
    }

    public Jiazhao getJiazhao(final String id) {
        final Jiazhao jiazhao = new Jiazhao();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_JIAZHAO,
                            null, " id = ? ",
                            new String[]{id}, null, null, null);
                    if (cur.moveToNext()) {
                        jiazhao.setId(cur.getString(cur.getColumnIndex("id")));
                        jiazhao.setJszh(cur.getString(cur.getColumnIndex("jszh")));
                        jiazhao.setXm(cur.getString(cur.getColumnIndex("xm")));
                        jiazhao.setStartTime(cur.getString(cur.getColumnIndex("startTime")));
                        jiazhao.setIsHidden(cur.getString(cur.getColumnIndex("isHidden")));
                        jiazhao.setSpace(cur.getInt(cur.getColumnIndex("space")));
                        jiazhao.setDabh(cur.getString(cur.getColumnIndex("dabh")));
                        jiazhao.setDate(cur.getString(cur.getColumnIndex("date")));
                        jiazhao.setRemindDate(cur.getString(cur.getColumnIndex("remindDate")));
                        jiazhao.setLjjf(cur.getInt(cur.getColumnIndex("score")));
                        jiazhao.setZjcx(cur.getString(cur.getColumnIndex("zjcx")));
                        jiazhao.setDbid(cur.getInt(cur.getColumnIndex("dbid")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (!StringUtils.isEmpty(jiazhao.getId()))
            return jiazhao;
        return null;
    }

    public List<Jiazhao> getJiazhaos() {
        final List<Jiazhao> list = new ArrayList<Jiazhao>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_JIAZHAO,
                            null, null,
                            null, null, null, null);
                    while (cur.moveToNext()) {
                        Jiazhao jiazhao = new Jiazhao();
                        jiazhao.setId(cur.getString(cur.getColumnIndex("id")));
                        jiazhao.setJszh(cur.getString(cur.getColumnIndex("jszh")));
                        jiazhao.setXm(cur.getString(cur.getColumnIndex("xm")));
                        jiazhao.setStartTime(cur.getString(cur.getColumnIndex("startTime")));
                        jiazhao.setIsHidden(cur.getString(cur.getColumnIndex("isHidden")));
                        jiazhao.setSpace(cur.getInt(cur.getColumnIndex("space")));
                        jiazhao.setDabh(cur.getString(cur.getColumnIndex("dabh")));
                        jiazhao.setDate(cur.getString(cur.getColumnIndex("date")));
                        jiazhao.setRemindDate(cur.getString(cur.getColumnIndex("remindDate")));
                        jiazhao.setLjjf(cur.getInt(cur.getColumnIndex("score")));
                        jiazhao.setZjcx(cur.getString(cur.getColumnIndex("zjcx")));
                        jiazhao.setDbid(cur.getInt(cur.getColumnIndex("dbid")));
                        list.add(jiazhao);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (list.size() > 0)
            return list;
        return null;
    }

    public void saveJiazhaos(final List<Jiazhao> list) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    database.beginTransaction();
                    database.delete(DatabaseHelper.TABLE_NAME_JIAZHAO, null, null);
                    for (Jiazhao jiazhao : list) {
                        ContentValues root = new ContentValues();
                        root.put("id", jiazhao.getId());
                        root.put("jszh", jiazhao.getJszh());
                        root.put("xm", jiazhao.getXm());
                        root.put("startTime", jiazhao.getStartTime());
                        root.put("isHidden", jiazhao.getIsHidden());
                        root.put("space", jiazhao.getSpace());
                        root.put("dabh", jiazhao.getDabh());
                        root.put("date", jiazhao.getDate());
                        root.put("remindDate", jiazhao.getRemindDate());
                        root.put("score", jiazhao.getLjjf());
                        root.put("zjcx", jiazhao.getZjcx());
                        database.insert(DatabaseHelper.TABLE_NAME_JIAZHAO, "id", root);
                    }
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (database != null)
                        database.endTransaction();
                }
            }
        });
    }

    public void saveJiazhao(final Jiazhao jiazhao) {
        if (jiazhao == null)
            return;
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    database.delete(DatabaseHelper.TABLE_NAME_JIAZHAO, " id = ? ", new String[]{jiazhao.getId()});
                    ContentValues root = new ContentValues();
                    root.put("id", jiazhao.getId());
                    root.put("jszh", jiazhao.getJszh());
                    root.put("xm", jiazhao.getXm());
                    root.put("startTime", jiazhao.getStartTime());
                    root.put("isHidden", jiazhao.getIsHidden());
                    root.put("space", jiazhao.getSpace());
                    root.put("dabh", jiazhao.getDabh());
                    root.put("date", jiazhao.getDate());
                    root.put("remindDate", jiazhao.getRemindDate());
                    root.put("score", jiazhao.getLjjf());
                    root.put("zjcx", jiazhao.getZjcx());
                    database.insert(DatabaseHelper.TABLE_NAME_JIAZHAO, "id", root);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        });
    }

    public void updateJiazhao(final Jiazhao jiazhao) {
        if (jiazhao == null)
            return;
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    String[] args = {String.valueOf(jiazhao.getId())};
                    ContentValues root = new ContentValues();
                    root.put("id", jiazhao.getId());
                    root.put("jszh", jiazhao.getJszh());
                    root.put("xm", jiazhao.getXm());
                    root.put("startTime", jiazhao.getStartTime());
                    root.put("isHidden", jiazhao.getIsHidden());
                    root.put("space", jiazhao.getSpace());
                    root.put("dabh", jiazhao.getDabh());
                    root.put("date", jiazhao.getDate());
                    root.put("remindDate", jiazhao.getRemindDate());
                    root.put("score", jiazhao.getLjjf());
                    root.put("zjcx", jiazhao.getZjcx());
                    database.update(DatabaseHelper.TABLE_NAME_JIAZHAO, root, " id = ? ", args);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        });
    }

    public List<ProviderInfo> getProviderInfos() {
        final List<ProviderInfo> list = new ArrayList<ProviderInfo>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABEL_NAME_DAZE_PROVIDER_INFO, null, null, null, null, null, null);
                    while (cur.moveToNext()) {
                        ProviderInfo providerInfo = new ProviderInfo();
                        providerInfo.setOpenUserId(cur.getString(cur.getColumnIndex("openUserId")));
                        providerInfo.setName(cur.getString(cur.getColumnIndex("name")));
                        providerInfo.setImage(cur.getString(cur.getColumnIndex("image")));
                        list.add(providerInfo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (list.isEmpty())
            return null;
        return list;
    }

    public void saveProviderInfo(final ProviderInfo providerInfo) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                database.delete(DatabaseHelper.TABEL_NAME_DAZE_PROVIDER_INFO, " openUserId = ? ", new String[]{providerInfo.getOpenUserId()});
                ContentValues contentValues = new ContentValues();
                contentValues.put("openUserId", providerInfo.getOpenUserId());
                contentValues.put("name", providerInfo.getName());
                contentValues.put("image", providerInfo.getImage());
                database.insert(DatabaseHelper.TABEL_NAME_DAZE_PROVIDER_INFO, null, contentValues);
            }
        });
    }

    public int deleteJiazhao(final String id) {
        if (!StringUtils.isEmpty(id)) {
            DatabaseManager.initializeInstance(helper);
            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    try {
                        nResult = database.delete(DatabaseHelper.TABLE_NAME_JIAZHAO, " id = ? ", new String[]{id});
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                    }
                }
            });
        }
        return nResult;
    }

    public int clearJiazhaos() {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    nResult = database.delete(DatabaseHelper.TABLE_NAME_JIAZHAO, null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        });
        return nResult;
    }

    public void saveJiazhaoAgainst(final List<JiazhaoAgainst> list) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    database.beginTransaction();
                    database.delete(DatabaseHelper.TABLE_NAME_JIAZHAO_AGAINST, null, null);
                    for (JiazhaoAgainst against : list) {
                        ContentValues root = new ContentValues();
                        root.put("vehicleNum", against.getVehicleNum());
                        root.put("total", against.getTotal());
                        database.insert(DatabaseHelper.TABLE_NAME_JIAZHAO_AGAINST, "id", root);
                    }
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (database != null)
                        database.endTransaction();
                }
            }
        });
    }

    public int deleteJiazhaoAgainst(final String vehicleNum) {
        if (!StringUtils.isEmpty(vehicleNum)) {
            DatabaseManager.initializeInstance(helper);
            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    try {
                        nResult = database.delete(DatabaseHelper.TABLE_NAME_JIAZHAO_AGAINST, " vehicleNum = ? ", new String[]{vehicleNum});
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                    }
                }
            });
        }
        return nResult;
    }

    public List<JiazhaoAgainst> getJiazhaoAgainst() {
        final List<JiazhaoAgainst> list = new ArrayList<JiazhaoAgainst>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_JIAZHAO_AGAINST,
                            null, null,
                            null, null, null, null);
                    while (cur.moveToNext()) {
                        JiazhaoAgainst against = new JiazhaoAgainst();
                        against.setVehicleNum(cur.getString(cur.getColumnIndex("vehicleNum")));
                        against.setTotal(cur.getInt(cur.getColumnIndex("total")));
                        list.add(against);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (list.size() > 0)
            return list;
        return null;
    }

    public List<RemindRestrict> getRemindRestricts() {
        final List<RemindRestrict> list = new ArrayList<RemindRestrict>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_REMIND_RESTRICT,
                            null, null,
                            null, null, null, null);
                    while (cur.moveToNext()) {
                        RemindRestrict restrict = new RemindRestrict();
                        restrict.setId(cur.getString(cur.getColumnIndex("id")));
                        restrict.setIsHidden(cur.getString(cur.getColumnIndex("isHidden")));
                        restrict.setVehicleNum(cur.getString(cur.getColumnIndex("vehicleNum")));
                        restrict.setMessageRemind(cur.getString(cur.getColumnIndex("messageRemind")));
                        restrict.setPhone(cur.getString(cur.getColumnIndex("phone")));
                        restrict.setRemindDateType(cur.getString(cur.getColumnIndex("remindDateType")));
                        restrict.setRemindDate(cur.getString(cur.getColumnIndex("remindDate")));
                        restrict.setDbid(cur.getInt(cur.getColumnIndex("dbid")));
                        restrict.setVehicleCityId(cur.getString(cur.getColumnIndex("vehicleCityId")));
                        list.add(restrict);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (list.size() > 0)
            return list;
        return null;
    }

    public RemindRestrict getRemindRestrict(final String vehicleNum) {
        final RemindRestrict restrict = new RemindRestrict();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_REMIND_RESTRICT,
                            null, " vehicleNum = ? ",
                            new String[]{vehicleNum}, null, null, null);
                    if (cur.moveToNext()) {
                        restrict.setId(cur.getString(cur.getColumnIndex("id")));
                        restrict.setIsHidden(cur.getString(cur.getColumnIndex("isHidden")));
                        restrict.setVehicleNum(cur.getString(cur.getColumnIndex("vehicleNum")));
                        restrict.setMessageRemind(cur.getString(cur.getColumnIndex("messageRemind")));
                        restrict.setPhone(cur.getString(cur.getColumnIndex("phone")));
                        restrict.setRemindDateType(cur.getString(cur.getColumnIndex("remindDateType")));
                        restrict.setRemindDate(cur.getString(cur.getColumnIndex("remindDate")));
                        restrict.setDbid(cur.getInt(cur.getColumnIndex("dbid")));
                        restrict.setVehicleCityId(cur.getString(cur.getColumnIndex("vehicleCityId")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (!StringUtils.isEmpty(restrict.getId()))
            return restrict;
        return null;
    }

    public void saveRemindRestricts(final List<RemindRestrict> list) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    database.beginTransaction();
                    database.delete(DatabaseHelper.TABLE_NAME_REMIND_RESTRICT, null, null);
                    for (RemindRestrict restrict : list) {
                        ContentValues root = new ContentValues();
                        root.put("id", restrict.getId());
                        root.put("isHidden", restrict.getIsHidden());
                        root.put("vehicleNum", restrict.getVehicleNum());
                        root.put("messageRemind", restrict.getMessageRemind());
                        root.put("phone", restrict.getPhone());
                        root.put("remindDateType", restrict.getRemindDateType());
                        root.put("remindDate", restrict.getRemindDate());
                        root.put("vehicleCityId", restrict.getVehicleCityId());
                        database.insert(DatabaseHelper.TABLE_NAME_REMIND_RESTRICT, "id", root);
                    }
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (database != null)
                        database.endTransaction();
                }
            }
        });
    }

    public void saveRemindRestrict(final RemindRestrict restrict) {
        if (restrict == null)
            return;
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    database.delete(DatabaseHelper.TABLE_NAME_REMIND_RESTRICT, " id = ? ", new String[]{restrict.getId()});
                    ContentValues root = new ContentValues();
                    root.put("id", restrict.getId());
                    root.put("isHidden", restrict.getIsHidden());
                    root.put("vehicleNum", restrict.getVehicleNum());
                    root.put("messageRemind", restrict.getMessageRemind());
                    root.put("phone", restrict.getPhone());
                    root.put("remindDateType", restrict.getRemindDateType());
                    root.put("remindDate", restrict.getRemindDate());
                    root.put("vehicleCityId", restrict.getVehicleCityId());
                    database.insert(DatabaseHelper.TABLE_NAME_REMIND_RESTRICT, "id", root);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        });
    }

    public void updateRemindRestrict(final RemindRestrict restrict) {
        if (restrict == null)
            return;
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    String[] args = {String.valueOf(restrict.getId())};
                    ContentValues root = new ContentValues();
                    root.put("id", restrict.getId());
                    root.put("isHidden", restrict.getIsHidden());
                    root.put("vehicleNum", restrict.getVehicleNum());
                    root.put("messageRemind", restrict.getMessageRemind());
                    root.put("phone", restrict.getPhone());
                    root.put("remindDateType", restrict.getRemindDateType());
                    root.put("remindDate", restrict.getRemindDate());
                    root.put("vehicleCityId", restrict.getVehicleCityId());
                    database.update(DatabaseHelper.TABLE_NAME_REMIND_RESTRICT, root, " id = ? ", args);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        });
    }

    public int deleteRemindRestrict(final String vehicleNum) {
        if (!StringUtils.isEmpty(vehicleNum)) {
            DatabaseManager.initializeInstance(helper);
            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    try {
                        nResult = database.delete(DatabaseHelper.TABLE_NAME_REMIND_RESTRICT, " vehicleNum = ? ", new String[]{vehicleNum});
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                    }
                }
            });
        }
        return nResult;
    }

    public List<Weather> getWeathers() {
        final List<Weather> list = new ArrayList<Weather>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_WEATHER,
                            null, null,
                            null, null, null, null);
                    while (cur.moveToNext()) {
                        Weather weather = new Weather();
                        weather.setPhenomenon(cur.getString(cur.getColumnIndex("phenomenon")));
                        weather.setTemperatureDay(cur.getString(cur.getColumnIndex("temperatureDay")));
                        weather.setWash(cur.getInt(cur.getColumnIndex("wash")));
                        weather.setTemperatureNight(cur.getString(cur.getColumnIndex("temperatureNight")));
                        weather.setType(cur.getInt(cur.getColumnIndex("type")));
                        weather.setWashDescr(cur.getString(cur.getColumnIndex("washDescr")));
                        weather.setDateStr(cur.getString(cur.getColumnIndex("dateStr")));
                        list.add(weather);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (list.size() > 0)
            return list;
        return null;
    }

    public Weather getWeather(final int type) {
        final Weather weather = new Weather();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_WEATHER,
                            null, " type = ? ",
                            new String[]{String.valueOf(type)}, null, null, null);
                    if (cur.moveToNext()) {
                        weather.setPhenomenon(cur.getString(cur.getColumnIndex("phenomenon")));
                        weather.setTemperatureDay(cur.getString(cur.getColumnIndex("temperatureDay")));
                        weather.setWash(cur.getInt(cur.getColumnIndex("wash")));
                        weather.setTemperatureNight(cur.getString(cur.getColumnIndex("temperatureNight")));
                        weather.setType(cur.getInt(cur.getColumnIndex("type")));
                        weather.setWashDescr(cur.getString(cur.getColumnIndex("washDescr")));
                        weather.setDateStr(cur.getString(cur.getColumnIndex("dateStr")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (!StringUtils.isEmpty(weather.getPhenomenon()))
            return weather;
        return null;
    }

    public void saveWeathers(final List<Weather> list) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    database.beginTransaction();
                    database.delete(DatabaseHelper.TABLE_NAME_WEATHER, null, null);
                    for (Weather weather : list) {
                        ContentValues root = new ContentValues();
                        root.put("phenomenon", weather.getPhenomenon());
                        root.put("temperatureDay", weather.getTemperatureDay());
                        root.put("wash", weather.getWash());
                        root.put("temperatureNight", weather.getTemperatureNight());
                        root.put("type", weather.getType());
                        root.put("washDescr", weather.getWashDescr());
                        root.put("dateStr", weather.getDateStr());
                        database.insert(DatabaseHelper.TABLE_NAME_WEATHER, "id", root);
                    }
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (database != null)
                        database.endTransaction();
                }
            }
        });
    }

    public int clearWeathers() {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    nResult = database.delete(DatabaseHelper.TABLE_NAME_WEATHER, null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        });
        return nResult;
    }

    public void saveOrderId(final long orderId) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    ContentValues root = new ContentValues();
                    root.put("orderId", orderId);
                    database.insert(DatabaseHelper.TABEL_NAME_DAZE_ORDER_PAYING, "orderId", root);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public String containOrderId(final long orderId) {
        final StringBuilder stringBuilder = new StringBuilder();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABEL_NAME_DAZE_ORDER_PAYING, new String[]{"orderId"}, " orderId = ? ", new String[]{"" + orderId}, null, null, null);
                    if (cur.getCount() > 0) {
                        stringBuilder.append("true");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        return stringBuilder.toString();
    }

    public void deleteOrderId(final long orderId) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    database.delete(DatabaseHelper.TABEL_NAME_DAZE_ORDER_PAYING, " orderId = ? ", new String[]{"" + orderId});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public List<CarServiceGroup> getCarServices(final long cityId) {
        final List<CarServiceGroup> list = new ArrayList<>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                Cursor cursor = null;
                try {
                    String where = " cityId = ? AND groupId = ?";
                    String orderBy = " sort ASC";

                    // 查询分组服务
                    cur = database.query(DatabaseHelper.TABLE_NAME_SERVICE_GROUPS,
                            null, " cityId = ? ", new String[]{String.valueOf(cityId)}, null, null, orderBy);
                    while (cur.moveToNext()) {
                        CarServiceGroup serviceGroup = new CarServiceGroup();
                        serviceGroup.setName(cur.getString(cur.getColumnIndex("name")));
                        int flag = cur.getInt(cur.getColumnIndex("system"));
                        serviceGroup.setSystem((flag != 0));
                        flag = cur.getInt(cur.getColumnIndex("showName"));
                        serviceGroup.setShowName((flag != 0));
                        serviceGroup.setIndexServiceCount(cur.getInt(cur.getColumnIndex("indexServiceCount")));
                        serviceGroup.setServiceCount(cur.getInt(cur.getColumnIndex("serviceCount")));
                        serviceGroup.setSort(cur.getInt(cur.getColumnIndex("sort")));

                        long id = cur.getLong(cur.getColumnIndex("id"));
                        serviceGroup.setId(id);

                        // 根据主键id查询服务
                        cursor = database.query(DatabaseHelper.TABLE_NAME_SERVICES,
                                null, where, new String[]{String.valueOf(cityId), String.valueOf(id)}, null, null, orderBy);

                        List<CarService> carServices = null;
                        if (null != cursor) {
                            carServices = new ArrayList<>();
                            while (cursor.moveToNext()) {
                                CarService carService = new CarService();
                                carService.setId(cursor.getLong(cursor.getColumnIndex("id")));
                                carService.setName(cursor.getString(cursor.getColumnIndex("name")));
                                carService.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                                carService.setInfo(cursor.getString(cursor.getColumnIndex("info")));
                                carService.setFlag(cursor.getInt(cursor.getColumnIndex("flag")));
                                carService.setFavorableTag(cursor.getString(cursor.getColumnIndex("favorableTag")));
                                carService.setListIcon(cursor.getString(cursor.getColumnIndex("listIcon")));
                                carService.setLinkIcon(cursor.getString(cursor.getColumnIndex("linkIcon")));
                                carService.setTabIcon(cursor.getString(cursor.getColumnIndex("tabIcon")));
                                carService.setMotionType(cursor.getString(cursor.getColumnIndex("motionType")));
                                carService.setMotionValue(cursor.getString(cursor.getColumnIndex("motionValue")));
                                carService.setSort(cursor.getInt(cursor.getColumnIndex("sort")));
                                carService.setTransitionUrl(cursor.getString(cursor.getColumnIndex("transitionUrl")));
                                carService.setMotionValueRelation(cursor.getString(cursor.getColumnIndex("motionValueRelation")));

                                carServices.add(carService);
                            }
                            cursor.close();
                        }
                        serviceGroup.setServices(carServices);

                        list.add(serviceGroup);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (null != cursor && !cursor.isClosed()) {
                        cursor.close();
                    }
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                    }
                }
            }
        });
        if (list.size() > 0)
            return list;
        return null;
    }

    public void saveCarService(final List<CarServiceGroup> list, final long cityId) {
        if (null == list || list.isEmpty()) {
            return;
        }
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    database.beginTransaction();
                    String where = " cityId = ? ";
                    String[] args = new String[]{String.valueOf(cityId)};
                    // 先删除再插入
                    database.delete(DatabaseHelper.TABLE_NAME_SERVICES, where, args);
                    database.delete(DatabaseHelper.TABLE_NAME_SERVICE_GROUPS, where, args);

                    // 插入分组服务
                    for (CarServiceGroup group : list) {
                        ContentValues values = new ContentValues();
                        values.put("id", group.getId());
                        values.put("name", group.getName());
                        values.put("system", group.getSystem());
                        values.put("showName", group.getShowName());
                        values.put("indexServiceCount", group.getIndexServiceCount());
                        values.put("serviceCount", group.getServiceCount());
                        values.put("sort", group.getSort());
                        values.put("cityId", cityId);

                        database.insert(DatabaseHelper.TABLE_NAME_SERVICE_GROUPS, "id", values);

                        List<CarService> services = group.getServices();
                        if (null != services) {
                            // 插入服务
                            for (CarService service : services) {
                                values = new ContentValues();
                                values.put("id", service.getId());
                                values.put("name", service.getName());
                                values.put("title", service.getTitle());
                                values.put("info", service.getInfo());
                                values.put("flag", service.getFlag());
                                values.put("favorableTag", service.getFavorableTag());
                                values.put("listIcon", service.getListIcon());
                                values.put("linkIcon", service.getLinkIcon());
                                values.put("tabIcon", service.getTabIcon());
                                values.put("motionType", service.getMotionType());
                                values.put("motionValue", service.getMotionValue());
                                values.put("transitionUrl", service.getTransitionUrl());
                                values.put("motionValueRelation", service.getMotionValueRelation());
                                values.put("sort", service.getSort());
                                values.put("groupId", group.getId());
                                values.put("cityId", cityId);
                                database.insert(DatabaseHelper.TABLE_NAME_SERVICES, "id", values);
                            }
                        }
                    }
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (database != null)
                        database.endTransaction();
                }
            }
        });
    }

    public void clearCarServices(final long cityId) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    String where = " cityId = ? ";
                    String[] args = new String[]{String.valueOf(cityId)};
                    database.delete(DatabaseHelper.TABLE_NAME_SERVICES, where, args);
                    database.delete(DatabaseHelper.TABLE_NAME_SERVICE_GROUPS, where, args);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void clearCarServices() {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    database.delete(DatabaseHelper.TABLE_NAME_SERVICES, null, null);
                    database.delete(DatabaseHelper.TABLE_NAME_SERVICE_GROUPS, null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public List<FWRequestInfo> getRequestInfo() {
        final List<FWRequestInfo> list = new ArrayList<>();
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                Cursor cur = null;
                try {
                    cur = database.query(DatabaseHelper.TABLE_NAME_REQUEST_INFO,
                            null, null,
                            null, null, null, null);
                    while (cur.moveToNext()) {
                        FWRequestInfo requestInfo = new FWRequestInfo();
                        requestInfo.setId(cur.getInt(cur.getColumnIndex("id")));
                        requestInfo.setCode(cur.getString(cur.getColumnIndex("code")));
                        requestInfo.setStatus(cur.getString(cur.getColumnIndex("status")));
                        requestInfo.setUid(cur.getLong(cur.getColumnIndex("uid")));
                        requestInfo.setVersion(cur.getInt(cur.getColumnIndex("version")));
                        requestInfo.setJoinInNum(cur.getInt(cur.getColumnIndex("joinInNum")));
                        requestInfo.setCreateTime(cur.getLong(cur.getColumnIndex("createTime")));
                        requestInfo.setModifyTime(cur.getLong(cur.getColumnIndex("modifyTime")));
                        requestInfo.setProviderId(cur.getString(cur.getColumnIndex("providerId")));
                        requestInfo.setServiceName(cur.getString(cur.getColumnIndex("serviceName")));
                        requestInfo.setServiceType(cur.getInt(cur.getColumnIndex("serviceType")));
                        requestInfo.setOpenUserId(cur.getString(cur.getColumnIndex("openUserId")));
                        requestInfo.setTelPhone(cur.getString(cur.getColumnIndex("telPhone")));
                        requestInfo.setPrice(cur.getFloat(cur.getColumnIndex("price")));
                        requestInfo.setNotifyNum(cur.getInt(cur.getColumnIndex("notifyNum")));
                        requestInfo.setExpectTime(cur.getLong(cur.getColumnIndex("expectTime")));
                        requestInfo.setExtendsion(cur.getString(cur.getColumnIndex("extendsion")));
                        requestInfo.setNickName(cur.getString(cur.getColumnIndex("nickName")));
                        list.add(requestInfo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });
        if (list.size() > 0)
            return list;
        return null;
    }

    public void saveRequestInfo(final List<FWRequestInfo> list) {
        DatabaseManager.initializeInstance(helper);
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                try {
                    database.beginTransaction();
                    database.delete(DatabaseHelper.TABLE_NAME_REQUEST_INFO, null, null);
                    for (FWRequestInfo requestInfo : list) {
                        ContentValues root = new ContentValues();
                        root.put("code", requestInfo.getCode());
                        root.put("status", requestInfo.getStatus());
                        root.put("uid", requestInfo.getUid());
                        root.put("version", requestInfo.getVersion());
                        root.put("joinInNum", requestInfo.getJoinInNum());
                        root.put("createTime", requestInfo.getCreateTime());
                        root.put("modifyTime", requestInfo.getModifyTime());
                        root.put("providerId", requestInfo.getProviderId());
                        root.put("serviceName", requestInfo.getServiceName());
                        root.put("serviceType", requestInfo.getServiceType());
                        root.put("openUserId", requestInfo.getOpenUserId());
                        root.put("telPhone", requestInfo.getTelPhone());
                        root.put("price", requestInfo.getPrice());
                        root.put("notifyNum", requestInfo.getNotifyNum());
                        root.put("expectTime", requestInfo.getExpectTime());
                        root.put("extendsion", requestInfo.getExtendsion());
                        root.put("nickName", requestInfo.getNickName());
                        database.insert(DatabaseHelper.TABLE_NAME_REQUEST_INFO, "id", root);
                    }
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (database != null)
                        database.endTransaction();
                }
            }
        });
    }
}
