package co.leapwise.expressionevaluator.dto;

import jakarta.validation.constraints.NotNull;

public class EvaluateRequestDTO {

    @NotNull
    private Long expressionId;

    @NotNull
    private String json;

    public EvaluateRequestDTO(final Long expressionId, final String json) {
        this.expressionId = expressionId;
        this.json = json;
    }

    public Long getExpressionId() {
        return expressionId;
    }

    public void setExpressionId(Long expressionId) {
        this.expressionId = expressionId;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
