package com.mysite.core.services;

import java.util.Map;

import com.day.cq.dam.api.Asset;

public interface ReferencedAssetService {
	Map<String, Asset> getReferencedAsset(String pagePath);
}
