package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Customer;
import com.example.demo.model.DAO_rent;
import com.example.demo.service.CustomerService;

@Controller
public class FrontController {

    private DAO_rent dao_rent;

    @Autowired
    private CustomerService customerService;

    public FrontController(DAO_rent dao_rent) {
        this.dao_rent = dao_rent;
    }

    /******************* 従業員ログイン *******************/
    @PostMapping("/login_employee")
    public String login_employee(HttpServletRequest r, HttpSession s) {

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
            s.setAttribute("loginEmployee", in_id);
            return "redirect:/employee_exclusive";
        } else {
            r.setAttribute("msg", "IDまたはパスワードが間違っています");
            return "login_employee_re";
        }
    }

    /******************* 顧客一覧表示 *******************/
    @GetMapping("/customer_info")
    public String customer_info(Model model) {

        List<Customer> customerList = customerService.findAll();
        model.addAttribute("customerList", customerList);

        return "customer_info";
    }

    /******************* 顧客更新画面へ *******************/
    @PostMapping("/customer_update")
    public String customer_update(@RequestParam("customerId") int customerId, Model model) {

        Customer customer = customerService.findById(customerId);
        model.addAttribute("customer", customer);

        return "customer_update";
    }

    /******************* 顧客削除確認画面へ *******************/
    @PostMapping("/customer_delete")
    public String customer_delete(@RequestParam("customerId") int customerId, Model model) {

        Customer customer = customerService.findById(customerId);
        model.addAttribute("customer", customer);

        return "customer_delete_confirm";
    }

    /******************* 画面遷移系（そのまま） *******************/
    @PostMapping("/customer_register")
    public String customer_register() {
        return "customer_register";
    }

    @PostMapping("/customer_register_confirm")
    public String customer_register_confirm() {
        return "customer_register_confirm";
    }

    @PostMapping("/customer_register_result")
    public String customer_register_result() {
        return "customer_register_result";
    }
    
    @GetMapping("/employee_exclusive")
    public String employee_exclusive(HttpSession session) {

        if (session.getAttribute("loginEmployee") == null) {
            return "redirect:/login_employee";
        }

        return "employee_exclusive";
    }
    
    @GetMapping("/login_employee")
    public String login_employee_form() {
        return "login_employee";
    }
    
    @GetMapping("/reserve_input")
    public String reserve_input(HttpSession session, Model model) {

        // ログインチェック
        if (session.getAttribute("loginEmployee") == null) {
            return "redirect:/login_employee";
        }

        // 一覧表示用（今は空でOK。後でDAO接続）
        model.addAttribute("rentList", List.of());

        return "reserve_input";
    }



}