package com.example.demo.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.DAO_customer;
import com.example.demo.model.DAO_rent;
import com.example.demo.model.Entitycar;
import com.example.demo.model.Entitycostmer;
import com.example.demo.model.Entityrent_car;
import com.example.demo.model.Entityres;
import com.example.demo.service.CustomerService;

@Controller
public class FrontController {

    /************** フィールド（※ここは統合担当のみ編集） **************/
    private final DAO_rent dao_rent;
    private final DAO_customer dao_cus;
    private final CustomerService customerService;

    public FrontController(DAO_rent dao_rent,
                            CustomerService customerService,DAO_customer dao_cus) {
        this.dao_rent = dao_rent;
        this.dao_cus = dao_cus;
        this.customerService = customerService;
    }
    
    
    /******************************************************************
     * Customer Login
     * 担当：A
     ******************************************************************/
    @RequestMapping( "/customer_register" )
	public String customer_register( @ModelAttribute Entitycostmer ec, HttpSession s ) {
		s.setAttribute( "ec" , ec ) ;
		return "customer_register" ;
		
	}

	@RequestMapping( "/customer_register_result" )
	public String customer_register_result( HttpSession s ) {

		Entitycostmer ec = ( Entitycostmer ) s.getAttribute("ec") ;
		// DBへ登録するプログラム。
		dao_cus.insert( ec ) ;
		System.out.println(ec.getPassword());

		return "customer_register_result" ;
		
	}
	
	@GetMapping("/login_customer")
    public String login_customer() {
        return "login_customer";
    }

    @PostMapping("/login_customer")
    public String login_customer(HttpServletRequest r, HttpSession s) {

        String idStr = r.getParameter("id");
        String pass = r.getParameter("password");

        if (idStr == null || idStr.isEmpty()) {
            r.setAttribute("msg", "IDを入力してください");
            return "login_customer_re";
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            r.setAttribute("msg", "IDは数字で入力してください");
            return "login_customer_re";
        }

        String pw = dao_cus.cus_login(id);
        if (pw != null && pw.equals(pass)) {
            s.setAttribute("logincustomer", id);
            return "redirect:/login_customer_result";
        }

        r.setAttribute("msg", "IDまたはパスワードが間違っています");
        return "login_customer_re";
    }
    
    @GetMapping("/login_customer_result")
    public String login_customer_result(HttpSession session, Model model) {
    	Integer customerId = (Integer) session.getAttribute("logincustomer");
        if (customerId == null) {
            return "redirect:/login_customer";
        }

        model.addAttribute("loginId", customerId);
        return "login_customer_result";
    }
    
    /***************************************************************************/
    @RequestMapping("/customer_info_login")
    public String customer_info_login(HttpSession session, Model model) {
    	 Integer customerId = (Integer) session.getAttribute("logincustomer");
    	    if (customerId == null) {
    	        return "redirect:/login_customer";
    	    }

    	    Entitycostmer customer = daoCustomer.findByCustomerId(customerId);
    	    if (customer == null) {
    	        model.addAttribute("msg", "情報未登録");
    	        return "error";
    	    }

    	    model.addAttribute("customer", customer);
    	    return "customer_info_login";
    }
    
    @GetMapping("/customer_update_login")
    public String customer_update_login(@RequestParam("id") int id, Model model) {
        Entitycostmer cu = dao_cus.getCusById(id);
        model.addAttribute("cu", cu);
        return "customer_update_login";  // Thymeleaf 更新页面
    }
    @PostMapping("/customer_update_confirm_login")
    public String customer_update_confirm_login(@RequestParam("customerId") int customer_id,
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam("address") String address,
            @RequestParam("password") String password,
            Model model) 
           {
			dao_cus.update(customer_id, name, phone, email, address, password);
			 Entitycostmer cu =
			            new Entitycostmer(customer_id, name, phone, email, address, password);

			    // 放进 model
			    model.addAttribute("cu", cu);
			return "customer_update_result_login"; 
           }// 更新後全件表示に戻る
    
    
    
    
    
    
    
    
    
    @GetMapping("/reserve_input_login")
    public String reserveInputLogin(HttpSession session, Model model) {
        Integer customerId = (Integer) session.getAttribute("logincustomer");
        if (customerId == null) return "redirect:/login_customer";

        model.addAttribute("loginId", customerId);

        List<Entityres> myReservations = dao_cus.findReservationsByCustomerId(customerId);
        model.addAttribute("myReservations", myReservations);

        return "reserve_input_login";
    }

