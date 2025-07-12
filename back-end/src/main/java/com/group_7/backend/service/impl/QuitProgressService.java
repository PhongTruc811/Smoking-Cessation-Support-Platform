package com.group_7.backend.service.impl;

import com.group_7.backend.dto.QuitProgressLogDto;
import com.group_7.backend.entity.QuitPlan;
import com.group_7.backend.entity.QuitProgressLog;
import com.group_7.backend.exception.ResourceNotFoundException;
import com.group_7.backend.mapper.QuitProgressLogMapper;
import com.group_7.backend.repository.QuitPlanRepository;
import com.group_7.backend.repository.QuitProgressLogRepository;
import com.group_7.backend.service.IQuitProgressLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuitProgressService implements IQuitProgressLogService {

    private final QuitProgressLogRepository quitProgressLogRepository;
    private final QuitPlanRepository quitPlanRepository;
    private final QuitProgressLogMapper quitProgressLogMapper;
    private final QuitPlanServiceImp quitPlanServiceImp;

    @Autowired
    public QuitProgressService(
            QuitProgressLogRepository quitProgressLogRepository,
            QuitPlanRepository quitPlanRepository,
            QuitProgressLogMapper quitProgressLogMapper,
            QuitPlanServiceImp quitPlanServiceImp
    ) {
        this.quitProgressLogRepository = quitProgressLogRepository;
        this.quitPlanRepository = quitPlanRepository;
        this.quitProgressLogMapper = quitProgressLogMapper;
        this.quitPlanServiceImp = quitPlanServiceImp;
    }

    @Override
    @Transactional
    public QuitProgressLogDto create(QuitProgressLogDto dto) {
        QuitPlan plan = quitPlanRepository.findById(dto.getQuitPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("QuitPlan not found with id: " + dto.getQuitPlanId()));

        if (quitProgressLogRepository.existsByCreatedAtAndQuitPlan_Id(dto.getCreatedAt(), plan.getId())) {
            throw new IllegalArgumentException("Progress Log for this date already exists!");
        }

        //Cập nhật số điếu thuốc giảm (tăng)
        quitPlanServiceImp.updateTotalSmoke(plan.getId(),plan.getDailySmoke()-dto.getCigarettesSmoked());

        QuitProgressLog log = quitProgressLogMapper.toEntity(dto, plan);
        QuitProgressLog saved = quitProgressLogRepository.save(log);
        return quitProgressLogMapper.toDto(saved);
    }

    @Override
    public QuitProgressLogDto getById(Long id) {
        QuitProgressLog log = quitProgressLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QuitProgressLog not found with id: " + id));
        return quitProgressLogMapper.toDto(log);
    }

    @Override
    public List<QuitProgressLogDto> getAll() {
        return quitProgressLogRepository.findAll().stream()
                .map(quitProgressLogMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public QuitProgressLogDto update(Long id, QuitProgressLogDto dto) {
        QuitProgressLog log = quitProgressLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QuitProgressLog not found with id: " + id));
        //Cập nhật số điếu thuốc giảm (tăng)
        int record=log.getCigarettesSmoked() - dto.getCigarettesSmoked();
        log.setCigarettesSmoked(dto.getCigarettesSmoked());
        log.setNotes(dto.getNotes());
        quitPlanServiceImp.updateTotalSmoke(dto.getQuitPlanId(), record);
        QuitProgressLog saved = quitProgressLogRepository.save(log);
        return quitProgressLogMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        QuitProgressLog log = quitProgressLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QuitProgressLog not found with id: " + id));
        quitProgressLogRepository.delete(log);
    }
}