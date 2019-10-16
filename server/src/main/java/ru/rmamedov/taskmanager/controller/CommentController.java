package ru.rmamedov.taskmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rmamedov.taskmanager.model.DTO.CommentProjection;
import ru.rmamedov.taskmanager.service.CommentService;

import java.util.Set;

/**
 * @author Rustam Mamedov
 */


@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping(value = "/all/taskId/{taskId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Set<CommentProjection>> findAllByUserId(@PathVariable final String taskId) {
        return new ResponseEntity<>(commentService.findAll(taskId), HttpStatus.OK);
    }

}
