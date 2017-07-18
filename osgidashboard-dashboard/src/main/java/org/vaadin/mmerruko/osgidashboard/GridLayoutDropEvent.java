/*
 * Copyright 2000-2016 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.vaadin.mmerruko.osgidashboard;

import java.util.Map;

import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.ui.dnd.DropEffect;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.dnd.DragSourceExtension;
import com.vaadin.ui.dnd.event.DropEvent;

public class GridLayoutDropEvent extends DropEvent<GridLayout> {

    private int column;
    private int row;

    public GridLayoutDropEvent(GridLayout target, Map<String, String> data,
            DropEffect dropEffect,
            DragSourceExtension<? extends AbstractComponent> dragSourceExtension,
            int column, int row, MouseEventDetails mouseEventDetails) {
        super(target, data, dropEffect, dragSourceExtension, mouseEventDetails);

        this.column = column;
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }
}
