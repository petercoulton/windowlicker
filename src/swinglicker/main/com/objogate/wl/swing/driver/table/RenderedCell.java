/**
 * Contains the contents of a cell within a table and its coordinates
 */
package com.objogate.wl.swing.driver.table;

import java.awt.Component;


public class RenderedCell {
  public final Cell cell;
  public final Component rendered;

  public RenderedCell(Cell cell, Component rendered) {
    this.cell = cell;
    this.rendered = rendered;
  }
}