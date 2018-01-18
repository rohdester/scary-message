package dk.bearsoft.scaries;

import dk.bearsoft.scaries.model.ScaryMessage;
import dk.bearsoft.scaries.repository.ScaryMessageRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import sun.reflect.annotation.ExceptionProxy;

import java.nio.charset.Charset;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest()
@WebAppConfiguration
public class MessageControllerTest {

    private MockMvc mockMvc;
    private MediaType contentType = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ScaryMessageRepository scaryMessageRepository;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        scaryMessageRepository.deleteAllInBatch();

        scaryMessageRepository.save(new ScaryMessage("Be aware of the Great Old Ones"));
        scaryMessageRepository.save(new ScaryMessage("Today is the day the rise from the seas!!!!"));
    }

    @Test
    public void createScaryMessage() throws Exception {
        ScaryMessage message = new ScaryMessage("Cthulhu is coming");
        String scaryMessageJson = new TestHelper().toJson(message);
        this.mockMvc.perform(post("/messages")
                .contentType(contentType)
                .content(scaryMessageJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void getAllScaryMessages() throws Exception {
        mockMvc.perform(get("/messages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].message", is("Today is the day the rise from the seas!!!!")));
    }

    @Test
    public void deleteScaryMessage() throws Exception {
        mockMvc.perform(delete("/messages/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void modifyScaryMessage() throws Exception {
        ScaryMessage message = new ScaryMessage("Cthulhu is coming");
        String scaryMessageJson = new TestHelper().toJson(message);
        mockMvc.perform(put("/messages/1")
                .contentType(contentType)
                .content(scaryMessageJson))
                .andExpect(status().isNoContent());
    }

    @Test
    public void failedModifyScaryMessage() throws Exception {
        ScaryMessage message = new ScaryMessage("Cthulhu is coming");
        String scaryMessageJson = new TestHelper().toJson(message);
        mockMvc.perform(put("/messages/16")
                .contentType(contentType)
                .content(scaryMessageJson))
                .andExpect(status().isBadRequest());
    }
}
