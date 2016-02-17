package com.kplus.car.receiver;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.kplus.car.Constants;
import com.kplus.car.KplusApplication;
import com.kplus.car.R;
import com.kplus.car.activity.BaoyangActivity;
import com.kplus.car.activity.ChedaiActivity;
import com.kplus.car.activity.ChexianActivity;
import com.kplus.car.activity.CustomRemindActivity;
import com.kplus.car.activity.JiazhaoListActivity;
import com.kplus.car.activity.NianjianActivity;
import com.kplus.car.activity.RestrictEditActivity;
import com.kplus.car.model.Jiazhao;
import com.kplus.car.model.RemindBaoyang;
import com.kplus.car.model.RemindChedai;
import com.kplus.car.model.RemindChexian;
import com.kplus.car.model.RemindCustom;
import com.kplus.car.model.RemindNianjian;
import com.kplus.car.model.RemindRestrict;
import com.kplus.car.model.UserVehicle;
import com.kplus.car.util.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RemindManager extends BroadcastReceiver {
    private static RemindManager mInstance;
    private Context mContext;
    private AlarmManager mAlarmManager;
    private NotificationManager mNotificationManager;

    public RemindManager() {
    }

    public synchronized static RemindManager getInstance(Context context) {
        if (mInstance == null){
            mInstance = new RemindManager();
            mInstance.mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            mInstance.mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        mInstance.mContext = context;
        return  mInstance;
    }

    public void set(int type, String id){
        Intent it = new Intent(mContext, RemindManager.class);
        it.putExtra("type", type);
        it.putExtra("id", id);
        set(it);
    }

    public void set(int type, String vehicleNum, int subtype, String title){
        Intent it = new Intent(mContext, RemindManager.class);
        it.putExtra("type", type);
        it.putExtra("vehicleNum", vehicleNum);
        it.putExtra("subtype", subtype);
        it.putExtra("title", title);
        set(it);
    }

    private void set(Intent it){
        KplusApplication app = (KplusApplication)mContext.getApplicationContext();
        int type = it.getIntExtra("type", 0);
        String vehicleNum = it.getStringExtra("vehicleNum");
        switch (type){
            case Constants.REQUEST_TYPE_JIAZHAOFEN:
                String id = it.getStringExtra("id");
                Jiazhao jiazhao = app.dbCache.getJiazhao(id);
                if (jiazhao != null){
                    it.putExtra("jiazhao", jiazhao);
                    PendingIntent pi = PendingIntent.getBroadcast(mContext, 1000000 + jiazhao.getDbid() * 2, it, PendingIntent.FLAG_UPDATE_CURRENT);
                    try {
                        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(jiazhao.getRemindDate());
                        mAlarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pi);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Constants.REQUEST_TYPE_NIANJIAN:
                RemindNianjian remindNianjian = app.dbCache.getRemindNianjian(vehicleNum);
                if (remindNianjian != null){
                    it.putExtra("RemindNianjian", remindNianjian);
                    PendingIntent pi = PendingIntent.getBroadcast(mContext, 4000000 + remindNianjian.getId() * 2, it, PendingIntent.FLAG_UPDATE_CURRENT);
                    try {
                        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(remindNianjian.getRemindDate1());
                        mAlarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pi);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Constants.REQUEST_TYPE_CHEXIAN:
                int subtype = it.getIntExtra("subtype", 1);
                RemindChexian remindChexian = app.dbCache.getRemindChexian(vehicleNum, subtype);
                if (remindChexian != null){
                    it.putExtra("RemindChexian", remindChexian);
                    PendingIntent pi = PendingIntent.getBroadcast(mContext, 2000000 + remindChexian.getId() * 2, it, PendingIntent.FLAG_UPDATE_CURRENT);
                    try {
                        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(remindChexian.getRemindDate1());
                        mAlarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pi);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Constants.REQUEST_TYPE_BAOYANG:
                RemindBaoyang remindBaoyang = app.dbCache.getRemindBaoyang(vehicleNum);
                if (remindBaoyang != null){
                    it.putExtra("RemindBaoyang", remindBaoyang);
                    PendingIntent pi = PendingIntent.getBroadcast(mContext, 3000000 + remindBaoyang.getId() * 2, it, PendingIntent.FLAG_UPDATE_CURRENT);
                    try {
                        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(remindBaoyang.getRemindDate1());
                        mAlarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pi);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Constants.REQUEST_TYPE_CHEDAI:
                RemindChedai remindChedai = app.dbCache.getRemindChedai(vehicleNum);
                if (remindChedai != null){
                    it.putExtra("RemindChedai", remindChedai);
                    PendingIntent pi = PendingIntent.getBroadcast(mContext, 5000000 + remindChedai.getId() * 2, it, PendingIntent.FLAG_UPDATE_CURRENT);
                    try {
                        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(remindChedai.getRemindDate1());
                        mAlarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pi);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Constants.REQUEST_TYPE_CUSTOM:
                String title = it.getStringExtra("title");
                RemindCustom remindCustom = app.dbCache.getRemindCustom(vehicleNum, title);
                if (remindCustom != null){
                    it.putExtra("RemindCustom", remindCustom);
                    PendingIntent pi = PendingIntent.getBroadcast(mContext, 6000000 + remindCustom.getId() * 2, it, PendingIntent.FLAG_UPDATE_CURRENT);
                    try {
                        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(remindCustom.getRemindDate1());
                        mAlarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pi);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Constants.REQUEST_TYPE_RESTRICT:
                RemindRestrict restrict = app.dbCache.getRemindRestrict(vehicleNum);
                UserVehicle userVehicle = app.dbCache.getVehicle(vehicleNum);
                if (restrict != null && userVehicle != null && userVehicle.getRestrictNums() != null){
                    it.putExtra("RemindRestrict", restrict);
                    it.putExtra("UserVehicle", userVehicle);
                    PendingIntent pi = PendingIntent.getBroadcast(mContext, 7000000 + restrict.getDbid() * 2, it, PendingIntent.FLAG_UPDATE_CURRENT);
                    try {
                        Calendar calendar = DateUtil.getNextRestrictRemindDate(userVehicle.getRestrictNums(), vehicleNum, restrict.getRemindDateType(), restrict.getRemindDate());
                        mAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTime().getTime(), pi);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public void setAll(int type){
        Intent it = new Intent(mContext, RemindManager.class);
        it.putExtra("type", type);
        setAll(it);
    }

    private void setAll(Intent it) {
        KplusApplication app = (KplusApplication)mContext.getApplicationContext();
        int type = it.getIntExtra("type", 0);
        switch (type){
            case Constants.REQUEST_TYPE_JIAZHAOFEN:
                List<Jiazhao> jiazhaoList = app.dbCache.getJiazhaos();
                if (jiazhaoList != null){
                    for (Jiazhao jiazhao : jiazhaoList){
                        it.putExtra("jiazhao", jiazhao);
                        PendingIntent pi = PendingIntent.getBroadcast(mContext, 1000000 + jiazhao.getDbid() * 2, it, PendingIntent.FLAG_UPDATE_CURRENT);
                        try {
                            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(jiazhao.getRemindDate());
                            if (date.after(Calendar.getInstance().getTime()))
                                mAlarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pi);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case Constants.REQUEST_TYPE_NIANJIAN:
                List<RemindNianjian> nianjianList = app.dbCache.getRemindNianjian();
                if (nianjianList != null){
                    for (RemindNianjian remindNianjian : nianjianList){
                        it.putExtra("RemindNianjian", remindNianjian);
                        PendingIntent pi = PendingIntent.getBroadcast(mContext, 4000000 + remindNianjian.getId() * 2, it, PendingIntent.FLAG_UPDATE_CURRENT);
                        try {
                            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(remindNianjian.getRemindDate1());
                            if (date.after(Calendar.getInstance().getTime()))
                                mAlarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pi);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case Constants.REQUEST_TYPE_CHEXIAN:
                List<RemindChexian> chexianList = app.dbCache.getRemindChexian();
                if (chexianList != null){
                    for (RemindChexian remindChexian : chexianList){
                        it.putExtra("RemindChexian", remindChexian);
                        PendingIntent pi = PendingIntent.getBroadcast(mContext, 2000000 + remindChexian.getId() * 2, it, PendingIntent.FLAG_UPDATE_CURRENT);
                        try {
                            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(remindChexian.getRemindDate1());
                            if (date.after(Calendar.getInstance().getTime()))
                                mAlarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pi);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case Constants.REQUEST_TYPE_BAOYANG:
                List<RemindBaoyang> baoyangList = app.dbCache.getRemindBaoyang();
                if (baoyangList != null){
                    for (RemindBaoyang remindBaoyang : baoyangList){
                        it.putExtra("RemindBaoyang", remindBaoyang);
                        PendingIntent pi = PendingIntent.getBroadcast(mContext, 3000000 + remindBaoyang.getId() * 2, it, PendingIntent.FLAG_UPDATE_CURRENT);
                        try {
                            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(remindBaoyang.getRemindDate1());
                            if (date.after(Calendar.getInstance().getTime()))
                                mAlarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pi);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case Constants.REQUEST_TYPE_CHEDAI:
                List<RemindChedai> chedaiList = app.dbCache.getRemindChedai();
                if (chedaiList != null){
                    for (RemindChedai remindChedai : chedaiList){
                        it.putExtra("RemindChedai", remindChedai);
                        PendingIntent pi = PendingIntent.getBroadcast(mContext, 5000000 + remindChedai.getId() * 2, it, PendingIntent.FLAG_UPDATE_CURRENT);
                        try {
                            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(remindChedai.getRemindDate1());
                            if (date.after(Calendar.getInstance().getTime()))
                                mAlarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pi);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case Constants.REQUEST_TYPE_CUSTOM:
                List<RemindCustom> customList = app.dbCache.getRemindCustom();
                if (customList != null){
                    for (RemindCustom remindCustom : customList){
                        it.putExtra("RemindCustom", remindCustom);
                        PendingIntent pi = PendingIntent.getBroadcast(mContext, 6000000 + remindCustom.getId() * 2, it, PendingIntent.FLAG_UPDATE_CURRENT);
                        try {
                            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(remindCustom.getRemindDate1());
                            if (date.after(Calendar.getInstance().getTime()))
                                mAlarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pi);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case Constants.REQUEST_TYPE_RESTRICT:
                List<RemindRestrict> restrictList = app.dbCache.getRemindRestricts();
                if (restrictList != null){
                    for (RemindRestrict restrict : restrictList){
                        if ("0".equals(restrict.getIsHidden())) {
                            UserVehicle userVehicle = app.dbCache.getVehicle(restrict.getVehicleNum());
                            if (userVehicle != null && userVehicle.getRestrictNums() != null) {
                                it.putExtra("RemindRestrict", restrict);
                                it.putExtra("UserVehicle", userVehicle);
                                PendingIntent pi = PendingIntent.getBroadcast(mContext, 7000000 + restrict.getDbid() * 2, it, PendingIntent.FLAG_UPDATE_CURRENT);
                                try {
                                    Calendar calendar = DateUtil.getNextRestrictRemindDate(userVehicle.getRestrictNums(), restrict.getVehicleNum(), restrict.getRemindDateType(), restrict.getRemindDate());
                                    mAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTime().getTime(), pi);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                break;
        }
    }

    public void cancel(String vehicleNum){
        cancel(vehicleNum, Constants.REQUEST_TYPE_NIANJIAN);
        cancel(vehicleNum, Constants.REQUEST_TYPE_CHEXIAN);
        cancel(vehicleNum, Constants.REQUEST_TYPE_BAOYANG);
        cancel(vehicleNum, Constants.REQUEST_TYPE_CHEDAI);
        cancel(vehicleNum, Constants.REQUEST_TYPE_CUSTOM);
    }

    public void cancel(String vehicleNum, int type){
        Intent it = new Intent(mContext, RemindManager.class);
        it.putExtra("type", type);
        it.putExtra("vehicleNum", vehicleNum);
        cancel(it);
    }

    public void cancel(String vehicleNum, int type, String title){
        Intent it = new Intent(mContext, RemindManager.class);
        it.putExtra("type", type);
        it.putExtra("vehicleNum", vehicleNum);
        it.putExtra("title", title);
        cancel(it);
    }

    public void cancel(int type, String id){
        Intent it = new Intent(mContext, RemindManager.class);
        it.putExtra("type", type);
        it.putExtra("id", id);
        cancel(it);
    }

    private void cancel(Intent it){
        KplusApplication app = (KplusApplication)mContext.getApplicationContext();
        int type = it.getIntExtra("type", 0);
        String vehicleNum = it.getStringExtra("vehicleNum");
        switch (type){
            case Constants.REQUEST_TYPE_JIAZHAOFEN:
                String id = it.getStringExtra("id");
                Jiazhao jiazhao = app.dbCache.getJiazhao(id);
                if (jiazhao != null){
                    PendingIntent pi = PendingIntent.getBroadcast(mContext, 1000000 + jiazhao.getDbid() * 2, it, PendingIntent.FLAG_UPDATE_CURRENT);
                    mAlarmManager.cancel(pi);
                }
                break;
            case Constants.REQUEST_TYPE_NIANJIAN:
                RemindNianjian remindNianjian = app.dbCache.getRemindNianjian(vehicleNum);
                if (remindNianjian != null){
                    PendingIntent pi = PendingIntent.getBroadcast(mContext, 4000000 + remindNianjian.getId() * 2, it, PendingIntent.FLAG_UPDATE_CURRENT);
                    mAlarmManager.cancel(pi);
                }
                break;
            case Constants.REQUEST_TYPE_CHEXIAN:
                List<RemindChexian> list = app.dbCache.getRemindChexian(vehicleNum);
                if (list != null){
                    for (RemindChexian remindChexian : list){
                        PendingIntent pi = PendingIntent.getBroadcast(mContext, 2000000 + remindChexian.getId() * 2, it, PendingIntent.FLAG_UPDATE_CURRENT);
                        mAlarmManager.cancel(pi);
                    }
                }
                break;
            case Constants.REQUEST_TYPE_BAOYANG:
                RemindBaoyang remindBaoyang = app.dbCache.getRemindBaoyang(vehicleNum);
                if (remindBaoyang != null){
                    PendingIntent pi = PendingIntent.getBroadcast(mContext, 3000000 + remindBaoyang.getId() * 2, it, PendingIntent.FLAG_UPDATE_CURRENT);
                    mAlarmManager.cancel(pi);
                }
                break;
            case Constants.REQUEST_TYPE_CHEDAI:
                RemindChedai remindChedai = app.dbCache.getRemindChedai(vehicleNum);
                if (remindChedai != null){
                    PendingIntent pi = PendingIntent.getBroadcast(mContext, 5000000 + remindChedai.getId() * 2, it, PendingIntent.FLAG_UPDATE_CURRENT);
                    mAlarmManager.cancel(pi);
                }
                break;
            case Constants.REQUEST_TYPE_CUSTOM:
                String title = it.getStringExtra("title");
                if (!"".equals(title)){
                    RemindCustom remindCustom = app.dbCache.getRemindCustom(vehicleNum, title);
                    if (remindCustom != null){
                        PendingIntent pi = PendingIntent.getBroadcast(mContext, 6000000 + remindCustom.getId() * 2, it, PendingIntent.FLAG_UPDATE_CURRENT);
                        mAlarmManager.cancel(pi);
                    }
                }
                else {
                    List<RemindCustom> list1 = app.dbCache.getRemindCustom(vehicleNum);
                    if (list1 != null){
                        for (RemindCustom remindCustom : list1){
                            PendingIntent pi = PendingIntent.getBroadcast(mContext, 6000000 + remindCustom.getId() * 2, it, PendingIntent.FLAG_UPDATE_CURRENT);
                            mAlarmManager.cancel(pi);
                        }
                    }
                }
                break;
            case Constants.REQUEST_TYPE_RESTRICT:
                RemindRestrict restrict = app.dbCache.getRemindRestrict(vehicleNum);
                if (restrict != null){
                    PendingIntent pi = PendingIntent.getBroadcast(mContext, 7000000 + restrict.getDbid() * 2, it, PendingIntent.FLAG_UPDATE_CURRENT);
                    mAlarmManager.cancel(pi);
                }
                break;
        }
    }

    public void cancelAll(int type){
        Intent it = new Intent(mContext, RemindManager.class);
        it.putExtra("type", type);
        cancelAll(it);
    }

    private void cancelAll(Intent it) {
        KplusApplication app = (KplusApplication)mContext.getApplicationContext();
        int type = it.getIntExtra("type", 0);
        switch (type){
            case Constants.REQUEST_TYPE_JIAZHAOFEN:
                List<Jiazhao> jiazhaoList = app.dbCache.getJiazhaos();
                if (jiazhaoList != null){
                    for (Jiazhao jiazhao : jiazhaoList){
                        PendingIntent pi = PendingIntent.getBroadcast(mContext, 1000000 + jiazhao.getDbid() * 2, it, PendingIntent.FLAG_UPDATE_CURRENT);
                        mAlarmManager.cancel(pi);
                    }
                }
                break;
            case Constants.REQUEST_TYPE_NIANJIAN:
                List<RemindNianjian> nianjianList = app.dbCache.getRemindNianjian();
                if (nianjianList != null){
                    for (RemindNianjian remindNianjian : nianjianList){
                        PendingIntent pi = PendingIntent.getBroadcast(mContext, 4000000 + remindNianjian.getId() * 2, it, PendingIntent.FLAG_UPDATE_CURRENT);
                        mAlarmManager.cancel(pi);
                    }
                }
                break;
            case Constants.REQUEST_TYPE_CHEXIAN:
                List<RemindChexian> chexianList = app.dbCache.getRemindChexian();
                if (chexianList != null){
                    for (RemindChexian remindChexian : chexianList){
                        PendingIntent pi = PendingIntent.getBroadcast(mContext, 2000000 + remindChexian.getId() * 2, it, PendingIntent.FLAG_UPDATE_CURRENT);
                        mAlarmManager.cancel(pi);
                    }
                }
                break;
            case Constants.REQUEST_TYPE_BAOYANG:
                List<RemindBaoyang> baoyangList = app.dbCache.getRemindBaoyang();
                if (baoyangList != null){
                    for (RemindBaoyang remindBaoyang : baoyangList){
                        PendingIntent pi = PendingIntent.getBroadcast(mContext, 3000000 + remindBaoyang.getId() * 2, it, PendingIntent.FLAG_UPDATE_CURRENT);
                        mAlarmManager.cancel(pi);
                    }
                }
                break;
            case Constants.REQUEST_TYPE_CHEDAI:
                List<RemindChedai> chedaiList = app.dbCache.getRemindChedai();
                if (chedaiList != null){
                    for (RemindChedai remindChedai : chedaiList){
                        PendingIntent pi = PendingIntent.getBroadcast(mContext, 5000000 + remindChedai.getId() * 2, it, PendingIntent.FLAG_UPDATE_CURRENT);
                        mAlarmManager.cancel(pi);
                    }
                }
                break;
            case Constants.REQUEST_TYPE_CUSTOM:
                List<RemindCustom> customList = app.dbCache.getRemindCustom();
                if (customList != null){
                    for (RemindCustom remindCustom : customList){
                        PendingIntent pi = PendingIntent.getBroadcast(mContext, 6000000 + remindCustom.getId() * 2, it, PendingIntent.FLAG_UPDATE_CURRENT);
                        mAlarmManager.cancel(pi);
                    }
                }
                break;
            case Constants.REQUEST_TYPE_RESTRICT:
                List<RemindRestrict> restrictList = app.dbCache.getRemindRestricts();
                if (restrictList != null){
                    for (RemindRestrict restrict : restrictList){
                        PendingIntent pi = PendingIntent.getBroadcast(mContext, 7000000 + restrict.getDbid() * 2, it, PendingIntent.FLAG_UPDATE_CURRENT);
                        mAlarmManager.cancel(pi);
                    }
                }
                break;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String content = "";
        int type = intent.getIntExtra("type", 0);
        Intent it = null;
        switch (type) {
            case Constants.REQUEST_TYPE_JIAZHAOFEN:
                Jiazhao jiazhao = (Jiazhao) intent.getSerializableExtra("jiazhao");
                Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(jiazhao.getDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (jiazhao.getXm() != null && !"".equals(jiazhao.getXm())) {
                    content = jiazhao.getXm() + "的驾照分将于" + (calendar.get(Calendar.MONTH) + 1) + "月"
                            + calendar.get(Calendar.DATE) + "日更新";
                }
                else {
                    String jszh = jiazhao.getJszh();
                    content = "尾号" + jszh.substring(jszh.length() - 4) + "的驾照分将于" + (calendar.get(Calendar.MONTH) + 1) + "月"
                            + calendar.get(Calendar.DATE) + "日更新";
                }
                it = new Intent(context, JiazhaoListActivity.class);
                break;
            case Constants.REQUEST_TYPE_NIANJIAN:
                RemindNianjian remindNianjian = (RemindNianjian) intent.getSerializableExtra("RemindNianjian");
                calendar = Calendar.getInstance();
                try {
                    calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(remindNianjian.getDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                content = "您的爱车" + remindNianjian.getVehicleNum() + "年检将于" + (calendar.get(Calendar.MONTH) + 1) + "月"
                        + calendar.get(Calendar.DATE) + "日到期";
                it = new Intent(context, NianjianActivity.class);
                it.putExtra("RemindNianjian", remindNianjian);
                break;
            case Constants.REQUEST_TYPE_CHEXIAN:
                RemindChexian remindChexian = (RemindChexian) intent.getSerializableExtra("RemindChexian");
                calendar = Calendar.getInstance();
                try {
                    calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(remindChexian.getDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (remindChexian.getType() == 1){
                    content = "您的爱车" + remindChexian.getVehicleNum() + "交强险将于" + (calendar.get(Calendar.MONTH) + 1) + "月"
                            + calendar.get(Calendar.DATE) + "日到期";
                }
                else{
                    content = "您的爱车" + remindChexian.getVehicleNum() + "商业险将于" + (calendar.get(Calendar.MONTH) + 1) + "月"
                            + calendar.get(Calendar.DATE) + "日到期";
                }
                it = new Intent(context, ChexianActivity.class);
                it.putExtra("vehicleNum", remindChexian.getVehicleNum());
                it.putExtra("type", remindChexian.getType());
                break;
            case Constants.REQUEST_TYPE_BAOYANG:
                RemindBaoyang remindBaoyang = (RemindBaoyang) intent.getSerializableExtra("RemindBaoyang");
                calendar = Calendar.getInstance();
                try {
                    calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(remindBaoyang.getDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                content = "您的爱车" + remindBaoyang.getVehicleNum() + "需要在" + (calendar.get(Calendar.MONTH) + 1) + "月"
                        + calendar.get(Calendar.DATE) + "日进行保养";
                it = new Intent(context, BaoyangActivity.class);
                it.putExtra("RemindBaoyang", remindBaoyang);
                break;
            case Constants.REQUEST_TYPE_CHEDAI:
                RemindChedai remindChedai = (RemindChedai) intent.getSerializableExtra("RemindChedai");
                calendar = Calendar.getInstance();
                try {
                    calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(remindChedai.getDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                content = "您的爱车" + remindChedai.getVehicleNum() + (calendar.get(Calendar.MONTH) + 1) + "月"
                        + calendar.get(Calendar.DATE) + "日需要还车贷啦";
                it = new Intent(context, ChedaiActivity.class);
                it.putExtra("RemindChedai", remindChedai);
                break;
            case Constants.REQUEST_TYPE_CUSTOM:
                RemindCustom remindCustom = (RemindCustom) intent.getSerializableExtra("RemindCustom");
                calendar = Calendar.getInstance();
                try {
                    calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(remindCustom.getDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                content = "您的爱车" + remindCustom.getVehicleNum() + remindCustom.getName() + "将于" + (calendar.get(Calendar.MONTH) + 1) + "月"
                        + calendar.get(Calendar.DATE) + "日到期";
                it = new Intent(context, CustomRemindActivity.class);
                it.putExtra("RemindCustom", remindCustom);
                break;
            case Constants.REQUEST_TYPE_RESTRICT:
                UserVehicle userVehicle = (UserVehicle) intent.getSerializableExtra("UserVehicle");
                RemindRestrict restrict = (RemindRestrict) intent.getSerializableExtra("RemindRestrict");
                Intent intent1 = new Intent(context, RemindManager.class);
                intent1.putExtra("type", type);
                intent1.putExtra("vehicleNum", userVehicle.getVehicleNum());
                PendingIntent pi = PendingIntent.getBroadcast(context, 7000000 + restrict.getDbid() * 2, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                try {
                    calendar = DateUtil.getNextRestrictRemindDate(userVehicle.getRestrictNums(), restrict.getVehicleNum(), restrict.getRemindDateType(), restrict.getRemindDate());
                    mAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTime().getTime(), pi);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String date = "1".equals(restrict.getRemindDateType()) ? "明天" : "今日";
                content = "您的爱车" + restrict.getVehicleNum() + date + "限行，请合理安排出行时间";
                it = new Intent(context, RestrictEditActivity.class);
                it.putExtra("RemindRestrict", restrict);
                it.putExtra("UserVehicle", userVehicle);
                break;
        }
        try {
            int id = (int) System.currentTimeMillis();
            PendingIntent pi = PendingIntent.getActivity(context, id, it, PendingIntent.FLAG_UPDATE_CURRENT);
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.notification_view);
            rv.setImageViewResource(R.id.icon, R.drawable.daze_icon);
            rv.setTextViewText(R.id.title, "橙牛汽车管家");
            rv.setTextViewText(R.id.content, content);
            NotificationCompat.Builder n = new NotificationCompat.Builder(context);
            n.setSmallIcon(R.drawable.daze_icon).setTicker(content).setContent(rv).
                    setDefaults(NotificationCompat.DEFAULT_ALL).setAutoCancel(true).setContentIntent(pi);
            getInstance(context).mNotificationManager.notify(id, n.build());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
