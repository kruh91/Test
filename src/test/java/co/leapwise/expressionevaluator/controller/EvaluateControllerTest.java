package co.leapwise.expressionevaluator.controller;

import co.leapwise.expressionevaluator.dto.ErrorDetailResponse;
import co.leapwise.expressionevaluator.dto.EvaluateRequestDTO;
import co.leapwise.expressionevaluator.exception.handler.ControllerExceptionHandler;
import co.leapwise.expressionevaluator.exception.ResourceNotFoundException;
import co.leapwise.expressionevaluator.service.EvaluationService;
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
import org.springframework.util.CollectionUtils;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EvaluateController.class)
class EvaluateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EvaluationService evaluationService;

    @Test
    void evaluate_True() throws Exception {
        String evaluateJson = """
                {
                  "customer": "test"
                }
                """;
        EvaluateRequestDTO evaluateRequestDTO = new EvaluateRequestDTO(1L, evaluateJson);
        String evaluateRequestJson = new ObjectMapper().writeValueAsString(evaluateRequestDTO);
        Mockito.when(evaluationService.evaluate(Mockito.any())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/evaluate")
                .content(evaluateRequestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void evaluate_False() throws Exception {
        String evaluateJson = """
                {
                  "customer": "test"
                }
                """;
        EvaluateRequestDTO evaluateRequestDTO = new EvaluateRequestDTO(1L, evaluateJson);
        String evaluateRequestJson = new ObjectMapper().writeValueAsString(evaluateRequestDTO);
        Mockito.when(evaluationService.evaluate(Mockito.any())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/evaluate")
                        .content(evaluateRequestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void evaluate_ResourceNotFound() throws Exception {
        String evaluateJson = """
                {
                  "customer": "test"
                }
                """;
        EvaluateRequestDTO evaluateRequestDTO = new EvaluateRequestDTO(1L, evaluateJson);
        String evaluateRequestJson = new ObjectMapper().writeValueAsString(evaluateRequestDTO);
        Mockito.when(evaluationService.evaluate(Mockito.any())).thenThrow(new ResourceNotFoundException("Message"));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/evaluate")
                        .content(evaluateRequestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    ErrorDetailResponse errorDetailResponse = new ObjectMapper()
                            .readValue(result.getResponse().getContentAsString(), ErrorDetailResponse.class);
                    Assertions.assertEquals(ControllerExceptionHandler.RESOURCE_NOT_FOUND_CODE, errorDetailResponse.getErrorCode());
                    Assertions.assertTrue(CollectionUtils.isEmpty(errorDetailResponse.getErrors()));
                })
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void evaluate_MethodArgumentNotValidException() throws Exception {

        EvaluateRequestDTO evaluateRequestDTO = new EvaluateRequestDTO(null, null);
        String evaluateRequestJson = new ObjectMapper().writeValueAsString(evaluateRequestDTO);
        Mockito.when(evaluationService.evaluate(Mockito.any())).thenThrow(new ResourceNotFoundException("Message"));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/evaluate")
                        .content(evaluateRequestJson)
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
