package lia.unrn.edu.ar.rest;

import lia.unrn.edu.ar.model.Account;
import lia.unrn.edu.ar.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
class LoginRestController {

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    Optional<Account> login(@RequestParam("username")  String username, @RequestParam("password")  String password) {
        return this.accountRepository.findByUsernameAndPassword(username,password);
    }

    private void validateUser(String userId) {
        this.accountRepository.findByUsername(userId).orElseThrow(
                () -> new UserNotFoundException(userId));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    class UserNotFoundException extends RuntimeException {

        public UserNotFoundException(String userId) {
            super("could not find user '" + userId + "'.");
        }

    }

}