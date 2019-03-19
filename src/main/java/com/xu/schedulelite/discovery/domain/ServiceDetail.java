package com.xu.schedulelite.discovery.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author CharleyXu Created on 2019/3/19.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDetail {

  private String desc;
  private int weight;

}
