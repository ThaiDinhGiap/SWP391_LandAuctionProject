--------------------------------TAO DATABASE--------------------------------------------------
--create database project_land_auction_swp391
use project_land_auction_swp391
--------------------------------TAO BANG--------------------------------------------------
CREATE TABLE Role (
    role_id int PRIMARY KEY IDENTITY(1,1), 
    role_name NVARCHAR(100) NOT NULL         
);
CREATE TABLE Tag (
    tag_id INT PRIMARY KEY IDENTITY(1,1),
    tag_name NVARCHAR(100) NOT NULL,
    description NVARCHAR(max)
);
CREATE TABLE Local_authority (
    local_authority_id INT PRIMARY KEY IDENTITY(1,1), 
    local_authority_name NVARCHAR(max) NOT NULL,     
    contact_person NVARCHAR(100) NOT NULL,          
    phone_number VARCHAR(10) NOT NULL,             
    email VARCHAR(100) NOT NULL,                     
    local_authority_address NVARCHAR(max),                
    created_date datetime2(3)         
);
CREATE TABLE Asset (
    asset_id INT PRIMARY KEY IDENTITY(1,1),
    location NVARCHAR(max) NOT NULL,
	length DECIMAL(6, 2) NOT NULL,
	width DECIMAL(6, 2) NOT NULL,
    area DECIMAL(12, 4) NOT NULL,
    description NVARCHAR(max),
    coordinates_on_map NVARCHAR(max),
    local_authority_id INT FOREIGN KEY REFERENCES Local_authority(local_authority_id),
    asset_status NVARCHAR(100),
	created_date datetime2(3)
);
CREATE TABLE Image (
    image_id INT PRIMARY KEY IDENTITY(1,1),
    asset_id INT FOREIGN KEY REFERENCES Asset(asset_id),
    path VARCHAR(255) NOT NULL,
    upload_date datetime2(3)
);
CREATE TABLE Notification (
    notification_id INT PRIMARY KEY IDENTITY(1,1),
    content NVARCHAR(max) NOT NULL,
    created_date datetime2(3),
    read_status NVARCHAR(100)
);
CREATE TABLE Document (
    document_id INT PRIMARY KEY IDENTITY(1,1),
    asset_id INT FOREIGN KEY REFERENCES Asset(asset_id),
    document_name NVARCHAR(255),
    path VARCHAR(255) NOT NULL,
    upload_date datetime2(3)
);
CREATE TABLE Account (
    account_id INT PRIMARY KEY IDENTITY(1,1), 
    username VARCHAR(100),          
    password VARCHAR(100),          
    status BIT NOT NULL,
	verify BIT NOT NULL,
    email VARCHAR(100),
	avatar_image_id INT FOREIGN KEY REFERENCES Image(image_id),
    role_id INT FOREIGN KEY REFERENCES Role(role_id),                              
    registration_date datetime2(3) 
);
CREATE TABLE Staff (
    staff_id INT PRIMARY KEY IDENTITY(1,1),
    account_id INT FOREIGN KEY REFERENCES Account(account_id), 
    full_name NVARCHAR(100), 
    gender NVARCHAR(1) CHECK (gender IN ('F', 'M')), 
    date_of_birth datetime2(3), 
    address NVARCHAR(max),                   
    phone_number VARCHAR(10),
	is_available BIT DEFAULT 1
);
CREATE TABLE Ban_log (
    log_id INT PRIMARY KEY IDENTITY(1,1),         
    admin_id INT FOREIGN KEY REFERENCES Account(account_id),                        
    account_id INT FOREIGN KEY REFERENCES Account(account_id),           
    timestamp datetime2(3),         
    reason NVARCHAR(max)
);
CREATE TABLE Auction_session (
    auction_id INT PRIMARY KEY IDENTITY(1,1), 
    auction_name NVARCHAR(255), 
    start_time datetime2(3), 
    expected_end_time datetime2(3), 
    actual_end_time datetime2(3), 
    starting_price bigint, 
    dealed_price bigint, 
    current_highest_price bigint, 
    winner_id INT FOREIGN KEY REFERENCES Account(account_id), 
    minimum_bid_increment bigint, 
    deposit bigint, 
    register_fee bigint,
    extra_time_unit INT, 
    status NVARCHAR(50), 
    registration_open_date DATETIME2(3),
    registration_close_date DATETIME2(3),
    auctioneer_id INT FOREIGN KEY REFERENCES Account(account_id),  
    asset_id INT FOREIGN KEY REFERENCES Asset(asset_id) 
);
CREATE TABLE Auction_change_log (
    change_id INT PRIMARY KEY IDENTITY(1,1),
    change_type NVARCHAR(255),
    reason NVARCHAR(max), 
    auction_id INT FOREIGN KEY REFERENCES Auction_session(auction_id),  
    time datetime2(3) 
);
CREATE TABLE Auction_register (
	register_id INT PRIMARY KEY IDENTITY(1,1),
    auction_id INT NOT NULL FOREIGN KEY REFERENCES Auction_session(auction_id),
    buyer_id INT NOT NULL FOREIGN KEY (buyer_id) REFERENCES Account(account_id),
    result NVARCHAR(100),
    rank INT,
    register_status NVARCHAR(100),
    purchase_status NVARCHAR(100),
    deposit_status NVARCHAR(100),
    nick_name NVARCHAR(100),
    registration_time DATETIME2(3),
);
CREATE TABLE Bid (
    bid_id INT PRIMARY KEY IDENTITY(1,1),
    register_id INT FOREIGN KEY REFERENCES Auction_register(register_id),
    bid_amount BIGINT,
    time_create_bid DATETIME2(3),
);
CREATE TABLE Asset_Tag (
    tag_id INT NOT NULL FOREIGN KEY (tag_id) REFERENCES Tag(tag_id),
    asset_id INT NOT NULL FOREIGN KEY (asset_id) REFERENCES Asset(asset_id),
    PRIMARY KEY (tag_id, asset_id)
);
CREATE TABLE Account_Notification (
    notification_id INT FOREIGN KEY REFERENCES Notification(notification_id),
    account_id INT FOREIGN KEY REFERENCES Account(account_id) --account nguoi nhan
	PRIMARY KEY (notification_id, account_id)
);
CREATE TABLE Task (
    task_id INT PRIMARY KEY IDENTITY(1,1),  
    property_agent_id INT NOT NULL FOREIGN KEY REFERENCES Account(account_id), --ma nguoi giao viec              
    auctioneer_id INT NOT NULL FOREIGN KEY REFERENCES Account(account_id),   --ma nguoi nhan viec             
    asset_id INT NOT NULL FOREIGN KEY REFERENCES Asset(asset_id),                 
    content_task NVARCHAR(max),   
    created_date datetime2(3), 
    finished_date datetime2(3),           
    status NVARCHAR(100)
);
CREATE TABLE Customer (
    customer_id INT PRIMARY KEY IDENTITY(1,1),      
    account_id INT FOREIGN KEY REFERENCES Account(account_id),                        
    full_name NVARCHAR(100),             
    gender NVARCHAR(1) CHECK (gender IN ('F', 'M')),                 
    date_of_birth datetime2(3),
    address NVARCHAR(max),                
    phone_number VARCHAR(10),             
    tax_identification_number VARCHAR(50),        
    citizen_identification VARCHAR(50),  
    id_issuance_date datetime2(3),                 
    id_issuance_place NVARCHAR(max),       
    id_card_front_image_id INT FOREIGN KEY REFERENCES Image(image_id),                    
    id_card_back_image_id INT FOREIGN KEY REFERENCES Image(image_id),                      
    bank_account_number VARCHAR(255),                      
    bank_name NVARCHAR(max),                       
    bank_branch NVARCHAR(max),                    
    bank_owner NVARCHAR(max)
);
CREATE TABLE Topic (
    topic_id INT PRIMARY KEY IDENTITY(1,1),
    parent_topic_id INT FOREIGN KEY REFERENCES Topic(topic_id), 
    topic_name NVARCHAR(MAX) NOT NULL, 
);
CREATE TABLE Question (
    question_id INT PRIMARY KEY IDENTITY(1,1),
    topic_id INT NOT NULL FOREIGN KEY REFERENCES Topic(topic_id),
    question NVARCHAR(max) NOT NULL,
    answer NVARCHAR(max) NOT NULL
);
CREATE TABLE Tag_for_news (
    tag_id INT PRIMARY KEY IDENTITY(1,1), 
    tag_name NVARCHAR(MAX) NOT NULL       
);
CREATE TABLE News (
    news_id INT PRIMARY KEY IDENTITY(1,1), 
    title NVARCHAR(MAX) NOT NULL,          
    content NVARCHAR(MAX) NOT NULL,     
	cover_photo_id INT FOREIGN KEY REFERENCES Image(image_id),
    created_date datetime2(3), 
    staff_id INT FOREIGN KEY REFERENCES Account(account_id)
);
CREATE TABLE News_Tag_for_news (
    tag_id INT NOT NULL FOREIGN KEY REFERENCES Tag_for_news(tag_id),
    news_id INT NOT NULL FOREIGN KEY REFERENCES News(news_id),
    PRIMARY KEY (tag_id, news_id)
);
---------------------------------DU LIEU CO DINH (KHONG DUOC XOA)-------------------------------------------------
INSERT INTO Tag_for_news(tag_name) VALUES
('Law'),
('Auction Notice'),
('Auction Results'),
('Other News');
INSERT INTO Role (role_name) VALUES 
('ROLE_Customer'),
('ROLE_Admin'),
('ROLE_Property_Agent'),
('ROLE_Auctioneer'),
('ROLE_Customer_Care'),
('ROLE_News_Writer');
INSERT INTO Tag (tag_name, description) VALUES 
('Residential', 'Residential: Land designated for building houses'),
('Commercial', 'Commercial: Land used for business, retail, or office purposes'),
('Industrial', 'Industrial: Land for building factories, warehouses, or industrial facilities'),
('Agricultural', 'Agricultural: Land used for farming or livestock raising'),
('Vacant Land', 'Vacant Land: Land without any construction, suitable for various purposes'),
('Investment', 'Investment: Land with potential for value appreciation, suitable for long-term investment'),
('Prime Location', 'Prime Location: Land located in a convenient, accessible, or developed area'),
('Near Amenities', 'Near Amenities: Land close to facilities such as schools, hospitals, supermarkets, or parks'),
('Access to Public Transportation', 'Access to Public Transportation: Land near bus stops, train stations, or major roads'),
('Waterfront', 'Waterfront: Land located near water sources such as rivers, lakes, or the sea'),
('Developed Land', 'Developed Land: Land that has been planned and has existing infrastructure like electricity, water, and roads'),
('Undeveloped Land', 'Undeveloped Land: Land that is unplanned and lacks infrastructure'),
('Forest Land', 'Forest Land: Land with many trees, typically used for conservation or logging purposes'),
('Hilly/Terrain', 'Hilly/Terrain: Land with uneven terrain, such as hills or mountains'),
('Flood Zone', 'Flood Zone: Land located in areas prone to flooding'),
('Corner Lot', 'Corner Lot: Land located at the corner of two streets, often with higher value'),
('Subdivision Potential', 'Subdivision Potential: Land with the potential to be divided into smaller lots'),
('Near Highway', 'Near Highway: Land close to highways, offering convenient transportation access'),
('Rural', 'Rural: Land located in rural areas, usually quiet and far from urban centers'),
('Urban', 'Urban: Land located within cities or urban centers');
INSERT INTO Image (asset_id, path, upload_date) 
VALUES (null, '/image/avatar_default.jpg', '2024-09-26 15:38:54.468'),
(null, '/image/auction_hammer.jpg', '2024-09-26 15:38:54.468');
INSERT INTO Topic (topic_name, parent_topic_id) VALUES 
('Auction Participation Process', NULL),
('Account Registration', 1),
('Auction Participation Registration', 1),
('Deposit Payment', 1),
('System Usage Guide', NULL),
('Personal Account', 5),
('Transaction and Auction History', 5),
('Auction Asset Information', NULL),
('Rights and Responsibilities of Auction Participants', NULL);
-- Insert questions for "Đăng ký tài khoản"
INSERT INTO Question (topic_id, question, answer) VALUES 
(2, 'How to register an account on the auction system?', 'To register an account on the VietLand Auction system, you need to access the registration page on the website, then fill in your personal information such as full name, email, and password. After filling in all the information, click the "Register" button to complete the process.'),
(2, 'What information do I need to prepare to register an account?', 'You need to prepare personal information including: full name, valid email address, phone number, tax identification number, and ID/CCCD. In addition, you need to create a password to secure your account.'),
(2, 'How to verify the account after registration?', 'After completing the registration, the system will send a verification email to the email address you registered. You need to check the email and click on the verification link to activate the account.');
-- Insert questions for "Đăng ký tham gia đấu giá"
INSERT INTO Question (topic_id, question, answer) VALUES 
(3, 'How to register to participate in an auction session?', 'To register for an auction session, you need to log into the VietLand Auction system, find and select the auction session you want to join. Then click the "Register to participate" button and follow the instructions on the system.'),
(3, 'What is the registration fee to participate in the auction?', 'The auction registration fee will be announced in the detailed information of each auction session. You need to check this information before registering to prepare the necessary amount.'),
(3, 'When does the registration open and close?', 'The registration opening and closing times will be displayed in the information of each auction session on the system. You should monitor this information so as not to miss the opportunity to participate.');
-- Insert questions for "Nộp tiền đặt cọc"
INSERT INTO Question (topic_id, question, answer) VALUES 
(4, 'Will the deposit be refunded if I do not win the auction?', 'The deposit will be refunded to you if you do not win the auction session. However, the refund process will depend on the VietLand Auction regulations and usually takes some time to process.');
-- Insert questions for "Tài khoản cá nhân"
INSERT INTO Question (topic_id, question, answer) VALUES 
(6, 'How to change personal information?', 'You can change your personal information by logging into your account. After logging in, go to the "Personal Information" or "My Account" section and select "Edit". Here, you can change information such as name, email, phone number, address, etc.'),
(6, 'How to update the profile picture?', 'To update your profile picture, after logging in, go to the "Personal Information" or "My Account" section. Find the profile picture section and select the "Change profile picture" option. You can upload a new picture from your computer or mobile device and save the changes.');
-- Insert questions for "Lịch sử giao dịch và đấu giá"
INSERT INTO Question (topic_id, question, answer) VALUES 
(7, 'How to view the history of auctions I participated in?', 'To view the history of auctions you participated in, you can access the "Personal Account" section on the VietLand Auction page. Then find the "Auction History" or "Participated Auctions" section. Here, you will see the list of auctions you participated in, including detailed information such as asset name, bid price, and auction results.'),
(7, 'How to check my deposit, payment, and top-up history?', 'To check your deposit, payment, and top-up history, you need to go to the "Wallet" or "Account Transactions" section in your personal page. Here, you can see the details of transactions such as: Top-up into the account, Deposit amounts for auction sessions, Payments for won assets, Withdrawal history and deposit refunds.');
-- Insert questions for "Thông tin về tài sản đấu giá"
INSERT INTO Question (topic_id, question, answer) VALUES 
(8, 'How to search for auctioned assets?', 'You can search for auctioned assets by accessing the "List of Auctioned Assets" page on the VietLand Auction system. Here, you can use search filters such as asset type, location, starting price, or auction status to narrow down the list and find assets that match your needs.'),
(8, 'What information is included in the auctioned asset details?', 'Detailed information about an auctioned asset typically includes: Asset name, Detailed description (area, location, legal status, etc.), Starting price and bid increment, Start and end times of the auction session, Images and documents related to the asset, Contact of the person in charge or asset manager.'),
(8, 'How to view images and documents related to the auctioned asset?', 'To view images and documents related to an auctioned asset, you need to access the asset detail page you are interested in. Here, you will find images, drawings, legal documents, and other related information to help you understand the asset before participating in the auction.');
-- Insert questions for "Quyền lợi và trách nhiệm của người tham gia đấu giá"
INSERT INTO Question (topic_id, question, answer) VALUES 
(9, 'What are the benefits for auction participants?', 'Auction participants have the following benefits: Full access to information about the auctioned asset, Participation in the auction process in a fair and transparent manner, The right to withdraw the deposit if not winning the auction session, Support with inquiries throughout the auction process.'),
(9, 'What are the responsibilities of the winner after the auction session?', 'After winning the auction session, the participant has the following responsibilities: Completing the payment according to the regulations within the specified time, Signing documents and contracts related to the asset, Complying with the terms and agreements made during the auction process.'),
(9, 'Are there any support policies for auction participants?', 'The auction system may provide support policies such as: Guidance and answering inquiries during the auction process, Providing consultation information about the asset and legal procedures, Support in completing procedures after winning the auction.');
---------------------------------DU LIEU MAU (DUOC XOA)-------------------------------------------------
INSERT INTO Local_authority (local_authority_name, contact_person, phone_number, email, local_authority_address, created_date) 
VALUES (N'Ủy ban nhân dân thành phố Hà Nội', N'Thái Đình Giáp', '0123456789', 'hanoi_authority@example.com', N'Số 12 Lê Lai, phường Lý Thái Tổ, Hoàn kiếm, Hà Nội', '2024-09-26 07:16:54.466');

