package uz.BTService.btservice.controller.dto.dtoUtil;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@Schema(name = "FilterForm", description = "Pagination details for data retrieval")
public class FilterForm implements Serializable {

    @Serial
    private static final long serialVersionUID = -1183975305038088044L;

    @Schema(description = "Starting index for data retrieval", example = "0")
    private Integer start;

    @Schema(description = "Number of items to retrieve", example = "10")
    private Integer length;

    @Schema(description = "Additional filters for data retrieval", example = "{\"startDate\":\"18-06-2023\", \"endDate\":\"18-07-2023\"}")
    private Map<String, Object> filter;

}
