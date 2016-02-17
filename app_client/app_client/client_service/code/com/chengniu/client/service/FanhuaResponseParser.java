package com.chengniu.client.service;

import com.chengniu.client.pojo.BaoxianInformalReport;
import com.chengniu.client.pojo.BaoxianUnderwriting;
import com.chengniu.client.pojo.BaoxianUnderwritingReport;
import com.chengniu.client.pojo.BaoxianUnderwritingRequest;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;

/**
 * 泛华接口处理
 */
public class FanhuaResponseParser {

    public static void parseRequest(JsonObject response,
                                    BaoxianUnderwritingRequest underwriting) {
        JsonObject dealOffer = response.get("dealOffer").getAsJsonObject();
        JsonObject info = dealOffer.get("suite").getAsJsonObject().get("items")
                .getAsJsonObject();
        try {
            underwriting.setCsx(info.get("VehicleDemageIns").getAsJsonObject()
                    .get("insuranceAmount").getAsString());
        } catch (Exception e) {
            underwriting.setCsx("0");
        }
        try {
            underwriting.setSjzrx(String.valueOf(new BigDecimal(info
                    .get("DriverIns").getAsJsonObject().get("insuranceAmount")
                    .getAsString()).intValue()));
        } catch (Exception e) {
            underwriting.setSjzrx("0");
        }
        try {
            underwriting.setSzx(String.valueOf(new BigDecimal(info
                    .get("ThirdPartyIns").getAsJsonObject()
                    .get("insuranceAmount").getAsString()).intValue()));
        } catch (Exception e) {
            underwriting.setSzx("0");
        }
        try {
            underwriting.setBlx(info.get("GlassIns").getAsJsonObject()
                    .get("insuranceAmount").getAsString());
        } catch (Exception e) {
            underwriting.setBlx("0");
        }
        try {
            underwriting.setHfx(String.valueOf(new BigDecimal(info
                    .get("ScratchIns").getAsJsonObject().get("insuranceAmount")
                    .getAsString()).intValue()));
        } catch (Exception e) {
            underwriting.setHfx("0");
        }
        try {
            underwriting.setZrx(String.valueOf(new BigDecimal(info
                    .get("CombustionIns").getAsJsonObject()
                    .get("insuranceAmount").getAsString()).intValue()));
        } catch (Exception e) {
            underwriting.setZrx("0");
        }
        try {
            underwriting.setCkx(String.valueOf(new BigDecimal(info
                    .get("PassengerIns").getAsJsonObject()
                    .get("insuranceAmount").getAsString()).intValue()));
        } catch (Exception e) {
            underwriting.setCkx("0");
        }
        try {
            underwriting.setJqx(Float.parseFloat(info
                    .get("VehicleCompulsoryIns").getAsJsonObject()
                    .get("insuranceAmount").getAsString()) > 0);
        } catch (Exception e) {
            underwriting.setJqx(false);
        }
        try {
            underwriting.setDqx(String.valueOf(new BigDecimal(info
                    .get("TheftIns").getAsJsonObject().get("insuranceAmount")
                    .getAsString()).intValue()));
        } catch (Exception e) {
            underwriting.setDqx("0");
        }
        try {
            underwriting.setCsxBjmp(Float.parseFloat(info
                    .get("NcfVehicleDemageIns").getAsJsonObject()
                    .get("insuranceAmount").getAsString()) > 0);
        } catch (Exception e) {
            underwriting.setCsxBjmp(false);
        }
        try {
            underwriting
                    .setDqxBjmp(Float.parseFloat(info.get("NcfTheftIns")
                            .getAsJsonObject().get("insuranceAmount")
                            .getAsString()) > 0);
        } catch (Exception e) {
            underwriting.setDqxBjmp(false);
        }
        try {
            underwriting
                    .setBlxBjmp(Float.parseFloat(info.get("NcfGlassIns")
                            .getAsJsonObject().get("insuranceAmount")
                            .getAsString()) > 0);
        } catch (Exception e) {
            underwriting.setBlxBjmp(false);
        }
        try {
            underwriting.setSzxBjmp(Float.parseFloat(info
                    .get("NcfThirdPartyIns").getAsJsonObject()
                    .get("insuranceAmount").getAsString()) > 0);
        } catch (Exception e) {
            underwriting.setSzxBjmp(false);
        }
        try {
            underwriting.setZrxBjmp(Float.parseFloat(info
                    .get("NcfCombustionIns").getAsJsonObject()
                    .get("insuranceAmount").getAsString()) > 0);
        } catch (Exception e) {
            underwriting.setZrxBjmp(false);
        }
        try {
            underwriting.setCkxBjmp(Float.parseFloat(info
                    .get("NcfPassengerIns").getAsJsonObject()
                    .get("insuranceAmount").getAsString()) > 0);
        } catch (Exception e) {
            underwriting.setCkxBjmp(false);
        }
        try {
            underwriting
                    .setSsxBjmp(Float.parseFloat(info.get("NcfWadingIns")
                            .getAsJsonObject().get("insuranceAmount")
                            .getAsString()) > 0);
        } catch (Exception e) {
            underwriting.setSsxBjmp(false);
        }
        try {
            underwriting
                    .setSjzrxBjmp(Float.parseFloat(info.get("NcfDriverIns")
                            .getAsJsonObject().get("insuranceAmount")
                            .getAsString()) > 0);
        } catch (Exception e) {
            underwriting.setSjzrxBjmp(false);
        }
        try {
            underwriting
                    .setHfxBjmp(Float.parseFloat(info.get("NcfScratchIns")
                            .getAsJsonObject().get("insuranceAmount")
                            .getAsString()) > 0);
        } catch (Exception e) {
            underwriting.setBlxBjmp(false);
        }
    }

