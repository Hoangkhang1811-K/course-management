# Course Management

Website quản lý khóa học được xây dựng bằng Java Servlet/JSP theo mô hình MVC. Hệ thống có 2 nhóm người dùng chính:

- Admin: quản lý khóa học, danh mục, bài học, học viên và lượt ghi danh.
- Student: xem danh sách khóa học, xem chi tiết khóa học, đăng ký khóa học và xem khóa học đã đăng ký.

## Công nghệ sử dụng

- Java 8
- Gradle
- Servlet API 4.0.1
- JSP/JSTL 1.2
- JDBC
- MySQL 8
- MySQL Connector/J 8.3.0
- HTML, CSS
- Apache Tomcat 9 hoặc server servlet tương thích Servlet 4.0
- JUnit 5.10.2 đã được khai báo trong Gradle để phục vụ kiểm thử

## Kiến trúc dự án

```text
src/main/java
├── controller  # Servlet xử lý request/response
├── dao         # Truy vấn dữ liệu bằng JDBC
├── model       # Entity/model của hệ thống
├── service     # Xử lý nghiệp vụ
└── util        # Kết nối DB, session, validate, password, datetime

src/main/webapp
├── assets/css  # Giao diện
├── views       # JSP theo từng nhóm màn hình
└── WEB-INF     # Cấu hình web.xml

src/database    # Script tạo database và dữ liệu mẫu
```

## Chức năng chính

### Người dùng chung

- Đăng ký tài khoản học viên.
- Đăng nhập, đăng xuất.
- Phân quyền theo role Admin và Student.
- Hiển thị thông báo thành công/lỗi bằng session flash message.
- Trang lỗi tùy chỉnh cho 403, 404 và 500.

### Student

- Xem trang chủ với thống kê và danh sách khóa học nổi bật.
- Xem danh sách khóa học.
- Tìm kiếm/lọc khóa học theo danh mục hoặc từ khóa.
- Xem chi tiết khóa học, danh sách bài học và khóa học liên quan.
- Đăng ký khóa học.
- Xem danh sách khóa học đã đăng ký tại `/my-courses`.

### Admin

- Xem dashboard quản trị.
- Quản lý danh mục khóa học: thêm, sửa, xóa, bật/tắt trạng thái.
- Quản lý khóa học: thêm, sửa, xóa, cập nhật thông tin, giá, level, ảnh đại diện và trạng thái.
- Quản lý bài học theo khóa học: thêm, sửa, xóa, sắp xếp thứ tự, hỗ trợ nội dung TEXT/VIDEO.
- Quản lý học viên/người dùng: thêm, sửa, xóa, khóa/mở khóa tài khoản, lọc theo role/trạng thái/từ khóa.
- Quản lý ghi danh: thêm, hủy, hoàn thành hoặc cập nhật trạng thái ghi danh của học viên.

## Cài đặt database

1. Tạo database và dữ liệu mẫu bằng file:

```text
src/database/course_management.sql
```

2. Có thể import bằng MySQL Workbench hoặc chạy lệnh:

```bash
mysql -u root -p < src/database/course_management.sql
```

3. Kiểm tra cấu hình kết nối trong file `src/main/java/util/DBConnection.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/course_management?useSSL=false&serverTimezone=UTC";
private static final String USER = "root";
private static final String PASSWORD = "abc123";
```

Nếu máy đang dùng mật khẩu MySQL khác, hãy đổi giá trị `PASSWORD` cho phù hợp.

## Tài khoản mẫu

Sau khi import database mẫu, có thể đăng nhập bằng các tài khoản sau:

| Vai trò | Email | Mật khẩu |
| --- | --- | --- |
| Admin | `admin@gmail.com` | `123456` |
| Student | `student1@gmail.com` | `123456` |
| Student | `student2@gmail.com` | `123456` |

## Cách build và chạy web

### Build file WAR

Trên Windows:

```bash
gradlew.bat clean war
```

Trên macOS/Linux:

```bash
./gradlew clean war
```

File WAR sau khi build nằm tại:

```text
build/libs/course-management-1.0-SNAPSHOT.war
```

### Deploy bằng Tomcat

1. Cài Apache Tomcat 9.
2. Copy file `build/libs/course-management-1.0-SNAPSHOT.war` vào thư mục `webapps` của Tomcat.
3. Khởi động Tomcat.
4. Mở trình duyệt:

