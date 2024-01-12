package com.bricksfunds.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name="users")
public class User implements UserDetails{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String  username;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String referralcode;
	private Boolean enable = false;
	private String otp;
	private String yourrefercode;
	private String dateofexpireotp;
	private String time;

	//User many roles
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER , mappedBy = "user")
	@JsonIgnore
	private Set<UserRole> userRoles = new HashSet<>();

	
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "userRefer",fetch = FetchType.LAZY)
	@JsonIgnore
	List<ReferMaster> referMaster =new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "user",fetch = FetchType.LAZY)
	@JsonIgnore
	List<InvestmentMaster> investmentMaster =new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "friendRefer",fetch = FetchType.LAZY)
	@JsonIgnore
	List<ReferMaster> referMaster1 =new ArrayList<>();
	
	

	public User() {
		
	}

	

	public User(Long id, Set<UserRole> userRoles,  String username, String password, String firstName, String lastName, String email,
			String phone,String referralcode, Boolean enable,String otp,String yourrefercode,String dateofexpireotp,String time) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.enable = enable;
		this.userRoles = userRoles;
		this.referralcode = referralcode;
		this.otp = otp;
		this.yourrefercode = yourrefercode;
		this.dateofexpireotp=dateofexpireotp;
		this.time=time;
	}
	
	

	public Set<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	
	
	


	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
	

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<Authority> set = new HashSet<>();
		this.userRoles.forEach(userRole -> {
			set.add(new Authority(userRole.getRole().getRoleName()));
		});
		return set;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return this.enable;
	}

	public String getReferralcode() {
		return referralcode;
	}

	public void setReferralcode(String referralcode) {
		this.referralcode = referralcode;
	}



	public String getOtp() {
		return otp;
	}



	public void setOtp(String otp) {
		this.otp = otp;
	}



	public String getYourrefercode() {
		return yourrefercode;
	}



	public void setYourrefercode(String yourrefercode) {
		this.yourrefercode = yourrefercode;
	}

	


	public String getDateofexpireotp() {
		return dateofexpireotp;
	}



	public void setDateofexpireotp(String dateofexpireotp) {
		this.dateofexpireotp = dateofexpireotp;
	}



	public String getTime() {
		return time;
	}



	public void setTime(String time) {
		this.time = time;
	}



	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", email=" + email + ", phone=" + phone + ", referralcode=" + referralcode
				+ ", enable=" + enable + ", userRoles=" + userRoles + "]";
	}
	
	
	
	
	
	
	

}
