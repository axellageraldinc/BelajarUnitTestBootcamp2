package Controller;

import com.jayway.restassured.RestAssured;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import springboot.Introduction;
import springboot.model.Todo;
import springboot.model.constants.TodoPriority;
import springboot.model.request.CreateTodoRequest;
import springboot.service.TodoService;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Introduction.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoControllerTest {

    @MockBean
    TodoService todoService;

    @LocalServerPort
    private int serverPort;

    private static final String name = "Mengerjakan PR";
    private static final TodoPriority priority = TodoPriority.HIGH;

    private static final String TODO = "{\"code\":200,\"message\":null,\"value\":[{\"name\":\"Mengerjakan PR\",\"priority\":\"HIGH\"}]}";

    @Before
    public void starting(){

    }

    @After
    public void finishing(){
        Mockito.verifyNoMoreInteractions(todoService);
    }

    @Test
    public void getAllTest(){
        List<Todo> todoList = new ArrayList<Todo>();
        todoList.add(new Todo(name, priority));

        BDDMockito.when(todoService.getAll()).thenReturn(todoList);

        RestAssured.given()
                .contentType("application/json")
                .when()
                .port(serverPort)
                .get("/todos")
                .then()
                .body(Matchers.containsString("value"))
                .body(Matchers.containsString(name))
                .body(Matchers.equalTo(TODO))
                .statusCode(200);

        Mockito.verify(todoService).getAll();
    }

    @Test
    public void insertTest(){

        CreateTodoRequest createTodoRequest = new CreateTodoRequest();
        createTodoRequest.setName(name);
        createTodoRequest.setPriority(priority);

        BDDMockito.when(todoService.saveTodo(createTodoRequest.getName(), createTodoRequest.getPriority())).thenReturn(true);

        RestAssured
                .given()
                .contentType("application/json")
                .body(createTodoRequest)
                .when()
                .port(serverPort)
                .post("/todos");

        Mockito.verify(todoService).saveTodo(name, priority);

    }

}
