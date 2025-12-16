package com.example.demo.controller;

import java.sql.Date;
import java.util.ArrayList;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

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
    public String reserveResult(HttpSession s, Model model) {
        Entityres er = (Entityres) s.getAttribute("er");

        Date startTime = er.getRent_sDate();
        Date endTime = er.getRent_eDate();


        if (!dao_rent.isCarAvailable(er.getCar_id(), startTime, endTime)) {
            model.addAttribute("error", "この時間帯の車はすでに予約済みのため、登録できません。");
            return "reserve_error"; // 返回输入页面并显示错误
        }

        dao_rent.res_touroku(er);  // 正常新增
        return "reserve_result";
    }
    
    
    @GetMapping("/reserve_error")
	  public String reserve_error(HttpSession s,Model m) {

	
	  return "reserve_error";
	  }
//    @PostMapping("/reserve_result")
//    public String reserveResult(HttpSession s) {
//    	Entityres er = ( Entityres ) s.getAttribute("er") ;
//		// DBへ登録するプログラム。
//		dao_rent.res_touroku( er) ;
//
//        return "reserve_result";
//    }
    
 // 編集ページ表示
    @GetMapping("/reserve_update")
    public String reserve_update(@RequestParam("id") int id, Model model) {
        Entityres res = dao_rent.getOrderById(id);  // 根据 id 查询数据
        model.addAttribute("res", res);
        return "reserve_update";
    }

//    // 更新処理
//    @PostMapping("/reserve_update_confirm")
//    public String reserve_update_confirm(
//        @RequestParam("res_id") int res_id,
//        @RequestParam("customer_id") int c_id,
//        @RequestParam("car_id") int car_id,
//        @RequestParam("rent_start") Date startTime,
//        @RequestParam("rent_end") Date endTime,
//        @RequestParam("status") String status) {
//
//
//        dao_rent.updateRes(res_id, c_id, car_id, startTime, endTime, status);
//        return "redirect:/reserve_input";  
//    }
    
    
    @PostMapping("/reserve_update_confirm") 
    public String reserve_update_confirm(
            @RequestParam("res_id") int res_id,
            @RequestParam("customer_id") int c_id,
            @RequestParam("car_id") int car_id,
            @RequestParam("rent_start") Date startTime,
            @RequestParam("rent_end") Date endTime,
            @RequestParam("status") String status,
            Model model) {

        // 检查车辆是否可用（排除当前预约）
        if (!dao_rent.isCarAvailableForUpdate(res_id, car_id, startTime, endTime)) {
            // 构建对象保留输入
            Entityres res = new Entityres();
            res.setRes_id(res_id);
            res.setC_id(c_id);
            res.setCar_id(car_id);
            res.setRent_sDate(startTime);
            res.setRent_eDate(endTime);
            res.setStatus(status);

            model.addAttribute("res", res);
            model.addAttribute("error", "この時間帯の車はすでに予約済みのため、登録できません。");

            // ⚠️ 直接返回视图名，不用 redirect
            return "reserve_error";  
        }

        dao_rent.updateRes(res_id, c_id, car_id, startTime, endTime, status);
        return "redirect:/reserve_input";
    }

}
    
    