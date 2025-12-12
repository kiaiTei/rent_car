package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FrontController {
	
	@PostMapping( "/login_employee" )
	public String login_employee() {
		return "redirect:/employee_exclusive.html";	
     }
	@PostMapping( "/employee_exclusive" )
	public String employee_exclusive() {
		return "employee_exclusive";
     }
	@PostMapping( "/reserve" )
	public String reserve() {
		return "reserve";
     }
	@PostMapping( "/reserve_input" )
	public String reserve_input() {
		return "reserve_input";
     }
	@PostMapping( "/reserve_confirm" )
	public String reserve_confirm() {
		return "reserve_confirm";
     }
	@PostMapping( "/reserve_result" )
	public String reserve_result() {
		return "reserve_result";
     }
	@PostMapping( "/reserve_update" )
	public String reserve_update() {
		return "reserve_update";
     }
	@PostMapping( "/reserve_update_result" )
	public String reserve_update_result() {
		return "reserve_update_result";
     }
	@PostMapping( "/reserve_delete" )
	public String reserve_delete() {
		return "reserve_delete";
     }
	@PostMapping( "/reserve_delete_result" )
	public String reserve_delete_result() {
		return "reserve_delete_result";
     }
	@PostMapping( "/customer" )
	public String customer() {
		return "customer";
     }
	
}

