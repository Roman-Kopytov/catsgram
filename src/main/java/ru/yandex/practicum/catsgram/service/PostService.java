package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.exception.ParameterNotValidException;
import ru.yandex.practicum.catsgram.model.Post;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final Map<Long, Post> posts = new HashMap<>();
    private Long id = 0L;


    private final UserService userService;

    public PostService(UserService userService) {
        this.userService = userService;
    }

    public Collection<Post> findAll(long from, long size, String sort) {
        return posts.values().stream().sorted(getComparator(sort))
                .filter(post -> post.getId() > from)
                .limit(size)
                .collect(Collectors.toList());
    }

    public Comparator<Post> getComparator(String sort) {
        switch (sort.toLowerCase()) {
            case "ascending":
            case "asc":
                return new Comparator<Post>() {
                    @Override
                    public int compare(Post o1, Post o2) {
                        return o1.getPostDate().compareTo(o2.getPostDate());
                    }
                };
            case "descending":
            case "desc":
                return new Comparator<Post>() {
                    @Override
                    public int compare(Post o1, Post o2) {
                        return o2.getPostDate().compareTo(o1.getPostDate());
                    }
                };
            default:
                throw new ParameterNotValidException("sort", "Получено: " + sort + " должно быть: ask или desc");

        }
    }

    public Optional<Post> findPost(Long id) {
        Post post = posts.get(id);
        if (post == null) {
            return Optional.empty();
        } else return Optional.of(post);
    }

    public Post create(Post post) {
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }
        if (userService.findUserById(post.getAuthorId()).isEmpty()) {
            throw new ConditionsNotMetException("Автор с id = " + post.getAuthorId() + " не найден");
        }
        post.setId(getNextId());
        post.setPostDate(Instant.now());
        posts.put(post.getId(), post);
        return post;
    }

    public Post update(Post newPost) {
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }


    private long getNextId() {
        return ++id;
    }
}