    public static void parseRequest(JsonObject result, BaoxianUnderwriting underwriting) {
        JsonObject dealOffer = result.get("dealOffer").getAsJsonObject();
        JsonObject info = dealOffer.get("suite").getAsJsonObject().get("items")
                .getAsJsonObject();

        try {
            underwriting
                    .setCsx(info.get("VehicleDemageIns").getAsJsonObject()
                            .get("insuranceAmount").getAsString());
        } catch (Exception e) {
            underwriting.setCsx("0");
        }
        try {
            underwriting.setSjzrx(String.valueOf(new BigDecimal(info
                    .get("DriverIns").getAsJsonObject()
                    .get("insuranceAmount").getAsString()).intValue()));
        } catch (Exception e) {
            underwriting.setSjzrx("0");
        }
        try {
            underwriting.setSzx(String.valueOf(new BigDecimal(
                    info.get("ThirdPartyIns").getAsJsonObject()
                            .get("insuranceAmount").getAsString()).intValue()));
        } catch (Exception e) {
            underwriting.setSzx("0");
        }
        try {
            underwriting.setBlx(info.get("GlassIns").getAsJsonObject()
                    .get("insuranceAmount").getAsString());
        } catch (Exception e) {
            underwriting.setBlx("0");
        }
        try {
            underwriting.setHfx(String.valueOf(new BigDecimal(info
                    .get("ScratchIns").getAsJsonObject()
                    .get("insuranceAmount").getAsString()).intValue()));
        } catch (Exception e) {
            underwriting.setHfx("0");
        }
        try {
            underwriting.setZrx(String.valueOf(new BigDecimal(
                    info.get("CombustionIns").getAsJsonObject()
                            .get("insuranceAmount").getAsString()).intValue()));
        } catch (Exception e) {
            underwriting.setZrx("0");
        }
        try {
            underwriting.setCkx(String.valueOf(new BigDecimal(info
                    .get("PassengerIns").getAsJsonObject()
                    .get("insuranceAmount").getAsString()).intValue()));
        } catch (Exception e) {
            underwriting.setCkx("0");
        }
        try {
            underwriting.setJqx(Float.parseFloat(info
                    .get("VehicleCompulsoryIns").getAsJsonObject()
                    .get("insuranceAmount").getAsString()) > 0);
        } catch (Exception e) {
            underwriting.setJqx(false);
        }
        try {
            underwriting.setDqx(String.valueOf(new BigDecimal(info
                    .get("TheftIns").getAsJsonObject()
                    .get("insuranceAmount").getAsString()).intValue()));
        } catch (Exception e) {
            underwriting.setDqx("0");
        }
        try {
            underwriting.setCsxBjmp(Float.parseFloat(info
                    .get("NcfVehicleDemageIns").getAsJsonObject()
                    .get("insuranceAmount").getAsString()) > 0);
        } catch (Exception e) {
            underwriting.setCsxBjmp(false);
        }
        try {
            underwriting.setDqxBjmp(Float.parseFloat(info
                    .get("NcfTheftIns").getAsJsonObject()
                    .get("insuranceAmount").getAsString()) > 0);
        } catch (Exception e) {
            underwriting.setDqxBjmp(false);
        }
        try {
            underwriting.setBlxBjmp(Float.parseFloat(info
                    .get("NcfGlassIns").getAsJsonObject()
                    .get("insuranceAmount").getAsString()) > 0);
        } catch (Exception e) {
            underwriting.setBlxBjmp(false);
        }
        try {
            underwriting.setSzxBjmp(Float.parseFloat(info
                    .get("NcfThirdPartyIns").getAsJsonObject()
                    .get("insuranceAmount").getAsString()) > 0);
        } catch (Exception e) {
            underwriting.setSzxBjmp(false);
        }
        try {
            underwriting.setZrxBjmp(Float.parseFloat(info
                    .get("NcfCombustionIns").getAsJsonObject()
                    .get("insuranceAmount").getAsString()) > 0);
        } catch (Exception e) {
            underwriting.setZrxBjmp(false);
        }
        try {
            underwriting.setCkxBjmp(Float.parseFloat(info
                    .get("NcfPassengerIns").getAsJsonObject()
                    .get("insuranceAmount").getAsString()) > 0);
        } catch (Exception e) {
            underwriting.setCkxBjmp(false);
        }
        try {
            underwriting.setSsxBjmp(Float.parseFloat(info
                    .get("NcfWadingIns").getAsJsonObject()
                    .get("insuranceAmount").getAsString()) > 0);
        } catch (Exception e) {
            underwriting.setSsxBjmp(false);
        }
        try {
            underwriting.setSjzrxBjmp(Float.parseFloat(info
                    .get("NcfDriverIns").getAsJsonObject()
                    .get("insuranceAmount").getAsString()) > 0);
        } catch (Exception e) {
            underwriting.setSjzrxBjmp(false);
        }
        try {
            underwriting.setHfxBjmp(Float.parseFloat(info
                    .get("NcfScratchIns").getAsJsonObject()
                    .get("insuranceAmount").getAsString()) > 0);
        } catch (Exception e) {
            underwriting.setBlxBjmp(false);
        }
    }