INSERT INTO Notification (content, created_date, read_status) 
VALUES (N'Hê sờ lô, hô sờ ly ly!!!', '2024-09-26 07:16:54.468837', 'unread');

INSERT INTO Account (username, password, status, verify, email, avatar_image_id, role_id, registration_date) 
VALUES ('admin', '$2a$10$43UPoYJoq5cJT.U6bSrZPOAQ4K.GrN8F5JzhGdBcxy.ZfFpvrsUAi', 1, 1, 'johndoe@example.com', 1, 2, '2024-09-26 07:16:54.470494'),
('propertyagent', '$2a$10$43UPoYJoq5cJT.U6bSrZPOAQ4K.GrN8F5JzhGdBcxy.ZfFpvrsUAi', 1, 1, 'chickenrice@example.com', 1, 3, '2024-09-26 07:16:54.470494'),
('auctioneer', '$2a$10$43UPoYJoq5cJT.U6bSrZPOAQ4K.GrN8F5JzhGdBcxy.ZfFpvrsUAi', 1, 1, 'bunbohue@example.com', 1, 4, '2024-09-26 07:16:54.470494'),
('customercare', '$2a$10$43UPoYJoq5cJT.U6bSrZPOAQ4K.GrN8F5JzhGdBcxy.ZfFpvrsUAi', 1, 1, 'banhmy@example.com', 1, 5, '2024-09-26 07:16:54.470494'),
('newswriter', '$2a$10$43UPoYJoq5cJT.U6bSrZPOAQ4K.GrN8F5JzhGdBcxy.ZfFpvrsUAi', 1, 1, 'meomeo@example.com', 1, 6, '2024-09-26 07:16:54.470494'),
('customer', '$2a$10$43UPoYJoq5cJT.U6bSrZPOAQ4K.GrN8F5JzhGdBcxy.ZfFpvrsUAi', 1, 1, 'alexpeter@example.com', 1, 1, '2024-09-26 07:16:54.470494');

