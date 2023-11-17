package main;

import main.model.Task;
import main.model.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/tasks")
    public ResponseEntity<String> createTask(@RequestParam String title,
                                             @RequestParam String description) {
        Task newTask = new Task();
        newTask.setTitle(title);
        newTask.setDescription(description);
        newTask.setCreationTime(LocalDateTime.now());
        newTask.setDone(false);

        taskRepository.save(newTask);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/tasks/ID")
    public ResponseEntity listId(@RequestParam int id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity<>(taskOptional.get(), HttpStatus.OK);
    }


    @GetMapping("/tasks")
    public List<Task> list() {
        Iterable<Task> taskIterable = taskRepository.findAll();
        ArrayList<Task> tasks = new ArrayList<>();
        for (Task task : taskIterable) {
            tasks.add(task);
        }
        return tasks;
    }

    @PatchMapping("/tasks/ID")
    public ResponseEntity<String> changeTask(@RequestParam int id,
                                             @RequestParam(value = "title", required = false) String title,
                                             @RequestParam(value = "", required = false) String description,
                                             @RequestParam(value = "isDone", required = false) Boolean isDone
    ) {

        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else if (title != null) {
            Task updatedTask = taskOptional.get();
            updatedTask.setTitle(title);
            taskRepository.save(updatedTask);
        }
        if (description != null) {
            Task updatedTask = taskOptional.get();
            updatedTask.setDescription(description);
            taskRepository.save(updatedTask);
        }
        if (isDone != null) {
            Task updatedTask = taskOptional.get();
            updatedTask.setDone(isDone);
            taskRepository.save(updatedTask);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/tasks/ID")
    public ResponseEntity deleteTask(@RequestParam int id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            taskRepository.deleteById(id);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
