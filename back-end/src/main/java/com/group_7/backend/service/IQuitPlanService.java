package com.group_7.backend.service;

import com.group_7.backend.dto.QuitPlanDto;

import java.util.List;

public interface IQuitPlanService extends ICRUDService<QuitPlanDto, QuitPlanDto, Long>{
    List<QuitPlanDto> getByUserId(Long userId);
}
