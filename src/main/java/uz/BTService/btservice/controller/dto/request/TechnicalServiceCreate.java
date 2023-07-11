package uz.BTService.btservice.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TechnicalServiceCreate {
    @NotBlank(message = "description must not be null!!!")
    private String description;

    @NotBlank(message = "attachId must not be null!!!")
    private String attachId;

    @NotBlank(message = "categoryId must not be null!!!")
    private Integer categoryId;
}
