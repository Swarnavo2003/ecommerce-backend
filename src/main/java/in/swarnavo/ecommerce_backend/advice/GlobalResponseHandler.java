package in.swarnavo.ecommerce_backend.advice;

import in.swarnavo.ecommerce_backend.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    private static final List<String> EXCLUDED_ROUTES = List.of(
            "/v3/api-docs",
            "/swagger-ui",
            "/actuator"
    );

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response
    ) {

        String path = request.getURI().getPath();

        boolean isAllowed = EXCLUDED_ROUTES
                .stream()
                .anyMatch(route -> request.getURI().getPath().contains(route));

        if(body instanceof ApiResponse<?> || body instanceof ErrorResponse || isAllowed) {
            return body;
        }

        int status = 200;
        if(response instanceof ServletServerHttpResponse) {
            HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
            status = servletResponse.getStatus();
        }

        ApiResponse<Object> apiResponse = new ApiResponse<>(body);
        apiResponse.setStatus(status);
        apiResponse.setPath(path);

        return apiResponse;
    }
}
