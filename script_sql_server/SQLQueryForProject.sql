--create database project_land_auction_swp391
use project_land_auction_swp391
----------------------------------------------------------------------------------
CREATE TABLE Role (
    role_id int PRIMARY KEY, 
    role_name NVARCHAR(100) NOT NULL         
);
CREATE TABLE Tag (
    tag_id INT PRIMARY KEY,
    tag_name NVARCHAR(100) NOT NULL,
    description NVARCHAR(max)
);
CREATE TABLE Local_authority (
    local_authority_id INT PRIMARY KEY, 
    local_authority_name NVARCHAR(max) NOT NULL,     
    contact_person NVARCHAR(100) NOT NULL,          
    phone_number VARCHAR(10) NOT NULL,             
    email VARCHAR(100) NOT NULL,                     
    local_authority_address NVARCHAR(max),                
    created_date datetime2(3)         
);
CREATE TABLE Asset (
    asset_id INT PRIMARY KEY,
    location NVARCHAR(max) NOT NULL,
	length DECIMAL(10, 2) NOT NULL,
	width DECIMAL(10, 2) NOT NULL,
    area DECIMAL(10, 2) NOT NULL,
    description NVARCHAR(max),
    classify NVARCHAR(max),
    local_authority_id INT FOREIGN KEY REFERENCES Local_authority(local_authority_id),
    asset_status NVARCHAR(100)
);
CREATE TABLE Image (
    image_id INT PRIMARY KEY,
    asset_id INT FOREIGN KEY REFERENCES Asset(asset_id),
    --response_id INT FOREIGN KEY REFERENCES Response(response_id),
    path VARCHAR(255) NOT NULL,
    upload_date datetime2(3)
);
CREATE TABLE Notification (
    notification_id INT PRIMARY KEY,
    content NVARCHAR(max) NOT NULL,
    created_date datetime2(3),
    read_status NVARCHAR(100)
);
CREATE TABLE Document_type (
    type_id INT PRIMARY KEY,
    type_name NVARCHAR(255) NOT NULL
);
CREATE TABLE Document (
    document_id INT PRIMARY KEY,
    asset_id INT FOREIGN KEY REFERENCES Asset(asset_id),
    --response_id INT FOREIGN KEY REFERENCES Response(response_id),
    document_name NVARCHAR(255),
    path VARCHAR(255) NOT NULL,
    type_id INT FOREIGN KEY REFERENCES Document_Type(type_id),
    upload_date datetime2(3)
);
CREATE TABLE Account (
    account_id VARCHAR(8) PRIMARY KEY, 
    username VARCHAR(100) NOT NULL,          
    password VARCHAR(100) NOT NULL,          
    status VARCHAR(20) CHECK (status IN ('unverified', 'verified', 'banned')),
    email VARCHAR(100),
	avatar_image_id INT FOREIGN KEY REFERENCES Image(image_id),
    role_id INT FOREIGN KEY REFERENCES Role(role_id),                              
    registration_date datetime2(3) 
);
CREATE TABLE Staff (
    staff_id INT PRIMARY KEY,
    account_id VARCHAR(8) FOREIGN KEY REFERENCES Account(account_id), 
    full_name NVARCHAR(100), 
    gender NVARCHAR(1) CHECK (gender IN ('F', 'M')), 
    date_of_birth datetime2(3), 
    address NVARCHAR(max),                   
    phone_number VARCHAR(10)
);
CREATE TABLE Ban_log (
    log_id INT PRIMARY KEY,         
    admin_id VARCHAR(8) FOREIGN KEY REFERENCES Account(account_id),                        
    account_id VARCHAR(8) FOREIGN KEY REFERENCES Account(account_id),           
    timestamp datetime2(3),         
    reason NVARCHAR(max)
);
CREATE TABLE Auction_session (
    auction_id INT PRIMARY KEY, 
    auction_name NVARCHAR(255), 
    start_time datetime2(3), 
    expected_end_time datetime2(3), 
    actual_end_time datetime2(3), 
    starting_price bigint, 
    starting_price_per_unit bigint, 
    dealed_price bigint, 
    dealed_price_per_unit bigint, 
    current_highest_price bigint, 
    winner_id VARCHAR(8) FOREIGN KEY REFERENCES Account(account_id), 
    minimum_bid_increment bigint, 
    deposit bigint, 
    register_fee bigint,
    extra_time_unit INT, 
    status NVARCHAR(50), 
    registration_open_date DATETIME2(3),
    registration_close_date DATETIME2(3),
    auctioneer_id VARCHAR(8) FOREIGN KEY REFERENCES Account(account_id),  
    asset_id INT FOREIGN KEY REFERENCES Asset(asset_id) 
);
CREATE TABLE Auction_change_log (
    change_id INT PRIMARY KEY,
    change_type NVARCHAR(255),
    reason NVARCHAR(max), 
    auction_id INT FOREIGN KEY REFERENCES Auction_session(auction_id),  
    time datetime2(3) 
);
CREATE TABLE Auction_register (
	register_id INT PRIMARY KEY,
    auction_id INT NOT NULL FOREIGN KEY REFERENCES Auction_session(auction_id),
    buyer_id VARCHAR(8) NOT NULL FOREIGN KEY (buyer_id) REFERENCES Account(account_id),
    result NVARCHAR(100),
    rank INT,
    register_status NVARCHAR(100),
    purchase_status NVARCHAR(100),
    deposit_status NVARCHAR(100),
    nick_name NVARCHAR(100),
    registration_time DATETIME2(3),
    --PRIMARY KEY (auction_id, buyer_id),
);
CREATE TABLE Bid (
    bid_id INT PRIMARY KEY,
    register_id INT FOREIGN KEY REFERENCES Auction_register(register_id),
    bid_amount BIGINT,
    time_create_bid DATETIME2(3),
);
CREATE TABLE Asset_Tag (
    tag_id INT NOT NULL FOREIGN KEY (tag_id) REFERENCES Tag(tag_id),
    asset_id INT NOT NULL FOREIGN KEY (asset_id) REFERENCES Asset(asset_id),
    PRIMARY KEY (tag_id, asset_id)
);
-- CREATE TABLE Response (
--    response_id INT PRIMARY KEY,
--    asset_id INT FOREIGN KEY REFERENCES Asset(asset_id),
--    manager_id VARCHAR(8) FOREIGN KEY REFERENCES Account(account_id),
--    title NVARCHAR(255),
--    content NVARCHAR(max),
--    conclusion NVARCHAR(max),
--    create_date datetime2(3),
--);
CREATE TABLE Account_Notification (
    notification_id INT FOREIGN KEY REFERENCES Notification(notification_id),
    account_id VARCHAR(8) FOREIGN KEY REFERENCES Account(account_id) --account nguoi nhan
	PRIMARY KEY (notification_id, account_id)
);
CREATE TABLE Task (
    task_id INT PRIMARY KEY,  
    property_agent_id VARCHAR(8) NOT NULL FOREIGN KEY REFERENCES Account(account_id), --ma nguoi giao viec              
    auctioneer_id VARCHAR(8) NOT NULL FOREIGN KEY REFERENCES Account(account_id),   --ma nguoi nhan viec             
    asset_id INT NOT NULL FOREIGN KEY REFERENCES Asset(asset_id),                 
    content_task NVARCHAR(max),   
    created_date datetime2(3), 
    finished_date datetime2(3),           
    status NVARCHAR(100)
);
CREATE TABLE Customer (
    customer_id INT PRIMARY KEY IDENTITY(1,1),      
    account_id VARCHAR(8) FOREIGN KEY REFERENCES Account(account_id),                        
    full_name NVARCHAR(100),             
    gender NVARCHAR(1) CHECK (gender IN ('F', 'M')),                 
    date_of_birth datetime2(3),                    
    address NVARCHAR(max),                
    phone_number VARCHAR(10),             
    tax_identification_number VARCHAR(50),        
    citizen_identification VARCHAR(50),  
    id_issuance_date DATE,                 
    id_issuance_place NVARCHAR(max),       
    id_card_front_image_id INT FOREIGN KEY (ID_Card_Front_Image_id) REFERENCES Image(image_id),                    
    id_card_back_image_id INT FOREIGN KEY (ID_Card_Back_Image_id) REFERENCES Image(image_id),                      
    bank_account_number VARCHAR(255),                      
    bank_name NVARCHAR(max),                       
    bank_branch NVARCHAR(max),                    
    bank_owner NVARCHAR(255)
);
----------------------------------------------------------------------------------

