package co.leapwise.expressionevaluator.controller;

import co.leapwise.expressionevaluator.dto.EvaluateRequestDTO;
import co.leapwise.expressionevaluator.service.EvaluationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("evaluate")
public class EvaluateController {

    private final EvaluationService evaluationService;

    public EvaluateController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @PostMapping
    public ResponseEntity<Boolean> evaluate(@Valid @RequestBody final EvaluateRequestDTO evaluateRequestDTO) {
        boolean result = evaluationService.evaluate(evaluateRequestDTO);

        return ResponseEntity.ok(result);
    }
}
