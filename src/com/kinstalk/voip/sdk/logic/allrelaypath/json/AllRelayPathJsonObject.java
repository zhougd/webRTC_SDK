package com.kinstalk.voip.sdk.logic.allrelaypath.json;

import java.util.ArrayList;
import java.util.List;

import com.kinstalk.voip.sdk.http.AbstractJsonObject;

public class AllRelayPathJsonObject extends AbstractJsonObject
{
	private List<List<String>> allRelayPaths;
	//private ArrayList<ArrayList<String>> allRelayPaths;
	
	public AllRelayPathJsonObject()
	{
		super.setmShouldPersist(true);
	}

	public List<List<String>>  getAllRelayPath() {
		return allRelayPaths;
	}

	public void setAllRelayPath(List<List<String>>  allRelayPaths) {
		this.allRelayPaths = allRelayPaths;
	}
	
}
