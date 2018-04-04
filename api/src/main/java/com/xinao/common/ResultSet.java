/*
 * Created by houyefeng on 2016-10-18.
 */

package com.xinao.common;

import java.io.Serializable;
import java.util.List;

/**
 * Service返回数据给API以及API返回JSON时使用.
 * {@code ResultSet}和{@code Result}的最大区别是前者返回的数据是数据集且可以包含数据总数，后者不能包含数据总数，在不需要总数时
 * 使用比较方便。
 * <p>
 * D是返回数据的类型；
 * C是状态码类型，控制层可以根据此状态码做适当的流程控制，可以是任何类型的对象，建议使用枚举类型。
 * </p>
 *
 * @author houyefeng
 * @version 0.0.1
 * @see Result
 * @since 0.0.1 2016-10-18
 */
public class ResultSet<D, C> extends Result<List<D>, C> implements Serializable {
  private static final long serialVersionUID = 2825631400630299082L;
  private int total = 0;

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

}
