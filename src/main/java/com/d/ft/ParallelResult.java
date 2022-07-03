package com.d.ft;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ParallelResult<T> {
    private ParallelStatus status;
    private T value;

    public static <T> ParallelResult<T> success(T v) {
        ParallelResult<T> result = new ParallelResult<>();
        result.setStatus(ParallelStatus.success);
        result.setValue(v);
        return result;
    }
}
