package co.leapwise.expressionevaluator.controller;

import co.leapwise.expressionevaluator.dto.ErrorDetailResponse;
import co.leapwise.expressionevaluator.dto.ExpressionRequestDTO;
import co.leapwise.expressionevaluator.exception.handler.ControllerExceptionHandler;
import co.leapwise.expressionevaluator.service.ExpressionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExpressionController.class)
class ExpressionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExpressionService expressionService;

    @Test
    void saveExpression() throws Exception {

        ExpressionRequestDTO expressionRequestDTO = new ExpressionRequestDTO("Complex logical expression",
                "(customer.firstName == \"JOHN\" && customer.salary < 100) OR (customer.address != null && customer.address.city == \"Washington\")");
        String expressionJson = new ObjectMapper().writeValueAsString(expressionRequestDTO);
        Mockito.when(expressionService.save(Mockito.any())).thenReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/expression")
                .content(expressionJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string("1"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void evaluate_MethodArgumentNotValidException() throws Exception {

        ExpressionRequestDTO expressionRequestDTO = new ExpressionRequestDTO("","");
        String expressionJson = new ObjectMapper().writeValueAsString(expressionRequestDTO);
        Mockito.when(expressionService.save(Mockito.any())).thenReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/expression")
                        .content(expressionJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    ErrorDetailResponse errorDetailResponse = new ObjectMapper()
                            .readValue(result.getResponse().getContentAsString(), ErrorDetailResponse.class);
                    Assertions.assertEquals(ControllerExceptionHandler.INVALID_REQUEST_CODE, errorDetailResponse.getErrorCode());
                    Assertions.assertEquals(2, errorDetailResponse.getErrors().size());
                })
                .andDo(MockMvcResultHandlers.print());
    }
}
