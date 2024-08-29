package org.gzhuoj.common.sdk.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Options<T, K> {
    private List<Option<T, K>> options;
    //
    public void add(Option<T, K> option){
        if(ObjectUtils.isEmpty(options)){
            this.options = new ArrayList<>();
        }
        this.options.add(option);
    }

    public Options (List<Option<T, K>> options){
        this.options = new ArrayList<>();
        this.options.addAll(options);
    }
}