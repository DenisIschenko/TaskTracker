package com.task.tracker.model;

import com.task.tracker.entity_helper.TaskStatusConverter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "task_id")
    private int id;

    @Column
    private String title;

    @Column(length=1024)
    private String description;

    @Column
    @Convert(converter = TaskStatusConverter.class)
    private Status status = Status.NEW;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @ManyToOne
    private User owner;

    @ManyToOne
    private User assignee;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public enum Status {
        NEW(0, "new"),
        ACCEPTED(1, "accepted"),
        CLOSED(2, "closed");

        private int code;
        private String text;

        Status(int code, String text) {
            this.code = code;
            this.text = text;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }

        public static Status findByCode(int code) {
            for (Status status : Status.values()) {
                if (status.code == code) {
                    return status;
                }
            }

            return null;
        }

        public static Status findByText(String text) {
            for (Status status : Status.values()) {
                if (status.text.equals(text)) {
                    return status;
                }
            }

            return null;
        }
    }
}