    public static BaoxianInformalReport parseReport(JsonObject result,
                                                    BaoxianInformalReport report) {
        JsonObject dealOffer = result.get("dealOffer").getAsJsonObject();
        JsonObject dealback = result.get("dealBack").getAsJsonObject();
        try {
            report.setJqxStartDate(DateUtils.parseDate(
                    dealOffer.get("efcEffectiveDate").getAsString(),
                    "yyyy-MM-dd"));
        } catch (Exception e) {
        }
        try {
            report.setSyxStartDate(DateUtils.parseDate(
                    dealOffer.get("bizEffectiveDate").getAsString(),
                    "yyyy-MM-dd"));
        } catch (Exception e) {
        }
        JsonObject info = dealOffer.get("suite").getAsJsonObject().get("items")
                .getAsJsonObject();
        try {
            report.setCsxPrice(new BigDecimal(info.get("VehicleDemageIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setCsxBjmpPrice(new BigDecimal(info
                    .get("NcfVehicleDemageIns").getAsJsonObject().get("amount")
                    .getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setCcsPrice(new BigDecimal(info.get("VehicleTax")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setSzxPrice(new BigDecimal(info.get("ThirdPartyIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setSzxBjmpPrice(new BigDecimal(info.get("NcfThirdPartyIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setSsxBjmpPrice(new BigDecimal(info.get("NcfWadingIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setSsxPrice(new BigDecimal(info.get("WadingIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setSjzrxBjmpPrice(new BigDecimal(info.get("NcfDriverIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setSjzrxPrice(new BigDecimal(info.get("DriverIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setSyxBjmpPrice(new BigDecimal(info.get("NcfDriverIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setCkxBjmpPrice(new BigDecimal(info.get("NcfPassengerIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setCkxPrice(new BigDecimal(info.get("PassengerIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setZrxPrice(new BigDecimal(info.get("CombustionIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setZrxBjmpPrice(new BigDecimal(info.get("NcfCombustionIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setJqxPrice(new BigDecimal(dealback.get("efcCharge")
                    .getAsString()));
        } catch (Exception e) {
            try {
                report.setJqxPrice(new BigDecimal(info
                        .get("VehicleCompulsoryIns").getAsJsonObject()
                        .get("amount").getAsString()));
            } catch (Exception ex) {
            }
        }
        try {
            report.setSyxPrice(new BigDecimal(dealback.get("bizCharge")
                    .getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setDqxBjmpPrice(new BigDecimal(info.get("NcfTheftIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setDqxPrice(new BigDecimal(info.get("TheftIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setBlxPrice(new BigDecimal(info.get("GlassIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setHfxBjmpPrice(new BigDecimal(info.get("NcfScratchIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setHfxPrice(new BigDecimal(info.get("ScratchIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setZdzxcPrice(new BigDecimal(info.get("SpecifyingPlantCla")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setTotalPrice(new BigDecimal(dealback.get("totalCharge")
                    .getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setMarketPrice(new BigDecimal(dealback.get("paymentPrice")
                    .getAsString()));
        } catch (Exception e) {
            try {
                report.setMarketPrice(new BigDecimal(dealback
                        .get("insurePrice").getAsString()));
            } catch (Exception e1) {
                report.setMarketPrice(report.getTotalPrice());
            }
        }
        try {
            report.setCpcssxPrice(new BigDecimal(info.get("CarToCarDamageIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setZrmctyPrice(new BigDecimal(info
                    .get("CombustionExclusionCla").getAsJsonObject()
                    .get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setSsmctyPrice(new BigDecimal(info.get("WadingExclusionCla")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setKxmptyPrice(new BigDecimal(info
                    .get("OptionalDeductiblesCla").getAsJsonObject()
                    .get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setDcsgmptyPrice(new BigDecimal(info
                    .get("AccidentDeductiblesCla").getAsJsonObject()
                    .get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setXzsbssxPrice(new BigDecimal(info.get("NewEquipmentIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setXzsbssxBjmpPrice(new BigDecimal(info
                    .get("NcfNewEquipmentIns").getAsJsonObject().get("amount")
                    .getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setCshwzrxPrice(new BigDecimal(info.get("GoodsOnVehicleIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setCshwzrxBjmpPrice(new BigDecimal(info
                    .get("NcfGoodsOnVehicleIns").getAsJsonObject()
                    .get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setSsxlwpssxPrice(new BigDecimal(info
                    .get("LossOfBaggageIns").getAsJsonObject().get("amount")
                    .getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setJlctyPrice(new BigDecimal(info.get("TrainingCarCla")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setFjxBjmpPrice(new BigDecimal(info
                    .get("NcfAddtionalClause").getAsJsonObject().get("amount")
                    .getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setJbxBjmpPrice(new BigDecimal(info.get("NcfBasicClause")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setFjcsryzrxBjmpPrice(new BigDecimal(info
                    .get("NcfDriverPassengerIns").getAsJsonObject()
                    .get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setJdctsssxPrice(new BigDecimal(info
                    .get("VehicleSuspendedIns").getAsJsonObject().get("amount")
                    .getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setOtherPrice(new BigDecimal(info.get("OthersIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setJsshfwjzrxPirce(new BigDecimal(info
                    .get("CompensationForMentalDistressIns").getAsJsonObject()
                    .get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setSgzrmpltyPrice(new BigDecimal(info
                    .get("AccidentFranchiseCla").getAsJsonObject()
                    .get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setTzckzxPrice(new BigDecimal(info
                    .get("SpecialVehicleExtensionIns").getAsJsonObject()
                    .get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setXlqjfybcxPrice(new BigDecimal(info
                    .get("CompensationDuringRepairIn").getAsJsonObject()
                    .get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setDcjcdshxPrice(new BigDecimal(info.get("MirrorLightIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setJdcssbxwfzddsftyxPrice(new BigDecimal(info
                    .get("VehicleDemageMissedThirdPartyCla").getAsJsonObject()
                    .get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setBjmpxPrice(new BigDecimal(info.get("NcfClause")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setExpireTime(com.kplus.orders.rpc.common.DateUtils.parse(
                    dealback.get("payLimitDate").getAsString(),
                    "yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
        }
        return report;
    }

    public static void parseReport(JsonObject result, BaoxianUnderwritingReport report) {
        JsonObject dealOffer = result.get("dealOffer").getAsJsonObject();
        JsonObject dealback = result.get("dealBack").getAsJsonObject();
        try {
            report.setJqxStartDate(com.kplus.orders.rpc.common.DateUtils.parse(
                    dealOffer.get("efcEffectiveDate").getAsString(),
                    "yyyy-MM-dd"));
        } catch (Exception e) {
        }

        try {
            report.setSyxStartDate(com.kplus.orders.rpc.common.DateUtils.parse(
                    dealOffer.get("bizEffectiveDate").getAsString(),
                    "yyyy-MM-dd"));
        } catch (Exception e) {
        }
        JsonObject info = dealOffer.get("suite").getAsJsonObject().get("items")
                .getAsJsonObject();
        try {
            report.setCsxPrice(new BigDecimal(info.get("VehicleDemageIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setCsxBjmpPrice(new BigDecimal(info
                    .get("NcfVehicleDemageIns").getAsJsonObject().get("amount")
                    .getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setCcsPrice(new BigDecimal(info.get("VehicleTax")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setSzxPrice(new BigDecimal(info.get("ThirdPartyIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setSzxBjmpPrice(new BigDecimal(info.get("NcfThirdPartyIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setSsxBjmpPrice(new BigDecimal(info.get("NcfWadingIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setSsxPrice(new BigDecimal(info.get("WadingIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setSjzrxBjmpPrice(new BigDecimal(info.get("NcfDriverIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setSjzrxPrice(new BigDecimal(info.get("DriverIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setSyxBjmpPrice(new BigDecimal(info.get("NcfDriverIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setCkxBjmpPrice(new BigDecimal(info.get("NcfPassengerIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setCkxPrice(new BigDecimal(info.get("PassengerIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setZrxPrice(new BigDecimal(info.get("CombustionIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setZrxBjmpPrice(new BigDecimal(info.get("NcfCombustionIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setJqxPrice(new BigDecimal(dealback.get("efcCharge")
                    .getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setSyxPrice(new BigDecimal(dealback.get("bizCharge")
                    .getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setDqxBjmpPrice(new BigDecimal(info.get("NcfTheftIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setDqxPrice(new BigDecimal(info.get("TheftIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setBlxPrice(new BigDecimal(info.get("GlassIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setHfxBjmpPrice(new BigDecimal(info.get("NcfScratchIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setHfxPrice(new BigDecimal(info.get("ScratchIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setZdzxcPrice(new BigDecimal(info.get("SpecifyingPlantCla")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setTotalPrice(new BigDecimal(dealback.get("totalCharge")
                    .getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setExpireTime(com.kplus.orders.rpc.common.DateUtils.parse(dealback.get("payLimitDate")
                    .getAsString(), "yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
        }
        try {
            report.setSyxPropNum(dealback.get("bizPropNum").getAsString());
            report.setJqxPropNum(dealback.get("efcPropNum").getAsString());
        } catch (Exception e) {
        }
        try {
            report.setJqxPolicyNo(dealback.get("efcPolicyCode").getAsString());
            report.setSyxPolicyNo(dealback.get("bizPolicyCode").getAsString());
        } catch (Exception e1) {
        }
        try {
            report.setMarketPrice(new BigDecimal(dealback.get("paymentPrice")
                    .getAsString()));
        } catch (Exception e) {
            try {
                report.setMarketPrice(new BigDecimal(dealback
                        .get("insurePrice").getAsString()));
            } catch (Exception e1) {
                report.setMarketPrice(report.getTotalPrice());
            }
        }
        try {
            report.setCpcssxPrice(new BigDecimal(info.get("CarToCarDamageIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setZrmctyPrice(new BigDecimal(info
                    .get("CombustionExclusionCla").getAsJsonObject()
                    .get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setSsmctyPrice(new BigDecimal(info.get("WadingExclusionCla")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setKxmptyPrice(new BigDecimal(info
                    .get("OptionalDeductiblesCla").getAsJsonObject()
                    .get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setDcsgmptyPrice(new BigDecimal(info
                    .get("AccidentDeductiblesCla").getAsJsonObject()
                    .get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setXzsbssxPrice(new BigDecimal(info.get("NewEquipmentIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setXzsbssxBjmpPrice(new BigDecimal(info
                    .get("NcfNewEquipmentIns").getAsJsonObject().get("amount")
                    .getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setCshwzrxPrice(new BigDecimal(info.get("GoodsOnVehicleIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setCshwzrxBjmpPrice(new BigDecimal(info
                    .get("NcfGoodsOnVehicleIns").getAsJsonObject()
                    .get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setSsxlwpssxPrice(new BigDecimal(info
                    .get("LossOfBaggageIns").getAsJsonObject().get("amount")
                    .getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setJlctyPrice(new BigDecimal(info.get("TrainingCarCla")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setFjxBjmpPrice(new BigDecimal(info
                    .get("NcfAddtionalClause").getAsJsonObject().get("amount")
                    .getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setJbxBjmpPrice(new BigDecimal(info.get("NcfBasicClause")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setFjcsryzrxBjmpPrice(new BigDecimal(info
                    .get("NcfDriverPassengerIns").getAsJsonObject()
                    .get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setJdctsssxPrice(new BigDecimal(info
                    .get("VehicleSuspendedIns").getAsJsonObject().get("amount")
                    .getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setOtherPrice(new BigDecimal(info.get("OthersIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setJsshfwjzrxPirce(new BigDecimal(info
                    .get("CompensationForMentalDistressIns").getAsJsonObject()
                    .get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setSgzrmpltyPrice(new BigDecimal(info
                    .get("AccidentFranchiseCla").getAsJsonObject()
                    .get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setTzckzxPrice(new BigDecimal(info
                    .get("SpecialVehicleExtensionIns").getAsJsonObject()
                    .get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setXlqjfybcxPrice(new BigDecimal(info
                    .get("CompensationDuringRepairIn").getAsJsonObject()
                    .get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setDcjcdshxPrice(new BigDecimal(info.get("MirrorLightIns")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setJdcssbxwfzddsftyxPrice(new BigDecimal(info
                    .get("VehicleDemageMissedThirdPartyCla").getAsJsonObject()
                    .get("amount").getAsString()));
        } catch (Exception e) {
        }
        try {
            report.setBjmpxPrice(new BigDecimal(info.get("NcfClause")
                    .getAsJsonObject().get("amount").getAsString()));
        } catch (Exception e) {
        }
    }

}
