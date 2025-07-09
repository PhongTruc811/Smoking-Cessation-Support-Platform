package com.group_7.backend.service.impl;

import com.group_7.backend.dto.QuitMethodDto;
import com.group_7.backend.dto.QuitMethodOptionDto;
import com.group_7.backend.entity.QuitMethod;
import com.group_7.backend.entity.QuitMethodOption;
import com.group_7.backend.exception.ResourceNotFoundException;
import com.group_7.backend.mapper.QuitMethodMapper;
import com.group_7.backend.mapper.QuitMethodOptionMapper;
import com.group_7.backend.repository.QuitMethodRepository;
import com.group_7.backend.repository.QuitMethodOptionRepository;
import com.group_7.backend.service.IQuitMethodService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class QuitMethodServiceImp implements IQuitMethodService {

    private final QuitMethodRepository quitMethodRepository;
    private final QuitMethodOptionRepository quitMethodOptionRepository;
    private final QuitMethodMapper quitMethodMapper;
    private final QuitMethodOptionMapper quitMethodOptionMapper;

    public QuitMethodServiceImp(QuitMethodRepository quitMethodRepository,
                                QuitMethodOptionRepository quitMethodOptionRepository,
                                QuitMethodMapper quitMethodMapper,
                                QuitMethodOptionMapper quitMethodOptionMapper) {
        this.quitMethodRepository = quitMethodRepository;
        this.quitMethodOptionRepository = quitMethodOptionRepository;
        this.quitMethodMapper = quitMethodMapper;
        this.quitMethodOptionMapper = quitMethodOptionMapper;
    }

    @Override
    @Transactional
    public QuitMethodDto create(QuitMethodDto quitMethodDto) {
        QuitMethod quitMethod = quitMethodMapper.toEntity(quitMethodDto);

        Set<QuitMethodOption> managedOptions = new HashSet<>();
        if (quitMethodDto.getOptions() != null && !quitMethodDto.getOptions().isEmpty()) {
            for (QuitMethodOptionDto optionDto : quitMethodDto.getOptions()) {
                QuitMethodOption option = null;
                if (optionDto.getId() != null) {
                    option = quitMethodOptionRepository.findById(optionDto.getId())
                            .orElse(null);
                }

                if (option == null) {
                    option = quitMethodOptionMapper.toEntity(optionDto);
                    option.setQuitMethod(quitMethod);
                } else {
                    option.setOptionText(optionDto.getOptionText());
                    option.setOptionDescription(optionDto.getOptionDescription());
                    option.setOptionNoti(optionDto.getOptionNoti());
                    option.setQuitMethod(quitMethod);
                }
                managedOptions.add(option);
            }
        }
        quitMethod.setOptions(managedOptions);

        QuitMethod savedQuitMethod = quitMethodRepository.save(quitMethod);

        return quitMethodMapper.toDto(savedQuitMethod);
    }

    @Override
    @Transactional(readOnly = true)
    public QuitMethodDto getById(Long id) {
        QuitMethod quitMethod = quitMethodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QuitMethod not found with id: " + id));
        return quitMethodMapper.toDto(quitMethod);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuitMethodDto> getAll() {
        List<QuitMethod> quitMethods = quitMethodRepository.findAll();
        return quitMethodMapper.toDtoList(quitMethods);
    }

    @Override
    @Transactional
    public QuitMethodDto update(Long id, QuitMethodDto quitMethodDto) {
        QuitMethod existingQuitMethod = quitMethodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QuitMethod not found with id: " + id));

        existingQuitMethod.setMethodName(quitMethodDto.getMethodName());
        existingQuitMethod.setMethodType(quitMethodDto.getMethodType());

        Set<QuitMethodOption> updatedOptions = new HashSet<>();
        Set<Long> dtoOptionIds = quitMethodDto.getOptions() != null ?
                quitMethodDto.getOptions().stream()
                        .map(QuitMethodOptionDto::getId)
                        .collect(Collectors.toSet())
                : new HashSet<>();

        existingQuitMethod.getOptions().removeIf(existingOption ->
                !dtoOptionIds.contains(existingOption.getId())
        );

        if (quitMethodDto.getOptions() != null) {
            for (QuitMethodOptionDto optionDto : quitMethodDto.getOptions()) {
                if (optionDto.getId() != null) {
                    QuitMethodOption existingOption = existingQuitMethod.getOptions().stream()
                            .filter(opt -> opt.getId().equals(optionDto.getId()))
                            .findFirst()
                            .orElse(null);

                    if (existingOption != null) {
                        existingOption.setOptionText(optionDto.getOptionText());
                        existingOption.setOptionDescription(optionDto.getOptionDescription());
                        existingOption.setOptionNoti(optionDto.getOptionNoti());
                        updatedOptions.add(existingOption);
                    } else {
                        QuitMethodOption dbOption = quitMethodOptionRepository.findById(optionDto.getId())
                                .orElseGet(() -> {
                                    QuitMethodOption newOption = quitMethodOptionMapper.toEntity(optionDto);
                                    newOption.setQuitMethod(existingQuitMethod);
                                    return newOption;
                                });
                        dbOption.setQuitMethod(existingQuitMethod);
                        updatedOptions.add(dbOption);
                    }
                } else {
                    QuitMethodOption newOption = quitMethodOptionMapper.toEntity(optionDto);
                    newOption.setQuitMethod(existingQuitMethod);
                    updatedOptions.add(newOption);
                }
            }
        }
        Set<QuitMethodOption> optionsToAdd = updatedOptions.stream()
                .filter(opt -> !existingQuitMethod.getOptions().contains(opt))
                .collect(Collectors.toSet());

        optionsToAdd.forEach(option -> {
            if (option.getId() == null) {
                quitMethodOptionRepository.save(option);
            }
            existingQuitMethod.getOptions().add(option);
        });

        QuitMethod updatedQuitMethod = quitMethodRepository.save(existingQuitMethod);
        return quitMethodMapper.toDto(updatedQuitMethod);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!quitMethodRepository.existsById(id)) {
            throw new ResourceNotFoundException("QuitMethod not found with id: " + id);
        }
        quitMethodRepository.deleteById(id);
    }
}