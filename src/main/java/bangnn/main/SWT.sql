CREATE DATABASE SWT;


USE SWT;

CREATE TABLE Book
(
    Isbn        char(13)     PRIMARY KEY ,
    Title       nvarchar(50) NOT NULL,
    Author      nvarchar(50) NOT NULL,
    Edition     int          NULL,
    PublishYear int          NULL
)

insert into Book values ('2518407786529', N'The Alchemist (Nhà giả kim)', N'Paulo Coelho', 1, 2013);
insert into Book values ('6911225907262', N'Tuổi Trẻ Đáng Giá Bao Nhiêu', N'Rosie Nguyễn', 2, 2018);
insert into Book values ('2425402340697', N'Đời Ngắn Đừng Ngủ Dài', N'Robin Sharma', 2, 2014);

CREATE TABLE Users
(
    Username   varchar(100) PRIMARY KEY,
    Password   varchar(100) NOT NULL,
    Fullname   varchar(100) NOT NULL,
    IsLoggedIn bit          NULL,
)

INSERT INTO Users VALUES ('admin', 'admin', 'Ngo Nguyen Bang')

