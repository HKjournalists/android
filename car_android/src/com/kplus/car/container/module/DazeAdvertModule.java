package com.kplus.car.container.module;

import com.kplus.car.container.command.DazeInvokedUrlCommand;
import com.kplus.car.model.Advert;
import com.kplus.car.util.AdvertUtil;

import org.json.JSONObject;

public class DazeAdvertModule extends DazeModule implements DazeModuleDelegate {
    @Override
    public String methodsForJS() {
        return "\"advertClick\"";
    }

    public void advertClick(DazeInvokedUrlCommand command){
        JSONObject result = new JSONObject();
        String status = "成功";
        try {
            JSONObject arguments = command.getArguments();
            Advert advert = new Advert();
            advert.setId(arguments.optLong("id"));
            advert.setImgUrl(arguments.optString("imgUrl"));
            advert.setMotionType(arguments.optString("motionType"));
            advert.setMotionValue(arguments.optString("motionValue"));
            advert.setSeq(arguments.optInt("seq"));
            advert.setCanClose(arguments.optBoolean("canClose"));
            AdvertUtil au = new AdvertUtil(viewController.getContext(), advert, arguments.optString("identity"));
            au.OnAdvertClick();
        }catch(Exception e){
            e.printStackTrace();
            status = "失败";
        }
        finally {
            if(command.getCallbackId() != null)
                sendResult(result, command, false);
        }
    }
}
