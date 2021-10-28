package com.zft.bluetooth;

import java.util.ArrayList;
import java.util.List;


public class DecodeUtils {

	/**
	 * ������ ���ݽ���
	 * 
	 * @param buffer
	 * @return
	 */
	public static double parseT(byte[] buffer) {
		double sum = 0;
		for (int i = 0; i < buffer.length; i++) {
			int tag = buffer[i] & 0xff;
			if (tag == 0xFF) {
				// by[0] = buffer[i];
				int h = buffer[++i] & 0xff;
				int l = buffer[++i] & 0xff;
				int check = buffer[++i];
				if ((h ^ l) == check) {
					sum = h * 256 + l;
					sum = (sum / 10);
				}
			}
		}
		return sum;
	}

	/**
	 * ���س� ���ݽ��� list ÿ����Ŀ�����ݶ���16���Ƶġ�
	 * 
	 * @param bys
	 * @return
	 */
	public static List<String> parse16Data(byte[] bys) {
		int[] lengths = { 1, 1, 1, 1, 2, 2, 2, 2, 2, 1, 2, 2 };
		List<String> values = new ArrayList<String>();
		int num = 0;
		for (int i = 0; i < lengths.length; i++) {
			int length = lengths[i];

			String value = "";
			for (int j = 0; j < length; j++) {
				byte by = bys[num];
				if (by > -1) {
					by = bys[num];
				} else {
					by = 0;
				}
				int intValue = by & 0xff;
				String hexString = String.format("%02X", intValue);
				num++;
				value += hexString + "";
			}
			values.add(value);
		}
		return values;
	}

	

	/**
	 * ��ֵΪ16���Ƶ�list���� ת�� Ϊ ֵΪ10 ���Ƶ� list����
	 * 
	 * @param list
	 *            ֵΪ16���Ƶļ���
	 * @return ֵΪ10���Ƶļ���
	 */
	public static List<String> parse10Data(List<String> list) {

		List<String> dataList = new ArrayList<String>();
		for (String s : list) {
			String ss = "";
			ss += Integer.valueOf(s, 16);
			dataList.add(ss);
		}
		return dataList;
	}
	

	
	
	
	
	

	/**
	 * ��ֵΪ16���Ƶ�list���� ת�� Ϊ ֵΪ10 ���Ƶ� list����
	 * @param list
	 *            ֵΪ16���Ƶļ���
	 * @return ֵΪ10���Ƶļ���
	 */
	public static List<String> processList(byte[] data) {
		List<String> analyzeData = parse16Data(data);
		List<String> resultStrs = new ArrayList<String>();
		for (int i = 0; i < analyzeData.size(); i++) {
			String data2 = analyzeData.get(i);
			switch (i) {
			case 4:
			case 7:
				data2 = (Integer.valueOf(data2, 16) / 10.0) + "kg";
				break;
			case 5:
			case 6:
			case 8:
				data2 = (Integer.valueOf(data2, 16) / 10.0) + "%";
				break;
			case 11:
				data2 = (Integer.valueOf(data2, 16) / 10.0) + "";
				break;
			default:
				data2 = Integer.valueOf(data2, 16) + "";
				break;
			}
			resultStrs.add(data2);
		}
		return resultStrs;
	}
	
}
