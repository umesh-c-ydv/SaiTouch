package com.compassites.sai;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class PropertyReader {

	ArrayList<HashMap<String, String>> radioList = new ArrayList<HashMap<String, String>>();

	public ArrayList<HashMap<String, String>> getRadioLists() {
		HashMap<String, String> radioDetail1 = new HashMap<String, String>();
		radioDetail1.put("radioName", "asia");
		radioDetail1.put("radioURL", "http://stream.radiosai.net:8002/");
		radioDetail1.put("radioMsg", "You are listening to Asian Stream");
		radioList.add(radioDetail1);

		HashMap<String, String> radioDetail2 = new HashMap<String, String>();
		radioDetail2.put("radioName", "africa");
		radioDetail2.put("radioURL", "http://stream.radiosai.net:8004/");
		radioDetail2.put("radioMsg", "You are listening to African Stream");
		radioList.add(radioDetail2);

		HashMap<String, String> radioDetail3 = new HashMap<String, String>();
		radioDetail3.put("radioName", "america");
		radioDetail3.put("radioURL", "http://stream.radiosai.net:8006/");
		radioDetail3.put("radioMsg", "You are listening to American Stream");
		radioList.add(radioDetail3);

		HashMap<String, String> radioDetail4 = new HashMap<String, String>();
		radioDetail4.put("radioName", "bhajan");
		radioDetail4.put("radioURL", "http://stream.radiosai.net:8000/");
		radioDetail4.put("radioMsg", "You are listening to Bhajan Stream");
		radioList.add(radioDetail4);

		HashMap<String, String> radioDetail5 = new HashMap<String, String>();
		radioDetail5.put("radioName", "discourse");
		radioDetail5.put("radioURL", "http://stream.radiosai.net:8008/");
		radioDetail5.put("radioMsg", "You are listening to Discourse Stream");
		radioList.add(radioDetail5);

		HashMap<String, String> radioDetail6 = new HashMap<String, String>();
		radioDetail6.put("radioName", "telugu");
		radioDetail6.put("radioURL", "http://stream.radiosai.net:8020/");
		radioDetail6.put("radioMsg", "You are listening to Telugu Stream");
		radioList.add(radioDetail6);

		return radioList;

	}

	public ArrayList<HashMap<String, String>> getRadioList() {
		HashMap<String, String> radioDetail = new HashMap<String, String>();

		Properties prop = new Properties();
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream(
					"radios.properties");
			prop.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String[] radioStations = prop.getProperty("radioStations").split("##");
		for (String radio : radioStations) {
			String[] stationDetail = prop.getProperty(radio).split("##");
			radioDetail.put("radioName", radio.toUpperCase());
			radioDetail.put("radioURL", stationDetail[0]);
			radioDetail.put("radioMsg", stationDetail[1]);
			radioList.add(radioDetail);
		}

		return radioList;
	}

}
