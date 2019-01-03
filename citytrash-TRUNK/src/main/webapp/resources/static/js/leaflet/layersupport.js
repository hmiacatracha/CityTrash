////////////////////////////////////////////////
// Quick and dirty implementation of enableMCG
////////////////////////////////////////////////
L.Control.TagFilterButton.include({
	// Goal: read from MCG instead of from _map
	enableMCG : function(mcgInstance) {
		this.registerCustomSource({
			name : 'mcg',
			source : {
				mcg : mcgInstance,
				hide : function(layerSource) {
					var releatedLayers = [];

					for (
						var r = 0; r < this._releatedFilterButtons.length; r++
					) {
						releatedLayers = releatedLayers.concat(
							this._releatedFilterButtons[r].getInvisibles()
						);
					}

					var toBeRemovedFromInvisibles = [],
						i,
						toAdd = [];

					for (var i = 0; i < this._invisibles.length; i++) {
						if (releatedLayers.indexOf(this._invisibles[i]) == -1) {
							for (
								var j = 0; j < this._invisibles[i].options.tags.length; j++
							) {
								if (
									this._selectedTags.length == 0 ||
									this._selectedTags.indexOf(
										this._invisibles[i].options.tags[j]
									) !== -1
								) {
									//this._map.addLayer(this._invisibles[i]);
									toAdd.push(this._invisibles[i]);
									toBeRemovedFromInvisibles.push(i);
									break;
								}
							}
						}
					}

					// Batch add into MCG
					layerSource.mcg.addLayers(toAdd);

					while (toBeRemovedFromInvisibles.length > 0) {
						this._invisibles.splice(
							toBeRemovedFromInvisibles.pop(),
							1
						);
					}

					var removedMarkers = [];
					var totalCount = 0;

					if (this._selectedTags.length > 0) {
						//this._map.eachLayer(
						layerSource.mcg.eachLayer(
							function(layer) {
								if (
									layer &&
									layer.options &&
									layer.options.tags
								) {
									totalCount++;
									if (releatedLayers.indexOf(layer) == -1) {
										var found = false;
										for (
											var i = 0; i < layer.options.tags.length; i++
										) {
											found = this._selectedTags.indexOf(
													layer.options.tags[i]
												) !== -1;
											if (found) {
												break;
											}
										}
										if (!found) {
											removedMarkers.push(layer);
										}
									}
								}
							}.bind(this)
						);

						for (i = 0; i < removedMarkers.length; i++) {
							//this._map.removeLayer(removedMarkers[i]);
							this._invisibles.push(removedMarkers[i]);
						}

						// Batch remove from MCG
						layerSource.mcg.removeLayers(removedMarkers);
					}

					return totalCount - removedMarkers.length;
				},
			},
		});

		this.layerSources.currentSource = this.layerSources.sources[
			'mcg'
		];
	},
});

////////////////////////////////////////////////
// Fix for TagFilterButton
////////////////////////////////////////////////
L.Control.TagFilterButton.include({
	_prepareLayerSources : function() {
		this.layerSources = new Object();
		this.layerSources['sources'] = new Object();

		this.registerCustomSource({
			name : 'default',
			source : {
				hide : function() {
					var releatedLayers = [];

					for (var r = 0; r < this._releatedFilterButtons.length; r++) {
						releatedLayers = releatedLayers.concat(
							this._releatedFilterButtons[r].getInvisibles()
						);
					}

					var toBeRemovedFromInvisibles = [],
						i;

					// "Fix": add var
					for (var i = 0; i < this._invisibles.length; i++) {
						if (releatedLayers.indexOf(this._invisibles[i]) == -1) {
							// "Fix": add var
							for (var j = 0; j < this._invisibles[i].options.tags.length; j++) {
								if (
									this._selectedTags.length == 0 ||
									this._selectedTags.indexOf(
										this._invisibles[i].options.tags[j]
									) !== -1
								) {
									this._map.addLayer(this._invisibles[i]);
									toBeRemovedFromInvisibles.push(i);
									break;
								}
							}
						}
					}

					while (toBeRemovedFromInvisibles.length > 0) {
						this._invisibles.splice(toBeRemovedFromInvisibles.pop(), 1);
					}

					var removedMarkers = [];
					var totalCount = 0;

					if (this._selectedTags.length > 0) {
						this._map.eachLayer(
							function(layer) {
								if (layer && layer.options && layer.options.tags) {
									totalCount++;
									if (releatedLayers.indexOf(layer) == -1) {
										var found = false;
										for (var i = 0; i < layer.options.tags.length; i++) {
											found = this._selectedTags.indexOf(layer.options.tags[i]) !==
												-1;
											if (found) {
												break;
											}
										}
										if (!found) {
											removedMarkers.push(layer);
										}
									}
								}
							}.bind(this)
						);

						for (i = 0; i < removedMarkers.length; i++) {
							this._map.removeLayer(removedMarkers[i]);
							this._invisibles.push(removedMarkers[i]);
						}
					}

					return totalCount - removedMarkers.length;
				},
			},
		});
		this.layerSources.currentSource = this.layerSources.sources['default'];
	},
});