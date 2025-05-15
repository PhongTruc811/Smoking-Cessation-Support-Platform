
-- ===============================
-- DEMO SCHEMA
-- ===============================

-- 1. Roles & Users
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

-- 2. Membership Packages
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

-- 3. Smoking Profile & Quit Plan
CREATE TABLE SmokingProfiles (
    SmokingProfileID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT UNIQUE,
    CigarettesPerDay INT,
    CostPerPack DECIMAL(10,2),
    MonthsSmoked INT,
    AddictionLevel NVARCHAR(50),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

CREATE TABLE QuitPlans (
    PlanID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT,
    Reason NVARCHAR(500),
    StartDate DATE,
    TargetEndDate DATE,
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

-- 4. Community & Blog
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

-- 5. Admin Monitoring
CREATE TABLE ActivityLogs (
    LogID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT,
    Action NVARCHAR(255),
    LogTime DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);
