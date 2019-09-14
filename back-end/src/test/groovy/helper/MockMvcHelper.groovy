package helper

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import ru.rmamedov.taskmanager.Application
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

/**
 * @author Rustam Mamedov
 */

@SpringBootTest(classes = [Application.class])
@AutoConfigureMockMvc
class MockMvcHelper extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private ObjectMapper mapper

    protected performPost(String url, Object object) {
        mockMvc.perform(post(url)
                .content(mapper.writeValueAsString(object))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
    }

    protected performGet(String url, String...vars) {
        mockMvc.perform(get(url, vars))
    }

}
