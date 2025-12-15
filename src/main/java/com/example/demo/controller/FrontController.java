package com.example.demo.controller;

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
    @RequestMapping("/car_input_confirm")
    public String carInputConfirm(@ModelAttribute Entitycar ec,
                                  HttpSession session) {

        session.setAttribute("ec", ec);

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
