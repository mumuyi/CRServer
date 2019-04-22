package cn.nuaa.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MethodDeclarationEntity implements Serializable{
	private static final long serialVersionUID = 1380260537662328834L;
	private String methodName;
	private String methodRetureType;
	private List<String> methodParameters = new ArrayList<String>();

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getMethodRetureType() {
		return methodRetureType;
	}

	public void setMethodRetureType(String methodRetureType) {
		this.methodRetureType = methodRetureType;
	}

	public List<String> getMethodParameters() {
		return methodParameters;
	}

	public void setMethodParameters(List<String> methodParameters) {
		this.methodParameters = methodParameters;
	}
}
