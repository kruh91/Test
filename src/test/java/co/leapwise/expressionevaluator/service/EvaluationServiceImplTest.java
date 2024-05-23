package co.leapwise.expressionevaluator.service;

import co.leapwise.expressionevaluator.dto.EvaluateRequestDTO;
import co.leapwise.expressionevaluator.dto.ExpressionRequestDTO;
import co.leapwise.expressionevaluator.exception.ResourceNotFoundException;
import co.leapwise.expressionevaluator.model.Expression;
import co.leapwise.expressionevaluator.repository.ExpressionRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.AssertionErrors;

import java.util.Optional;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@AutoConfigureTestDatabase
class EvaluationServiceImplTest {

    @Autowired
    private EvaluationService evaluationService;

    @Autowired
    private ExpressionRepository expressionRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void init() {
        expressionRepository.deleteAll();
    }

    @Test
    void testEvaluate_False() throws Exception{
        String json = """
                {
                    "customer":
                    {
                      "firstName": "JOHN",
                      "lastName": "DOE",
                      "address":
                      {
                        "city": "Chicago",
                        "zipCode": 1234,
                        "street": "56th",
                        "houseNumber": 2345
                      },
                      "salary": 99,
                      "type": "BUSINESS"
                    }
                  }
                """;
        JsonNode jsonNode = objectMapper.readTree(json);
        Expression expression = new Expression("Complex logical expression",
                "(customer.firstName == \"ANN\" && customer.salary < 100) OR (customer.address != null && customer.address.city == \"New York\")");
        Long id = expressionRepository.save(expression).getId();

        EvaluateRequestDTO evaluateRequestDTO = new EvaluateRequestDTO(id, jsonNode);
        Assertions.assertFalse(evaluationService.evaluate(evaluateRequestDTO));
    }

    @Test
    void testEvaluate_True() throws Exception {
        String json = """
                {
                    "customer":
                    {
                      "firstName": "JOHN",
                      "lastName": "DOE",
                      "address":
                      {
                        "city": "Chicago",
                        "zipCode": 1234,
                        "street": "56th",
                        "houseNumber": 2345
                      },
                      "salary": 99,
                      "type": "BUSINESS"
                    }
                  }
                """;
        JsonNode jsonNode = objectMapper.readTree(json);
        Expression expression = new Expression("Complex logical expression",
                "(customer.firstName == \"JOHN\" && customer.salary < 100) OR (customer.address != null && customer.address.city == \"Washington\")");
        Long id = expressionRepository.save(expression).getId();

        EvaluateRequestDTO evaluateRequestDTO = new EvaluateRequestDTO(id, jsonNode);
        Assertions.assertTrue(evaluationService.evaluate(evaluateRequestDTO));
    }

    @Test
    void testEvaluate_SpelEvaluationException() throws Exception {
        String json = """
                {
                    "test" : "value"
                }""";
        JsonNode jsonNode = objectMapper.readTree(json);
        Expression expression = new Expression("Complex logical expression",
                "(customer.firstName == \"JOHN\" && customer.salary < 100) OR (customer.address != null && customer.address.city == \"Washington\")");
        Long id = expressionRepository.save(expression).getId();

        EvaluateRequestDTO evaluateRequestDTO = new EvaluateRequestDTO(id, jsonNode);
        Assertions.assertFalse(evaluationService.evaluate(evaluateRequestDTO));
    }

    @Test
    void testEvaluate_ExpressionNotFound() throws Exception {

        String json = """
                {
                  "customer": "test"
                }
                """;
        JsonNode jsonNode = objectMapper.readTree(json);
        EvaluateRequestDTO evaluateRequestDTO = new EvaluateRequestDTO(1L, jsonNode);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> evaluationService.evaluate(evaluateRequestDTO));
    }
}
