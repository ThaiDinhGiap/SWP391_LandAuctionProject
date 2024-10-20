package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.entity.Task;
import com.se1858.group4.Land_Auction_SWP391.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private TaskRepository taskRepository;
    private AccountService accountService;
    public TaskService(TaskRepository taskRepository, AccountService accountService) {
        this.taskRepository = taskRepository;
        this.accountService = accountService;
    }
    public Task save(Task task) {
        task.setContentTask("Giao viec quan ly phien dau gia cho tai san co id la: "+task.getAsset().getAssetId());
        task.setCreatedDate(LocalDateTime.now());
        task.setFinishedDate(null);
        task.setStatus("In progress");
        return taskRepository.save(task);
    }
    public List<Task> getAllTasksByAuctioneerId(int auctioneerId, String status) {
        List<Task> list = taskRepository.findByAuctioneer_AccountIdAndStatus(auctioneerId, status);
        return list;
    }
    public void changeTaskStatus(int taskId, String status) {
        Task task = findTaskById(taskId);
        if(task!=null) {
            task.setStatus(status);
            taskRepository.save(task);
        }
    }
    public Task findTaskById(int taskId) {
        Optional<Task> task = taskRepository.findById(taskId);
        if(task.isPresent()) {
            return task.get();
        }
        else return null;
    }
}
