package lockcar;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class analysis {

	private JFrame frame;
	private JTextArea InArea;
	private JTextArea OutArea;
	private static analysis window;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				try {
					window = new analysis();
					window.frame.setResizable(false);
					window.frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static Map<String, String> analyshead(String RMESSAGE) {
		int[] R8f40 = { 2, 4, 4, 12, 4, 2, 2 };
		Map<String, String> map = new HashMap<>();
		int i = 0;
		// System.out.println(R8F40);

		RMESSAGE = RMESSAGE.replaceAll("7D02", "7E");
		// System.out.println(R8F40);
		RMESSAGE = RMESSAGE.replaceAll("7D01", "7D");
		// System.out.println(R8F40);
		map.put("标识位", RMESSAGE.substring(i, i = +i + R8f40[0]));
		String id = RMESSAGE.substring(i, i = +i + R8f40[1]);
		map.put("消息 ID", id);
		map.put("消息体属性", RMESSAGE.substring(i, i = +i + R8f40[2]));
		map.put("终端手机号", RMESSAGE.substring(i, i = +i + R8f40[3]));
		map.put("消息流水号", RMESSAGE.substring(i, i = +i + R8f40[4]));
		map.put("校验码", RMESSAGE.substring(RMESSAGE.length() - 2 - 2, RMESSAGE.length() - 2));
		String body = RMESSAGE.substring(i, RMESSAGE.length() - 4);
		// System.out.println(body);
		if (id.equals("8F40")) {
			int j = 0;
			String lock_type = body.substring(j, j += 2);

			map.put("锁车协议类型", lockTypes(lock_type) + ", " + lock_type);
			String subcommand = body.substring(j, j += 2);
			if (subcommand.equals("01") || subcommand.equals("02")) {
				map.put("子命令", subCommands(subcommand));
				String stats = body.substring(j, j += 4);
				if (stats.equals("15F4")) {
					map.put("锁车功能", "激活");
				} else if (stats.equals("4F51")) {
					map.put("锁车功能", "关闭激活");
				}
				map.put(" GPSID ", body.substring(j, j += 6));
				map.put("固定密钥", body.substring(j, j += 6));
			} else if (subcommand.equals("03") || subcommand.equals("04")) {
				map.put("子命令", subCommands(subcommand));
				map.put("限制转速", body.substring(j, j += 4));
				map.put("  Reserved  ", body.substring(j, j += 6));
				map.put(" GPSID ", body.substring(j, j += 6));
				if (j != body.length()) {
					map.put("锁车参数", body.substring(j, j += 4));
				}
			} else if (subcommand.equals("10")) {
				map.put("子命令", subCommands(subcommand));
			}
		} else if (id.equals("0F40")) {
			int j = 0;
			map.put("应答流水号 ", body.substring(j, j += 4));
			String execResult = body.substring(j, j += 2);
			map.put("执行结果 ", execResults(execResult) + ", " + execResult);
			String lock_type = body.substring(j, j += 2);
			map.put("锁车协议类型 ", lockTypes(lock_type) + ", " + lock_type);
			String responsecommand = body.substring(j, j += 2);
			map.put("响应命令 ", responseCommands(responsecommand));
		} else if (id.equals("8F51")) {
			int j = 0;
			map.put("Version ", body.substring(j, j += 2));
			map.put("请求时间", body.substring(j, j += 12));
			String funs = body.substring(j, j += 2);
			map.put("功能个数", funs);
			for (int k = 0; k < Integer.valueOf(funs); k++) {
				String fun_no = body.substring(j, j += 2);
				map.put("功能编号" + k + 1, getCarStausFuncationList(fun_no));
			}

		} else if (id.equals("0F51")) {
			int j = 0;
			map.put("Version ", body.substring(j, j += 2));
			map.put("应答时间", body.substring(j, j += 12));
			String PackageTag = body.substring(j, j += 2);
			map.put("包标志 ", getPackageTag(PackageTag));
			map.put("应答流水号 ", body.substring(j, j += 4));
			String funs = body.substring(j, j += 2);
			map.put("功能个数", funs);
			for (int k = 0; k < Integer.valueOf(funs); k++) {
				String fun_no = body.substring(j, j += 2);
				map.put("功能编号" + k + 1, getCarStausFuncationList(fun_no));
				String fun_length = body.substring(j, j += 2);
				map.put("功能编号" + k + 1 + "结果长度", fun_length);
				String fun_content = body.substring(j, j += 2 * Integer.valueOf(fun_length));
				map.put("功能内容：", getFunConttent(fun_no, fun_content));
			}
		} else if (id.equals("8F41")) {
			int j = 0;
			map.put("Version ", body.substring(j, j += 2));
			map.put("请求时间", body.substring(j, j += 12));
			String funs = body.substring(j, j += 2);
			map.put("功能个数", funs);
			for (int k = 0; k < Integer.valueOf(funs); k++) {
				String fun_no = body.substring(j, j += 2);
				map.put("功能编号" + k + 1, getcontrolFun(fun_no));
				String FunCmd = body.substring(j, j += 2);
				map.put("功能指令 ", getcontrolcmd(fun_no, FunCmd));
				String FunItems = body.substring(j, j += 2);
				map.put("功能参数 ", getFunItems(fun_no, FunItems));
			}
		} else if (id.equals("0F41")) {
			int j = 0;
			map.put("Version ", body.substring(j, j += 2));
			map.put("应答时间", body.substring(j, j += 12));
			map.put("应答流水号 ", body.substring(j, j += 4));
			String funs = body.substring(j, j += 2);
			map.put("功能个数", funs);
			for (int k = 0; k < Integer.valueOf(funs); k++) {
				String fun_no = body.substring(j, j += 2);
				map.put("功能编号" + k + 1, getcontrolFun(fun_no));
				String FunCmd = body.substring(j, j += 2);
				map.put("功能指令 ", getcontrolcmd(fun_no, FunCmd));
				String EXCStauts = body.substring(j, j += 2);
				;
				map.put("执行状态 ", gettExcStatus(EXCStauts));

			}
		} else if (id.equals("0F37")) {
			int j = 0;
			int k = 1;
			while ((body.length()) - j > 32) {
				String GPSinfo = body.substring(j, j += 32);
				String data_no = body.substring(j, j += 2);
				String F37data = body.substring(j, j += 26);
				map.put("数据单元" + k++,
						"\n{\tGPS信息：" + getGPSInfo(GPSinfo) + "\n\t数据单元个数：" + data_no + "\n\t" + getF37Data(F37data));
			}

		} else if (id.equals("0F38")) {
			int j = 0;
			String GPSInfo = body.substring(j, j += 40);
			String StatisticsInfo =body.substring(j,j+124);
			map.put("GPS信息", getF38GPSinfo(GPSInfo));
			map.put("统计信息", getStaticticsInfo(StatisticsInfo));

		} else {
			map.put("不支持通讯协议报文:", id);
		}
		return map;
	}

	public static String getStaticticsInfo(String rs) {
		String RS = rs;
		String xingchengkaisshijian = rs.substring(0, 8);
		xingchengkaisshijian = TimeStamp2Date(deCode(xingchengkaisshijian)+"");

		String xingchengjieshushijian = rs.substring(8, 16);
		xingchengjieshushijian = TimeStamp2Date(deCode(xingchengjieshushijian)+"");

		String jiashixunhuanzonglicheng = rs.substring(16, 24);
		jiashixunhuanzonglicheng = deCode(jiashixunhuanzonglicheng) + "m";

		String kongdanghuaxinglicheng = rs.substring(24, 32);
		kongdanghuaxinglicheng = deCode(kongdanghuaxinglicheng) + "m";

		String zaidanghuaxinglicheng = rs.substring(32, 40);
		zaidanghuaxinglicheng = deCode(zaidanghuaxinglicheng) + "m";

		String tingchedaisushijian = rs.substring(40, 48);
		tingchedaisushijian = deCode(tingchedaisushijian) + "s";

		String zhidongchishu = rs.substring(48, 52);
		zhidongchishu = deCode(zhidongchishu) + "";

		String zhidongleijilicheng = rs.substring(52, 60);
		zhidongleijilicheng = deCode(zhidongleijilicheng) + "m";

		String zhidongshichang = rs.substring(60, 68);
		zhidongshichang = deCode(zhidongshichang) + "s";

		String jijiasuchishu = rs.substring(68, 72);
		jijiasuchishu = deCode(jijiasuchishu) + "";

		String jijiansuchishu = rs.substring(72, 76);
		jijiansuchishu = deCode(jijiansuchishu) + "";

		String zhengchegusuanzaihe = rs.substring(76, 84);
		zhengchegusuanzaihe = deCode(zhengchegusuanzaihe) + "kg";

		String qishiyouhao = rs.substring(84, 92);
		qishiyouhao = deCode(qishiyouhao) + "L";

		String zhongzhiyouhao = rs.substring(92, 100);
		zhongzhiyouhao = deCode(zhongzhiyouhao) + "L";

		String xunahnglicheng = rs.substring(100, 108);
		xunahnglicheng = deCode(xunahnglicheng) + "m";

		String pinjunchesu = rs.substring(108, 116);
		pinjunchesu = deCode(pinjunchesu) + "s";

		String chaosuxingshichishu = rs.substring(116, 120);
		chaosuxingshichishu = deCode(chaosuxingshichishu) + "";

		String jizhuanwanchishu = rs.substring(120, 124);
		jizhuanwanchishu = deCode(jizhuanwanchishu) + "";

		RS = "\n\t行程开始时间：" + xingchengkaisshijian + "\n\t行程结束时间：" + xingchengjieshushijian + "\n\t驾驶循环总里程："
				+ jiashixunhuanzonglicheng + "\n\t空档滑行里程：" + kongdanghuaxinglicheng + "\n\t在档滑行里程："
				+ zaidanghuaxinglicheng + "\n\t停车怠速时间：" + tingchedaisushijian + "\n\t制动次数：" + zhidongchishu
				+ "\n\t制动累计里程：" + zhidongleijilicheng + "\n\t制动时长：" + zhidongshichang + "\n\t急加速次数：" + jijiasuchishu
				+ "\n\t急减速次数：" + jijiansuchishu + "\n\t整车估算载荷：" + zhengchegusuanzaihe + "\n\t起始油耗：" + qishiyouhao
				+ "\n\t终止油耗：" + zhongzhiyouhao + "\n\t巡航里程：" + xunahnglicheng + "\n\t平均车速行：" + pinjunchesu
				+ "\n\t超速行驶次数：" + chaosuxingshichishu + "\n\t急转弯次数：" + jizhuanwanchishu;

		return RS;
	}

	// Convert Unix timestamp to normal date style
	public static String TimeStamp2Date(String timestampString) {
		Long timestamp = Long.parseLong(timestampString) * 1000;
		String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(timestamp));
		return date;
	}

	public static String getF38GPSinfo(String rs) {
		String RS = rs;
		String time = rs.substring(0, 12);
		String Latitude = (Integer.valueOf(deCode(rs.substring(12, 20))) * 0.000001) + "°";
		String longitude = (Integer.valueOf(deCode(rs.substring(20, 28))) * 0.000001) + "°";
		String high = deCode(rs.substring(28, 32)) + "m";
		String orientation = deCode(rs.substring(32, 36)) + "°";
		String speed = (deCode(rs.substring(36, 40)) / 10) + "km/h";

		RS = "\n\t时间：" + time + "\n\t纬度：" + Latitude + "\n\t经度：" + longitude + "\n\t高度：" + high + "\n\t方向："
				+ orientation + "\n\t速度：0" + speed;
		return RS;
	}

	public static String getF37Data(String rs) {
		String RS = rs;
		String fadongjiniuju = rs.substring(0, 2);
		fadongjiniuju = (deCode(fadongjiniuju) - 125) + "%";

		String zonghechesu = rs.substring(2, 6);
		zonghechesu = (deCode(zonghechesu) / 256) + "km/h";

		String youmen = rs.substring(6, 8);
		youmen = (deCode(youmen) * 0.4) + "%";

		String zhidongkaiguan = rs.substring(8, 10);
		switch (zhidongkaiguan) {
		case "00":
			zhidongkaiguan = "制动踏板被松开";
			break;
		case "01":
			zhidongkaiguan = "制动踏板被踩下";
			break;
		case "10":
			zhidongkaiguan = "出错";
			break;
		case "11":
			zhidongkaiguan = "不可";
			break;
		}

		String fadongjizhuansu = rs.substring(10, 14);
		fadongjizhuansu = (deCode(fadongjizhuansu) * 0.125) + "rpm";

		String dangwei = rs.substring(14, 16);
		dangwei = (deCode(dangwei) - 125) + "";

		String liheqikaiguan = rs.substring(16, 18);
		switch (liheqikaiguan) {
		case "00":
			liheqikaiguan = "离合器踏板被松开";
			break;
		case "01":
			liheqikaiguan = "离合器踏板被踩下";
			break;
		case "10":
			liheqikaiguan = "出错";
			break;
		case "11":
			liheqikaiguan = "不可";
			break;
		}

		String fadongjisunshiyouhao = rs.substring(18, 22);
		fadongjisunshiyouhao = (deCode(fadongjisunshiyouhao) / 512) + "km/L";

		String fadongjiranyouxiaohanlv = rs.substring(22, 26);
		fadongjiranyouxiaohanlv = (deCode(fadongjiranyouxiaohanlv) * 0.05) + "L/h";
		RS = "\n\t实时数据：\n\t发动机输出扭矩:" + fadongjiniuju + "\n\t\\t综合车速:" + zonghechesu + "\n\t\\t油门:" + youmen
				+ "\n\t\\t制动开关:" + zhidongkaiguan + "\n\t\\t发动机转速:" + fadongjizhuansu + "\n\t\\t挡位:" + dangwei
				+ "\n\t\\t离合器开关:" + liheqikaiguan + "\n\t\\t发动机瞬时油耗:" + fadongjisunshiyouhao + "\n\t\\t发动机燃油消耗率:"
				+ fadongjiranyouxiaohanlv + "\n}";
		return RS;
	}

	public static int deCode(String rs) {
		return Integer.decode("0x" + rs);
	}

	public static String getGPSInfo(String rs) {
		String RS = rs;
		String time = rs.substring(0, 12);
		String Latitude = (Integer.valueOf(deCode(rs.substring(12, 20))) * 0.000001) + "°";
		String longitude = (Integer.valueOf(deCode(rs.substring(20, 28))) * 0.000001) + "°";
		String high = deCode(rs.substring(28, 32)) + "m";
		RS = "\n\t时间：" + time + "\n\t纬度：" + Latitude + "\n\t经度：" + longitude + "\n\t高度" + high;
		return RS;
	}

	public static String gettExcStatus(String rs) {
		String RS = rs;
		switch (rs) {
		case "00":
			RS = "执行成功";
			break;
		case "01":
			RS = "执行失败";
			break;
		case "02":
			RS = "执行超时";
			break;
		}
		return RS;
	}

	public static String getFunItems(String fun_no, String rs) {
		String RS = rs;
		switch (fun_no) {
		case "04":
			RS = "请求设置百分比" + rs;
			break;
		case "23":
			RS = "设置温度" + rs;
			break;
		}
		return RS;
	}

	public static String getcontrolcmd(String fun_no, String rs) {
		String RS = rs;
		switch (fun_no) {
		case "01":
			switch (rs) {
			case "00":
				RS = "主驾驶门解锁";
				break;
			case "10":
				RS = "主驾驶门上锁";
				break;
			case "01":
				RS = "副驾驶门解锁";
				break;
			case "11":
				RS = "副驾驶门上锁";
				break;
			case "02":
				RS = "主后门解锁";
				break;
			case "12":
				RS = "主后门上锁";
				break;
			case "03":
				RS = "副后门解锁";
				break;
			case "13":
				RS = "副后门上锁";
				break;
			case "04":
				RS = "尾门解锁";
				break;
			case "14":
				RS = "尾门上锁";
				break;
			case "09":
				RS = "中控上锁";
				break;
			case "19":
				RS = "中控解锁";
				break;
			}
			break;
		case "04":
			switch (rs) {
			case "01":
				RS = "主驾驶窗";
				break;
			case "02":
				RS = "副驾驶窗";
				break;
			case "03":
				RS = "主后驾驶窗";
				break;
			case "04":
				RS = "副后驾驶";
				break;
			}
			break;
		case "23":
			switch (rs) {
			case "01":
				RS = "开启热车";
				break;
			case "00":
				RS = "关闭热车";
				break;

			}
			break;
		}
		return RS;
	}

	public static String getcontrolFun(String rs) {
		String RS = rs;
		switch (rs) {
		case "00":
			RS = "N/A，保留";
			break;
		case "01":
			RS = "车门控制";
			break;
		case "03":
			RS = "双闪控制";
			break;
		case "04":
			RS = "车窗升降控制";
			break;
		case "06":
			RS = "天窗控制";
			break;
		case "08":
			RS = "灯光控制";
			break;
		case "0A":
			RS = "保留";
			break;
		case "0C":
			RS = "保留";
			break;
		case "0E":
			RS = "发动机启动/停止";
			break;
		case "10":
			RS = "车辆电源上电/下电";
			break;
		case "12":
			RS = "WIFI 打开/关闭";
			break;
		case "15":
			RS = "空调打开/关闭";
			break;
		case "16":
			RS = "空调车窗除霜功能打开/关闭";
			break;
		case "17":
			RS = "空调后视镜除霜功能打开/关闭";
			break;
		case "18":
			RS = "空调温度设置";
			break;
		case "19":
			RS = "雨刮控制";
			break;
		case "20":
			RS = "后视镜控制";
			break;
		case "23":
			RS = "开启/关闭水暖热车";
			break;

		}
		return RS;
	}

	public static String getFunConttent(String fun_no, String rs) {
		String RS = rs;
		switch (fun_no) {
		case "01":
			RS = rs;
			break;
		case "05":
			RS = rs;
			break;
		case "23":
			String CurrentWorkStayus = getcathotstatus(rs.substring(0, 2));
			String currentTemperture = rs.substring(2, 4);
			String error_no = rs.substring(4, 6);
			if (Integer.valueOf(error_no) > 0) {
				String[] errors = new String[Integer.valueOf(error_no)];
				for (int i = 0; i < Integer.valueOf(error_no); i++) {
					errors[i] = rs.substring(6 + i * 4, 8 + i * 4);
				}
				RS = RS + "当前工作状态：" + CurrentWorkStayus + "\n" + "当前水温：" + currentTemperture + "\n" + "当前故障：\n";
				for (int i = 0; i < Integer.valueOf(error_no); i++) {
					RS = RS + "故障" + i + 1 + ":" + errors[i];
				}
			} else {
				RS = "当前工作状态：" + CurrentWorkStayus + "\n" + "当前水温：" + currentTemperture + "\n" + "当前故障：无故障";
			}

			break;
		case "24":
			RS = getwaketype(rs);
			break;
		default:
			RS = "不支持功能内容";

		}
		return RS;
	}

	public static String getwaketype(String rs) {
		String RS = rs;
		switch (rs) {
		case "00":
			RS = "异常类型";
			break;
		case "01":
			RS = "定时唤醒事件";
			break;
		case "02":
			RS = "Gsensor唤醒事件";
			break;
		case "03":
			RS = "can信号唤醒事件";
			break;
		case "04":
			RS = "短信唤醒事";
			break;

		}
		return RS;
	}

	public static String getcathotstatus(String rs) {
		String RS = rs;
		switch (rs) {
		case "00":
			RS = "水暖设备空闲";
			break;
		case "01":
			RS = "水暖设备加热中";
			break;
		case "02":
			RS = "水暖设备关闭中";
			break;
		case "03":
			RS = "水暖设备待机中";
			break;
		case "10":
			RS = "车辆不支持热车功";
			break;

		}
		return RS;
	}

	public static String getPackageTag(String rs) {
		String RS = rs;
		switch (rs) {
		case "00":
			RS = "获取车辆状态应答";
			break;
		case "01":
			RS = "主动时间上报，平台不关心请求流水号";
			break;
		}
		return RS;
	}

	public static String getCarStausFuncationList(String rs) {
		String RS = rs;
		switch (rs) {
		case "00":
			RS = "N/A，保留";
			break;
		case "01":
			RS = "获取车门状态";
			break;
		case "05":
			RS = "获取车窗状态";
			break;
		case "07":
			RS = "获取天窗状态";
			break;
		case "09":
			RS = "获取灯光状态";
			break;
		case "0B":
			RS = "获取金融锁车状态";
			break;
		case "0D":
			RS = "获取销贷激活状态";
			break;
		case "0F":
			RS = "获取发动机状态";
			break;
		case "11":
			RS = "获取车辆电源状态";
			break;
		case "13":
			RS = "获取WIFI开关状态";
			break;
		case "14":
			RS = "获取空调状态";
			break;
		case "23":
			RS = "获取/报告当前水暖热车情况";
			break;
		case "24":
			RS = "终端唤醒后报告平台唤醒情况";
			break;

		}
		return RS;
	}

	public static String subCommands(String rs) {
		String RS = rs;
		switch (rs) {
		case "01":
			rs = "激活";
			break;
		case "02":
			rs = "失活";
			break;
		case "03":
			rs = "锁车";
			break;
		case "04":
			rs = "解锁";
			break;
		case "10":
			rs = "查询锁车状态";
			break;
		}
		RS = rs;
		return RS;
	}

	public static String lockTypes(String lock_type) {
		String RS = lock_type;
		if (lock_type.equals("01")) {
			lock_type = "1. 青气 MD5";
		} else if (lock_type.equals("02")) {
			lock_type = "2.青气 TSC1 锁车";
		} else if (lock_type.equals("03")) {
			lock_type = "3.MD5 锁车 1";
		} else if (lock_type.equals("04")) {
			lock_type = "4.MD5 锁车 2";
		} else if (lock_type.equals("00")) {
			lock_type = "未知协议，默认不填充";
		}
		RS = lock_type;
		return RS;
	}

	public static String responseCommands(String rs) {
		String RS = rs;
		switch (rs) {
		case "01":
			rs = "激活响应";
			break;
		case "02":
			rs = "失活响应";
			break;
		case "03":
			rs = "锁车响应";
			break;
		case "04":
			rs = "解锁响应";
			break;
		}
		RS = rs;
		return RS;
	}

	public static String execResults(String rs) {
		String RS = rs;
		switch (rs) {
		case "51":
			rs = "未学习；";
			break;
		case "52":
			rs = "协议学习中；";
			break;
		case "53":
			rs = "协议学习失败；";
			break;
		case "54":
			rs = "协议学习成功；";
			break;
		case "55":
			rs = "协议不支持；";
			break;
		case "56":
			rs = "未配置锁车协议；";
			break;
		case "57":
			rs = "配置协议中；";
			break;
		case "58":
			rs = "配置协议失败；";
			break;
		case "59":
			rs = "配置协议成功；";
			break;
		case "5A":
			rs = "未激活；";
			break;
		case "5B":
			rs = "激活中；";
			break;
		case "5C":
			rs = "激活失败；";
			break;
		case "5D":
			rs = "激活成功；";
			break;
		case "5E":
			rs = "握手中";
			break;
		case "5F":
			rs = "握手失败；";
			break;
		case "60":
			rs = "握手成功；";
			break;
		case "61":
			rs = "没有锁车设置命令；";
			break;
		case "62":
			rs = "锁车设置执行中；";
			break;
		case "63":
			rs = "锁车设置失败；";
			break;
		case "64":
			rs = "锁车设置成功；";
			break;
		case "65":
			rs = "锁车中";
			break;
		case "66":
			rs = "锁车失败；";
			break;
		case "67":
			rs = "锁车成功；";
			break;
		case "68":
			rs = "没有解锁设置命令；";
			break;
		case "69":
			rs = "解锁设置执行中；";
			break;
		case "6A":
			rs = "解锁设置失败；";
			break;
		case "6B":
			rs = "解锁设置成功；";
			break;
		case "6C":
			rs = "解锁中";
			break;
		case "6D":
			rs = "解锁失败；";
			break;
		case "6E":
			rs = "解锁成功；";
			break;
		case "6F":
			rs = "没有关闭锁车指令；";
			break;
		case "70":
			rs = "关闭锁车执行中；";
			break;
		case "71":
			rs = "关闭锁车失败；";
			break;
		case "72":
			rs = "关闭锁车成功";
			break;
		case "01":
			rs = "无通道号";
			break;
		case "02":
			rs = "配置协议成功";
			break;
		case "03":
			rs = "配置指令成功";
			break;
		case "04":
			rs = "配置协议失败(协议不支持)";
			break;
		case "05":
			rs = "配置指令失败";
			break;
		case "06":
			rs = "激活参数配置成功";
			break;
		case "07":
			rs = "配置指令错误";
			break;
		case "91":
			rs = "配置指令无效：无锁车协议，需先配置协议";
			break;
		case "92":
			rs = "配置激活指令无效：激活指令执行中 ";
			break;
		case "93":
			rs = "配置激活指令无效：激活指令执行已成功";
			break;
		case "94":
			rs = "配置锁车指令无效：未激活，需先激活";
			break;
		case "95":
			rs = "配置锁车指令无效：锁车指令执行中";
			break;
		case "96":
			rs = "配置锁车指令无效：锁车设置中";
			break;
		case "97":
			rs = "配置锁车指令无效：锁车设置成功";
			break;
		case "98":
			rs = "配置锁车指令无效：锁车指令执行已成功";
			break;
		case "99":
			rs = "配置解锁命令无效：车辆还未锁车";
			break;
		case "9A":
			rs = "配置解锁指令无效：解锁指令执行中 ";
			break;
		case "9B":
			rs = "配置解锁指令无效：解锁设置中";
			break;
		case "9C":
			rs = "配置解锁指令无效：解锁设置成功";
			break;
		case "9D":
			rs = "配置解锁指令无效：解锁指令执行已成功";
			break;
		case "9E":
			rs = "配置关闭指令无效：未激活";
			break;
		case "9F":
			rs = "配置关闭指令无效：关闭指令执行中 ";
			break;
		case "A0":
			rs = "配置关闭指令无效：关闭指令执行已成功";
			break;
		case "A1":
			rs = "配置关闭指令无效：锁车指令设置中，请等待";
			break;
		case "A2":
			rs = "配置关闭指令无效：锁车指令设置成功，请等待";
			break;
		case "A3":
			rs = "配置关闭指令无效：锁车指令执行中，请等待";
			break;
		case "A4":
			rs = "配置关闭指令无效：锁车指令已执行成功，请解锁";
			break;
		case "A5":
			rs = "配置关闭指令无效：解锁指令设置中，等待解锁";
			break;
		case "A6":
			rs = "配置关闭指令无效：解锁指令设置失败，等待解锁";
			break;
		case "A7":
			rs = "配置关闭指令无效：解锁指令设置成功，等待解锁";
			break;
		case "A8":
			rs = "配置关闭指令无效：解锁指令执行中，等待解锁";
			break;
		case "A9":
			rs = "配置关闭指令无效：解锁指令已执行失败";
			break;
		case "AA":
			rs = "不执行操作：GPSID不匹";
			break;

		}
		RS = rs;
		return RS;
	}

	/**
	 * Create the application.
	 */
	public analysis() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("支持解析8F40/8F41/8F51/0F40/0F41/0F51/0F37/0F38");
		frame.setBounds(100, 100, 798, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, 13, 749, 516);
		frame.getContentPane().add(panel_1);

		JLabel lblNewLabel = new JLabel("请输入协议内容：");

		JButton btnNewButton = new JButton("解析");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (InArea.getText() != null) {
					String Rtext = InArea.getText();
					Map<String, String> maps = new HashMap<>();
					maps = analyshead(Rtext);
					Set<String> set = maps.keySet();
					Iterator<String> it = set.iterator();
					String out = "";
					while (it.hasNext()) {
						String key = it.next();
						out = out + key + ":" + maps.get(key) + "\n";
					}
					OutArea.setText(out);
					window.frame.setVisible(true);
				} else {
					OutArea.setText("请输入协议内容!");
				}

			}
		});

		JLabel lblNewLabel_1 = new JLabel("协议内容解析如下：");

		JScrollPane scrollPane = new JScrollPane();

		JScrollPane scrollPane_1 = new JScrollPane();
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel_1
				.createSequentialGroup().addContainerGap(54, Short.MAX_VALUE)
				.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel_1.createSequentialGroup()
								.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
										.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 611,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lblNewLabel))
								.addGap(98))
						.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addComponent(lblNewLabel_1)
								.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
										.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 66,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 707,
												GroupLayout.PREFERRED_SIZE))))));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_1
				.createSequentialGroup().addGap(18).addComponent(lblNewLabel)
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING, false)
						.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE))
				.addPreferredGap(ComponentPlacement.RELATED, 39, Short.MAX_VALUE).addComponent(lblNewLabel_1)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 282, GroupLayout.PREFERRED_SIZE)));

		InArea = new JTextArea();
		scrollPane_1.setViewportView(InArea);
		InArea.setLineWrap(true);

		OutArea = new JTextArea();
		OutArea.setLineWrap(true);
		scrollPane.setViewportView(OutArea);
		panel_1.setLayout(gl_panel_1);
	}
}
