package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

@Service
public class UserService {

    private final HashMap<Long, User> users = new HashMap<>();
    private final ArrayList<String> emails = new ArrayList<>();
    private Long id = 0L;


    public Collection<User> getUsers() {
        return users.values();
    }

    public User findUser(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException(id + " not found");
        } else return user;
    }

    public User createUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }
        if (emails.contains(user.getEmail())) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }
        user.setId(generateId());
        user.setRegistrationDate(Instant.now());
        emails.add(user.getEmail());
        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(User newUser) {
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            if (newUser.getEmail() != null)
                if (emails.contains(newUser.getEmail())) {
                    throw new DuplicatedDataException("Этот имейл уже используется");
                } else oldUser.setEmail(newUser.getEmail());
            if (newUser.getUsername() != null) {
                oldUser.setUsername(newUser.getUsername());
            }
            if (newUser.getPassword() != null) {
                oldUser.setPassword(newUser.getPassword());
            }
            return oldUser;
        }
        throw new NotFoundException("Пост с id = " + newUser.getId() + " не найден");
    }

    protected Optional<User> findUserById(Long userId) {
        if (users.get(userId) == null) {
            return Optional.empty();
        } else return Optional.of(users.get(userId));
    }

    private Long generateId() {
        return ++id;
    }
}
