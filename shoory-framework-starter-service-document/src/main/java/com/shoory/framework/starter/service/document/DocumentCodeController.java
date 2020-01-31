package com.shoory.framework.starter.service.document;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import com.shoory.framework.starter.api.ApiInfo;
import com.shoory.framework.starter.service.I18nComponent;
import com.shoory.framework.starter.service.document.models.MethodInfos;
import com.shoory.framework.starter.service.document.models.ModelInfos;
import com.shoory.framework.starter.service.document.models.ModuleInfos;
import com.shoory.framework.starter.service.document.models.ServiceInfos;
import com.shoory.framework.starter.service.document.utils.DocumentUtils;
import com.shoory.framework.starter.service.document.utils.ServiceUtils;
import com.shoory.framework.starter.utils.PojoUtils;

@RestController
@CrossOrigin
public class DocumentCodeController {
	@Autowired
	private PojoUtils pojoUtils;
	@Autowired
	private DocumentUtils documentUtils;
	@Autowired
	private I18nComponent i18nComponent;
	private Map<String, String> mapDict =  new HashMap<String, String>();
	private ThreadLocal<Integer> lineNumber = new ThreadLocal<Integer>();

	@GetMapping(value = "/doc/code/{lang}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String codeLang(@PathVariable("lang") String lang, HttpServletRequest request,
			HttpServletResponse response) {
		documentUtils.ready();
		
		return Optional.ofNullable(this.mapDict.get(lang))
				.orElseGet(() -> {
					StringBuilder sb = new StringBuilder();
					
					lineNumber.set(0);
					documentUtils.getMapCode().keySet()
						.stream()
						.sorted()
						.forEach(code -> {
							int line = lineNumber.get();
							if (line % 9 == 0) {
								sb.append(String.format("#%s Line %d", lang, line + 1));
							}
							sb.append(String.format("%s = %s\r\n", code, i18nComponent.getMessage(code, lang)));
							lineNumber.set(lineNumber.get() + 1);
						});
					this.mapDict.put(lang, sb.toString());
					
					return this.mapDict.get(lang);
			});
	}

	@GetMapping(value = "/doc/code", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String code(HttpServletRequest request, HttpServletResponse response) {
		documentUtils.ready();
		return pojoUtils.toJson(documentUtils.getMapCode().keySet());
	}
}