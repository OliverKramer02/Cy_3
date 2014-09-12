package org.apache.commons.math3.exception.util;

import java.io.Serializable;
import java.util.Locale;

public abstract interface Localizable
  extends Serializable
{
  public abstract String getSourceString();
  
  public abstract String getLocalizedString(Locale paramLocale);
}


/* Location:           C:\Users\Olli\Desktop\NetworkPrioritizer-1.01.jar
 * Qualified Name:     org.apache.commons.math3.exception.util.Localizable
 * JD-Core Version:    0.7.0.1
 */