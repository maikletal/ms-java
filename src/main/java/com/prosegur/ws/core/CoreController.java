package com.prosegur.ws.core;

import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller base
 */
@RestController
public class CoreController {

    private static final Logger log = LoggerFactory.getLogger(CoreController.class);

    /**
     * Method for handle exceptions
     * @param e Exception received
     * @return Error response to client
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> genericException(Exception e) {
        log.error("Error Exception: [{}]", e.getMessage());
        return new ResponseEntity<>("Error, excepcion no controlada: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
