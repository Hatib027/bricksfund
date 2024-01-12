package com.bricksfunds.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bricksfunds.entity.ReferMaster;
import com.bricksfunds.entity.User;
import com.bricksfunds.service.ReferService;
import com.bricksfunds.service.UserService;

@RestController
@RequestMapping("/referral")
public class ReferralController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	ReferService referService;

	@PostMapping("/getYourRefer")
	public List<User> getYourRefer(@RequestBody String referCode){
		
	 List<User> getYourRefer =  this.userService.findyByFriendRefer(referCode,true);
	 
	 return getYourRefer;
	}
	
	@GetMapping("/getReferHistory")
	public List<ReferMaster> getReferHistory(HttpServletRequest request){
	Principal p = request.getUserPrincipal();
   User user = userService.getUser(p.getName());
	
  
	List<ReferMaster> referMaster = this.referService.getYourAmount(user);

	
	return referMaster;
	
	}
	
}
