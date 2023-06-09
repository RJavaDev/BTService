package uz.BTService.btservice.controller.convert;


import lombok.experimental.UtilityClass;
import uz.BTService.btservice.controller.dto.request.TechnicalServiceCreate;
import uz.BTService.btservice.controller.dto.response.TechnicalServiceResponseDto;
import uz.BTService.btservice.entity.TechnicalServiceEntity;

import java.util.List;

@UtilityClass
public class TechnicalServiceConvert {

    public TechnicalServiceEntity convertToEntity(TechnicalServiceCreate technicalServiceCreate){
        TechnicalServiceEntity technicalServiceEntity = new TechnicalServiceEntity();
        technicalServiceEntity.setDescription(technicalServiceCreate.getDescription());
        return technicalServiceEntity;
    }

    public TechnicalServiceResponseDto from(TechnicalServiceEntity technicalServiceEntity){

        TechnicalServiceResponseDto technicalServiceResponseDto = technicalServiceEntity.toDto("category", "attach");
        technicalServiceResponseDto.setAttachResponse(AttachConvert.convertToAttachUrlDto(technicalServiceEntity.getAttach()));
        technicalServiceResponseDto.setCategory(CategoryConvert.fromOpenData(technicalServiceEntity.getCategory()));

        return technicalServiceResponseDto;

    }

    public List<TechnicalServiceResponseDto> from(List<TechnicalServiceEntity> technicalServiceEntityList){
        return technicalServiceEntityList.stream().map(TechnicalServiceConvert::from).toList();
    }
}
