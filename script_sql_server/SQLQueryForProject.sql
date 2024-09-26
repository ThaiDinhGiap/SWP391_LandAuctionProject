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
	length DECIMAL(10, 2) NOT NULL,
	width DECIMAL(10, 2) NOT NULL,
    area DECIMAL(10, 2) NOT NULL,
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
    status VARCHAR(20) CHECK (status IN ('unverified', 'verified', 'banned')),
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
    phone_number VARCHAR(10)
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
    starting_price_per_unit bigint, 
    dealed_price bigint, 
    dealed_price_per_unit bigint, 
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
    id_issuance_date DATE,                 
    id_issuance_place NVARCHAR(max),       
    id_card_front_image_id INT FOREIGN KEY (ID_Card_Front_Image_id) REFERENCES Image(image_id),                    
    id_card_back_image_id INT FOREIGN KEY (ID_Card_Back_Image_id) REFERENCES Image(image_id),                      
    bank_account_number VARCHAR(255),                      
    bank_name NVARCHAR(max),                       
    bank_branch NVARCHAR(max),                    
    bank_owner NVARCHAR(255)
);
---------------------------------DU LIEU CO DINH (KHONG DUOC XOA)-------------------------------------------------
INSERT INTO Role (role_name) VALUES 
('Customer'),
('Admin'),
('Property Agent'),
('Auctioneer'),
('Customer Care'),
('News Writer');
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
VALUES (null, '/static/image/avatar_default.jpg', '2024-09-26 15:38:54.468');
---------------------------------DU LIEU MAU (DUOC XOA)-------------------------------------------------
INSERT INTO Local_authority (local_authority_name, contact_person, phone_number, email, local_authority_address, created_date) 
VALUES (N'Ủy ban nhân dân thành phố Hà Nội', N'Thái Đình Giáp', '0123456789', 'hanoi_authority@example.com', N'Số 12 Lê Lai, phường Lý Thái Tổ, Hoàn kiếm, Hà Nội', '2024-09-26 07:16:54.466');

INSERT INTO Asset (location, length, width, area, description, coordinates_on_map, local_authority_id, asset_status, created_date) 
VALUES (N'Thạch Hòa, Thạch Thất, Hà Nội', 100.5, 50.25, 5050.125, N'Đất của đại học FPT, rất là rộng rãi, nhiều cây xanh, ao hồ đủ cả', 'https://maps.app.goo.gl/EtRBYVZTavDJnJ6T8', 1, 'Waiting for Auction Scheduling', '2024-09-26 07:16:54.466');

INSERT INTO Notification (content, created_date, read_status) 
VALUES ('Auction has started', '2024-09-26 07:16:54.468837', 'unread');

INSERT INTO Account (username, password, status, email, avatar_image_id, role_id, registration_date) 
VALUES ('admin', '123', 'verified', 'johndoe@example.com', 1, 2, '2024-09-26 07:16:54.470494'),
('customer', '123', 'verified', 'alexpeter@example.com', 1, 1, '2024-09-26 07:16:54.470494');

INSERT INTO Staff (account_id, full_name, gender, date_of_birth, address, phone_number) 
VALUES (1, 'John Doe', 'M', '1990-01-01 00:00:00', '123 Main St, Hanoi', '0123456789');

INSERT INTO Asset_Tag (tag_id, asset_id) VALUES (1, 1);

INSERT INTO Account_Notification (notification_id, account_id) VALUES (1, 2);

INSERT INTO Customer (account_id, full_name, gender, date_of_birth, address, phone_number, tax_identification_number, citizen_identification, id_issuance_date, id_issuance_place, id_card_front_image_id, id_card_back_image_id, bank_account_number, bank_name, bank_branch, bank_owner) 
VALUES (2, 'Alex Peter', 'M', '1985-05-05', '456 Buyer St, Hanoi', '0987654321', 'TAX123456', 'ID123456789', '2010-01-01', 'Hanoi', 1, 1, '1234567890', 'Bank of Hanoi', 'Hanoi Main Branch', 'Alex Peter');