    @PostMapping("/reserve_confirm_login")
    public String reserveConfirm(@RequestParam int car_id,
                                 @RequestParam Date rent_sDate,
                                 @RequestParam Date rent_eDate,
                                 @RequestParam String status,
                                 HttpSession session,
                                 Model model) {

        Integer customerId = (Integer) session.getAttribute("logincustomer");
        if (customerId == null) return "redirect:/login_customer";

        Entityres er = new Entityres(0, customerId, car_id, rent_sDate, rent_eDate, status);
        session.setAttribute("er", er);

        model.addAttribute("er", er);
        return "reserve_confirm_login"; // 显示确认页面
    }

    @PostMapping("/reserve_result_login")
    public String reserveResultLogin(HttpSession session, Model model) {
        Entityres er = (Entityres) session.getAttribute("er");
        if (er == null) return "redirect:/reserve_input_login";

        if (!dao_cus.isCarAvailable(er.getCar_id(), er.getRent_sDate(), er.getRent_eDate())) {
            model.addAttribute("error", "この時間帯の車はすでに予約済みです。");
            return "reserve_error_login";
        }

        dao_cus.insertReservation(er);
        model.addAttribute("er", er); 
        session.removeAttribute("er");
        return "reserve_result_login";
    }

    @GetMapping("/reserve_error_login")
    public String reserveErrorLogin() {
        return "reserve_error_login";
    }
    
    
    
    @GetMapping("/reserve_update_login")
    public String reserveUpdateLogin(@RequestParam("id") int resId, HttpSession session, Model model) {
        Integer loginId = (Integer) session.getAttribute("logincustomer");
        if (loginId == null) return "redirect:/login_customer";

        Entityres res = dao_rent.getOrderById(resId);
        if (res == null || res.getC_id() != loginId) {
            return "redirect:/reserve_input_login"; // 只能修改自己的预约
        }

        model.addAttribute("res", res);
        return "reserve_update_login";
    }

    // 修改提交
    @PostMapping("/reserve_update_confirm_login")
    public String reserveUpdateConfirmLogin(
            @RequestParam("res_id") int res_id,
            @RequestParam("c_id") int c_id,
            @RequestParam("car_id") int car_id,
            @RequestParam("rent_sDate") Date rent_sDate,
            @RequestParam("rent_eDate") Date rent_eDate,
            @RequestParam("status") String status,
            HttpSession session,
            Model model) {

        Integer loginId = (Integer) session.getAttribute("logincustomer");
        if (loginId == null || loginId != c_id) return "redirect:/login_customer";

        // 检查车辆是否可用（排除当前预约）
        if (!dao_rent.isCarAvailableForUpdate(res_id, car_id, rent_sDate, rent_eDate)) {
            Entityres res = new Entityres(res_id, c_id, car_id, rent_sDate, rent_eDate, status);
            model.addAttribute("res", res);
            model.addAttribute("error", "この時間帯の車はすでに予約済みのため、登録できません。");
            return "reserve_error_login";  
        }

        dao_rent.updateRes(res_id, c_id, car_id, rent_sDate, rent_eDate, status);
        return "redirect:/reserve_input_login";
    }
    
    

    @GetMapping("/reserve_delete_confirm_login")
    public String reserveDeleteConfirmLogin(@RequestParam("id") int resId,
                                            HttpSession session,
                                            Model model) {
        Integer loginId = (Integer) session.getAttribute("logincustomer");
        if (loginId == null) return "redirect:/login_customer";

        Entityres res = dao_rent.getOrderById(resId);
        if (res == null || res.getC_id() != loginId) {
            return "redirect:/reserve_input_login"; // 只能删除自己的
        }

        model.addAttribute("res", res);
        return "reserve_delete_confirm_login";
    }

    // 执行删除
    @PostMapping("/reserve_delete_result_login")
    public String reserveDeleteResultLogin(@RequestParam("res_id") int resId,
                                           HttpSession session,
                                           RedirectAttributes redirectAttributes) {
        Integer loginId = (Integer) session.getAttribute("logincustomer");
        if (loginId == null) return "redirect:/login_customer";

        Entityres res = dao_rent.getOrderById(resId);
        if (res != null && res.getC_id() == loginId) {
            dao_rent.deleteById(resId);
            redirectAttributes.addFlashAttribute("successMsg", "予約を削除しました。");
        }

        return "redirect:/reserve_input_login";
    }




    /***************************************************************************/

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
    @Autowired
    private DAO_customer daoCustomer;
    
    @GetMapping("/customer")
    public String customer() {
    	
        return "customer";
    }
    
