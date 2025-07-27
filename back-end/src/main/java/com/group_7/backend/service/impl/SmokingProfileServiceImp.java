package com.group_7.backend.service.impl;

import com.group_7.backend.dto.SmokingProfileDto;
import com.group_7.backend.dto.quiz.OptionDto;
import com.group_7.backend.dto.quiz.QuestionDto;
import com.group_7.backend.dto.request.UserQuizAnswerRequestDto;
import com.group_7.backend.entity.SmokingProfile;
import com.group_7.backend.entity.User;
import com.group_7.backend.exception.ResourceNotFoundException;
import com.group_7.backend.mapper.SmokingProfileMapper;
import com.group_7.backend.repository.QuizRepository;
import com.group_7.backend.repository.SmokingProfileRepository;
import com.group_7.backend.repository.UserRepository;
import com.group_7.backend.service.ISmokingProfileService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SmokingProfileServiceImp implements ISmokingProfileService {

    private final SmokingProfileRepository smokingProfileRepository;
    private final UserRepository userRepository;
    private final SmokingProfileMapper smokingProfileMapper;
    private final QuizRepository quizRepository;

    public SmokingProfileServiceImp(
            SmokingProfileRepository smokingProfileRepository,
            UserRepository userRepository,
            SmokingProfileMapper smokingProfileMapper, QuizRepository quizRepository) {
        this.smokingProfileRepository = smokingProfileRepository;
        this.userRepository = userRepository;
        this.smokingProfileMapper = smokingProfileMapper;
        this.quizRepository = quizRepository;
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_COACH') or hasAuthority('ROLE_ADMIN')")
    public SmokingProfileDto getById(Long id) {
        SmokingProfile entity = smokingProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SmokingProfile not found with id: " + id));
        return smokingProfileMapper.toDto(entity);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<SmokingProfileDto> getAll() {
        return smokingProfileRepository.findAll()
                .stream()
                .map(smokingProfileMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @PreAuthorize("#dto.userId == authentication.principal.id")
    public SmokingProfileDto update(Long id, SmokingProfileDto dto) {
        SmokingProfile profile = smokingProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SmokingProfile not found with id: " + id));

        profile.setCigarettesPerDay(dto.getCigarettesPerDay());
        profile.setCostPerPack(dto.getCostPerPack());
        profile.setWeekSmoked(dto.getWeekSmoked());
        profile.setNote(dto.getNote());
        profile.setLastUpdateDate(LocalDate.now());
        SmokingProfile saved = smokingProfileRepository.save(profile);
        return smokingProfileMapper.toDto(saved);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void delete(Long id) {
        SmokingProfile entity = smokingProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SmokingProfile not found with id: " + id));
        smokingProfileRepository.delete(entity);
    }

    @Override
    @Transactional
    @PreAuthorize("#dto.userId == authentication.principal.id")
    public SmokingProfileDto create(SmokingProfileDto dto) {
        if (smokingProfileRepository.existsByUserUserId(dto.getUserId())) {
            throw new IllegalArgumentException("Smoking profile for this user already exists");
        }
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));
        SmokingProfile entity = smokingProfileMapper.toEntity(dto, user);

        SmokingProfile saved = smokingProfileRepository.save(entity);
        return smokingProfileMapper.toDto(saved);
    }

    @Override
    public SmokingProfileDto getByUserId(Long userId) {
        SmokingProfile entity = smokingProfileRepository.findByUserUserId(userId);
        if (entity == null) {
            throw new ResourceNotFoundException("SmokingProfile not found for user id: " + userId);
        }
        return smokingProfileMapper.toDto(entity);
    }

    @Transactional
    @PreAuthorize("#requestDto.userId == authentication.principal.id")
    public SmokingProfileDto submitUserQuiz(UserQuizAnswerRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với ID: " + requestDto.getUserId()));

        //Kiểm tra quizId
        if (!"FGT_QUIZ001".equals(requestDto.getQuizId())) {
            throw new ResourceNotFoundException("ID bài kiểm tra không khớp. Dự kiến 'FGT_QUIZ001', nhận được: " + requestDto.getQuizId());
        }

        // Create/update SmokingProfile
        SmokingProfile entity = smokingProfileRepository.findByUserUserId(user.getUserId());

        if (entity == null) {
            entity = new SmokingProfile();
        } else {
            entity.setLastUpdateDate(LocalDate.now());
        }
        entity.setUser(user);

        Map<Long, QuestionDto> questionMap = requestDto.getQuestions().stream()
                .collect(Collectors.toMap(QuestionDto::getId, Function.identity()));

        // Set costPerPack (ID 7)
        QuestionDto costPerPackQ = questionMap.get(7L);
        if (costPerPackQ != null && costPerPackQ.getAnswerText() != null) {
            try {
                entity.setCostPerPack(BigDecimal.valueOf(Double.parseDouble(costPerPackQ.getAnswerText())));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Giá gói thuốc không hợp lệ: " + costPerPackQ.getAnswerText());
            }
        } else {
            throw new IllegalArgumentException("Không tìm thấy hoặc chưa trả lời câu hỏi 'Giá gói thuốc'.");
        }

        // Set weekSmoked (ID 8)
        QuestionDto weekSmokedQ = questionMap.get(8L);
        if (weekSmokedQ != null && weekSmokedQ.getAnswerText() != null) {
            try {
                entity.setWeekSmoked(Integer.parseInt(weekSmokedQ.getAnswerText()));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Số tuần hút thuốc không hợp lệ: " + weekSmokedQ.getAnswerText());
            }
        } else {
            throw new IllegalArgumentException("Không tìm thấy hoặc chưa trả lời câu hỏi 'Tuần hút thuốc'.");
        }

        // Set cigarettesPerDay (ID 4)
        QuestionDto cigarettesPerDayQ = questionMap.get(4L);
        if (cigarettesPerDayQ != null && cigarettesPerDayQ.getOptionDtos() != null && !cigarettesPerDayQ.getOptionDtos().isEmpty()) {
            entity.setCigarettesPerDay(cigarettesPerDayQ.getOptionDtos().iterator().next().getOptionText());
        } else {
            throw new IllegalArgumentException("Không tìm thấy hoặc chưa trả lời câu hỏi 'Số điếu thuốc mỗi ngày'.");
        }

        // Set Note (ID 9)
        QuestionDto noteQ = questionMap.get(9L);
        if (noteQ != null && noteQ.getAnswerText() != null) {
            entity.setNote(noteQ.getAnswerText());
        } else {
            entity.setNote(null);
        }
        //Tính toán FTND Score và tự động set NicotineAddiction
        entity.setFtndScore(ftndScoreCalculator(requestDto.getQuestions()));
        return smokingProfileMapper.toDto(smokingProfileRepository.save(entity));
    }

    private int ftndScoreCalculator(List<QuestionDto> questions) {
        int totalScore = 0;
        for (QuestionDto questionDto : questions) {
            Set<OptionDto> selectedOptionDtos = questionDto.getOptionDtos();
            if (selectedOptionDtos != null) {
                for (OptionDto optionDto : selectedOptionDtos) {
                    totalScore += optionDto.getScore();
                }
            }

        }
        return totalScore;
    }


}