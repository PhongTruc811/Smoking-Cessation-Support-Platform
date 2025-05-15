
-- ===============================
-- 1. Users, Roles, UserRoles
-- ===============================
CREATE TABLE Roles (
    RoleID INT PRIMARY KEY IDENTITY(1,1),
    RoleName NVARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE Users (
    UserID INT PRIMARY KEY IDENTITY(1,1),
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
    Price DECIMAL(10,2),
    DurationInDays INT
);

CREATE TABLE UserMemberships (
    UserMembershipID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT,
    PackageID INT,
    StartDate DATETIME DEFAULT GETDATE(),
    EndDate DATE,
    Status NVARCHAR(50),
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
    YearsSmoked INT,
    AddictionLevel NVARCHAR(50),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

CREATE TABLE QuitPlans (
    PlanID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT,
    Reason NVARCHAR(500),
    StartDate DATE,
    TargetEndDate DATE,
    CreatedAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

CREATE TABLE QuitPlanStages (
    StageID INT PRIMARY KEY IDENTITY(1,1),
    PlanID INT,
    StageName NVARCHAR(100),
    Description NVARCHAR(255),
    StartDate DATE,
    EndDate DATE,
    FOREIGN KEY (PlanID) REFERENCES QuitPlans(PlanID)
);

CREATE TABLE QuitProgressLogs (
    LogID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT,
    LogDate DATE,
    CigarettesSmoked INT,
    Mood NVARCHAR(100),
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
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

CREATE TABLE Badges (
    BadgeID INT PRIMARY KEY IDENTITY(1,1),
    BadgeName NVARCHAR(100),
    Description NVARCHAR(255)
);

CREATE TABLE UserBadges (
    UserBadgeID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT,
    BadgeID INT,
    AwardedAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (BadgeID) REFERENCES Badges(BadgeID)
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
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

CREATE TABLE Comments (
    CommentID INT PRIMARY KEY IDENTITY(1,1),
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
