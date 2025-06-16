package com.group_7.backend.config;

import com.group_7.backend.entity.*;
import com.group_7.backend.entity.enums.MembershipStatusEnum;
import com.group_7.backend.entity.enums.QuitPlanStatusEnum;
import com.group_7.backend.entity.enums.UserGenderEnum;
import com.group_7.backend.entity.enums.UserRoleEnum;
import com.group_7.backend.repository.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataInit implements ApplicationRunner {

    @Autowired private UserRepository userRepository;
    @Autowired private MembershipPackageRepository membershipPackageRepository;
    @Autowired private UserMembershipRepository userMembershipRepository;
    @Autowired private SmokingProfileRepository smokingProfileRepository;
    @Autowired private QuitPlanRepository quitPlanRepository;
    @Autowired private QuitPlanStageRepository quitPlanStageRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private CommentRepository commentRepository;
    @Autowired private LikeRepository likeRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        System.out.println("=== BẮT ĐẦU SEED DỮ LIỆU MẪU ===");
        if (userRepository.count() > 0) {
            System.out.println("Dữ liệu đã tồn tại, không seed lại.");
            return;
        }
        // Avoid duplicate seed

        // 2. Users
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

        // 4. MembershipPackages
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

        // 5. UserMemberships
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

        // 6. SmokingProfiles
        SmokingProfile sp1 = new SmokingProfile();
        sp1.setUser(user1);
        sp1.setCigarettesPerDay(10);
        sp1.setCostPerPack(25000.0);
        sp1.setWeekSmoked(5);
        sp1.setNote("Hút thường ngày");
        smokingProfileRepository.save(sp1);

        SmokingProfile sp2 = new SmokingProfile();
        sp2.setUser(coach1);
        sp2.setCigarettesPerDay(5);
        sp2.setCostPerPack(35000.0);
        sp2.setWeekSmoked(2);
        sp2.setNote("Chỉ hút khi stress");
        smokingProfileRepository.save(sp2);

        // 7. QuitPlans
        QuitPlan plan1 = new QuitPlan();
        plan1.setUser(user1);
        plan1.setReason("Vì sức khỏe bản thân");
        plan1.setStartDate(LocalDate.of(2025,5,1));
        plan1.setTargetEndDate(LocalDate.of(2025,7,1));
        plan1.setStatus(QuitPlanStatusEnum.IN_PROGRESS);
        quitPlanRepository.save(plan1);

        QuitPlan plan2 = new QuitPlan();
        plan2.setUser(coach1);
        plan2.setReason("Do gia đình động viên");
        plan2.setStartDate(LocalDate.of(2025,6,1));
        plan2.setTargetEndDate(LocalDate.of(2025,8,1));
        plan2.setStatus(QuitPlanStatusEnum.IN_PROGRESS);
        quitPlanRepository.save(plan2);

        // 8. QuitPlanStages
        QuitPlanStage stage1 = new QuitPlanStage();
        stage1.setQuitPlan(plan1);
        stage1.setStageName("Giảm dần");
        stage1.setDescription("Giảm số điếu mỗi ngày");
        stage1.setStartDate(LocalDate.of(2025,5,1));
        stage1.setEndDate(LocalDate.of(2025,6,1));
        quitPlanStageRepository.save(stage1);

        QuitPlanStage stage2 = new QuitPlanStage();
        stage2.setQuitPlan(plan1);
        stage2.setStageName("Ngừng hoàn toàn");
        stage2.setDescription("Không hút nữa");
        stage2.setStartDate(LocalDate.of(2025,6,2));
        stage2.setEndDate(LocalDate.of(2025,7,1));
        quitPlanStageRepository.save(stage2);

        QuitPlanStage stage3 = new QuitPlanStage();
        stage3.setQuitPlan(plan2);
        stage3.setStageName("Giảm buổi sáng");
        stage3.setDescription("Chỉ hút sau 10h sáng");
        stage3.setStartDate(LocalDate.of(2025,6,1));
        stage3.setEndDate(LocalDate.of(2025,6,15));
        quitPlanStageRepository.save(stage3);

        // 11. Posts
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

        // 14. Comments
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

        // 15. Likes
        Like like1 = new Like();
        like1.setUser(coach1);
        like1.setPost(post1);
        likeRepository.save(like1);

        Like like2 = new Like();
        like2.setUser(user1);
        like2.setPost(post2);
        likeRepository.save(like2);

        System.out.println("=== ĐÃ SEED XONG DỮ LIỆU MẪU ===");
    }

}