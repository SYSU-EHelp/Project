package com.ehelp.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehelp.entity.Help;
import com.ehelp.entity.Response;
import com.ehelp.service.HelpServiceImpl;

@Controller
@RequestMapping("/helps")
public class HelpController {

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	@Autowired
	private HelpServiceImpl helpService;
	
	/*
	 * 获取求助列表
	 */
	@RequestMapping(value="", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getHelps(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (session.getAttribute("user") == null) {
			map.put("status", 500);
			map.put("ermsg", "请先登录");
			return map;
		}
		map.put("status", "200");
	 	List<Object[]> results = helpService.getAllHelps();
	 	List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		for (Object[] o : results) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("id", o[0]);
			m.put("title", o[1]);
			m.put("description", o[2]);
			m.put("address", o[3]);
			m.put("finished", o[4]);
			m.put("date", o[5]);
			m.put("launcher_username", o[6]);
			m.put("launcher_avatar", o[7]);
			m.put("phone", o[8]);
			data.add(m);
		}
		map.put("data", data);
		return map;
	}
	
	/*
	 * 查看求助详情
	 */
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getHelp(@PathVariable("id") int id, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (session.getAttribute("user") == null) {
			map.put("status", 500);
			map.put("ermsg", "请先登录");
			return map;
		}
		map.put("status", "200");
	 	List<Object[]> results = helpService.getHelp(id);
	 	List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		for (Object[] o : results) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("id", o[0]);
			m.put("title", o[1]);
			m.put("description", o[2]);
			m.put("address", o[3]);
			m.put("finished", o[4]);
			m.put("date", o[5]);
			m.put("launcher_username", o[6]);
			m.put("launcher_avatar", o[7]);
			m.put("phone", o[8]);
			data.add(m);
		}
		map.put("data", data);
		return map;
	}
	
	/*
	 * 响应求助事件
	 */
	@RequestMapping(value="/{id}/response", method=RequestMethod.PATCH)
	@ResponseBody
	public Map<String, Object> responseHelp(@PathVariable("id") int id, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (session.getAttribute("user") == null) {
			map.put("status", 500);
			map.put("ermsg", "请先登录");
			return map;
		}
		int user_id = (Integer) session.getAttribute("user");
		Response r = new Response(1, id, user_id);
		if (helpService.responseHelp(r)) map.put("status", 200);
		else {
			map.put("status", 500);
			map.put("errmsg", "响应失败");
		}
		return map;
	}
	
	/*
	 * 结束求助事件
	 */
	@RequestMapping(value="/{id}/finish", method=RequestMethod.PATCH)
	@ResponseBody
	public Map<String, Object> finishHelp(@PathVariable("id") int id, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (session.getAttribute("user") == null) {
			map.put("status", 500);
			map.put("ermsg", "请先登录");
			return map;
		}
		if (helpService.endHelp(id)) map.put("status", 200);
		else {
			map.put("status", 500);
			map.put("errmsg", "操作失败");
		}
		return map;
	}
	
	/*
	 * 发起求助
	 */
	@RequestMapping(value="", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> launchHelp(@RequestParam(value="title")String title, @RequestParam(value="description")String description,
			@RequestParam(value="address")String address, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (session.getAttribute("user") == null) {
			map.put("status", 500);
			map.put("ermsg", "请先登录");
			return map;
		}
		try {
			int id = (Integer) session.getAttribute("user");
			Date date = sdf.parse(sdf.format(new Date()));
			Help h = new Help(id, title, description, date, address, 0);
			if (helpService.launchHelp(h)) map.put("status", 200);
			else {
				map.put("status", 500);
				map.put("errmsg", "求助失败");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		return map;
	}
	
	/*
	 * 求助者查看响应详情
	 */
	@RequestMapping(value="/{id}/responses", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> response(@PathVariable("id") int id, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (session.getAttribute("user") == null) {
			map.put("status", 500);
			map.put("ermsg", "请先登录");
			return map;
		}
		map.put("status", "200");
		List<Object[]> results = helpService.getAllResponse(id);
	 	List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		for (Object[] o : results) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("responser_username", o[0]);
			m.put("responser_avatar", o[1]);
			m.put("phone", o[2]);
			data.add(m);
		}
		map.put("data", data);

		return map;
	}
	
}



