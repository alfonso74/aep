/*
 *  Spinner.java  - A spinner component
 *  Mark Bryan Yu
 *  swtcalendar.sourceforge.net
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package rcp.manticora.calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TypedListener;

public class Spinner extends Composite {
    static final int BUTTON_WIDTH = 16;
    private Label text;
    private Button up, down;
    private int minimum, maximum;
    private Font font;

    public Spinner(Composite parent, int style) {
        super(parent, style);
        text = new Label(this, style | SWT.BORDER);
        up = new Button(this, style | SWT.ARROW | SWT.UP);
        down = new Button(this, style | SWT.ARROW | SWT.DOWN);

        up.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
                up();
            }
        });

        down.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
                down();
            }
        });

        addListener(SWT.Resize, new Listener() {
            public void handleEvent(Event e) {
                resize();
            }
        });

        Color oldBackground = getDisplay().getSystemColor(SWT.COLOR_WHITE);
        font = new Font(getDisplay(), text.getFont().getFontData()[0].getName(),
                            text.getFont().getFontData()[0].getHeight()+2, text.getFont().getFontData()[0].getStyle());
        text.setFont(font);
        text.setBackground(oldBackground);

        minimum = 0;
        maximum = 9;
        setSelection(minimum);

        addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent event) {
                font.dispose();
            }
        });
    }

    void up() {
        setSelection(getSelection() + 1);
        notifyListeners(SWT.Selection, new Event());
    }

    void down() {
        setSelection(getSelection() - 1);
        notifyListeners(SWT.Selection, new Event());
    }

    public void setSelection(int selection) {
        if (selection < minimum) {
            selection = minimum;
        } else if (selection > maximum) {
            selection = maximum;
        }

        text.setText(String.valueOf(selection));
    }


    public int getSelection() {
        return Integer.parseInt(text.getText());
    }


    public void setMaximum(int maximum) {
        checkWidget();
        this.maximum = maximum;
        resize();
    }


    public int getMaximum() {
        return maximum;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }

    public int getMinimum() {
        return minimum;
    }

    void resize() {
        Point pt = computeSize(SWT.DEFAULT, SWT.DEFAULT);
        int textWidth = pt.x - BUTTON_WIDTH;
        int buttonHeight = pt.y / 2;
        text.setBounds(0, 0, textWidth, pt.y);
        up.setBounds(textWidth, 0, BUTTON_WIDTH, buttonHeight);
        down.setBounds(textWidth, pt.y - buttonHeight, BUTTON_WIDTH, buttonHeight);
    }


    public Point computeSize(int wHint, int hHint, boolean changed) {
        GC gc = new GC(text);
        Point textExtent = gc.textExtent(String.valueOf(maximum));
        gc.dispose();
        Point pt = text.computeSize(textExtent.x, textExtent.y);
        int width = pt.x + BUTTON_WIDTH + 10;
        int height = pt.y;
        if (wHint != SWT.DEFAULT) width = wHint;
        if (hHint != SWT.DEFAULT) height = hHint;
        return new Point(width, height);
    }

    public void addSelectionListener(SelectionListener listener) {
        if (listener == null) throw new SWTError(SWT.ERROR_NULL_ARGUMENT);
        addListener(SWT.Selection, new TypedListener(listener));
    }
}