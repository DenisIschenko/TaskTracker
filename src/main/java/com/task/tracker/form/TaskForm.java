package com.task.tracker.form;

import com.task.tracker.model.Task;
import com.task.tracker.model.User;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TaskForm {

  @NotEmpty(message = "*Please provide Task title")
  private String title;

  @NotNull
  @Size(min = 1)
  private String description;

  @NotNull
  private String statusText;

  @NotNull
  private Task.Status status;

  @ManyToOne
  private User owner;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String content) {
    this.description = content;
  }

  public String getStatusText() {
    return statusText;
  }

  public void setStatusText(String statusText) {
    this.statusText = statusText;
    this.status = Task.Status.findByText(statusText);
  }

  public Task.Status getStatus() {
    return status;
  }

  public void setStatus(Task.Status status) {
    this.status = status;
  }

  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }
}
