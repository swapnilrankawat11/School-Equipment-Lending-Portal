
package com.schoolportal.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schoolportal.dto.RequestStatusTypeDTO;
import com.schoolportal.entity.RequestStatusType;
import com.schoolportal.repository.RequestStatusTypeRepository;

@Service
public class RequestStatusTypeService {

    private final RequestStatusTypeRepository statusRepository;

    public RequestStatusTypeService(RequestStatusTypeRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Transactional(readOnly = true)
    public List<RequestStatusTypeDTO> getAllRequestStatusTypes() {

        List<RequestStatusType> statuses = statusRepository.findAll();

        return statuses.stream().map(status -> {

            RequestStatusTypeDTO dto = new RequestStatusTypeDTO();

            dto.setId(status.getId());
            dto.setStatusType(status.getStatusType());
            dto.setDescription(status.getDescription());

            dto.setCreatedBy(status.getCreatedBy());
            dto.setModifiedBy(status.getModifiedBy());

            dto.setCreatedAt(status.getCreatedAt());
            dto.setModifiedAt(status.getModifiedAt());

            return dto;

        }).toList();
    }
}
