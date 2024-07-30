package com.dimmer;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.util.HashSet;

@PluginDescriptor(
        name = "Dimmer",
        description = "Dim the client based on current region.",
        tags = {"dimmer", "dark", "dark mode"}
)
@Slf4j
public class DimmerPlugin extends Plugin {

    @Inject
    public Client client;

    @Inject
    public DimmerConfig config;

    @Inject
    public DimmerOverlay overlay;

    @Inject
    public OverlayManager overlayManager;

    @Provides
    DimmerConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(DimmerConfig.class);
    }

    public HashSet<Integer> regions;

    public boolean dimmerEnabled;

    public int clickedRegionId;

    @Override
    public void startUp() {

        regions = new HashSet<Integer>();

        fillRegions();
        evaluateEnabled();

        overlayManager.add(overlay);

    }

    @Override
    public void shutDown() {

        overlayManager.remove(overlay);
        regions = null;

    }

    public String regionsToString() {

        if (regions == null || regions.size() == 0) return "";

//        if (regions.size() == 1) return regions.get(0).toString();

        StringBuilder sb = new StringBuilder();

        for (Integer i : regions) {

            if (sb.length() > 0) {

                sb.append(',');

            }

            sb.append(i.toString());

        }

        return sb.toString();

    }

    public void fillRegions() {

        if (regions == null) return;

        try {

            String value = config.getDimmerRegions();

            if (value.length() == 0) return;

            if (!value.contains(",")) {

                regions.add(Integer.valueOf(value));
                return;

            }

            String[] split = value.split(",");

            for (String s : split) {

                regions.add(Integer.valueOf(s));

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    @Subscribe
    public void onConfigChanged(ConfigChanged configChanged) {

        if (!configChanged.getGroup().equals(DimmerConfig.GROUP)) return;

        fillRegions();
        evaluateEnabled();

    }

    @Subscribe
    public void onGameTick(GameTick event) {

        evaluateEnabled();

    }

    public void evaluateEnabled() {

        if (config.dimmerStrength() <= 0) {

            dimmerEnabled = false;
            return;

        }

        if (config.dimmerFilter()) {

            Player player = client.getLocalPlayer();

            if (player == null) {

                dimmerEnabled = !config.dimmerWhitelist();
                return;

            }

            WorldPoint wp = player.getWorldLocation();

            if (wp == null) {

                dimmerEnabled = !config.dimmerWhitelist();
                return;

            }

            boolean isInRegion = regions.contains(wp.getRegionID());

            if (config.dimmerWhitelist()) {

                dimmerEnabled = isInRegion;

            } else {

                dimmerEnabled = !isInRegion;

            }


        } else {

            dimmerEnabled = true;
            return;

        }

    }

    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded event) {

        if (regions == null || event.getType() != MenuAction.WALK.getId() || !client.isKeyPressed(KeyCode.KC_SHIFT)) return;

        int id = client.getSelectedSceneTile().getWorldLocation().getRegionID();

        if (regions.contains(id)) {

            client.createMenuEntry(-1).setOption("Dimmer").setTarget("Remove Region").setType(MenuAction.RUNELITE).onClick(this::removeRegion);

        } else {

            client.createMenuEntry(-1).setOption("Dimmer").setTarget("Add Region").setType(MenuAction.RUNELITE).onClick(this::addRegion);

        }

        clickedRegionId = id;

    }

    public void removeRegion(MenuEntry menuEntry) {

        if (regions == null) return;

        config.setDimmerRegions(regionsToString());

    }

    public void addRegion(MenuEntry menuEntry) {

        if (regions == null) return;

        config.setDimmerRegions(regionsToString());

    }

}
