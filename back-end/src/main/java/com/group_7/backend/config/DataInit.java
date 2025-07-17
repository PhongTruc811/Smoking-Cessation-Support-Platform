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
    @Autowired
    private UserServiceImp userServiceImp;

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
        user1.setUsername("user1");
        user1.setFullName("Nguyễn Văn An");
        user1.setEmail("vana@gmail.com");
        user1.setPassword("123456");
        user1.setDob(LocalDate.of(1990, 5, 1));
        user1.setGender(UserGenderEnum.MALE);
        user1.setStatus(true);
        user1.setRole(UserRoleEnum.MEMBER);
        userRepository.save(user1);

        User coach1 = new User();
        coach1.setUsername("coach1");
        coach1.setFullName("Trần Thị Bình");
        coach1.setEmail("thib@gmail.com");
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

        //MembershipPackages
        MembershipPackage pkg1 = new MembershipPackage();
        pkg1.setPackageName("Gói cơ bản");
        pkg1.setDescription("Sử dụng các tính năng cơ bản");
        pkg1.setPrice(BigDecimal.ZERO);
        pkg1.setDurationInDays(30);
        membershipPackageRepository.save(pkg1);

        MembershipPackage pkg2 = new MembershipPackage();
        pkg2.setPackageName("Gói hội viên");
        pkg2.setDescription("Hỗ trợ coach và thống kê nâng cao");
        pkg2.setPrice(BigDecimal.valueOf(100000));
        pkg2.setDurationInDays(30);
        membershipPackageRepository.save(pkg2);

        //UserMemberships
        UserMembership um1 = new UserMembership();
        um1.setUser(user1);
        um1.setMembershipPackage(pkg1);
        um1.setStartDate(LocalDate.now());
        um1.setStatus(MembershipStatusEnum.ACTIVE);
        um1.setPaymentMethod("VNPay");
        userMembershipRepository.save(um1);

        UserMembership um2 = new UserMembership();
        um2.setUser(coach1);
        um2.setMembershipPackage(pkg2);
        um2.setStartDate(LocalDate.now());
        um2.setStatus(MembershipStatusEnum.ACTIVE);
        um2.setPaymentMethod("VNPay");
        userMembershipRepository.save(um2);

        //SmokingProfiles
        SmokingProfile sp1 = new SmokingProfile();
        sp1.setUser(user1);
        sp1.setCigarettesPerDay("10");
        sp1.setCostPerPack(new BigDecimal("25000"));
        sp1.setWeekSmoked(5);
        sp1.setNote("Hút thường ngày");
        smokingProfileRepository.save(sp1);

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

        // --- Time-based Achievements ---
        Achievement ach1 = new Achievement();
        ach1.setName("First 24 Hours");
        ach1.setIcon("🏅");
        ach1.setIconType(IconTypeEnum.EMOJI);
        ach1.setLocked(false);
        ach1.setDescription("You made it through the first day!");
        ach1.setCategory("time");
        achievementRepository.save(ach1);

        Achievement ach2 = new Achievement();
        ach2.setName("Three-Day Trial");
        ach2.setIcon("🏅");
        ach2.setIconType(IconTypeEnum.EMOJI);
        ach2.setLocked(false);
        ach2.setDescription("You've completed 3 days smoke-free!");
        ach2.setCategory("time");
        achievementRepository.save(ach2);

        Achievement ach3 = new Achievement();
        ach3.setName("Week One Warrior");
        ach3.setIcon("🏅");
        ach3.setIconType(IconTypeEnum.EMOJI);
        ach3.setLocked(false);
        ach3.setDescription("One full week without smoking.");
        ach3.setCategory("time");
        achievementRepository.save(ach3);

        Achievement ach4 = new Achievement();
        ach4.setName("One Year Legend");
        ach4.setIcon("🏅");
        ach4.setIconType(IconTypeEnum.EMOJI);
        ach4.setLocked(true);
        ach4.setDescription("A full year clean. You're legendary.");
        ach4.setCategory("time");
        achievementRepository.save(ach4);

// --- Financial Achievements ---
        Achievement ach5 = new Achievement();
        ach5.setName("First 10,000 VND Saved");
        ach5.setIcon("💰");
        ach5.setIconType(IconTypeEnum.EMOJI);
        ach5.setLocked(false);
        ach5.setDescription("You've saved the first 10,000đ!");
        ach5.setCategory("financial");
        achievementRepository.save(ach5);

        Achievement ach6 = new Achievement();
        ach6.setName("Money Saver (100,000 VND)");
        ach6.setIcon("💰");
        ach6.setIconType(IconTypeEnum.EMOJI);
        ach6.setLocked(true);
        ach6.setDescription("You've saved over 100,000đ!");
        ach6.setCategory("financial");
        achievementRepository.save(ach6);

        Achievement ach9 = new Achievement();
        ach9.setName("250,000 VND Saved");
        ach9.setIcon("💰");
        ach9.setIconType(IconTypeEnum.EMOJI);
        ach9.setLocked(true);
        ach9.setDescription("You've saved over 250,000đ!");
        ach9.setCategory("financial");
        achievementRepository.save(ach9);

// --- Health Achievements ---
        Achievement ach7 = new Achievement();
        ach7.setName("Breathe Easy");
        ach7.setIcon("❤️");
        ach7.setIconType(IconTypeEnum.EMOJI);
        ach7.setLocked(false);
        ach7.setDescription("Your lungs already feel the difference.");
        ach7.setCategory("health");
        achievementRepository.save(ach7);

        Achievement ach8 = new Achievement();
        ach8.setName("Heart Helper");
        ach8.setIcon("❤️");
        ach8.setIconType(IconTypeEnum.EMOJI);
        ach8.setLocked(true);
        ach8.setDescription("Heart attack risk drops significantly.");
        ach8.setCategory("health");
        achievementRepository.save(ach8);

        System.out.println("=== ĐÃ SEED XONG DỮ LIỆU MẪU ===");
    }
}