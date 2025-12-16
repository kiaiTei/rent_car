package com.example.demo.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.DAO_rent;
import com.example.demo.model.Entitycar;
import com.example.demo.model.Entityres;
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
    @GetMapping("/login_employee")
    public String loginForm() {
        return "login_employee";
    }

    @PostMapping("/login_employee")
    public String loginEmployee(HttpServletRequest r, HttpSession s) {

        String idStr = r.getParameter("id");
        String pass = r.getParameter("password");

        if (idStr == null || idStr.isEmpty()) {
            r.setAttribute("msg", "IDを入力してください");
            return "login_employee_re";
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            r.setAttribute("msg", "IDは数字で入力してください");
            return "login_employee_re";
        }

        String pw = dao_rent.login(id);
        if (pw != null && pw.equals(pass)) {
            s.setAttribute("loginEmployee", id);
            return "redirect:/employee_exclusive";
        }

        r.setAttribute("msg", "IDまたはパスワードが間違っています");
        return "login_employee_re";
    }

    @GetMapping("/employee_exclusive")
    public String employeeExclusive(HttpSession session) {
        if (session.getAttribute("loginEmployee") == null) {
            return "redirect:/login_employee";
        }
        return "employee_exclusive";
    }

    /******************************************************************
     * CUSTOMER（顧客管理）ブロック
     * 担当：B
     ******************************************************************/
    @GetMapping("/customer_info")
    public String customerInfo(Model model) {
        return "customer_info";
    }

    @PostMapping("/customer_update")
    public String customerUpdate(@RequestParam int customerId, Model model) {
        return "customer_update";
    }

    @PostMapping("/customer_delete")
    public String customerDelete(@RequestParam int customerId, Model model) {
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

  	
  	@RequestMapping( "/all_select" )
	public String all_select( Model m ,@ModelAttribute Entitycar ec, HttpSession s ) {

  		ArrayList < Entitycar > all_car = dao_rent.zenken_kensaku( ) ;
        m.addAttribute( "all_car" , all_car ) ;
        
        
        return "all_select";
		
	}
/*******************************************************************/
  	
  	
/******************** CAR INPUT *******************************/  	
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
  	
  	/******************* CAR EDIT *****************************************/
	// 編集ページ表示
    @GetMapping("/car_edit")
    public String carEdit(@RequestParam("id") int id, Model model) {
        Entitycar car = dao_rent.getCarById(id);  // 根据 id 查询数据
        model.addAttribute("car", car);
        return "car_edit";
    }

    // 更新処理
    @PostMapping("/car_update")
    public String carUpdate(@RequestParam("id") int id,
                            @RequestParam("brand") String brand,
                            @RequestParam("model") String model,
                            @RequestParam("seats") int seats,
                            @RequestParam("price") int price,
                            @RequestParam("status") String status) {

        dao_rent.updateCar(id, brand, model, seats, price, status);
        return "redirect:/all_select";  // 更新後全件表示に戻る
    }

    /******************************************************************
     * RESERVE（予約管理）ブロック
     * 担当：C
     ******************************************************************/
    @GetMapping("/reserve_input")
    public String reserveInput(Model m ,@ModelAttribute Entitycar ec, HttpSession s) {
        if (s.getAttribute("loginEmployee") == null) {
            return "redirect:/login_employee";
        }
        
  		ArrayList < Entityres > all_res = dao_rent.res_zenken( ) ;
        m.addAttribute( "all_res" , all_res ) ;
        return "reserve_input";
    }
    
    @PostMapping("/reserve_confirm")
	public String reserve_confirm( Entityres er, HttpSession s ) {
		s.setAttribute( "er" , er ) ;
		return "reserve_confirm" ;
		
	}

    @PostMapping("/reserve_result")
    public String reserveResult(HttpSession s) {
    	Entityres er = ( Entityres ) s.getAttribute("er") ;
		// DBへ登録するプログラム。
		dao_rent.res_touroku( er) ;

        return "reserve_result";
    }
    
 // 編集ページ表示
    @GetMapping("/reserve_update")
    public String reserve_update(@RequestParam("id") int id, Model model) {
        Entityres res = dao_rent.getOrderById(id);  // 根据 id 查询数据
        model.addAttribute("res", res);
        return "reserve_update";
    }

    // 更新処理
    @PostMapping("/reserve_update_confirm")
    public String reserve_update_confirm(
        @RequestParam("res_id") int res_id,
        @RequestParam("customer_id") int c_id,
        @RequestParam("car_id") int car_id,
        @RequestParam("rent_start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate rent_sDate,
        @RequestParam("rent_end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate rent_eDate,
        @RequestParam("status") String status) {

        // 如果 dao_rent.updateRes 需要 LocalDateTime，可以在这里转换：
        LocalDateTime startTime = rent_sDate.atStartOfDay();
        LocalDateTime endTime = rent_eDate.atStartOfDay();

        dao_rent.updateRes(res_id, c_id, car_id, startTime, endTime, status);
        return "redirect:/reserve_input";  
    }



}
