package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FrontController {
	@RequestMapping( "/login" )
	public String login() {
		return "login";
	}
	@RequestMapping( "/login_employee" )
		public String login_employee() {
			return "login_employee";	

     }
	@RequestMapping( "/reserve" )
	public String reserve() {
		return "reserve";
     }
	@RequestMapping( "/reserve_input" )
	public String reserve_input() {
		return "reserve_input";
     }
	@RequestMapping( "/reserve_update" )
	public String reserve_update() {
		return "reserve_update";
     }
	@RequestMapping( "/reserve_update_result" )
	public String reserve_update_result() {
		return "reserve_update_result";
     }
	@RequestMapping( "/reserve_delete" )
	public String reserve_delete() {
		return "reserve_delete";
     }
	@RequestMapping( "/reserve_delete_result" )
	public String reserve_delete_result() {
		return "reserve_delete_result";
     }
//	@RequestMapping( "/customer" )
//	public String customer() {
//		return "customer";
//     }
}

