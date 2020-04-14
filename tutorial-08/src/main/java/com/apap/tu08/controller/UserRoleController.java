package com.apap.tu08.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.apap.tu08.model.PasswordModel;
import com.apap.tu08.model.UserRoleModel;
import com.apap.tu08.repository.UserRoleDb;
import com.apap.tu08.service.UserRoleService;

@Controller
@RequestMapping("/user")
public class UserRoleController {
	@Autowired
    private UserRoleService userService;
	
	@Autowired
    private UserRoleDb userRoleDb;
	
	 @RequestMapping(value = "/addUser", method = RequestMethod.POST)
	    private String addUserSubmit(@ModelAttribute UserRoleModel user) {
		 userService.addUser(user);
		 return "home";
	 }
	 
	 @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	    private String changePassword(Principal principal, @ModelAttribute PasswordModel password, Model model) {
	        UserRoleModel userSekarang = userRoleDb.findByUsername(principal.getName());

	        PasswordEncoder bcpe = new BCryptPasswordEncoder();

	        Boolean pencocokan1 = bcpe.matches(password.getPasswordLama(), userSekarang.getPassword());
	        Boolean pencocokan2 = password.getPasswordBaru().equals(password.getKonfirmasiPassword());
	        
	        if (pencocokan1 && pencocokan2) {
	        	UserRoleModel user = new UserRoleModel();
	        	
	            user.setId(userSekarang.getId());
	            user.setUsername(userSekarang.getUsername());
	            user.setRole(userSekarang.getRole());
	            user.setPassword(password.getPasswordBaru());
	            
	            userService.addUser(user);
	            
	            model.addAttribute("success", "Success Change Password!");
	        }else {
	        	List <String> pesan = new ArrayList<String>();
	            
	        	if (!pencocokan1) {
	        		pesan.add("Passwrod Salah");
	            }
	            if (!pencocokan2) {
	            	pesan.add("Password Tidak Cocok");
	            }
	            model.addAttribute("error", pesan);
	            
	        }
	        return "update-password";
	    }
}