INSERT INTO Staff (account_id, full_name, gender, date_of_birth, address, phone_number) 
VALUES (1, 'John Doe', 'M', '1990-01-01 00:00:00', '123 Main St, Hanoi', '0123456789'),
(2, 'Chicken Rice', 'M', '1990-01-01 00:00:00', '123 Main St, Hanoi', '0123456789'),
(3, 'Bun Bo Hue', 'F', '1990-01-01 00:00:00', '123 Main St, Hanoi', '0123456789'),
(4, 'Banh My', 'M', '1990-01-01 00:00:00', '123 Main St, Hanoi', '0123456789'),
(5, 'Meo Meo', 'F', '1990-01-01 00:00:00', '123 Main St, Hanoi', '0123456789');

INSERT INTO Customer (account_id, full_name, gender, date_of_birth, address, phone_number, tax_identification_number, citizen_identification, id_issuance_date, id_issuance_place, id_card_front_image_id, id_card_back_image_id, bank_account_number, bank_name, bank_branch, bank_owner) 
VALUES (6, 'Alex Peter', 'M', '1985-05-05', '456 Buyer St, Hanoi', '0987654321', 'TAX123456', 'ID123456789', '2010-01-01', 'Hanoi', 1, 1, '1234567890', 'Bank of Hanoi', 'Hanoi Main Branch', 'Alex Peter');

INSERT INTO Account_Notification (notification_id, account_id) VALUES (1, 2);




