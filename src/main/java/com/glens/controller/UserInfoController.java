package com.glens.controller;

import com.alibaba.fastjson.JSONObject;
import com.glens.model.UserInfo;
import com.glens.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author yk
 */
@Controller
@RequestMapping(value="/user")
public class UserInfoController {

	@Autowired
	private UserInfoService userInfoService;

	/**
	 * 得到所有用户信息
	 * @param map map
	 * @return String
	 */
	@RequestMapping(value="/getAllUser")
	public String getAllUser(Map<String, Object> map){
		List<UserInfo> userList = userInfoService.findAll();
		map.put("ALLUSER", userList);
		return "allUser";
	}
	/**
	 * 通过handler前往添加用户页面
	 * @param map Map
	 * @return String
	 */
	@RequestMapping(value="/addUser",method= RequestMethod.GET)
	public String addUser(Map<String, Object> map){
		//因为页面使用spring的form标签，其中属性modelAttribute需要存在bean 要不会报错
		map.put("command", new UserInfo());
		return "addUser";
	}

	/**
	 * 添加用户操作
	 * @param userinfo UserInfo
	 * @return String
	 */
	@RequestMapping(value="/addUser",method=RequestMethod.POST)
	public String save(UserInfo userinfo){
		int result = userInfoService.insert(userinfo);
		System.out.println("添加:"+result);
		return "redirect:/user/getAllUser";
	}
	/**
	 * 删除用户操作
	 * @param id 主键
	 * @return String
	 */
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	public String delete(@PathVariable(value="id") int id){
		int result = userInfoService.deleteByPrimaryKey(id);
		System.out.println("删除用户的操作结果为："+result+"传递进来的id为："+id);
		return "redirect:/user/getAllUser";
	}
	/**
	 * 更新前先根据id找到用户信息，回显到页面上
	 * @param id 主键
	 * @param map Map
	 * @return String
	 */
	@RequestMapping(value="/detail/{id}",method=RequestMethod.GET)
	public String input(@PathVariable(value="id") Integer id,Map<String, Object> map){
		map.put("command", userInfoService.selectByPrimaryKey(id));
		return "addUser";
	}

	@ModelAttribute
	public void getUserInfo(@RequestParam(value="userId",required=false) Integer id
			,Map<String, Object> map){
		System.out.println("每个controller 方法都会先调用我哦");
		if(id != null){
			System.out.println("update 操作");
			map.put("userInfo", userInfoService.selectByPrimaryKey(id));
		}
		System.out.println("insert 操作");
		System.out.println("可以的"+ JSONObject.toJSON(userInfoService.selectByPrimaryKey(id)));
	}

	@RequestMapping(value="/addUser",method=RequestMethod.PUT)
	public String update(UserInfo userinfo){
		userInfoService.updateByPrimaryKey(userinfo);
		return "redirect:/user/getAllUser";
	}
}
