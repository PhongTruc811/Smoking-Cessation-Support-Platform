--CREATE DATABASE AntiSmoking
--DROP DATABASE AntiSmoking

USE AntiSmoking
-- ===============================
-- 1. Users, Roles, UserRoles
-- ===============================
GO

CREATE TABLE Users (
    UserID INT PRIMARY KEY IDENTITY(1,1),
    Username VARCHAR(30) NOT NULL UNIQUE,
    FullName NVARCHAR(50),
    Email VARCHAR(50) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    DOB DATE,
    Gender CHAR(10),
    Status BIT DEFAULT 1,
    Role VARCHAR(30),
    CreatedAt DATETIME DEFAULT GETDATE()
);

-- ===============================
-- 2. Membership Packages
-- ===============================
CREATE TABLE MembershipPackages (
    PackageID INT PRIMARY KEY IDENTITY(1,1),
    PackageName NVARCHAR(100),
    Description NVARCHAR(MAX),
    Price DECIMAL(10,2) NOT NULL,
    DurationInDays INT NOT NULL,
    IsActive BIT DEFAULT 1
);

CREATE TABLE UserMemberships (
    UserMembershipID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT,
    PackageID INT,
    StartDate DATETIME DEFAULT GETDATE(),
    Status NVARCHAR(50), -- 'Pending', 'Completed', 'Failed',
    PaymentMethod NVARCHAR(50),
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (PackageID) REFERENCES MembershipPackages(PackageID)
);

