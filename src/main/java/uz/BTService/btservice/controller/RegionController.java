package uz.BTService.btservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.BTService.btservice.controller.convert.CategoryConvert;
import uz.BTService.btservice.controller.convert.RegionConvert;
import uz.BTService.btservice.controller.dto.CategoryDto;
import uz.BTService.btservice.controller.dto.RegionDto;
import uz.BTService.btservice.controller.dto.dtoUtil.HttpResponse;
import uz.BTService.btservice.controller.dto.request.RegionCreateRequestDto;
import uz.BTService.btservice.controller.dto.request.RegionUpdateRequestDto;
import uz.BTService.btservice.entity.CategoryEntity;
import uz.BTService.btservice.entity.RegionEntity;
import uz.BTService.btservice.service.RegionService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/region")
@RequiredArgsConstructor
@Tag(name = "Region", description = "This Region CRUD")
public class RegionController {

    private final RegionService regionService;

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @Operation(summary = "This method for post", description = "This method Region add")
    @PostMapping("/add")
    public HttpResponse<Object> addRegion(@RequestBody RegionCreateRequestDto regionDto) {
        HttpResponse<Object> response = HttpResponse.build(false);

        RegionEntity region = RegionConvert.convertToEntity(regionDto);
        boolean regionSave = regionService.addObject(region);
        return response
                .code(HttpResponse.Status.OK)
                .success(true)
                .body(regionSave)
                .message(HttpResponse.Status.OK.name());

    }

    @PreAuthorize("permitAll()")
    @Operation(summary = "This method for getId", description = "This method Region getId")
    @GetMapping("/get/tree/{id}")
    public HttpResponse<Object> getRegionIdTree(@PathVariable Integer id) {
        HttpResponse<Object> response = HttpResponse.build(false);

        RegionEntity region = regionService.getObjectByIdTree(id);
        RegionDto fromTree = RegionConvert.fromTree(region);
        return response
                .code(HttpResponse.Status.OK)
                .success(true)
                .body(fromTree)
                .message(HttpResponse.Status.OK.name());

    }

    @PreAuthorize("permitAll()")
    @Operation(summary = "This method for getId", description = "This method Region getId")
    @GetMapping("/get/{id}")
    public HttpResponse<Object> getRegionId(@PathVariable Integer id) {

        HttpResponse<Object> response = HttpResponse.build(false);

        RegionEntity region = regionService.getObjectById(id);
        RegionDto responseRegionDto = RegionConvert.fromNoTree(region);

        return response
                .code(HttpResponse.Status.OK)
                .success(true)
                .body(responseRegionDto)
                .message(HttpResponse.Status.OK.name());
    }

    @PreAuthorize("permitAll()")
    @Operation(summary = "This method for getAll", description = "This method user getAll")
    @GetMapping("/get/all")
    public HttpResponse<Object> getAllRegion() {
        HttpResponse<Object> response = HttpResponse.build(false);
        List<RegionEntity> allRegion = regionService.getAllObject();
        response
                .code(HttpResponse.Status.OK)
                .success(true)
                .body(RegionConvert.fromNoTree(allRegion))
                .message(HttpResponse.Status.OK.name());


        return response;
    }

    @PreAuthorize("permitAll()")
    @Operation(summary = "This method for getAll", description = "This method user getAll")
    @GetMapping("/get/all-tree")
    public HttpResponse<Object> getAllTreeRegion() {
        HttpResponse<Object> response = HttpResponse.build(false);

        List<RegionEntity> allRegionTree = regionService.getAllObjectTree();
        List<RegionDto> regionTreeList = RegionConvert.fromTree(allRegionTree);
        response
                .code(HttpResponse.Status.OK)
                .success(true)
                .body(regionTreeList)
                .message(HttpResponse.Status.OK.name());


        return response;
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @Operation(summary = "This method for Post", description = "This method user update")
    @PatchMapping("/update/{id}")
    public HttpResponse<Object> update(@RequestBody RegionUpdateRequestDto regionDto, @PathVariable Integer id) {
        HttpResponse<Object> response = HttpResponse.build(false);
        RegionEntity regionEntity = RegionConvert.convertToEntity(regionDto);
        boolean isUpdate = regionService.updateObject(regionEntity, id, null);

        response.code(HttpResponse.Status.OK)
                .success(true)
                .body(isUpdate)
                .message(HttpResponse.Status.OK.name());

        return response;
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @Operation(summary = "This method for Delete", description = "This method user delete")
    @DeleteMapping("/delete/{id}")
    public HttpResponse<Object> deleteRegion(@PathVariable Integer id) {
        HttpResponse<Object> response = HttpResponse.build(false);

        regionService.delete(id);

        return response.code(HttpResponse.Status.OK)
                .success(true)
                .body(true)
                .message(HttpResponse.Status.OK.name());

    }
}
