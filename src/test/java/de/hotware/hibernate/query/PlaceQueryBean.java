/*  
 * This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.*
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *   
 *   (C) Martin Braun 2014
 */
package de.hotware.hibernate.query;

import de.hotware.hibernate.query.intelligent.annotations.Junction;
import de.hotware.hibernate.query.stringbridge.ToStringStringBridge;

public class PlaceQueryBean extends BaseQueryBean<Place> {

	private String name;

	@SearchField(fieldNames = { "name", "sorcerers.name" }, betweenFields = Junction.SHOULD, 
			queryType = StockQueryTypes.Term.class, stringBridge = ToStringStringBridge.class)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
