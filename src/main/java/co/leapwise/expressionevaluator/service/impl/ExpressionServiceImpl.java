package co.leapwise.expressionevaluator.service.impl;

import co.leapwise.expressionevaluator.dto.ExpressionRequestDTO;
import co.leapwise.expressionevaluator.model.Expression;
import co.leapwise.expressionevaluator.repository.ExpressionRepository;
import co.leapwise.expressionevaluator.service.ExpressionService;
import org.springframework.stereotype.Service;

@Service("expressionService")
public class ExpressionServiceImpl implements ExpressionService {

    private final ExpressionRepository expressionRepository;

    public ExpressionServiceImpl(final ExpressionRepository expressionRepository) {
        this.expressionRepository = expressionRepository;
    }

    @Override
    public Long save(ExpressionRequestDTO expressionRequestDTO) {

        Expression expression = new Expression(expressionRequestDTO.getName(), expressionRequestDTO.getValue());
        return expressionRepository.save(expression).getId();
    }
}
