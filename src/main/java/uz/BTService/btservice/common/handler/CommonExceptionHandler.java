package uz.BTService.btservice.common.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
import org.hibernate.validator.internal.util.annotation.ConstraintAnnotationDescriptor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.BTService.btservice.common.handler.pojo.FieldErrorResponse;
import uz.BTService.btservice.common.handler.pojo.TraceableErrorResponse;
import uz.BTService.btservice.exceptions.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.metadata.ConstraintDescriptor;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class CommonExceptionHandler {
    private final Environment environment;

    @ExceptionHandler({BindException.class})
    @ResponseStatus(BAD_REQUEST)
    List<FieldErrorResponse> handle(BindException ex) {
        return Stream.concat(
                ex.getBindingResult().getFieldErrors().stream()
                        .map(fieldError -> FieldErrorResponse.builder()
                                .field(fieldError.getField())
                                .message(fieldError.getDefaultMessage())
                                .code(fieldError.getCode())
                                .build()
                        ),
                ex.getBindingResult().getGlobalErrors().stream()
                        .map(globalError -> FieldErrorResponse.builder()
                                .field(globalError.getObjectName())
                                .message(globalError.getDefaultMessage())
                                .code(globalError.getCode())
                                .build())
        ).toList();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(BAD_REQUEST)
    FieldErrorResponse handle(MissingServletRequestParameterException ex) {
        return FieldErrorResponse.builder()
                .code("ParameterMissing")
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    List<FieldErrorResponse> handle(ConstraintViolationException ex) {
        return ex.getConstraintViolations().stream()
                .map(constraintViolation -> FieldErrorResponse
                        .builder()
                        .field(extractField(constraintViolation.getPropertyPath()))
                        .code(extractCode(constraintViolation.getConstraintDescriptor()))
                        .message(constraintViolation.getMessage())
                        .build())
                .toList();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(BAD_REQUEST)
    FieldErrorResponse handle(HttpMessageNotReadableException ex) {
        return FieldErrorResponse.builder()
                .message(ex.getMessage())
                .code("InvalidRequest")
                .build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    FieldErrorResponse handle(IllegalArgumentException ex) {
        return FieldErrorResponse.builder()
                .message(ex.getMessage())
                .code("IllegalArgument")
                .build();
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(CONFLICT)
    FieldErrorResponse handle(IllegalStateException ex) {
        return FieldErrorResponse.builder()
                .message(ex.getMessage())
                .code("IllegalState")
                .build();
    }

    @ExceptionHandler(URISyntaxException.class)
    @ResponseStatus(BAD_REQUEST)
    FieldErrorResponse handle(URISyntaxException ex) {
        return FieldErrorResponse.builder()
                .message(ex.getMessage())
                .code("URISyntaxException")
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    FieldErrorResponse handle(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        String errorMessage = bindingResult.getFieldErrors()
                .stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Validation failed");

        return FieldErrorResponse.builder()
                .message(errorMessage)
                .code("URISyntaxException")
                .build();
    }



    @ExceptionHandler(RecordNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    FieldErrorResponse handle(RecordNotFoundException ex) {
        return FieldErrorResponse.builder()
                .message(ex.getMessage())
                .code("RecordNotFound")
                .build();
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    FieldErrorResponse handle(UsernameNotFoundException ex) {
        return FieldErrorResponse.builder()
                .message(ex.getMessage())
                .code("User not found")
                .build();
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    FieldErrorResponse handle(CategoryNotFoundException ex) {
        return FieldErrorResponse.builder()
                .message(ex.getMessage())
                .code("Category not found")
                .build();
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    FieldErrorResponse handle(ProductNotFoundException ex) {
        return FieldErrorResponse.builder()
                .message(ex.getMessage())
                .code("Product not found")
                .build();
    }

    @ExceptionHandler(RegionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    FieldErrorResponse handle(RegionNotFoundException ex) {
        return FieldErrorResponse.builder()
                .message(ex.getMessage())
                .code("Region not found")
                .build();
    }

    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    FieldErrorResponse handle(FileNotFoundException ex) {
        return FieldErrorResponse.builder()
                .message(ex.getMessage())
                .code("File not found")
                .build();
    }

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    FieldErrorResponse handle(OrderNotFoundException ex) {
        return FieldErrorResponse.builder()
                .message(ex.getMessage())
                .code("Order not found")
                .build();
    }

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(CONFLICT)
    FieldErrorResponse handle(AlreadyExistsException ex) {
        return FieldErrorResponse.builder()
                .message(ex.getMessage())
                .code("RecordAlreadyExist")
                .build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(FORBIDDEN)
    FieldErrorResponse handle(AccessDeniedException ex) {
        return FieldErrorResponse.builder()
                .message(ex.getMessage())
                .code("AccessDenied")
                .build();
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(UNAUTHORIZED)
    FieldErrorResponse handle(AuthenticationException ex) {
        return FieldErrorResponse.builder()
                .message(ex.getMessage())
                .code("Authentication")
                .build();
    }

    @ExceptionHandler(UserUnauthorizedAction.class)
    @ResponseStatus(UNAUTHORIZED)
    FieldErrorResponse handle(UserUnauthorizedAction ex) {
        return FieldErrorResponse.builder()
                .message(ex.getMessage())
                .code("Authentication")
                .build();
    }

    @ExceptionHandler(UserDataException.class)
    @ResponseStatus(BAD_REQUEST)
    FieldErrorResponse handle(UserDataException ex) {
        return FieldErrorResponse.builder()
                .message(ex.getMessage())
                .code(BAD_REQUEST.toString())
                .build();
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(BAD_REQUEST)
    TraceableErrorResponse handle(Exception ex) {
        return TraceableErrorResponse.builder()
                .message(ex.getMessage())
                .correlationId(UUID.randomUUID().toString())
                .stackTrace(isNonProdEnv() ? ExceptionUtils.getStackTrace(ex) : null)
                .build();
    }

    private boolean isNonProdEnv() {
        return Arrays.stream(environment.getActiveProfiles())
                .anyMatch(profile -> "dev".equals(profile) || "qa".equals(profile));
    }

    private String extractCode(ConstraintDescriptor<?> constraintDescriptor) {
        ConstraintAnnotationDescriptor<?> annotationDescriptor = ((ConstraintDescriptorImpl<?>) constraintDescriptor).getAnnotationDescriptor();
        Class<?> type = annotationDescriptor.getType();
        return type.getName().replace(type.getPackageName(), "")
                .replaceFirst("\\.", "");
    }

    private String extractField(Path propertyPath) {
        String field = null;
        for (Path.Node node : propertyPath) {
            field = node.getName();
        }

        return field;
    }

}
