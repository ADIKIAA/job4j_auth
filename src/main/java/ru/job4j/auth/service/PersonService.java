package ru.job4j.auth.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public List<Person> findAll() {
        return findAll();
    }

    public Optional<Person> findById(int id) {
        return personRepository.findById(id);
    }

    public Optional<Person> findByUsername(String username) {
        return personRepository.findByUsername(username);
    }

    public Person save(Person person) {
        Person rsl;
        try {
            rsl = personRepository.save(person);
        } catch (Exception e) {
            e.printStackTrace();
            rsl = null;
        }
        return rsl;
    }

    public boolean delete(Person person) {
        boolean rsl = true;
        try {
            personRepository.delete(person);
        } catch (Exception e) {
            e.printStackTrace();
            rsl = false;
        }
        return rsl;

    }

}
