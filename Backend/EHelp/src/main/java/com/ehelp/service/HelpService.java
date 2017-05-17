package com.ehelp.service;

import java.util.List;

import com.ehelp.entity.Help;
import com.ehelp.entity.Response;

public interface HelpService {

	//获取求助列表
	public List<Object[]> getAllHelps();
		
	//根据id查看求助详情
	public List<Object[]> getHelp(int id);
	
	//响应求助
	public boolean responseHelp(Response r);
	
	//结束求助
	public boolean endHelp(int id);
	
	//添加求助
	public boolean launchHelp(Help p);
	
	//根据id查看响应详情
	public List<Object[]> getAllResponse(int id);
	
}
