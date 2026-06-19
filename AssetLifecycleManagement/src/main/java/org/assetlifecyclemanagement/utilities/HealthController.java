package org.assetlifecyclemanagement.utilities;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@Tag(name = "Health", description = "Checks health of the application")
public class HealthController {

    @GetMapping()
    public ResponseEntity<String> getHealth() {
        return new ResponseEntity<>("Health Ok!", HttpStatus.OK);
    }

}
