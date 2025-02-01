package com.ZmiTracker;

import net.runelite.client.ui.PluginPanel;
import net.runelite.client.game.ItemManager;
import net.runelite.api.ItemID;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class ZmiTrackerPanel extends PluginPanel {
    private final ItemManager itemManager;
    private final JLabel totalRunesLabel;
    private final Map<Integer, JLabel> runeLabels;

    public ZmiTrackerPanel(ItemManager itemManager) {
        this.itemManager = itemManager;
        this.runeLabels = new HashMap<>();

        setLayout(new GridLayout(0, 1));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        totalRunesLabel = new JLabel("Total Runes: 0");
        add(totalRunesLabel);

        // Initialize labels for each rune type
        int[] runeIds = {
                ItemID.AIR_RUNE, ItemID.WATER_RUNE, ItemID.EARTH_RUNE, ItemID.FIRE_RUNE,
                ItemID.MIND_RUNE, ItemID.CHAOS_RUNE, ItemID.DEATH_RUNE, ItemID.BLOOD_RUNE,
                ItemID.COSMIC_RUNE, ItemID.NATURE_RUNE, ItemID.LAW_RUNE, ItemID.BODY_RUNE,
                ItemID.SOUL_RUNE, ItemID.ASTRAL_RUNE,
        };

        for (int runeId : runeIds) {
            BufferedImage icon = itemManager.getImage(runeId);
            JLabel label = new JLabel(new ImageIcon(icon));
            label.setText("0");
            label.setHorizontalTextPosition(SwingConstants.LEFT);
            runeLabels.put(runeId, label);
            add(label);

        }
    }
    public void updateRuneCount(int runeId, int count) {
        JLabel label = runeLabels.get(runeId);
        if (label != null) {
            label.setText(String.valueOf(count));
        }
    }
    public void updateTotalRunes(int total) {
        totalRunesLabel.setText("Total Runes: " + total);
    }
}

