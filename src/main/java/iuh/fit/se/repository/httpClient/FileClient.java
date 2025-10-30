package iuh.fit.se.repository.httpClient;

import iuh.fit.se.configuration.AuthenticationRequestInterceptor;
import iuh.fit.se.dto.request.DeleteRequest;
import iuh.fit.se.dto.response.ApiResponse;
import iuh.fit.se.dto.response.FileClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "file-service",configuration = {AuthenticationRequestInterceptor.class})
public interface FileClient {
    @PostMapping(value = "/s3/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    FileClientResponse uploadFile(@RequestPart("files") List<MultipartFile> files);
    @PostMapping(value = "/s3/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    FileClientResponse deleteByUrl(@RequestBody DeleteRequest deleteRequest);
}
