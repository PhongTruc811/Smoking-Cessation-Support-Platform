package com.group_7.backend.config;

import com.group_7.backend.dto.QuitMethodOptionDto;
import com.group_7.backend.dto.QuitPlanDto;
import com.group_7.backend.dto.quiz.*;
import com.group_7.backend.entity.*;
import com.group_7.backend.entity.enums.*;
import com.group_7.backend.mapper.QuitMethodOptionMapper;
import com.group_7.backend.repository.*;
import com.group_7.backend.service.impl.EmailSenderService;
import com.group_7.backend.service.impl.QuitMethodServiceImp;
import com.group_7.backend.service.impl.QuitPlanServiceImp;
import com.group_7.backend.service.impl.QuizServiceImp;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
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
    @Autowired private QuitPlanRepository quitPlanRepository;
    @Autowired private QuitMethodRepository quitMethodRepository;
    @Autowired private QuitMethodOptionRepository quitMethodOptionRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private CommentRepository commentRepository;
    @Autowired private LikeRepository likeRepository;
    @Autowired private EmailSenderService emailSenderService;
    @Autowired private QuizServiceImp quizServiceImp;
    @Autowired private QuitPlanServiceImp quitPlanServiceImp;
    @Autowired private QuitMethodOptionMapper quitMethodOptionMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        System.out.println("Gửi email:");
