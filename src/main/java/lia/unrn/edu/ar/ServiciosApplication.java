package lia.unrn.edu.ar;

import lia.unrn.edu.ar.model.Account;
import lia.unrn.edu.ar.model.Bookmark;
import lia.unrn.edu.ar.repository.AccountRepository;
import lia.unrn.edu.ar.repository.BookmarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.*;


import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

	@Configuration
	@ComponentScan
	@EnableAutoConfiguration
	public class ServiciosApplication {


		public static void main(String[] args) {
			SpringApplication.run(ServiciosApplication.class, args);
		}
	}
// end::runner[]

	@RestController
	@RequestMapping("/{userId}/bookmarks")
	class BookmarkRestController {

		private final BookmarkRepository bookmarkRepository;

		private final AccountRepository accountRepository;

		@RequestMapping(method = RequestMethod.POST)
		ResponseEntity<?> add(@PathVariable String userId, @RequestBody Bookmark input) {
			this.validateUser(userId);
			return this.accountRepository
					.findByUsername(userId)
					.map(account -> {
						Bookmark result = bookmarkRepository.save(new Bookmark(account,
								input.uri, input.description));

						HttpHeaders httpHeaders = new HttpHeaders();
						httpHeaders.setLocation(ServletUriComponentsBuilder
								.fromCurrentRequest().path("/{id}")
								.buildAndExpand(result.getId()).toUri());
						return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
					}).get();

		}

		@RequestMapping(value = "/{bookmarkId}", method = RequestMethod.GET)
		Bookmark readBookmark(@PathVariable String userId, @PathVariable Long bookmarkId) {
			this.validateUser(userId);
			return this.bookmarkRepository.findOne(bookmarkId);
		}

		@RequestMapping(method = RequestMethod.GET)
		Collection<Bookmark> readBookmarks(@PathVariable String userId) {
			this.validateUser(userId);
			return this.bookmarkRepository.findByAccountUsername(userId);
		}

		@Autowired
		BookmarkRestController(BookmarkRepository bookmarkRepository,
							   AccountRepository accountRepository) {
			this.bookmarkRepository = bookmarkRepository;
			this.accountRepository = accountRepository;
		}

		private void validateUser(String userId) {
			this.accountRepository.findByUsername(userId).orElseThrow(
					() -> new UserNotFoundException(userId));
		}
	}

@RestController
class LoginRestController {

	@Autowired
	private AccountRepository accountRepository;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	Optional<Account> login(@RequestParam("username")  String username,@RequestParam("password")  String password) {
			return this.accountRepository.findByUsernameAndPassword(username,password);
	}

}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	class UserNotFoundException extends RuntimeException {

		public UserNotFoundException(String userId) {
			super("could not find user '" + userId + "'.");
		}

	}
