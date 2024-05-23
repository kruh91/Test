package co.leapwise.expressionevaluator.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ExpressionRequestDTO {

    @NotNull
    @Size(min = 1, max = 50)
    private String name;

    @NotNull
    @Size(min = 1, max = 255)
    private String value;

    public ExpressionRequestDTO(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
