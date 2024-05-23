package co.leapwise.expressionevaluator.service;

import co.leapwise.expressionevaluator.dto.EvaluateRequestDTO;
import co.leapwise.expressionevaluator.dto.ExpressionRequestDTO;
import co.leapwise.expressionevaluator.exception.ResourceNotFoundException;
import co.leapwise.expressionevaluator.model.Expression;
import co.leapwise.expressionevaluator.repository.ExpressionRepository;
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

    @BeforeEach
    void init() {
        expressionRepository.deleteAll();
    }

    @Test
    void testEvaluate_False() {
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

        Expression expression = new Expression("Complex logical expression",
                "(customer.firstName == \"ANN\" && customer.salary < 100) OR (customer.address != null && customer.address.city == \"New York\")");
        Long id = expressionRepository.save(expression).getId();

        EvaluateRequestDTO evaluateRequestDTO = new EvaluateRequestDTO(id, json);
        Assertions.assertFalse(evaluationService.evaluate(evaluateRequestDTO));
    }

    @Test
    void testEvaluate_True() {
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

        Expression expression = new Expression("Complex logical expression",
                "(customer.firstName == \"JOHN\" && customer.salary < 100) OR (customer.address != null && customer.address.city == \"Washington\")");
        Long id = expressionRepository.save(expression).getId();

        EvaluateRequestDTO evaluateRequestDTO = new EvaluateRequestDTO(id, json);
        Assertions.assertTrue(evaluationService.evaluate(evaluateRequestDTO));
    }

    @Test
    void testEvaluate_SpelEvaluationException() {
        String json = """
                {
                    "test" : "value"
                }""";

        Expression expression = new Expression("Complex logical expression",
                "(customer.firstName == \"JOHN\" && customer.salary < 100) OR (customer.address != null && customer.address.city == \"Washington\")");
        Long id = expressionRepository.save(expression).getId();

        EvaluateRequestDTO evaluateRequestDTO = new EvaluateRequestDTO(id, json);
        Assertions.assertFalse(evaluationService.evaluate(evaluateRequestDTO));
    }

    @Test
    void testEvaluate_ExpressionNotFound() {

        String json = """
                {
                  "customer": "test"
                }
                """;

        EvaluateRequestDTO evaluateRequestDTO = new EvaluateRequestDTO(1L, json);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> evaluationService.evaluate(evaluateRequestDTO));
    }
}
