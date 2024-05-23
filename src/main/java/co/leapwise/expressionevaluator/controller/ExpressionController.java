package co.leapwise.expressionevaluator.controller;

import co.leapwise.expressionevaluator.dto.ExpressionRequestDTO;
import co.leapwise.expressionevaluator.service.ExpressionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("expression")
public class ExpressionController {

    private final ExpressionService expressionService;

    public ExpressionController(final ExpressionService expressionService) {
        this.expressionService = expressionService;
    }

    @PostMapping
    public ResponseEntity<Long> createExpression(@Valid @RequestBody final ExpressionRequestDTO expressionRequestDTO) {
        Long expressionId = expressionService.save(expressionRequestDTO);

        return new ResponseEntity<>(expressionId, HttpStatus.CREATED);
    }
}
