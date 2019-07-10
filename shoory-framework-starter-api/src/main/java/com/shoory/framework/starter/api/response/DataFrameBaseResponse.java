package com.shoory.framework.starter.api.response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.shoory.framework.starter.api.DataFrameReader;
import com.shoory.framework.starter.api.DataFrameWriter;
import com.shoory.framework.starter.api.annotation.ApiDescription;
import com.shoory.framework.starter.api.annotation.ApiHidden;
import com.shoory.framework.starter.api.annotation.ApiName;
import com.shoory.framework.starter.api.annotation.ApiRequired;
import com.shoory.framework.starter.api.constants.BizException;
import com.shoory.framework.starter.api.constants.Resultable;
import com.shoory.framework.starter.api.constants.SysException;
import com.shoory.framework.starter.api.request.DataFrameBaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class DataFrameBaseResponse extends BaseResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public abstract byte[] toDataFrame();

	protected byte[] toDataFrame(DataFrameWriter writer) {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = null;
		try {
			dos = new DataOutputStream(baos);
			writer.write(dos);
			
			return baos.toByteArray();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				dos.close();
				baos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
