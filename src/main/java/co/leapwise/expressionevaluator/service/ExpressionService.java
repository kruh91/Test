package co.leapwise.expressionevaluator.service;

import co.leapwise.expressionevaluator.dto.ExpressionRequestDTO;

public interface ExpressionService {
    Long save(ExpressionRequestDTO expressionRequestDTO);
}
