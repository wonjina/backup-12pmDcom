package com.gabia.project.internproject.controller;

import com.gabia.project.internproject.common.helper.Notification;
import com.gabia.project.internproject.controller.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@Slf4j
public class CustomErrorController implements ErrorController {
    private static final String ERROR_PATH = "/error";
/*

    @GetMapping("/error")
    public RedirectView redirectRoot() {
         return new RedirectView("/");
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
*/


    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    @RequestMapping("/error")
    public ResponseEntity<ErrorResponseDto> handleError(HttpServletRequest request) {

        final ErrorResponseDto response;
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if(status.toString().equals(HttpStatus.UNAUTHORIZED.value()+"")) {
            response = ErrorResponseDto.of(HttpStatus.UNAUTHORIZED, Notification.UNAUTHORIZED.getMsg());
        } else {
            response = ErrorResponseDto.of(HttpStatus.NOT_FOUND, Notification.NOT_FOUND.getMsg());
        }
        HttpStatus httpStatus = HttpStatus.valueOf(Integer.valueOf(status.toString()));

        return new ResponseEntity(response, httpStatus);
    }
}
