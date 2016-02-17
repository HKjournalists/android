package com.kplus.car.container;

import com.kplus.car.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class DazeConfigParser {
	private JSONObject settings;
	private String startPage;
	private Map<String, String > modulesDict;
	private List<String> startupMethodNames;
	private List<String> whiteList;
	
	private void init(){
		settings = new JSONObject();
		modulesDict = new HashMap<String, String>();
		startupMethodNames = new ArrayList<String>();
	}
	
	public void initWithJson(String json){
		init();
		try {
			JSONObject jo = new JSONObject(json);
			settings.put("package", jo);
			startPage = jo.optString("entry");
			parseModule(jo.optJSONArray("require"));
			String depends = jo.toString();
			depends = depends.substring(depends.indexOf("\"depends\":[") + "\"depends\":[".length());
			depends = depends.substring(0, depends.indexOf("],"));
			parseDepends(depends);
			String white = null;
			white = json.substring(json.indexOf("\"white\":[") + "\"white\":[".length());
			white = white.substring(0, white.indexOf("]"));
			parseWhite(white);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void parseModule(JSONArray array){
		if(array != null && array.length() > 0){
			try{
				for(int i=0;i<array.length();i++){
					JSONObject jo = array.getJSONObject(i);
					modulesDict.put(jo.optString("moduleName"), jo.optString("className"));
				}
			}
			catch(Exception E){
				E.printStackTrace();
			}
		}
	}
	
	private void parseDepends(String depends){
		try{
			depends = depends.replaceAll("\\\"", "");
			String[] params = depends.split(",");
			if(params != null && params.length > 0)
				for(String param :params){
					startupMethodNames.add(param);
				}
		}catch(Exception e){
			e.printStackTrace();
		}
 	}

	private void parseWhite(String white){
		try {
			if (white != null) {
				String[] whites = white.split(",");
				if(whites != null && whites.length > 0){
					whiteList = new ArrayList<String>(whites.length);
					for(String item : whites){
						if(!StringUtils.isEmpty(item)){
							whiteList.add(item);
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public JSONObject getSettings() {
		return settings;
	}

	public void setSettings(JSONObject settings) {
		this.settings = settings;
	}

	public String getStartPage() {
		return startPage;
	}

	public void setStartPage(String startPage) {
		this.startPage = startPage;
	}

	public Map<String, String> getModulesDict() {
		return modulesDict;
	}

	public void setModulesDict(Map<String, String> modulesDict) {
		this.modulesDict = modulesDict;
	}

	public List<String> getStartupMethodNames() {
		return startupMethodNames;
	}

	public void setStartupMethodNames(List<String> startupMethodNames) {
		this.startupMethodNames = startupMethodNames;
	}

	public List<String> getWhiteList() {
		return whiteList;
	}

	public void setWhiteList(List<String> whiteList) {
		this.whiteList = whiteList;
	}
}
