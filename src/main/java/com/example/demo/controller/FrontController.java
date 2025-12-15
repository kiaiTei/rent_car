package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Customer;
import com.example.demo.model.DAO_rent;
import com.example.demo.model.Entitycar;
import com.example.demo.service.CustomerService;

@Controller
public class FrontController {

    /************** フィールド（※ここは統合担当のみ編集） **************/
    private final DAO_rent dao_rent;
    private final CustomerService customerService;

    public FrontController(DAO_rent dao_rent,
                            CustomerService customerService) {
        this.dao_rent = dao_rent;
        this.customerService = customerService;
    }

    /******************************************************************
     * AUTH（ログイン）ブロック
     * 担当：A
     ******************************************************************/
    @PostMapping("/login_employee")
  	public String login_employee(Model m, HttpServletRequest r, HttpSession s) {
  	    String idStr = r.getParameter("id");
  	    String in_pass = r.getParameter("password");

  	    if (idStr == null || idStr.isEmpty()) {
  	        r.setAttribute("msg", "IDを入力してください");
  	        return "login_employee_re";
  	    }
  	    
  	  r.setAttribute("id",idStr);

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
    /******************************************************************
     * CUSTOMER（顧客管理）ブロック
     * 担当：B
     ******************************************************************/
    @GetMapping("/customer_info")
    public String customerInfo(Model model) {
        List<Customer> list = customerService.findAll();
        model.addAttribute("customerList", list);
        return "customer_info";
    }

    @PostMapping("/customer_update")
    public String customerUpdate(@RequestParam int customerId, Model model) {
        Customer customer = customerService.findById(customerId);
        model.addAttribute("customer", customer);
        return "customer_update";
    }

    @PostMapping("/customer_delete")
    public String customerDelete(@RequestParam int customerId, Model model) {
        Customer customer = customerService.findById(customerId);
        model.addAttribute("customer", customer);
        return "customer_delete_confirm";
    }

    @PostMapping("/customer_register")
    public String customerRegister() {
        return "customer_register";
    }

    @PostMapping("/customer_register_confirm")
    public String customerRegisterConfirm() {
        return "customer_register_confirm";
    }

    @PostMapping("/customer_register_result")
    public String customerRegisterResult() {
        return "customer_register_result";
    }

    /******************************************************************
     * CAR（車両管理）ブロック
     * 担当：C
     ******************************************************************/
    /******************** CAR KANNRI *******************************/
  	@PostMapping( "/car_info" )
	public String car_info() {
  		
  		//全件表示
		return "car_info";
     }
  	
  	@RequestMapping( "/all_select" )
	public String all_select( Model m ) {

  		ArrayList < Entitycar > all_car = dao_rent.zenken_kensaku( ) ;
        m.addAttribute( "all_veg" , all_car ) ;
        return "all_select";
		
	}
/*******************************************************************/
  	
  	
/******************** CAR INPUT *******************************/
  
  	
  	/*
  	 * 
  	 * 	@RequestMapping( "/car_input_confirm" )
	public String car_input_confirm( @ModelAttribute Entitycar ec, HttpServletRequest r,HttpSession s ) {
  		String plate_num = r.getParameter("plate_num");
  	    String brand = r.getParameter("brand");
  	  String model = r.getParameter("model");
  	int seats = Integer.parseInt(r.getParameter("seats"));
  	int rent_price = Integer.parseInt(r.getParameter("rent_price"));
  	String status = r.getParameter("status");
		// ユーザーが入力したデータをセッションに保存する。
  	String statusValue = r.getParameter("status");
  	String statusText = "";

  	switch (statusValue) {
  	    case "available":
  	        statusText = "予約可能";
  	        break;
  	    case "rented":
  	        statusText = "貸出中";
  	        break;
  	    case "maintenance":
  	        statusText = "整備中";
  	        break;
  	}

  	

  	ec.setBrand(brand);
    ec.setModel(model);
    ec.setSeats(seats);
    ec.setPrice(rent_price);
    ec.setStatus(status);

   
		s.setAttribute( "ec" , ec ) ;
		s.setAttribute( "plate_num" , plate_num ) ;
		s.setAttribute( "brand" , brand ) ;
		s.setAttribute( "model" , model ) ;
		s.setAttribute( "seats" , seats ) ;
		s.setAttribute( "rent_price" , rent_price ) ;
		s.setAttribute( "status" , statusText ) ;

		return "car_input_confirm" ;
		
	}
  	
  	 * 
  	 * @RequestMapping( "/car_insert_result" )
  	 
	public String insert_result( HttpServletRequest r,HttpSession s ) {

		Entitycar ev = ( Entitycar ) s.getAttribute("ev") ;
		String plate_num = (String) s.getAttribute("plate_num");
		System.out.println(plate_num);
		String brand = (String) s.getAttribute("brand");
		String model = (String) s.getAttribute("model");
		String seats_str= s.getAttribute("seats");
		System.out.println(seats_str);
		int seats = Integer.parseInt(seats_str);
		int rent_price = Integer.parseInt((String) s.getAttribute("rent_price"));
		String status = (String) s.getAttribute("status");
		
		// DBへ登録するプログラム。
		dao_rent.car_touroku(plate_num, brand,model,seats,rent_price,status ) ;
		
		return "car_insert_result" ;
		
	}
	
	*/
  	
  	
  	
  	@RequestMapping( "/car_input_confirm" )
	public String car_input_confirm( @ModelAttribute Entitycar ec, HttpSession s ) {
		// これを書くとev変数の中にパラーメーターを自動で詰めてくれる。
		// 裏では以下のソースコードが自動で処理される。
		
		/*
		// ユーザーが入力したパラメーターを受け取り。
		String vn = r.getParameter("vegetableName") ;
		String p = r.getParameter("price") ;
		int p_int = Integer.parseInt(p) ;
		
		// そのデータを、エンティティーに詰め込む。
		EntityVegetable ev = new EntityVegetable( 0 , vn , p_int ) ;
		*/
		
		
		// ユーザーが入力したデータをセッションに保存する。
		s.setAttribute( "ec" , ec ) ;
		
		String statusText = "";

	  	switch (ec.getStatus()) {
	  	    case "available":
	  	        statusText = "予約可能";
	  	        break;
	  	    case "rented":
	  	        statusText = "貸出中";
	  	        break;
	  	    case "maintenance":
	  	        statusText = "整備中";
	  	        break;
	  	}
	  	
	  	s.setAttribute( "statusText" , statusText ) ;
		return "car_input_confirm" ;
		
	}
	
	
	
	
	
	@RequestMapping( "/car_insert_result" )
	public String car_insert_result( HttpSession s ) {

		Entitycar ec = ( Entitycar ) s.getAttribute("ec") ;
		String statusText = (String) s.getAttribute("statusText");

		
		
		// DBへ登録するプログラム。
		dao_rent.car_touroku( ec ) ;

		return "car_insert_result" ;
		
	}
  	
  	
/*******************************************************************/
    /******************************************************************
     * RESERVE（予約管理）ブロック
     * 担当：C
     ******************************************************************/
    @GetMapping("/reserve_input")
    public String reserveInput(HttpSession session) {
        if (session.getAttribute("loginEmployee") == null) {
            return "redirect:/login_employee";
        }
        return "reserve_input";
    }

    @PostMapping("/reserve_confirm")
    public String reserveConfirm() {
        return "reserve_confirm";
    }

    @PostMapping("/reserve_result")
    public String reserveResult() {
        return "reserve_result";
    }
}
