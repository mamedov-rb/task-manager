package ru.rmamedov.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rmamedov.taskmanager.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

}
