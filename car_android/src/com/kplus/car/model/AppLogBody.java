package com.kplus.car.model;

/**
 * Created by Administrator on 2015/9/21.
 */
public class AppLogBody {
    private AppLogLaunch launch;
    private AppLogTerminate terminate;
    private AppLogPage page;
    private AppLogEvent event;

    public AppLogLaunch getLaunch() {
        return launch;
    }

    public void setLaunch(AppLogLaunch launch) {
        this.launch = launch;
    }

    public AppLogTerminate getTerminate() {
        return terminate;
    }

    public void setTerminate(AppLogTerminate terminate) {
        this.terminate = terminate;
    }

    public AppLogPage getPage() {
        return page;
    }

    public void setPage(AppLogPage page) {
        this.page = page;
    }

    public AppLogEvent getEvent() {
        return event;
    }

    public void setEvent(AppLogEvent event) {
        this.event = event;
    }
}
