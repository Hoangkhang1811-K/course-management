DROP DATABASE IF EXISTS course_management;
CREATE DATABASE course_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE course_management;

-- =========================
-- 1. TABLE: roles
-- =========================
CREATE TABLE roles (
                       role_id INT AUTO_INCREMENT PRIMARY KEY,
                       role_name VARCHAR(50) NOT NULL UNIQUE
);

-- =========================
-- 2. TABLE: users
-- =========================
CREATE TABLE users (
                       user_id INT AUTO_INCREMENT PRIMARY KEY,
                       role_id INT NOT NULL,
                       full_name VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       phone VARCHAR(20),
                       status TINYINT NOT NULL DEFAULT 1 COMMENT '1=active, 0=inactive',
                       created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       CONSTRAINT fk_users_role
                           FOREIGN KEY (role_id) REFERENCES roles(role_id)
                               ON UPDATE CASCADE
                               ON DELETE RESTRICT
);

-- =========================
-- 3. TABLE: categories
-- =========================
CREATE TABLE categories (
                            category_id INT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(100) NOT NULL UNIQUE,
                            description VARCHAR(255),
                            status TINYINT NOT NULL DEFAULT 1 COMMENT '1=active, 0=inactive'
);

-- =========================
-- 4. TABLE: courses
-- =========================
CREATE TABLE courses (
                         course_id INT AUTO_INCREMENT PRIMARY KEY,
                         category_id INT NOT NULL,
                         created_by INT NOT NULL,
                         title VARCHAR(150) NOT NULL,
                         short_description VARCHAR(255),
                         description TEXT,
                         thumbnail_url VARCHAR(255),
                         level VARCHAR(50) DEFAULT 'Beginner',
                         price DECIMAL(10,2) NOT NULL DEFAULT 0.00,
                         status TINYINT NOT NULL DEFAULT 1 COMMENT '1=active, 0=inactive',
                         created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         CONSTRAINT fk_courses_category
                             FOREIGN KEY (category_id) REFERENCES categories(category_id)
                                 ON UPDATE CASCADE
                                 ON DELETE RESTRICT,
                         CONSTRAINT fk_courses_created_by
                             FOREIGN KEY (created_by) REFERENCES users(user_id)
                                 ON UPDATE CASCADE
                                 ON DELETE RESTRICT
);

-- =========================
-- 5. TABLE: lessons
-- =========================
CREATE TABLE lessons (
                         lesson_id INT AUTO_INCREMENT PRIMARY KEY,
                         course_id INT NOT NULL,
                         title VARCHAR(150) NOT NULL,
                         lesson_order INT NOT NULL,
                         content_type VARCHAR(20) NOT NULL DEFAULT 'TEXT' COMMENT 'TEXT or VIDEO',
                         content_value TEXT,
                         status TINYINT NOT NULL DEFAULT 1 COMMENT '1=active, 0=inactive',
                         CONSTRAINT fk_lessons_course
                             FOREIGN KEY (course_id) REFERENCES courses(course_id)
                                 ON UPDATE CASCADE
                                 ON DELETE CASCADE,
                         CONSTRAINT uq_lessons_course_order UNIQUE (course_id, lesson_order)
);

-- =========================
-- 6. TABLE: enrollments
-- =========================
CREATE TABLE enrollments (
                             enrollment_id INT AUTO_INCREMENT PRIMARY KEY,
                             user_id INT NOT NULL,
                             course_id INT NOT NULL,
                             enrolled_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             status VARCHAR(20) NOT NULL DEFAULT 'ENROLLED' COMMENT 'ENROLLED, CANCELLED, COMPLETED',
                             CONSTRAINT fk_enrollments_user
                                 FOREIGN KEY (user_id) REFERENCES users(user_id)
                                     ON UPDATE CASCADE
                                     ON DELETE CASCADE,
                             CONSTRAINT fk_enrollments_course
                                 FOREIGN KEY (course_id) REFERENCES courses(course_id)
                                     ON UPDATE CASCADE
                                     ON DELETE CASCADE,
                             CONSTRAINT uq_enrollments_user_course UNIQUE (user_id, course_id)
);

