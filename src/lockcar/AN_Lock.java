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
		 * �õ����ڵ����� JLabel L1 = new JLabel("Hello,world!"); // ����һ����ǩ �����ó�ʼ����
		 * conn.add(L1); jf.setBounds(200, 200, 300, 200); // ���ô��ڵ����� ����λ���Լ����ڵĴ�С
		 * jf.setVisible(true);// ���ô��ڿɼ� // jf.setDefaultCloseOperation(); //���ùرշ�ʽ
		 * ��������õĻ� �ƺ��رմ���֮�󲻻��˳�����
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
		map.put("��ʶλ", R8F40.substring(i, i = +i + R8f40[0]));
		String id = R8F40.substring(i, i = +i + R8f40[1]);
		map.put("��Ϣ ID", id);
		map.put("��Ϣ������", R8F40.substring(i, i = +i + R8f40[2]));
		map.put("�ն��ֻ���", R8F40.substring(i, i = +i + R8f40[3]));
		map.put("��Ϣ��ˮ��", R8F40.substring(i,i=+i+R8f40[4]));

		map.put("У����", R8F40.substring(R8F40.length() - 1 - 2 - 2, R8F40.length() - 1 - 2));
		map.put("��ʶλ", R8F40.substring(R8F40.length() - 1 - 2, R8F40.length() - 1));
		String body = R8F40.substring(i, R8F40.length() - 1 - 2 - 2);
		if (id.equals("8F40")) {
			int j = 0;
			map.put("����Э����", body.substring(j, j += 1));
			String subcommand = body.substring(j, j += 1);
			map.put("������", subcommand);
			if (subcommand.equals("1") || subcommand.equals("2")) {
				String stats = body.substring(j, j += 4);
				if (stats.equals("15F4")) {
					map.put("�������ܼ���ر�", "����");
				} else if (stats.equals("4F51")) {
					map.put("�������ܼ���ر�", "����ر�");
				}

				map.put(" GPSID ", body.substring(j, j += 3));
				map.put("�̶���Կ", body.substring(j, j += 3));
			} else if (subcommand.equals("3") || subcommand.equals("4")) {
				map.put("����ת��", body.substring(j, j += 4));
				map.put(" GPSID ", body.substring(j, j += 3));
				map.put("�̶���Կ", body.substring(j, j += 3));
				if (j != body.length()) {
					map.put("��������", body.substring(j, j += 4));
				}
			}
		}else if(id.equals("0F40")) {
			int j=0;
			map.put("Ӧ����ˮ�� ", body.substring(j, j += 4));
			map.put("ִ�н�� ", body.substring(j, j += 2));
			String lock_type =body.substring(j, j += 2);
			if(lock_type.equals("01")) {
				lock_type = "1. ���� MD5";
			}else if(lock_type.equals("02")) {
				lock_type = "2.���� TSC1 ����";
			} 
			else if(lock_type.equals("03")) {
				lock_type = "3.MD5 ���� 1";
			} 
			else if(lock_type.equals("04")) {
				lock_type = "4.MD5 ���� 2";
			} 
			map.put("����Э������ ", lock_type);
			map.put("��Ӧ���� ", body.substring(j, j += 2));
		}else {
			System.out.printf("��֧��Э������");
		}
		return map;
	}
}
