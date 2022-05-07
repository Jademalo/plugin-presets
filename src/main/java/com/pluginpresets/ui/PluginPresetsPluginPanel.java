/*
 * Copyright (c) 2021, antero111 <https://github.com/antero111>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.pluginpresets.ui;

import com.pluginpresets.PluginConfig;
import com.pluginpresets.PluginPreset;
import com.pluginpresets.PluginPresetsPlugin;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.IconTextField;
import net.runelite.client.ui.components.PluginErrorPanel;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.LinkBrowser;

public class PluginPresetsPluginPanel extends PluginPanel
{
	private static final ImageIcon NOTIFICATION_ICON;
	private static final ImageIcon NOTIFICATION_HOVER_ICON;
	private static final ImageIcon HELP_ICON;
	private static final ImageIcon HELP_HOVER_ICON;
	private static final ImageIcon REFRESH_ICON;
	private static final ImageIcon REFRESH_HOVER_ICON;
	private static final ImageIcon ADD_ICON;
	private static final ImageIcon ADD_HOVER_ICON;
	private static final ImageIcon ARROW_LEFT_ICON;
	private static final ImageIcon ARROW_LEFT_HOVER_ICON;
	private static final ImageIcon SWITCH_ON_ICON;
	private static final ImageIcon SWITCH_OFF_ICON;

	static
	{
		final BufferedImage notificationImg = ImageUtil.loadImageResource(PluginPresetsPlugin.class,
			"warning_icon.png");
		NOTIFICATION_ICON = new ImageIcon(notificationImg);
		NOTIFICATION_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(notificationImg, 0.53f));

		final BufferedImage helpImg = ImageUtil.loadImageResource(PluginPresetsPlugin.class, "help_icon.png");
		HELP_ICON = new ImageIcon(helpImg);
		HELP_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(helpImg, 0.53f));

		final BufferedImage refreshImg = ImageUtil.loadImageResource(PluginPresetsPlugin.class, "refresh_icon.png");
		REFRESH_ICON = new ImageIcon(refreshImg);
		REFRESH_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(refreshImg, 0.53f));

		final BufferedImage addImg = ImageUtil.loadImageResource(PluginPresetsPlugin.class, "add_icon.png");
		ADD_ICON = new ImageIcon(addImg);
		ADD_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(addImg, 0.53f));

		final BufferedImage arrowLeftImg = ImageUtil.loadImageResource(PluginPresetsPlugin.class, "arrow_left_icon.png");
		ARROW_LEFT_ICON = new ImageIcon(arrowLeftImg);
		ARROW_LEFT_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(arrowLeftImg, 0.53f));

		final BufferedImage switchOnImg = ImageUtil.loadImageResource(PluginPresetsPlugin.class, "switch_on_icon.png");
		SWITCH_ON_ICON = new ImageIcon(switchOnImg);

		final BufferedImage switchOffImg = ImageUtil.loadImageResource(PluginPresetsPlugin.class, "switch_off_icon.png");
		SWITCH_OFF_ICON = new ImageIcon(switchOffImg);
	}

	private final PluginPresetsPlugin plugin;
	private final JLabel errorNotification = new JLabel(NOTIFICATION_ICON);
	private final JLabel helpButton = new JLabel(HELP_ICON);
	private final JLabel refreshPlugins = new JLabel(REFRESH_ICON);
	private final JLabel addPreset = new JLabel(ADD_ICON);
	private final JLabel stopEdit = new JLabel(ARROW_LEFT_ICON);
	private final JLabel toggleAll = new JLabel();
	private final JLabel updateAll = new JLabel(REFRESH_ICON);
	private final JLabel title = new JLabel();
	private final JLabel editTitle = new JLabel();
	private final IconTextField searchBar = new IconTextField();
	private final PluginErrorPanel noPresetsPanel = new PluginErrorPanel();
	private final JPanel titlePanel = new JPanel(new BorderLayout());
	private final JPanel editPanel = new JPanel(new BorderLayout());
	private final JPanel contentView = new JPanel(new GridBagLayout());
	private final GridBagConstraints constraints = new GridBagConstraints();
	private boolean allOn = true;

	public PluginPresetsPluginPanel(PluginPresetsPlugin pluginPresetsPlugin)
	{
		super(false);

		this.plugin = pluginPresetsPlugin;

		setLayout(new BorderLayout());

		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.setBorder(new EmptyBorder(10, 10, 10, 5));

		title.setText("Plugin Presets");
		title.setForeground(Color.WHITE);
		title.setBorder(new EmptyBorder(0, 0, 0, 40));

		JPanel presetActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 3));

		errorNotification.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent mouseEvent)
			{
				errorNotification.setVisible(false);
			}

			@Override
			public void mouseEntered(MouseEvent mouseEvent)
			{
				errorNotification.setIcon(NOTIFICATION_HOVER_ICON);
			}

			@Override
			public void mouseExited(MouseEvent mouseEvent)
			{
				errorNotification.setIcon(NOTIFICATION_ICON);
			}
		});

		helpButton.setToolTipText("Need help?");
		helpButton.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent mouseEvent)
			{
				LinkBrowser.browse(PluginPresetsPlugin.HELP_LINK);
			}

			@Override
			public void mouseEntered(MouseEvent mouseEvent)
			{
				helpButton.setIcon(HELP_HOVER_ICON);
			}

			@Override
			public void mouseExited(MouseEvent mouseEvent)
			{
				helpButton.setIcon(HELP_ICON);
			}
		});

		refreshPlugins.setToolTipText("Refresh presets");
		refreshPlugins.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent mouseEvent)
			{
				plugin.refreshPresets();
			}

			@Override
			public void mouseEntered(MouseEvent mouseEvent)
			{
				refreshPlugins.setIcon(REFRESH_HOVER_ICON);
			}

			@Override
			public void mouseExited(MouseEvent mouseEvent)
			{
				refreshPlugins.setIcon(REFRESH_ICON);
			}
		});

		addPreset.setToolTipText("Create new plugin preset");
		addPreset.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent mouseEvent)
			{
				if (mouseEvent.getButton() == MouseEvent.BUTTON3) // Right click
				{
					JPopupMenu importPopupMenu = getImportMenuPopup();
					addPreset.setComponentPopupMenu(importPopupMenu);
				}
				else
				{
					promptPresetCreation(false);
				}
			}

			@Override
			public void mouseEntered(MouseEvent mouseEvent)
			{
				addPreset.setIcon(ADD_HOVER_ICON);
			}

			@Override
			public void mouseExited(MouseEvent mouseEvent)
			{
				addPreset.setIcon(ADD_ICON);
			}
		});

		presetActions.add(errorNotification);
		presetActions.add(helpButton);
		presetActions.add(refreshPlugins);
		presetActions.add(addPreset);

		titlePanel.add(title, BorderLayout.WEST);
		titlePanel.add(presetActions, BorderLayout.EAST);

		editTitle.setForeground(Color.WHITE);

		stopEdit.setToolTipText("Back to presets");
		stopEdit.setBorder(new EmptyBorder(0, 0, 0, 10));
		stopEdit.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent mouseEvent)
			{
				plugin.setEditedPreset(null);
				emptyBar();
				rebuild();
			}

			@Override
			public void mouseEntered(MouseEvent mouseEvent)
			{
				stopEdit.setIcon(ARROW_LEFT_HOVER_ICON);
			}

			@Override
			public void mouseExited(MouseEvent mouseEvent)
			{
				stopEdit.setIcon(ARROW_LEFT_ICON);
			}
		});

		searchBar.setIcon(IconTextField.Icon.SEARCH);
		searchBar.setPreferredSize(new Dimension(PluginPanel.PANEL_WIDTH - 20, 30));
		searchBar.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		searchBar.setHoverBackgroundColor(ColorScheme.DARK_GRAY_HOVER_COLOR);
		searchBar.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void insertUpdate(DocumentEvent e)
			{
				rebuild();
			}

			@Override
			public void removeUpdate(DocumentEvent e)
			{
				rebuild();

			}

			@Override
			public void changedUpdate(DocumentEvent e)
			{
				rebuild();
			}
		});

		JPanel searchWrapper = new JPanel(new BorderLayout());
		searchWrapper.add(searchBar, BorderLayout.CENTER);
		searchWrapper.setBorder(new EmptyBorder(10, 0, 3, 0));

		toggleAll.setPreferredSize(new Dimension(19, 15));
		toggleAll.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent mouseEvent)
			{
				plugin.toggleAll(allOn);
			}
		});

		// TODO: update all icon
		updateAll.setToolTipText("Update all modified configurations with your current settings.");
		updateAll.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent mouseEvent)
			{
				plugin.updateAllModified();
			}

			@Override
			public void mouseEntered(MouseEvent mouseEvent)
			{
				updateAll.setIcon(REFRESH_HOVER_ICON);
			}

			@Override
			public void mouseExited(MouseEvent mouseEvent)
			{
				updateAll.setIcon(REFRESH_ICON);
			}
		});

		JPanel editActions = new JPanel();
		editActions.setLayout(new FlowLayout(FlowLayout.RIGHT, 8, 0));

		editActions.add(updateAll);
		editActions.add(toggleAll);

		editPanel.add(stopEdit, BorderLayout.WEST);
		editPanel.add(editTitle, BorderLayout.CENTER);
		editPanel.add(editActions, BorderLayout.EAST);
		editPanel.add(searchWrapper, BorderLayout.SOUTH);
		editPanel.setVisible(false);

		northPanel.add(titlePanel, BorderLayout.NORTH);
		northPanel.add(editPanel, BorderLayout.CENTER);

		contentView.setBorder(new EmptyBorder(0, 7, 0, 7));

		JPanel contentWrapper = new JPanel(new BorderLayout());
		contentWrapper.add(contentView, BorderLayout.NORTH);

		JScrollPane scrollableContainer = new JScrollPane(contentWrapper);
		scrollableContainer.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		noPresetsPanel.setContent("Plugin Presets", "Presets of your plugin configurations.");
		noPresetsPanel.setVisible(false);

		add(northPanel, BorderLayout.NORTH);
		add(scrollableContainer, BorderLayout.CENTER);

	}

	public void rebuild()
	{
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1;
		constraints.gridx = 0;
		constraints.gridy = 0;

		contentView.removeAll();

		boolean editingPreset = plugin.getEditedPreset() != null;

		titlePanel.setVisible(!editingPreset);
		editPanel.setVisible(editingPreset);

		if (editingPreset)
		{
			renderEditView();
		}
		else
		{
			renderPresetView();
		}

		errorNotification.setVisible(false);

		repaint();
		revalidate();
	}

	private void renderPresetView()
	{
		for (final PluginPreset preset : plugin.getPluginPresets())
		{
			contentView.add(new PresetPanel(preset, plugin), constraints);
			constraints.gridy++;

			contentView.add(Box.createRigidArea(new Dimension(0, 10)), constraints);
			constraints.gridy++;
		}

		boolean empty = constraints.gridy == 0;
		noPresetsPanel.setVisible(empty);
		title.setVisible(!empty);

		contentView.add(noPresetsPanel, constraints);
		constraints.gridy++;
	}

	private void renderEditView()
	{
		PluginPreset preset = plugin.getEditedPreset();
		List<PluginConfig> presetConfigs = preset.getPluginConfigs();

		editTitle.setText("Editing Preset " + preset.getName());

		searchBar.requestFocusInWindow();

		List<PluginConfig> configurations = plugin.getPresetManager().getCurrentConfigurations();

		// Add configurations that are in the preset but not in current configurations
		// e.g. preset is from a friend and the preset has settings
		// to some plugin hub plugin that you don't have in your current configs
		List<String> names = configurations.stream().map(PluginConfig::getName).collect(Collectors.toList());
		for (PluginConfig config : presetConfigs)
		{
			if (!names.contains(config.getName()))
			{
				configurations.add(config);
			}
		}

		configurations = filterIfSearchKeyword(configurations);
		sortAlphabetically(configurations);

		allOn = true;
		boolean modified = false;

		for (final PluginConfig currentConfig : configurations)
		{
			PluginConfig presetConfig = getPresetConfig(presetConfigs, currentConfig);

			if (allOn && presetConfig == null)
			{
				allOn = false;
			}

			if (presetConfig != null && !presetConfig.equals(currentConfig))
			{
				modified = true;
			}

			contentView.add(new ConfigPanel(currentConfig, presetConfig, plugin), constraints);
			constraints.gridy++;
		}

		if (allOn)
		{
			toggleAll.setToolTipText("Click to remove all configurations from this preset");
			toggleAll.setIcon(SWITCH_ON_ICON);
		}
		else
		{
			toggleAll.setToolTipText("Click to add all your configurations to this preset");
			toggleAll.setIcon(SWITCH_OFF_ICON);
		}

		updateAll.setVisible(modified);
	}

	private List<PluginConfig> filterIfSearchKeyword(List<PluginConfig> currentConfigurations)
	{
		final String text = searchBar.getText();
		if (!text.isEmpty())
		{
			currentConfigurations = currentConfigurations.stream()
				.filter(
					c -> c.getName().toLowerCase()
						.contains(text.toLowerCase()))
				.collect(Collectors.toList());
		}
		return currentConfigurations;
	}

	private PluginConfig getPresetConfig(List<PluginConfig> presetConfigs, final PluginConfig currentConfig)
	{
		PluginConfig presetConfig = null;
		for (PluginConfig config : presetConfigs)
		{
			if (config.getName().equals(currentConfig.getName()))
			{
				presetConfig = config;
				break;
			}
		}
		return presetConfig;
	}

	private void sortAlphabetically(List<PluginConfig> currentConfigurations)
	{
		// // Sort alphabetically similar to the configurations tab
		currentConfigurations.sort(Comparator.comparing(PluginConfig::getConfigName));
	}

	private JPopupMenu getImportMenuPopup()
	{
		JMenuItem importMenuOption = new JMenuItem();
		importMenuOption.setText("Import preset from clipboard");
		importMenuOption.addActionListener(e -> plugin.importPresetFromClipboard());

		JMenuItem importMenuOption2 = new JMenuItem();
		importMenuOption2.setText("Create new empty preset");
		importMenuOption2.addActionListener(e -> promptPresetCreation(true));

		JPopupMenu importPopupMenu = new JPopupMenu();
		importPopupMenu.setBorder(new EmptyBorder(2, 2, 2, 0));
		importPopupMenu.add(importMenuOption);
		importPopupMenu.add(importMenuOption2);
		return importPopupMenu;
	}

	private void promptPresetCreation(boolean empty)
	{
		String customPresetName = JOptionPane.showInputDialog(PluginPresetsPluginPanel.this,
			"Name your new preset.", "New Plugin Preset", JOptionPane.PLAIN_MESSAGE);
		if (customPresetName != null)
		{
			plugin.createPreset(customPresetName, empty);
		}
	}

	public void renderNotification(String errorMessage)
	{
		errorNotification.setToolTipText(errorMessage);
		errorNotification.setVisible(true);
	}

	private void emptyBar()
	{
		searchBar.setText("");
	}
}
