package com.quezx.analytics.model;

import com.quezx.analytics.model.innerModel.UserDashBoardData;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDashBoard implements Serializable {

private List<UserDashBoardData> data = null;
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

public List<UserDashBoardData> getData() {
return data;
}

public void setData(List<UserDashBoardData> data) {
this.data = data;
}

public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

}