package io.oxalate.fillstation.oxalate_fillstation.service;

import io.oxalate.fillstation.oxalate_fillstation.entity.Cylinder;
import io.oxalate.fillstation.oxalate_fillstation.repository.CylinderRepository;
import io.oxalate.fillstation.api.request.CylinderRequest;
import io.oxalate.fillstation.api.response.CylinderResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CylinderService {

    private final CylinderRepository cylinderRepository;

    public List<CylinderResponse> getCylinders(Long userId) {
        return cylinderRepository.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    public CylinderResponse create(Long userId, CylinderRequest request) {
        Cylinder cylinder = Cylinder.builder()
                .userId(userId)
                .name(request.getName())
                .volume(request.getVolume())
                .workingPressure(request.getWorkingPressure())
                .serialNumber(request.getSerialNumber())
                .build();
        return toResponse(cylinderRepository.save(cylinder));
    }

    public CylinderResponse update(Long userId, Long cylinderId, CylinderRequest request) {
        Cylinder cylinder = cylinderRepository.findByIdAndUserId(cylinderId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cylinder not found"));
        cylinder.setName(request.getName());
        cylinder.setVolume(request.getVolume());
        cylinder.setWorkingPressure(request.getWorkingPressure());
        cylinder.setSerialNumber(request.getSerialNumber());
        return toResponse(cylinderRepository.save(cylinder));
    }

    public void delete(Long userId, Long cylinderId) {
        Cylinder cylinder = cylinderRepository.findByIdAndUserId(cylinderId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cylinder not found"));
        cylinderRepository.delete(cylinder);
    }

    private CylinderResponse toResponse(Cylinder c) {
        return CylinderResponse.builder()
                .id(c.getId())
                .userId(c.getUserId())
                .name(c.getName())
                .volume(c.getVolume())
                .workingPressure(c.getWorkingPressure())
                .serialNumber(c.getSerialNumber())
                .build();
    }
}
