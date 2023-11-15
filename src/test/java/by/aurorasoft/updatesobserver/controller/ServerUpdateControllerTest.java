package by.aurorasoft.updatesobserver.controller;

import by.aurorasoft.updatesobserver.service.ServerUpdateService;
import by.aurorasoft.updatesobserver.service.factory.ServerUpdateFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(ServerUpdateController.class)
public class ServerUpdateControllerTest {

    @MockBean
    private ServerUpdateFactory mockedUpdateFactory;

    @MockBean
    private ServerUpdateService mockedUpdateService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void updateShouldBeCreatedAndSaved() {
        this.mockMvc.perform(get())
    }
}
