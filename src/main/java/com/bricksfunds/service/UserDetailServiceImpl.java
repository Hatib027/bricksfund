package com.bricksfunds.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bricksfunds.entity.User;
import com.bricksfunds.repo.UserRepository;



@Service
public class UserDetailServiceImpl  implements UserDetailsService{

	    @Autowired
	    private UserRepository userRepository;

	    @Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        User user = this.userRepository.findByUsernameAndEnable(username,true);
	        if (user == null) {
	            System.out.println("User not found");
	            throw new UsernameNotFoundException("No user found !!");
	        }
	        return user;
	    }

}
