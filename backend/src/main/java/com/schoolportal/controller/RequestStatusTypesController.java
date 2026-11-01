
package com.schoolportal.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolportal.dto.RequestStatusTypeDTO;
import com.schoolportal.service.RequestStatusTypeService;

@RestController
@RequestMapping("/RequestStatusTypesServlet")
public class RequestStatusTypesController {

    private final RequestStatusTypeService statusService;

    public RequestStatusTypesController(RequestStatusTypeService statusService) {
        this.statusService = statusService;
    }

    @GetMapping
    public ResponseEntity<List<RequestStatusTypeDTO>> getAllRequestStatusTypes() {

        List<RequestStatusTypeDTO> statuses =
                statusService.getAllRequestStatusTypes();

        return ResponseEntity.ok(statuses);
    }
}