    @RequestMapping("/customer_info")
    public String customerInfo(Model m) {
    	 ArrayList<Entitycostmer> all_customer = daoCustomer.zenken_kensaku();
         m.addAttribute("all_customer", all_customer);
        return "customer_info";
    }
    

    //***************** 更新処理********************************************//
    @GetMapping("/customer_update")
    public String customerUpdate(@RequestParam("id") int id, Model model) {
        Entitycostmer cu = dao_cus.getCusById(id);
        model.addAttribute("cu", cu);
        return "customer_update";  // Thymeleaf 更新页面
    }
    @PostMapping("/customer_update_confirm")
    public String customerUpdateConfirm(@RequestParam("customerId") int customer_id,
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam("address") String address,
            @RequestParam("password") String password,
            Model model) 
           {
			dao_cus.update(customer_id, name, phone, email, address, password);
			 Entitycostmer cu =
			            new Entitycostmer(customer_id, name, phone, email, address, password);

			    // 放进 model
			    model.addAttribute("cu", cu);
			return "customer_update_result"; 
           }// 更新後全件表示に戻る


 // 更新
//    @PostMapping("/customer/update/result")
//    public String customerUpdateResult(
//            @ModelAttribute Entitycostmer customer,
//            Model model) {
//
//        // DB更新
//        //dao_cus.update(customer);
//
//        model.addAttribute("customer", customer);
//        return "customer_update_result";
//    }


//    @PostMapping("/customer_delete_confirm")
//    public String customerDeleteConfirm(
//            @RequestParam("customerId") int id,
//            Model model) {
//         return "customer_delete_confirm";
//    }
    @PostMapping("/customer_delete_confirm")
    public String customerDelete(@RequestParam("customerId") int id,
                                 RedirectAttributes redirectAttributes) {

        dao_cus.cus_deleteById(id);

        redirectAttributes.addFlashAttribute(
                "message", "顧客ID " + id + " を削除しました");

        return "redirect:/customer_delete_result";
    }
    
    @GetMapping("/customer_delete_result")
    public String customerDeleteResult() {
        return "customer_delete_result";
    }
 
    @GetMapping("/customer_register")
    public String customerRegister() {
        return "customer_register";
    }





    // 登録確認
    @PostMapping("/customer_register_confirm")
    public String customerRegisterConfirm(
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam("address") String address,
            @RequestParam("password") String password,
            Model model) {

        Entitycostmer customer =
                new Entitycostmer(name, phone, email, address,password);

        model.addAttribute("customer", customer);
        return "customer_register_confirm";
    }


    // 登録実行
    @PostMapping("/customer_register_result")
    public String customerRegisterResult(Entitycostmer customer, Model model) {

        daoCustomer.insert(customer);

        model.addAttribute("customer", customer);

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
    
    
    @PostMapping("/car_delete_result")
    public String car_delete_result(@RequestParam("id") int id) {
        dao_rent.car_deleteById(id);
        return "/car_delete_success";
    }
    
    @GetMapping("/car_delete_success")
    public String car_delete_success() {
        return "reserve_delete_success";
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

            return "reserve_error";  
        }

        dao_rent.updateRes(res_id, c_id, car_id, startTime, endTime, status);
        return "redirect:/reserve_input";
    }
    
    
    
    @GetMapping("/reserve_info")
    public String reserve_info(@RequestParam("id") int rentId, Model model) {
        Entityrent_car rentWithCar = dao_rent.getRentWithCarById(rentId);
        model.addAttribute("rent", rentWithCar);
        return "reserve_info";  // Thymeleaf 页面
    }
    
//    @PostMapping("/reserve_delete_confirm")
//    public String reserve_delete_confirm(
//            @RequestParam("id") int id,
//            Model model,
//            HttpSession session) {
//
//        Entityres ev = dao_rent.getOrderById(id);
//
//        if (ev == null) {
//            return "not_found";
//        }
//
//        model.addAttribute("sel_order", ev);
//        model.addAttribute("sid", id);
//
//        return "reserve_delete_confirm";
//    }
	
    @PostMapping("/reserve_delete_result")
    public String reserve_delete_result(
            @RequestParam("id") int id,
            RedirectAttributes redirectAttributes) {

        dao_rent.deleteById(id);

        // 传递成功消息
        redirectAttributes.addFlashAttribute("deleteSuccess", true);

        return "redirect:/reserve_delete_success";
    }
    
    @GetMapping("/reserve_delete_success")
    public String reserve_delete_success() {
        return "reserve_delete_success";
    }
    
}

    
    