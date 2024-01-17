package com.bricksfunds.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bricksfunds.entity.ForgotPassword;
import com.bricksfunds.entity.Role;
import com.bricksfunds.entity.User;
import com.bricksfunds.entity.UserRole;
import com.bricksfunds.repo.UserRepository;
import com.bricksfunds.service.EmailService;
import com.bricksfunds.service.UserService;
import com.bricksfunds.service.UserServiceImpl;



@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	@Autowired
	 BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	EmailService emailService;
	
	int otps= 0;
	
	 
   
	
	//creating user
	
	@PostMapping("/addUser")
	public User createUser(@RequestBody User user,HttpServletRequest httpServletRequest) throws Exception {
		
		System.out.println(user);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		Set<UserRole> roles = new HashSet<>();
		Role role = new Role();
		role.setRoleId(2L);
		role.setRoleName("NORMAL");
		
		UserRole userRole = new UserRole();
		userRole.setUser(user);
		userRole.setRole(role);
		HttpSession httpSession = httpServletRequest.getSession(true);
		roles.add(userRole);
		System.out.println("Role Added");
		User userRefer = userService.findByYourReferCode(user.getReferralcode());
		System.out.println(userRefer);
		if(userRefer == null) {
			throw new Exception("Refer Code Wrong");
		}
		
		List<User> mailcheck =userService.getUserByMail(user.getEmail());
		System.out.println("mail Check" + mailcheck);
		User users = new User();
		if(!mailcheck.isEmpty())
		{
			users	 = mailcheck.get(0);
		}
		
		
		User userMain =userService.getUser(user.getUsername());
		System.out.println("Near User main"+userMain);
		
		
		if(userMain != null && userMain.getEnable() == true) {
			throw new Exception("User Already Found");
		}
		else if(!mailcheck.isEmpty() && users.getEnable() == true) {
			throw new Exception("Mail Already Registered");
		}
		else {
			
			String numbers = "0123456789";
			Random rndm_method = new Random();
			
			char[] jtp = new char[6];
			for (int i = 0; i < 6; i++) {
				jtp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
			}
			Integer otp = Integer.parseInt(new String(jtp));
			httpSession.setAttribute("otp", otp);
			String message = "" + "<div style='border:1px solid #e2e2e2;padding:20px;'>" + "<h1>" + "OTP is : " + "<b>"
					+ otp + "</b>" + "</h1>" + "</div>";

			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
			
			
			SimpleDateFormat d = new SimpleDateFormat("dd-MM-yyyy");
			
			 String time = dtf.format(LocalDateTime.now()).toString();
			 
			if(userMain != null && userMain.getEnable() == false) {
				user.setId(userMain.getId());
				user.setOtp(otp.toString());
			
			
			    user.setDateofexpireotp(d.format(new Date()));
			    user.setTime(time);
				userMain = user;
				
				userService.createUser(userMain, null);
			}else {
				 user.setDateofexpireotp(d.format(new Date()));
				   user.setTime(time);
				user.setOtp(otp.toString());
				userService.createUser(user, roles);
			}
			
			

			
			boolean flag = this.emailService.sendEmail("Otp For Register", message, user.getEmail());
			
			return user;
		}
	}
	
	@PostMapping("/verify-otp")
	public void verifyOtp(@RequestBody Map<String, String> json) throws Exception {
		String username = json.get("username").toString();
		User userMain =userService.getUser(username);
		
		
         SimpleDateFormat d = new SimpleDateFormat("dd-MM-yyyy");
		
		String datesss1 =userMain.getDateofexpireotp();
		String datesss2 = d.format(new Date());
		if(!datesss1.equals(datesss2)) {
			throw new Exception();
		}
		  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
	        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
	        
	        String time = dtf.format(LocalDateTime.now()).toString();
	        Date date1 = simpleDateFormat.parse(time);
	        
	        String time1 =userMain.getTime();
	        Date date2 = simpleDateFormat.parse(time1);
	        
	        long differenceInMilliSeconds = Math.abs(date2.getTime() - date1.getTime());

	        long differenceInMinutes = (differenceInMilliSeconds / (60 * 1000)) % 60;
	        
	        if(differenceInMinutes > 20) {
	        	throw new Exception();
	        }
		
		int otpByUser = Integer.parseInt(json.get("otp").toString());
		
		int otpByData =Integer.parseInt(userMain.getOtp());
		
		if(otpByUser == otpByData) {
			userMain.setEnable(true);
			userMain.setYourrefercode("bricksfunds/"+userMain.getId());
			userMain.setOtp("");
			userMain.setDateofexpireotp("");
			userMain.setTime("");
			userService.createUser(userMain, null);
		}else {
			throw new Exception("Otp Wrong");
		}
		
		
		
		
	}
	
	@GetMapping("/{username}")
	public User getUser(@PathVariable("username") String username) {
		
		
		return userService.getUser(username);
		
	}

	
	//delete user by id
	
	@DeleteMapping("/{userId}")
	public void deleteUser(@PathVariable("userId") Long userId) {
		
	   this.userService.deleteUser(userId);
	}
	

	
	
	@PostMapping("/email-verify")
	public String mailVerify(@RequestBody String email) throws Exception  {
		
		
		List<User> user = this.userService.getUserByMail(email);
		
		if(user.isEmpty()) {
			throw new Exception();
		}
		
	
	
		 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		ForgotPassword p = this.userServiceImpl.findByEmailForgot(email);
		
		SimpleDateFormat d = new SimpleDateFormat("dd-MM-yyyy");
		
		 String time = dtf.format(LocalDateTime.now()).toString();
		
		String token ="";
		if(p == null) {
			ForgotPassword ps = new ForgotPassword();
			ps.setEmail(email);
			token =HelperEncrypt.getSaltString();
			ps.setToken(token);
			ps.setExpire(d.format(new Date()));
			ps.setTime(time);
			this.userServiceImpl.addForgotData(ps);
		}else {
			token = HelperEncrypt.getSaltString();
			p.setToken(token);
			p.setExpire(d.format(new Date()));
			p.setTime(time);
			this.userServiceImpl.addForgotData(p);
		}
		
	

		
		
		
		
		
		String s ="https://bricksfunds.web.app/change-password/"+token;
		String message = "" + "<div style='border:1px solid #e2e2e2;padding:20px;'>" + "<h2>" + "Welcome To Bricks-Funds "  + "</h2>"+
		"<br>"+"<a style='cursor:pointer;' href= "+s+">"+
		"<button style='background-color:#03A9F4;width:150px;height:50px;border-radius:25px;justify-content:center;margin:auto;color:white;border:1px solid white;'>"
				+"Verify-Email"  + "</button>" +"</a>" +"</div>";
		
	//	String message = "" + "<div style='border:1px solid #e2e2e2;padding:20px;'>" + "<h1>" + "OTP is :" + "<b>"
	//			+ "<a href='google.com'>Hatib</a>" + "</b>" + "</h1>" + "</div>";
		
		boolean flag = this.emailService.sendEmail("Verify-Email", message, email);
		return "";
	}

	
	@PostMapping("/change-password")
	public void changePassword(@RequestBody Map<String,String> s) throws Exception {
		
		
		    
		ForgotPassword f = this.userServiceImpl.findByTokenForgot(s.get("token"));
		
		if(f == null) {
			throw new Exception();
		}
		
		SimpleDateFormat d = new SimpleDateFormat("dd-MM-yyyy");
		
		String datesss1 =f.getExpire();
		String datesss2 = d.format(new Date());
		if(!datesss1.equals(datesss2)) {
			throw new Exception();
		}
		 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
	        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
	        
	        String time = dtf.format(LocalDateTime.now()).toString();
	        Date date1 = simpleDateFormat.parse(time);
	        
	        String time1 = f.getTime();
	        Date date2 = simpleDateFormat.parse(time1);
	        
	        long differenceInMilliSeconds = Math.abs(date2.getTime() - date1.getTime());

	        long differenceInMinutes = (differenceInMilliSeconds / (60 * 1000)) % 60;
	        
	        if(differenceInMinutes > 15) {
	        	throw new Exception();
	        }
		List<User> users =  this.userService.getUserByMail(f.getEmail());
		
		User user = users.get(0);
		user.setPassword(passwordEncoder.encode(s.get("password")));
		
		this.userService.createUser(user, null);
	}


	
 
}
