package co.leapwise.expressionevaluator.service;

import co.leapwise.expressionevaluator.dto.ExpressionRequestDTO;
import co.leapwise.expressionevaluator.model.Expression;
import co.leapwise.expressionevaluator.repository.ExpressionRepository;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.AssertionErrors;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@AutoConfigureTestDatabase
class ExpressionServiceImplTest {

    @Autowired
    private ExpressionService expressionService;

    @Autowired
    private ExpressionRepository expressionRepository;

    @BeforeEach
    void init() {
        expressionRepository.deleteAll();
    }

    @Test
    void testSave() {
        long expressionId = expressionService.save(new ExpressionRequestDTO("test name", "test expression"));
        Iterable<Expression> allExpressions = expressionRepository.findAll();

        AssertionErrors.assertEquals("Expression ID is not as expected.", 1L, expressionId);
        AssertionErrors.assertEquals("Wrong number of results returned.", 1, IterableUtil.sizeOf(allExpressions));
    }
}
