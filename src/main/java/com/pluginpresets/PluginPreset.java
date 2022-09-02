/*
 * Copyright (c) 2022, antero111 <https://github.com/antero111>
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
package com.pluginpresets;

import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.runelite.client.config.Keybind;

/**
 * Class depicting single PluginPreset file. 
 *
 * @param id            Time of creation used as id
 * @param name          Name of the preset
 * @param keybind       Used to enable the preset without the side panel. (Optional)
 * @param local         Used to identify whether the preset is stored in /presets or settings.properties
 * @param pluginConfigs List of saved plugin configurations.
 */
public class PluginPreset
{
	@Getter
	@Setter
	private long id;

	@Getter
	@Setter
	private String name;

	@Getter
	@Setter
	private Keybind keybind;

	@Getter
	@Setter
	private Boolean local;

	@Getter
	@Setter
	private List<PluginConfig> pluginConfigs;

	public PluginPreset(String name, List<PluginConfig> pluginConfigs)
	{
		this.id = Instant.now().toEpochMilli();
		this.name = name;
		this.keybind = null;
		this.local = true;
		this.pluginConfigs = pluginConfigs;
	}
}
