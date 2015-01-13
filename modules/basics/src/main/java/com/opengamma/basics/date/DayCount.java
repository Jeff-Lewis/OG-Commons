/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.basics.date;

import java.time.LocalDate;

import org.joda.convert.FromString;
import org.joda.convert.ToString;

import com.opengamma.basics.schedule.Frequency;
import com.opengamma.collect.ArgChecker;
import com.opengamma.collect.named.ExtendedEnum;
import com.opengamma.collect.named.Named;

/**
 * A convention defining how to calculate fractions of a year.
 * <p>
 * The purpose of this convention is to define how to convert dates into numeric year fractions.
 * The is of use when calculating accrued interest over time.
 * <p>
 * The most common implementations are provided in {@link DayCounts}.
 * Additional implementations may be added by implementing this interface.
 * <p>
 * All implementations of this interface must be immutable and thread-safe.
 */
public interface DayCount
    extends Named {

  /**
   * Obtains a {@code DayCount} from a unique name.
   * 
   * @param uniqueName  the unique name
   * @return the day count
   * @throws IllegalArgumentException if the name is not known
   */
  @FromString
  public static DayCount of(String uniqueName) {
    ArgChecker.notNull(uniqueName, "uniqueName");
    return extendedEnum().lookup(uniqueName);
  }

  /**
   * Gets the extended enum helper.
   * <p>
   * This helper allows instances of {@code DayCount} to be lookup up.
   * It also provides the complete set of available instances.
   * 
   * @return the extended enum helper
   */
  public static ExtendedEnum<DayCount> extendedEnum() {
    return DayCounts.ENUM_LOOKUP;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the year fraction between the specified dates.
   * <p>
   * Given two dates, this method returns the fraction of a year between these
   * dates according to the convention.
   * <p>
   * This uses a simple {@link ScheduleInfo} which has the end-of-month convention
   * set to false, but throws an exception for other methods.
   * Certain implementations of {@code DayCount} need the missing information,
   * and thus will throw an exception.
   * 
   * @param firstDate  the first date
   * @param secondDate  the second date, on or after the first date
   * @return the year fraction
   * @throws UnsupportedOperationException if the year fraction cannot be obtained
   */
  public default double yearFraction(LocalDate firstDate, LocalDate secondDate) {
    return yearFraction(firstDate, secondDate, DayCounts.SIMPLE_SCHEDULE_INFO);
  }

  /**
   * Gets the year fraction between the specified dates.
   * <p>
   * Given two dates, this method returns the fraction of a year between these
   * dates according to the convention.
   * 
   * @param firstDate  the first date
   * @param secondDate  the second date, on or after the first date
   * @param scheduleInfo  the schedule information
   * @return the year fraction
   * @throws UnsupportedOperationException if the year fraction cannot be obtained
   */
  public double yearFraction(LocalDate firstDate, LocalDate secondDate, ScheduleInfo scheduleInfo);

  /**
   * Gets the name that uniquely identifies this convention.
   * <p>
   * This name is used in serialization and can be parsed using {@link #of(String)}.
   * 
   * @return the unique name
   */
  @ToString
  @Override
  public String getName();

  //-------------------------------------------------------------------------
  /**
   * Information about the schedule necessary to calculate the day count.
   * <p>
   * Some {@link DayCount} implementations require additional information about the schedule.
   * Implementations of this interface provide that information.
   */
  public interface ScheduleInfo {

    /**
     * Gets the start date of the schedule.
     * <p>
     * The first date in the schedule.
     * If the schedule adjusts for business days, then this is the adjusted date.
     * <p>
     * This throws an exception by default.
     * 
     * @return the schedule start date
     * @throws UnsupportedOperationException if the date cannot be obtained
     */
    public default LocalDate getStartDate() {
      throw new UnsupportedOperationException("The start date of the schedule is required");
    }

    /**
     * Gets the end date of the schedule.
     * <p>
     * The last date in the schedule.
     * If the schedule adjusts for business days, then this is the adjusted date.
     * <p>
     * This throws an exception by default.
     * 
     * @return the schedule end date
     * @throws UnsupportedOperationException if the date cannot be obtained
     */
    public default LocalDate getEndDate() {
      throw new UnsupportedOperationException("The end date of the schedule is required");
    }

    /**
     * Gets the end date of the schedule period.
     * <p>
     * This is called when a day count requires the end date of the schedule period.
     * <p>
     * This throws an exception by default.
     * 
     * @param date  the date to find the period end date for
     * @return the period end date
     * @throws UnsupportedOperationException if the date cannot be obtained
     */
    public default LocalDate getPeriodEndDate(LocalDate date) {
      throw new UnsupportedOperationException("The end date of the schedule period is required");
    }

    /**
     * Gets the periodic frequency of the schedule period.
     * <p>
     * This is called when a day count requires the periodic frequency of the schedule.
     * <p>
     * This throws an exception by default.
     * 
     * @return the periodic frequency
     * @throws UnsupportedOperationException if the frequency cannot be obtained
     */
    public default Frequency getFrequency() {
      throw new UnsupportedOperationException("The frequency of the schedule is required");
    }

    /**
     * Checks if the end of month convention is in use.
     * <p>
     * This is called when a day count needs to know whether the end-of-month convention is in use.
     * <p>
     * This is true by default.
     * 
     * @return true if the end of month convention is in use
     */
    public default boolean isEndOfMonthConvention() {
      return true;
    }
  }

}