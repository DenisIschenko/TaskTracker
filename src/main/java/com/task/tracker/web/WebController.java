package com.task.tracker.web;

import com.task.tracker.form.TaskForm;
import com.task.tracker.model.Task;
import com.task.tracker.model.User;
import com.task.tracker.service.NotificationServiceImpl;
import com.task.tracker.service.TaskService;
import com.task.tracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class WebController {
    @Autowired
    private UserService userService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private NotificationServiceImpl notificationService;

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView registration() {
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findByLogin(user.getLogin());
        if (userExists != null) {
            bindingResult
                    .rejectValue("login", "error.user",
                            "There is already a user registered with the login provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            String pass = user.getPassword();
            String login = user.getLogin();
            userService.save(user);
            auto_login(login, pass);
            notificationService.addInfoMessage("User successfully created");
            return new ModelAndView("redirect:/");
        }
        return modelAndView;
    }

    private void auto_login(String login, String password) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(login, password, grantedAuthorities);

        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @RequestMapping(value = {"/", "/tasks"}, method = RequestMethod.GET)
    public ModelAndView tasks(@RequestParam(value="start", required=false) String start,
                              @RequestParam(value = "end", required = false) String end,
                              @RequestParam(value = "statuses", required = false) List<String> statuses) throws ParseException {

        List tasks;

        if (start != null && end != null && statuses != null){
            List<Task.Status> statusList = new ArrayList<>();
            statuses.forEach((temp) -> statusList.add(Task.Status.findByText(temp)));

            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            Date start_date = formatter.parse(start);
            Date end_date = formatter.parse(end);

            tasks = taskService.AllByCreationDateAndStatuses(statusList, start_date, end_date);

        } else if (start != null && end != null){
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            Date start_date = formatter.parse(start);
            Date end_date = formatter.parse(end);

            tasks = taskService.findAllByCreationDateBetween(start_date, end_date);
        } else if (statuses != null){
            List<Task.Status> statusList = new ArrayList<>();
            statuses.forEach((temp) -> statusList.add(Task.Status.findByText(temp)));

            tasks = taskService.findByStatuses(statusList);
        } else {
            tasks = taskService.findAll();
        }
        ModelAndView modelAndView = new ModelAndView();
        User user = getAuthUser();
        Date today = new Date();
        Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));

        modelAndView.addObject("user", user.getLogin());
        modelAndView.addObject("tasks", tasks);
        modelAndView.addObject("today", today);
        modelAndView.addObject("tomorrow", tomorrow);
        modelAndView.setViewName("tasks");
        return modelAndView;
    }

    @RequestMapping(value = "tasks/add", method = RequestMethod.GET)
    public ModelAndView addTask() {
        Task task = new Task();
        TaskForm taskForm = new TaskForm();
        User user = getAuthUser();
        List users = userService.findAll();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("task", task);
        modelAndView.addObject("taskForm", taskForm);
        modelAndView.addObject("user", user.getLogin());
        modelAndView.addObject("users", users);

        modelAndView.setViewName("add_task");
        return modelAndView;
    }

    @RequestMapping(value = "tasks/add", method = RequestMethod.POST)
    public ModelAndView createTask(@Valid TaskForm taskForm, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User user = getAuthUser();
        taskForm.setOwner(user);
        List users = userService.findAll();

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("user", user.getLogin());
            modelAndView.addObject("users", users);
            modelAndView.setViewName("add_task");
        } else {
            Task task = taskService.create(taskForm);
            notificationService.addInfoMessage("Task successfully created");
            return new ModelAndView("redirect:/tasks/" + task.getId());
        }
        return modelAndView;
    }

    @RequestMapping(value = "tasks/{id}", method = RequestMethod.GET)
    public ModelAndView viewTask(@PathVariable("id") Integer id) {
        Task task = taskService.findById(id);
        if (task == null) {
            notificationService.addErrorMessage("Cannot find task: " + id);
            return new ModelAndView("redirect:/tasks/");
        }

        User user = getAuthUser();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("task", task);
        modelAndView.addObject("user", user.getLogin());

        modelAndView.setViewName("view_task");
        return modelAndView;
    }

    @RequestMapping(value = "tasks/{id}/edit", method = RequestMethod.GET)
    public ModelAndView editTask(@PathVariable("id") Integer id) {
        TaskForm taskForm = new TaskForm();
        Task task = taskService.findById(id);
        if (task == null) {
            notificationService.addErrorMessage("Cannot find task: " + id);
            return new ModelAndView("redirect:/tasks/");
        }

        taskForm.setTitle(task.getTitle());
        taskForm.setDescription(task.getDescription());
        taskForm.setAssigneeLogin(task.getAssignee().getLogin());
        taskForm.setStatusText(task.getStatus().getText());

        User user = getAuthUser();
        List users = userService.findAll();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("task", task);
        modelAndView.addObject("taskForm", taskForm);
        modelAndView.addObject("user", user.getLogin());
        modelAndView.addObject("users", users);

        modelAndView.setViewName("edit_task");
        return modelAndView;
    }

    @RequestMapping(value = "tasks/{id}/edit", method = RequestMethod.POST)
    public ModelAndView updateTask(@PathVariable("id") Integer id, @Valid TaskForm taskForm, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        Task task = taskService.findById(id);
        if (task == null) {
            notificationService.addErrorMessage("Cannot find task: " + id);
            return new ModelAndView("redirect:/tasks/");
        }

        User user = getAuthUser();
        List users = userService.findAll();

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("user", user.getLogin());
            modelAndView.addObject("users", users);
            modelAndView.setViewName("edit_task");
        } else {
            taskService.update(task, taskForm);
            notificationService.addInfoMessage("Task successfully updated");
            return new ModelAndView("redirect:/tasks/" + task.getId());
        }
        return modelAndView;
    }

    private User getAuthUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.findByLogin(auth.getName());
    }
}