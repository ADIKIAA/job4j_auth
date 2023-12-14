package ru.job4j.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.service.PersonService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/person")
@AllArgsConstructor
public class PersonController {

    private final PersonService personService;

    private final BCryptPasswordEncoder encoder;

    private final ObjectMapper objectMapper;

    @GetMapping("/")
    public List<Person> findAll() {
        return this.personService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        Optional<Person> person = this.personService.findById(id);
        if (person.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person is not found");
        }
        return new ResponseEntity<>(person.get(), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Person> create(@RequestBody Person person) {
        var username = person.getUsername();
        var password = person.getPassword();
        if (username == null || password == null) {
            throw new NullPointerException("Username and password mustn't be empty");
        }
        if (username.startsWith("_")) {
            throw new IllegalArgumentException("Username mustn't start with '_'");
        }
        person.setPassword(encoder.encode(person.getPassword()));
        var savedPerson = personService.save(person);
        if (savedPerson == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        var updatedPerson = personService.save(person);
        if (updatedPerson == null) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Person person = new Person();
        person.setId(id);
        boolean deleted = personService.delete(person);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }

    @ExceptionHandler(value = { IllegalArgumentException.class })
    public void exceptionHandler(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() { {
            put("message", e.getMessage());
            put("type", e.getClass());
        }}));
    }

}
