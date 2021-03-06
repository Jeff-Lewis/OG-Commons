/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.collect.named;

/**
 * Mock named object.
 */
public class MockNameds implements MockNamed {

  public static final MockNameds STANDARD = new MockNameds();

  @Override
  public String getName() {
    return "Standard";
  }

}
