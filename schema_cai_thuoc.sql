
-- ===============================
-- 1. Users, Roles, UserRoles
-- ===============================
CREATE TABLE Roles (
    RoleID INT PRIMARY KEY IDENTITY(1,1),
    RoleName NVARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE Users (
    UserID INT PRIMARY KEY IDENTITY(1,1),
    Username VARCHAR(50) NOT NULL UNIQUE,
    FullName NVARCHAR(50),
    Email VARCHAR(100) NOT NULL UNIQUE,
    PasswordHash VARCHAR(100) NOT NULL,
    DOB DATE,
    Gender CHAR(1),
    Status BIT DEFAULT 1,
    CreatedAt DATETIME DEFAULT GETDATE()
);

CREATE TABLE UserRoles (
    UserID INT,
    RoleID INT,
    PRIMARY KEY (UserID, RoleID),
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (RoleID) REFERENCES Roles(RoleID)
);

-- ===============================
-- 2. Membership Packages
-- ===============================
CREATE TABLE MembershipPackages (
    PackageID INT PRIMARY KEY IDENTITY(1,1),
    PackageName NVARCHAR(100),
    Description NVARCHAR(255),
    Price DECIMAL(10,2) NOT NULL,
    DurationInDays INT NOT NULL,
    IsActive BIT DEFAULT 1
);

CREATE TABLE UserMemberships (
    UserMembershipID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT,
    PackageID INT,
    StartDate DATETIME DEFAULT GETDATE(),
    EndDate DATE,
    Status NVARCHAR(50) -- 'Pending', 'Completed', 'Failed',
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
    UserID INT,
    LogDate DATE,
    CigarettesSmoked INT,
    HealthNote NVARCHAR(MAX),
    Notes NVARCHAR(MAX),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
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
    UserId UNIQUEIDENTIFIER NOT NULL,
    BadgeId UNIQUEIDENTIFIER NOT NULL,
    AchievedDate DATETIME DEFAULT GETDATE(),
    IsShared BIT DEFAULT 0,
    PRIMARY KEY (UserId, BadgeId),
    FOREIGN KEY (UserId) REFERENCES Users(UserId),
    FOREIGN KEY (BadgeId) REFERENCES AchievementBadges(BadgeId)
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
    ParentCommentId UNIQUEIDENTIFIER, -- Các bình luận lồng nhau
    PostID INT,
    UserID INT,
    Content NVARCHAR(MAX),
    CreatedAt DATETIME DEFAULT GETDATE(),
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
