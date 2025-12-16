package com.example.demo.controller;

import java.util.ArrayList;

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

import com.example.demo.model.DAO_customer;
import com.example.demo.model.DAO_rent;
import com.example.demo.model.Entitycar;
import com.example.demo.model.Entitycostmer;
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

    @PostMapping("/customer/update")
    public String customerUpdate(
            @RequestParam("customerId") int customerId,
            Model model) {

        Entitycostmer customer = daoCustomer.findById(customerId);
        model.addAttribute("customer", customer);

        return "customer_update";
    }
    @PostMapping("/customer/update/confirm")
    public String customerUpdateConfirm(
            @RequestParam("customerId") int customerId,
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam("address") String address,
            Model model) {

        // 入力された内容（更新後）
        Entitycostmer newCustomer =
                new Entitycostmer(customerId, name, phone, email, address);

        model.addAttribute("customer", newCustomer);

        return "customer_update_confirm";
    }
 // 更新
    @PostMapping("/customer/update/result")
    public String customerUpdateResult(
            @ModelAttribute Entitycostmer customer,
            Model model) {

        // DB更新
        daoCustomer.update(customer);

        model.addAttribute("customer", customer);
        return "customer_update_result";
    }



    @PostMapping("/customer/delete/confirm")
    public String customerDeleteConfirm(
            @RequestParam("customerId") int customerId,
            Model model) {

        Entitycostmer customer = daoCustomer.findById(customerId);
        model.addAttribute("customer", customer);

        return "customer_delete_confirm";
    }

    @PostMapping("/customer/delete/result")
    public String customerDeleteResult(
            @RequestParam("customerId") int customerId,
            Model model) {

        // 削除実行
        daoCustomer.delete(customerId);

        // 完了メッセージ用
        model.addAttribute("customerId", customerId);

        return "customer_delete_result";
    }



    @GetMapping("/customer/register")
    public String customerRegister() {
        return "customer_register";
    }





    // 登録確認
    @PostMapping("/customer/register/confirm")
    public String customerRegisterConfirm(
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam("address") String address,
            Model model) {

        Entitycostmer customer =
                new Entitycostmer(0, name, phone, email, address);

        model.addAttribute("customer", customer);
        return "customer_register_confirm";
    }


    // 登録実行
    @PostMapping("/customer/register/result")
    public String customerRegisterResult(
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam("address") String address) {

        Entitycostmer customer =
                new Entitycostmer(0, name, phone, email, address);

        daoCustomer.insert(customer);

        return "customer_register_result";
    }
   




    /******************************************************************
     * CAR（車両管理）ブロック
     * 担当：C
     ******************************************************************/
    @PostMapping( "/car_info" )
	public String car_info() {
  		
  		//全件表示
		return "car_info";
     }
  	
  	@RequestMapping( "/all_select" )
	public String all_select( Model m ) {

  		ArrayList < Entitycar > all_car = dao_rent.zenken_kensaku( ) ;
        m.addAttribute( "all_car" , all_car ) ;
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
>>>>>>> branch 'master' of https://github.com/kiaiTei/rent_car.git

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
        session.setAttribute("statusText", statusText);

        return "car_input_confirm";
    }

    @RequestMapping("/car_insert_result")
    public String carInsertResult(HttpSession session) {
        Entitycar ec = (Entitycar) session.getAttribute("ec");
        dao_rent.car_touroku(ec);
        return "car_insert_result";
    }

<<<<<<< HEAD
=======
   
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
