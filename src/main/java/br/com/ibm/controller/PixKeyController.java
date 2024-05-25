package br.com.ibm.controller;

import br.com.ibm.dto.PixKeyDto;
import br.com.ibm.service.PixKeyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pixkey")
public class PixKeyController {

    private final static Logger logger = LoggerFactory.getLogger(PixKeyController.class);
    private final PixKeyService pixKeyService;


    public PixKeyController(PixKeyService pixKeyService) {
        this.pixKeyService = pixKeyService;
    }

    @PostMapping
    public ResponseEntity<String> createPixKey(@RequestBody PixKeyDto pixKeyDto) {
        logger.info("Calling createPixKey {} {}", pixKeyDto.getType(), pixKeyDto.getKeyValue());
        try {
            return ResponseEntity.ok(pixKeyService.createPixKey(pixKeyDto).getKeyValue());
        } catch (IllegalArgumentException ie) {
            logger.error("ERROR: {}", ie.getMessage());
            return ResponseEntity.badRequest().body(ie.getMessage());
        }
    }
}

