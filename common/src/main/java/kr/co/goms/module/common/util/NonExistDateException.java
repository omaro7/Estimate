package kr.co.goms.module.common.util;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003.08.20</p>
 * <p>Company: duzon</p>
 * @author bjkim@duzon.co.kr
 * @version 1.0
 */

import java.text.ParseException;

public class NonExistDateException extends ParseException {

  public NonExistDateException(String s) {
    this(s, -1);
  }

  public NonExistDateException(String s,int errOffset) {
    super(s, errOffset);
  }
}
