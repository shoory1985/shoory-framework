package com.shoory.framework.starter.api.request;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shoory.framework.starter.api.DataFrameReader;
import com.shoory.framework.starter.api.annotation.ApiHidden;
import com.shoory.framework.starter.api.annotation.ApiName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class DataFrameBaseRequest extends UserBaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public abstract void fromDataFrame(byte[] dataFrame);
	
	protected void fromDataFrame(byte[] dataFrame, DataFrameReader reader) {
		DataInputStream dis = null;
		try {
			dis = new DataInputStream(new ByteArrayInputStream(dataFrame));
			reader.read(dis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (dis != null) {
				try {
					dis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dis = null;
			}
		}
	}

}
