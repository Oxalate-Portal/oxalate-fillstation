package io.oxalate.fillstation.service;

import io.oxalate.fillstation.entity.Cylinder;
import io.oxalate.fillstation.entity.FillEntry;
import io.oxalate.fillstation.entity.FillStatus;
import io.oxalate.fillstation.repository.CylinderRepository;
import io.oxalate.fillstation.repository.FillEntryRepository;
import io.oxalate.fillstation.api.request.FillEntryRequest;
import io.oxalate.fillstation.api.response.FillEntryResponse;
import io.oxalate.fillstation.api.response.GasUsageSummary;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class FillEntryService {

    private static final int EDIT_WINDOW_HOURS = 24;
    private static final int SCALE = 4;

    private final FillEntryRepository fillEntryRepository;
    private final CylinderRepository cylinderRepository;
    private final EmailService emailService;

    public List<FillEntryResponse> getFills(Long userId) {
        return fillEntryRepository.findByUserId(userId).stream()
                .map(f -> toResponse(f, isEditable(f)))
                .toList();
    }

    public FillEntryResponse getFill(Long userId, Long fillId) {
        FillEntry fill = fillEntryRepository.findByIdAndUserId(fillId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fill entry not found"));
        return toResponse(fill, isEditable(fill));
    }

    public FillEntryResponse create(Long userId, FillEntryRequest request) {
        Cylinder cylinder = cylinderRepository.findByIdAndUserId(request.getCylinderId(), userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cylinder not found"));

        BigDecimal gasAdded = calculateGasAdded(cylinder.getVolume(), request.getStartPressure(), request.getEndPressure());
        BigDecimal o2Added = calculateO2Added(cylinder.getVolume(), request.getStartPressure(),
                request.getEndPressure(), request.getStartO2Percentage(), request.getEndO2Percentage());
        BigDecimal heAdded = calculateHeAdded(cylinder.getVolume(), request.getStartPressure(),
                request.getEndPressure(), request.getStartHePercentage(), request.getEndHePercentage());

        FillEntry fill = FillEntry.builder()
                .userId(userId)
                .cylinderId(cylinder.getId())
                .fillDate(request.getFillDate())
                .startPressure(request.getStartPressure())
                .endPressure(request.getEndPressure())
                .startO2Percentage(request.getStartO2Percentage())
                .startHePercentage(request.getStartHePercentage())
                .endO2Percentage(request.getEndO2Percentage())
                .endHePercentage(request.getEndHePercentage())
                .o2Added(o2Added)
                .heAdded(heAdded)
                .gasAdded(gasAdded)
                .notes(request.getNotes())
                .status(FillStatus.ACTIVE)
                .updatedAt(LocalDateTime.now())
                .build();

        return toResponse(fillEntryRepository.save(fill), true);
    }

    public FillEntryResponse update(Long userId, Long fillId, FillEntryRequest request) {
        FillEntry fill = fillEntryRepository.findByIdAndUserId(fillId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fill entry not found"));

        if (!isEditable(fill)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Fill entry is no longer editable");
        }

        Cylinder cylinder = cylinderRepository.findByIdAndUserId(request.getCylinderId(), userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cylinder not found"));

        BigDecimal gasAdded = calculateGasAdded(cylinder.getVolume(), request.getStartPressure(), request.getEndPressure());
        BigDecimal o2Added = calculateO2Added(cylinder.getVolume(), request.getStartPressure(),
                request.getEndPressure(), request.getStartO2Percentage(), request.getEndO2Percentage());
        BigDecimal heAdded = calculateHeAdded(cylinder.getVolume(), request.getStartPressure(),
                request.getEndPressure(), request.getStartHePercentage(), request.getEndHePercentage());

        fill.setCylinderId(cylinder.getId());
        fill.setFillDate(request.getFillDate());
        fill.setStartPressure(request.getStartPressure());
        fill.setEndPressure(request.getEndPressure());
        fill.setStartO2Percentage(request.getStartO2Percentage());
        fill.setStartHePercentage(request.getStartHePercentage());
        fill.setEndO2Percentage(request.getEndO2Percentage());
        fill.setEndHePercentage(request.getEndHePercentage());
        fill.setO2Added(o2Added);
        fill.setHeAdded(heAdded);
        fill.setGasAdded(gasAdded);
        fill.setNotes(request.getNotes());

        return toResponse(fillEntryRepository.save(fill), true);
    }

    public void delete(Long userId, Long fillId) {
        FillEntry fill = fillEntryRepository.findByIdAndUserId(fillId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fill entry not found"));
        if (!isEditable(fill)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Fill entry is no longer editable");
        }
        fillEntryRepository.delete(fill);
    }

    public GasUsageSummary getGasUsage(Long userId) {
        Object[] totals = fillEntryRepository.sumsByUserId(userId);
        Object[] sinceZero = fillEntryRepository.sumsSinceLastZero(userId);

        return GasUsageSummary.builder()
                .totalO2Added(toBigDecimal(totals[0]))
                .totalHeAdded(toBigDecimal(totals[1]))
                .totalGasAdded(toBigDecimal(totals[2]))
                .sinceLastZeroO2Added(toBigDecimal(sinceZero[0]))
                .sinceLastZeroHeAdded(toBigDecimal(sinceZero[1]))
                .sinceLastZeroGasAdded(toBigDecimal(sinceZero[2]))
                .build();
    }

    public void zeroFills(Long userId, String userEmail, String userName, String userLanguage) {
        List<FillEntry> activeFills = fillEntryRepository.findByUserIdAndStatus(userId, FillStatus.ACTIVE);
        List<FillEntry> lockedFills = fillEntryRepository.findByUserIdAndStatus(userId, FillStatus.LOCKED);

        activeFills.forEach(f -> f.setStatus(FillStatus.ZEROED));
        lockedFills.forEach(f -> f.setStatus(FillStatus.ZEROED));

        fillEntryRepository.saveAll(activeFills);
        fillEntryRepository.saveAll(lockedFills);

        emailService.sendFillsZeroedEmail(userEmail, userName, userLanguage);
    }

    public void lockExpiredFills() {
        LocalDateTime lockBefore = LocalDateTime.now().minusHours(EDIT_WINDOW_HOURS);
        List<FillEntry> toLock = fillEntryRepository.findByStatus(FillStatus.ACTIVE).stream()
                .filter(f -> f.getCreatedAt() != null && f.getCreatedAt().isBefore(lockBefore))
                .toList();
        toLock.forEach(f -> f.setStatus(FillStatus.LOCKED));
        fillEntryRepository.saveAll(toLock);
    }

    // Gas calculation: gasAdded = volume * (endPressure - startPressure)
    public BigDecimal calculateGasAdded(BigDecimal volume, BigDecimal startPressure, BigDecimal endPressure) {
        return volume.multiply(endPressure.subtract(startPressure)).setScale(SCALE, RoundingMode.HALF_UP);
    }

    // o2Added = volume * (endPressure * endO2% - startPressure * startO2%) / 100
    public BigDecimal calculateO2Added(BigDecimal volume, BigDecimal startPressure, BigDecimal endPressure,
                                        BigDecimal startO2Pct, BigDecimal endO2Pct) {
        BigDecimal endPart = endPressure.multiply(endO2Pct);
        BigDecimal startPart = startPressure.multiply(startO2Pct);
        return volume.multiply(endPart.subtract(startPart))
                .divide(BigDecimal.valueOf(100), SCALE, RoundingMode.HALF_UP);
    }

    // heAdded = volume * (endPressure * endHe% - startPressure * startHe%) / 100
    public BigDecimal calculateHeAdded(BigDecimal volume, BigDecimal startPressure, BigDecimal endPressure,
                                        BigDecimal startHePct, BigDecimal endHePct) {
        BigDecimal endPart = endPressure.multiply(endHePct);
        BigDecimal startPart = startPressure.multiply(startHePct);
        return volume.multiply(endPart.subtract(startPart))
                .divide(BigDecimal.valueOf(100), SCALE, RoundingMode.HALF_UP);
    }

    private boolean isEditable(FillEntry fill) {
        if (fill.getStatus() == FillStatus.ZEROED || fill.getStatus() == FillStatus.LOCKED) {
            return false;
        }
        if (fill.getCreatedAt() == null) {
            return true;
        }
        return fill.getCreatedAt().isAfter(LocalDateTime.now().minusHours(EDIT_WINDOW_HOURS));
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value == null) return BigDecimal.ZERO;
        if (value instanceof BigDecimal bd) return bd;
        return new BigDecimal(value.toString());
    }

    private FillEntryResponse toResponse(FillEntry f, boolean editable) {
        return FillEntryResponse.builder()
                .id(f.getId())
                .userId(f.getUserId())
                .cylinderId(f.getCylinderId())
                .fillDate(f.getFillDate())
                .startPressure(f.getStartPressure())
                .endPressure(f.getEndPressure())
                .startO2Percentage(f.getStartO2Percentage())
                .startHePercentage(f.getStartHePercentage())
                .endO2Percentage(f.getEndO2Percentage())
                .endHePercentage(f.getEndHePercentage())
                .o2Added(f.getO2Added())
                .heAdded(f.getHeAdded())
                .gasAdded(f.getGasAdded())
                .notes(f.getNotes())
                .status(f.getStatus().name())
                .createdAt(f.getCreatedAt())
                .updatedAt(f.getUpdatedAt())
                .editable(editable)
                .build();
    }
}
