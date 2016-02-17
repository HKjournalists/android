package com.chengniu.client.common;

import com.daze.intergation.dto.FundInRequest;
import com.daze.intergation.utils.ChannelCodeEnum;
import com.daze.intergation.utils.InstCodeEnum;

public enum PayType {
	UNKOWN(0), // 未知
	ALIPAYAPP(1), // 支付宝快捷支付
	ALIPAYWAP(2), // 支付宝wap支付
	UNAPP(3), // 银联快捷支付
	UNWAP(4), // 银联wap支付
	BALANCE(5), // 余额支付
	ALIPAYAPPBALANCE(6), // 支付宝快捷+余额支付
	ALIPAYWAPBALANCE(7), // 支付宝wap+余额支付
	UNAPPBALANCE(8), // 银联快捷支付+余额支付
	UNWAPBALANCE(9), // 银联wap支付+余额支付
	WXAPP(10), // //微信支付
	WXAPPBALANCE(11), // 微信+余额支付
	LIANLIAN(12), // 连连科技支付
	LIANLIAN_WEB(13), // 连连科技wap支付
	BALANCE_LIANLIAN(14), // 连连科技+余额支付
	BALANCE_LIANLIAN_WEB(15), // 连连科技wap+余额支付
	OPENTRADE(16), // 阿里百川支付
	BALANCE_OPENTRADE(17);// 阿里百川+余额支付

	private final int value;

	PayType(int intValue) {
		value = intValue;
	}

	public int value() {
		return value;
	}

	public String readableName() {
		switch (this.value) {
		case 1:
			return "支付宝快捷支付";
		case 2:
			return "支付宝wap支付";
		case 3:
			return "银联快捷支付";
		case 4:
			return "银联wap支付";
		case 5:
			return "余额支付";
		case 6:
			return "支付宝快捷+余额支付";
		case 7:
			return "支付宝wap+余额支付";
		case 8:
			return "银联快捷支付+余额支付";
		case 9:
			return "银联wap支付+余额支付";
		case 10:
			return "微信支付";
		case 11:
			return "微信+余额支付";
		case 12:
			return "连连科技支付";
		case 13:
			return "连连科技wap支付";
		case 14:
			return "连连科技+余额支付";
		case 15:
			return "连连科技wap+余额支付";
		case 16:
			return "阿里百川支付";
		case 17:
			return "阿里百川+余额支付";
		}

		return "未知";
	}

	public static PayType valueOf(int v) {
		switch (v) {
		case 1:
			return ALIPAYAPP;
		case 2:
			return ALIPAYWAP;
		case 3:
			return UNWAPBALANCE;
		case 4:
			return UNWAP;
		case 5:
			return BALANCE;
		case 6:
			return ALIPAYAPPBALANCE;
		case 7:
			return ALIPAYWAPBALANCE;
		case 8:
			return UNAPPBALANCE;
		case 9:
			return UNWAPBALANCE;
		case 10:
			return WXAPP;
		case 11:
			return WXAPPBALANCE;
		case 12:
			return LIANLIAN;
		case 13:
			return LIANLIAN_WEB;
		case 14:
			return BALANCE_LIANLIAN;
		case 15:
			return BALANCE_LIANLIAN_WEB;
		case 16:
			return OPENTRADE;
		case 17:
			return BALANCE_OPENTRADE;
		}
		return UNKOWN;
	}

	public static PayType valueOfB2CPayType(int v) {
		switch (v) {
			case 1:
				return ALIPAYAPP;
			case 2:
				return WXAPP;
			case 3:
				return UNWAP;
			case 4:
				return LIANLIAN;
			case 5:
				return OPENTRADE;
		}
		return UNKOWN;
	}

	public static String asReadableName(Integer value) {
		if (value == null) {
			return "未知";
		}

		PayType type = valueOf(value);
		if (type == UNKOWN) {
			return "未知类型：" + value;
		}

		return type.readableName();
	}

	public static FundInRequest findFundInChannel(int type) {
		FundInRequest request = new FundInRequest();
		switch (type) {
			case 1:
			case 6:
				request.setInstCode(InstCodeEnum.支付宝);
				request.setChannelCode(ChannelCodeEnum.IOS);
				break;
			case 2:
			case 7:
				request.setInstCode(InstCodeEnum.支付宝);
				request.setChannelCode(ChannelCodeEnum.WAP);
				break;
			case 3:
			case 8:
				request.setInstCode(InstCodeEnum.银联);
				request.setChannelCode(ChannelCodeEnum.IOS);
				break;
			case 4:
			case 9:
				request.setInstCode(InstCodeEnum.银联);
				request.setChannelCode(ChannelCodeEnum.WAP);
				break;
			case 5:
				request.setInstCode("BALANCE");
				break;
			case 13:
			case 15:
				request.setInstCode(InstCodeEnum.连连科技);
				request.setChannelCode(ChannelCodeEnum.WAP);
				break;
			case 10:
			case 11:
				request.setInstCode(InstCodeEnum.微信);
				request.setChannelCode(ChannelCodeEnum.IOS);
				break;
			case 12:
			case 14:
				request.setInstCode(InstCodeEnum.连连科技);
				request.setChannelCode(ChannelCodeEnum.IOS);
				break;
			case 16:
			case 17:
				request.setInstCode(InstCodeEnum.阿里百川 );
				request.setChannelCode(ChannelCodeEnum.IOS);
				break;
			case 18:
				request.setInstCode(InstCodeEnum.微信);
				request.setChannelCode(ChannelCodeEnum.WAP);
		}
		return request;
	}
}