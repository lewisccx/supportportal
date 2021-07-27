package com.supportportal.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PageObject<T> implements Serializable {

    List<T> payload;
    int currentPage;
    int totalItems;
    int totalPage;


}
