package ru.rmamedov.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rmamedov.taskmanager.model.Project;

@Repository
public interface IProjectRepository extends JpaRepository<Project, String> {

}
