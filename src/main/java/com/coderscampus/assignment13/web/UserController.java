package com.coderscampus.assignment13.web;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.Address;
import com.coderscampus.assignment13.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private AddressService addressService;

	@GetMapping("/register")
	public String getCreateUser (ModelMap model) {
		model.put("user", new User());
		return "register";
	}

	@PostMapping("/register")
	public String postCreateUser (User user) {
		userService.saveUser(user);
		return "redirect:/register";
	}

	@GetMapping("/users")
	public String getAllUsers (ModelMap model) {
		Set<User> users = userService.findAll();
		model.put("users", users);
		if (users.size() == 1) {
			model.put("user", users.iterator().next());
		}
		return "users";
	}

	@GetMapping("/users/{userId}")
	public String getOneUser (ModelMap model, @PathVariable Long userId) {
		User user = userService.findById(userId);
		model.put("users", Arrays.asList(user));
		model.put("user", user);
		return "users";
	}

	@PostMapping("/users/{userId}")
	public String postOneUser (User user, @PathVariable Long userId) {
		User existingUser = userService.findByIdWithAccounts(userId);
		user.setAccounts(existingUser.getAccounts());
		if (existingUser.getAddress() == null) {
			Address newAddress = user.getAddress();
			newAddress.setUser(existingUser);
			newAddress.setUserId(user.getUserId());
			existingUser.setAddress(newAddress);
			userService.saveUser(existingUser);
			return "redirect:/users/"+user.getUserId();
		}
		Address address = user.getAddress();
		address.setUser(user);
		address.setUserId(userId);
		addressService.save(user.getAddress());
		user.setAddress(address);
		userService.saveUser(user);
		return "redirect:/users/"+user.getUserId();
	}

	@GetMapping("/users/{userId}/accounts/{accountId}")
	public String getOneUserAccounts (ModelMap model, @PathVariable Long userId, @PathVariable Long accountId) {
		Account account = userService.findAccountById(accountId);
        User user = userService.findById(userId);
        model.put("account", account);
        model.put("user", user);
        return "account";
	}

	@PostMapping("/users/{userId}/delete")
	public String deleteOneUser (@PathVariable Long userId) {
		userService.delete(userId);
		return "redirect:/users";
	}

	@PostMapping("/users/{userId}/accounts/{accountId}")
	public String postOneUserAccounts (Account account, @PathVariable Long accountId, @PathVariable Long userId) {
		userService.saveExistingAccount(account, userId);
        return "redirect:/users/{userId}/accounts/" + account.getAccountId();
    }

	@PostMapping("/users/{userId}/accounts")
	public String getOneUserAccounts (ModelMap model, @PathVariable Long userId) {
		Account account = userService.saveNewAccount(userId);
		User user = userService.findById(userId);
		return "redirect:/users/{userId}/accounts/" + account.getAccountId();
	}
}
