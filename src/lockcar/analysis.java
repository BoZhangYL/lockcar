package lockcar;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JScrollBar;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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

	public static Map<String, String> analyshead(String R8F40) {
		int[] R8f40 = { 2, 4, 4, 12, 4, 2, 2 };
		Map<String, String> map = new HashMap<>();
		int i = 0;
		//System.out.println(R8F40);
		
		R8F40 = R8F40.replaceAll("7D02", "7E");
		//System.out.println(R8F40);
		R8F40 = R8F40.replaceAll("7D01", "7D");
		//System.out.println(R8F40);
		map.put("标识位", R8F40.substring(i, i = +i + R8f40[0]));
		String id = R8F40.substring(i, i = +i + R8f40[1]);
		map.put("消息 ID", id);
		map.put("消息体属性", R8F40.substring(i, i = +i + R8f40[2]));
		map.put("终端手机号", R8F40.substring(i, i = +i + R8f40[3]));
		map.put("消息流水号", R8F40.substring(i, i = +i + R8f40[4]));

		map.put("校验码", R8F40.substring(R8F40.length() - 2 - 2, R8F40.length() - 2));
		String body = R8F40.substring(i, R8F40.length() - 4);
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
		} else {
			System.out.printf("不支持协议类型");
		}
		return map;
	}

	public static String subCommands(String rs) {
		String RS = null;
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
		String RS = null;
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
		String RS = null;
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
		String RS = null;
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
		frame = new JFrame();
		frame.setBounds(100, 100, 798, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, 13, 749, 516);
		frame.getContentPane().add(panel_1);

		JLabel lblNewLabel = new JLabel("请输入协议内容：");

		InArea = new JTextArea();

		JButton btnNewButton = new JButton("解析");
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

		OutArea = new JTextArea();

		JLabel lblNewLabel_1 = new JLabel("协议内容解析如下：");
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_1
				.createSequentialGroup()
				.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_1.createSequentialGroup()
						.addGap(24)
						.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addComponent(OutArea, GroupLayout.PREFERRED_SIZE, 675, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panel_1.createSequentialGroup()
										.addComponent(InArea, GroupLayout.PREFERRED_SIZE, 578,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18).addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 66,
												GroupLayout.PREFERRED_SIZE))))
						.addGroup(gl_panel_1.createSequentialGroup().addContainerGap().addComponent(lblNewLabel))
						.addGroup(gl_panel_1.createSequentialGroup().addContainerGap().addComponent(lblNewLabel_1)))
				.addContainerGap(50, Short.MAX_VALUE)));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup().addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup().addContainerGap().addComponent(lblNewLabel)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(InArea, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_1.createSequentialGroup().addGap(66).addComponent(btnNewButton))).addGap(25)
						.addComponent(lblNewLabel_1).addGap(18)
						.addComponent(OutArea, GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE).addContainerGap()));
		panel_1.setLayout(gl_panel_1);
	}
}