-- =========================
-- INDEX
-- =========================
CREATE INDEX idx_users_role_id ON users(role_id);
CREATE INDEX idx_courses_category_id ON courses(category_id);
CREATE INDEX idx_courses_created_by ON courses(created_by);
CREATE INDEX idx_lessons_course_id ON lessons(course_id);
CREATE INDEX idx_enrollments_user_id ON enrollments(user_id);
CREATE INDEX idx_enrollments_course_id ON enrollments(course_id);

-- =========================
-- SEED DATA
-- =========================

-- roles
INSERT INTO roles(role_name) VALUES
                                 ('ADMIN'),
                                 ('STUDENT');

-- users
-- password_hash hiện tại chỉ là text demo, sau này Java có thể hash thật
INSERT INTO users(role_id, full_name, email, password_hash, phone, status) VALUES
                                                                               (1, 'Hoang Khang', 'admin@gmail.com', '123456', '0901111111', 1),
                                                                               (2, 'Nguyen Van A', 'student1@gmail.com', '123456', '0902222222', 1),
                                                                               (2, 'Tran Thi B', 'student2@gmail.com', '123456', '0903333333', 1);

-- categories
INSERT INTO categories(name, description, status) VALUES
                                                      ('Java Web', 'Khoa hoc ve JSP Servlet JDBC', 1),
                                                      ('Frontend', 'HTML CSS JavaScript Bootstrap', 1),
                                                      ('Database', 'MySQL co ban den nang cao', 1);

-- courses
INSERT INTO courses(category_id, created_by, title, short_description, description, thumbnail_url, level, price, status) VALUES
                                                                                                                             (1, 1, 'Java Web Co Ban', 'Hoc JSP Servlet JDBC MVC', 'Khoa hoc Java Web danh cho nguoi moi bat dau.', 'images/javaweb.jpg', 'Beginner', 0.00, 1),
                                                                                                                             (2, 1, 'HTML CSS JS Co Ban', 'Hoc frontend nen tang', 'Khoa hoc HTML CSS JavaScript Bootstrap 5.', 'images/frontend.jpg', 'Beginner', 0.00, 1),
                                                                                                                             (3, 1, 'MySQL Co Ban', 'Hoc thiet ke va truy van CSDL', 'Khoa hoc MySQL tu co ban den thuc hanh.', 'images/mysql.jpg', 'Beginner', 0.00, 1);

-- lessons
INSERT INTO lessons(course_id, title, lesson_order, content_type, content_value, status) VALUES
                                                                                             (1, 'Gioi thieu JSP Servlet', 1, 'TEXT', 'Noi dung bai hoc 1', 1),
                                                                                             (1, 'Mo hinh MVC', 2, 'TEXT', 'Noi dung bai hoc 2', 1),
                                                                                             (1, 'JDBC ket noi MySQL', 3, 'VIDEO', 'https://youtube.com/example1', 1),

                                                                                             (2, 'HTML co ban', 1, 'TEXT', 'Noi dung HTML', 1),
                                                                                             (2, 'CSS co ban', 2, 'TEXT', 'Noi dung CSS', 1),
                                                                                             (2, 'JavaScript co ban', 3, 'VIDEO', 'https://youtube.com/example2', 1),

                                                                                             (3, 'Lenh SELECT', 1, 'TEXT', 'Noi dung SELECT', 1),
                                                                                             (3, 'Lenh JOIN', 2, 'TEXT', 'Noi dung JOIN', 1),
                                                                                             (3, 'Lenh GROUP BY', 3, 'VIDEO', 'https://youtube.com/example3', 1);

-- enrollments
INSERT INTO enrollments(user_id, course_id, status) VALUES
                                                        (2, 1, 'ENROLLED'),
                                                        (2, 2, 'ENROLLED'),
                                                        (3, 1, 'ENROLLED');