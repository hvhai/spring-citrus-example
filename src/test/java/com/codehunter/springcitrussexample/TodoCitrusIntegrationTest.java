package com.codehunter.springcitrussexample;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

import com.codehunter.springcitrussexample.config.EndpointConfig;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.config.CitrusSpringConfig;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.junit.jupiter.spring.CitrusSpringSupport;
import com.consol.citrus.message.MessageType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;

@CitrusSpringSupport
@ContextConfiguration(classes = {CitrusSpringConfig.class, EndpointConfig.class})
// @TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class TodoCitrusIntegrationTest {
  public static final String TASK_1 = "Task 1";
  @Autowired private HttpClient appClient;
  static ObjectMapper objectMapper;

  @BeforeAll
  public static void setup() {
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
  }

  @DisplayName("Create todo")
  @Test
  @Order(1)
  @CitrusTest
  void createTodo(@CitrusResource TestCaseRunner actions) throws Exception {
    actions.variable("task", TASK_1);
    actions.variable("payload", createJsonTodo(TASK_1));
    actions.$(http().client(appClient).send().post("/todos").message().body("${payload}"));

    actions.$(
        http()
            .client(appClient)
            .receive()
            .response(HttpStatus.OK)
            .message()
            .type(MessageType.JSON)
            .validate(jsonPath().expression("task", "${task}")));
  }

  @DisplayName("Get all todos")
  @Test
  @Order(2)
  @CitrusTest
  void getAllTodo(@CitrusResource TestCaseRunner actions) throws Exception {
    actions.variable("task", TASK_1);
    actions.$(http().client(appClient).send().get("/todos").message());

    actions.$(
        http()
            .client(appClient)
            .receive()
            .response(HttpStatus.OK)
            .message()
            .type(MessageType.JSON)
            .validate(jsonPath().expression("$[0].task", "${task}")));
  }

  //    @AfterAll
  //    public void clean() {
  //        log.info("Clean after test");
  //
  //    }
  private String createJsonTodo(String task) throws Exception {
    Map<String, Object> jsonMap = new HashMap<>();
    jsonMap.put("task", task);
    return objectMapper.writeValueAsString(jsonMap);
  }
}
