package com.sh.coffeeshop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("admin").password("12345").roles("ADMIN");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .and()
                .formLogin()
                .loginPage("/coffeeList") // Specifies the login page URL
                .loginProcessingUrl("/signin") // Specifies the URL,which is used
                //in action attribute on the <from> tag
                .usernameParameter("userid")  // Username parameter, used in name attribute
                // of the <input> tag. Default is 'username'.
                .passwordParameter("passwd")  // Password parameter, used in name attribute
                // of the <input> tag. Default is 'password'.
                .successHandler((req,res,auth)->{    //Success handler invoked after successful authentication
                    res.sendRedirect("admin/listOrder"); // Redirect user to index/home page
                })
                .failureHandler((req,res,exp)->{  // Failure handler invoked after authentication failure
                    String errMsg="";
                    if(exp.getClass().isAssignableFrom(BadCredentialsException.class)){
                        errMsg="Invalid username or password.";
                    }else{
                        errMsg="Unknown error - "+exp.getMessage();
                    }
                    req.getSession().setAttribute("message", errMsg);
                    res.sendRedirect("./coffeeList"); // Redirect user to login page with error message.
                })
                .permitAll() // Allow access to any URL associate to formLogin()
                .and()
                .logout()
                .logoutUrl("/signout")   // Specifies the logout URL, default URL is '/logout'
                .logoutSuccessHandler((req,res,auth)->{   // Logout handler called after successful logout
                    req.getSession().setAttribute("message", "You are logged out successfully.");
                    res.sendRedirect("coffeeList"); // Redirect user to login page with message.
                })
                .permitAll() // Allow access to any URL associate to logout()
                .and()
                .csrf().disable(); // Disable CSRF support
    }
}