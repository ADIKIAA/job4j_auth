package ru.job4j.auth.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.service.PersonService;

import java.util.List;

@RestController
@RequestMapping("/person")
@AllArgsConstructor
public class PersonController {

    private final PersonService personService;

    private final BCryptPasswordEncoder encoder;

    @GetMapping("/")
    public List<Person> findAll() {
        return this.personService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        var person = this.personService.findById(id);
        return new ResponseEntity<Person>(
                person.orElse(new Person()),
                person.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/")
    public ResponseEntity<Person> create(@RequestBody Person person) {
        ResponseEntity<Person> rsl;
        try {
            this.personService.save(person);
            rsl = ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            rsl = ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return rsl;
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        ResponseEntity<Void> rsl;
        try {
            this.personService.save(person);
            rsl = ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            rsl = ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return rsl;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        ResponseEntity<Void> rsl;
        try {
            Person person = new Person();
            person.setId(id);
            this.personService.delete(person);
            rsl = ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            rsl = ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return rsl;
    }

    @PostMapping("sign-up")
    public void signUp(@RequestBody Person person) {
        person.setPassword(encoder.encode(person.getPassword()));
        personService.save(person);
    }

}
