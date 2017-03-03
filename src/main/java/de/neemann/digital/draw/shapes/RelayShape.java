package de.neemann.digital.draw.shapes;

import de.neemann.digital.core.Observer;
import de.neemann.digital.core.element.ElementAttributes;
import de.neemann.digital.core.element.Keys;
import de.neemann.digital.core.element.PinDescriptions;
import de.neemann.digital.core.switching.Relay;
import de.neemann.digital.draw.elements.IOState;
import de.neemann.digital.draw.elements.Pin;
import de.neemann.digital.draw.elements.Pins;
import de.neemann.digital.draw.graphics.*;

import static de.neemann.digital.draw.shapes.GenericShape.SIZE;
import static de.neemann.digital.draw.shapes.GenericShape.SIZE2;

/**
 * The relay shape
 */
public class RelayShape implements Shape {

    private final PinDescriptions inputs;
    private final PinDescriptions outputs;
    private final String label;
    private boolean invers;
    private Relay relay;

    /**
     * Creates a new instance
     *
     * @param attributes the attributes
     * @param inputs     the inputs
     * @param outputs    the outputs
     */
    public RelayShape(ElementAttributes attributes, PinDescriptions inputs, PinDescriptions outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
        invers = attributes.get(Keys.RELAY_NORMALLY_CLOSED);
        label = attributes.getCleanLabel();
    }

    @Override
    public Pins getPins() {
        return new Pins()
                .add(new Pin(new Vector(0, -SIZE * 2), inputs.get(0)))
                .add(new Pin(new Vector(SIZE * 2, -SIZE * 2), inputs.get(1)))
                .add(new Pin(new Vector(0, 0), outputs.get(0)))
                .add(new Pin(new Vector(SIZE * 2, 0), outputs.get(1)));
    }

    @Override
    public InteractorInterface applyStateMonitor(IOState ioState, Observer guiObserver) {
        relay = (Relay) ioState.getElement();
        ioState.getInput(0).addObserverToValue(guiObserver);
        ioState.getInput(1).addObserverToValue(guiObserver);
        return null;
    }

    @Override
    public void drawTo(Graphic graphic, boolean highLight) {
        int yOffs = 0;

        boolean closed = invers;
        if (relay != null)
            closed = relay.isClosed();

        if (closed) {
            graphic.drawLine(new Vector(0, 0), new Vector(SIZE * 2, 0), Style.NORMAL);
        } else {
            yOffs = SIZE2 / 2;
            graphic.drawLine(new Vector(0, 0), new Vector(SIZE * 2 - 4, -yOffs * 2), Style.NORMAL);
        }
        graphic.drawLine(new Vector(SIZE, -yOffs), new Vector(SIZE, 1 - SIZE), Style.THIN);

        graphic.drawPolygon(new Polygon(true)
                .add(SIZE2, -SIZE)
                .add(SIZE2, -SIZE * 3)
                .add(SIZE + SIZE2, -SIZE * 3)
                .add(SIZE + SIZE2, -SIZE), Style.NORMAL);

        graphic.drawLine(new Vector(SIZE2, -SIZE - SIZE2), new Vector(SIZE + SIZE2, -SIZE * 2 - SIZE2), Style.THIN);

        graphic.drawLine(new Vector(SIZE2, -SIZE * 2), new Vector(0, -SIZE * 2), Style.NORMAL);
        graphic.drawLine(new Vector(SIZE + SIZE2, -SIZE * 2), new Vector(SIZE * 2, -SIZE * 2), Style.NORMAL);

        if (label != null && label.length() > 0)
            graphic.drawText(new Vector(SIZE, 4), new Vector(SIZE * 2, 4), label, Orientation.CENTERTOP, Style.SHAPE_PIN);
    }

}