```text
http://localhost:8080/course-management-1.0-SNAPSHOT/home
```

Nếu cấu hình context path khác trong IDE hoặc Tomcat, thay `course-management-1.0-SNAPSHOT` bằng context path tương ứng.

### Chạy bằng IntelliJ IDEA

1. Mở project bằng IntelliJ IDEA.
2. Đảm bảo Gradle đã sync thành công.
3. Tạo cấu hình Tomcat Local.
4. Thêm artifact `course-management:war exploded`.
5. Chạy Tomcat và truy cập `/home` hoặc `/login`.

## Hướng dẫn sử dụng

### Với Admin

1. Truy cập `/login`.
2. Đăng nhập bằng tài khoản `admin@gmail.com` / `123456`.
3. Sau khi đăng nhập, hệ thống chuyển đến `/admin/dashboard`.
4. Dùng thanh điều hướng để quản lý:
   - Dashboard
   - Categories
   - Courses
   - Lessons
   - Enrollments
   - Users
5. Khi thêm/sửa dữ liệu, nhập đủ thông tin bắt buộc và nhấn lưu.
6. Khi không có quyền truy cập, hệ thống chuyển đến trang lỗi 403.

### Với Student

1. Truy cập `/register` để tạo tài khoản mới hoặc `/login` để đăng nhập.
2. Sau khi đăng nhập, hệ thống chuyển về trang chủ.
3. Vào `/courses` để xem danh sách khóa học.
4. Nhấn vào một khóa học để xem chi tiết.
5. Nhấn đăng ký để ghi danh khóa học.
6. Vào `/my-courses` để xem các khóa học đã đăng ký.

## Các đường dẫn chính

| Đường dẫn | Mô tả |
| --- | --- |
| `/home` hoặc `/views/home` | Trang chủ |
| `/login` | Đăng nhập |
| `/register` | Đăng ký |
| `/logout` | Đăng xuất |
| `/courses` hoặc `/course-list` | Danh sách khóa học |
| `/course-detail?id=...` | Chi tiết khóa học |
| `/enroll` | Đăng ký khóa học |
| `/my-courses` | Khóa học của tôi |
| `/admin/dashboard` | Dashboard admin |
| `/admin/categories` | Quản lý danh mục |
| `/admin/courses` | Quản lý khóa học |
| `/admin/lessons` | Quản lý bài học |
| `/admin/enrollments` | Quản lý ghi danh |
| `/admin/users` | Quản lý người dùng |

## Phân công công việc của 2 thành viên

Dựa trên lịch sử commit và các chức năng hiện có trong dự án:

| Thành viên | Công việc đã thực hiện |
| --- | --- |
| Hoàng Khang (`Khangne`, `Hoangkhang1811-K`) | Khởi tạo project và database; xây dựng nhiều servlet quản trị; làm dashboard admin; xử lý đăng nhập/đăng xuất; tạo session utility; tách validate utility; thêm hash mật khẩu PBKDF2; phát triển giao diện/course-list; cập nhật CSS; làm quản lý khóa học, bài học, ghi danh, người dùng; chỉnh navbar; xử lý hiển thị hình ảnh; thêm trang lỗi 403/404/500. |
| Võ Thiện Khang (`Vothienkhang`, `khangvo95`) | Xây dựng model, DAO và kết nối database giai đoạn đầu; cập nhật UserDAO và Enrollment model; làm chức năng login ban đầu; phát triển CourseService, CategoryService, EnrollmentService, LessonService; cập nhật CategoryServlet; làm DateTimeUtil; xây dựng và cải thiện dashboard, category-list, category-form, common.css; cập nhật cấu hình Gradle và .gitignore. |

## Ghi chú

- Database mẫu đang có mật khẩu dạng text để dễ demo. Code hiện tại có `PasswordUtil` hỗ trợ kiểm tra cả mật khẩu cũ dạng text và mật khẩu mới đã hash bằng PBKDF2.
- Khi tạo tài khoản mới hoặc tạo người dùng mới từ admin, mật khẩu sẽ được hash trước khi lưu.
- Nếu ảnh khóa học không hiển thị, kiểm tra lại đường dẫn ảnh trong trường `thumbnail_url` hoặc thư mục upload/static tương ứng.
