package com.prosegur.ws.rest;

import com.prosegur.integration.ExternalService;
import com.prosegur.integration.RabbitMQClient;
import com.prosegur.ws.core.CoreController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controller example
 */
@RestController
@RequestMapping(GeneralController.API_V1)
@Api(value = "General Controller")
public class GeneralController extends CoreController {

    private static final Logger logger = LoggerFactory.getLogger(GeneralController.class);
    static final String API_V1 = "v1/general";
    @Value("${msjava.param.example}")
    private String param;

    @Autowired
    ExternalService externalService;

    @Autowired
    RabbitMQClient rabbitMQClient;

    /**
     * Automatic request for not found endpoints
     * @return status 404
     */
    @RequestMapping(value = "/**")
    @ApiOperation(value = "Indica que se ha realizado una petición inexistente")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Petición no encontrada"),
            @ApiResponse(code = 500, message = "Servicio no operativo")
    })
    public ResponseEntity<Object> notFound() {
        logger.info("Petición no encontrada");
        return new ResponseEntity<>("Petición no encontrada", HttpStatus.NOT_FOUND);
    }

    /**
     * Automatic request for test service
     * @return status
     */
    @GetMapping(value = "/myService")
    @ApiOperation(value = "Servicio de prueba")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Servicio operativo"),
    })
    public ResponseEntity<Object> myService() {
        logger.info("Servicio de prueba funcionando");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Request for checking endpoint
     * @param request Received parameters
     * @return Request check response
     */
    @GetMapping(value = "/check")
    @ApiOperation(value = "Chequeo de servicio levantado y testeo de variable")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Servicio operativo, variable correcta"),
            @ApiResponse(code = 400, message = "Servicio operativo, variable incorrecta"),
            @ApiResponse(code = 404, message = "Servicio operativo, variable no encontrada"),
            @ApiResponse(code = 500, message = "Servicio no operativo")
    })
    public ResponseEntity<Object> check(
            @RequestParam Map<String, String> request
    ) {
        logger.info("Service in construction...");
        String val = request.get("val");
        if (val == null) {
            return new ResponseEntity<>("val is not found", HttpStatus.NOT_FOUND);
        }
        try {
            int value = Integer.parseInt(val);
            return new ResponseEntity<>(value + " is a number", HttpStatus.OK);
        }
        catch (NumberFormatException e) {
            logger.info("Failed to parse val = " + val);
            return new ResponseEntity<>(val + " is not a number", HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * Request for test infinity loop detection in Veracode
     * @return status
     */
    @GetMapping(value = "/infinityLoop")
    @ApiOperation(value = "Servicio infinito")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Salida correcta del bucle"),
            @ApiResponse(code = 400, message = "Salida incorrecta del bucle"),
            @ApiResponse(code = 500, message = "Servicio no operativo")
    })
    public ResponseEntity<Object> infinityLoop() {
        logger.info("Service in construction...");
        try {
            logger.info("Inicio bucle infinito para prueba de detección en Veracode...");
            int i = 0;
            while (true) {
                i++;
                logger.info("Ejecución número: " + i);
                if (i==-9) { break;}
            }
            return new ResponseEntity<>("Salida correcta del bucle", HttpStatus.OK);
        }
        catch (Exception e) {
            logger.info("Error que provoca salida del bucle: " + e.getMessage());
            return new ResponseEntity<>("Salida incorrecta del bucle: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * Request for checking location param
     * @return status
     */
    @GetMapping(value = "/example")
    @ApiOperation(value = "Chequeo de servicio levantado y testeo de variable location")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Servicio operativo, variable correcta"),
            @ApiResponse(code = 400, message = "Servicio operativo, variable no encontrada"),
            @ApiResponse(code = 500, message = "Servicio no operativo")
    })
    public ResponseEntity<Object> checkLocation() {
        logger.info("Service in construction...");
        logger.info("Location is: " + this.param);
        if (this.param == null) {
            return new ResponseEntity<>("location_error", HttpStatus.BAD_REQUEST);
        } else  {
            return new ResponseEntity<>(this.param, HttpStatus.OK);
        }

    }

    /**
     * Request for checking aemet
     * @return status
     */
    @GetMapping(value = "/aemet")
    @ApiOperation(value = "Chequeo de servicio AEMET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Petición correcta"),
            @ApiResponse(code = 400, message = "Petición incorrecta"),
            @ApiResponse(code = 500, message = "Servicio no operativo")
    })
    public ResponseEntity<Object> checkAemet() {
        logger.info("Service in testing...");
        String apiKey = "jyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqbW9udGVyb2dAYWVtZXQuZXMiLCJqdGkiOiI3NDRiYmVhMy02NDEyLTQxYWMtYmYzOC01MjhlZWJlM2FhMWEiLCJleHAiOjE0NzUwNTg3ODcsImlzcyI6IkFFTUVUIiwiaWF0IjoxNDc0NjI2Nzg3LCJ1c2VySWQiOiI3NDRiYmVhMy02NDEyLTQxYWMtYmYzOC01MjhlZWJlM2FhMWEiLCJyb2xlIjoiIn0.xh3LstTlsP9h5cxz3TLmYF4uJwhOKzA0B6-vH8lPGGw";
        String url = "https://opendata.aemet.es/opendata/api/valores/climatologicos/inventarioestaciones/todasestaciones/";
        Pair<Integer, Object> result = externalService.sendRequest(url, apiKey);
        return new ResponseEntity<>(result.getRight(), HttpStatus.resolve(result.getLeft()));
    }

    /**
     * Send message to RabbitMQ
     * @return status
     */
    @GetMapping(value = "/send")
    @ApiOperation(value = "Envía mensajes a RabbitMQ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Mensaje enviado"),
            @ApiResponse(code = 400, message = "Fallo al enviar el mensaje"),
            @ApiResponse(code = 500, message = "Servicio no operativo")
    })
    public ResponseEntity<Object> sendMessage(
            @RequestParam String queue,
            @RequestParam String msg
    ) {
        logger.info("Service in testing...");
        rabbitMQClient.connect();
        rabbitMQClient.recv(queue);
        String result = rabbitMQClient.send(queue, msg);
        return new ResponseEntity<>(result, "ok".equals(result) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

}
