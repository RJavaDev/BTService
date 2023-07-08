package uz.BTService.btservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.BTService.btservice.controller.convert.BannerConvert;
import uz.BTService.btservice.controller.convert.ProductConvert;
import uz.BTService.btservice.controller.dto.dtoUtil.HttpResponse;
import uz.BTService.btservice.controller.dto.request.BannerCreateRequestDto;
import uz.BTService.btservice.controller.dto.request.ProductCreateRequestDto;
import uz.BTService.btservice.controller.dto.response.BannerResponseDto;
import uz.BTService.btservice.controller.dto.response.ProductResponseForUserDto;
import uz.BTService.btservice.entity.BannerEntity;
import uz.BTService.btservice.entity.ProductEntity;
import uz.BTService.btservice.service.BannerService;

import java.util.List;


@RestController
@RequestMapping("/api/v1/banner")
@RequiredArgsConstructor
@Tag(name = "Banner Controller", description = "This controller manages the banners on the website")
public class BannerController {

    private final BannerService bannerService;

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('CONTEND_MANAGER','SUPER_ADMIN')")
    @Operation(summary = "Add Banner", description = "This method adds a new banner and updates the banner with the given position, deleting the previous banner.")
    @PostMapping("/add")
    public HttpResponse<Object> addProduct(@RequestBody BannerCreateRequestDto bannerCreateRequestDto) {

        HttpResponse<Object> response = HttpResponse.build(false);

        BannerEntity bannerEntity = BannerConvert.convertToEntity(bannerCreateRequestDto);
        boolean save = bannerService.addObject(bannerEntity);

        return response
                .code(HttpResponse.Status.OK)
                .success(true)
                .body(save)
                .message("Banner added successfully and the previous banner at the given position has been deleted.");
    }


    @Operation(summary = "Get All Banners", description = "This method retrieves all banners sorted by position.")
    @GetMapping("/get/all")
    public HttpResponse<Object> getBannerAll() {
        HttpResponse<Object> response = HttpResponse.build(false);

        List<BannerEntity> banner = bannerService.getAllObject();
        List<BannerResponseDto> bannerResponseDtoList = BannerConvert.from(banner);
        return response
                .code(HttpResponse.Status.OK)
                .success(true)
                .body(bannerResponseDtoList)
                .message("Successfully retrieved all banners sorted by position.");

    }

    @Operation(summary = "Get Banner by ID", description = "This method retrieves a banner by its ID.")
    @GetMapping("/get/{id}")
    public HttpResponse<Object> getBannerId(@PathVariable Integer id) {
        HttpResponse<Object> response = HttpResponse.build(false);

        BannerEntity banner = bannerService.getObjectById(id);
        BannerResponseDto bannerResponseDto = BannerConvert.from(banner);

        return response
                .code(HttpResponse.Status.OK)
                .success(true)
                .body(bannerResponseDto)
                .message(HttpResponse.Status.OK.name());

    }

}
