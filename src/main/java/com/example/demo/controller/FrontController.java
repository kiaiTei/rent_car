package com.example.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.DAO_rent;

@Controller
public class FrontController {
	
		// DAOを使う準備。
	  	private DAO_rent dao_rent ;
	  	
		// DAO_Vegetableのコンストラクター。
	  	public FrontController( DAO_rent dv ) {
			this.dao_rent = dv ;
		}
	/*
	@PostMapping( "/login_employee" )
	public String login_employee() {
		return "redirect:/employee_exclusive.html";	
     }
	*/
	
	/******************* LOGIN  test    **********************/
	
	  	@PostMapping("/login_employee")
	  	public String login_employee(Model m, HttpServletRequest r, HttpSession s) {
	  	    String idStr = r.getParameter("id");
	  	    String in_pass = r.getParameter("password");

	  	    if (idStr == null || idStr.isEmpty()) {
	  	        r.setAttribute("msg", "IDを入力してください");
	  	        return "login_employee_re";
	  	    }

	  	    int in_id;
	  	    try {
	  	        in_id = Integer.parseInt(idStr);
	  	    } catch (NumberFormatException e) {
	  	        r.setAttribute("msg", "IDは数字で入力してください");
	  	        return "login_employee_re";
	  	    }

	  	  String pw = dao_rent.login(in_id); 

	      if (pw != null && in_pass.equals(pw)) {
	          return "redirect:/employee_exclusive.html"; 
	      } else {
	          r.setAttribute("msg", "IDまたはパスワードが間違っています");
	          return "login_employee_re"; 
	      }
	  	}



	
	/************************************************************/
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
	@PostMapping( "/reserve_update_confirm" )
	public String reserve_update_confirm() {
		return "reserve_update_confirm";
     }
	@PostMapping( "/reserve_update_result" )
	public String reserve_update_result() {
		return "reserve_update_result";
     }
	@PostMapping( "/reserve_delete" )
	public String reserve_delete() {
		return "reserve_delete";
     }
	@PostMapping( "/reserve_delete_confirm" )
	public String reserve_delete_confirm() {
		return "reserve_delete_confirm";
     }
	@PostMapping( "/reserve_delete_result" )
	public String reserve_delete_result() {
		return "reserve_delete_result";
     }
	@PostMapping( "/customer" )
	public String customer() {
		return "customer";
     }
	@PostMapping( "/customer_info" )
	public String customer_info() {
		return "customer_info";
     }
	@PostMapping( "/customer_delete" )
	public String customer_delete() {
		return "customer_delete";
     }
	@PostMapping( "/customer_delete_confirm" )
	public String customer_delete_confirm() {
		return "customer_delete_confirm";
     }
	@PostMapping( "/customer_delete_result" )
	public String customer_delete_result() {
		return "customer_delete_result";
     }
	@PostMapping( "/customer_update" )
	public String customer_update() {
		return "customer_update";
     }
	@PostMapping( "/customer_update_confirm" )
	public String customer_update_confirm() {
		return "customer_update_confirm";
     }
	@PostMapping( "/customer_update_result" )
	public String customer_update_result() {
		return "customer_update_result";
     }
	
	@PostMapping( "/customer_register" )
	public String customer_register() {
		return "customer_register";
     }
	@PostMapping( "/customer_register_confirm" )
	public String customer_register_confirm() {
		return "customer_register_confirm";
     }
	@PostMapping( "/customer_register_result" )
	public String customer_register_result() {
		return "customer_register_result";
     }
}

