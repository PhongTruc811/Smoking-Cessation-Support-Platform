package com.group_7.backend.service;

import com.group_7.backend.dto.QuitPlanDto;
import com.group_7.backend.entity.enums.QuitPlanStatusEnum;

import java.util.List;

public interface IQuitPlanService extends ICRUDService<QuitPlanDto, QuitPlanDto, Long>{
    List<QuitPlanDto> getByUserId(Long userId);
    QuitPlanDto updateStatus(Long id, QuitPlanStatusEnum status);
}
