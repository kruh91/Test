package co.leapwise.expressionevaluator.service;

import co.leapwise.expressionevaluator.dto.EvaluateRequestDTO;

public interface EvaluationService {
    boolean evaluate(EvaluateRequestDTO evaluateRequestDTO);
}
