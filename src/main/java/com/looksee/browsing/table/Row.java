package com.looksee.browsing.table;

import com.looksee.browsing.ElementNode;
import com.looksee.models.Element;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *	Contains the {@link ElementNode}s that make up a row in a {@link Table}
 */
@Getter
@Setter
@NoArgsConstructor
public class Row {
	private List<ElementNode<Element>> rowCells;
	
	/**
	 * Constructs a {@link Row}
	 * 
	 * @param table_row the list of {@link ElementNode}s that make up the row
	 */
	public Row(List<ElementNode<Element>> table_row){
		this.setRowCells(table_row);
	}
}
