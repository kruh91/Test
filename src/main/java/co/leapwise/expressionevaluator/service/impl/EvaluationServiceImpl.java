package co.leapwise.expressionevaluator.service.impl;

import co.leapwise.expressionevaluator.dto.EvaluateRequestDTO;
import co.leapwise.expressionevaluator.exception.ResourceNotFoundException;
import co.leapwise.expressionevaluator.model.Expression;
import co.leapwise.expressionevaluator.repository.ExpressionRepository;
import co.leapwise.expressionevaluator.service.EvaluationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.integration.json.JsonPropertyAccessor;
import org.springframework.stereotype.Service;

@Service("evaluationService")
public class EvaluationServiceImpl implements EvaluationService {

    private static final Logger log = LoggerFactory.getLogger(EvaluationServiceImpl.class);

    private final ExpressionRepository expressionRepository;

    public EvaluationServiceImpl(final ExpressionRepository expressionRepository) {
        this.expressionRepository = expressionRepository;
    }

    @Override
    public boolean evaluate(EvaluateRequestDTO evaluateRequestDTO) {

        Long expressionId = evaluateRequestDTO.getExpressionId();
        Expression expression = expressionRepository.findById(expressionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(String.format("Expression with ID %s does not exist", expressionId))
                );

        ExpressionParser expressionParser = new SpelExpressionParser();
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext(evaluateRequestDTO.getJson());
        evaluationContext.addPropertyAccessor(new JsonPropertyAccessor());

        Boolean result = false;
        try {
            result = expressionParser.parseExpression(expression.getValue()).getValue(evaluationContext, Boolean.class);
        } catch (SpelEvaluationException e) {
            log.warn("Returning false - {}", e.getMessage());
        }


        return Boolean.TRUE.equals(result);
    }
}
