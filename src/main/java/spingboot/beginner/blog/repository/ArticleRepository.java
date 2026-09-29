package spingboot.beginner.blog.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import spingboot.beginner.blog.model.Article;

@Repository // không có thao tác nào cần implement riêng.
/*
Mặc dù là interface nhưng khi gọi hay dùng nó thì đã có 1 class ẩn implement các phương thức rồi.

Các thao tác CrudRepo thường trả về Optional, Optional là 1 class đặc biệt, khi trả về
1 object Optional nó có thể rỗng hoặc chứa data (object cần tìm thật sự) bên trong -> tránh được lỗi null-safety.

Ngoài ra còn trả về một interface Iterable chỉ có chức năng duyệt từng phần tử không có hàm thêm, xóa, sửa, nhưng vẫn cập
nhật dữ liệu bên trong từng phần tử được.
-> Không cho thay đổi cấu trúc lưu trữ + Tăng tính trừu tượng hóa có thể làm việc với nhiều DB lưu trữ khác nhau.
*/
public interface ArticleRepository extends CrudRepository<Article, String> {

}
