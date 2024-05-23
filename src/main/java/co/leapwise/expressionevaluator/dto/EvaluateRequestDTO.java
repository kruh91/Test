package co.leapwise.expressionevaluator.dto;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;

public class EvaluateRequestDTO {

    @NotNull
    private Long expressionId;

    private JsonNode json;

    public EvaluateRequestDTO(final Long expressionId, final JsonNode json) {
        this.expressionId = expressionId;
        this.json = json;
    }

    public Long getExpressionId() {
        return expressionId;
    }

    public void setExpressionId(Long expressionId) {
        this.expressionId = expressionId;
    }

    public JsonNode getJson() {
        return json;
    }

    public void setJson(JsonNode json) {
        this.json = json;
    }
}