-- ===============================
-- 3. Smoking Profiles and Quit Plans
-- ===============================
CREATE TABLE SmokingProfiles (
    SmokingProfileID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT UNIQUE,
    CigarettesPerDay INT,
    CostPerPack DECIMAL(10,2),
    WeekSmoked INT,
    Note NVARCHAR(MAX),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

CREATE TABLE QuitPlans (
    PlanID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT,
    Reason NVARCHAR(MAX) NOT NULL,
    StartDate DATE NOT NULL,
    TargetEndDate DATE,
    CreatedAt DATETIME DEFAULT GETDATE(),
    Status VARCHAR(20) NOT NULL, -- 'In Progess', 'Completed', 'Failed'
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

CREATE TABLE QuitPlanStages (
    StageID INT PRIMARY KEY IDENTITY(1,1),
    PlanID INT,
    StageName NVARCHAR(100),
    Description NVARCHAR(255),
    StartDate DATE,
    EndDate DATE,
    IsCompleted BIT DEFAULT 0,
    FOREIGN KEY (PlanID) REFERENCES QuitPlans(PlanID)
);

CREATE TABLE QuitProgressLogs (
    LogID INT PRIMARY KEY IDENTITY(1,1),
    StageID INT NOT NULL UNIQUE,  -- Mỗi stage chỉ có 1 log
    LogDate DATE DEFAULT GETDATE(),
    CigarettesSmoked INT,
    HealthNote NVARCHAR(MAX),
    Notes NVARCHAR(MAX),
    FOREIGN KEY (StageID) REFERENCES QuitPlanStages(StageID)
);

-- ===============================
-- 4. Notifications, Badges, Statistics
-- ===============================
CREATE TABLE Notifications (
    NotificationID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT,
    Message NVARCHAR(MAX),
    SentAt DATETIME DEFAULT GETDATE(),
    Type NVARCHAR(50),
    IsRead BIT DEFAULT 0,
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

CREATE TABLE Badges (
    BadgeID INT PRIMARY KEY IDENTITY(1,1),
    BadgeName NVARCHAR(100),
    Description NVARCHAR(255),
    ImageUrl VARCHAR(255)
);

CREATE TABLE UserBadges (
    UserId INT NOT NULL,
    BadgeId INT NOT NULL,
    AchievedDate DATETIME DEFAULT GETDATE(),
    IsShared BIT DEFAULT 0,
    PRIMARY KEY (UserId, BadgeId),
    FOREIGN KEY (UserId) REFERENCES Users(UserId),
    FOREIGN KEY (BadgeId) REFERENCES Badges(BadgeId)
);

-- ===============================
-- 5. Community & Messaging
-- ===============================
CREATE TABLE Posts (
    PostID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT,
    Title NVARCHAR(255),
    Content NVARCHAR(MAX),
    CreatedAt DATETIME DEFAULT GETDATE(),
    IsPublished BIT DEFAULT 1,
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

CREATE TABLE Comments (
    CommentID INT PRIMARY KEY IDENTITY(1,1),
    ParentCommentId INT NULL, -- Cho phép lồng nhau
    PostID INT,
    UserID INT,
    Content NVARCHAR(MAX),
    CreatedAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (ParentCommentId) REFERENCES Comments(CommentID), -- FK tự tham chiếu
    FOREIGN KEY (PostID) REFERENCES Posts(PostID),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

CREATE TABLE Likes (
    LikeID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT,
    PostID INT NULL,
    CommentID INT NULL,
    CreatedAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (PostID) REFERENCES Posts(PostID),
    FOREIGN KEY (CommentID) REFERENCES Comments(CommentID)
);

CREATE TABLE Messages (
    MessageID INT PRIMARY KEY IDENTITY(1,1),
    SenderID INT,
    ReceiverID INT,
    Content NVARCHAR(MAX),
    SentAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (SenderID) REFERENCES Users(UserID),
    FOREIGN KEY (ReceiverID) REFERENCES Users(UserID)
);

-- ===============================
-- 6. Coaching and Feedback
-- ===============================
CREATE TABLE CoachingSessions (
    SessionID INT PRIMARY KEY IDENTITY(1,1),
    CoachID INT,
    MemberID INT,
    SessionDate DATETIME,
    Notes NVARCHAR(MAX),
    FOREIGN KEY (CoachID) REFERENCES Users(UserID),
    FOREIGN KEY (MemberID) REFERENCES Users(UserID)
);

CREATE TABLE CoachFeedback (
    FeedbackID INT PRIMARY KEY IDENTITY(1,1),
    CoachID INT,
    MemberID INT,
    Rating INT CHECK (Rating BETWEEN 1 AND 5),
    Comment NVARCHAR(MAX),
    CreatedAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (CoachID) REFERENCES Users(UserID),
    FOREIGN KEY (MemberID) REFERENCES Users(UserID)
);

-- ===============================
-- 7. Admin tools
-- ===============================
CREATE TABLE Feedbacks (
    FeedbackID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT,
    Message NVARCHAR(MAX),
    SubmittedAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

CREATE TABLE ActivityLogs (
    LogID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT,
    Action NVARCHAR(255),
    LogTime DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

GO
-- SAMPLE DATA


-- 2. Users (tài khoản)
INSERT INTO Users (Username, FullName, Email, Password, DOB, Gender, Status, Role)
VALUES 
('user1', N'Nguyễn Văn An', 'vana@gmail.com', '123456', '1990-05-01', 'MALE', 1, 'MEMBER'),
('coach1', N'Trần Thị Bình', 'thib@gmail.com', '123456', '1985-09-10', 'FEMALE', 1, 'COACH'),
('admin1', N'Phạm Văn Cường', 'vanc@gmail.com', 'admin123', '1980-12-20', 'MALE', 1, 'ADMIN');

-- 4. MembershipPackages (gói thành viên)
INSERT INTO MembershipPackages (PackageName, Description, Price, DurationInDays)
VALUES 
(N'Gói cơ bản', N'Sử dụng các tính năng cơ bản', 0, 30),
(N'Gói hội viên', N'Hỗ trợ coach và thống kê nâng cao', 200000, 90);

-- 5. UserMemberships (member đăng ký gói)
INSERT INTO UserMemberships (UserID, PackageID, StartDate, Status, PaymentMethod)
VALUES 
(1, 1, GETDATE(), 'Completed', N'VNPay'),
(2, 2, GETDATE(), 'Completed', N'VNPay');

-- 6. SmokingProfiles (hồ sơ hút thuốc)
INSERT INTO SmokingProfiles (UserID, CigarettesPerDay, CostPerPack, WeekSmoked, Note)
VALUES 
(1, 10, 25000, 5, N'Hút thường ngày'),
(2, 5, 35000, 2, N'Chỉ hút khi stress');

-- 7. QuitPlans (kế hoạch cai thuốc)
INSERT INTO QuitPlans (UserID, Reason, StartDate, TargetEndDate, Status)
VALUES 
(1, N'Vì sức khỏe bản thân', '2025-05-01', '2025-07-01', 'In Progress'),
(2, N'Do gia đình động viên', '2025-06-01', '2025-08-01', 'In Progress');

-- 8. QuitPlanStages (các giai đoạn cai thuốc)
INSERT INTO QuitPlanStages (PlanID, StageName, Description, StartDate, EndDate)
VALUES 
(1, N'Giảm dần', N'Giảm số điếu mỗi ngày', '2025-05-01', '2025-06-01'),
(1, N'Ngừng hoàn toàn', N'Không hút nữa', '2025-06-02', '2025-07-01'),
(2, N'Giảm buổi sáng', N'Chỉ hút sau 10h sáng', '2025-06-01', '2025-06-15');

-- 9. Badges (huy hiệu)
INSERT INTO Badges (BadgeName, Description, ImageUrl)
VALUES 
(N'1 ngày không hút', N'Đạt thành tích 1 ngày không hút thuốc', 'badge1.png'),
(N'Tiết kiệm 100k', N'Tiết kiệm được 100,000 đồng', 'badge2.png');

-- 10. UserBadges (user đạt huy hiệu)
INSERT INTO UserBadges (UserId, BadgeId)
VALUES 
(1, 1),
(1, 2),
(2, 1);

-- 11. Posts (bài chia sẻ cộng đồng)
INSERT INTO Posts (UserID, Title, Content)
VALUES 
(1, N'Hành trình cai thuốc', N'Mình đã cai được 3 tuần, cảm thấy khỏe hơn rất nhiều.'),
(2, N'Chia sẻ kinh nghiệm', N'Nên tìm bạn đồng hành để cai thuốc hiệu quả hơn.');

-- 12. Feedbacks (góp ý)
INSERT INTO Feedbacks (UserID, Message)
VALUES 
(1, N'Nên có thêm chức năng nhắc nhở qua SMS'),
(2, N'Giao diện dùng tốt, cảm ơn team!');

-- 13. CoachFeedback (đánh giá huấn luyện viên)
INSERT INTO CoachFeedback (CoachID, MemberID, Rating, Comment)
VALUES 
(2, 1, 5, N'Coach rất nhiệt tình, tư vấn hữu ích');

-- 14. Comments (bình luận bài viết)
INSERT INTO Comments (PostID, UserID, Content)
VALUES
(1, 2, N'Cố lên bạn nhé!'),
(2, 1, N'Rất đồng ý với chia sẻ của bạn.');

-- 15. Likes (like bài viết/bình luận)
INSERT INTO Likes (UserID, PostID)
VALUES (2, 1), (1, 2);

-- 16. Notifications (thông báo)
INSERT INTO Notifications (UserID, Message, Type)
VALUES 
(1, N'Bạn đã đạt thành tích 1 ngày không hút thuốc!', N'Badge'),
(2, N'Bạn còn 5 ngày nữa để hoàn thành mục tiêu!', N'Reminder');