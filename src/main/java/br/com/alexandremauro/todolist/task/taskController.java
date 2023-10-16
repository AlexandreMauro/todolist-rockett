package br.com.alexandremauro.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.config.Task;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alexandremauro.todolist.utils.utils;


import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class taskController {

    @Autowired
    public ItaskRepository taskRepository;

    @PostMapping("/")

    public ResponseEntity create(@RequestBody taskModel taskModel, HttpServletRequest request) {
        var iduser = request.getAttribute("iduser");
        taskModel.setIduser((UUID) iduser);

        var curentData = LocalDateTime.now();
        if (curentData.isAfter(taskModel.getStartAT()) || curentData.isAfter(taskModel.getEndAT())) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A DATA DE INICIO  E DATA DE TEMINO DEVE SER IGUAL OU MAIOR QUE A ATUAL");
        }
        if (taskModel.getStartAT().isAfter(taskModel.getEndAT())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A DATA DE INICIO  DEVE SER MENOR Q A DATA DE TERMINO");
        }

        var task = this.taskRepository.save(taskModel);

        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @GetMapping("/")
    public List<taskModel> list(HttpServletRequest request) {
        var iduser = request.getAttribute("iduser");
        var tasks = this.taskRepository.findByIduser((UUID) iduser);
        return tasks;
    }

    // UPDATE TASKS
    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody taskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {

        var task = this.taskRepository.findById(id).orElse(null);
        var iduser = request.getAttribute("iduser");

        if (task == null) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Tarefa não Encontrda");

        }

        if (!task.getIduser().equals(iduser)) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("O Usuario não tem permissão ");
        }
        utils.copyNonNullProperties(taskModel, task);

        var taskUpdate = this.taskRepository.save(task);
        return ResponseEntity.ok().body(taskUpdate);

    }

}
