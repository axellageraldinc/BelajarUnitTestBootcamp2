package Service;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import springboot.model.Todo;
import springboot.model.constants.TodoPriority;
import springboot.repository.TodoRepository;
import springboot.service.TodoService;

import java.util.ArrayList;
import java.util.List;

public class TodoServiceTest {

    private TodoService todoService;

    @Mock
    //maksudnya Mock adalah membuat mocking (tiruan/bohongan) dari class TodoRepository, jadi udah bukan class TodoRepository yang asli yang dipanggil
    //Sehingga kalau todoRepository.getAll() masih kosong gak ada implementasi gitu bisa tetap di test
    private TodoRepository todoRepository;

    //yang di mock jangan class yang di test saat ini. Karena ini ngetest todoService, maka yg di mock jangan todoService, melainkan dependency-nya yaitu todoRepository

    @Before
    public void starting() {
        MockitoAnnotations.initMocks(this); //inisiasi Mock Annotation
        this.todoService = new TodoService(this.todoRepository);
    }

    @After
    public void finishing() {
        //memastikan kalau yang dipanggil hanya getAll saja gak ada fungsi lainnya
        //kalau ada memanggil fungsi lain yang gak di specify maka akan error...
        //@After ini dipanggil PADA SETIAP METHOD YANG DI TEST SELESAI...
        Mockito.verifyNoMoreInteractions(todoRepository);
    }

    @Test
    public void getAllTest() {

        List<Todo> todoList = new ArrayList<Todo>();
        todoList.add(new Todo("Belajar unit test", TodoPriority.HIGH)); //add item ke todoList

        //given
        BDDMockito.given(todoRepository.getAll()).willReturn(todoList); //given saat todoService getAll dieksekusi

        //when
        //TODO : INI PIYE
        todoList = todoService.getAll();

        //then
        Assert.assertThat(todoList, org.hamcrest.Matchers.notNullValue()); //kita berharap todoList not null
        Assert.assertThat(todoList.isEmpty(), org.hamcrest.Matchers.equalTo(false)); //kita berharap (assert = menuntut) todoList itu not empty

        //verify
        //memastikan bahwa memanggil todoRepository beneran di todoService.getAll(), bukan hardcode di todoService.getAll insert ke List manual.
        //dan hanya getAll() saja yang dipanggil
        BDDMockito.then(todoRepository).should().getAll();
    }

    @Test
    public void saveTodo() {

        String name = "Mengerjakan PR";
        TodoPriority priority = TodoPriority.HIGH;

        //given
        BDDMockito.given(todoRepository.store(new Todo(name, priority))).willReturn(true);

        //when
        Boolean isInsertSuccess = todoService.saveTodo(name, priority);

        //then
        Assert.assertThat(name, Matchers.notNullValue());
        Assert.assertThat(priority, Matchers.notNullValue());
        Assert.assertThat(isInsertSuccess, Matchers.equalTo(true));

        //verify
        BDDMockito.then(todoRepository).should().store(new Todo(name, priority));
    }

}
