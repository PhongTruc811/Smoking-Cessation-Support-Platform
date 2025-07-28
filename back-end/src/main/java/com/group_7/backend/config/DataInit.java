package com.group_7.backend.config;

import com.group_7.backend.dto.QuitMethodOptionDto;
import com.group_7.backend.dto.QuitPlanDto;
import com.group_7.backend.dto.quiz.*;
import com.group_7.backend.entity.*;
import com.group_7.backend.entity.enums.*;
import com.group_7.backend.mapper.QuitMethodOptionMapper;
import com.group_7.backend.repository.*;
import com.group_7.backend.service.impl.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataInit implements ApplicationRunner {

    @Autowired private UserRepository userRepository;
    @Autowired private MembershipPackageRepository membershipPackageRepository;
    @Autowired private UserMembershipRepository userMembershipRepository;
    @Autowired private SmokingProfileRepository smokingProfileRepository;
    @Autowired private QuitMethodRepository quitMethodRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private CommentRepository commentRepository;
    @Autowired private LikeRepository likeRepository;
    @Autowired private EmailSenderService emailSenderService;
    @Autowired private QuizServiceImp quizServiceImp;
    @Autowired private QuitPlanServiceImp quitPlanServiceImp;
    @Autowired private QuitMethodOptionMapper quitMethodOptionMapper;
    @Autowired private AchievementRepository achievementRepository;
    @Autowired private UserRecordRepository userRecordRepository; // ADD THIS
    @Autowired private UserServiceImp userServiceImp;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        System.out.println("=== BẮT ĐẦU SEED DỮ LIỆU MẪU ===");
        if (userRepository.count() > 0) {
            System.out.println("Dữ liệu đã tồn tại, không seed lại.");
            return;
        }
        // Avoid duplicate seed

        //Users
        User user1 = new User();
        user1.setUsername("vanan");
        user1.setFullName("Nguyễn Văn An");
        user1.setEmail("vana@gmail.com");
        user1.setPassword("123456");
        user1.setDob(LocalDate.of(1990, 5, 1));
        user1.setGender(UserGenderEnum.MALE);
        user1.setStatus(true);
        user1.setRole(UserRoleEnum.MEMBER);
        userRepository.save(user1);

        User coach1 = new User();
        coach1.setUsername("binhtran");
        coach1.setFullName("Trần Thị Bình");
        coach1.setEmail("tranthib@gmail.com");
        coach1.setPassword("123456");
        coach1.setDob(LocalDate.of(1985, 9, 10));
        coach1.setGender(UserGenderEnum.FEMALE);
        coach1.setStatus(true);
        coach1.setRole(UserRoleEnum.COACH);
        userRepository.save(coach1);

        User admin1 = new User();
        admin1.setUsername("admin");
        admin1.setFullName("admin");
        admin1.setEmail("admin@gmail.com");
        admin1.setPassword("admin123");
        admin1.setDob(LocalDate.of(1980, 12, 20));
        admin1.setGender(UserGenderEnum.MALE);
        admin1.setStatus(true);
        admin1.setRole(UserRoleEnum.ADMIN);
        userRepository.save(admin1);

        // ADD MORE TEST USERS FOR ACHIEVEMENT TESTING
        User testUser1 = new User();
        testUser1.setUsername("dochi");
        testUser1.setFullName("Đỗ Hữu Chí");
        testUser1.setEmail("dochi@gmail.com");
        testUser1.setPassword("123456");
        testUser1.setDob(LocalDate.of(1992, 3, 15));
        testUser1.setGender(UserGenderEnum.MALE);
        testUser1.setStatus(true);
        testUser1.setRole(UserRoleEnum.MEMBER);
        userRepository.save(testUser1);

        User testUser2 = new User();
        testUser2.setUsername("vanlam");
        testUser2.setFullName("Nguyễn Văn Lâm");
        testUser2.setEmail("vanlam@gmail.com");
        testUser2.setPassword("123456");
        testUser2.setDob(LocalDate.of(1988, 7, 22));
        testUser2.setGender(UserGenderEnum.MALE);
        testUser2.setStatus(true);
        testUser2.setRole(UserRoleEnum.MEMBER);
        userRepository.save(testUser2);

        User testUser3 = new User();
        testUser3.setUsername("hoadang");
        testUser3.setFullName("Đặng Ngọc Hoa");
        testUser3.setEmail("ngochoa@gmail.com");
        testUser3.setPassword("123456");
        testUser3.setDob(LocalDate.of(1985, 11, 8));
        testUser3.setGender(UserGenderEnum.FEMALE);
        testUser3.setStatus(true);
        testUser3.setRole(UserRoleEnum.MEMBER);
        userRepository.save(testUser3);

        //MembershipPackages

        MembershipPackage pkg2 = new MembershipPackage();
        pkg2.setPackageName("Gói hội viên");
        pkg2.setDescription("Hỗ trợ coach và thống kê nâng cao");
        pkg2.setPrice(BigDecimal.valueOf(100000));
        pkg2.setDurationInDays(30);
        membershipPackageRepository.save(pkg2);

        //UserMemberships

        UserMembership um2 = new UserMembership();
        um2.setUser(user1);
        um2.setMembershipPackage(pkg2);
        um2.setStartDate(LocalDate.now());
        um2.setStatus(MembershipStatusEnum.ACTIVE);
        um2.setPaymentMethod("VNPay");
        userMembershipRepository.save(um2);

        //QuitMethod và QuitMethodOptions ---
        QuitMethod nicotineMethod = new QuitMethod();
        nicotineMethod.setMethodName("Smoking Cessation Medications");
        nicotineMethod.setMethodType(QuestionTypeEnum.SINGLE_CHOICE);
        nicotineMethod = quitMethodRepository.save(nicotineMethod);


        QuitMethodOption gumOption = new QuitMethodOption(
                "Nicotine Gum - NRT",
                "<ul>" +
                        "<li><strong>Function:</strong> Quickly reduces cravings by delivering nicotine through the oral mucosa.</li>" +
                        "<li><strong>Usage:</strong> Chew the gum slowly, then place it between the cheek and gums to absorb nicotine.</li>" +
                        "<li><strong>Note:</strong> Avoid drinking anything except water 15 minutes before and during chewing.</li>" +
                        "<li><strong>Common side effects:</strong> Dry mouth, jaw pain, hiccups.</li>" +
                        "</ul>",
                ""
        );
        gumOption.setQuitMethod(nicotineMethod);

        QuitMethodOption patchOption = new QuitMethodOption(
                "Nicotine Patch - NRT",
                "<ul>" +
                        "<li><strong>Function:</strong> Provides a steady level of nicotine throughout the day to reduce cravings.</li>" +
                        "<li><strong>Usage:</strong> Apply the patch to a hairless area in the morning; rotate application sites daily.</li>" +
                        "<li><strong>Note:</strong> Remove the 24-hour patch before bedtime if it causes insomnia.</li>" +
                        "<li><strong>Common side effects:</strong> Skin irritation, insomnia.</li>" +
                        "</ul>",
                ""
        );
        patchOption.setQuitMethod(nicotineMethod);

        QuitMethodOption bupropionOption = new QuitMethodOption(
                "Bupropion SR",
                "<ul>" +
                        "<li><strong>Function:</strong> Reduces withdrawal symptoms and cravings by affecting neurotransmitters.</li>" +
                        "<li><strong>Usage:</strong> Start 1–2 weeks before the quit date. Take 150mg/day for 3 days, then 150mg twice daily.</li>" +
                        "<li><strong>Note:</strong> If insomnia occurs, take the second dose earlier in the afternoon.</li>" +
                        "<li><strong>Common side effects:</strong> Insomnia (35–40%), dry mouth.</li>" +
                        "</ul>",
                ""
        );
        bupropionOption.setQuitMethod(nicotineMethod);

        QuitMethodOption vareniclineOption = new QuitMethodOption(
                "Varenicline",
                "<ul>" +
                        "<li><strong>Function:</strong> Reduces the rewarding effects of smoking and decreases cravings by acting on nicotine receptors.</li>" +
                        "<li><strong>Usage:</strong> Start 1 week before the quit date. Gradually increase from 0.5mg/day to 1mg twice daily.</li>" +
                        "<li><strong>Note:</strong> Take after meals to reduce nausea and in the afternoon to avoid insomnia.</li>" +
                        "<li><strong>Common side effects:</strong> Nausea, sleep disturbances. May affect mood and behavior.</li>" +
                        "</ul>",
                ""
        );
        vareniclineOption.setQuitMethod(nicotineMethod);


        nicotineMethod.getOptions().add(gumOption);
        nicotineMethod.getOptions().add(patchOption);
        nicotineMethod.getOptions().add(bupropionOption);
        nicotineMethod.getOptions().add(vareniclineOption);
        quitMethodRepository.save(nicotineMethod);

        //Posts
        Post post1 = new Post();
        post1.setUser(user1);
        post1.setTitle("Hành trình cai thuốc");
        post1.setContent("Mình đã cai được 3 tuần, cảm thấy khỏe hơn rất nhiều.");
        postRepository.save(post1);

        Post post2 = new Post();
        post2.setUser(coach1);
        post2.setTitle("Chia sẻ kinh nghiệm");
        post2.setContent("Nên tìm bạn đồng hành để cai thuốc hiệu quả hơn.");
        postRepository.save(post2);

        //Comments
        Comment cmt1 = new Comment();
        cmt1.setPost(post1);
        cmt1.setUser(coach1);
        cmt1.setContent("Cố lên bạn nhé!");
        commentRepository.save(cmt1);

        Comment cmt2 = new Comment();
        cmt2.setPost(post2);
        cmt2.setUser(user1);
        cmt2.setContent("Rất đồng ý với chia sẻ của bạn.");
        commentRepository.save(cmt2);

        //Likes
        Like like1 = new Like();
        like1.setUser(coach1);
        like1.setPost(post1);
        likeRepository.save(like1);

        Like like2 = new Like();
        like2.setUser(user1);
        like2.setPost(post2);
        likeRepository.save(like2);

        QuizDto fagerstromQuiz = new QuizDto();
        fagerstromQuiz.setQuizId("FGT_QUIZ001");
        fagerstromQuiz.setName("Fagerström Test for Nicotine Dependence");
        fagerstromQuiz.setDescription("A questionnaire to assess the level of nicotine dependence using the Fagerström Test for Nicotine Dependence (FTND).");

        List<QuestionDto> questions = List.of(
                new QuestionDto(null, QuestionTypeEnum.SINGLE_CHOICE, "How soon after waking do you smoke your first cigarette?",
                        Set.of(
                                new OptionDto(null, "Within 5 minutes", 3),
                                new OptionDto(null, "6–30 minutes", 2),
                                new OptionDto(null, "31–60 minutes", 1),
                                new OptionDto(null, "After 60 minutes", 0)
                        )
                ),
                new QuestionDto(null, QuestionTypeEnum.SINGLE_CHOICE,"Do you find it difficult to refrain from smoking in places where it is forbidden?",
                        Set.of(
                                new OptionDto(null, "Yes", 1),
                                new OptionDto(null, "No", 0)
                        )
                ),
                new QuestionDto(null, QuestionTypeEnum.SINGLE_CHOICE,"Which cigarette would you hate most to give up?",
                        Set.of(
                                new OptionDto(null, "The first one in the morning", 1),
                                new OptionDto(null, "Any other", 0)
                        )
                ),
                new QuestionDto(null, QuestionTypeEnum.SINGLE_CHOICE,"How many cigarettes do you smoke per day?",
                        Set.of(
                                new OptionDto(null, "10 or less", 0),
                                new OptionDto(null, "11–20", 1),
                                new OptionDto(null, "21–30", 2),
                                new OptionDto(null, "31 or more", 3)
                        )
                ),
                new QuestionDto(null, QuestionTypeEnum.SINGLE_CHOICE,"Do you smoke more frequently in the morning?",
                        Set.of(
                                new OptionDto(null, "Yes", 1),
                                new OptionDto(null, "No", 0)
                        )
                ),
                new QuestionDto(null, QuestionTypeEnum.SINGLE_CHOICE,"Do you smoke even when you're ill and bedridden?",
                        Set.of(
                                new OptionDto(null, "Yes", 1),
                                new OptionDto(null, "No", 0)
                        )
                ),
                new QuestionDto(null, QuestionTypeEnum.NUMBER_ANSWER,"Price per pack (20 cigarettes)", ""),
                new QuestionDto(null, QuestionTypeEnum.NUMBER_ANSWER,"How long have you been smoking? (in weeks)", ""),
                new QuestionDto(null, QuestionTypeEnum.TEXT_ANSWER, "Notes:","")
        );
        fagerstromQuiz.setQuestions(questions);
        quizServiceImp.createQuiz(fagerstromQuiz);

        QuizDto feedbackQuiz = new QuizDto();
        feedbackQuiz.setQuizId("SFB_QUIZ001");
        feedbackQuiz.setName("System Feedback Survey");
        feedbackQuiz.setDescription("We would like to hear your thoughts to improve the system.");

        List<QuestionDto> questions2 = List.of(
                new QuestionDto(null, QuestionTypeEnum.SINGLE_CHOICE, "How satisfied are you with the system overall?",
                        Set.of(
                                new OptionDto(null, "Very dissatisfied", 1),
                                new OptionDto(null, "Dissatisfied", 2),
                                new OptionDto(null, "Neutral", 3),
                                new OptionDto(null, "Satisfied", 4),
                                new OptionDto(null, "Very satisfied", 5)
                        )
                ),
                new QuestionDto(null, QuestionTypeEnum.SINGLE_CHOICE, "Do you find the system easy to use?",
                        Set.of(
                                new OptionDto(null, "Very difficult to use", 1),
                                new OptionDto(null, "Difficult to use", 2),
                                new OptionDto(null, "Average", 3),
                                new OptionDto(null, "Easy to use", 4),
                                new OptionDto(null, "Very easy to use", 5)
                        )
                ),
                new QuestionDto(null, QuestionTypeEnum.SINGLE_CHOICE, "Is the system’s response speed meeting your expectations?",
                        Set.of(
                                new OptionDto(null, "Very slow", 1),
                                new OptionDto(null, "Slow", 2),
                                new OptionDto(null, "Average", 3),
                                new OptionDto(null, "Fast", 4),
                                new OptionDto(null, "Very fast", 5)
                        )
                ),
                new QuestionDto(null, QuestionTypeEnum.SINGLE_CHOICE, "Have you encountered any errors or issues while using the system?",
                        Set.of(
                                new OptionDto(null, "Very frequently", 1),
                                new OptionDto(null, "Frequently", 2),
                                new OptionDto(null, "Occasionally", 3),
                                new OptionDto(null, "Rarely", 4),
                                new OptionDto(null, "Never", 5)
                        )
                ),
                new QuestionDto(null, QuestionTypeEnum.NUMBER_ANSWER, "How many hours per week do you use this system?", ""),
                new QuestionDto(null, QuestionTypeEnum.TEXT_ANSWER, "Which feature do you find most useful?", ""),
                new QuestionDto(null, QuestionTypeEnum.TEXT_ANSWER, "Are there any features you'd like us to add or improve?", ""),
                new QuestionDto(null, QuestionTypeEnum.TEXT_ANSWER, "Please share any other comments or feedback you may have.", "")
        );
        feedbackQuiz.setQuestions(questions2);
        quizServiceImp.createQuiz(feedbackQuiz);

        // Initialize dynamic rule-based achievements
        initializeAchievements();

        // ADD USER RECORDS FOR ACHIEVEMENT TESTING
        initializeUserRecords(user1, testUser1, testUser2, testUser3);

        System.out.println("=== ĐÃ SEED XONG DỮ LIỆU MẪU ===");
    }

    // ADD THIS NEW METHOD FOR USER RECORDS
    private void initializeUserRecords(User user1, User testUser1, User testUser2, User testUser3) {
        if (userRecordRepository.count() == 0) {
            System.out.println("=== Initializing UserRecords for Achievement Testing ===");

            // Original user - moderate progress
            UserRecord record1 = new UserRecord();
            record1.setUser(user1);
            record1.setTotalQuitDays(21); // 3 weeks
            record1.setTotalQuitSmokes(210); // 21 days * 10 cigarettes/day
            record1.setTotalSaveMoney(new BigDecimal("157000.0")); // 21 days * 7.5$/day
            userRecordRepository.save(record1);

            // Test User 1 - Beginner (should earn basic achievements)
            UserRecord record2 = new UserRecord();
            record2.setUser(testUser1);
            record2.setTotalQuitDays(5); // 5 days
            record2.setTotalQuitSmokes(75); // 5 days * 15 cigarettes/day
            record2.setTotalSaveMoney(new BigDecimal("37500.00")); // 5 days * 7500/day
            userRecordRepository.save(record2);

            // Test User 2 - Advanced (should earn many achievements)
            UserRecord record3 = new UserRecord();
            record3.setUser(testUser2);
            record3.setTotalQuitDays(45); // 6+ weeks
            record3.setTotalQuitSmokes(900); // 45 days * 20 cigarettes/day
            record3.setTotalSaveMoney(new BigDecimal("450000.00")); // 45 days * 10000/day
            userRecordRepository.save(record3);

            // Test User 3 - Expert (should earn most achievements)
            UserRecord record4 = new UserRecord();
            record4.setUser(testUser3);
            record4.setTotalQuitDays(120); // 4 months
            record4.setTotalQuitSmokes(3000); // 120 days * 25 cigarettes/day
            record4.setTotalSaveMoney(new BigDecimal("1200000.00")); // 120 days * 10.000/day
            userRecordRepository.save(record4);

            System.out.println("=== UserRecords initialized successfully ===");
            System.out.println("Test User IDs for Achievement API testing:");
            System.out.println("- Beginner (5 days): " + testUser1.getUserId());
            System.out.println("- Advanced (45 days): " + testUser2.getUserId());
            System.out.println("- Expert (120 days): " + testUser3.getUserId());
        }
    }

    private void initializeAchievements() {
        if (achievementRepository.count() == 0) {
            List<Achievement> achievements = List.of(
                    // Time-based achievements with dynamic rules
                    new Achievement("First Day", "🎯", IconTypeEnum.EMOJI, false,
                            "Your first day smoke-free!", "time",
                            "DAYS_SMOKE_FREE", 1, ">="),

                    new Achievement("Three Days Strong", "📅", IconTypeEnum.EMOJI, false,
                            "Three days without smoking!", "time",
                            "DAYS_SMOKE_FREE", 3, ">="),

                    new Achievement("First Week", "📅", IconTypeEnum.EMOJI, false,
                            "One week without smoking!", "time",
                            "DAYS_SMOKE_FREE", 7, ">="),

                    new Achievement("First Month", "🗓️", IconTypeEnum.EMOJI, false,
                            "30 days smoke-free milestone!", "time",
                            "DAYS_SMOKE_FREE", 30, ">="),

                    new Achievement("3 Months Champion", "🌟", IconTypeEnum.EMOJI, false,
                            "Quarter year achievement!", "time",
                            "DAYS_SMOKE_FREE", 90, ">="),

                    new Achievement("6 Months Hero", "🏆", IconTypeEnum.EMOJI, false,
                            "Half year smoke-free!", "time",
                            "DAYS_SMOKE_FREE", 180, ">="),

                    new Achievement("One Year Legend", "👑", IconTypeEnum.EMOJI, false,
                            "A full year clean. You're legendary!", "time",
                            "DAYS_SMOKE_FREE", 365, ">="),

                    // Financial achievements with dynamic rules (in VND)
                    new Achievement("First 10,000 VND Saved", "💰", IconTypeEnum.EMOJI, false,
                            "You've saved your first 10,000 VND!", "financial",
                            "MONEY_SAVED", 10000, ">="),

                    new Achievement("50,000 VND Saver", "💵", IconTypeEnum.EMOJI, false,
                            "You've saved 50,000 VND!", "financial",
                            "MONEY_SAVED", 50000, ">="),

                    new Achievement("100,000 VND Milestone", "💵", IconTypeEnum.EMOJI, false,
                            "Milestone reached: 100,000 VND saved!", "financial",
                            "MONEY_SAVED", 100000, ">="),

                    new Achievement("500,000 VND Saving", "💵", IconTypeEnum.EMOJI, false,
                            "You've saved 500,000 VND – halfway to a million!", "financial",
                            "MONEY_SAVED", 500000, ">="),

                    new Achievement("1,000,000 VND Champion", "💵", IconTypeEnum.EMOJI, false,
                            "You've saved 1,000,000 VND – you're a true savings champion!", "financial",
                            "MONEY_SAVED", 1000000, ">="),

                    // Health achievements (cigarettes avoided)
                    new Achievement("First 20 Cigarettes Avoided", "🚭", IconTypeEnum.EMOJI, false,
                            "You've avoided your first pack!", "health",
                            "CIGARETTES_AVOIDED", 20, ">="),

                    new Achievement("100 Cigarettes Avoided", "🚭", IconTypeEnum.EMOJI, false,
                            "You've avoided 100 cigarettes!", "health",
                            "CIGARETTES_AVOIDED", 100, ">="),

                    new Achievement("500 Cigarettes Avoided", "🏆", IconTypeEnum.EMOJI, false,
                            "500 cigarettes not smoked!", "health",
                            "CIGARETTES_AVOIDED", 500, ">="),

                    new Achievement("1000 Cigarettes Avoided", "👑", IconTypeEnum.EMOJI, false,
                            "One thousand cigarettes avoided!", "health",
                            "CIGARETTES_AVOIDED", 1000, ">="),

                    new Achievement("5000 Cigarettes Avoided", "🌟", IconTypeEnum.EMOJI, false,
                            "Five thousand cigarettes not smoked!", "health",
                            "CIGARETTES_AVOIDED", 5000, ">=")
            );

            achievementRepository.saveAll(achievements);
        }
    }
}