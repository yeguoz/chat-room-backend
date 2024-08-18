package icu.yeguo.chat.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor // 为所有字段生成构造函数
public class CursorResponse<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = -9191962396766181716L;
    private Long roomId;
    private Long pageSize;
    private Long cursorId;
    private List<T> records;
}
