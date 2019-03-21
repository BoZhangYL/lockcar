package lockcar;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class AN_Lock {
	public static void main(String[] arg) {
		/*
		 * JFrame jf = new JFrame("8F40"); Container conn = jf.getContentPane(); //
		 * 得到窗口的容器 JLabel L1 = new JLabel("Hello,world!"); // 创建一个标签 并设置初始内容
		 * conn.add(L1); jf.setBounds(200, 200, 300, 200); // 设置窗口的属性 窗口位置以及窗口的大小
		 * jf.setVisible(true);// 设置窗口可见 // jf.setDefaultCloseOperation(); //设置关闭方式
		 * 如果不设置的话 似乎关闭窗口之后不会退出程序
		 */ String Rtext = "7E0F40000501827400000800936703920001D17E";
		Map<String, String> maps = new HashMap<>();
		maps = analyshead(Rtext);
		Set<String> set = maps.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String key = it.next();
			System.out.printf(key + ":" + maps.get(key) + "\n");
		}

	}

	public static Map<String, String> analyshead(String R8F40) {
		int[] R8f40 = { 2, 4, 4, 12, 4, 2, 2 };
		Map<String, String> map = new HashMap<>();
		int i = 0;
		map.put("标识位", R8F40.substring(i, i = +i + R8f40[0]));
		String id = R8F40.substring(i, i = +i + R8f40[1]);
		map.put("消息 ID", id);
		map.put("消息体属性", R8F40.substring(i, i = +i + R8f40[2]));
		map.put("终端手机号", R8F40.substring(i, i = +i + R8f40[3]));
		map.put("消息流水号", R8F40.substring(i,i=+i+R8f40[4]));

		map.put("校验码", R8F40.substring(R8F40.length() - 1 - 2 - 2, R8F40.length() - 1 - 2));
		map.put("标识位", R8F40.substring(R8F40.length() - 1 - 2, R8F40.length() - 1));
		String body = R8F40.substring(i, R8F40.length() - 1 - 2 - 2);
		if (id.equals("8F40")) {
			int j = 0;
			map.put("锁车协议类", body.substring(j, j += 1));
			String subcommand = body.substring(j, j += 1);
			map.put("子命令", subcommand);
			if (subcommand.equals("1") || subcommand.equals("2")) {
				String stats = body.substring(j, j += 4);
				if (stats.equals("15F4")) {
					map.put("锁车功能激活关闭", "激活");
				} else if (stats.equals("4F51")) {
					map.put("锁车功能激活关闭", "激活关闭");
				}

				map.put(" GPSID ", body.substring(j, j += 3));
				map.put("固定密钥", body.substring(j, j += 3));
			} else if (subcommand.equals("3") || subcommand.equals("4")) {
				map.put("限制转速", body.substring(j, j += 4));
				map.put(" GPSID ", body.substring(j, j += 3));
				map.put("固定密钥", body.substring(j, j += 3));
				if (j != body.length()) {
					map.put("锁车参数", body.substring(j, j += 4));
				}
			}
		}else if(id.equals("0F40")) {
			int j=0;
			map.put("应答流水号 ", body.substring(j, j += 4));
			map.put("执行结果 ", body.substring(j, j += 2));
			String lock_type =body.substring(j, j += 2);
			if(lock_type.equals("01")) {
				lock_type = "1. 青气 MD5";
			}else if(lock_type.equals("02")) {
				lock_type = "2.青气 TSC1 锁车";
			} 
			else if(lock_type.equals("03")) {
				lock_type = "3.MD5 锁车 1";
			} 
			else if(lock_type.equals("04")) {
				lock_type = "4.MD5 锁车 2";
			} 
			map.put("锁车协议类型 ", lock_type);
			map.put("响应命令 ", body.substring(j, j += 2));
		}else {
			System.out.printf("不支持协议类型");
		}
		return map;
	}
}