//       emailSenderService.sendMessage("phongtruc811@gmail.com","AntiSmokingPlatform", "Chúc mừng bạn đã đạt được chuỗi 17 ngày không hút thuốc");
//        emailSenderService.sendMessage("duongvuthaian@gmail.com","AQUA IS THE BEST", "GODDESS BLESSING!!!!!!");


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
        pkg2.setPrice(BigDecimal.valueOf(200000));
        pkg2.setDurationInDays(90);
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
        nicotineMethod.setMethodName("Nicotine Replacement Therapy (NRT)");
        nicotineMethod.setMethodType(QuestionTypeEnum.SINGLE_CHOICE);
        nicotineMethod = quitMethodRepository.save(nicotineMethod);


        QuitMethodOption gumOption = new QuitMethodOption("Nicotine Gum", "Use nicotine gum when cravings hit.", "Remember to chew slowly for best results.");
        gumOption.setQuitMethod(nicotineMethod);

        QuitMethodOption patchOption = new QuitMethodOption("Nicotine Patch", "Apply a patch daily for steady nicotine release.", "Change patch daily and apply to a clean, hairless area.");
        patchOption.setQuitMethod(nicotineMethod);

        QuitMethodOption lozengeOption = new QuitMethodOption("Nicotine Lozenge", "Dissolve lozenge slowly in mouth for nicotine absorption.", "Do not chew or swallow the lozenge.");
        lozengeOption.setQuitMethod(nicotineMethod);

        nicotineMethod.getOptions().add(gumOption);
        nicotineMethod.getOptions().add(patchOption);
        nicotineMethod.getOptions().add(lozengeOption);
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

        //Quiz Fagerström
        QuizDto fagerstromQuiz = new QuizDto();
        fagerstromQuiz.setQuizId("FGT_QUIZ001");
        fagerstromQuiz.setName("Thang đo Fagerström về mức độ lệ thuộc nicotine");
        fagerstromQuiz.setDescription("Bài trắc nghiệm đánh giá mức độ lệ thuộc nicotine bằng thang Fagerström (FTND)");

        List<QuestionDto> questions = List.of(
                new QuestionDto(null, QuestionTypeEnum.SINGLE_CHOICE, "Bạn hút điếu đầu tiên sau bao lâu kể từ khi thức dậy?",
                        Set.of(
                                new OptionDto(null, "Trong vòng 5 phút", 3),
                                new OptionDto(null, "Từ 6 đến 30 phút", 2),
                                new OptionDto(null, "Từ 31 đến 60 phút", 1),
                                new OptionDto(null, "Sau 60 phút", 0)
                        )
                ),
                new QuestionDto(null, QuestionTypeEnum.SINGLE_CHOICE,"Bạn có cảm thấy khó bỏ hút thuốc ở những nơi bị cấm không?",
                        Set.of(
                                new OptionDto(null, "Có", 1),
                                new OptionDto(null, "Không", 0)
                        )
                ),
                new QuestionDto(null, QuestionTypeEnum.SINGLE_CHOICE,"Điếu thuốc nào bạn cảm thấy khó bỏ nhất?",
                        Set.of(
                                new OptionDto(null, "Điếu đầu tiên trong ngày", 1),
                                new OptionDto(null, "Các điếu còn lại", 0)
                        )
                ),
                new QuestionDto(null, QuestionTypeEnum.SINGLE_CHOICE,"Bạn hút bao nhiêu điếu thuốc mỗi ngày?",
                        Set.of(
                                new OptionDto(null, "10 điếu hoặc ít hơn", 0),
                                new OptionDto(null, "11–20 điếu", 1),
                                new OptionDto(null, "21–30 điếu", 2),
                                new OptionDto(null, "31 điếu trở lên", 3)
                        )
                ),
                new QuestionDto(null, QuestionTypeEnum.SINGLE_CHOICE,"Bạn có hút thuốc nhiều hơn vào buổi sáng không?",
                        Set.of(
                                new OptionDto(null, "Có", 1),
                                new OptionDto(null, "Không", 0)
                        )
                ),
                new QuestionDto(null, QuestionTypeEnum.SINGLE_CHOICE,"Bạn có hút thuốc ngay cả khi bị ốm nằm trên giường?",
                        Set.of(
                                new OptionDto(null, "Có", 1),
                                new OptionDto(null, "Không", 0)
                        )
                ),
                new QuestionDto(null,QuestionTypeEnum.NUMBER_ANSWER,"Giá tiền mỗi gói thuốc (20 điếu)", ""),
                new QuestionDto(null,QuestionTypeEnum.NUMBER_ANSWER,"Bạn đã hút thuốc được bao lâu (week)?",""),
                new QuestionDto(null, QuestionTypeEnum.TEXT_ANSWER, "Ghi chú (nếu có):","")
        );

        fagerstromQuiz.setQuestions(questions);
        quizServiceImp.createQuiz(fagerstromQuiz);

        QuizDto feedbackQuiz = new QuizDto();
        feedbackQuiz.setQuizId("SFB_QUIZ001");
        feedbackQuiz.setName("Khảo sát Phản hồi Hệ thống");
        feedbackQuiz.setDescription("Chúng tôi muốn lắng nghe ý kiến của bạn để cải thiện hệ thống.");

        List<QuestionDto> questions2 = List.of(
                new QuestionDto(null, QuestionTypeEnum.SINGLE_CHOICE, "Mức độ hài lòng chung của bạn với hệ thống là gì?",
                        Set.of(
                                new OptionDto(null, "Rất không hài lòng", 1),
                                new OptionDto(null, "Không hài lòng", 2),
                                new OptionDto(null, "Bình thường", 3),
                                new OptionDto(null, "Hài lòng", 4),
                                new OptionDto(null, "Rất hài lòng", 5)
                        )
                ),
                new QuestionDto(null, QuestionTypeEnum.SINGLE_CHOICE, "Bạn thấy hệ thống có dễ sử dụng không?",
                        Set.of(
                                new OptionDto(null, "Rất khó sử dụng", 1),
                                new OptionDto(null, "Khó sử dụng", 2),
                                new OptionDto(null, "Trung bình", 3),
                                new OptionDto(null, "Dễ sử dụng", 4),
                                new OptionDto(null, "Rất dễ sử dụng", 5)
                        )
                ),
                new QuestionDto(null, QuestionTypeEnum.SINGLE_CHOICE, "Tốc độ phản hồi của hệ thống có đáp ứng kỳ vọng của bạn không?",
                        Set.of(
                                new OptionDto(null, "Rất chậm", 1),
                                new OptionDto(null, "Chậm", 2),
                                new OptionDto(null, "Bình thường", 3),
                                new OptionDto(null, "Nhanh", 4),
                                new OptionDto(null, "Rất nhanh", 5)
                        )
                ),
                new QuestionDto(null, QuestionTypeEnum.SINGLE_CHOICE, "Bạn có gặp phải lỗi hoặc sự cố nào khi sử dụng hệ thống không?",
                        Set.of(
                                new OptionDto(null, "Rất thường xuyên", 1),
                                new OptionDto(null, "Thường xuyên", 2),
                                new OptionDto(null, "Thỉnh thoảng", 3),
                                new OptionDto(null, "Hiếm khi", 4),
                                new OptionDto(null, "Chưa từng", 5)
                        )
                ),
                new QuestionDto(null, QuestionTypeEnum.NUMBER_ANSWER, "Bạn sử dụng hệ thống này bao nhiêu giờ mỗi tuần?", ""),
                new QuestionDto(null, QuestionTypeEnum.TEXT_ANSWER, "Tính năng nào của hệ thống bạn thấy hữu ích nhất?", ""),
                new QuestionDto(null, QuestionTypeEnum.TEXT_ANSWER, "Có tính năng nào bạn muốn chúng tôi bổ sung hoặc cải thiện không?", ""),
                new QuestionDto(null, QuestionTypeEnum.TEXT_ANSWER, "Xin vui lòng chia sẻ bất kỳ góp ý hoặc nhận xét nào khác của bạn.", "")
        );

        feedbackQuiz.setQuestions(questions2);
        quizServiceImp.createQuiz(feedbackQuiz);
        System.out.println("=== ĐÃ SEED XONG DỮ LIỆU MẪU ===");
    }

}