package com.example.demo.controller;

import com.example.demo.model.Project;
import com.example.demo.model.Task;
import com.example.demo.service.ProjectService;
import com.example.demo.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private ProjectService projectService;

    private List<Task> taskList;

    @Before
    public void setup() {
        taskList = new ArrayList<>();

        Task task = new Task();
        task.setTitle("test title 1");
        task.setDescription("test description 1");
        task.setCreateDate(new Date());
        task.setActive(true);
        task.setPriority(1);
        task.setProject(new Project());

        Task task1 = new Task();
        task1.setTitle("test title 2");
        task1.setDescription("test description 2");
        task1.setCreateDate(new Date());
        task1.setActive(true);
        task1.setPriority(2);
        task1.setProject(new Project());

        taskList.add(task);
        taskList.add(task1);
    }

    @Test
    public void getTaskList() throws Exception {
        given(taskService.getTaskByProjectId(1)).willReturn(taskList);

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/list/1")
                .with(user("test").password("test"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound())
                .andReturn();
    }

    @Test
    public void postSaveTask() throws Exception {
        Project project = new Project();
        project.setId(1);
        project.setName("project name");

        given(projectService.getById(1)).willReturn(project);
        ObjectMapper objectMapper = new ObjectMapper();

        taskList.get(0).setProject(project);
        given(taskService.save(taskList.get(0))).willReturn(taskList.get(0));

        mockMvc.perform(MockMvcRequestBuilders.post("/tasks/save/1")
                .with(user("test").password("test"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskList.get(0))))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void deleteTask() throws Exception {
        willDoNothing().given(projectService).deleteById(1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/delete/1")
                .with(user("test").password("test")))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    public void putUpdateTask() throws Exception {
        Project project = new Project();
        project.setId(1);
        project.setName("project name");

        given(projectService.getById(1)).willReturn(project);

        given(taskService.update(taskList.get(0))).willReturn(taskList.get(0));

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/update/1/projects/2")
                .with(user("test").password("test"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(taskList.get(0))))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getTaskById() throws Exception {
        given(taskService.getById(1)).willReturn(taskList.get(0));

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/1")
                .with(user("test").password("test")))
                .andExpect(status().isFound())
                .andReturn();
    }

    @Test
    public void getAllTask() throws Exception {
        given(taskService.getAll()).willReturn(taskList);

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/getAll")
                .with(user("test").password("test")))
                .andExpect(status().isFound())
                .andReturn();
    }

    @Test
    public void putUpdateTaskStatus() throws Exception {
        given(taskService.getById(1)).willReturn(taskList.get(0));
        given(taskService.update(taskList.get(0))).willReturn(taskList.get(0));

        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/active/1")
                .with(user("test").password("test")))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getTaskByStatus() throws Exception {
        given(taskService.getTaskByProjectIdAndActive(1, true)).willReturn(taskList);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
//        objectMapper.setDateFormat(dateFormat);
//        String expectedJson = objectMapper.writeValueAsString(taskList);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/tasks/active/1/1")
                .with(user("test").password("test"))
                .param("isActive", "true"))
                .andExpect(status().isFound())
                .andReturn();

//
//        String actualJson = mvcResult.getResponse().getContentAsString();
//
//        System.out.println(actualJson);
//
//        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void putUpdateTaskPriority() throws Exception {
        willDoNothing().given(taskService).updatePriority(1, 1);

        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/priority/{taskId}", 1)
                .with(user("test").password("test"))
                .param("priority", "1"))
                .andExpect(status().isOk())
                .andReturn();

    }


}
