import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import springboot.model.Todo;
import springboot.model.constants.TodoPriority;

import java.util.Arrays;
import java.util.List;

public class TodoRepositoryTest {

    @Mock
    List<Todo> todoList;

    private static final String name = "Mengerjakan PR";
    private static final TodoPriority todoPriority = TodoPriority.HIGH;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void saveTodoTest(){
        BDDMockito.when(todoList.add(new Todo(name, todoPriority))).thenReturn(true);

        Boolean isInsertSuccess = todoList.add(new Todo(name, todoPriority));

        Assert.assertTrue(isInsertSuccess);

        BDDMockito.then(todoList).should().add(new Todo(name, todoPriority));

        Mockito.verifyNoMoreInteractions(todoList);
    }

    @Test
    public void getAllTest(){
        todoList = Arrays.asList(new Todo(name, todoPriority));
        Assert.assertThat(todoList, Matchers.hasItem(new Todo(name, todoPriority)));
    }

}
