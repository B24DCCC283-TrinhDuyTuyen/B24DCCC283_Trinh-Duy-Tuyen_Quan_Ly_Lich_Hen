# Ứng dụng Quản lý lịch hẹn

Ứng dụng Android viết bằng **Java + XML**, lưu trữ dữ liệu bằng **SQLiteOpenHelper**.

## Chức năng
- Đăng nhập bằng tài khoản lưu trong SQLite.
- Danh sách lịch hẹn, sắp xếp tăng dần theo ngày và giờ.
- Thêm lịch hẹn, chọn ngày/giờ, bật/tắt trạng thái nhắc nhở.
- Xem chi tiết, cập nhật nhắc nhở và xóa lịch hẹn.
- Thống kê tổng số lịch hẹn và số lịch đang bật nhắc nhở.
- Dữ liệu tồn tại sau khi đóng và mở lại ứng dụng.

## Công nghệ
- Java
- Android XML layouts
- SQLite thông qua `SQLiteOpenHelper`
- Intent để chuyển Activity và truyền appointment ID

## Mở dự án
1. Giải nén repository.
2. Mở thư mục `QuanLyLichHen` bằng Android Studio.
3. Chờ Gradle Sync hoàn tất.
4. Chọn emulator hoặc điện thoại Android đã bật USB debugging.
5. Nhấn Run.

Yêu cầu môi trường đề xuất: Android Studio bản mới, JDK 17, Android SDK 35.

## Tài khoản mẫu
- Username: `admin`
- Password: `123456`

Tài khoản được chèn vào bảng `users` trong `DatabaseHelper.onCreate()`, không hard-code trong Activity.

## Cấu trúc
```text
app/src/main/
├── AndroidManifest.xml
├── java/com/example/appointmentmanager/
│   ├── data/DatabaseHelper.java
│   ├── model/Appointment.java
│   ├── ui/LoginActivity.java
│   ├── ui/MainActivity.java
│   ├── ui/AddAppointmentActivity.java
│   ├── ui/AppointmentDetailActivity.java
│   └── ui/AppointmentAdapter.java
└── res/
    ├── layout/
    ├── drawable/
    └── values/
```

## Thiết kế cơ sở dữ liệu
Database: `appointments.db`, version `1`.

### users
| Cột | Kiểu | Ràng buộc |
|---|---|---|
| id | INTEGER | PRIMARY KEY AUTOINCREMENT |
| username | TEXT | NOT NULL UNIQUE |
| password | TEXT | NOT NULL |

### appointments
| Cột | Kiểu | Ràng buộc |
|---|---|---|
| id | INTEGER | PRIMARY KEY AUTOINCREMENT |
| title | TEXT | NOT NULL |
| location | TEXT | NOT NULL |
| date | TEXT | NOT NULL, yyyy-MM-dd |
| time | TEXT | NOT NULL, HH:mm |
| reminder | INTEGER | NOT NULL, 1 hoặc 0 |

## Ghi chú
- Theo yêu cầu bài thực hành, mật khẩu mẫu được lưu dạng văn bản thuần.
- Trường `reminder` chỉ lưu trạng thái bật/tắt; ứng dụng chưa lên lịch thông báo hệ thống.
- Danh sách `ArrayList` chỉ được dùng làm dữ liệu truy vấn để hiển thị, SQLite là nơi lưu trữ chính.


## Khắc phục lỗi Duplicate class Kotlin
Trong `app/build.gradle`, dự án đồng bộ các dependency thuộc `org.jetbrains.kotlin`
về phiên bản `1.8.22` bằng `resolutionStrategy`. Nếu Android Studio vẫn giữ cache cũ,
chọn **File > Sync Project with Gradle Files**, sau đó **Build > Clean Project** và
**Build > Rebuild Project**.
