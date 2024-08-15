package icu.yeguo.chat.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
@Data
@AllArgsConstructor // 为所有字段生成构造函数
public class PageResponse<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 8918627189428409313L;
    private long totalPages;
    private long currentPage;
    private long totalRecords;
    private long size;
    private List<T> records;
}
