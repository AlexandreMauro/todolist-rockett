package br.com.alexandremauro.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "tb_tasks")
public class taskModel {

    /**
     * ID
     * USUARIO (ID_USUARIO)
     * DESCRIÇÃO
     * TITULO
     * DATA DE INICIO
     * DATA DE TERMINO
     * PRIORIDADE
     */
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private String description;

    @Column(length = 50)
    private String title;
    private LocalDateTime StartAT;
    private LocalDateTime endAT;
    private String priority;
    @CreationTimestamp
    private LocalDateTime createAT;

    private UUID iduser;

    public void setTitle(String title) throws Exception{

        if(title.length() > 50){
            throw new Exception("Ocampo deve conter no maximo 50 caracteres");
        }
        this.title = title;
    }
}
