package ru.rmamedov.taskmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rmamedov.taskmanager.exception.ProjectNotFoundException;
import ru.rmamedov.taskmanager.model.Project;
import ru.rmamedov.taskmanager.model.DTO.ProjectDTO;
import ru.rmamedov.taskmanager.repository.ProjectRepository;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Transactional
    public void create(final Project project) {
        projectRepository.save(project);
    }

    @Transactional(readOnly = true)
    public ProjectDTO findById(final String id) {
        final Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project with id: " + id + " - Not found."));
        return ProjectDTO.of(project);
    }

}
