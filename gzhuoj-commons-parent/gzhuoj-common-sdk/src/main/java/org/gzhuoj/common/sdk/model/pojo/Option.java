package org.gzhuoj.common.sdk.model.pojo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Option<T, K> {
    private T key;
    private K value;
}
