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

	public static Map<String, String> analyshead(String RMESSAGE) {
		int[] R8f40 = { 2, 4, 4, 12, 4, 2, 2 };
		Map<String, String> map = new HashMap<>();
		int i = 0;
		// System.out.println(R8F40);

		RMESSAGE = RMESSAGE.replaceAll("7D02", "7E");
		// System.out.println(R8F40);
		RMESSAGE = RMESSAGE.replaceAll("7D01", "7D");
		// System.out.println(R8F40);
		map.put("��ʶλ", RMESSAGE.substring(i, i = +i + R8f40[0]));
		String id = RMESSAGE.substring(i, i = +i + R8f40[1]);
		map.put("��Ϣ ID", id);
		map.put("��Ϣ������", RMESSAGE.substring(i, i = +i + R8f40[2]));
		map.put("�ն��ֻ���", RMESSAGE.substring(i, i = +i + R8f40[3]));
		map.put("��Ϣ��ˮ��", RMESSAGE.substring(i, i = +i + R8f40[4]));

		map.put("У����", RMESSAGE.substring(RMESSAGE.length() - 2 - 2, RMESSAGE.length() - 2));
		String body = RMESSAGE.substring(i, RMESSAGE.length() - 4);
		// System.out.println(body);
		if (id.equals("8F40")) {
			int j = 0;
			String lock_type = body.substring(j, j += 2);

			map.put("����Э������", lockTypes(lock_type) + ", " + lock_type);
			String subcommand = body.substring(j, j += 2);
			if (subcommand.equals("01") || subcommand.equals("02")) {
				map.put("������", subCommands(subcommand));
				String stats = body.substring(j, j += 4);
				if (stats.equals("15F4")) {
					map.put("��������", "����");
				} else if (stats.equals("4F51")) {
					map.put("��������", "�رռ���");
				}
				map.put(" GPSID ", body.substring(j, j += 6));
				map.put("�̶���Կ", body.substring(j, j += 6));
			} else if (subcommand.equals("03") || subcommand.equals("04")) {
				map.put("������", subCommands(subcommand));
				map.put("����ת��", body.substring(j, j += 4));
				map.put("  Reserved  ", body.substring(j, j += 6));
				map.put(" GPSID ", body.substring(j, j += 6));
				if (j != body.length()) {
					map.put("��������", body.substring(j, j += 4));
				}
			} else if (subcommand.equals("10")) {
				map.put("������", subCommands(subcommand));
			}
		} else if (id.equals("0F40")) {
			int j = 0;
			map.put("Ӧ����ˮ�� ", body.substring(j, j += 4));
			String execResult = body.substring(j, j += 2);
			map.put("ִ�н�� ", execResults(execResult) + ", " + execResult);
			String lock_type = body.substring(j, j += 2);
			map.put("����Э������ ", lockTypes(lock_type) + ", " + lock_type);
			String responsecommand = body.substring(j, j += 2);
			map.put("��Ӧ���� ", responseCommands(responsecommand));
		} else if (id.equals("8F51")) {
			int j = 0;
			map.put("Version ", body.substring(j, j += 2));
			map.put("����ʱ��", body.substring(j, j += 12));
			String funs = body.substring(j, j += 2);
			map.put("���ܸ���", funs);
			for (int k = 0; k < Integer.valueOf(funs); k++) {
				String fun_no = body.substring(j, j += 2);
				map.put("���ܱ��" + k + 1, getCarStausFuncationList(fun_no));
			}

		} else if (id.equals("0F51")) {
			int j = 0;
			map.put("Version ", body.substring(j, j += 2));
			map.put("Ӧ��ʱ��", body.substring(j, j += 12));
			String PackageTag = body.substring(j, j += 2);
			map.put("����־ ", getPackageTag(PackageTag));
			map.put("Ӧ����ˮ�� ", body.substring(j, j += 4));
			String funs = body.substring(j, j += 2);
			map.put("���ܸ���", funs);
			for (int k = 0; k < Integer.valueOf(funs); k++) {
				String fun_no = body.substring(j, j += 2);
				map.put("���ܱ��" + k + 1, getCarStausFuncationList(fun_no));
				String fun_length = body.substring(j, j += 2);
				map.put("���ܱ��" + k + 1 + "�������", fun_length);
				String fun_content = body.substring(j, j += 2 * Integer.valueOf(fun_length));
				map.put("�������ݣ�", getFunConttent(fun_no, fun_content));
			}
		} else if (id.equals("8F41")) {
			int j = 0;
			map.put("Version ", body.substring(j, j += 2));
			map.put("����ʱ��", body.substring(j, j += 12));
			String funs = body.substring(j, j += 2);
			map.put("���ܸ���", funs);
			for (int k = 0; k < Integer.valueOf(funs); k++) {
				String fun_no = body.substring(j, j += 2);
				map.put("���ܱ��" + k + 1, getcontrolFun(fun_no));
				String FunCmd = body.substring(j, j += 2);
				map.put("����ָ�� ", getcontrolcmd(fun_no, FunCmd));
				String FunItems = body.substring(j, j += 2);
				map.put("���ܲ��� ", getFunItems(fun_no, FunItems));
			}
		} else if (id.equals("0F41")) {
			int j = 0;
			map.put("Version ", body.substring(j, j += 2));
			map.put("Ӧ��ʱ��", body.substring(j, j += 12));
			map.put("Ӧ����ˮ�� ", body.substring(j, j += 4));
			String funs = body.substring(j, j += 2);
			map.put("���ܸ���", funs);
			for (int k = 0; k < Integer.valueOf(funs); k++) {
				String fun_no = body.substring(j, j += 2);
				map.put("���ܱ��" + k + 1, getcontrolFun(fun_no));
				String FunCmd = body.substring(j, j += 2);
				map.put("����ָ�� ", getcontrolcmd(fun_no, FunCmd));
				String EXCStauts = body.substring(j, j += 2);
				;
				map.put("ִ��״̬ ", gettExcStatus(EXCStauts));

			}
		} else {
			map.put("��֧��ͨѶЭ�鱨��:", id);
		}
		return map;
	}

	public static String gettExcStatus(String rs) {
		String RS = rs;
		switch (rs) {
		case "00":
			RS = "ִ�гɹ�";
			break;
		case "01":
			RS = "ִ��ʧ��";
			break;
		case "02":
			RS = "ִ�г�ʱ";
			break;
		}
		return RS;
	}

	public static String getFunItems(String fun_no, String rs) {
		String RS = rs;
		switch (fun_no) {
		case "04":
			RS = "�������ðٷֱ�" + rs;
			break;
		case "23":
			RS = "�����¶�" + rs;
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
				RS = "����ʻ�Ž���";
				break;
			case "10":
				RS = "����ʻ������";
				break;
			case "01":
				RS = "����ʻ�Ž���";
				break;
			case "11":
				RS = "����ʻ������";
				break;
			case "02":
				RS = "�����Ž���";
				break;
			case "12":
				RS = "����������";
				break;
			case "03":
				RS = "�����Ž���";
				break;
			case "13":
				RS = "����������";
				break;
			case "04":
				RS = "β�Ž���";
				break;
			case "14":
				RS = "β������";
				break;
			case "09":
				RS = "�п�����";
				break;
			case "19":
				RS = "�пؽ���";
				break;
			}
			break;
		case "04":
			switch (rs) {
			case "01":
				RS = "����ʻ��";
				break;
			case "02":
				RS = "����ʻ��";
				break;
			case "03":
				RS = "�����ʻ��";
				break;
			case "04":
				RS = "�����ʻ";
				break;
			}
			break;
		case "23":
			switch (rs) {
			case "01":
				RS = "�����ȳ�";
				break;
			case "00":
				RS = "�ر��ȳ�";
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
			RS = "N/A������";
			break;
		case "01":
			RS = "���ſ���";
			break;
		case "03":
			RS = "˫������";
			break;
		case "04":
			RS = "������������";
			break;
		case "06":
			RS = "�촰����";
			break;
		case "08":
			RS = "�ƹ����";
			break;
		case "0A":
			RS = "����";
			break;
		case "0C":
			RS = "����";
			break;
		case "0E":
			RS = "����������/ֹͣ";
			break;
		case "10":
			RS = "������Դ�ϵ�/�µ�";
			break;
		case "12":
			RS = "WIFI ��/�ر�";
			break;
		case "15":
			RS = "�յ���/�ر�";
			break;
		case "16":
			RS = "�յ�������˪���ܴ�/�ر�";
			break;
		case "17":
			RS = "�յ����Ӿ���˪���ܴ�/�ر�";
			break;
		case "18":
			RS = "�յ��¶�����";
			break;
		case "19":
			RS = "��ο���";
			break;
		case "20":
			RS = "���Ӿ�����";
			break;
		case "23":
			RS = "����/�ر�ˮů�ȳ�";
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
				RS = RS + "��ǰ����״̬��" + CurrentWorkStayus + "\n" + "��ǰˮ�£�" + currentTemperture + "\n" + "��ǰ���ϣ�\n";
				for (int i = 0; i < Integer.valueOf(error_no); i++) {
					RS = RS + "����" + i + 1 + ":" + errors[i];
				}
			} else {
				RS = "��ǰ����״̬��" + CurrentWorkStayus + "\n" + "��ǰˮ�£�" + currentTemperture + "\n" + "��ǰ���ϣ��޹���";
			}

			break;
		case "24":
			RS = getwaketype(rs);
			break;
		default:
			RS = "��֧�ֹ�������";

		}
		return RS;
	}

	public static String getwaketype(String rs) {
		String RS = rs;
		switch (rs) {
		case "00":
			RS = "�쳣����";
			break;
		case "01":
			RS = "��ʱ�����¼�";
			break;
		case "02":
			RS = "Gsensor�����¼�";
			break;
		case "03":
			RS = "can�źŻ����¼�";
			break;
		case "04":
			RS = "���Ż�����";
			break;

		}
		return RS;
	}

	public static String getcathotstatus(String rs) {
		String RS = rs;
		switch (rs) {
		case "00":
			RS = "ˮů�豸����";
			break;
		case "01":
			RS = "ˮů�豸������";
			break;
		case "02":
			RS = "ˮů�豸�ر���";
			break;
		case "03":
			RS = "ˮů�豸������";
			break;
		case "10":
			RS = "������֧���ȳ���";
			break;

		}
		return RS;
	}

	public static String getPackageTag(String rs) {
		String RS = rs;
		switch (rs) {
		case "00":
			RS = "��ȡ����״̬Ӧ��";
			break;
		case "01":
			RS = "����ʱ���ϱ���ƽ̨������������ˮ��";
			break;
		}
		return RS;
	}

	public static String getCarStausFuncationList(String rs) {
		String RS = rs;
		switch (rs) {
		case "00":
			RS = "N/A������";
			break;
		case "01":
			RS = "��ȡ����״̬";
			break;
		case "05":
			RS = "��ȡ����״̬";
			break;
		case "07":
			RS = "��ȡ�촰״̬";
			break;
		case "09":
			RS = "��ȡ�ƹ�״̬";
			break;
		case "0B":
			RS = "��ȡ��������״̬";
			break;
		case "0D":
			RS = "��ȡ��������״̬";
			break;
		case "0F":
			RS = "��ȡ������״̬";
			break;
		case "11":
			RS = "��ȡ������Դ״̬";
			break;
		case "13":
			RS = "��ȡWIFI����״̬";
			break;
		case "14":
			RS = "��ȡ�յ�״̬";
			break;
		case "23":
			RS = "��ȡ/���浱ǰˮů�ȳ����";
			break;
		case "24":
			RS = "�ն˻��Ѻ󱨸�ƽ̨�������";
			break;

		}
		return RS;
	}

	public static String subCommands(String rs) {
		String RS = rs;
		switch (rs) {
		case "01":
			rs = "����";
			break;
		case "02":
			rs = "ʧ��";
			break;
		case "03":
			rs = "����";
			break;
		case "04":
			rs = "����";
			break;
		case "10":
			rs = "��ѯ����״̬";
			break;
		}
		RS = rs;
		return RS;
	}

	public static String lockTypes(String lock_type) {
		String RS = lock_type;
		if (lock_type.equals("01")) {
			lock_type = "1. ���� MD5";
		} else if (lock_type.equals("02")) {
			lock_type = "2.���� TSC1 ����";
		} else if (lock_type.equals("03")) {
			lock_type = "3.MD5 ���� 1";
		} else if (lock_type.equals("04")) {
			lock_type = "4.MD5 ���� 2";
		} else if (lock_type.equals("00")) {
			lock_type = "δ֪Э�飬Ĭ�ϲ����";
		}
		RS = lock_type;
		return RS;
	}

	public static String responseCommands(String rs) {
		String RS = rs;
		switch (rs) {
		case "01":
			rs = "������Ӧ";
			break;
		case "02":
			rs = "ʧ����Ӧ";
			break;
		case "03":
			rs = "������Ӧ";
			break;
		case "04":
			rs = "������Ӧ";
			break;
		}
		RS = rs;
		return RS;
	}

	public static String execResults(String rs) {
		String RS = rs;
		switch (rs) {
		case "51":
			rs = "δѧϰ��";
			break;
		case "52":
			rs = "Э��ѧϰ�У�";
			break;
		case "53":
			rs = "Э��ѧϰʧ�ܣ�";
			break;
		case "54":
			rs = "Э��ѧϰ�ɹ���";
			break;
		case "55":
			rs = "Э�鲻֧�֣�";
			break;
		case "56":
			rs = "δ��������Э�飻";
			break;
		case "57":
			rs = "����Э���У�";
			break;
		case "58":
			rs = "����Э��ʧ�ܣ�";
			break;
		case "59":
			rs = "����Э��ɹ���";
			break;
		case "5A":
			rs = "δ���";
			break;
		case "5B":
			rs = "�����У�";
			break;
		case "5C":
			rs = "����ʧ�ܣ�";
			break;
		case "5D":
			rs = "����ɹ���";
			break;
		case "5E":
			rs = "������";
			break;
		case "5F":
			rs = "����ʧ�ܣ�";
			break;
		case "60":
			rs = "���ֳɹ���";
			break;
		case "61":
			rs = "û�������������";
			break;
		case "62":
			rs = "��������ִ���У�";
			break;
		case "63":
			rs = "��������ʧ�ܣ�";
			break;
		case "64":
			rs = "�������óɹ���";
			break;
		case "65":
			rs = "������";
			break;
		case "66":
			rs = "����ʧ�ܣ�";
			break;
		case "67":
			rs = "�����ɹ���";
			break;
		case "68":
			rs = "û�н����������";
			break;
		case "69":
			rs = "��������ִ���У�";
			break;
		case "6A":
			rs = "��������ʧ�ܣ�";
			break;
		case "6B":
			rs = "�������óɹ���";
			break;
		case "6C":
			rs = "������";
			break;
		case "6D":
			rs = "����ʧ�ܣ�";
			break;
		case "6E":
			rs = "�����ɹ���";
			break;
		case "6F":
			rs = "û�йر�����ָ�";
			break;
		case "70":
			rs = "�ر�����ִ���У�";
			break;
		case "71":
			rs = "�ر�����ʧ�ܣ�";
			break;
		case "72":
			rs = "�ر������ɹ�";
			break;
		case "01":
			rs = "��ͨ����";
			break;
		case "02":
			rs = "����Э��ɹ�";
			break;
		case "03":
			rs = "����ָ��ɹ�";
			break;
		case "04":
			rs = "����Э��ʧ��(Э�鲻֧��)";
			break;
		case "05":
			rs = "����ָ��ʧ��";
			break;
		case "06":
			rs = "����������óɹ�";
			break;
		case "07":
			rs = "����ָ�����";
			break;
		case "91":
			rs = "����ָ����Ч��������Э�飬��������Э��";
			break;
		case "92":
			rs = "���ü���ָ����Ч������ָ��ִ���� ";
			break;
		case "93":
			rs = "���ü���ָ����Ч������ָ��ִ���ѳɹ�";
			break;
		case "94":
			rs = "��������ָ����Ч��δ������ȼ���";
			break;
		case "95":
			rs = "��������ָ����Ч������ָ��ִ����";
			break;
		case "96":
			rs = "��������ָ����Ч������������";
			break;
		case "97":
			rs = "��������ָ����Ч���������óɹ�";
			break;
		case "98":
			rs = "��������ָ����Ч������ָ��ִ���ѳɹ�";
			break;
		case "99":
			rs = "���ý���������Ч��������δ����";
			break;
		case "9A":
			rs = "���ý���ָ����Ч������ָ��ִ���� ";
			break;
		case "9B":
			rs = "���ý���ָ����Ч������������";
			break;
		case "9C":
			rs = "���ý���ָ����Ч���������óɹ�";
			break;
		case "9D":
			rs = "���ý���ָ����Ч������ָ��ִ���ѳɹ�";
			break;
		case "9E":
			rs = "���ùر�ָ����Ч��δ����";
			break;
		case "9F":
			rs = "���ùر�ָ����Ч���ر�ָ��ִ���� ";
			break;
		case "A0":
			rs = "���ùر�ָ����Ч���ر�ָ��ִ���ѳɹ�";
			break;
		case "A1":
			rs = "���ùر�ָ����Ч������ָ�������У���ȴ�";
			break;
		case "A2":
			rs = "���ùر�ָ����Ч������ָ�����óɹ�����ȴ�";
			break;
		case "A3":
			rs = "���ùر�ָ����Ч������ָ��ִ���У���ȴ�";
			break;
		case "A4":
			rs = "���ùر�ָ����Ч������ָ����ִ�гɹ��������";
			break;
		case "A5":
			rs = "���ùر�ָ����Ч������ָ�������У��ȴ�����";
			break;
		case "A6":
			rs = "���ùر�ָ����Ч������ָ������ʧ�ܣ��ȴ�����";
			break;
		case "A7":
			rs = "���ùر�ָ����Ч������ָ�����óɹ����ȴ�����";
			break;
		case "A8":
			rs = "���ùر�ָ����Ч������ָ��ִ���У��ȴ�����";
			break;
		case "A9":
			rs = "���ùر�ָ����Ч������ָ����ִ��ʧ��";
			break;
		case "AA":
			rs = "��ִ�в�����GPSID��ƥ";
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
		frame = new JFrame("֧�ֽ���8F40/8F41/8F51/0F40/0F41/0F51");
		frame.setBounds(100, 100, 798, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, 13, 749, 516);
		frame.getContentPane().add(panel_1);

		JLabel lblNewLabel = new JLabel("������Э�����ݣ�");

		InArea = new JTextArea();

		JButton btnNewButton = new JButton("����");
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
					OutArea.setText("������Э������!");
				}

			}
		});

		OutArea = new JTextArea();

		JLabel lblNewLabel_1 = new JLabel("Э�����ݽ������£�");
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
