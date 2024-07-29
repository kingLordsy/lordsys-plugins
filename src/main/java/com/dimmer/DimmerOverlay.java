package com.dimmer;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

import javax.inject.Inject;
import java.awt.*;

public class DimmerOverlay extends Overlay {

    public final DimmerConfig config;
    public final DimmerPlugin plugin;
    public final Client client;

    @Inject
    public DimmerOverlay(DimmerPlugin plugin, DimmerConfig config, Client client) {

        super(plugin);

        this.plugin = plugin;
        this.config = config;
        this.client = client;

        setPosition(OverlayPosition.DYNAMIC);
        setPriority(OverlayPriority.LOW);
        setLayer(OverlayLayer.ABOVE_SCENE);

    }

    public Dimension render(Graphics2D graphics) {

        if (plugin.dimmerEnabled) {

            graphics.setColor(new Color(0, 0, 0, config.dimmerStrength()));
            graphics.fill(client.getCanvas().getBounds());

        }

        return null;
    }

}
