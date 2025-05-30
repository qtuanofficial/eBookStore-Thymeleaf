-- Insert into users
INSERT INTO users (avatar, first_name, last_name, username, email, password, birth_of_date, phone_number) VALUES
('avatar1.jpg', 'Nguyen', 'Van An', 'nguyenvanan', 'nguyenvanan@gmail.com', '$12$Nu32f2FSt9.NmT6kWNAyN.1r1D59H/ckxbzjDGLF2TPd7m5tAWtA2', '1990-05-15', '0905123456'),
('avatar2.jpg', 'Tran', 'Thi Bich', 'tranthibich', 'tranthibich@gmail.com', 'hashed_password2', '1995-08-20', '0918765432'),
('avatar3.jpg', 'Le', 'Hoang', 'lehoang', 'lehoang@gmail.com', 'hashed_password3', '1988-03-10', '0933123456');

-- Insert into addresses
INSERT INTO addresses (user_id, address_line, ward, district, city, country, postal_code) VALUES
(1, '123 Đường Láng', 'Láng Thượng', 'Đống Đa', 'Hà Nội', 'Vietnam', '100000'),
(1, '45 Nguyễn Huệ', 'Bến Nghé', 'Quận 1', 'TP Hồ Chí Minh', 'Vietnam', '700000'),
(2, '78 Trần Phú', 'Phường 5', 'Quận 5', 'TP Hồ Chí Minh', 'Vietnam', '700000'),
(3, '56 Lê Lợi', 'Phường 1', 'Quận 3', 'TP Hồ Chí Minh', 'Vietnam', '700000');

-- Insert into categories
INSERT INTO categories (name, description) VALUES
('Văn học', 'Sách văn học Việt Nam và thế giới'),
('Khoa học', 'Sách về các lĩnh vực khoa học và công nghệ'),
('Kinh tế', 'Sách về kinh doanh, tài chính và quản lý');

-- Insert into sub_categories
INSERT INTO sub_categories (parent_id, name, description) VALUES
(1, 'Tiểu thuyết', 'Tiểu thuyết Việt Nam và quốc tế'),
(1, 'Truyện ngắn', 'Tập hợp các truyện ngắn'),
(2, 'Khoa học tự nhiên', 'Sách về vật lý, hóa học, sinh học'),
(3, 'Quản trị kinh doanh', 'Sách về quản lý và lãnh đạo');

-- Insert into books
INSERT INTO books (title, author, cover, sub_category_id, price) VALUES
('Dế Mèn Phiêu Lưu Ký', 'Tô Hoài', 'cover1.jpg', 1, 150000),
('Nhà Giả Kim', 'Paulo Coelho', 'cover2.jpg', 1, 200000),
('Vật Lý Thiên Văn', 'Neil deGrasse Tyson', 'cover3.jpg', 3, 250000),
('Tư Duy Nhanh Và Chậm', 'Daniel Kahneman', 'cover4.jpg', 4, 300000);

-- Insert into book_detail
INSERT INTO book_detail (book_id, description, summary, isbn, publisher, publication_date, pages, file_url) VALUES
(1, 'Tác phẩm kinh điển của Tô Hoài kể về hành trình phiêu lưu của chú Dế Mèn.', 'Dế Mèn khám phá thế giới với những bài học sâu sắc.', '978-604-1-12345-6', 'NXB Kim Đồng', '1941-01-01', 150, 'books/de-men-phieu-luu-ky.pdf'),
(2, 'Tiểu thuyết nổi tiếng của Paulo Coelho về hành trình tìm kiếm giấc mơ.', 'Một chàng trai chăn cừu tìm kiếm kho báu trong giấc mơ.', '978-604-2-12345-7', 'NXB Văn Học', '1988-01-01', 200, 'books/nha-gia-kim.pdf'),
(3, 'Sách khoa học của Neil deGrasse Tyson về vũ trụ.', 'Giới thiệu các khái niệm vật lý thiên văn một cách dễ hiểu.', '978-604-3-12345-8', 'NXB Khoa Học', '2017-01-01', 300, 'books/vat-ly-thien-van.pdf'),
(4, 'Sách kinh tế về tư duy và ra quyết định.', 'Phân tích cách con người đưa ra quyết định.', '978-604-4-12345-9', 'NXB Trẻ', '2011-01-01', 500, 'books/tu-duy-nhanh-va-cham.pdf');

-- Insert into book_images
INSERT INTO book_images (book_id, image_url, alt_text) VALUES
(1, 'images/de-men-1.jpg', 'Bìa trước Dế Mèn Phiêu Lưu Ký'),
(1, 'images/de-men-2.jpg', 'Hình minh họa Dế Mèn'),
(2, 'images/nha-gia-kim-1.jpg', 'Bìa trước Nhà Giả Kim'),
(3, 'images/vat-ly-thien-van-1.jpg', 'Bìa trước Vật Lý Thiên Văn');

-- Insert into reviews
INSERT INTO reviews (user_id, book_id, rating, comment) VALUES
(1, 1, 5, 'Tác phẩm tuyệt vời, rất ý nghĩa cho cả trẻ em và người lớn!'),
(2, 2, 4, 'Câu chuyện truyền cảm hứng, nhưng hơi dài dòng ở một số đoạn.'),
(3, 3, 5, 'Rất thú vị, dễ hiểu dù là sách khoa học!'),
(1, 4, 3, 'Nội dung sâu sắc nhưng cần kiên nhẫn để đọc.');

-- Insert into wishlist
INSERT INTO wishlist (user_id, book_id) VALUES
(1, 2),
(1, 3),
(2, 1),
(3, 4);

-- Insert into cart
INSERT INTO cart (user_id, total) VALUES
(1, 350000),
(2, 150000),
(3, 0);

-- Insert into cart_item
INSERT INTO cart_item (cart_id, book_id, quantity, price) VALUES
(1, 1, 1, 150000),
(1, 2, 1, 200000),
(2, 1, 1, 150000);

-- Insert into roles
INSERT INTO roles (name, description) VALUES
('admin', 'Quản trị viên hệ thống'),
('user', 'Người dùng thông thường');

-- Insert into user_roles
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1), -- User 1 is admin
(1, 2), -- User 1 is also a user
(2, 2), -- User 2 is a user
(3, 2); -- User 3 is a user