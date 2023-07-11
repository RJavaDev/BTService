package uz.BTService.btservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import uz.BTService.btservice.controller.convert.AttachConvert;
import uz.BTService.btservice.controller.dto.response.AttachDownloadDTO;
import uz.BTService.btservice.controller.dto.dtoUtil.HttpResponse;
import uz.BTService.btservice.controller.dto.response.AttachResponseDto;
import uz.BTService.btservice.service.AttachService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/attach")
@RequiredArgsConstructor
public class AttachController {

    private final AttachService service;

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Upload Image", description = "This method is used to upload an image")
    @PostMapping("/upload")
    public HttpResponse<Object> upload(@RequestParam MultipartFile file){

        HttpResponse<Object> response = new HttpResponse<>(true);
        AttachResponseDto attach = AttachConvert.from(service.saveAttach(file));
        return response
                .code(HttpResponse.Status.OK)
                .message(HttpStatus.OK.name())
                .body(attach);

    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Upload Image", description = "This method is used to upload an image")
    @PostMapping("/uploads")
    public HttpResponse<Object> uploadAttachList(@RequestParam List<MultipartFile> file){

        HttpResponse<Object> response = new HttpResponse<>(true);
        List<AttachResponseDto> attachDtoList = AttachConvert.from(service.saveAttach(file));
        return response
                .code(HttpResponse.Status.OK)
                .message(HttpStatus.OK.name())
                .body(attachDtoList);

    }



    @Operation(summary = "Download Image", description = "This method is used to download an image")
    @GetMapping("/download/{fineName}")
    public ResponseEntity<Resource> download(@PathVariable("fineName") String fileName) {
        AttachDownloadDTO result = service.download(fileName);

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(result.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + result.getResource().getFilename() + "\"").body(result.getResource());
    }


    @Operation(summary = "Get Attachments with Pagination", description = "This method retrieves attachments with pagination")
    @GetMapping("/get")
    public ResponseEntity<?> getWithPage(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        Page<AttachResponseDto> result = service.getWithPage(page, size);
        return ResponseEntity.ok(result);
    }


    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('ADMIN','CONTEND_MANAGER','SUPER_ADMIN')")
    @Operation(summary = "Delete Attachment by ID", description = "This method is used to delete an attachment by its fileName")
    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<?> deleteById(@PathVariable("fileName") String fileName) {
        String result = service.deleteById(fileName);
        return ResponseEntity.ok(result);
    }

}
