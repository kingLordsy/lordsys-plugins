package com.dimmer;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup(DimmerConfig.GROUP)
public interface DimmerConfig extends Config {

    String GROUP = "dimmer";

    @ConfigItem(
            position = 1,
            keyName = "dimmerStrength",
            name = "Dimmer Strength",
            description = "Sets the strength of the dimmer."
    )
    @Range(
            min = 0,
            max = 255
    )
    default int dimmerStrength()
    {
        return 100;
    }

    @ConfigItem(
            position = 2,
            keyName = "dimmerFilter",
            name = "Dimmer Filter",
            description = "If enabled, the dimmer will filter its on/off behavior based on the configured list."
    )
    default boolean dimmerFilter()
    {
        return false;
    }

    @ConfigItem(
            position = 3,
            keyName = "dimmerWhitelistMode",
            name = "Dimmer Whitelist",
            description = "If enabled, the dimmer will enable based on if the current region is in the configured list. If disabled, the opposite."
    )
    default boolean dimmerWhitelist()
    {
        return true;
    }

    @ConfigItem(
            position = 4,
            keyName = "dimmerRegions",
            name = "Regions to enable/disable dimmer",
            description = "List of Region IDs to enable/disable dimmer in, if whitelist/blacklist is enabled. Format: (Region ID),(Region ID)"
    )
    default String getDimmerRegions()
    {
        return "";
    }

    @ConfigItem(
            keyName = "dimmerRegions",
            name = "",
            description = ""
    )
    void setDimmerRegions(String dimmerRegions);

}
