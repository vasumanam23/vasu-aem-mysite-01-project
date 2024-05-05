package com.mysite.core.services;

import java.util.Map;


import com.day.cq.dam.api.Asset;

public interface ModifiedReferencedAssetService {
	Map<String, Asset> getModifiedReferencedAsset(String pagePath);
}
